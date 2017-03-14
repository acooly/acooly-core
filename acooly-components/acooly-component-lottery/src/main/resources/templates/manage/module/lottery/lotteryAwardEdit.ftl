<#assign jodd=JspTaglibs["http://www.springside.org.cn/jodd_form"] />
<div style="margin-top: 10px;">
    <form id="manage_lotteryAward_editform" action="${rc.contextPath}/manage/module/lottery/lotteryAward/<#if action == 'create'>save<#else>update</#if>Json.html" method="post">
      <@jodd.form bean="lotteryAward" scope="request">
        <input name="id" type="hidden" />
        <input name="lotteryId" value="${lotteryId}" type="hidden">
        <table class="tableForm" width="100%">
			<th width="25%">奖项类型：</th>
				<td>
					<select id="awardType" name="awardType" editable="false" panelHeight="auto"  >
						<#list allAwardTypes as k,v><option value="${k}" <#if lotteryAward?? && lotteryAward.awardType.code == k>selected</#if> >${v}</option></#list>
					</select>
				</td>
			</tr>
			<tr>
				<th width="30%">奖项：</th>
				<td><input type="text" class="text" name="award" class="easyui-validatebox" data-options="required:true" validType="byteLength[1,64]"/></td>
			</tr>
			<tr>
				<th width="30%">奖项金额：</th>
				<td><input type="text" class="text" name="awardValue" value="<#if lotteryAward?? && lotteryAward.awardType == 'money'>${lotteryAward.awardValue/100}<#else>${lotteryAward.awardValue}</#if>" class="easyui-numberbox" data-options="required:true" validType="byteLength[1,19]"/>
			</tr>
			<tr>
				<th>奖项说明：</th>
				<td><input type="text" size="256" class="text" style="width: 300px;" name="awardNote" class="easyui-validatebox"  data-options="required:true" validType="byteLength[1,256]"/></td>
			</tr>
			<tr>
				<th>权重：</th>
				<td><input type="text" class="text" name="weight" class="easyui-numberbox" data-options="required:true" validType="byteLength[1,10]"/>
                    <div style="margin-top: 5px;">中奖率 = 本奖项权重值 / 所有奖项权重值代数和</div>
                </td>
			</tr>
			<tr>
				<th>最大中奖数：</th>
				<td><select style="width: 80px;height: 26px;" id="lottery_award_edit_maxPeriod" onchange="manage_lotteryAward_editform_maxPeriodChange()" name="maxPeriod" editable="false" panelHeight="auto" class="easyui-combobox">
					<#list allMaxPeriods as k,v><option value="${k}" <#if lotteryAward?? && lotteryAward.maxPeriod.code == k>selected</#if> >${v}</option></#list>
					</select>
				 &nbsp;<input type="text" class="text" id="lottery_award_edit_maxWiner" name="maxWiner" class="easyui-numberbox"  validType="byteLength[1,10]"/>
                    <div style="margin-top: 5px;">0表示无限，定义每种(无限制，按天，按月等)周期的最大中奖数量。</div></td>
			</tr>
			<tr>
				<th>保持中奖记录</th>
				<td><select style="width: 80px;height: 26px;" name="recordWinner" editable="false" panelHeight="auto" class="easyui-combobox">
					<#list allRecordWinners as k,v><option value="${k}">${v}</option></#list>
					</select></td>
			</tr>
			<tr>
				<th>奖项位置：</th>
				<td><input type="text" class="text" name="awardPosition" class="easyui-validatebox" data-options="required:true" validType="byteLength[1,64]"/>
				 <div style="margin-top: 5px;">如果是转盘，则为360度的圆，格式：开始度数,结束度数.如:60,100;339,23</div>
				</td>
			</tr>
			<tr>
				<th>备注：</th>
				<td><textarea rows="3" cols="40" style="width: 300px;" name="comments" class="easyui-validatebox"  validType="byteLength[1,256]"></textarea></td>
			</tr>
        </table>
      </@jodd.form>
    </form>
    <script type="text/javascript">

	$(document).ready(function() {
		function manage_lotteryAward_editform_maxPeriodChange(v){
		  if(v == 'ulimit'){
		    $('#lottery_award_edit_maxWiner').attr("value",'0');
		    $('#lottery_award_edit_maxWiner').hide();
		  }else{
		    $('#lottery_award_edit_maxWiner').show();
		  }
		}

		$("#lottery_award_edit_maxPeriod").combobox({
			onChange: function (n,o) {
				console.info("onchange",n,o);
				manage_lotteryAward_editform_maxPeriodChange(n)
			}
		});
		var val = $("#lottery_award_edit_maxPeriod").val()
		manage_lotteryAward_editform_maxPeriodChange(val);
	});

    </script>
</div>
