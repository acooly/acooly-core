<script type="text/javascript">
$(function() {
	$.acooly.framework.registerKeydown('manage_app_searchform','manage_app_datagrid');
});

</script>
<div class="easyui-layout" data-options="fit : true,border : false">
  <!-- 查询条件 -->
  <div data-options="region:'north',border:false" style="padding:5px; overflow: hidden;" align="left">
    <form id="manage_app_searchform" onsubmit="return false">
      <table class="tableForm" width="100%">
        <tr>
          <td align="left">
          	<div>
					create_time: <input type="text" class="text" size="15" name="search_LIKE_createTime"/>
					update_time: <input type="text" class="text" size="15" name="search_LIKE_updateTime"/>
					raw_add_time: <input type="text" class="text" size="15" name="search_LIKE_rawAddTime"/>
					raw_update_time: <input type="text" class="text" size="15" name="search_LIKE_rawUpdateTime"/>
          	<a href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:false" onclick="$.acooly.framework.search('manage_app_searchform','manage_app_datagrid');"><i class="fa fa-search fa-lg fa-fw fa-col"></i>查询</a>
          	</div>
          </td>
        </tr>
      </table>
    </form>
  </div>

  <!-- 列表和工具栏 -->
  <div data-options="region:'center',border:false">
    <table id="manage_app_datagrid" class="easyui-datagrid" url="/manage/test/core/app/listJson.html" toolbar="#manage_app_toolbar" fit="true" border="false" fitColumns="false"
      pagination="true" idField="id" pageSize="20" pageList="[ 10, 20, 30, 40, 50 ]" sortName="id" sortOrder="desc" checkOnSelect="true" selectOnCheck="true" singleSelect="true">
      <thead>
        <tr>
        	<th field="showCheckboxWithId" checkbox="true" data-options="formatter:function(value, row, index){ return row.id }">编号</th>
			<th field="id" sortable="true" sum="true">id</th>
		    <th field="createTime" formatter="dateTimeFormatter">create_time</th>
		    <th field="updateTime" formatter="dateTimeFormatter">update_time</th>
			<th field="displayName" formatter="contentFormatter">display_name</th>
			<th field="name" formatter="contentFormatter">name</th>
			<th field="parentAppId" sortable="true" sum="true">parent_app_id</th>
			<th field="parentId" sortable="true" sum="true">parent_id</th>
		    <th field="rawAddTime" formatter="dateTimeFormatter">raw_add_time</th>
		    <th field="rawUpdateTime" formatter="dateTimeFormatter">raw_update_time</th>
			<th field="type" formatter="contentFormatter">type</th>
			<th field="userId" sortable="true" sum="true">user_id</th>
			<th field="price" sortable="true">price</th>
          	<th field="rowActions" data-options="formatter:function(value, row, index){return formatAction('manage_app_action',value,row)}">动作</th>
        </tr>
      </thead>
    </table>

    <!-- 每行的Action动作模板 -->
    <div id="manage_app_action" style="display: none;">
      <a onclick="$.acooly.framework.edit({url:'/manage/test/core/app/edit.html',id:'{0}',entity:'app',width:500,height:500});" href="#" title="编辑"><i class="fa fa-pencil fa-lg fa-fw fa-col"></i></a>
      <a onclick="$.acooly.framework.show('/manage/test/core/app/show.html?id={0}',500,500);" href="#" title="查看"><i class="fa fa-file-o fa-lg fa-fw fa-col"></i></a>
      <a onclick="$.acooly.framework.remove('/manage/test/core/app/deleteJson.html','{0}','manage_app_datagrid');" href="#" title="删除"><i class="fa fa-trash-o fa-lg fa-fw fa-col"></i></a>
    </div>

    <!-- 表格的工具栏 -->
    <div id="manage_app_toolbar">
      <a href="#" class="easyui-linkbutton" plain="true" onclick="$.acooly.framework.create({url:'/manage/test/core/app/create.html',entity:'app',width:500,height:500})"><i class="fa fa-plus-circle fa-lg fa-fw fa-col"></i>添加</a>
      <a href="#" class="easyui-linkbutton" plain="true" onclick="$.acooly.framework.removes('/manage/test/core/app/deleteJson.html','manage_app_datagrid')"><i class="fa fa-trash-o fa-lg fa-fw fa-col"></i>批量删除</a>
      <a href="#" class="easyui-menubutton" data-options="menu:'#manage_app_exports_menu'"><i class="fa fa-arrow-circle-o-down fa-lg fa-fw fa-col"></i>批量导出</a>
      <div id="manage_app_exports_menu" style="width:150px;">
        <div onclick="$.acooly.framework.exports('/manage/test/core/app/exportXls.html','manage_app_searchform','app')"><i class="fa fa-file-excel-o fa-lg fa-fw fa-col"></i>Excel</div>
        <div onclick="$.acooly.framework.exports('/manage/test/core/app/exportCsv.html','manage_app_searchform','app')"><i class="fa fa-file-text-o fa-lg fa-fw fa-col"></i>CSV</div>
      </div>
      <a href="#" class="easyui-linkbutton" plain="true" onclick="$.acooly.framework.imports({url:'/manage/test/core/app/importView.html',uploader:'manage_app_import_uploader_file'});"><i class="fa fa-arrow-circle-o-up fa-lg fa-fw fa-col"></i>批量导入</a>
    </div>
  </div>

</div>
