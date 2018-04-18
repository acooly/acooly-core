<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/jsp/manage/common/taglibs.jsp" %>

<script type="text/javascript">
    $(function () {
        $.acooly.framework.registerKeydown('manage_pointTrade_searchform', 'manage_pointTrade_datagrid');
    });

</script>
<div class="easyui-layout" data-options="fit : true,border : false">
    <!-- 查询条件 -->
    <div data-options="region:'north',border:false" style="padding:5px; overflow: hidden;" align="left">
        <form id="manage_pointTrade_searchform" onsubmit="return false">
            <table class="tableForm" width="100%">
                <tr>
                    <td align="left">
                        <div>
                            用户名: <input type="text" class="text" size="15" name="search_EQ_userName"/>
                            交易类型: <select style="width:80px;height:27px;" name="search_EQ_tradeType" editable="false" panelHeight="auto"
                                          class="easyui-combobox">
                            <option value="">所有</option>
                            <c:forEach var="e" items="${allTradeTypes}">
                                <option value="${e.key}" ${param.search_EQ_tradeType == e.key?'selected':''}>${e.value}</option>
                            </c:forEach></select>
                            业务类型: <input type="text" class="text" size="15" name="search_EQ_busiType"/>
                            交易订单号: <input type="text" class="text" size="15" name="search_EQ_tradeNo"/>
                            创建时间: <input size="10" class="text" id="search_GTE_createTime" name="search_GTE_createTime"
                                         onFocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd'})"/>
                            至<input size="10" class="text" id="search_LTE_createTime" name="search_LTE_createTime"
                                    onFocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd'})"/>
                            <a href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:false"
                               onclick="$.acooly.framework.search('manage_pointTrade_searchform','manage_pointTrade_datagrid');"><i
                                    class="fa fa-search fa-lg fa-fw fa-col"></i>查询</a>
                        </div>
                    </td>
                </tr>
            </table>
        </form>
    </div>

    <!-- 列表和工具栏 -->
    <div data-options="region:'center',border:false">
        <table id="manage_pointTrade_datagrid" class="easyui-datagrid"
               url="${pageContext.request.contextPath}/manage/point/pointTrade/listJson.html" toolbar="#manage_pointTrade_toolbar"
               fit="true" border="false" fitColumns="false"
               pagination="true" idField="id" pageSize="20" pageList="[ 10, 20, 30, 40, 50 ]" sortName="id" sortOrder="desc"
               checkOnSelect="true" selectOnCheck="true" singleSelect="true">
            <thead>
            <tr>
                <th field="showCheckboxWithId" checkbox="true" data-options="formatter:function(value, row, index){ return row.id }">编号</th>
                <th field="id" sum="true">id</th>
                <th field="tradeNo">交易订单号</th>
                <th field="tradeType"
                    data-options="formatter:function(value){ return formatRefrence('manage_pointTrade_datagrid','allTradeTypes',value);} ">
                    交易类型
                </th>
                <th field="userName">用户名</th>
                <th field="amount" sum="true">交易积分</th>
                <th field="endFreeze" sum="true">交易后冻结积分</th>
                <th field="endBalance" sum="true">交易后积分</th>
                <th field="endAvailable" sum="true">交易后有效积分</th>
                <th field="busiId">业务Id</th>
                <th field="busiType">业务类型</th>
                <th field="busiTypeText">业务类型描述</th>
                <th field="busiData">相关业务数据</th>
                <th field="createTime" formatter="formatDate">创建时间</th>
                <th field="updateTime" formatter="formatDate">修改时间</th>
                <th field="memo">备注</th>
                <th field="rowActions"
                    data-options="formatter:function(value, row, index){return formatAction('manage_pointTrade_action',value,row)}">动作
                </th>
            </tr>
            </thead>
        </table>

        <!-- 每行的Action动作模板 -->
        <div id="manage_pointTrade_action" style="display: none;">
            <!--       <a onclick="$.acooly.framework.edit({url:'/manage/point/pointTrade/edit.html',id:'{0}',entity:'pointTrade',width:500,height:400});" href="#" title="编辑"><i class="fa fa-pencil fa-lg fa-fw fa-col"></i></a> -->
            <a onclick="$.acooly.framework.show('/manage/point/pointTrade/show.html?id={0}',500,400);" href="#" title="查看"><i
                    class="fa fa-file-o fa-lg fa-fw fa-col"></i></a>
            <!--       <a onclick="$.acooly.framework.remove('/manage/point/pointTrade/deleteJson.html','{0}','manage_pointTrade_datagrid');" href="#" title="删除"><i class="fa fa-trash-o fa-lg fa-fw fa-col"></i></a> -->
        </div>

        <!-- 表格的工具栏 -->
        <div id="manage_pointTrade_toolbar">
            <!--       <a href="#" class="easyui-linkbutton" plain="true" onclick="$.acooly.framework.create({url:'/manage/point/pointTrade/create.html',entity:'pointTrade',width:500,height:400})"><i class="fa fa-plus-circle fa-lg fa-fw fa-col"></i>添加</a> -->
            <!--       <a href="#" class="easyui-linkbutton" plain="true" onclick="$.acooly.framework.removes('/manage/point/pointTrade/deleteJson.html','manage_pointTrade_datagrid')"><i class="fa fa-trash-o fa-lg fa-fw fa-col"></i>批量删除</a> -->
            <!--       <a href="#" class="easyui-menubutton" data-options="menu:'#manage_pointTrade_exports_menu'"><i class="fa fa-arrow-circle-o-down fa-lg fa-fw fa-col"></i>批量导出</a> -->
            <!--       <div id="manage_pointTrade_exports_menu" style="width:150px;"> -->
            <!--         <div onclick="$.acooly.framework.exports('/manage/point/pointTrade/exportXls.html','manage_pointTrade_searchform','积分交易信息')"><i class="fa fa-file-excel-o fa-lg fa-fw fa-col"></i>Excel</div> -->
            <!--         <div onclick="$.acooly.framework.exports('/manage/point/pointTrade/exportCsv.html','manage_pointTrade_searchform','积分交易信息')"><i class="fa fa-file-text-o fa-lg fa-fw fa-col"></i>CSV</div> -->
            <!--       </div> -->
            <!--       <a href="#" class="easyui-linkbutton" plain="true" onclick="$.acooly.framework.imports({url:'/manage/point/pointTrade/importView.html',uploader:'manage_pointTrade_import_uploader_file'});"><i class="fa fa-arrow-circle-o-up fa-lg fa-fw fa-col"></i>批量导入</a> -->
        </div>
    </div>

</div>
