<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/manage/common/taglibs.jsp"%>
<div style="margin-top: 10px;">
	<form id="manage_lottery_editform" action="${pageContext.request.contextPath}/manage/module/lottery/lottery/${action=='create'?'saveJson':'updateJson'}.html" method="post">
		<jodd:form bean="lottery" scope="request">
			<input name="id" type="hidden" />
			<table class="tableForm" width="100%">
				<tr>
					<th width="25%">标题：</th>
					<td><input type="text" name="title" style="width: 300px;" class="easyui-validatebox" validType="byteLength[1,64]" /></td>
				</tr>
				<tr>
					<th>说明：</th>
					<td><textarea rows="6" style="width: 300px;" cols="40" name="note" class="easyui-validatebox" validType="byteLength[1,255]"></textarea></td>
				</tr>
				<tr>
					<th>类型：</th>
					<td><select style="width: 80px;" name="type" editable="false" panelHeight="auto" class="easyui-combobox"><c:forEach var="e" items="${allTypes}">
								<option value="${e.key}" ${param.search_LIKE_type == e.key?'selected':''}>${e.value}</option>
							</c:forEach></select></td>
				</tr>
				<tr>
					<th width="25%">最大参入人数：</th>
					<td><input type="text" name="maxWinners" style="width: 300px;" class="easyui-numberbox" validType="byteLength[1,10]" />
						<div style="margin-top: 5px">0表示无限制。达到最大参与人数后，后动自动结束</div></td>
				</tr>
				<tr>
					<th>开始时间：</th>
					<td><input type="text" name="startTime" size="15" value="<fmt:formatDate value="${lottery.startTime}" pattern="yyyy-MM-dd"/>" onFocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd'})" /></td>
				</tr>
				<tr>
					<th>结束时间：</th>
					<td><input type="text" name="endTime" size="15" value="<fmt:formatDate value="${lottery.endTime}" pattern="yyyy-MM-dd"/>" onFocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd'})" /></td>
				</tr>
				<tr>
					<th>备注：</th>
					<td><textarea rows="6" style="width: 300px;" cols="40" name="comments" class="easyui-validatebox" validType="byteLength[1,255]"></textarea></td>
				</tr>
			</table>
		</jodd:form>
	</form>
</div>

