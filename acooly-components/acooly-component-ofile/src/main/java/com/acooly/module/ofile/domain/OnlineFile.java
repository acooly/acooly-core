/**
 * create by zhangpu
 * date:2015年5月16日
 */
package com.acooly.module.ofile.domain;

import com.acooly.core.common.domain.AbstractEntity;
import com.acooly.module.ofile.enums.OFileType;

import javax.persistence.*;
import java.util.Date;

/**
 * @author zhangpu
 *
 */
@Entity
@Table(name = "sys_ofile")
public class OnlineFile extends AbstractEntity {

	/** serialVersionUID */
	private static final long serialVersionUID = -2665757624295221489L;

	@Id
	@GeneratedValue
	protected Long id;

	/** 文件对象ID(唯一标识) */
	@Column(name = "object_id", nullable = false, length = 64, columnDefinition = "varchar(64) not null COMMENT '对象ID'")
	private String objectId;

	/** 表单名称 */
	@Column(name = "input_name", nullable = false, length = 64, columnDefinition = "varchar(64) not null COMMENT '表单名称'")
	private String inputName;

	/** 文件名 */
	@Column(name = "file_name", nullable = false, length = 64, columnDefinition = "varchar(64) not null COMMENT '文件名'")
	private String fileName;

	/** 文件类型 */
	@Column(name = "file_type", nullable = false, length = 16, columnDefinition = "varchar(16) not null COMMENT '文件类型'")
	@Enumerated(EnumType.STRING)
	private OFileType fileType;

	/** 模块分类,如:客户，积分等根据业务的分类 */
	@Column(name = "module", nullable = true, length = 16, columnDefinition = "varchar(16) COMMENT '模块分类'")
	private String module;

	/** 扩展名 */
	@Column(name = "file_ext", nullable = true, length = 8, columnDefinition = "varchar(8)  COMMENT '扩展名'")
	private String fileExt;

	/** 文件大小(byte) */
	@Column(name = "file_size", nullable = false, columnDefinition = "bigint COMMENT '文件大小'")
	private long fileSize;

	/** 存储位置 */
	@Column(name = "file_path", nullable = false, length = 128, columnDefinition = "varchar(128) not null COMMENT '图片路径'")
	private String filePath;

	/** 图片缩略图 */
	@Column(name = "thumbnail", nullable = true, length = 128, columnDefinition = "varchar(128) COMMENT '缩略图路径'")
	private String thumbnail;

	@Column(name = "user_name", nullable = true, length = 32, columnDefinition = "varchar(32) COMMENT '用户名'")
	private String userName;

	/** 上传时间 */
	@Column(name = "create_time", columnDefinition = "datetime NOT NULL DEFAULT CURRENT_TIMESTAMP  COMMENT '创建时间'")
	private Date createTime = new Date();

	/** 更新时间 */
	@Column(name = "update_time", columnDefinition = "datetime COMMENT '最后更新时间'")
	private Date updateTime;

	@Column(name = "status", nullable = true, length = 16, columnDefinition = "varchar(16) DEFAULT 'enable' COMMENT '状态'")
	private String status = "enable";

	/** 原始文件名 */
	@Column(name = "original_name", nullable = false, length = 64, columnDefinition = "varchar(64) not null COMMENT '原始文件名'")
	private String originalName;

	/** 文件元数据 */
	@Column(name = "metadatas", nullable = true, length = 255, columnDefinition = "varchar(255) COMMENT '元数据，可以是JSON格式'")
	private String metadatas;

	/** 备注 */
	@Column(name = "comments", nullable = true, length = 255, columnDefinition = "varchar(255) COMMENT '备注'")
	private String comments;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getObjectId() {
		return objectId;
	}

	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public OFileType getFileType() {
		return fileType;
	}

	public void setFileType(OFileType fileType) {
		this.fileType = fileType;
	}

	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}

	public String getFileExt() {
		return fileExt;
	}

	public void setFileExt(String fileExt) {
		this.fileExt = fileExt;
	}

	public long getFileSize() {
		return fileSize;
	}

	public void setFileSize(long fileSize) {
		this.fileSize = fileSize;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getOriginalName() {
		return originalName;
	}

	public void setOriginalName(String originalName) {
		this.originalName = originalName;
	}

	public String getMetadatas() {
		return metadatas;
	}

	public void setMetadatas(String metadatas) {
		this.metadatas = metadatas;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getInputName() {
		return inputName;
	}

	public void setInputName(String inputName) {
		this.inputName = inputName;
	}

	@Override
	public String toString() {
		return String.format("OnlineFile: {id:%s, inputName:%s, fileName:%s, fileType:%s, fileSize:%s}", id, inputName,
				fileName, fileType, fileSize);
	}

}
