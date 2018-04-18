<script type="text/javascript">
    /**
     * 页面加载完成后执行
     */
    $(function () {
        manage_resource_loadTree();
    });
</script>

<div class="easyui-layout" data-options="fit : true,border : false">
    <div data-options="region:'north'" style="overflow: hidden; height:30px;padding-left: 2px;">
        <div id="manage_resource_toolbar" style="margin-top: 1px;">
            <a href="#" class="easyui-linkbutton" plain="true" onclick="manage_resource_tree_toggle()"><i
                    class="fa fa-plus-square fa-lg fa-fw fa-col"></i>展开/收起菜单</a>
            <a href="#" class="easyui-linkbutton" plain="true" onclick="manage_resource_form_add(true)"><i
                    class="fa fa-plus-circle fa-lg fa-fw fa-col"></i>添加顶级菜单</a>
        </div>
    </div>
    <!-- 菜单树 -->
    <div data-options="region:'west',border:true,split:true" style="width:300px;padding-left: 10px;" align="left">
        <div id="manage_resource_tree" class="ztree"></div>
    </div>

    <!-- 列表和工具栏 -->
    <div data-options="region:'center',border:false">
        <form id="manage_resource_editform" action="${rc.contextPath}/manage/system/resource/updateJson.html" method="post">
            <jodd:form bean="resource" scope="request">
                <input name="id" id="manage_resource_node_id" type="hidden"/>
                <table class="tableForm" width="100%">
                    <tr>
                        <th style="width: 80px">父节点：</th>
                        <td>
                            <div id="manage_resource_node_parentName"></div>
                            <input id="manage_resource_node_parentId" name="parentId" type="hidden"/>
                        </td>
                    </tr>
                    <tr>
                        <th style="width: 80px">资源名称：</th>
                        <td><input name="name" type="text" class="easyui-validatebox" data-options="required:true"/></td>
                    </tr>
                    <tr>
                        <th>加载方式：</th>
                        <td>
                            <select name="showMode" style="width: 150px;" editable="false" panelHeight="auto" class="easyui-combobox"
                                    data-options="required:true">
                   <#list allShowModes as k,v>
                       <option value="${k}">${v}</option></#list>
                            </select>
                        </td>
                    </tr>
                    <tr>
                        <th>资源类型：</th>
                        <td>
                            <select name="type" editable="false" panelHeight="auto" style="width: 150px;" class="easyui-combobox"
                                    data-options="required:true">
                    <#list allTypes as k,v>
                        <option value="${k}">${v}</option></#list>
                            </select>
                        </td>
                    </tr>
                    <tr>
                        <th>是否显示：</th>
                        <td>
                            <select id="manage_resource_form_showState" name="showState" style="width: 150px;" editable="false"
                                    panelHeight="auto" class="easyui-combobox" data-options="required:true">
                   <#list allShowStates as k,v>
                       <option value="${k}">${v}</option></#list>
                            </select>
                        </td>
                    </tr>
                    <tr>
                        <th>资源串：</th>
                        <td><input name="value" type="text" class="easyui-validatebox" size="50"/></td>
                    </tr>
                    <tr>
                        <th>资源图标：</th>
                        <td>
                            <div id="iconContainer">
            <#list allIcons as e>
                <span><input type="radio" name="icon" value="${e}"/></span>
                <span style='vertical-align:middle;display:inline-block; width:16px; height:16px;' class="${e}"></span>
            </#list>
                            </div>
                        </td>
                    </tr>
                    <tr>
                        <th></th>
                        <td><a href="#" class="easyui-linkbutton" iconCls="icon-add" onclick="manage_resource_form_submit()">保存</a></td>
                    </tr>
                </table>
            </jodd:form>
        </form>
    </div>

</div>
