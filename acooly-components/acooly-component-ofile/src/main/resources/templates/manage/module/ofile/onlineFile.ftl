<script type="text/javascript">
$(function() {
	$.acooly.framework.registerKeydown('manage_onlineFile_searchform','manage_onlineFile_datagrid');
});

function formatThumbnail(value,row){
	return '<a class="manage_onlineFile_datagrid_Thumbnail" target="_blank" href="/media'+row.filePath+'" title="'+row.fileName+'"><img src="/media'+value+'" width="32" height="32" /></a>';
}

</script>
<div class="easyui-layout" data-options="fit : true,border : false">
  <!-- 查询条件 -->
  <div data-options="region:'north',border:false" style="padding:5px; overflow: hidden;" align="left">
    <form id="manage_onlineFile_searchform" onsubmit="return false">
      <table class="tableForm" width="100%">
        <tr>
          <td align="left">
					文件名:<input type="text" size="15" name="search_LIKE_fileName"/>
					文件类型:<select name="search_EQ_fileType" editable="false" style="width: 80px;" panelHeight="auto" class="easyui-combobox"><option value="">所有</option>
						<#list ofileTypes.keySet() as key>
							<option value="${key}">${ofileTypes.get(key)}</option>
						</#list>					
					</select>
					文件名:<input type="text" size="15" name="search_LIKE_originalName" />
					上传时间:<input id="search_GTE_createTime" name="search_GTE_createTime" size="10" onFocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd'})" />
					至<input id="search_LTE_createTime" name="search_LTE_createTime" size="10" onFocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd'})" /> 					
          	<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-search',plain:false" onclick="$.acooly.framework.search('manage_onlineFile_searchform','manage_onlineFile_datagrid');">查询</a> 
          </td>
        </tr>
      </table>
    </form>
  </div>

  <!-- 列表和工具栏 -->
  <div data-options="region:'center',border:false">
    <table id="manage_onlineFile_datagrid" class="easyui-datagrid" url="${rc.contextPath}/manage/module/ofile/onlineFile/listJson.html" toolbar="#manage_onlineFile_toolbar" fit="true" border="false" fitColumns="false"
      pagination="true" idField="id" pageSize="20" pageList="[ 10, 20, 30, 40, 50 ]" sortName="id" sortOrder="desc" checkOnSelect="true" selectOnCheck="true">
      <thead>
        <tr>
        	<th field="showCheckboxWithId" checkbox="true" data-options="formatter:function(value, row, index){ return row.id }">编号</th>
			<th field="id">id</th>
			<th field="thumbnail" data-options="formatter:function(value,row,index){ if(row.fileType == 'picture'){ return formatThumbnail(value,row); }else{ return '' } }">缩略图</th>
			<th field="fileName">文件名</th>
			<th field="originalName">原文件名</th>
			<th field="inputName">表单名</th>
			<th field="fileType">类型</th>
			<th field="fileSize" formatter="formatFileSize">大小</th>
			<th field="filePath" data-options="formatter:function(value,row,index){ return '<a target=\'_blank\' href=\'/ofile/image/'+row.id+'.html\'>' + value + '</a>';}">路径</th>
			<th field="userName">用户名</th>
			<th field="createTime">创建时间</th>
          	<th field="rowActions" data-options="formatter:function(value, row, index){return formatAction('manage_onlineFile_action',value,row)}">动作</th>
        </tr>
      </thead>
    </table>
    
    <!-- 每行的Action动作模板 -->
    <div id="manage_onlineFile_action" style="display: none;">
      <a class="line-action icon-show" onclick="$.acooly.framework.show('/manage/module/ofile/onlineFile/show.html?id={0}',600,620);" href="#" title="查看"></a>
      <a class="line-action icon-delete" onclick="$.acooly.framework.remove('/manage/module/ofile/onlineFile/deleteJson.html','{0}','manage_onlineFile_datagrid');" href="#" title="删除"></a>
    </div>
    <!-- 表格的工具栏 -->
    <div id="manage_onlineFile_toolbar">
      <a href="#" class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="$.acooly.framework.removes('/manage/module/ofile/onlineFile/deleteJson.html','manage_onlineFile_datagrid')">批量删除</a>
    </div>
  </div>
