<#assign jodd=JspTaglibs["http://www.springside.org.cn/jodd_form"] />
<script type="text/javascript">
    $(function () {
    	var token = $("meta[name='X-CSRF-TOKEN']").attr("content");// 从meta中获取token
        $.acooly.framework.kingEditor({
            uploadUrl : '/ofile/kindEditor.html?_csrf=' + token,
            minHeight : '310',
            textareaId : 'contentId'
        });
    });
</script>
<div>
    <form id="manage_content${RequestParameters.code}_editform"
          action="${rc.contextPath}/manage/module/cms/content/<#if action == 'create'>save<#else>update</#if>Json.html"
          method="post"
          enctype="multipart/form-data">
    <@jodd.form bean="content" scope="request">
        <input name="id" type="hidden"/>
        <input type="hidden" name="code" value="${RequestParameters.code}"/>
        <table class="tableForm" width="100%">
            <tr>
                <th width="10%">标题：</th>
                <td>
                    <input type="text" style="width: 300px;" name="title" size="128" class="easyui-validatebox"
                           data-options="required:true" validType="byteLength[1,128]"/>
                    <span>关键字：<input type="text" name="keywords" size="20"
                                     class="easyui-validatebox"
                                     validType="byteLength[1,128]"/></span>
                    <span>编码：<input type="text" name="keycode" size="15"
                                    class="easyui-validatebox" validType="byteLength[1,32]"/> 唯一标识</span>

                </td>
            </tr>
            <tr>
                <th>封面图片：</th>
                <td>
                    <input type="file" name="cover_f" id="cover_f" class="easyui-validatebox"
                           validType="validImg['jpg,gif,png','只能上传jpg,gif,png格式的图片']"/>
                    <#if content.cover?? && content.cover != '' && action!='create'>
                        <span><a href="${mediaRoot }/${content.cover}" target="_blank"
                                 data-lightbox="cover">查看</a></span>
                    </#if>
                </td>
            </tr>
            <!--
            <tr>
                <th>主题：</th>
                <td><textarea rows="2" cols="90" name="subject" class="easyui-validatebox"
                              validType="byteLength[1,255]"></textarea></td>
            </tr>
            -->
            <tr>
                <td colspan="2">
                    <textarea id="contentId" name="contents" data-options="required:true"
                              style="width:100%;height:430px;">${content.contentBody.body}</textarea>
                </td>
            </tr>
        </table>
    </@jodd.form>
    </form>
</div>
