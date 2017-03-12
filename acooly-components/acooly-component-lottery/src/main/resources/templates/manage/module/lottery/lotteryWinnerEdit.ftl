<#assign jodd=JspTaglibs["http://www.springside.org.cn/jodd_form"] />
<div>
    <form id="manage_lotteryWinner_editform" action="${rc.contextPath}/manage/module/lottery/lotteryWinner/<#if action == 'create'>save<#else>update</#if>Json.html" method="post">
      <@jodd.form bean="lotteryWinner" scope="request">
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
				<td>${lotteryWinner.createTime?string('yyyy-MM-dd HH:mm:ss')}</td>
			</tr>					
			<tr>
				<th>状态：</th>
				<td>
				<select name="status" editable="false" panelHeight="auto" class="easyui-combobox">
					<#list allStatuss as key,val><option value="${key}" <#if allStatuss?? && allStatuss.status.code == key>selected</#if> >${val}</option></#list>
				</select>
				</td>
			</tr>					
			<tr>
				<th>备注：</th>
				<td><textarea rows="3" cols="40" name="comments" class="easyui-validatebox"  validType="byteLength[1,256]"></textarea></td>
			</tr>					
        </table>
      </@jodd.form>
    </form>
</div>
