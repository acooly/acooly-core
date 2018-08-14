package com.acooly.module.cache.limit;

import com.acooly.core.utils.Ids;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 参考  https://engineering.classdojo.com/blog/2015/02/06/rolling-rate-limiter/
 *
 * @author qiuboboy@qq.com
 * @date 2018-08-10 14:07
 */
public class RedisRateChecker implements RateChecker {
    @Autowired
    private RedisTemplate<String, String> redisOperations;

    protected static class Callback implements SessionCallback<Boolean> {

        private final String key;

        private final Long maxRequests;

        private final int interval;

        private final String requestId;

        public Callback(final String key, final Long maxRequests, final Integer interval) {
            this.key = key;
            this.maxRequests = maxRequests;
            this.interval = interval;
            this.requestId = Ids.gid();
        }


        @Override
        @SuppressWarnings("unchecked")
        public <K, V> Boolean execute(final RedisOperations<K, V> redisOperations) throws DataAccessException {
            return executeInternal((RedisOperations<String, String>) redisOperations);
        }

        private Boolean executeInternal(final RedisOperations<String, String> redisOperations) {
            redisOperations.multi();
            final long milliseconds = System.currentTimeMillis();
            final String callKey = requestId.concat("-").concat(Long.toString(milliseconds));

            // remove any older then one interval
            redisOperations.opsForZSet().removeRangeByScore(key, Double.MIN_VALUE, milliseconds - interval);

            // add current request (milliseconds should be sufficient, but add UUID)
            redisOperations.opsForZSet().add(key, callKey, milliseconds);

            // set expire for entire set, save memory
            redisOperations.expire(key, interval, TimeUnit.MILLISECONDS);

            // count total remaining
            redisOperations.opsForZSet().count(key, Double.MIN_VALUE, Double.MAX_VALUE);

            final List<Object> result = redisOperations.exec();

            // we made four calls during MULTI, expect that result size
            // last result hast be count and Long
            if (CollectionUtils.isEmpty(result) || result.size() != 4 || !(result.get(3) instanceof Long)) {
                return false;
            }
            final Long count = (Long) result.get(3);

            // more then we can handle, remove ours, call never happened
            // possible race condition here
            // another request might get blocked as well
            // this is better then blocking every other request if burst is too large
            if (count > maxRequests) {
                redisOperations.opsForZSet().remove(key, callKey);
                return false;
            }

            // limit has not been reached
            return true;
        }
    }

    @Override
    public boolean check(final String key, final Integer interval, final Long maxRequests) {
        final Boolean execute = redisOperations.execute(new Callback(key, maxRequests, interval));
        if (execute == null) {
            return false;
        }
        return execute;
    }

}