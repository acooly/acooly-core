<#assign jodd=JspTaglibs["http://www.springside.org.cn/jodd_form"] />
<div>
    <form id="manage_app_editform" action="/manage/test/core/app/<#if action=='create'>saveJson<#else>updateJson</#if>.html" method="post">
		<@jodd.form bean="app" scope="request">
        <input name="id" type="hidden" />
        <table class="tableForm" width="100%">
			<tr>
				<th>display_name：</th>
				<td><textarea rows="3" cols="40" placeholder="请输入display_name..." name="displayName" class="easyui-validatebox" data-options="validType:['length[1,255]']"></textarea></td>
			</tr>					
			<tr>
				<th>name：</th>
				<td><textarea rows="3" cols="40" placeholder="请输入name..." name="name" class="easyui-validatebox" data-options="validType:['length[1,255]']"></textarea></td>
			</tr>					
			<tr>
				<th>parent_app_id：</th>
				<td><input type="text" name="parentAppId" placeholder="请输入parent_app_id..." class="easyui-numberbox" style="height: 30px;width: 260px;line-height: 1.3em;" data-options="validType:['number[0,2147483646]']"/></td>
			</tr>					
			<tr>
				<th>parent_id：</th>
				<td><input type="text" name="parentId" placeholder="请输入parent_id..." class="easyui-numberbox" style="height: 30px;width: 260px;line-height: 1.3em;" data-options="validType:['number[0,2147483646]']"/></td>
			</tr>					
			<tr>
				<th>raw_add_time：</th>
				<td><input type="text" name="rawAddTime" placeholder="请输入raw_add_time..." class="easyui-validatebox" value="<#if app.rawAddTime??>${app.rawAddTime?string('yyyy-MM-dd HH:mm:ss')}</#if>" onFocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd HH:mm:ss'})"  /></td>
			</tr>					
			<tr>
				<th>raw_update_time：</th>
				<td><input type="text" name="rawUpdateTime" placeholder="请输入raw_update_time..." class="easyui-validatebox" value="<#if app.rawUpdateTime??>${app.rawUpdateTime?string('yyyy-MM-dd HH:mm:ss')}</#if>" onFocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd HH:mm:ss'})"  /></td>
			</tr>					
			<tr>
				<th>type：</th>
				<td><textarea rows="3" cols="40" placeholder="请输入type..." name="type" class="easyui-validatebox" data-options="validType:['length[1,255]']"></textarea></td>
			</tr>					
			<tr>
				<th>user_id：</th>
				<td><input type="text" name="userId" placeholder="请输入user_id..." class="easyui-numberbox" style="height: 30px;width: 260px;line-height: 1.3em;" data-options="validType:['number[0,2147483646]']"/></td>
			</tr>					
			<tr>
				<th>price：</th>
				<td><input type="text" name="price" placeholder="请输入price..." class="easyui-numberbox" style="height: 30px;width: 260px;line-height: 1.3em;" data-options="validType:['number[0,999999999]']"/></td>
			</tr>					
        </table>
      </@jodd.form>
    </form>
</div>
