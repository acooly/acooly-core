<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/manage/common/taglibs.jsp"%>
<div style="padding: 5px;font-family:微软雅黑;">
<table class="tableForm" width="100%">
	<tr>
		<th width="25%">抽奖ID:</th>
		<td>${lotteryWhitelist.lotteryId}</td>
	</tr>					
	<tr>
		<th>奖项ID:</th>
		<td>${lotteryWhitelist.awardId}</td>
	</tr>					
	<tr>
		<th>抽奖用户:</th>
		<td>${lotteryWhitelist.user}</td>
	</tr>					
	<tr>
		<th>创建时间:</th>
		<td><fmt:formatDate value="${lotteryWhitelist.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
	</tr>					
	<tr>
		<th>修改时间:</th>
		<td><fmt:formatDate value="${lotteryWhitelist.updateTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
	</tr>					
	<tr>
		<th>状态:</th>
		<td>${lotteryWhitelist.status}</td>
	</tr>					
	<tr>
		<th>备注:</th>
		<td>${lotteryWhitelist.comments}</td>
	</tr>					
</table>
</div>
