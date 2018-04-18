<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/jsp/manage/common/taglibs.jsp" %>
<div>
    <form id="manage_pointClearConfig_editform"
          action="${pageContext.request.contextPath}/manage/point/pointClearConfig/${action=='create'?'saveJson':'updateJson'}.html"
          method="post">
        <jodd:form bean="pointClearConfig" scope="request">
            <input name="id" type="hidden"/>
            <table class="tableForm" width="100%">
                <tr>
                    <th width="25%">开始交易时间：</th>
                    <td><input type="text" name="startTradeTime" size="20" class="easyui-validatebox text"
                               value="<fmt:formatDate value="${pointClearConfig.startTradeTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
                               onFocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd 00:00:00'})" data-options="required:true"/></td>
                </tr>
                <tr>
                    <th>结束交易时间：</th>
                    <td><input type="text" name="endTradeTime" size="20" class="easyui-validatebox text"
                               value="<fmt:formatDate value="${pointClearConfig.endTradeTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
                               onFocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd 23:59:59'})" data-options="required:true"/></td>
                </tr>
                <tr>
                    <th>开始清理时间：</th>
                    <td><input type="text" name="startClearTime" size="20" class="easyui-validatebox text"
                               value="<fmt:formatDate value="${pointClearConfig.startClearTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
                               onFocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd 00:00:00'})" data-options="required:true"/></td>
                </tr>
                <tr>
                    <th>结束清理时间：</th>
                    <td><input type="text" name="endClearTime" size="20" class="easyui-validatebox text"
                               value="<fmt:formatDate value="${pointClearConfig.endClearTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
                               onFocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd 23:59:59'})" data-options="required:true"/></td>
                </tr>
                <tr>
                    <th>清零时间：</th>
                    <td><input type="text" name="clearTime" size="20" class="easyui-validatebox text"
                               value="<fmt:formatDate value="${pointClearConfig.clearTime}" pattern="yyyy-MM-dd"/>"
                               onFocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd'})" data-options="required:true"/></td>
                </tr>
                <tr>
                    <th>备注：</th>
                    <td><textarea rows="3" cols="40" style="width:300px;" name="memo" class="easyui-validatebox"
                                  validType="byteLength[1,256]"></textarea></td>
                </tr>
            </table>
        </jodd:form>
    </form>
</div>
