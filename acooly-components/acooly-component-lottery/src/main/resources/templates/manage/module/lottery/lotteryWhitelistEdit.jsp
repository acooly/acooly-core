<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/manage/common/taglibs.jsp"%>
<div>
    <form id="manage_lotteryWhitelist_editform" action="${pageContext.request.contextPath}/manage/module/lottery/lotteryWhitelist/${action=='create'?'saveJson':'updateJson'}.html" method="post">
      <jodd:form bean="lotteryWhitelist" scope="request">
        <input name="id" type="hidden" />
        <table class="tableForm" width="100%">
			<tr>
				<th width="25%">抽奖ID：</th>
				<td><input type="text" name="lotteryId" class="easyui-numberbox" data-options="required:true" validType="byteLength[1,19]"/></td>
			</tr>					
			<tr>
				<th>奖项ID：</th>
				<td><input type="text" name="awardId" class="easyui-numberbox" data-options="required:true" validType="byteLength[1,19]"/></td>
			</tr>					
			<tr>
				<th>抽奖用户：</th>
				<td><input type="text" name="user" class="easyui-validatebox" data-options="required:true" validType="byteLength[1,64]"/></td>
			</tr>					
			<tr>
				<th>创建时间：</th>
				<td><input type="text" name="createTime" size="15" value="<fmt:formatDate value="${lotteryWhitelist.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/>" onFocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd HH:mm:ss'})"  /></td>
			</tr>					
			<tr>
				<th>修改时间：</th>
				<td><input type="text" name="updateTime" size="15" value="<fmt:formatDate value="${lotteryWhitelist.updateTime}" pattern="yyyy-MM-dd HH:mm:ss"/>" onFocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd HH:mm:ss'})"  /></td>
			</tr>					
			<tr>
				<th>状态：</th>
				<td><select name="status" editable="false" panelHeight="auto" class="easyui-combobox" >
					<c:forEach items="${allStatuss}" var="e">
						<option value="${e.key}">${e.value}</option>
					</c:forEach>
				</select></td>
			</tr>					
			<tr>
				<th>备注：</th>
				<td><input type="text" name="comments" class="easyui-validatebox"  validType="byteLength[1,128]"/></td>
			</tr>					
        </table>
      </jodd:form>
    </form>
</div>
