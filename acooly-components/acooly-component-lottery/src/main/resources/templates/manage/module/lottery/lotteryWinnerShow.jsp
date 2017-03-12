<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/manage/common/taglibs.jsp"%>
<div style="padding: 5px;font-family:微软雅黑;">
<table class="tableForm" width="100%">
	<tr>
		<th width="25%">lottery_id:</th>
		<td>${lotteryWinner.lotteryId}</td>
	</tr>					
	<tr>
		<th>award_id:</th>
		<td>${lotteryWinner.awardId}</td>
	</tr>					
	<tr>
		<th>lottery_title:</th>
		<td>${lotteryWinner.lotteryTitle}</td>
	</tr>					
	<tr>
		<th>award:</th>
		<td>${lotteryWinner.award}</td>
	</tr>					
	<tr>
		<th>中奖人:</th>
		<td>${lotteryWinner.winner}</td>
	</tr>					
	<tr>
		<th>中奖时间:</th>
		<td><fmt:formatDate value="${lotteryWinner.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
	</tr>					
	<tr>
		<th>最后修改时间:</th>
		<td><fmt:formatDate value="${lotteryWinner.modifyTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
	</tr>					
	<tr>
		<th>状态:</th>
		<td>${lotteryWinner.status}</td>
	</tr>					
	<tr>
		<th>备注:</th>
		<td>${lotteryWinner.comments}</td>
	</tr>					
</table>
</div>
