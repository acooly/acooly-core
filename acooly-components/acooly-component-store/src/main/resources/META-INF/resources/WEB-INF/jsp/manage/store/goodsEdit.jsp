<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/jsp/manage/common/taglibs.jsp" %>
<div>
    <form id="manage_goods_editform"
          action="${pageContext.request.contextPath}/manage/store/goods/${action=='create'?'saveJson':'updateJson'}.html" method="post">
        <jodd:form bean="goods" scope="request">
            <input name="id" type="hidden"/>
            <table class="tableForm" width="100%">
                <tr>
                    <th width="25%">仓库编码：</th>
                    <td><input type="text" name="storeCode" size="48" class="easyui-validatebox text" validType="byteLength[1,32]"/></td>
                </tr>
                <tr>
                    <th>品类ID：</th>
                    <td><input type="text" name="categroryId" size="48" class="easyui-numberbox text" data-options="required:true"
                               validType="byteLength[1,19]"/></td>
                </tr>
                <tr>
                    <th>品类名称：</th>
                    <td><input type="text" name="categroryName" size="48" class="easyui-validatebox text" validType="byteLength[1,32]"/>
                    </td>
                </tr>
                <tr>
                    <th>商品编码：</th>
                    <td><input type="text" name="code" size="48" class="easyui-validatebox text" validType="byteLength[1,32]"/></td>
                </tr>
                <tr>
                    <th>name：</th>
                    <td><input type="text" name="name" size="48" class="easyui-validatebox text" validType="byteLength[1,32]"/></td>
                </tr>
                <tr>
                    <th>商品简介：</th>
                    <td><textarea rows="3" cols="40" style="width:300px;" name="descn" class="easyui-validatebox"
                                  validType="byteLength[1,255]"></textarea></td>
                </tr>
                <tr>
                    <th>单价：</th>
                    <td><input type="text" name="price" size="48" class="easyui-numberbox text" validType="byteLength[1,19]"/></td>
                </tr>
                <tr>
                    <th>单位：</th>
                    <td><input type="text" name="unit" size="48" class="easyui-validatebox text" validType="byteLength[1,16]"/></td>
                </tr>
                <tr>
                    <th>库存：</th>
                    <td><input type="text" name="stock" size="48" class="easyui-numberbox text" validType="byteLength[1,10]"/></td>
                </tr>
                <tr>
                    <th>型号：</th>
                    <td><input type="text" name="model" size="48" class="easyui-validatebox text" validType="byteLength[1,32]"/></td>
                </tr>
                <tr>
                    <th>品牌：</th>
                    <td><input type="text" name="brand" size="48" class="easyui-validatebox text" validType="byteLength[1,32]"/></td>
                </tr>
                <tr>
                    <th>供应商：</th>
                    <td><input type="text" name="supplier" size="48" class="easyui-validatebox text" validType="byteLength[1,32]"/></td>
                </tr>
                <tr>
                    <th>展示地址：</th>
                    <td><input type="text" name="detailUrl" size="48" class="easyui-validatebox text" validType="byteLength[1,128]"/></td>
                </tr>
                <tr>
                    <th>备注：</th>
                    <td><textarea rows="3" cols="40" style="width:300px;" name="comments" class="easyui-validatebox"
                                  validType="byteLength[1,255]"></textarea></td>
                </tr>
            </table>
        </jodd:form>
    </form>
</div>
