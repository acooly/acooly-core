<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/manage/common/taglibs.jsp"%>
<div>
    <form id="manage_lotteryUserCount_editform" action="${pageContext.request.contextPath}/manage/module/lottery/lotteryUserCount/${action=='create'?'saveJson':'updateJson'}.html" method="post">
      <jodd:form bean="lotteryUserCount" scope="request">
        <input name="id" type="hidden" />
        <table class="tableForm" width="100%">
			<tr>
				<th width="25%">活动ID：</th>
				<td><input type="text" name="lotteryid" class="easyui-numberbox" data-options="required:true" validType="byteLength[1,19]"/></td>
			</tr>					
			<tr>
				<th>活动标题：</th>
				<td><input type="text" name="lotteryTitle" class="easyui-validatebox"  validType="byteLength[1,64]"/></td>
			</tr>					
			<tr>
				<th>参与人：</th>
				<td><input type="text" name="user" class="easyui-validatebox" data-options="required:true" validType="byteLength[1,64]"/></td>
			</tr>					
			<tr>
				<th>获参次数：</th>
				<td><input type="text" name="totalTimes" class="easyui-numberbox" data-options="required:true" validType="byteLength[1,10]"/></td>
			</tr>					
			<tr>
				<th>参与次数：</th>
				<td><input type="text" name="playTimes" class="easyui-numberbox" data-options="required:true" validType="byteLength[1,10]"/></td>
			</tr>					
			<tr>
				<th>创建时间：</th>
				<td><input type="text" name="createTime" size="15" value="<fmt:formatDate value="${lotteryUserCount.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/>" onFocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd HH:mm:ss'})" data-options="required:true" /></td>
			</tr>					
			<tr>
				<th>最后修改时间：</th>
				<td><input type="text" name="updateTime" size="15" value="<fmt:formatDate value="${lotteryUserCount.updateTime}" pattern="yyyy-MM-dd HH:mm:ss"/>" onFocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd HH:mm:ss'})" data-options="required:true" /></td>
			</tr>					
			<tr>
				<th>备注：</th>
				<td><input type="text" name="comments" class="easyui-validatebox" data-options="required:true" validType="byteLength[1,128]"/></td>
			</tr>					
        </table>
      </jodd:form>
    </form>
</div>
