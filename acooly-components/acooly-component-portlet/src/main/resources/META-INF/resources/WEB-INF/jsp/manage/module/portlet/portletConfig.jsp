<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/manage/common/taglibs.jsp"%>

<script type="text/javascript">
$(function() {
	$.acooly.framework.registerKeydown('manage_portletConfig_searchform','manage_portletConfig_datagrid');
});

</script>
<div class="easyui-layout" data-options="fit : true,border : false">
  <!-- 查询条件 -->
  <div data-options="region:'north',border:false" style="padding:5px; overflow: hidden;" align="left">
    <form id="manage_portletConfig_searchform" onsubmit="return false">
      <table class="tableForm" width="100%">
        <tr>
          <td align="left">
          	<div>
				类型: <select style="width:80px;height:27px;" name="search_EQ_type" editable="false" panelHeight="auto" class="easyui-combobox"><option value="">所有</option><c:forEach var="e" items="${allTypes}"><option value="${e.key}" ${param.search_EQ_type == e.key?'selected':''}>${e.value}</option></c:forEach></select>
					标题: <input type="text" class="text" size="15" name="search_LIKE_title"/>
					参数键: <input type="text" class="text" size="15" name="search_LIKE_key"/>
					create_time: <input size="15" class="text" id="search_GTE_createTime" name="search_GTE_createTime" onFocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd'})" />
					至<input size="15" class="text" id="search_LTE_createTime" name="search_LTE_createTime" onFocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd'})" />
					update_time: <input size="15" class="text" id="search_GTE_updateTime" name="search_GTE_updateTime" onFocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd'})" />
					至<input size="15" class="text" id="search_LTE_updateTime" name="search_LTE_updateTime" onFocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd'})" />
          	<a href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:false" onclick="$.acooly.framework.search('manage_portletConfig_searchform','manage_portletConfig_datagrid');"><i class="fa fa-search fa-lg fa-fw fa-col"></i>查询</a>
          	</div>
          </td>
        </tr>
      </table>
    </form>
  </div>

  <!-- 列表和工具栏 -->
  <div data-options="region:'center',border:false">
    <table id="manage_portletConfig_datagrid" class="easyui-datagrid" url="${pageContext.request.contextPath}/manage/module/portlet/portletConfig/listJson.html" toolbar="#manage_portletConfig_toolbar" fit="true" border="false" fitColumns="false"
      pagination="true" idField="id" pageSize="20" pageList="[ 10, 20, 30, 40, 50 ]" sortName="id" sortOrder="desc" checkOnSelect="true" selectOnCheck="true" singleSelect="true">
      <thead>
        <tr>
        	<th field="showCheckboxWithId" checkbox="true" data-options="formatter:function(value, row, index){ return row.id }">编号</th>
			<th field="id" sum="true">主键</th>
			<th field="type" data-options="formatter:function(value){ return formatRefrence('manage_portletConfig_datagrid','allTypes',value);} ">类型</th>
			<th field="title">标题</th>
			<th field="key">参数键</th>
			<th field="value">参数值</th>
			<th field="comments">备注</th>
		    <th field="createTime" formatter="formatDate">create_time</th>
		    <th field="updateTime" formatter="formatDate">update_time</th>
          	<th field="rowActions" data-options="formatter:function(value, row, index){return formatAction('manage_portletConfig_action',value,row)}">动作</th>
        </tr>
      </thead>
    </table>

    <!-- 每行的Action动作模板 -->
    <div id="manage_portletConfig_action" style="display: none;">
      <a onclick="$.acooly.framework.edit({url:'/manage/module/portlet/portletConfig/edit.html',id:'{0}',entity:'portletConfig',width:500,height:400});" href="#" title="编辑"><i class="fa fa-pencil fa-lg fa-fw fa-col"></i></a>
      <a onclick="$.acooly.framework.show('/manage/module/portlet/portletConfig/show.html?id={0}',500,400);" href="#" title="查看"><i class="fa fa-file-o fa-lg fa-fw fa-col"></i></a>
      <a onclick="$.acooly.framework.remove('/manage/module/portlet/portletConfig/deleteJson.html','{0}','manage_portletConfig_datagrid');" href="#" title="删除"><i class="fa fa-trash-o fa-lg fa-fw fa-col"></i></a>
    </div>

    <!-- 表格的工具栏 -->
    <div id="manage_portletConfig_toolbar">
      <a href="#" class="easyui-linkbutton" plain="true" onclick="$.acooly.framework.create({url:'/manage/module/portlet/portletConfig/create.html',entity:'portletConfig',width:500,height:400})"><i class="fa fa-plus-circle fa-lg fa-fw fa-col"></i>添加</a>
      <a href="#" class="easyui-linkbutton" plain="true" onclick="$.acooly.framework.removes('/manage/module/portlet/portletConfig/deleteJson.html','manage_portletConfig_datagrid')"><i class="fa fa-trash-o fa-lg fa-fw fa-col"></i>批量删除</a>
      <a href="#" class="easyui-menubutton" data-options="menu:'#manage_portletConfig_exports_menu'"><i class="fa fa-arrow-circle-o-down fa-lg fa-fw fa-col"></i>批量导出</a>
      <div id="manage_portletConfig_exports_menu" style="width:150px;">
        <div onclick="$.acooly.framework.exports('/manage/module/portlet/portletConfig/exportXls.html','manage_portletConfig_searchform','p_portlet_config')"><i class="fa fa-file-excel-o fa-lg fa-fw fa-col"></i>Excel</div>
        <div onclick="$.acooly.framework.exports('/manage/module/portlet/portletConfig/exportCsv.html','manage_portletConfig_searchform','p_portlet_config')"><i class="fa fa-file-text-o fa-lg fa-fw fa-col"></i>CSV</div>
      </div>
      <a href="#" class="easyui-linkbutton" plain="true" onclick="$.acooly.framework.imports({url:'/manage/module/portlet/portletConfig/importView.html',uploader:'manage_portletConfig_import_uploader_file'});"><i class="fa fa-arrow-circle-o-up fa-lg fa-fw fa-col"></i>批量导入</a>
    </div>
  </div>

</div>
