<#assign jodd=JspTaglibs["http://www.springside.org.cn/jodd_form"] />
<script type="text/javascript">
	function manage_user_editform_onSubmit() {
	<#if action == 'create'>
		var isValid = $(this).form('validate');
		if (!isValid) {
			return false;
		}
		//自定义检查合法性
		if ($('#manage_user_passwd').val() != $('#manage_user_confirmPasswd').val()) {
			$.messager.alert('提示', '两次密码输入不一致');
			return false;
		}
		return true;
	<#else>
		return $(this).form('validate');
	</#if>
	}
</script>

<div align="center">
	<form id="manage_user_editform" action="${rc.contextPath}/manage/system/user/<#if action == 'create'>save<#else>update</#if>Json.html" method="post">
		<@jodd.form bean="user" scope="request">
			<input name="id" type="hidden" />
			<table class="tableForm">
				<tr>
					<th style="width: 80px;">登录名称</th>
					<td>
						<#if action == 'create'>
						<input name="username" type="text" class="easyui-validatebox text" data-options="required:true" style="width:200px" />
						<#else>${user.username}</#if>
					</td>
				</tr>
				<#if action == 'create'>
					<tr>
						<th>密码</th>
						<td><input name="password" id="manage_user_passwd" type="password" validType="commonRegExp['${PASSWORD_REGEX}','${PASSWORD_ERROR}']" class="easyui-validatebox text" data-options="required:true" style="width: 200px;" /></td>
					</tr>
					<tr>
						<th>确认密码</th>
						<td><input name="confirmPasswd" id="manage_user_confirmPasswd" validType="equals['#manage_user_passwd']" type="password" class="easyui-validatebox text" data-options="required:true" style="width: 200px;" /></td>
					</tr>
				</#if>
				<tr>
					<th>真实姓名</th>
					<td><input name="realName" type="text" class="easyui-validatebox text" data-options="required:true" style="width:200px"/></td>
				</tr>
				<tr>
					<th>E-Mail</th>
					<td><input name="email" type="text" class="easyui-validatebox text" validType="email" data-options="required:true" style="width: 200px;" /></td>
				</tr>
				<tr>
					<th>手机号码</th>
					<td><input name="mobileNo" type="text" class="easyui-validatebox text" style="width: 200px;" /></td>
				</tr>
				<tr>
					<th>类型</th>
					<td><select name="userType" panelHeight="auto" style="width: 200px;height:27px;" class="easyui-combobox">
						<#list allUserTypes.keySet() as key><option value="${key}">${allUserTypes.get(key)}</option></#list>
					</select></td>
				</tr>
				<tr>
					<th>状态</th>
					<td><select name="status" panelHeight="auto" style="width: 200px;height:27px;" class="easyui-combobox">
							<#list allStatus.keySet() as key><option value="${key}">${allStatus.get(key)}</option></#list>
					</select></td>
				</tr>
				<tr>
					<th>所属角色</th>
					<td><select name="role" panelHeight="auto" style="width: 200px;height:27px;" class="easyui-combobox">
							<#list allRoles as e><option <#if user.roles?contains(e)>selected</#if> value="${e.id}">${e.name}</option></#list>
					</select></td>
				</tr>
				<tr>
					<th>备注</th>
					<td><textarea name="descn" rows="3" cols="40" class="easyui-validatebox"></textarea></td>
				</tr>
			</table>
		</@jodd.form>
	</form>
</div>