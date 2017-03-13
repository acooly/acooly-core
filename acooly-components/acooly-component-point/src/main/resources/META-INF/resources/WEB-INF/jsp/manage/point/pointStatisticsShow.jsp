<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/manage/common/taglibs.jsp"%>
<div style="padding: 5px;font-family:微软雅黑;">
<table class="tableForm" width="100%">
	<tr>
		<th>ID:</th>
		<td>${pointStatistics.id}</td>
	</tr>					
	<tr>
		<th width="25%">用户名:</th>
		<td>${pointStatistics.userName}</td>
	</tr>					
	<tr>
		<th>统计条数:</th>
		<td>${pointStatistics.num}</td>
	</tr>					
	<tr>
		<th>积分余额:</th>
		<td>${pointStatistics.point}</td>
	</tr>					
	<tr>
		<th>开始时间:</th>
		<td><fmt:formatDate value="${pointStatistics.startTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
	</tr>					
	<tr>
		<th>结束时间:</th>
		<td><fmt:formatDate value="${pointStatistics.endTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
	</tr>					
	<tr>
		<th>状态:</th>
		<td>${pointStatistics.status.message}</td>
	</tr>					
	<tr>
		<th>创建时间:</th>
		<td><fmt:formatDate value="${pointStatistics.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
	</tr>					
	<tr>
		<th>修改时间:</th>
		<td><fmt:formatDate value="${pointStatistics.updateTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
	</tr>					
	<tr>
		<th>备注:</th>
		<td>${pointStatistics.memo}</td>
	</tr>					
</table>
</div>
