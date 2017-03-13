<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/manage/common/taglibs.jsp"%>
<script type="text/javascript">
$(function() {
	$.acooly.framework.createUploadify({
				/** 上传导入的URL */
				url:'/manage/point/pointAccount/importJson.html?_csrf=${requestScope["org.springframework.security.web.csrf.CsrfToken"].token}&splitKey=v',
				/** 导入操作消息容器 */
				messager:'manage_pointAccount_import_uploader_message',
				/** 上传导入文件表单ID */
				uploader:'manage_pointAccount_import_uploader_file',
				jsessionid:'<%=request.getSession().getId()%>'
	});	
});

function onUploadSuccess(file, data, response) {
	var result = $.parseJSON(data);
	if(result.success){
		$('#manage_pointAccount_import_uploader_message').html(result.message);
	}else{
		$('#manage_pointAccount_import_uploader_message').html('导入失败:'+result.message);
	}
}
</script>
<div align="center">
<table class="tableForm" width="100%">
  <tr>
    <th width="30%">文件类型：</th>
    <td>
    1: 目前支持的格式包括：Excel2000(*.xls)。<br/>
 	2：电子表格依次排列<span style="color: red">【用户名，发放积分，发放备注】</span>，其他字段无效<br/>
<!--     根据文件扩展名自动适配导入文件类型，目前支持的格式包括：Excel2000(*.xls)和CSV(*.csv)。 请<a href="/manage/point/template/pointTemplate.xls">下载模板文件</a>。 -->
    </td>
  </tr>	
  <tr>
    <th width="30%" height="15"></th>
    <td><div id="manage_pointAccount_import_uploader_message" style="color: red;"></div></td>
  </tr>          				
  <tr>
    <th width="30%">文件：</th>
    <td>
    <input type="file" name="manage_pointAccount_import_uploader_file" id="manage_pointAccount_import_uploader_file" />
    </td>
  </tr>		
</table>
</div>
