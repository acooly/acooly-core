<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/manage/common/taglibs.jsp"%>
<div style="padding: 5px;font-family:微软雅黑;">
<table class="tableForm" width="100%">
	<tr>
		<th>主键:</th>
		<td>${portletConfig.id}</td>
	</tr>					
	<tr>
		<th width="25%">类型:</th>
		<td>${portletConfig.type.message}</td>
	</tr>					
	<tr>
		<th>标题:</th>
		<td>${portletConfig.title}</td>
	</tr>					
	<tr>
		<th>参数键:</th>
		<td>${portletConfig.key}</td>
	</tr>					
	<tr>
		<th>参数值:</th>
		<td>${portletConfig.value}</td>
	</tr>					
	<tr>
		<th>备注:</th>
		<td>${portletConfig.comments}</td>
	</tr>					
	<tr>
		<th>create_time:</th>
		<td><fmt:formatDate value="${portletConfig.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
	</tr>					
	<tr>
		<th>update_time:</th>
		<td><fmt:formatDate value="${portletConfig.updateTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
	</tr>					
</table>
</div>
