<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/manage/common/taglibs.jsp"%>
<div>
    <form id="manage_lotteryCount_editform" action="${pageContext.request.contextPath}/manage/module/lottery/lotteryCount/${action=='create'?'saveJson':'updateJson'}.html" method="post">
      <jodd:form bean="lotteryCount" scope="request">
        <input name="id" type="hidden" />
        <table class="tableForm" width="100%">
			<tr>
				<th width="25%">关键字：</th>
				<td><input type="text" name="key" class="easyui-validatebox" data-options="required:true" validType="byteLength[1,64]"/></td>
			</tr>					
			<tr>
				<th>计数值：</th>
				<td><input type="text" name="value" class="easyui-numberbox" data-options="required:true" validType="byteLength[1,10]"/></td>
			</tr>					
			<tr>
				<th>创建时间：</th>
				<td><input type="text" name="createTime" size="15" value="<fmt:formatDate value="${lotteryCount.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/>" onFocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd HH:mm:ss'})" data-options="required:true" /></td>
			</tr>					
			<tr>
				<th>更新时间：</th>
				<td><input type="text" name="updateTime" size="15" value="<fmt:formatDate value="${lotteryCount.updateTime}" pattern="yyyy-MM-dd HH:mm:ss"/>" onFocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd HH:mm:ss'})" data-options="required:true" /></td>
			</tr>					
			<tr>
				<th>备注：</th>
				<td><textarea rows="3" cols="40" name="comment" class="easyui-validatebox"  validType="byteLength[1,255]"></textarea></td>
			</tr>					
        </table>
      </jodd:form>
    </form>
</div>
