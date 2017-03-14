<#assign jodd=JspTaglibs["http://www.springside.org.cn/jodd_form"] />
<div style="margin-top: 10px;">
	<form id="manage_lottery_editform" action="${rc.contextPath}/manage/module/lottery/lottery/<#if action == 'create'>save<#else>update</#if>Json.html" method="post">
		<@jodd.form bean="lottery" scope="request">
			<input name="id" type="hidden" />
			<table class="tableForm" width="100%">
				<tr>
					<th width="25%">标题：</th>
					<td><input type="text" class="text" name="title" style="width: 300px;" class="easyui-validatebox" data-options="required:true" validType="byteLength[1,64]"/></td>
				</tr>
				<tr>
					<th>说明：</th>
					<td><textarea rows="3" style="width: 300px;" cols="40" name="note" class="easyui-validatebox" validType="byteLength[1,255]"></textarea></td>
				</tr>
				<tr>
					<th>类型：</th>
					<td><select style="width: 80px;height: 28px;" name="type" editable="false" panelHeight="auto" class="easyui-combobox">
					<#list allTypes as key,v><option value="${key}">${v}</option></#list>
					</select></td>
				</tr>
				<tr>
					<th width="25%">活动最大人数：</th>
					<td><input type="text" class="text" name="maxWinners" style="width: 300px;" class="easyui-numberbox" validType="byteLength[1,10]" data-options="required:true"/>
						<div style="margin-top: 5px">0表示无限制。达到最大参与人数后，活动自动结束</div></td>
				</tr>
				<tr>
					<th width="25%">单人最大次数：</th>
					<td><input type="text" class="text" name="multiPlay" style="width: 300px;" class="easyui-numberbox" validType="byteLength[1,10]" data-options="required:true"/>
						<div style="margin-top: 5px">0表示无限制,每个用户最大可参与的次数定义。</div></td>
				</tr>
                <tr>
                    <th width="25%">用户参与计数：</th>
                    <td><select style="width: 80px;height: 28px;" name="userCounter" editable="false" panelHeight="auto" class="easyui-combobox">
                        <#list allUserCounters as key,v><option value="${key}">${v}</option></#list>
                    </select>
                        <div style="margin-top: 5px">如果开启，则表示每个参与抽奖的用户都必须在"用户抽奖计数"功能中设置明确次数。</div></td>
                </tr>

                <tr>
                    <th width="25%">发布事件：</th>
                    <td><select style="width: 80px;height: 28px;" name="publishEvent" editable="false" panelHeight="auto" class="easyui-combobox">
						<#list allUserCounters as key,v><option value="${key}">${v}</option></#list>
                    </select>
                        <div style="margin-top: 5px">如果开启，则表示每次中奖都发布通知事件(LotteryEvent)。</div></td>
                </tr>

				<tr>
					<th>活动周期：</th>
					<td><input type="text" class="text" name="startTime" size="15" value="<#if lottery??>${lottery.startTime?string("yyyy-MM-dd")}</#if>" data-options="required:true"/>
					到 <input type="text" class="text" name="endTime" size="15" value="<#if lottery??>${(lottery.endTime)?string("yyyy-MM-dd")}</#if>" data-options="required:true"/>
                        <div style="margin-top: 5px">活动到期后，活动自动结束。</div>
					</td>
				</tr>
				<tr>
					<th>备注：</th>
					<td><textarea rows="3" style="width: 300px;" cols="40" name="comments" class="easyui-validatebox" validType="byteLength[1,255]"></textarea></td>
				</tr>
			</table>
		</@jodd.form>
	</form>
</div>
