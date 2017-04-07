<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/manage/common/taglibs.jsp"%>
<div>
    <form id="manage_schedulerRule_editform" action="${pageContext.request.contextPath}/manage/schedulerRule/${action=='create'?'saveJson':'updateJson'}.html" method="post">
      <jodd:form bean="schedulerRule" scope="request">
        <input name="id" type="hidden" />
        <table class="tableForm" width="100%">
            <tr>
                <th width="25%">任务名：</th>
                <td><textarea rows="1" cols="40" style="width:300px;" name="memo" class="easyui-validatebox"  validType="byteLength[1,255]"></textarea></td>
            </tr>
            <tr>
                <th width="25%">cron表达式：</th>
                <td>
                    <div style="margin-top: 5px">0 0/10 * * * ? 每10分钟执行一次；<br>0/10 * * * * ? 每10秒执行一次；<br>0 59 23 * * ? 每天23点29分钟执行一次；<br>0 0 9,11 * * ? 每天9点11点执行一次</div>
                    <textarea rows="1" cols="60" style="width:300px;" name="cronString" class="easyui-validatebox"  validType="byteLength[1,255]"></textarea>
                </td>
            </tr>
            <tr>
                <th width="25%">任务类型：</th>
                <td>
                    <div style="margin-top: 5px">本地任务只需要填全路径类名、方法名，并把此类注解为spring bean;<br>HTTP任务只需要填HTTP地址</div>
                    <select name="actionType" editable="false" style="height:27px;" panelHeight="auto" class="easyui-combobox" data-options="required:true">
                    <c:forEach items="${allTaskTypes}" var="e">
                        <option value="${e.key}">${e.value}</option>
                    </c:forEach>
                     </select>
                </td>
            </tr>
            <tr>
                <th>HTTP地址 ：</th>
                <td><textarea rows="1" cols="50" style="width:300px;" name="properties" class="easyui-validatebox"  validType="byteLength[1,255]"></textarea></td>
            </tr>
			<tr>
				<th>类名：</th>
				<td><textarea rows="1" cols="40" style="width:300px;" name="className" class="easyui-validatebox"  validType="byteLength[1,255]"></textarea></td>
			</tr>					
			<tr>
				<th>方法名：</th>
				<td><textarea rows="1" cols="40" style="width:300px;" name="methodName" class="easyui-validatebox"  validType="byteLength[1,255]"></textarea></td>
			</tr>
            <tr>
                <th>开始时间：</th>
                <td><input type="text" name="validityStart" size="20" class="easyui-validatebox text" value="<fmt:formatDate value="${schedulerRule.validityStart}" pattern="yyyy-MM-dd HH:mm:ss"/>" onFocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd HH:mm:ss'})"  /></td>
            </tr>
			<tr>
				<th>结束时间：</th>
				<td><input type="text" name="validityEnd" size="20" class="easyui-validatebox text" value="<fmt:formatDate value="${schedulerRule.validityEnd}" pattern="yyyy-MM-dd HH:mm:ss"/>" onFocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd HH:mm:ss'})"  /></td>
			</tr>
            <tr>
                <th>状态：</th>
                <td><select name="status" editable="false" style="height:27px;" panelHeight="auto" class="easyui-combobox" data-options="required:true">
                    <c:forEach items="${allStatuss}" var="e">
                        <option value="${e.key}">${e.value}</option>
                    </c:forEach>
                </select></td>
            </tr>
        </table>
      </jodd:form>
    </form>
</div>
