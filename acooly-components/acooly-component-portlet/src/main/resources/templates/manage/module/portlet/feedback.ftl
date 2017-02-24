<script type="text/javascript">
$(function() {
	$.acooly.framework.registerKeydown('manage_feedback_searchform','manage_feedback_datagrid');
});
</script>
<div class="easyui-layout" data-options="fit : true,border : false">
  <!-- 查询条件 -->
  <div data-options="region:'north',border:false" style="padding:5px; overflow: hidden;" align="left">
    <form id="manage_feedback_searchform" onsubmit="return false">
      <table class="tableForm" width="100%">
        <tr>
          <td align="left">
					类型:<input type="text" size="15" name="search_LIKE_type" />
					标题:<input type="text" size="15" name="search_LIKE_title" />
					日期:<input size="15" id="search_GTE_createTime" name="search_GTE_createTime" size="15" onFocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd HH:mm:ss'})" />
					至<input size="15" id="search_LTE_createTime" name="search_LTE_createTime" size="15" onFocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd HH:mm:ss'})" /> 			
          	<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-search',plain:false" onclick="$.acooly.framework.search('manage_feedback_searchform','manage_feedback_datagrid');">查询</a>
          </td>
        </tr>
      </table>
    </form>
  </div>

  <!-- 列表和工具栏 -->
  <div data-options="region:'center',border:false">
    <table id="manage_feedback_datagrid" class="easyui-datagrid" url="${pageContext.request.contextPath}/manage/module/portlet/feedback/listJson.html" toolbar="#manage_feedback_toolbar" fit="true" border="false" fitColumns="true"
      pagination="true" idField="id" pageSize="20" pageList="[ 10, 20, 30, 40, 50 ]" sortName="id" sortOrder="desc" checkOnSelect="true" selectOnCheck="true">
      <thead>
        <tr>
        	<th field="showCheckboxWithId" checkbox="true" data-options="formatter:function(value, row, index){ return row.id }">编号</th>
			<th field="id">id</th>
			<th field="type">类型</th>
			<th field="title">标题</th>
			<th field="content">内容</th>
			<th field="userName">用户名</th>
			<th field="telephone">联系电话</th>
			<th field="address">联系地址</th>
			<th field="contactInfo">联系信息</th>
		    <th field="createTime" formatter="formatDate">日期</th>
          	<th field="rowActions" data-options="formatter:function(value, row, index){return formatAction('manage_feedback_action',value,row)}">动作</th>
        </tr>
      </thead>
    </table>
    
    <!-- 每行的Action动作模板 -->
    <div id="manage_feedback_action" style="display: none;">
      <a class="line-action icon-show" onclick="$.acooly.framework.show('/manage/module/portlet/feedback/show.html?id={0}',500,400);" href="#" title="查看"></a>&nbsp
    </div>
    
    <!-- 表格的工具栏 -->
    <div id="manage_feedback_toolbar">
      <a href="#" class="easyui-menubutton" data-options="menu:'#manage_feedback_exports_menu',iconCls:'icon-export'">批量导出</a>
      <div id="manage_feedback_exports_menu" style="width:150px;">
        <div data-options="iconCls:'icon-excel'" onclick="$.acooly.framework.exports('/manage/module/portlet/feedback/exportXls.html','manage_feedback_searchform','客户反馈')">Excel</div>
        <div data-options="iconCls:'icon-csv'" onclick="$.acooly.framework.exports('/manage/module/portlet/feedback/exportCsv.html','manage_feedback_searchform','客户反馈')">CSV</div>
      </div>
    </div>
  </div>

</div>
