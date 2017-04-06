<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/manage/common/taglibs.jsp"%>
<div>
    <form id="manage_schedulerRule_editform" action="${pageContext.request.contextPath}/manage/schedulerRule/${action=='create'?'saveJson':'updateJson'}.html" method="post">
      <jodd:form bean="schedulerRule" scope="request">
        <input name="id" type="hidden" />
        <table class="tableForm" width="100%">
			<tr>
				<th width="25%">创建时间：</th>
				<td><input type="text" name="rawAddTime" size="20" class="easyui-validatebox text" value="<fmt:formatDate value="${schedulerRule.rawAddTime}" pattern="yyyy-MM-dd HH:mm:ss"/>" onFocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd HH:mm:ss'})" data-options="required:true" /></td>
			</tr>					
			<tr>
				<th>修改时间：</th>
				<td><input type="text" name="rawUpdateTime" size="20" class="easyui-validatebox text" value="<fmt:formatDate value="${schedulerRule.rawUpdateTime}" pattern="yyyy-MM-dd HH:mm:ss"/>" onFocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd HH:mm:ss'})"  /></td>
			</tr>					
			<tr>
				<th>action_type：</th>
				<td><textarea rows="3" cols="40" style="width:300px;" name="actionType" class="easyui-validatebox"  validType="byteLength[1,255]"></textarea></td>
			</tr>					
			<tr>
				<th>class_name：</th>
				<td><textarea rows="3" cols="40" style="width:300px;" name="className" class="easyui-validatebox"  validType="byteLength[1,255]"></textarea></td>
			</tr>					
			<tr>
				<th>creater：</th>
				<td><textarea rows="3" cols="40" style="width:300px;" name="creater" class="easyui-validatebox"  validType="byteLength[1,255]"></textarea></td>
			</tr>					
			<tr>
				<th>cron_string：</th>
				<td><textarea rows="3" cols="40" style="width:300px;" name="cronString" class="easyui-validatebox"  validType="byteLength[1,255]"></textarea></td>
			</tr>					
			<tr>
				<th>exception_at_last_execute：</th>
				<td><textarea rows="3" cols="40" style="width:300px;" name="exceptionAtLastExecute" class="easyui-validatebox"  validType="byteLength[1,255]"></textarea></td>
			</tr>					
			<tr>
				<th>execute_num：</th>
				<td><input type="text" name="executeNum" size="48" class="easyui-numberbox text"  validType="byteLength[1,10]"/></td>
			</tr>					
			<tr>
				<th>last_execute_time：</th>
				<td><input type="text" name="lastExecuteTime" size="20" class="easyui-validatebox text" value="<fmt:formatDate value="${schedulerRule.lastExecuteTime}" pattern="yyyy-MM-dd HH:mm:ss"/>" onFocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd HH:mm:ss'})"  /></td>
			</tr>					
			<tr>
				<th>memo：</th>
				<td><textarea rows="3" cols="40" style="width:300px;" name="memo" class="easyui-validatebox"  validType="byteLength[1,255]"></textarea></td>
			</tr>					
			<tr>
				<th>method_name：</th>
				<td><textarea rows="3" cols="40" style="width:300px;" name="methodName" class="easyui-validatebox"  validType="byteLength[1,255]"></textarea></td>
			</tr>					
			<tr>
				<th>modifyer：</th>
				<td><textarea rows="3" cols="40" style="width:300px;" name="modifyer" class="easyui-validatebox"  validType="byteLength[1,255]"></textarea></td>
			</tr>					
			<tr>
				<th>properties：</th>
				<td><textarea rows="3" cols="40" style="width:300px;" name="properties" class="easyui-validatebox"  validType="byteLength[1,255]"></textarea></td>
			</tr>					
			<tr>
				<th>retry_time_on_exception：</th>
				<td><input type="text" name="retryTimeOnException" size="48" class="easyui-numberbox text"  validType="byteLength[1,10]"/></td>
			</tr>					
			<tr>
				<th>status：</th>
				<td><textarea rows="3" cols="40" style="width:300px;" name="status" class="easyui-validatebox"  validType="byteLength[1,255]"></textarea></td>
			</tr>					
			<tr>
				<th>validity_end：</th>
				<td><input type="text" name="validityEnd" size="20" class="easyui-validatebox text" value="<fmt:formatDate value="${schedulerRule.validityEnd}" pattern="yyyy-MM-dd HH:mm:ss"/>" onFocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd HH:mm:ss'})"  /></td>
			</tr>					
			<tr>
				<th>validity_start：</th>
				<td><input type="text" name="validityStart" size="20" class="easyui-validatebox text" value="<fmt:formatDate value="${schedulerRule.validityStart}" pattern="yyyy-MM-dd HH:mm:ss"/>" onFocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd HH:mm:ss'})"  /></td>
			</tr>					
			<tr>
				<th>是否删除：</th>
				<td><input type="text" name="isDel" size="48" class="easyui-validatebox text"  validType="byteLength[1,1]"/></td>
			</tr>					
        </table>
      </jodd:form>
    </form>
</div>
