<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/jsp/manage/common/taglibs.jsp" %>

<script type="text/javascript">
    $(function () {
        $.acooly.framework.registerKeydown('manage_pointStatistics_searchform', 'manage_pointStatistics_datagrid');
    });

</script>
<div class="easyui-layout" data-options="fit : true,border : false">
    <!-- 查询条件 -->
    <div data-options="region:'north',border:false" style="padding:5px; overflow: hidden;" align="left">
        <form id="manage_pointStatistics_searchform" onsubmit="return false">
            <table class="tableForm" width="100%">
                <tr>
                    <td align="left">
                        <div>
                            用户名: <input type="text" class="text" size="15" name="search_EQ_userName"/>
                            统计时间: <input size="15" class="text" id="search_GTE_startTime" name="search_GTE_startTime"
                                         onFocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd'})"/>
                            至<input size="15" class="text" id="search_LTE_endTime" name="search_LTE_endTime"
                                    onFocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd'})"/>
                            状态: <select style="width:80px;height:27px;" name="search_EQ_status" editable="false" panelHeight="auto"
                                        class="easyui-combobox">
                            <option value="">所有</option>
                            <c:forEach var="e" items="${allStatuss}">
                                <option value="${e.key}" ${param.search_EQ_status == e.key?'selected':''}>${e.value}</option>
                            </c:forEach></select>
                            <a href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:false"
                               onclick="$.acooly.framework.search('manage_pointStatistics_searchform','manage_pointStatistics_datagrid');"><i
                                    class="fa fa-search fa-lg fa-fw fa-col"></i>查询</a>
                        </div>
                    </td>
                </tr>
            </table>
        </form>
    </div>

    <!-- 列表和工具栏 -->
    <div data-options="region:'center',border:false">
        <table id="manage_pointStatistics_datagrid" class="easyui-datagrid"
               url="${pageContext.request.contextPath}/manage/point/pointStatistics/listJson.html" toolbar="#manage_pointStatistics_toolbar"
               fit="true" border="false" fitColumns="false"
               pagination="true" idField="id" pageSize="20" pageList="[ 10, 20, 30, 40, 50 ]" sortName="id" sortOrder="desc"
               checkOnSelect="true" selectOnCheck="true" singleSelect="true">
            <thead>
            <tr>
                <th field="showCheckboxWithId" checkbox="true" data-options="formatter:function(value, row, index){ return row.id }">编号</th>
                <th field="id" sum="true">ID</th>
                <th field="userName">用户名</th>
                <th field="num" sum="true">统计条数</th>
                <th field="point" sum="true">统计积分</th>
                <th field="actualPoint" sum="true">真实处理积分</th>
                <th field="startTime" formatter="formatDate">开始时间</th>
                <th field="endTime" formatter="formatDate">结束时间</th>
                <th field="status"
                    data-options="formatter:function(value){ return formatRefrence('manage_pointStatistics_datagrid','allStatuss',value);} ">
                    状态
                </th>
                <th field="createTime" formatter="formatDate">创建时间</th>
                <th field="updateTime" formatter="formatDate">修改时间</th>
                <th field="memo">备注</th>
                <th field="rowActions"
                    data-options="formatter:function(value, row, index){return formatAction('manage_pointStatistics_action',value,row)}">动作
                </th>
            </tr>
            </thead>
        </table>

        <!-- 每行的Action动作模板 -->
        <div id="manage_pointStatistics_action" style="display: none;">
            <!--       <a onclick="$.acooly.framework.edit({url:'/manage/point/pointStatistics/edit.html',id:'{0}',entity:'pointStatistics',width:500,height:400});" href="#" title="编辑"><i class="fa fa-pencil fa-lg fa-fw fa-col"></i></a> -->
            <!--       <a onclick="$.acooly.framework.show('/manage/point/pointStatistics/show.html?id={0}',500,400);" href="#" title="查看"><i class="fa fa-file-o fa-lg fa-fw fa-col"></i></a> -->
            <!--       <a onclick="$.acooly.framework.remove('/manage/point/pointStatistics/deleteJson.html','{0}','manage_pointStatistics_datagrid');" href="#" title="删除"><i class="fa fa-trash-o fa-lg fa-fw fa-col"></i></a> -->
        </div>

        <!-- 表格的工具栏 -->
        <div id="manage_pointStatistics_toolbar">
            <!--       <a href="#" class="easyui-linkbutton" plain="true" onclick="$.acooly.framework.create({url:'/manage/point/pointStatistics/create.html',entity:'pointStatistics',width:500,height:400})"><i class="fa fa-plus-circle fa-lg fa-fw fa-col"></i>添加</a> -->
            <!--       <a href="#" class="easyui-linkbutton" plain="true" onclick="$.acooly.framework.removes('/manage/point/pointStatistics/deleteJson.html','manage_pointStatistics_datagrid')"><i class="fa fa-trash-o fa-lg fa-fw fa-col"></i>批量删除</a> -->
            <!--       <a href="#" class="easyui-menubutton" data-options="menu:'#manage_pointStatistics_exports_menu'"><i class="fa fa-arrow-circle-o-down fa-lg fa-fw fa-col"></i>批量导出</a> -->
            <!--       <div id="manage_pointStatistics_exports_menu" style="width:150px;"> -->
            <!--         <div onclick="$.acooly.framework.exports('/manage/point/pointStatistics/exportXls.html','manage_pointStatistics_searchform','积分统计')"><i class="fa fa-file-excel-o fa-lg fa-fw fa-col"></i>Excel</div> -->
            <!--         <div onclick="$.acooly.framework.exports('/manage/point/pointStatistics/exportCsv.html','manage_pointStatistics_searchform','积分统计')"><i class="fa fa-file-text-o fa-lg fa-fw fa-col"></i>CSV</div> -->
            <!--       </div> -->
            <!--       <a href="#" class="easyui-linkbutton" plain="true" onclick="$.acooly.framework.imports({url:'/manage/point/pointStatistics/importView.html',uploader:'manage_pointStatistics_import_uploader_file'});"><i class="fa fa-arrow-circle-o-up fa-lg fa-fw fa-col"></i>批量导入</a> -->

        </div>
    </div>

</div>
