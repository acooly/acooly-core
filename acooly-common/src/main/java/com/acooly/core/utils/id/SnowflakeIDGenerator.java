package com.acooly.core.utils.id;

import java.security.SecureRandom;
import java.util.Date;

public class SnowflakeIDGenerator
        implements IdentifierGenerator<Long>, IdentifierAnalyzer<Long, SnowflakeIDElement> {
    private static final long regionIdBits = 3L;
    private static final long workerIdBits = 10L;
    private static final long sequenceBits = 10L;
    private static final long maxRegionId = 7L;
    private static final long maxWorkerId = 1023L;
    private static final long sequenceMask = 1023L;
    private static final long workerIdShift = 10L;
    private static final long regionIdShift = 20L;
    private static final long timestampLeftShift = 23L;
    private static long lastTimestamp = -1L;
    private final Long workerId;
    private final Long regionId;
    private long twepoch = 1288834974657L;
    private long sequence = 0L;

    public SnowflakeIDGenerator(long workerId, long regionId) {
        if ((workerId > 1023L) || (workerId < 0L)) {
            throw new IllegalArgumentException(
                    String.format(
                            "worker Id can't be greater than %d or less than 0",
                            new Object[]{Long.valueOf(1023L)}));
        }
        if ((regionId > 7L) || (regionId < 0L)) {
            throw new IllegalArgumentException(
                    String.format(
                            "region Id can't be greater than %d or less than 0",
                            new Object[]{Long.valueOf(7L)}));
        }
        this.workerId = Long.valueOf(workerId);
        this.regionId = Long.valueOf(regionId);
    }

    public SnowflakeIDGenerator(long workerId) {
        if ((workerId > 1023L) || (workerId < 0L)) {
            throw new IllegalArgumentException(
                    String.format(
                            "worker Id can't be greater than %d or less than 0",
                            new Object[]{Long.valueOf(1023L)}));
        }
        this.workerId = Long.valueOf(workerId);
        this.regionId = Long.valueOf(0L);
    }

    public Long generate() {
        return Long.valueOf(nextId(false, 0L));
    }

    private synchronized long nextId(boolean isPadding, long busId) {
        long timestamp = timeGen();
        long paddingnum = this.regionId.longValue();
        if (isPadding) {
            paddingnum = busId;
        }
        if (timestamp < lastTimestamp) {
            try {
                throw new Exception(
                        "Clock moved backwards.  Refusing to generate id for "
                                + (lastTimestamp - timestamp)
                                + " milliseconds");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (lastTimestamp == timestamp) {
            this.sequence = (this.sequence + 1L & 0x3FF);
            if (this.sequence == 0L) {
                timestamp = tailNextMillis(lastTimestamp);
            }
        } else {
            this.sequence = new SecureRandom().nextInt(10);
        }
        lastTimestamp = timestamp;
        return timestamp - this.twepoch << 23
                | paddingnum << 20
                | this.workerId.longValue() << 10
                | this.sequence;
    }

    private long tailNextMillis(long lastTimestamp) {
        long timestamp = timeGen();
        while (timestamp <= lastTimestamp) {
            timestamp = timeGen();
        }
        return timestamp;
    }

    protected long timeGen() {
        return System.currentTimeMillis();
    }

    public Long getWorkerId() {
        return this.workerId;
    }

    public Long getRegionId() {
        return this.regionId;
    }

    public SnowflakeIDElement analyze(Long id) {
        if (id == null) {
            return null;
        }
        SnowflakeIDElement element = new SnowflakeIDElement(id);

        element.setDate(new Date((id.longValue() >> 23) + this.twepoch));

        element.setRegionId((id.longValue() ^ id.longValue() >> 23 << 23) >> 20);

        element.setWorkerId(
                (id.longValue()
                        ^ id.longValue() >> 23 << 23
                        ^ (id.longValue() ^ id.longValue() >> 23 << 23) >> 20 << 20)
                        >> 10);
        return element;
    }
}
