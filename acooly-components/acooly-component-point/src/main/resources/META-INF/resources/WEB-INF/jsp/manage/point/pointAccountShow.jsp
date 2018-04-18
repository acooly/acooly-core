<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/jsp/manage/common/taglibs.jsp" %>
<div style="padding: 5px;font-family:微软雅黑;">
    <table class="tableForm" width="100%">
        <tr>
            <th>ID:</th>
            <td>${pointAccount.id}</td>
        </tr>
        <tr>
            <th width="25%">用户名:</th>
            <td>${pointAccount.userName}</td>
        </tr>
        <tr>
            <th>积分余额:</th>
            <td>${pointAccount.balance}</td>
        </tr>
        <tr>
            <th>冻结:</th>
            <td>${pointAccount.freeze}</td>
        </tr>
        <tr>
            <th>状态:</th>
            <td>${pointAccount.status.message}</td>
        </tr>
        <tr>
            <th>用户等级:</th>
            <td>${allPointGrades[pointAccount.gradeId]}</td>
        </tr>
        <tr>
            <th>创建时间:</th>
            <td><fmt:formatDate value="${pointAccount.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
        </tr>
        <tr>
            <th>修改时间:</th>
            <td><fmt:formatDate value="${pointAccount.updateTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
        </tr>
        <tr>
            <th>备注:</th>
            <td>${pointAccount.memo}</td>
        </tr>
    </table>
</div>
