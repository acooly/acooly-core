<div style="padding: 5px; font-family: 微软雅黑;">
	<table class="tableForm" width="100%">
		<tr>
			<th>ID:</th>
			<td>${onlineFile.id}</td>
		</tr>
		<tr>
			<th>对象ID:</th>
			<td>${onlineFile.objectId}</td>
		</tr>		
		<tr>
			<th>表单名:</th>
			<td>${onlineFile.inputName}</td>
		</tr>
		<tr>
			<th>文件名:</th>
			<td>${onlineFile.fileName}</td>
		</tr>
		<tr>
			<th>原始文件名:</th>
			<td>${onlineFile.originalName}</td>
		</tr>	
		<tr>
			<th>文件类型:</th>
			<td>${onlineFile.fileType}</td>
		</tr>	
		<tr>
			<th>文件大小:</th>
			<td>${onlineFile.fileSize}</td>
		</tr>
		<#if onlineFile.thumbnail??>
		<tr>
			<th>缩略图:</th>
			<td>
			<a class="online_file_image" href="/media/${onlineFile.filePath}"><img src="/media${onlineFile.thumbnail}"></a>
			</td>
		</tr>
		<script>
			$('.online_file_image').fancybox();
		</script>
		</#if>		
		<tr>
			<th>文件路径:</th>
			<td>${onlineFile.filePath}</td>
		</tr>
		<tr>
			<th>元数据:</th>
			<td>${onlineFile.metadatas}</td>
		</tr>
		<tr>
			<th>模块分类:</th>
			<td>${onlineFile.module}</td>
		</tr>
		<tr>
			<th>用户名:</th>
			<td>${onlineFile.userName}</td>
		</tr>
		<tr>
			<th>状态:</th>
			<td>${onlineFile.status}</td>
		</tr>
		<tr>
			<th>上传时间:</th>
			<td>${onlineFile.createTime}</td>
		</tr>
		<tr>
			<th width="25%">备注:</th>
			<td>${onlineFile.comments}</td>
		</tr>		
	</table>
</div>