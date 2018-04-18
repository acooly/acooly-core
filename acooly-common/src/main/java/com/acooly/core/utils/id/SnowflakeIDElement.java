package com.acooly.core.utils.id;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SnowflakeIDElement implements Serializable {
    private static final long serialVersionUID = 5392219277256664953L;
    private Long id;
    private Date date;
    private long regionId;
    private long workerId;

    public SnowflakeIDElement(Long id) {
        this.id = id;
    }

    public Date getDate() {
        return this.date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public long getRegionId() {
        return this.regionId;
    }

    public void setRegionId(long regionId) {
        this.regionId = regionId;
    }

    public long getWorkerId() {
        return this.workerId;
    }

    public void setWorkerId(long workerId) {
        this.workerId = workerId;
    }

    public String toString() {
        return "id=["
                + this.id
                + "],date=["
                + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(this.date)
                + "],regionId=["
                + this.regionId
                + "],workerId=["
                + this.workerId
                + "]";
    }

    public Long getId() {
        return this.id;
    }
}
