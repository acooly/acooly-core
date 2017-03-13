<#assign jodd=JspTaglibs["http://www.springside.org.cn/jodd_form"] /> 
<div>
    <form id="manage_lotteryWhitelist_editform" action="${rc.contextPath}/manage/module/lottery/lotteryWhitelist/<#if action == 'create'>save<#else>update</#if>Json.html" method="post">
      <@jodd.form bean="lotteryWhitelist" scope="request">
        <input name="id" type="hidden" />
        <table class="tableForm" width="100%">
			<tr>
				<th width="25%">抽奖活动：</th>
				<td>
                    <select name="lotteryId" style="height: 26px;" editable="false" panelHeight="auto" class="easyui-combobox">
                    <#list allLotterys as e><option value="${e.id}">${e.title}</option></#list>
                    </select>
                </td>
			</tr>					
			<tr>
				<th>奖项ID：</th>
				<td><input type="text" class="text" name="awardId" class="easyui-numberbox" data-options="required:true" validType="byteLength[1,19]"/>
                    <div style="margin-top: 5px">请从"抽奖活动"模块的奖项管理获取奖项的ID</div>
                </td>
			</tr>					
			<tr>
				<th>抽奖用户：</th>
				<td><input type="text" class="text" name="user" class="easyui-validatebox" data-options="required:true" validType="byteLength[1,64]"/></td>
			</tr>
          <#if action != 'create'>
			<tr>
				<th>状态：</th>
				<td>${lotteryWhitelist.status.message}</td>
			</tr>
          </#if>
			<tr>
				<th>备注：</th>
				<td><input type="text" class="text" style="width: 260px;" name="comments" class="easyui-validatebox"  validType="byteLength[1,128]"/></td>
			</tr>					
        </table>
      </@jodd.form>
    </form>
</div>
