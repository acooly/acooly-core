<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/manage/common/taglibs.jsp"%>
<div>
    <form id="manage_lotteryWinner_editform" action="${pageContext.request.contextPath}/manage/module/lottery/lotteryWinner/${action=='create'?'saveJson':'updateJson'}.html" method="post">
      <jodd:form bean="lotteryWinner" scope="request">
        <input name="id" type="hidden" />
        <table class="tableForm" width="100%">
			<tr>
				<th>抽奖活动：</th>
				<td>${lotteryWinner.lotteryTitle}</td>
			</tr>					
			<tr>
				<th>奖项：</th>
				<td>${lotteryWinner.award}</td>
			</tr>					
			<tr>
				<th>抽奖人：</th>
				<td>${lotteryWinner.winner}</td>
			</tr>					
			<tr>
				<th>中奖时间：</th>
				<td><fmt:formatDate value="${lotteryWinner.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
			</tr>					
			<tr>
				<th>状态：</th>
				<td>
				<select name="status" editable="false" panelHeight="auto" class="easyui-combobox"><c:forEach var="e" items="${allStatuss}"><option value="${e.key}">${e.value}</option></c:forEach></select>
				</td>
			</tr>					
			<tr>
				<th>备注：</th>
				<td><textarea rows="3" cols="40" name="comments" class="easyui-validatebox"  validType="byteLength[1,256]"></textarea></td>
			</tr>					
        </table>
      </jodd:form>
    </form>
</div>
