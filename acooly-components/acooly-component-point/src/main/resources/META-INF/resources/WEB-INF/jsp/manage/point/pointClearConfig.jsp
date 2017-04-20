<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/manage/common/taglibs.jsp"%>

<script type="text/javascript">
$(function() {
	$.acooly.framework.registerKeydown('manage_pointClearConfig_searchform','manage_pointClearConfig_datagrid');
});


/**
**动作操作
*/
function manage_pointClearConfig_rowAction_formatter(value,row,index){
	var actionHtml = "";
	if(row.status == 'init'){
		actionHtml += formatString($('#manage_pointClearConfig_action').html(), row.id);
	}
	return actionHtml;
}

</script>
<div class="easyui-layout" data-options="fit : true,border : false">
  <!-- 查询条件 -->
  <div data-options="region:'north',border:false" style="padding:5px; overflow: hidden;" align="left">
    <form id="manage_pointClearConfig_searchform" onsubmit="return false">
      <table class="tableForm" width="100%">
        <tr>
          <td align="left">
          	<div>
					开始交易时间: <input size="8" class="text" id="search_GTE_startTradeTime" name="search_GTE_startTradeTime" onFocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd'})" />
					至<input size="8" class="text" id="search_LTE_startTradeTime" name="search_LTE_startTradeTime" onFocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd'})" />
					结束交易时间: <input size="8" class="text" id="search_GTE_endTradeTime" name="search_GTE_endTradeTime" onFocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd'})" />
					至<input size="8" class="text" id="search_LTE_endTradeTime" name="search_LTE_endTradeTime" onFocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd'})" />
					<br/>
					开始清理时间: <input size="8" class="text" id="search_GTE_startClearTime" name="search_GTE_startClearTime" onFocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd'})" />
					至<input size="8" class="text" id="search_LTE_startClearTime" name="search_LTE_startClearTime" onFocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd'})" />
					结束清理时间: <input size="8" class="text" id="search_GTE_endClearTime" name="search_GTE_endClearTime" onFocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd'})" />
					至<input size="8" class="text" id="search_LTE_endClearTime" name="search_LTE_endClearTime" onFocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd'})" />
					清零时间: <input size="8" class="text" id="search_GTE_clearTime" name="search_GTE_clearTime" onFocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd'})" />
					至<input size="8" class="text" id="search_LTE_clearTime" name="search_LTE_clearTime" onFocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd'})" />
					状态: <select style="width:80px;height:27px;" name="search_EQ_status" editable="false" panelHeight="auto" class="easyui-combobox"><option value="">所有</option><c:forEach var="e" items="${allStatuss}"><option value="${e.key}" ${param.search_EQ_status == e.key?'selected':''}>${e.value}</option></c:forEach></select>
          	<a href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:false" onclick="$.acooly.framework.search('manage_pointClearConfig_searchform','manage_pointClearConfig_datagrid');"><i class="fa fa-search fa-lg fa-fw fa-col"></i>查询</a>
          	</div>
          </td>
        </tr>
      </table>
    </form>
  </div>

  <!-- 列表和工具栏 -->
  <div data-options="region:'center',border:false">
    <table id="manage_pointClearConfig_datagrid" class="easyui-datagrid" url="${pageContext.request.contextPath}/manage/point/pointClearConfig/listJson.html" toolbar="#manage_pointClearConfig_toolbar" fit="true" border="false" fitColumns="false"
      pagination="true" idField="id" pageSize="20" pageList="[ 10, 20, 30, 40, 50 ]" sortName="id" sortOrder="desc" checkOnSelect="true" selectOnCheck="true" singleSelect="true">
      <thead>
        <tr>
        	<th field="showCheckboxWithId" checkbox="true" data-options="formatter:function(value, row, index){ return row.id }">编号</th>
			<th field="id" sum="true">ID</th>
		    <th field="startTradeTime" formatter="formatDate">开始交易时间</th>
		    <th field="endTradeTime" formatter="formatDate">结束交易时间</th>
		    <th field="startClearTime" formatter="formatDate">开始清理时间</th>
		    <th field="endClearTime" formatter="formatDate">结束清理时间</th>
		    <th field="clearTime" formatter="formatDate">清零时间</th>
			<th field="status" data-options="formatter:function(value){ return formatRefrence('manage_pointClearConfig_datagrid','allStatuss',value);} ">状态</th>
		    <th field="createTime" formatter="formatDate">创建时间</th>
		    <th field="updateTime" formatter="formatDate">修改时间</th>
			<th field="memo">备注</th>
			<th field="rowActions" formatter="manage_pointClearConfig_rowAction_formatter">动作</th>
        </tr>
      </thead>
    </table>
    
    <!-- 每行的Action动作模板 -->
    <div id="manage_pointClearConfig_action" style="display: none;">
      <a onclick="$.acooly.framework.confirmSubmit('/manage/point/pointClearConfig/pointClear.html','{0}','manage_pointClearConfig_datagrid','积分清零','此操作影响用户积分,请再次确认是否执行《积分清零》?');" href="#" title="积分清零"><i class="fa fa-history fa-lg fa-fw fa-col"></i></a>
      <a onclick="$.acooly.framework.edit({url:'/manage/point/pointClearConfig/edit.html',id:'{0}',entity:'pointClearConfig',width:500,height:400});" href="#" title="编辑"><i class="fa fa-pencil fa-lg fa-fw fa-col"></i></a>
      <a onclick="$.acooly.framework.show('/manage/point/pointClearConfig/show.html?id={0}',500,400);" href="#" title="查看"><i class="fa fa-file-o fa-lg fa-fw fa-col"></i></a>
      <a onclick="$.acooly.framework.remove('/manage/point/pointClearConfig/deleteJson.html','{0}','manage_pointClearConfig_datagrid');" href="#" title="删除"><i class="fa fa-trash-o fa-lg fa-fw fa-col"></i></a>
    </div>

    <!-- 表格的工具栏 -->
    <div id="manage_pointClearConfig_toolbar">
      <a href="#" class="easyui-linkbutton" plain="true" onclick="$.acooly.framework.create({url:'/manage/point/pointClearConfig/create.html',entity:'pointClearConfig',width:500,height:400})"><i class="fa fa-plus-circle fa-lg fa-fw fa-col"></i>添加</a>
      <a href="#" class="easyui-linkbutton" plain="true" onclick="$.acooly.framework.removes('/manage/point/pointClearConfig/deleteJson.html','manage_pointClearConfig_datagrid')"><i class="fa fa-trash-o fa-lg fa-fw fa-col"></i>批量删除</a>
      <a href="#" class="easyui-menubutton" data-options="menu:'#manage_pointClearConfig_exports_menu'"><i class="fa fa-arrow-circle-o-down fa-lg fa-fw fa-col"></i>批量导出</a>
      <div id="manage_pointClearConfig_exports_menu" style="width:80px;">
        <div onclick="$.acooly.framework.exports('/manage/point/pointClearConfig/exportXls.html','manage_pointClearConfig_searchform','积分清零设置')"><i class="fa fa-file-excel-o fa-lg fa-fw fa-col"></i>Excel</div>
        <div onclick="$.acooly.framework.exports('/manage/point/pointClearConfig/exportCsv.html','manage_pointClearConfig_searchform','积分清零设置')"><i class="fa fa-file-text-o fa-lg fa-fw fa-col"></i>CSV</div>
      </div>
      <a href="#" class="easyui-linkbutton" plain="true" onclick="$.acooly.framework.imports({url:'/manage/point/pointClearConfig/importView.html',uploader:'manage_pointClearConfig_import_uploader_file'});"><i class="fa fa-arrow-circle-o-up fa-lg fa-fw fa-col"></i>批量导入</a>
    </div>
  </div>

</div>
