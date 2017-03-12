<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/manage/common/taglibs.jsp"%>
<div style="padding: 5px;font-family:微软雅黑;">
<table class="tableForm" width="100%">
	<tr>
		<th width="25%">抽奖ID:</th>
		<td>${lotteryAward.lotteryId}</td>
	</tr>					
	<tr>
		<th>奖项:</th>
		<td>${lotteryAward.award}</td>
	</tr>					
	<tr>
		<th>award_note:</th>
		<td>${lotteryAward.awardNote}</td>
	</tr>					
	<tr>
		<th>奖品图片:</th>
		<td>${lotteryAward.awardPhoto}</td>
	</tr>					
	<tr>
		<th>权重:</th>
		<td>${lotteryAward.weight}</td>
	</tr>					
	<tr>
		<th>最大中奖数:</th>
		<td>${lotteryAward.maxWiner}</td>
	</tr>					
	<tr>
		<th>创建时间:</th>
		<td><fmt:formatDate value="${lotteryAward.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
	</tr>					
	<tr>
		<th>最后修改时间:</th>
		<td><fmt:formatDate value="${lotteryAward.modifyTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
	</tr>					
	<tr>
		<th>备注:</th>
		<td>${lotteryAward.comments}</td>
	</tr>					
</table>
</div>
