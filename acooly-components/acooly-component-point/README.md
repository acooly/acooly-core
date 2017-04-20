acooly-components-point(积分组件)
====

## 简介

acooly-components-point以acooly框架为基础开发的积分业务组件

## 接口说明：

	1：积分账户查询 
	com.acooly.point.service.PointAccountService#findByUserName
	
	2：积分产生 
	com.acooly.point.service.PointTradeService#pointProduce
	
	3：积分消费 
	com.acooly.point.service.PointTradeService#pointExpense
	
	4：积分冻结 
	com.acooly.point.service.PointTradeService#pointFreeze
	
	5：积分解冻 
	com.acooly.point.service.PointTradeService#pointUnfreeze

	6：积分等级排名（gradeId为空：所有用户排名；gradeId不为空：同等级用户排名）
	com.acooly.point.service.PointAccountService#pointRank

	7：获取即将清零积分
	com.acooly.point.service.PointTradeService#getClearPoint


