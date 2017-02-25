<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/manage/common/taglibs.jsp"%>
<div>
    <form id="manage_feedback_editform" action="${pageContext.request.contextPath}/manage/module/sitetools/feedback/${action=='create'?'saveJson':'updateJson'}.html" method="post">
      <jodd:form bean="feedback" scope="request">
        <input name="id" type="hidden" />
        <table class="tableForm" width="100%">
			<tr>
				<th width="25%">类型：</th>
				<td><input type="text" name="type" class="easyui-validatebox" data-options="required:true" validType="byteLength[1,16]"/></td>
			</tr>					
			<tr>
				<th>标题：</th>
				<td><input type="text" name="title" class="easyui-validatebox" data-options="required:true" validType="byteLength[1,64]"/></td>
			</tr>					
			<tr>
				<th>内容：</th>
				<td><textarea rows="3" cols="40" name="content" class="easyui-validatebox" data-options="required:true" validType="byteLength[1,512]"></textarea></td>
			</tr>					
			<tr>
				<th>用户名：</th>
				<td><input type="text" name="userName" class="easyui-validatebox" data-options="required:true" validType="byteLength[1,32]"/></td>
			</tr>					
			<tr>
				<th>联系电话：</th>
				<td><input type="text" name="telephone" class="easyui-validatebox"  validType="byteLength[1,21]"/></td>
			</tr>					
			<tr>
				<th>联系地址：</th>
				<td><textarea rows="3" cols="40" name="address" class="easyui-validatebox"  validType="byteLength[1,128]"></textarea></td>
			</tr>					
			<tr>
				<th>联系信息：</th>
				<td><textarea rows="3" cols="40" name="contactInfo" class="easyui-validatebox"  validType="byteLength[1,255]"></textarea></td>
			</tr>					
			<tr>
				<th>创建时间：</th>
				<td><input type="text" name="createTime" size="15" value="<fmt:formatDate value="${feedback.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/>" onFocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd HH:mm:ss'})"  /></td>
			</tr>					
			<tr>
				<th>修改时间：</th>
				<td><input type="text" name="modifyTime" size="15" value="<fmt:formatDate value="${feedback.updateTime}" pattern="yyyy-MM-dd HH:mm:ss"/>" onFocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd HH:mm:ss'})"  /></td>
			</tr>					
			<tr>
				<th>备注：</th>
				<td><textarea rows="3" cols="40" name="comments" class="easyui-validatebox"  validType="byteLength[1,255]"></textarea></td>
			</tr>					
        </table>
      </jodd:form>
    </form>
</div>
