<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/manage/common/taglibs.jsp"%>
<div style="padding: 5px;font-family:微软雅黑;">
<table class="tableForm" width="100%">
	<tr>
		<th>id:</th>
		<td>${store.id}</td>
	</tr>					
	<tr>
		<th width="25%">仓库编码:</th>
		<td>${store.storeCode}</td>
	</tr>					
	<tr>
		<th>仓库名称:</th>
		<td>${store.storeName}</td>
	</tr>					
	<tr>
		<th>create_time:</th>
		<td><fmt:formatDate value="${store.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
	</tr>					
	<tr>
		<th>update_time:</th>
		<td><fmt:formatDate value="${store.updateTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
	</tr>					
	<tr>
		<th>备注:</th>
		<td>${store.comments}</td>
	</tr>					
</table>
</div>
