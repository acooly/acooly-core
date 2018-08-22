<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/manage/common/taglibs.jsp"%>
<c:if test="${initParam['ssoEnable']=='true'}">
    <%@ include file="/WEB-INF/jsp/manage/common/ssoInclude.jsp" %>
</c:if>
<script type="text/javascript">
$(function() {
	$.acooly.framework.registerKeydown('manage_eavAttribute_searchform','manage_eavAttribute_datagrid');
});

</script>
<div class="easyui-layout" data-options="fit : true,border : false">
  <!-- 查询条件 -->
  <div data-options="region:'north',border:false" style="padding:5px; overflow: hidden;" align="left">
    <form id="manage_eavAttribute_searchform" onsubmit="return false">
      <table class="tableForm" width="100%">
        <tr>
          <td align="left">
          	<div>
					方案id: <input type="text" class="text" size="15" name="search_EQ_schemaId"/>
          	<a href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:false" onclick="$.acooly.framework.search('manage_eavAttribute_searchform','manage_eavAttribute_datagrid');"><i class="fa fa-search fa-lg fa-fw fa-col"></i>查询</a>
          	</div>
          </td>
        </tr>
      </table>
    </form>
  </div>

  <!-- 列表和工具栏 -->
  <div data-options="region:'center',border:false">
    <table id="manage_eavAttribute_datagrid" class="easyui-datagrid" url="${pageContext.request.contextPath}/manage/module/eav/eavAttribute/listJson.html" toolbar="#manage_eavAttribute_toolbar" fit="true" border="false" fitColumns="false"
      pagination="true" idField="id" pageSize="20" pageList="[ 10, 20, 30, 40, 50 ]" sortName="id" sortOrder="desc" checkOnSelect="true" selectOnCheck="true" singleSelect="true">
      <thead>
        <tr>
        	<th field="showCheckboxWithId" checkbox="true" data-options="formatter:function(value, row, index){ return row.id }">编号</th>
			<th field="id" sum="true">id</th>
			<th field="schemaId" sum="true">方案id</th>
			<th field="name">属性名称</th>
			<th field="displayName">展示名称</th>
			<th field="memo">备注</th>
			<th field="nullable">是否可以为空</th>
			<th field="minimum" sum="true">最小值</th>
			<th field="maximum" sum="true">最大值</th>
			<th field="minLength" >最小长度</th>
			<th field="maxLength" >最大长度</th>
			<th field="regex">正则表达式</th>
			<th field="enumValue">枚举值</th>
			<th field="attributeType" formatter="mappingFormatter">属性类型</th>
		    <th field="createTime" formatter="dateTimeFormatter">创建时间</th>
		    <th field="updateTime" formatter="dateTimeFormatter">修改时间</th>
          	<th field="rowActions" data-options="formatter:function(value, row, index){return formatAction('manage_eavAttribute_action',value,row)}">动作</th>
        </tr>
      </thead>
    </table>

    <!-- 每行的Action动作模板 -->
    <div id="manage_eavAttribute_action" style="display: none;">
      <a onclick="$.acooly.framework.edit({url:'/manage/module/eav/eavAttribute/edit.html',id:'{0}',entity:'eavAttribute',width:500,height:400});" href="#" title="编辑"><i class="fa fa-pencil fa-lg fa-fw fa-col"></i></a>
      <a onclick="$.acooly.framework.show('/manage/module/eav/eavAttribute/show.html?id={0}',500,400);" href="#" title="查看"><i class="fa fa-file-o fa-lg fa-fw fa-col"></i></a>
      <a onclick="$.acooly.framework.remove('/manage/module/eav/eavAttribute/deleteJson.html','{0}','manage_eavAttribute_datagrid');" href="#" title="删除"><i class="fa fa-trash-o fa-lg fa-fw fa-col"></i></a>
    </div>

    <!-- 表格的工具栏 -->
    <div id="manage_eavAttribute_toolbar">
      <a href="#" class="easyui-linkbutton" plain="true" onclick="$.acooly.framework.create({url:'/manage/module/eav/eavAttribute/create.html',entity:'eavAttribute',width:500,height:400})"><i class="fa fa-plus-circle fa-lg fa-fw fa-col"></i>添加</a>
      <a href="#" class="easyui-linkbutton" plain="true" onclick="$.acooly.framework.removes('/manage/module/eav/eavAttribute/deleteJson.html','manage_eavAttribute_datagrid')"><i class="fa fa-trash-o fa-lg fa-fw fa-col"></i>批量删除</a>
      <a href="#" class="easyui-menubutton" data-options="menu:'#manage_eavAttribute_exports_menu'"><i class="fa fa-arrow-circle-o-down fa-lg fa-fw fa-col"></i>批量导出</a>
      <div id="manage_eavAttribute_exports_menu" style="width:150px;">
        <div onclick="$.acooly.framework.exports('/manage/module/eav/eavAttribute/exportXls.html','manage_eavAttribute_searchform','eav_attribute')"><i class="fa fa-file-excel-o fa-lg fa-fw fa-col"></i>Excel</div>
        <div onclick="$.acooly.framework.exports('/manage/module/eav/eavAttribute/exportCsv.html','manage_eavAttribute_searchform','eav_attribute')"><i class="fa fa-file-text-o fa-lg fa-fw fa-col"></i>CSV</div>
      </div>
      <a href="#" class="easyui-linkbutton" plain="true" onclick="$.acooly.framework.imports({url:'/manage/module/eav/eavAttribute/importView.html',uploader:'manage_eavAttribute_import_uploader_file'});"><i class="fa fa-arrow-circle-o-up fa-lg fa-fw fa-col"></i>批量导入</a>
    </div>
  </div>

</div>