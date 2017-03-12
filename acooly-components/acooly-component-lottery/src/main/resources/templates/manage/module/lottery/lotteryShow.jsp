<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/manage/common/taglibs.jsp"%>
<div style="padding: 5px;font-family:微软雅黑;">
<table class="tableForm" width="100%">
	<tr>
		<th width="25%">标题:</th>
		<td>${lottery.title}</td>
	</tr>					
	<tr>
		<th>类型:</th>
		<td>${lottery.type}</td>
	</tr>					
	<tr>
		<th>开始时间:</th>
		<td><fmt:formatDate value="${lottery.startTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
	</tr>					
	<tr>
		<th>结束时间:</th>
		<td><fmt:formatDate value="${lottery.endTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
	</tr>					
	<tr>
		<th>创建时间:</th>
		<td><fmt:formatDate value="${lottery.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
	</tr>					
	<tr>
		<th>状态:</th>
		<td>${lottery.status}</td>
	</tr>					
	<tr>
		<th>备注:</th>
		<td>${lottery.comments}</td>
	</tr>					
</table>
</div>
