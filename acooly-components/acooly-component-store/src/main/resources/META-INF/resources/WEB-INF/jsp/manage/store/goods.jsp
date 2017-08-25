<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/manage/common/taglibs.jsp"%>
<c:if test="${initParam['ssoEnable']=='true'}">
    <%@ include file="/WEB-INF/jsp/manage/common/ssoInclude.jsp" %>
</c:if>
<script type="text/javascript">
$(function() {
	$.acooly.framework.registerKeydown('manage_goods_searchform','manage_goods_datagrid');
});

</script>
<div class="easyui-layout" data-options="fit : true,border : false">
  <!-- 查询条件 -->
  <div data-options="region:'north',border:false" style="padding:5px; overflow: hidden;" align="left">
    <form id="manage_goods_searchform" onsubmit="return false">
      <table class="tableForm" width="100%">
        <tr>
          <td align="left">
          	<div>
					仓库编码: <input type="text" class="text" size="15" name="search_LIKE_storeCode"/>
					品类ID: <input type="text" class="text" size="15" name="search_EQ_categroryId"/>
					品类名称: <input type="text" class="text" size="15" name="search_LIKE_categroryName"/>
					商品编码: <input type="text" class="text" size="15" name="search_LIKE_code"/>
					name: <input type="text" class="text" size="15" name="search_LIKE_name"/>
					单价: <input type="text" class="text" size="15" name="search_EQ_price"/>
					单位: <input type="text" class="text" size="15" name="search_LIKE_unit"/>
					库存: <input type="text" class="text" size="15" name="search_EQ_stock"/>
					型号: <input type="text" class="text" size="15" name="search_LIKE_model"/>
					品牌: <input type="text" class="text" size="15" name="search_LIKE_brand"/>
					供应商: <input type="text" class="text" size="15" name="search_LIKE_supplier"/>
					create_time: <input size="15" class="text" id="search_GTE_createTime" name="search_GTE_createTime" onFocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd'})" />
					至<input size="15" class="text" id="search_LTE_createTime" name="search_LTE_createTime" onFocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd'})" />
					update_time: <input size="15" class="text" id="search_GTE_updateTime" name="search_GTE_updateTime" onFocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd'})" />
					至<input size="15" class="text" id="search_LTE_updateTime" name="search_LTE_updateTime" onFocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd'})" />
          	<a href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:false" onclick="$.acooly.framework.search('manage_goods_searchform','manage_goods_datagrid');"><i class="fa fa-search fa-lg fa-fw fa-col"></i>查询</a>
          	</div>
          </td>
        </tr>
      </table>
    </form>
  </div>

  <!-- 列表和工具栏 -->
  <div data-options="region:'center',border:false">
    <table id="manage_goods_datagrid" class="easyui-datagrid" url="${pageContext.request.contextPath}/manage/store/goods/listJson.html" toolbar="#manage_goods_toolbar" fit="true" border="false" fitColumns="false"
      pagination="true" idField="id" pageSize="20" pageList="[ 10, 20, 30, 40, 50 ]" sortName="id" sortOrder="desc" checkOnSelect="true" selectOnCheck="true" singleSelect="true">
      <thead>
        <tr>
        	<th field="showCheckboxWithId" checkbox="true" data-options="formatter:function(value, row, index){ return row.id }">编号</th>
			<th field="id" sum="true">id</th>
			<th field="storeCode">仓库编码</th>
			<th field="categroryId" sum="true">品类ID</th>
			<th field="categroryName">品类名称</th>
			<th field="code">商品编码</th>
			<th field="name">name</th>
			<th field="descn">商品简介</th>
			<th field="price" sum="true">单价</th>
			<th field="unit">单位</th>
			<th field="stock" >库存</th>
			<th field="model">型号</th>
			<th field="brand">品牌</th>
			<th field="supplier">供应商</th>
			<th field="detailUrl">展示地址</th>
		    <th field="createTime" formatter="formatDate">create_time</th>
		    <th field="updateTime" formatter="formatDate">update_time</th>
			<th field="comments">备注</th>
          	<th field="rowActions" data-options="formatter:function(value, row, index){return formatAction('manage_goods_action',value,row)}">动作</th>
        </tr>
      </thead>
    </table>

    <!-- 每行的Action动作模板 -->
    <div id="manage_goods_action" style="display: none;">
      <a onclick="$.acooly.framework.edit({url:'/manage/store/goods/edit.html',id:'{0}',entity:'goods',width:500,height:400});" href="#" title="编辑"><i class="fa fa-pencil fa-lg fa-fw fa-col"></i></a>
      <a onclick="$.acooly.framework.show('/manage/store/goods/show.html?id={0}',500,400);" href="#" title="查看"><i class="fa fa-file-o fa-lg fa-fw fa-col"></i></a>
      <a onclick="$.acooly.framework.remove('/manage/store/goods/deleteJson.html','{0}','manage_goods_datagrid');" href="#" title="删除"><i class="fa fa-trash-o fa-lg fa-fw fa-col"></i></a>
    </div>

    <!-- 表格的工具栏 -->
    <div id="manage_goods_toolbar">
      <a href="#" class="easyui-linkbutton" plain="true" onclick="$.acooly.framework.create({url:'/manage/store/goods/create.html',entity:'goods',width:500,height:400})"><i class="fa fa-plus-circle fa-lg fa-fw fa-col"></i>添加</a>
      <a href="#" class="easyui-linkbutton" plain="true" onclick="$.acooly.framework.removes('/manage/store/goods/deleteJson.html','manage_goods_datagrid')"><i class="fa fa-trash-o fa-lg fa-fw fa-col"></i>批量删除</a>
      <a href="#" class="easyui-menubutton" data-options="menu:'#manage_goods_exports_menu'"><i class="fa fa-arrow-circle-o-down fa-lg fa-fw fa-col"></i>批量导出</a>
      <div id="manage_goods_exports_menu" style="width:150px;">
        <div onclick="$.acooly.framework.exports('/manage/store/goods/exportXls.html','manage_goods_searchform','商品信息')"><i class="fa fa-file-excel-o fa-lg fa-fw fa-col"></i>Excel</div>
        <div onclick="$.acooly.framework.exports('/manage/store/goods/exportCsv.html','manage_goods_searchform','商品信息')"><i class="fa fa-file-text-o fa-lg fa-fw fa-col"></i>CSV</div>
      </div>
      <a href="#" class="easyui-linkbutton" plain="true" onclick="$.acooly.framework.imports({url:'/manage/store/goods/importView.html',uploader:'manage_goods_import_uploader_file'});"><i class="fa fa-arrow-circle-o-up fa-lg fa-fw fa-col"></i>批量导入</a>
    </div>
  </div>

</div>
