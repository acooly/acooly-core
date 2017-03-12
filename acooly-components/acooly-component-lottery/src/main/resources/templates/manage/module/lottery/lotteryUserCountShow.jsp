<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/manage/common/taglibs.jsp"%>
<div style="padding: 5px;font-family:微软雅黑;">
<table class="tableForm" width="100%">
	<tr>
		<th width="25%">活动ID:</th>
		<td>${lotteryUserCount.lotteryid}</td>
	</tr>					
	<tr>
		<th>活动标题:</th>
		<td>${lotteryUserCount.lotteryTitle}</td>
	</tr>					
	<tr>
		<th>参与人:</th>
		<td>${lotteryUserCount.user}</td>
	</tr>					
	<tr>
		<th>获参次数:</th>
		<td>${lotteryUserCount.totalTimes}</td>
	</tr>					
	<tr>
		<th>参与次数:</th>
		<td>${lotteryUserCount.playTimes}</td>
	</tr>					
	<tr>
		<th>创建时间:</th>
		<td><fmt:formatDate value="${lotteryUserCount.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
	</tr>					
	<tr>
		<th>最后修改时间:</th>
		<td><fmt:formatDate value="${lotteryUserCount.updateTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
	</tr>					
	<tr>
		<th>备注:</th>
		<td>${lotteryUserCount.comments}</td>
	</tr>					
</table>
</div>
