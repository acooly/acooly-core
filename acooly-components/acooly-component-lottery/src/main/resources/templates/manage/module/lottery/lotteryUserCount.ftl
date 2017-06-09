<#if ssoEnable>
    <#include "*/include.ftl">
</#if>
<script type="text/javascript">
$(function() {
	$.acooly.framework.registerKeydown('manage_lotteryUserCount_searchform','manage_lotteryUserCount_datagrid');
});
</script>
<div class="easyui-layout" data-options="fit : true,border : false">
  <!-- 查询条件 -->
  <div data-options="region:'north',border:false" style="padding:5px; overflow: hidden;" align="left">
    <form id="manage_lotteryUserCount_searchform" onsubmit="return false">
      <table class="tableForm" width="100%">
        <tr>
          <td align="left">
					活动ID:<input type="text" size="10" name="search_EQ_lotteryid" />
					活动标题:<input type="text" size="15" name="search_LIKE_lotteryTitle" />
					参与人:<input type="text" size="15" name="search_LIKE_user" />
          	<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-search',plain:false" onclick="$.acooly.framework.search('manage_lotteryUserCount_searchform','manage_lotteryUserCount_datagrid');">查询</a> 
          </td>
        </tr>
      </table>
    </form>
  </div>

  <!-- 列表和工具栏 -->
  <div data-options="region:'center',border:false">
    <table id="manage_lotteryUserCount_datagrid" class="easyui-datagrid" url="${pageContext.request.contextPath}/manage/module/lottery/lotteryUserCount/listJson.html" toolbar="#manage_lotteryUserCount_toolbar" fit="true" border="false" fitColumns="true"
      pagination="true" idField="id" pageSize="20" pageList="[ 10, 20, 30, 40, 50 ]" sortName="id" sortOrder="desc" checkOnSelect="true" selectOnCheck="true">
      <thead>
        <tr>
        	<th field="showCheckboxWithId" checkbox="true" data-options="formatter:function(value, row, index){ return row.id }">编号</th>
			<th field="id">ID</th>
			<th field="lotteryId">活动ID</th>
			<th field="lotteryTitle">活动标题</th>
			<th field="user">参与人</th>
			<th field="totalTimes">获参次数</th>
			<th field="playTimes">已参与次数</th>
			<th field="availTimes" data-options="formatter:function(value,row,index){ return (row.totalTimes - row.playTimes) }">可参与次数</th>
		    <th field="createTime" formatter="formatDate">创建时间</th>
		    <th field="updateTime" formatter="formatDate">最后修改时间</th>
			<th field="comments">备注</th>
          	<!--<th field="rowActions" data-options="formatter:function(value, row, index){return formatAction('manage_lotteryUserCount_action',value,row)}">动作</th>-->
        </tr>
      </thead>
    </table>
    
    <!-- 每行的Action动作模板 -->
    <div id="manage_lotteryUserCount_action" style="display: none;">
      <a class="line-action icon-edit" onclick="$.acooly.framework.edit({url:'/manage/module/lottery/lotteryUserCount/edit.html',id:'{0}',entity:'lotteryUserCount',width:500,height:400});" href="#" title="编辑"></a>
      <a class="line-action icon-delete" onclick="$.acooly.framework.remove('/manage/module/lottery/lotteryUserCount/deleteJson.html','{0}','manage_lotteryUserCount_datagrid');" href="#" title="删除"></a>
    </div>
    
    <!-- 表格的工具栏 -->
    <div id="manage_lotteryUserCount_toolbar">
      <!--
      <a href="#" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="$.acooly.framework.create({url:'/manage/module/lottery/lotteryUserCount/create.html',entity:'lotteryUserCount',width:500,height:400})">添加</a> 
      <a href="#" class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="$.acooly.framework.removes('/manage/module/lottery/lotteryUserCount/deleteJson.html','manage_lotteryUserCount_datagrid')">批量删除</a>
      <a href="#" class="easyui-linkbutton" iconCls="icon-import" plain="true" onclick="$.acooly.framework.imports({url:'/manage/module/lottery/lotteryUserCount/importView.html',uploader:'manage_lotteryUserCount_import_uploader_file'});">批量导入</a> 
      -->
      <a href="#" class="easyui-menubutton" data-options="menu:'#manage_lotteryUserCount_exports_menu',iconCls:'icon-export'">批量导出</a>
      <div id="manage_lotteryUserCount_exports_menu" style="width:150px;">
        <div data-options="iconCls:'icon-excel'" onclick="$.acooly.framework.exports('/manage/module/lottery/lotteryUserCount/exportXls.html','manage_lotteryUserCount_searchform','lottery_user_count')">Excel</div>  
        <div data-options="iconCls:'icon-csv'" onclick="$.acooly.framework.exports('/manage/module/lottery/lotteryUserCount/exportCsv.html','manage_lotteryUserCount_searchform','lottery_user_count')">CSV</div> 
      </div>
    </div>
  </div>

</div>
