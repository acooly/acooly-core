<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/manage/common/taglibs.jsp"%>
<div>
    <form id="manage_store_editform" action="${pageContext.request.contextPath}/manage/store/store/${action=='create'?'saveJson':'updateJson'}.html" method="post">
      <jodd:form bean="store" scope="request">
        <input name="id" type="hidden" />
        <table class="tableForm" width="100%">
			<tr>
				<th width="25%">仓库编码：</th>
				<td><input type="text" name="storeCode" size="48" class="easyui-validatebox text" data-options="required:true" validType="byteLength[1,32]"/></td>
			</tr>					
			<tr>
				<th>仓库名称：</th>
				<td><input type="text" name="storeName" size="48" class="easyui-validatebox text" data-options="required:true" validType="byteLength[1,32]"/></td>
			</tr>					
			<tr>
				<th>备注：</th>
				<td><textarea rows="3" cols="40" style="width:300px;" name="comments" class="easyui-validatebox"  validType="byteLength[1,255]"></textarea></td>
			</tr>					
        </table>
      </jodd:form>
    </form>
</div>
