<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/manage/common/taglibs.jsp"%>
<div style="padding: 5px;font-family:微软雅黑;">
<table class="tableForm" width="100%">
	<tr>
		<th>id:</th>
		<td>${pointGrade.id}</td>
	</tr>					
	<tr>
		<th width="25%">等级:</th>
		<td>${pointGrade.num}</td>
	</tr>					
	<tr>
		<th>标题:</th>
		<td>${pointGrade.title}</td>
	</tr>					
	<tr>
		<th>积分区间_开始:</th>
		<td>${pointGrade.startPoint}</td>
	</tr>					
	<tr>
		<th>积分区间_结束:</th>
		<td>${pointGrade.endPoint}</td>
	</tr>					
	<tr>
		<th>create_time:</th>
		<td><fmt:formatDate value="${pointGrade.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
	</tr>					
	<tr>
		<th>update_time:</th>
		<td><fmt:formatDate value="${pointGrade.updateTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
	</tr>					
	<tr>
		<th> 图标:</th>
		<td>${pointGrade.picture}</td>
	</tr>					
	<tr>
		<th>备注:</th>
		<td>${pointGrade.memo}</td>
	</tr>					
</table>
</div>
