<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/manage/common/taglibs.jsp"%>

<script type="text/javascript">
$(function() {
	$.acooly.framework.registerKeydown('manage_actionLog_searchform','manage_actionLog_datagrid');
});

</script>
<div class="easyui-layout" data-options="fit : true,border : false">
  <!-- 查询条件 -->
  <div data-options="region:'north',border:false" style="padding:5px; overflow: hidden;" align="left">
    <form id="manage_actionLog_searchform" onsubmit="return false">
      <table class="tableForm" width="100%">
        <tr>
          <td align="left">
          	<div>
					操作: <input type="text" class="text" size="15" name="search_LIKE_actionKey"/>
					操作名称: <input type="text" class="text" size="15" name="search_LIKE_actionName"/>
					用户名: <input type="text" class="text" size="15" name="search_LIKE_userName"/>
				渠道: <select style="width:80px;height:27px;" name="search_EQ_channel" editable="false" panelHeight="auto" class="easyui-combobox"><option value="">所有</option><c:forEach var="e" items="${allChannels}"><option value="${e.key}" ${param.search_EQ_channel == e.key?'selected':''}>${e.value}</option></c:forEach></select>
					渠道版本: <input type="text" class="text" size="15" name="search_LIKE_channelVersion"/>
					访问IP: <input type="text" class="text" size="15" name="search_LIKE_userIp"/>
					创建时间: <input size="15" class="text" id="search_GTE_createTime" name="search_GTE_createTime" onFocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd'})" />
					至<input size="15" class="text" id="search_LTE_createTime" name="search_LTE_createTime" onFocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd'})" />
					更新时间: <input size="15" class="text" id="search_GTE_updateTime" name="search_GTE_updateTime" onFocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd'})" />
					至<input size="15" class="text" id="search_LTE_updateTime" name="search_LTE_updateTime" onFocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd'})" />
          	<a href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:false" onclick="$.acooly.framework.search('manage_actionLog_searchform','manage_actionLog_datagrid');"><i class="fa fa-search fa-lg fa-fw fa-col"></i>查询</a>
          	</div>
          </td>
        </tr>
      </table>
    </form>
  </div>

  <!-- 列表和工具栏 -->
  <div data-options="region:'center',border:false">
    <table id="manage_actionLog_datagrid" class="easyui-datagrid" url="${pageContext.request.contextPath}/manage/module/portlet/actionLog/listJson.html" toolbar="#manage_actionLog_toolbar" fit="true" border="false" fitColumns="false"
      pagination="true" idField="id" pageSize="20" pageList="[ 10, 20, 30, 40, 50 ]" sortName="id" sortOrder="desc" checkOnSelect="true" selectOnCheck="true" singleSelect="true">
      <thead>
        <tr>
        	<th field="showCheckboxWithId" checkbox="true" data-options="formatter:function(value, row, index){ return row.id }">编号</th>
			<th field="id" sum="true">ID</th>
			<th field="actionKey">操作</th>
			<th field="actionName">操作名称</th>
			<th field="actionUrl">URL连接</th>
			<th field="userName">用户名</th>
			<th field="channel" data-options="formatter:function(value){ return formatRefrence('manage_actionLog_datagrid','allChannels',value);} ">渠道</th>
			<th field="channelInfo">渠道信息</th>
			<th field="channelVersion">渠道版本</th>
			<th field="userIp">访问IP</th>
		    <th field="createTime" formatter="formatDate">创建时间</th>
		    <th field="updateTime" formatter="formatDate">更新时间</th>
			<th field="comments">备注</th>
          	<th field="rowActions" data-options="formatter:function(value, row, index){return formatAction('manage_actionLog_action',value,row)}">动作</th>
        </tr>
      </thead>
    </table>

    <!-- 每行的Action动作模板 -->
    <div id="manage_actionLog_action" style="display: none;">
      <a onclick="$.acooly.framework.edit({url:'/manage/module/portlet/actionLog/edit.html',id:'{0}',entity:'actionLog',width:500,height:400});" href="#" title="编辑"><i class="fa fa-pencil fa-lg fa-fw fa-col"></i></a>
      <a onclick="$.acooly.framework.show('/manage/module/portlet/actionLog/show.html?id={0}',500,400);" href="#" title="查看"><i class="fa fa-file-o fa-lg fa-fw fa-col"></i></a>
      <a onclick="$.acooly.framework.remove('/manage/module/portlet/actionLog/deleteJson.html','{0}','manage_actionLog_datagrid');" href="#" title="删除"><i class="fa fa-trash-o fa-lg fa-fw fa-col"></i></a>
    </div>

    <!-- 表格的工具栏 -->
    <div id="manage_actionLog_toolbar">
      <a href="#" class="easyui-linkbutton" plain="true" onclick="$.acooly.framework.create({url:'/manage/module/portlet/actionLog/create.html',entity:'actionLog',width:500,height:400})"><i class="fa fa-plus-circle fa-lg fa-fw fa-col"></i>添加</a>
      <a href="#" class="easyui-linkbutton" plain="true" onclick="$.acooly.framework.removes('/manage/module/portlet/actionLog/deleteJson.html','manage_actionLog_datagrid')"><i class="fa fa-trash-o fa-lg fa-fw fa-col"></i>批量删除</a>
      <a href="#" class="easyui-menubutton" data-options="menu:'#manage_actionLog_exports_menu'"><i class="fa fa-arrow-circle-o-down fa-lg fa-fw fa-col"></i>批量导出</a>
      <div id="manage_actionLog_exports_menu" style="width:150px;">
        <div onclick="$.acooly.framework.exports('/manage/module/portlet/actionLog/exportXls.html','manage_actionLog_searchform','portlet_action_log')"><i class="fa fa-file-excel-o fa-lg fa-fw fa-col"></i>Excel</div>
        <div onclick="$.acooly.framework.exports('/manage/module/portlet/actionLog/exportCsv.html','manage_actionLog_searchform','portlet_action_log')"><i class="fa fa-file-text-o fa-lg fa-fw fa-col"></i>CSV</div>
      </div>
      <a href="#" class="easyui-linkbutton" plain="true" onclick="$.acooly.framework.imports({url:'/manage/module/portlet/actionLog/importView.html',uploader:'manage_actionLog_import_uploader_file'});"><i class="fa fa-arrow-circle-o-up fa-lg fa-fw fa-col"></i>批量导入</a>
    </div>
  </div>

</div>