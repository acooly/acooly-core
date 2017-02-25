<#assign jodd=JspTaglibs["http://www.springside.org.cn/jodd_form"] />
<div>
    <form id="manage_content${RequestParameters.code}_editform"
          action="${rc.contextPath}/manage/module/cms/content/<#if action == 'create'>save<#else>update</#if>Json.html" method="post" enctype="multipart/form-data">
    <@jodd.form bean="content" scope="request">
        <input name="id" type="hidden"/>
        <input type="hidden" name="code" value="${RequestParameters.code}"/>
        <table class="tableForm" width="100%">
            <tr>
                <th width="10%">标题：</th>
                <td>
                    <input type="text" style="width: 300px;" class="text" name="title" size="128" class="easyui-validatebox"
                           data-options="required:true" class="text" validType="byteLength[1,128]"/>
                    <#if RequestParameters.cmsType != 'banner'>
                    <span>关键字：<input type="text" class="text" name="keywords" size="20"
                                     class="easyui-validatebox" validType="byteLength[1,128]"/></span>
                    <span>编码：<input type="text" class="text" name="keycode" size="15"
                                    class="easyui-validatebox" validType="byteLength[1,32]"/> 唯一标识</span>
                    </#if>
                </td>
            </tr>
            <tr>
                <th>图片：</th>
                <td>
                    <input type="file" name="cover_f" id="cover_f" class="easyui-validatebox" validType="validImg['jpg,gif,png','只能上传jpg,gif,png格式的图片']"/>
                    <#if content.cover?? && content.cover != '' && action!='create'>
                        <div><a href="${mediaRoot}/${content.cover}" target="_blank" data-lightbox="cover"><img src="${mediaRoot}/${content.cover}" width="200"></a></div>
                    </#if>
                </td>
            </tr>
            <#if RequestParameters.cmsType = 'banner'>
            <tr>
                <th>链接：</th>
                <td><input type="text" style="width: 300px;" class="text" name="link" size="128" class="easyui-validatebox"
                           data-options="required:true" class="text" validType="byteLength[1,128]"/></td>
            </tr>
            <#else>
            <tr>
                <td colspan="2">
                    <textarea id="contentId" name="contents" data-options="required:true"
                              style="width:100%;height:430px;">${content.contentBody.body}</textarea>
                </td>
            </tr>
                <script type="text/javascript">
                    $(function () {
                        var token = $("meta[name='X-CSRF-TOKEN']").attr("content");// 从meta中获取token
                        var ke = $.acooly.framework.kingEditor({
                            uploadUrl : '/ofile/kindEditor.html?_csrf=' + token,
                            minHeight : '310',
                            textareaId : 'contentId'
                        });
                    });
                </script>
            </#if>
            <tr>
                <th>备注：</th>
                <td><textarea name="comments" rows="2" style="width:300px;"></textarea></td>
            </tr>
        </table>
    </@jodd.form>
    </form>
</div>
