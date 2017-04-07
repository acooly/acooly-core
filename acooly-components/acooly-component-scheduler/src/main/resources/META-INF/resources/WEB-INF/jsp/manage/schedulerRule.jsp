<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/manage/common/taglibs.jsp"%>

<script type="text/javascript">
$(function() {
	$.acooly.framework.registerKeydown('manage_schedulerRule_searchform','manage_schedulerRule_datagrid');
});

</script>
<div class="easyui-layout" data-options="fit : true,border : false">
  <!-- 查询条件 -->
  <div data-options="region:'north',border:false" style="padding:5px; overflow: hidden;" align="left">
    <form id="manage_schedulerRule_searchform" onsubmit="return false">
      <table class="tableForm" width="100%">
        <tr>
          <td align="left">
          	<div>
					创建时间: <input size="15" class="text" id="search_GTE_createTime" name="search_GTE_createTime" onFocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd'})" />
					至<input size="15" class="text" id="search_LTE_createTime" name="search_LTE_createTime" onFocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd'})" />
					修改时间: <input size="15" class="text" id="search_GTE_updateTime" name="search_GTE_updateTime" onFocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd'})" />
					至<input size="15" class="text" id="search_LTE_updateTime" name="search_LTE_updateTime" onFocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd'})" />
					execute_num: <input type="text" class="text" size="15" name="search_EQ_executeNum"/>
					last_execute_time: <input size="15" class="text" id="search_GTE_lastExecuteTime" name="search_GTE_lastExecuteTime" onFocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd'})" />
					至<input size="15" class="text" id="search_LTE_lastExecuteTime" name="search_LTE_lastExecuteTime" onFocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd'})" />
					retry_time_on_exception: <input type="text" class="text" size="15" name="search_EQ_retryTimeOnException"/>
					validity_end: <input size="15" class="text" id="search_GTE_validityEnd" name="search_GTE_validityEnd" onFocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd'})" />
					至<input size="15" class="text" id="search_LTE_validityEnd" name="search_LTE_validityEnd" onFocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd'})" />
					validity_start: <input size="15" class="text" id="search_GTE_validityStart" name="search_GTE_validityStart" onFocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd'})" />
					至<input size="15" class="text" id="search_LTE_validityStart" name="search_LTE_validityStart" onFocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd'})" />
					是否删除: <input type="text" class="text" size="15" name="search_LIKE_isDel"/>
          	<a href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:false" onclick="$.acooly.framework.search('manage_schedulerRule_searchform','manage_schedulerRule_datagrid');"><i class="fa fa-search fa-lg fa-fw fa-col"></i>查询</a>
          	</div>
          </td>
        </tr>
      </table>
    </form>
  </div>

  <!-- 列表和工具栏 -->
  <div data-options="region:'center',border:false">
    <table id="manage_schedulerRule_datagrid" class="easyui-datagrid" url="${pageContext.request.contextPath}/manage/schedulerRule/listJson.html" toolbar="#manage_schedulerRule_toolbar" fit="true" border="false" fitColumns="false"
      pagination="true" idField="id" pageSize="20" pageList="[ 10, 20, 30, 40, 50 ]" sortName="id" sortOrder="desc" checkOnSelect="true" selectOnCheck="true" singleSelect="true">
      <thead>
        <tr>
        	<th field="showCheckboxWithId" checkbox="true" data-options="formatter:function(value, row, index){ return row.id }">编号</th>
			<th field="id" sum="true">id</th>
		    <th field="createTime" formatter="formatDate">创建时间</th>
		    <th field="updateTime" formatter="formatDate">修改时间</th>
			<th field="actionType">action_type</th>
			<th field="className">class_name</th>
			<th field="creater">creater</th>
			<th field="cronString">cron_string</th>
			<th field="exceptionAtLastExecute">exception_at_last_execute</th>
			<th field="executeNum" >execute_num</th>
		    <th field="lastExecuteTime" formatter="formatDate">last_execute_time</th>
			<th field="memo">memo</th>
			<th field="methodName">method_name</th>
			<th field="modifyer">modifyer</th>
			<th field="properties">properties</th>
			<th field="retryTimeOnException" >retry_time_on_exception</th>
			<th field="status">status</th>
		    <th field="validityEnd" formatter="formatDate">validity_end</th>
		    <th field="validityStart" formatter="formatDate">validity_start</th>
			<th field="isDel">是否删除</th>
          	<th field="rowActions" data-options="formatter:function(value, row, index){return formatAction('manage_schedulerRule_action',value,row)}">动作</th>
        </tr>
      </thead>
    </table>

    <!-- 每行的Action动作模板 -->
    <div id="manage_schedulerRule_action" style="display: none;">
      <a onclick="$.acooly.framework.edit({url:'/manage/schedulerRule/edit.html',id:'{0}',entity:'schedulerRule',width:500,height:400});" href="#" title="编辑"><i class="fa fa-pencil fa-lg fa-fw fa-col"></i></a>
      <a onclick="$.acooly.framework.show('/manage/schedulerRule/show.html?id={0}',500,400);" href="#" title="查看"><i class="fa fa-file-o fa-lg fa-fw fa-col"></i></a>
      <a onclick="$.acooly.framework.remove('/manage/schedulerRule/deleteJson.html','{0}','manage_schedulerRule_datagrid');" href="#" title="删除"><i class="fa fa-trash-o fa-lg fa-fw fa-col"></i></a>
    </div>

    <!-- 表格的工具栏 -->
    <div id="manage_schedulerRule_toolbar">
      <a href="#" class="easyui-linkbutton" plain="true" onclick="$.acooly.framework.create({url:'/manage/schedulerRule/create.html',entity:'schedulerRule',width:500,height:400})"><i class="fa fa-plus-circle fa-lg fa-fw fa-col"></i>添加</a>
      <a href="#" class="easyui-linkbutton" plain="true" onclick="$.acooly.framework.removes('/manage/schedulerRule/deleteJson.html','manage_schedulerRule_datagrid')"><i class="fa fa-trash-o fa-lg fa-fw fa-col"></i>批量删除</a>
      <a href="#" class="easyui-menubutton" data-options="menu:'#manage_schedulerRule_exports_menu'"><i class="fa fa-arrow-circle-o-down fa-lg fa-fw fa-col"></i>批量导出</a>
      <div id="manage_schedulerRule_exports_menu" style="width:150px;">
        <div onclick="$.acooly.framework.exports('/manage/schedulerRule/exportXls.html','manage_schedulerRule_searchform','scheduler_rule')"><i class="fa fa-file-excel-o fa-lg fa-fw fa-col"></i>Excel</div>
        <div onclick="$.acooly.framework.exports('/manage/schedulerRule/exportCsv.html','manage_schedulerRule_searchform','scheduler_rule')"><i class="fa fa-file-text-o fa-lg fa-fw fa-col"></i>CSV</div>
      </div>
      <a href="#" class="easyui-linkbutton" plain="true" onclick="$.acooly.framework.imports({url:'/manage/schedulerRule/importView.html',uploader:'manage_schedulerRule_import_uploader_file'});"><i class="fa fa-arrow-circle-o-up fa-lg fa-fw fa-col"></i>批量导入</a>
    </div>
  </div>

</div>
