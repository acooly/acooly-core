<script type="text/javascript">
$(function() {
	$.acooly.framework.registerKeydown('manage_lottery_searchform','manage_lottery_datagrid');
});

function getSelectLotteryId(){
	var rowData=$.acooly.framework.getSelectedRow("manage_lottery_datagrid");
	if(!rowData){
		$.messager.show({title : '提示',msg : '请先选择操作抽奖活动'});
	      return null;
	}
	return rowData.id;
}


function manage_lottery_action(row){
	var html = '';
	if(row.status != 'disable'){
		html = formatString($('#manage_lottery_action').html(), row.id);
	}
	return html;
}

function manage_lottery_tabs_load(){
	manage_lotteryAward_load();
}


/**
 * 奖项设置异步加载 
 */
function manage_lotteryAward_load(){
	var lotteryId = getSelectLotteryId();
	$.ajax({
		url:contextPath + '/manage/module/lottery/lotteryAward/listJson.html?'+new Date(),
		data : {
			"search_EQ_lotteryId":lotteryId,
			"sort":"id",
			"order":"asc"
		},
		success : function(result) {
			$('#manage_lotteryAward_datagrid').datagrid('loadData',result);
		}
	});
};

/**
 * 添加奖项
 */
function manage_lotteryAward_add(){
	var lotteryId = getSelectLotteryId();
	$.acooly.framework.create({url:'/manage/module/lottery/lotteryAward/create.html?lotteryId='+lotteryId,entity:'lotteryAward',width:500,height:400})
}


</script>
<div class="easyui-layout" data-options="fit:true,border : false">
  <div data-options="region:'north',border:false" style="height:250px;">
    <table id="manage_lottery_datagrid" class="easyui-datagrid" url="${pageContext.request.contextPath}/manage/module/lottery/lottery/listJson.html" toolbar="#manage_lottery_toolbar" fit="true" border="false" fitColumns="false"
      pagination="true" idField="id" pageSize="20" pageList="[ 10, 20, 30, 40, 50 ]" sortName="id" sortOrder="desc" checkOnSelect="true" selectOnCheck="true" singleSelect="true"
      data-options="onClickRow:manage_lottery_tabs_load">
      <thead>
        <tr>
        	<th field="showCheckboxWithId" checkbox="true" data-options="formatter:function(value, row, index){ return row.id }">编号</th>
			<th field="id">ID</th>
			<th field="code">编码</th>
			<th field="title">标题</th>
			<th field="type" data-options="formatter:function(value){ return formatRefrence('manage_lottery_datagrid','allTypes',value);}">类型</th>
		    <th field="maxWinners">最大参与人数(0表示无限)</th>
		    <th field="startTime" formatter="dateFormatter">开始日期</th>
		    <th field="endTime" formatter="dateFormatter">结束日期</th>
		    <th field="createTime" formatter="formatDate">创建时间</th>
			<th field="status" data-options="formatter:function(value){ return formatRefrence('manage_lottery_datagrid','allStatuss',value);}">状态</th>
          	<th field="rowActions" data-options="formatter:function(value,row){ return manage_lottery_action(row)}">动作</th>
        </tr>
      </thead>
    </table>
    
    <!-- 每行的Action动作模板 -->
    <div id="manage_lottery_action" style="display: none;">
    	<a class="line-action icon-edit" onclick="$.acooly.framework.edit({url:'/manage/module/lottery/lottery/edit.html',id:'{0}',entity:'lottery',width:500,height:500});" href="#" title="编辑"></a>
    	<a class="line-action icon-play" onclick="$.acooly.framework.confirmSubmit('/manage/module/lottery/lottery/status.html','{0}','manage_lottery_datagrid');" href="#" title="暂停/恢复"></a>
    	<a class="line-action icon-delete" onclick="$.acooly.framework.remove('/manage/module/lottery/lottery/deleteJson.html','{0}','manage_lottery_datagrid');" href="#" title="删除"></a>
    </div>
    <!-- 表格的工具栏 -->
    <div id="manage_lottery_toolbar">
    <form id="manage_lottery_searchform" onsubmit="return false">
      <a href="#" class="easyui-linkbutton" style="float: left;" iconCls="icon-add" plain="true" onclick="$.acooly.framework.create({url:'/manage/module/lottery/lottery/create.html',entity:'lottery',width:500,height:500})">添加</a> 
	  <div class="datagrid-btn-separator"></div>
	  &nbsp;标题:<input type="text" size="15" name="search_LIKE_title" value="${param.search_LIKE_title}"  />
	  类型:<select style="width:80px;" name="search_LIKE_type" editable="false" panelHeight="auto" class="easyui-combobox"><option value="">所有</option><c:forEach var="e" items="${allTypes}"><option value="${e.key}" ${param.search_LIKE_type == e.key?'selected':''}>${e.value}</option></c:forEach></select>
	  开始日期:<input size="15" id="search_GTE_startTime" name="search_GTE_startTime" size="10" onFocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd'})" />
	  至<input size="15" id="search_LTE_startTime" name="search_LTE_startTime" size="10" onFocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd'})" /> 			
      <a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-search',plain:false" onclick="$.acooly.framework.search('manage_lottery_searchform','manage_lottery_datagrid');">查询</a> 
    </form>
    </div>
  </div>
  
  <div data-options="region:'center',border:false" style="height:40%;">
	<div id="manage_lottery_tabs" class="easyui-tabs" fit="true">
		<div title="奖项设置" style="margin-left: 0px;">
		    <table id="manage_lotteryAward_datagrid" class="easyui-datagrid" toolbar="#manage_lotteryAward_toolbar" fit="true" border="false" fitColumns="true" pagination="false" idField="id" pageSize="20"
					pageList="[ 10, 20, 30, 40, 50 ]" sortName="id" sortOrder="desc" checkOnSelect="true" selectOnCheck="true" singleSelect="true">
		      <thead>
		        <tr>
		        	<th field="showCheckboxWithId" checkbox="true" data-options="formatter:function(value, row, index){ return row.id }">编号</th>
					<th field="id">ID</th>
					<th field="award">奖项</th>
					<th field="awardNote">奖项说明</th>
					<th field="awardPosition">奖项位置(360度坐标)</th>
					<th field="weight">权重</th>
					<th field="maxWiner">最大中奖数</th>
				    <th field="createTime" formatter="formatDate">创建时间</th>
					<th field="comments">备注</th>
		          	<th field="rowActions" data-options="formatter:function(value, row, index){return formatAction('manage_lotteryAward_action',value,row)}">动作</th>
		        </tr>
		      </thead>
		    </table>
		    
		    <!-- 每行的Action动作模板 -->
		    <div id="manage_lotteryAward_action" style="display: none;">
		      <a class="line-action icon-edit" onclick="$.acooly.framework.edit({url:'/manage/module/lottery/lotteryAward/edit.html',id:'{0}',entity:'lotteryAward',width:500,height:400});" href="#" title="编辑"></a>
		      <a class="line-action icon-delete" onclick="$.acooly.framework.remove('/manage/module/lottery/lotteryAward/deleteJson.html','{0}','manage_lotteryAward_datagrid');" href="#" title="删除"></a>
		    </div>
		    
		    <!-- 表格的工具栏 -->
		    <div id="manage_lotteryAward_toolbar">
		      <a href="#" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="manage_lotteryAward_add()">添加</a> 
		      <a href="#" class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="$.acooly.framework.removes('/manage/module/lottery/lotteryAward/deleteJson.html','manage_lotteryAward_datagrid')">批量删除</a>
		    </div>		
		</div>
	</div>
  </div>
</div>