</div>
<script type="text/javascript">
	$(function() {
		$(".manage_onlineFile_datagrid_Thumbnail").fancybox();	
	});
</script> 
=======
<script type="text/javascript">
$(function() {
	$.acooly.framework.registerKeydown('manage_onlineFile_searchform','manage_onlineFile_datagrid');
});
</script>
<div class="easyui-layout" data-options="fit : true,border : false">
  <!-- 查询条件 -->
  <div data-options="region:'north',border:false" style="padding:5px; overflow: hidden;" align="left">
    <form id="manage_onlineFile_searchform" onsubmit="return false">
      <table class="tableForm" width="100%">
        <tr>
          <td align="left">
					文件名:<input type="text" size="15" name="search_LIKE_fileName" />
					文件类型:<select name="search_EQ_fileType" editable="false" style="width: 80px;" panelHeight="auto" class="easyui-combobox"><option value="">所有</option><c:forEach var="e" items="${ofileTypes}"><option value="${e.key}" >${e.value}</option></c:forEach></select>
					文件名:<input type="text" size="15" name="search_LIKE_originalName" />
					上传时间:<input id="search_GTE_createTime" name="search_GTE_createTime" size="10" onFocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd HH:mm:ss'})" />
					至<input id="search_LTE_createTime" name="search_LTE_createTime" size="10" onFocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd HH:mm:ss'})" /> 					
          	<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-search',plain:false" onclick="$.acooly.framework.search('manage_onlineFile_searchform','manage_onlineFile_datagrid');">查询</a> 
          </td>
        </tr>
      </table>
    </form>
  </div>

  <!-- 列表和工具栏 -->
  <div data-options="region:'center',border:false">
    <table id="manage_onlineFile_datagrid" class="easyui-datagrid" url="${rc.contextPath}/manage/module/ofile/onlineFile/listJson.html" toolbar="#manage_onlineFile_toolbar" fit="true" border="false" fitColumns="false"
      pagination="true" idField="id" pageSize="20" pageList="[ 10, 20, 30, 40, 50 ]" sortName="id" sortOrder="desc" checkOnSelect="true" selectOnCheck="true">
      <thead>
        <tr>
        	<th field="showCheckboxWithId" checkbox="true" data-options="formatter:function(value, row, index){ return row.id }">编号</th>
			<th field="id">id</th>
			<th field="thumbnail" data-options="formatter:function(value,row,index){ if(row.fileType == 'picture'){ return '<img src=\'/ofile/thumb/'+row.id+'.html\' width=\'32\' height=\'32\'>'; }else{ return '' } }">缩略图</th>
			<th field="originalName">原始文件名</th>
			<th field="fileType">文件类型</th>
			<th field="fileSize">文件大小</th>
			<th field="filePath" data-options="formatter:function(value,row,index){ return linkFormatter('/ofile/image/'+row.id+'.html') + ' ' + value;}">路径</th>
			<th field="objectId">对象ID</th>
			<th field="module">模块分类</th>
			<th field="userName">用户名</th>
			<th field="createTime" formatter="formatDate">创建时间</th>
          	<th field="rowActions" data-options="formatter:function(value, row, index){return formatAction('manage_onlineFile_action',value,row)}">动作</th>
        </tr>
      </thead>
    </table>
    
    <!-- 每行的Action动作模板 -->
    <div id="manage_onlineFile_action" style="display: none;">
      <a class="line-action icon-show" onclick="$.acooly.framework.show('/manage/module/ofile/onlineFile/show.html?id={0}',500,400);" href="#" title="查看"></a>
      <a class="line-action icon-delete" onclick="$.acooly.framework.remove('/manage/module/ofile/onlineFile/deleteJson.html','{0}','manage_onlineFile_datagrid');" href="#" title="删除"></a>
    </div>
    
    <!-- 表格的工具栏 -->
    <div id="manage_onlineFile_toolbar">
      <a href="#" class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="$.acooly.framework.removes('/manage/module/ofile/onlineFile/deleteJson.html','manage_onlineFile_datagrid')">批量删除</a>
    </div>
  </div>

</div>