package com.acooly.module.point.dao.impl;

import com.acooly.module.point.dao.PointStatisticsExtDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service("pointStatisticsExtDao")
public class PointStatisticsExtDaoImpl implements PointStatisticsExtDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void pointStatistics(String startTime, String endTime) {
        String sql =
                "insert into point_statistics(user_name,num,point,start_time,end_time,status,create_time,update_time)" //
                        + "(select user_name,count(id) as num," //
                        + "sum(case trade_type when 'produce' then amount else 0 end)-(case trade_type when 'expense' then amount else 0 end) as point," //
                        + "'"
                        + startTime
                        + "','"
                        + endTime
                        + "','init',now(),now() from point_trade " //
                        + "where create_time<=('"
                        + endTime
                        + "') and create_time>=('"
                        + startTime
                        + "') " //
                        + "and trade_type in('produce','expense') group by user_name)";
        jdbcTemplate.execute(sql);
    }
}
