package com.acooly.module.point.event;

/**
 * 发布积分事件
 *
 * @author cuifuq7
 */
public class PointEvent {
    /**
     * 用户名
     */
    private String userName;
    /**
     * 积分余额
     */
    private Long balance = 0l;
    /**
     * 冻结
     */
    private Long freeze = 0l;
    /**
     * 可用余额
     */
    private Long available = 0l;
    /**
     * 总消费积分 *
     */
    private Long totalExpensePoint = 0l;
    /**
     * 总产生积分 *
     */
    private Long totalProducePoint = 0l;
    /**
     * 积分等级 *
     */
    private Long gradeId;
    /**
     * 积分等级标题 *
     */
    private String gradeTitle;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Long getBalance() {
        return balance;
    }

    public void setBalance(Long balance) {
        this.balance = balance;
    }

    public Long getFreeze() {
        return freeze;
    }

    public void setFreeze(Long freeze) {
        this.freeze = freeze;
    }

    public Long getAvailable() {
        return available;
    }

    public void setAvailable(Long available) {
        this.available = available;
    }

    public Long getTotalExpensePoint() {
        return totalExpensePoint;
    }

    public void setTotalExpensePoint(Long totalExpensePoint) {
        this.totalExpensePoint = totalExpensePoint;
    }

    public Long getTotalProducePoint() {
        return totalProducePoint;
    }

    public void setTotalProducePoint(Long totalProducePoint) {
        this.totalProducePoint = totalProducePoint;
    }

    public Long getGradeId() {
        return gradeId;
    }

    public void setGradeId(Long gradeId) {
        this.gradeId = gradeId;
    }

    public String getGradeTitle() {
        return gradeTitle;
    }

    public void setGradeTitle(String gradeTitle) {
        this.gradeTitle = gradeTitle;
    }
}
