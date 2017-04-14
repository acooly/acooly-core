<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/manage/common/taglibs.jsp"%>
<div>
    <form id="manage_actionLog_editform" action="${pageContext.request.contextPath}/manage/module/portlet/actionLog/${action=='create'?'saveJson':'updateJson'}.html" method="post">
      <jodd:form bean="actionLog" scope="request">
        <input name="id" type="hidden" />
        <table class="tableForm" width="100%">
			<tr>
				<th width="25%">操作：</th>
				<td><input type="text" name="actionKey" size="48" class="easyui-validatebox text" data-options="required:true" validType="byteLength[1,32]"/></td>
			</tr>					
			<tr>
				<th>操作名称：</th>
				<td><input type="text" name="actionName" size="48" class="easyui-validatebox text"  validType="byteLength[1,32]"/></td>
			</tr>					
			<tr>
				<th>URL连接：</th>
				<td><input type="text" name="actionUrl" size="48" class="easyui-validatebox text"  validType="byteLength[1,128]"/></td>
			</tr>					
			<tr>
				<th>用户名：</th>
				<td><input type="text" name="userName" size="48" class="easyui-validatebox text" data-options="required:true" validType="byteLength[1,32]"/></td>
			</tr>					
			<tr>
				<th>渠道：</th>
				<td><select name="channel" editable="false" style="height:27px;" panelHeight="auto" class="easyui-combobox" data-options="required:true">
					<c:forEach items="${allChannels}" var="e">
						<option value="${e.key}">${e.value}</option>
					</c:forEach>
				</select></td>
			</tr>					
			<tr>
				<th>渠道信息：</th>
				<td><textarea rows="3" cols="40" style="width:300px;" name="channelInfo" class="easyui-validatebox"  validType="byteLength[1,255]"></textarea></td>
			</tr>					
			<tr>
				<th>渠道版本：</th>
				<td><input type="text" name="channelVersion" size="48" class="easyui-validatebox text"  validType="byteLength[1,16]"/></td>
			</tr>					
			<tr>
				<th>访问IP：</th>
				<td><input type="text" name="userIp" size="48" class="easyui-validatebox text"  validType="byteLength[1,16]"/></td>
			</tr>					
			<tr>
				<th>备注：</th>
				<td><input type="text" name="comments" size="48" class="easyui-validatebox text"  validType="byteLength[1,128]"/></td>
			</tr>					
        </table>
      </jodd:form>
    </form>
</div>
