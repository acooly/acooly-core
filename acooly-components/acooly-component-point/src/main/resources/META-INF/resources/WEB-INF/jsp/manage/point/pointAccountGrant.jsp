<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/manage/common/taglibs.jsp"%>
<div>
    <form id="manage_pointAccount_editform" action="${pageContext.request.contextPath}/manage/point/pointAccount/grantJson.html" method="post">
        <table class="tableForm" width="100%">
			<tr>
				<th width="25%">用户名：</th>
				<td>
				<textarea rows="5" cols="40" style="width:300px;" name="userNames" data-options="required:true" class="easyui-validatebox"  validType="byteLength[1,256]"></textarea>
				</br>
				<span style="color: red;">多个用户名使用英文逗号分隔</span>
				</td>
			</tr>					
			<tr>
				<th>发放积分：</th>
				<td>
				<input type="text" name="point" size="23" class="easyui-numberbox text" data-options="required:true" validType="byteLength[1,19]"/>
				<br/>
				每一个用户发放数量
				</td>
			</tr>					
			<tr>
				<th>备注：</th>
				<td><input type="text" name="memo" size="23" class="easyui-validatebox text" data-options="required:true" validType="byteLength[1,19]"/></td>
			</tr>					
        </table>
    </form>
</div>
