<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/manage/common/taglibs.jsp"%>


<script type="text/javascript">
$(function() {
	$.acooly.framework.registerKeydown('manage_lotteryWhitelist_searchform','manage_lotteryWhitelist_datagrid');
});
</script>
<div class="easyui-layout" data-options="fit : true,border : false">
  <!-- 查询条件 -->
  <div data-options="region:'north',border:false" style="padding:5px; overflow: hidden;" align="left">
    <form id="manage_lotteryWhitelist_searchform" onsubmit="return false">
      <table class="tableForm" width="100%">
        <tr>
          <td align="left">
					抽奖ID:<input type="text" size="15" name="search_EQ_lotteryId" value="${param.search_EQ_lotteryId}"  />
					奖项ID:<input type="text" size="15" name="search_EQ_awardId" value="${param.search_EQ_awardId}"  />
					抽奖用户:<input type="text" size="15" name="search_LIKE_user" value="${param.search_LIKE_user}"  />
					创建时间:<input size="15" id="search_GTE_createTime" name="search_GTE_createTime" datePicker({readOnly:true,dateFmt:'yyyy-MM-dd'})" />
					至<input size="15" id="search_LTE_createTime" name="search_LTE_createTime" onFocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd'})" /> 			
					修改时间:<input size="15" id="search_GTE_updateTime" name="search_GTE_updateTime" datePicker({readOnly:true,dateFmt:'yyyy-MM-dd'})" />
					至<input size="15" id="search_LTE_updateTime" name="search_LTE_updateTime" onFocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd'})" /> 			
				状态:<select style="width:80px;" name="search_EQ_status" editable="false" panelHeight="auto" class="easyui-combobox"><option value="">所有</option><c:forEach var="e" items="${allStatuss}"><option value="${e.key}" ${param.search_EQ_status == e.key?'selected':''}>${e.value}</option></c:forEach></select>
					备注:<input type="text" size="15" name="search_LIKE_comments" value="${param.search_LIKE_comments}"  />
          	<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-search',plain:false" onclick="$.acooly.framework.search('manage_lotteryWhitelist_searchform','manage_lotteryWhitelist_datagrid');">查询</a> 
          </td>
        </tr>
      </table>
    </form>
  </div>

  <!-- 列表和工具栏 -->
  <div data-options="region:'center',border:false">
    <table id="manage_lotteryWhitelist_datagrid" class="easyui-datagrid" url="${pageContext.request.contextPath}/manage/module/lottery/lotteryWhitelist/listJson.html" toolbar="#manage_lotteryWhitelist_toolbar" fit="true" border="false" fitColumns="false"
      pagination="true" idField="id" pageSize="20" pageList="[ 10, 20, 30, 40, 50 ]" sortName="id" sortOrder="desc" checkOnSelect="true" selectOnCheck="true" singleSelect="true">
      <thead>
        <tr>
        	<th field="showCheckboxWithId" checkbox="true" data-options="formatter:function(value, row, index){ return row.id }">编号</th>
			<th field="id">ID</th>
			<th field="lotteryId">抽奖ID</th>
			<th field="awardId">奖项ID</th>
			<th field="user">抽奖用户</th>
		    <th field="createTime" formatter="formatDate">创建时间</th>
		    <th field="updateTime" formatter="formatDate">修改时间</th>
			<th field="status" data-options="formatter:function(value){ return formatRefrence('manage_lotteryWhitelist_datagrid','allStatuss',value);} ">状态</th>
			<th field="comments">备注</th>
          	<th field="rowActions" data-options="formatter:function(value, row, index){return formatAction('manage_lotteryWhitelist_action',value,row)}">动作</th>
        </tr>
      </thead>
    </table>
    
    <!-- 每行的Action动作模板 -->
    <div id="manage_lotteryWhitelist_action" style="display: none;">
      <a class="line-action icon-edit" onclick="$.acooly.framework.edit({url:'/manage/module/lottery/lotteryWhitelist/edit.html',id:'{0}',entity:'lotteryWhitelist',width:500,height:400});" href="#" title="编辑"></a>
      <a class="line-action icon-show" onclick="$.acooly.framework.show('/manage/module/lottery/lotteryWhitelist/show.html?id={0}',500,400);" href="#" title="查看"></a>
      <a class="line-action icon-delete" onclick="$.acooly.framework.remove('/manage/module/lottery/lotteryWhitelist/deleteJson.html','{0}','manage_lotteryWhitelist_datagrid');" href="#" title="删除"></a>
    </div>
    
    <!-- 表格的工具栏 -->
    <div id="manage_lotteryWhitelist_toolbar">
      <a href="#" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="$.acooly.framework.create({url:'/manage/module/lottery/lotteryWhitelist/create.html',entity:'lotteryWhitelist',width:500,height:400})">添加</a> 
      <a href="#" class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="$.acooly.framework.removes('/manage/module/lottery/lotteryWhitelist/deleteJson.html','manage_lotteryWhitelist_datagrid')">批量删除</a>
      <a href="#" class="easyui-menubutton" data-options="menu:'#manage_lotteryWhitelist_exports_menu',iconCls:'icon-export'">批量导出</a>
      <div id="manage_lotteryWhitelist_exports_menu" style="width:150px;">
        <div data-options="iconCls:'icon-excel'" onclick="$.acooly.framework.exports('/manage/module/lottery/lotteryWhitelist/exportXls.html','manage_lotteryWhitelist_searchform','lottery_whitelist')">Excel</div>  
        <div data-options="iconCls:'icon-csv'" onclick="$.acooly.framework.exports('/manage/module/lottery/lotteryWhitelist/exportCsv.html','manage_lotteryWhitelist_searchform','lottery_whitelist')">CSV</div> 
      </div>
      <a href="#" class="easyui-linkbutton" iconCls="icon-import" plain="true" onclick="$.acooly.framework.imports({url:'/manage/module/lottery/lotteryWhitelist/importView.html',uploader:'manage_lotteryWhitelist_import_uploader_file'});">批量导入</a> 
    </div>
  </div>

</div>
