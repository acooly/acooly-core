<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/manage/common/taglibs.jsp"%>
<div>
    <form id="manage_pointAccount_editform" action="${pageContext.request.contextPath}/manage/point/pointAccount/clearJson.html" method="post">
        <table class="tableForm" width="100%">
			<tr>
				<th>提示：</th>
				<td>积分清零不可恢复,请小心操作</td>
			</tr>
			<tr>
				<th>清零开始时间：</th>
				<td><input type="text" name="startTime" size="20" class="easyui-validatebox text" onFocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd 00:00:00'})" data-options="required:true" /></td>
			</tr>					
			<tr>
				<th>清零结束时间：</th>
				<td><input type="text" name="endTime" size="20" class="easyui-validatebox text" onFocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd 23:59:59'})" data-options="required:true" /></td>
			</tr>					
			<tr>
				<th>清零备注：</th>
				<td><input type="text" name="memo" size="23" class="easyui-validatebox text" data-options="required:true" validType="byteLength[1,19]"/></td>
			</tr>					
        </table>
    </form>
</div>
