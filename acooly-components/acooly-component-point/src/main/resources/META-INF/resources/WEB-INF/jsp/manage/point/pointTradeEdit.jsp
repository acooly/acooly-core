<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/manage/common/taglibs.jsp"%>
<div>
    <form id="manage_pointTrade_editform" action="${pageContext.request.contextPath}/manage/point/pointTrade/${action=='create'?'saveJson':'updateJson'}.html" method="post">
      <jodd:form bean="pointTrade" scope="request">
        <input name="id" type="hidden" />
        <table class="tableForm" width="100%">
			<tr>
				<th width="25%">交易订单号：</th>
				<td><input type="text" name="tradeNo" size="48" class="easyui-validatebox text" data-options="required:true" validType="byteLength[1,32]"/></td>
			</tr>					
			<tr>
				<th>交易类型：</th>
				<td><select name="tradeType" editable="false" style="height:27px;" panelHeight="auto" class="easyui-combobox" data-options="required:true">
					<c:forEach items="${allTradeTypes}" var="e">
						<option value="${e.key}">${e.value}</option>
					</c:forEach>
				</select></td>
			</tr>					
			<tr>
				<th>用户名：</th>
				<td><input type="text" name="userName" size="48" class="easyui-validatebox text" data-options="required:true" validType="byteLength[1,32]"/></td>
			</tr>					
			<tr>
				<th>积分账户ID：</th>
				<td><input type="text" name="accountId" size="48" class="easyui-numberbox text" data-options="required:true" validType="byteLength[1,19]"/></td>
			</tr>					
			<tr>
				<th>交易积分：</th>
				<td><input type="text" name="amount" size="48" class="easyui-numberbox text" data-options="required:true" validType="byteLength[1,19]"/></td>
			</tr>					
			<tr>
				<th>交易后积分：</th>
				<td><input type="text" name="balance" size="48" class="easyui-numberbox text" data-options="required:true" validType="byteLength[1,19]"/></td>
			</tr>					
			<tr>
				<th>相关业务数据：</th>
				<td><textarea rows="3" cols="40" style="width:300px;" name="businessData" class="easyui-validatebox"  validType="byteLength[1,256]"></textarea></td>
			</tr>					
			<tr>
				<th>备注：</th>
				<td><textarea rows="3" cols="40" style="width:300px;" name="memo" class="easyui-validatebox"  validType="byteLength[1,256]"></textarea></td>
			</tr>					
        </table>
      </jodd:form>
    </form>
</div>
