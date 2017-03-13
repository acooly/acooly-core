<script type="text/javascript">
$(function() {
	$.acooly.framework.registerKeydown('manage_lotteryWinner_searchform','manage_lotteryWinner_datagrid');
});
</script>
<div class="easyui-layout" data-options="fit : true,border : false">
  <!-- 查询条件 -->
  <div data-options="region:'north',border:false" style="padding:5px; overflow: hidden;" align="left">
    <form id="manage_lotteryWinner_searchform" onsubmit="return false">
      <table class="tableForm" width="100%">
        <tr>
          <td align="left">
					抽奖活动:<select name="search_EQ_lotteryId" style="height: 26px;" editable="false" panelHeight="auto" class="easyui-combobox"><option value="">所有</option>
					<#list allLotterys as e><option value="${e.id}">${e.title}</option></#list>
					</select>
                    类型:<select name="search_EQ_awardType" style="height: 26px;" editable="false" panelHeight="auto" class="easyui-combobox"><option value="">所有</option>
                    <#list allAwardTypes as key,val><option value="${key}">${val}</option></#list>
                  </select>
					中奖人:<input type="text" size="15" name="search_LIKE_winner" class="text"/>
					 抽奖时间:<input size="15" class="text" id="search_GTE_createTime" name="search_GTE_createTime"onFocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd HH:mm:ss'})" />
					至 <input size="15" class="text" id="search_LTE_createTime" name="search_LTE_createTime" onFocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd HH:mm:ss'})" />
					状态:<select name="search_EQ_status" style="height: 26px; width:80px;" editable="false" panelHeight="auto" class="easyui-combobox"><option value="">所有</option>
					<#list allStatuss as key,val><option value="${key}">${val}</option></#list>
					</select>
          	<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-search',plain:false" onclick="$.acooly.framework.search('manage_lotteryWinner_searchform','manage_lotteryWinner_datagrid');">查询</a>
          </td>
        </tr>
      </table>
    </form>
  </div>

  <!-- 列表和工具栏 -->
  <div data-options="region:'center',border:false">
    <table id="manage_lotteryWinner_datagrid" class="easyui-datagrid" url="${rc.contextPath}/manage/module/lottery/lotteryWinner/listJson.html" toolbar="#manage_lotteryWinner_toolbar" fit="true" border="false" fitColumns="true"
      pagination="true" idField="id" pageSize="20" pageList="[ 10, 20, 30, 40, 50 ]" sortName="id" sortOrder="desc" checkOnSelect="true" selectOnCheck="true">
      <thead>
        <tr>
        	<th field="showCheckboxWithId" checkbox="true" data-options="formatter:function(value, row, index){ return row.id }">编号</th>
			<th field="id">id</th>
			<th field="lotteryTitle">抽奖活动</th>
			<th field="winner">抽奖人</th>
			<th field="awardType" data-options="formatter:function(value){ return formatRefrence('manage_lotteryWinner_datagrid','allAwardTypes',value);}">奖项类型</th>
			<th field="award">奖项</th>
			<th field="awardAmount" data-options="formatter:function(value){return '￥'+formatCurrency(value/100);}"  sum="true">奖项金额</th>
		    <th field="createTime" formatter="formatDate">抽奖时间</th>
			<th field="status" data-options="formatter:function(value){ return formatRefrence('manage_lotteryWinner_datagrid','allStatuss',value);}">状态</th>
          	<th field="rowActions" data-options="formatter:function(value, row, index){return formatAction('manage_lotteryWinner_action',value,row)}">动作</th>
        </tr>
      </thead>
    </table>

    <!-- 每行的Action动作模板 -->
    <div id="manage_lotteryWinner_action" style="display: none;">
      <a class="line-action icon-edit" onclick="$.acooly.framework.edit({url:'/manage/module/lottery/lotteryWinner/edit.html',id:'{0}',entity:'lotteryWinner',width:500,height:300});" href="#" title="编辑"></a>
      <!--
      <a class="line-action icon-delete" onclick="$.acooly.framework.remove('/manage/module/lottery/lotteryWinner/deleteJson.html','{0}','manage_lotteryWinner_datagrid');" href="#" title="删除"></a>
	  -->
    </div>

    <!-- 表格的工具栏 -->
    <div id="manage_lotteryWinner_toolbar">
      <a href="#" class="easyui-menubutton" data-options="menu:'#manage_lotteryWinner_exports_menu',iconCls:'icon-export'">批量导出</a>
      <div id="manage_lotteryWinner_exports_menu" style="width:150px;">
        <div data-options="iconCls:'icon-excel'" onclick="$.acooly.framework.exports('/manage/module/lottery/lotteryWinner/exportXls.html','manage_lotteryWinner_searchform','lottery_winner')">Excel</div>
        <div data-options="iconCls:'icon-csv'" onclick="$.acooly.framework.exports('/manage/module/lottery/lotteryWinner/exportCsv.html','manage_lotteryWinner_searchform','lottery_winner')">CSV</div>
      </div>
    </div>
  </div>

</div>
