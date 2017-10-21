<div align="center">
    <table class="tableForm" width="100%">
        <tr>
            <th width="20%">ID：</th>
            <td>${olog.id}</td>
        </tr>
        <tr>
            <th>操作员：</th>
            <td>${olog.operateUser}</td>
        </tr>
        <tr>
            <th>操作时间：</th>
            <td>${olog.operateTime}</td>
        </tr>
        <tr>
            <th>系统：</th>
            <td>${olog.system}</td>
        </tr>
        <tr>
            <th>模块：</th>
            <td><#if olog.module == olog.moduleName>${olog.module}<#else>${olog.moduleName}[${olog.module}]</#if></td>
        </tr>
        <tr>
            <th>功能：</th>
            <td>${olog.actionName}[${olog.action}]</td>
        </tr>
        <tr>
            <th>客户端信息：</th>
            <td>${olog.clientInformations}</td>
        </tr>
        <tr>
            <th>执行时长度：</th>
            <td>${olog.executeMilliseconds}ms</td>
        </tr>
        <tr>
            <th>请求参数：</th>
            <td>
                <div id="manage_olog_show_parameter_container">${olog.requestParameters}</div>
            </td>
        </tr>
        <tr>
            <th>操作结果：</th>
            <td>${olog.operateResult}</td>
        </tr>
    <#if (olog.operateMessage)?? && olog.operateMessage != "">
        <tr>
            <th>结果消息：</th>
            <td>${olog.operateMessage}</td>
        </tr>
    </#if>
        <tr>
            <th>备注：</th>
            <td>${olog.descn}</td>
        </tr>
    </table>
</div>
<script>

    $('#manage_olog_show_parameter_container').html($.acooly.format.json($('#manage_olog_show_parameter_container').html()));

</script>