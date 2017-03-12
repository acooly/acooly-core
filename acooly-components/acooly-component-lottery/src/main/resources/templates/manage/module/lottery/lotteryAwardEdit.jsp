<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/manage/common/taglibs.jsp"%>
<div style="margin-top: 10px;">
    <form id="manage_lotteryAward_editform" action="${pageContext.request.contextPath}/manage/module/lottery/lotteryAward/${action=='create'?'saveJson':'updateJson'}.html" method="post">
      <jodd:form bean="lotteryAward" scope="request">
        <input name="id" type="hidden" />
        <input name="lotteryId" value="${lotteryId}" type="hidden">
        <table class="tableForm" width="100%">
			<tr>
				<th width="30%">奖项：</th>
				<td><input type="text" name="award" class="easyui-validatebox" data-options="required:true" validType="byteLength[1,64]"/></td>
			</tr>					
			<tr>
				<th>奖项说明：</th>
				<td><input type="text" name="awardNote" class="easyui-validatebox"  data-options="required:true" validType="byteLength[1,256]"/></td>
			</tr>					
			<tr>
				<th>权重：</th>
				<td><input type="text" name="weight" class="easyui-numberbox" data-options="required:true" validType="byteLength[1,10]"/></td>
			</tr>					
			<tr>
				<th>最大中奖数：</th>
				<td><input type="text" name="maxWiner" class="easyui-numberbox"  validType="byteLength[1,10]"/></td>
			</tr>
			<tr>
				<th>奖项位置：</th>
				<td><input type="text" name="awardPosition" class="easyui-validatebox" data-options="required:true" validType="byteLength[1,64]"/>
				 <div style="margin-top: 5px;">360度的圆，格式：开始度数,结束度数.如:60,100;339,23</div>
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
