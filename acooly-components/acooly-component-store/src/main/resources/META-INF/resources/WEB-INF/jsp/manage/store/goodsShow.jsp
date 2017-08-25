<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/manage/common/taglibs.jsp"%>
<div style="padding: 5px;font-family:微软雅黑;">
<table class="tableForm" width="100%">
	<tr>
		<th>id:</th>
		<td>${goods.id}</td>
	</tr>					
	<tr>
		<th width="25%">仓库编码:</th>
		<td>${goods.storeCode}</td>
	</tr>					
	<tr>
		<th>品类ID:</th>
		<td>${goods.categroryId}</td>
	</tr>					
	<tr>
		<th>品类名称:</th>
		<td>${goods.categroryName}</td>
	</tr>					
	<tr>
		<th>商品编码:</th>
		<td>${goods.code}</td>
	</tr>					
	<tr>
		<th>name:</th>
		<td>${goods.name}</td>
	</tr>					
	<tr>
		<th>商品简介:</th>
		<td>${goods.descn}</td>
	</tr>					
	<tr>
		<th>单价:</th>
		<td>${goods.price}</td>
	</tr>					
	<tr>
		<th>单位:</th>
		<td>${goods.unit}</td>
	</tr>					
	<tr>
		<th>库存:</th>
		<td>${goods.stock}</td>
	</tr>					
	<tr>
		<th>型号:</th>
		<td>${goods.model}</td>
	</tr>					
	<tr>
		<th>品牌:</th>
		<td>${goods.brand}</td>
	</tr>					
	<tr>
		<th>供应商:</th>
		<td>${goods.supplier}</td>
	</tr>					
	<tr>
		<th>展示地址:</th>
		<td>${goods.detailUrl}</td>
	</tr>					
	<tr>
		<th>create_time:</th>
		<td><fmt:formatDate value="${goods.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
	</tr>					
	<tr>
		<th>update_time:</th>
		<td><fmt:formatDate value="${goods.updateTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
	</tr>					
	<tr>
		<th>备注:</th>
		<td>${goods.comments}</td>
	</tr>					
</table>
</div>
