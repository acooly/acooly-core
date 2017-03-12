<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/manage/common/taglibs.jsp"%>


<script type="text/javascript">
$(function() {
	$.acooly.framework.registerKeydown('manage_lotteryAward_searchform','manage_lotteryAward_datagrid');
});
</script>
<div class="easyui-layout" data-options="fit : true,border : false">
  <!-- 列表和工具栏 -->
  <div data-options="region:'center',border:false">
    <table id="manage_lotteryAward_datagrid" class="easyui-datagrid" url="${pageContext.request.contextPath}/manage/module/lottery/lotteryAward/listJson.html?lotteryId=${lotteryId}" toolbar="#manage_lotteryAward_toolbar" fit="true" border="false" fitColumns="true"
      pagination="true" idField="id" pageSize="20" pageList="[ 10, 20, 30, 40, 50 ]" sortName="id" sortOrder="desc" checkOnSelect="true" selectOnCheck="true">
      <thead>
        <tr>
        	<th field="showCheckboxWithId" checkbox="true" data-options="formatter:function(value, row, index){ return row.id }">编号</th>
			<th field="id">ID</th>
			<th field="award">奖项</th>
			<th field="awardNote">奖项说明</th>
			<th field="awardPhoto">奖品图片</th>
			<th field="weight">权重</th>
			<th field="maxWiner">最大中奖数</th>
		    <th field="createTime" formatter="formatDate">创建时间</th>
		    <th field="modifyTime" formatter="formatDate">最后修改时间</th>
			<th field="comments">备注</th>
          	<th field="rowActions" data-options="formatter:function(value, row, index){return formatAction('manage_lotteryAward_action',value,row)}">动作</th>
        </tr>
      </thead>
    </table>
    
    <!-- 每行的Action动作模板 -->
    <div id="manage_lotteryAward_action" style="display: none;">
      <a class="line-action icon-edit" onclick="$.acooly.framework.edit({url:'/manage/module/lottery/lotteryAward/edit.html',id:'{0}',entity:'lotteryAward',width:500,height:400});" href="#" title="编辑"></a>&nbsp
      <a class="line-action icon-show" onclick="$.acooly.framework.show('/manage/module/lottery/lotteryAward/show.html?id={0}',500,400);" href="#" title="查看"></a>&nbsp
      <a class="line-action icon-delete" onclick="$.acooly.framework.remove('/manage/module/lottery/lotteryAward/deleteJson.html','{0}','manage_lotteryAward_datagrid');" href="#" title="删除"></a>
    </div>
    
    <!-- 表格的工具栏 -->
    <div id="manage_lotteryAward_toolbar">
      <a href="#" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="$.acooly.framework.create({url:'/manage/module/lottery/lotteryAward/create.html?lotteryId=${lotteryId}',entity:'lotteryAward',width:500,height:400})">添加</a> 
      <a href="#" class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="$.acooly.framework.removes('/manage/module/lottery/lotteryAward/deleteJson.html','manage_lotteryAward_datagrid')">批量删除</a>
    </div>
  </div>

</div>
