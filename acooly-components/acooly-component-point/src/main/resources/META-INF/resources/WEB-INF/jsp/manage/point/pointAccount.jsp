<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/manage/common/taglibs.jsp"%>

<script type="text/javascript">
$(function() {
	$.acooly.framework.registerKeydown('manage_pointAccount_searchform','manage_pointAccount_datagrid');
});



</script>
<div class="easyui-layout" data-options="fit : true,border : false">
  <!-- 查询条件 -->
  <div data-options="region:'north',border:false" style="padding:5px; overflow: hidden;" align="left">
    <form id="manage_pointAccount_searchform" onsubmit="return false">
      <table class="tableForm" width="100%">
        <tr>
          <td align="left">
          	<div>
					用户名: <input type="text" class="text" size="15" name="search_EQ_userName"/>
<%-- 					状态: <select style="width:80px;height:27px;" name="search_EQ_status" editable="false" panelHeight="auto" class="easyui-combobox"><option value="">所有</option><c:forEach var="e" items="${allStatuss}"><option value="${e.key}" ${param.search_EQ_status == e.key?'selected':''}>${e.value}</option></c:forEach></select> --%>
					用户等级: <select style="width:80px;height:27px;" name="search_EQ_gradeId" editable="false" panelHeight="auto" class="easyui-combobox"><option value="">所有</option><c:forEach var="e" items="${allPointGrades}"><option value="${e.key}" ${param.search_EQ_gradeId == e.key?'selected':''}>${e.value}</option></c:forEach></select>
					创建时间: <input size="15" class="text" id="search_GTE_createTime" name="search_GTE_createTime" onFocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd'})" />
					至<input size="15" class="text" id="search_LTE_createTime" name="search_LTE_createTime" onFocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd'})" />
          	<a href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:false" onclick="$.acooly.framework.search('manage_pointAccount_searchform','manage_pointAccount_datagrid');"><i class="fa fa-search fa-lg fa-fw fa-col"></i>查询</a>
          	</div>
          </td>
        </tr>
      </table>
    </form>
  </div>

  <!-- 列表和工具栏 -->
  <div data-options="region:'center',border:false">
    <table id="manage_pointAccount_datagrid" class="easyui-datagrid" url="${pageContext.request.contextPath}/manage/point/pointAccount/listJson.html" toolbar="#manage_pointAccount_toolbar" fit="true" border="false" fitColumns="false"
      pagination="true" idField="id" pageSize="20" pageList="[ 10, 20, 30, 40, 50 ]" sortName="id" sortOrder="desc" checkOnSelect="true" selectOnCheck="true" singleSelect="true">
      <thead>
        <tr>
        	<th field="showCheckboxWithId" checkbox="true" data-options="formatter:function(value, row, index){ return row.id }">编号</th>
			<th field="id" sum="true">ID</th>
			<th field="userName">用户名</th>
			<th field="balance" sum="true">积分余额</th>
			<th field="freeze" sum="true">冻结积分</th>
			<th field="available" sum="true">有效积分</th>
			<th field="totalExpensePoint" sum="true">总消费积分</th>
			<th field="totalProducePoint" sum="true">总产生积分</th>
			<th field="status" data-options="formatter:function(value){ return formatRefrence('manage_pointAccount_datagrid','allStatuss',value);} ">状态</th>
			<th field="gradeId" data-options="formatter:function(value){ return formatRefrence('manage_pointAccount_datagrid','allPointGrades',value);} ">用户等级</th>
		    <th field="createTime" formatter="formatDate">创建时间</th>
		    <th field="updateTime" formatter="formatDate">修改时间</th>
			<th field="memo">备注</th>
          	<th field="rowActions" data-options="formatter:function(value, row, index){return formatAction('manage_pointAccount_action',value,row)}">动作</th>
        </tr>
      </thead>
    </table>

    <!-- 每行的Action动作模板 -->
    <div id="manage_pointAccount_action" style="display: none;">
<!--       <a onclick="$.acooly.framework.edit({url:'/manage/point/pointAccount/edit.html',id:'{0}',entity:'pointAccount',width:500,height:400});" href="#" title="编辑"><i class="fa fa-pencil fa-lg fa-fw fa-col"></i></a> -->
      <a onclick="$.acooly.framework.show('/manage/point/pointAccount/show.html?id={0}',500,400);" href="#" title="查看"><i class="fa fa-file-o fa-lg fa-fw fa-col"></i></a>
<!--       <a onclick="$.acooly.framework.remove('/manage/point/pointAccount/deleteJson.html','{0}','manage_pointAccount_datagrid');" href="#" title="删除"><i class="fa fa-trash-o fa-lg fa-fw fa-col"></i></a> -->
    </div>

    <!-- 表格的工具栏 -->
    <div id="manage_pointAccount_toolbar">
<!--       <a href="#" class="easyui-linkbutton" plain="true" onclick="$.acooly.framework.create({url:'/manage/point/pointAccount/create.html',entity:'pointAccount',width:500,height:400})"><i class="fa fa-plus-circle fa-lg fa-fw fa-col"></i>添加</a> -->
<!--       <a href="#" class="easyui-linkbutton" plain="true" onclick="$.acooly.framework.removes('/manage/point/pointAccount/deleteJson.html','manage_pointAccount_datagrid')"><i class="fa fa-trash-o fa-lg fa-fw fa-col"></i>批量删除</a> -->
<!--       <a href="#" class="easyui-menubutton" data-options="menu:'#manage_pointAccount_exports_menu'"><i class="fa fa-arrow-circle-o-down fa-lg fa-fw fa-col"></i>批量导出</a> -->
<!--       <div id="manage_pointAccount_exports_menu" style="width:150px;"> -->
<!--         <div onclick="$.acooly.framework.exports('/manage/point/pointAccount/exportXls.html','manage_pointAccount_searchform','积分账户')"><i class="fa fa-file-excel-o fa-lg fa-fw fa-col"></i>Excel</div> -->
<!--         <div onclick="$.acooly.framework.exports('/manage/point/pointAccount/exportCsv.html','manage_pointAccount_searchform','积分账户')"><i class="fa fa-file-text-o fa-lg fa-fw fa-col"></i>CSV</div> -->
<!--       </div> -->
      <a href="#" class="easyui-linkbutton" plain="true" onclick="$.acooly.framework.create({url:'/manage/point/pointAccount/grant.html',entity:'pointAccount',width:500,height:400,addButton:'积分发放',reload:true})"><i class="fa fa-plus-circle fa-lg fa-location-arrow fa-col"></i>积分发放</a>
      <a href="#" class="easyui-linkbutton" plain="true" onclick="$.acooly.framework.imports({url:'/manage/point/pointAccount/importView.html',uploader:'manage_pointAccount_import_uploader_file',width:500,height:400,});"><i class="fa fa-arrow-circle-o-up fa-lg fa-fw fa-col"></i>导入文件发放</a>
<!--       <a href="#" class="easyui-linkbutton" plain="true" onclick="$.acooly.framework.create({url:'/manage/point/pointAccount/clear.html',entity:'pointAccount',width:500,height:400,addButton:'积分清零',reload:true})"><i class="fa fa-plus-circle fa-lg fa-arrow-up fa-col"></i>积分清零</a> -->
    </div>
  </div>

</div>
