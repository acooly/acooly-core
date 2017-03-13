<script type="text/javascript">
$(function() {
	$.acooly.framework.registerKeydown('manage_lotteryCount_searchform','manage_lotteryCount_datagrid');
});
</script>
<div class="easyui-layout" data-options="fit : true,border : false">
  <!-- 查询条件 -->
  <div data-options="region:'north',border:false" style="padding:5px; overflow: hidden;" align="left">
    <form id="manage_lotteryCount_searchform" onsubmit="return false">
      <table class="tableForm" width="100%">
        <tr>
          <td align="left">
					关键字:<input type="text" size="15" name="search_EQ_ukey" />
          	<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-search',plain:false" onclick="$.acooly.framework.search('manage_lotteryCount_searchform','manage_lotteryCount_datagrid');">查询</a> 
          </td>
        </tr>
      </table>
    </form>
  </div>

  <!-- 列表和工具栏 -->
  <div data-options="region:'center',border:false">
    <table id="manage_lotteryCount_datagrid" class="easyui-datagrid" url="${rc.contextPath}/manage/module/lottery/lotteryCount/listJson.html" toolbar="" fit="true" border="false" fitColumns="true"
      pagination="true" idField="id" pageSize="20" pageList="[ 10, 20, 30, 40, 50 ]" sortName="id" sortOrder="desc" checkOnSelect="true" selectOnCheck="true">
      <thead>
        <tr>
        	<th field="showCheckboxWithId" checkbox="true" data-options="formatter:function(value, row, index){ return row.id }">编号</th>
			<th field="id">ID</th>
			<th field="ukey">关键字</th>
			<th field="count">计数值</th>
			<th field="lotteryId">抽奖ID</th>
			<th field="awardId">奖项ID</th>
		    <th field="createTime" formatter="formatDate">创建时间</th>
		    <th field="updateTime" formatter="formatDate">更新时间</th>
			<th field="comment">备注</th>
        </tr>
      </thead>
    </table>
    
    <!-- 每行的Action动作模板 -->
    <div id="manage_lotteryCount_action" style="display: none;">
      <a class="line-action icon-edit" onclick="$.acooly.framework.edit({url:'/manage/module/lottery/lotteryCount/edit.html',id:'{0}',entity:'lotteryCount',width:500,height:400});" href="#" title="编辑"></a>
      <a class="line-action icon-show" onclick="$.acooly.framework.show('/manage/module/lottery/lotteryCount/show.html?id={0}',500,400);" href="#" title="查看"></a>
      <a class="line-action icon-delete" onclick="$.acooly.framework.remove('/manage/module/lottery/lotteryCount/deleteJson.html','{0}','manage_lotteryCount_datagrid');" href="#" title="删除"></a>
    </div>
    
    <!-- 表格的工具栏 -->
    <div id="manage_lotteryCount_toolbar">
      <a href="#" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="$.acooly.framework.create({url:'/manage/module/lottery/lotteryCount/create.html',entity:'lotteryCount',width:500,height:400})">添加</a> 
      <a href="#" class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="$.acooly.framework.removes('/manage/module/lottery/lotteryCount/deleteJson.html','manage_lotteryCount_datagrid')">批量删除</a>
      <a href="#" class="easyui-menubutton" data-options="menu:'#manage_lotteryCount_exports_menu',iconCls:'icon-export'">批量导出</a>
      <div id="manage_lotteryCount_exports_menu" style="width:150px;">
        <div data-options="iconCls:'icon-excel'" onclick="$.acooly.framework.exports('/manage/module/lottery/lotteryCount/exportXls.html','manage_lotteryCount_searchform','lottery_count')">Excel</div>  
        <div data-options="iconCls:'icon-csv'" onclick="$.acooly.framework.exports('/manage/module/lottery/lotteryCount/exportCsv.html','manage_lotteryCount_searchform','lottery_count')">CSV</div> 
      </div>
      <a href="#" class="easyui-linkbutton" iconCls="icon-import" plain="true" onclick="$.acooly.framework.imports({url:'/manage/module/lottery/lotteryCount/importView.html',uploader:'manage_lotteryCount_import_uploader_file'});">批量导入</a> 
    </div>
  </div>

</div>
