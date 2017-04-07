<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/manage/common/taglibs.jsp"%>
<div style="padding: 5px;font-family:微软雅黑;">
<table class="tableForm" width="100%">
	<tr>
		<th>id:</th>
		<td>${schedulerRule.id}</td>
	</tr>					
	<tr>
		<th width="25%">创建时间:</th>
		<td><fmt:formatDate value="${schedulerRule.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
	</tr>					
	<tr>
		<th>修改时间:</th>
		<td><fmt:formatDate value="${schedulerRule.updateTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
	</tr>					
	<tr>
		<th>action_type:</th>
		<td>${schedulerRule.actionType}</td>
	</tr>					
	<tr>
		<th>class_name:</th>
		<td>${schedulerRule.className}</td>
	</tr>					
	<tr>
		<th>creater:</th>
		<td>${schedulerRule.creater}</td>
	</tr>					
	<tr>
		<th>cron_string:</th>
		<td>${schedulerRule.cronString}</td>
	</tr>					
	<tr>
		<th>exception_at_last_execute:</th>
		<td>${schedulerRule.exceptionAtLastExecute}</td>
	</tr>					
	<tr>
		<th>execute_num:</th>
		<td>${schedulerRule.executeNum}</td>
	</tr>					
	<tr>
		<th>last_execute_time:</th>
		<td><fmt:formatDate value="${schedulerRule.lastExecuteTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
	</tr>					
	<tr>
		<th>memo:</th>
		<td>${schedulerRule.memo}</td>
	</tr>					
	<tr>
		<th>method_name:</th>
		<td>${schedulerRule.methodName}</td>
	</tr>					
	<tr>
		<th>modifyer:</th>
		<td>${schedulerRule.modifyer}</td>
	</tr>					
	<tr>
		<th>properties:</th>
		<td>${schedulerRule.properties}</td>
	</tr>					
	<tr>
		<th>retry_time_on_exception:</th>
		<td>${schedulerRule.retryTimeOnException}</td>
	</tr>					
	<tr>
		<th>status:</th>
		<td>${schedulerRule.status}</td>
	</tr>					
	<tr>
		<th>validity_end:</th>
		<td><fmt:formatDate value="${schedulerRule.validityEnd}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
	</tr>					
	<tr>
		<th>validity_start:</th>
		<td><fmt:formatDate value="${schedulerRule.validityStart}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
	</tr>					
	<tr>
		<th>是否删除:</th>
		<td>${schedulerRule.isDel}</td>
	</tr>					
</table>
</div>
