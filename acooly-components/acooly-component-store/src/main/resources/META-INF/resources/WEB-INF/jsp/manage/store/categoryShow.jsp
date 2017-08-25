<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/manage/common/taglibs.jsp"%>
<div style="padding: 5px;font-family:微软雅黑;">
<table class="tableForm" width="100%">
	<tr>
		<th>ID:</th>
		<td>${category.id}</td>
	</tr>					
	<tr>
		<th width="25%">父ID:</th>
		<td>${category.parentId}</td>
	</tr>					
	<tr>
		<th>查询路径:</th>
		<td>${category.path}</td>
	</tr>					
	<tr>
		<th>仓库编码:</th>
		<td>${category.storeCode}</td>
	</tr>					
	<tr>
		<th>品类名称:</th>
		<td>${category.name}</td>
	</tr>					
	<tr>
		<th>create_time:</th>
		<td><fmt:formatDate value="${category.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
	</tr>					
	<tr>
		<th>update_time:</th>
		<td><fmt:formatDate value="${category.updateTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
	</tr>					
	<tr>
		<th>备注:</th>
		<td>${category.comments}</td>
	</tr>					
</table>
</div>
