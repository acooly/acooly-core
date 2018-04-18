<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/jsp/manage/common/taglibs.jsp" %>
<div>
    <form id="manage_pointStatistics_editform"
          action="${pageContext.request.contextPath}/manage/point/pointStatistics/${action=='create'?'saveJson':'updateJson'}.html"
          method="post">
        <jodd:form bean="pointStatistics" scope="request">
            <input name="id" type="hidden"/>
            <table class="tableForm" width="100%">
                <tr>
                    <th width="25%">用户名：</th>
                    <td><input type="text" name="userName" size="48" class="easyui-validatebox text" data-options="required:true"
                               validType="byteLength[1,32]"/></td>
                </tr>
                <tr>
                    <th>统计条数：</th>
                    <td><input type="text" name="num" size="48" class="easyui-numberbox text" validType="byteLength[1,19]"/></td>
                </tr>
                <tr>
                    <th>积分余额：</th>
                    <td><input type="text" name="point" size="48" class="easyui-numberbox text" data-options="required:true"
                               validType="byteLength[1,19]"/></td>
                </tr>
                <tr>
                    <th>开始时间：</th>
                    <td><input type="text" name="startTime" size="20" class="easyui-validatebox text"
                               value="<fmt:formatDate value="${pointStatistics.startTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
                               onFocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd HH:mm:ss'})" data-options="required:true"/></td>
                </tr>
                <tr>
                    <th>结束时间：</th>
                    <td><input type="text" name="endTime" size="20" class="easyui-validatebox text"
                               value="<fmt:formatDate value="${pointStatistics.endTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
                               onFocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd HH:mm:ss'})" data-options="required:true"/></td>
                </tr>
                <tr>
                    <th>状态：</th>
                    <td><select name="status" editable="false" style="height:27px;" panelHeight="auto" class="easyui-combobox"
                                data-options="required:true">
                        <c:forEach items="${allStatuss}" var="e">
                            <option value="${e.key}">${e.value}</option>
                        </c:forEach>
                    </select></td>
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
