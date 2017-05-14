<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/manage/common/taglibs.jsp"%>
<div style="padding: 5px;font-family:微软雅黑;">
<table class="tableForm" width="100%">
	<tr>
		<th>ID:</th>
		<td>${pointClearConfig.id}</td>
	</tr>					
	<tr>
		<th width="25%">开始交易时间:</th>
		<td><fmt:formatDate value="${pointClearConfig.startTradeTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
	</tr>					
	<tr>
		<th>结束交易时间:</th>
		<td><fmt:formatDate value="${pointClearConfig.endTradeTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
	</tr>					
	<tr>
		<th>开始清理时间:</th>
		<td><fmt:formatDate value="${pointClearConfig.startClearTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
	</tr>					
	<tr>
		<th>结束清理时间:</th>
		<td><fmt:formatDate value="${pointClearConfig.endClearTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
	</tr>					
	<tr>
		<th>清零时间:</th>
		<td><fmt:formatDate value="${pointClearConfig.clearTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
	</tr>					
	<tr>
		<th>状态:</th>
		<td>${pointClearConfig.status.message}</td>
	</tr>					
	<tr>
		<th>创建时间:</th>
		<td><fmt:formatDate value="${pointClearConfig.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
	</tr>					
	<tr>
		<th>修改时间:</th>
		<td><fmt:formatDate value="${pointClearConfig.updateTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
	</tr>					
	<tr>
		<th>备注:</th>
		<td>${pointClearConfig.memo}</td>
	</tr>					
</table>
</div>
