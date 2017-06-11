package com.acooly.module.obs.model;

import com.acooly.module.obs.common.util.DateUtil;
import com.acooly.module.obs.common.HttpHeaders;

import java.text.ParseException;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/** Object的元数据。 */
public class ObjectMetadata {

  // 用户自定义的元数据，表示以x-oss-meta-为前缀的请求头。
  private Map<String, String> userMetadata = new HashMap<String, String>();

  // 非用户自定义的元数据。
  private Map<String, Object> metadata = new HashMap<String, Object>();

  public static final String AES_256_SERVER_SIDE_ENCRYPTION = "AES256";

  /**
   * 获取用户自定义的元数据。
   *
   * <p>OSS内部保存用户自定义的元数据时，会以x-oss-meta-为请求头的前缀。 但用户通过该接口处理用户自定义元数据里，不需要加上前缀“x-oss-meta-”。
   * 同时，元数据字典的键名是不区分大小写的，并且在从服务器端返回时会全部以小写形式返回， 即使在设置时给定了大写字母。比如键名为：MyUserMeta，通过getObjectMetadata接口
   * 返回时键名会变为：myusermeta。
   *
   * @return 用户自定义的元数据。
   */
  public Map<String, String> getUserMetadata() {
    return userMetadata;
  }

  /**
   * 设置用户自定义的元数据，表示以x-oss-meta-为前缀的请求头。
   *
   * @param userMetadata 用户自定义的元数据。
   */
  public void setUserMetadata(Map<String, String> userMetadata) {
    this.userMetadata.clear();
    if (userMetadata != null && !userMetadata.isEmpty()) {
      this.userMetadata.putAll(userMetadata);
    }
  }

  /**
   * 设置请求头（内部使用）。
   *
   * @param key 请求头的Key。
   * @param value 请求头的Value。
   */
  public void setHeader(String key, Object value) {
    metadata.put(key, value);
  }

  /**
   * 添加一个用户自定义的元数据。
   *
   * @param key 请求头的Key。 这个Key不需要包含OSS要求的前缀，即不需要加入“x-oss-meta-”。
   * @param value 请求头的Value。
   */
  public void addUserMetadata(String key, String value) {
    this.userMetadata.put(key, value);
  }

  /**
   * 获取Last-Modified请求头的值，表示Object最后一次修改的时间。
   *
   * @return Object最后一次修改的时间。
   */
  public Date getLastModified() {
    return (Date) metadata.get(HttpHeaders.LAST_MODIFIED);
  }

  /**
   * 设置Last-Modified请求头的值，表示Object最后一次修改的时间（内部使用）。
   *
   * @param lastModified Object最后一次修改的时间。
   */
  public void setLastModified(Date lastModified) {
    metadata.put(HttpHeaders.LAST_MODIFIED, lastModified);
  }

  /**
   * 获取Expires响应头，返回其Rfc822日期表示形式。 如果Object没有定义过期时间，则返回null。
   *
   * @return Expires响应头的Rfc822日期表示形式。
   * @throws ParseException 无法将Expires解析为Rfc822格式，抛出该异常。
   */
  public Date getExpirationTime() throws ParseException {
    return DateUtil.parseRfc822Date((String) metadata.get(HttpHeaders.EXPIRES));
  }

  /**
   * 获取原始的Expires响应头，不对其进行日期格式解析，返回其字符串表示形式。 如果Object没有定义过期时间，则返回null。
   *
   * @return 原始的Expires响应头。
   */
  public String getRawExpiresValue() {
    return (String) metadata.get(HttpHeaders.EXPIRES);
  }

  /**
   * 设置Expires请求头。
   *
   * @param expirationTime 过期时间。
   */
  public void setExpirationTime(Date expirationTime) {
    metadata.put(HttpHeaders.EXPIRES, DateUtil.formatRfc822Date(expirationTime));
  }

  /**
   * 获取Content-Length请求头，表示Object内容的大小。
   *
   * @return Object内容的大小。
   */
  public long getContentLength() {
    Long contentLength = (Long) metadata.get(HttpHeaders.CONTENT_LENGTH);
    return contentLength == null ? 0 : contentLength.longValue();
  }

  /**
   * 设置Content-Length请求头，表示Object内容的大小。 当上传Object到OSS时，请总是指定正确的content length。
   *
   * @param contentLength Object内容的大小。
   */
  public void setContentLength(long contentLength) {
    metadata.put(HttpHeaders.CONTENT_LENGTH, contentLength);
  }

  /**
   * 获取Content-Type请求头，表示Object内容的类型，为标准的MIME类型。
   *
   * @return Object内容的类型，为标准的MIME类型。
   */
  public String getContentType() {
    return (String) metadata.get(HttpHeaders.CONTENT_TYPE);
  }

  /**
   * 获取Content-Type请求头，表示Object内容的类型，为标准的MIME类型。
   *
   * @param contentType Object内容的类型，为标准的MIME类型。
   */
  public void setContentType(String contentType) {
    metadata.put(HttpHeaders.CONTENT_TYPE, contentType);
  }

  public String getContentMD5() {
    return (String) metadata.get(HttpHeaders.CONTENT_MD5);
  }

  public void setContentMD5(String contentMD5) {
    metadata.put(HttpHeaders.CONTENT_MD5, contentMD5);
  }

  /**
   * 获取Content-Encoding请求头，表示Object内容的编码方式。
   *
   * @return Object内容的编码方式。
   */
  public String getContentEncoding() {
    return (String) metadata.get(HttpHeaders.CONTENT_ENCODING);
  }

  /**
   * 设置Content-Encoding请求头，表示Object内容的编码方式。
   *
   * @param encoding 表示Object内容的编码方式。
   */
  public void setContentEncoding(String encoding) {
    metadata.put(HttpHeaders.CONTENT_ENCODING, encoding);
  }

  /**
   * 获取Cache-Control请求头，表示用户指定的HTTP请求/回复链的缓存行为。
   *
   * @return Cache-Control请求头。
   */
  public String getCacheControl() {
    return (String) metadata.get(HttpHeaders.CACHE_CONTROL);
  }

  /**
   * 设置Cache-Control请求头，表示用户指定的HTTP请求/回复链的缓存行为。
   *
   * @param cacheControl Cache-Control请求头。
   */
  public void setCacheControl(String cacheControl) {
    metadata.put(HttpHeaders.CACHE_CONTROL, cacheControl);
  }

  /**
   * 获取Content-Disposition请求头，表示MIME用户代理如何显示附加的文件。
   *
   * @return Content-Disposition请求头
   */
  public String getContentDisposition() {
    return (String) metadata.get(HttpHeaders.CONTENT_DISPOSITION);
  }

  /**
   * 设置Content-Disposition请求头，表示MIME用户代理如何显示附加的文件。
   *
   * @param disposition Content-Disposition请求头
   */
  public void setContentDisposition(String disposition) {
    metadata.put(HttpHeaders.CONTENT_DISPOSITION, disposition);
  }

  /**
   * 获取一个值表示与Object相关的hex编码的128位MD5摘要。
   *
   * @return 与Object相关的hex编码的128位MD5摘要。
   */
  public String getETag() {
    return (String) metadata.get(HttpHeaders.ETAG);
  }

  /**
   * 获取一个值表示Object的服务器加密的熵编码
   *
   * @return 服务器端加密的熵编码，null表示没有进行加密
   */
  //    public String getServerSideEncryption() {
  //        return (String)metadata.get(HttpHeaders.OSS_SERVER_SIDE_ENCRYPTION);
  //    }

  /**
   * 设置Object服务器端熵编码的类型
   *
   * @param 服务器端加密的熵编码类型
   */
  //    public void setServerSideEncryption(String serverSideEncryption) {
  //        metadata.put(HttpHeaders.OSS_SERVER_SIDE_ENCRYPTION, serverSideEncryption);
  //    }

  /**
   * 获取Object存储类型，目前支持Normal、Appendable两类。
   *
   * @return Object存储类型。
   */
  //    public String getObjectType() {
  //        return (String)metadata.get(HttpHeaders.OSS_OBJECT_TYPE);
  //    }

  /**
   * 设置Object访问权限，目前支持default, private, public-read, public-read-write四种访问权限。
   *
   * @param cannedAcl Object访问权限。
   */
  //    public void setObjectAcl(CannedAccessControlList cannedAcl) {
  //        metadata.put(HttpHeaders.OSS_OBJECT_ACL, cannedAcl != null ? cannedAcl.toString() : "");
  //    }

  /**
   * 返回内部保存的请求头的元数据（内部使用）。
   *
   * @return 内部保存的请求头的元数据（内部使用）。
   */
  public Map<String, Object> getRawMetadata() {
    return Collections.unmodifiableMap(metadata);
  }

  /**
   * 获取RequestId。
   *
   * @return RequestId。
   */
  //    public String getRequestId() {
  //        return (String)metadata.get(HttpHeaders.OSS_HEADER_REQUEST_ID);
  //    }

  /**
   * 获取文件的存储类型
   *
   * @return
   */
  //    public StorageClass getObjectStorageClass() {
  //        String storageClassString = (String)metadata.get(HttpHeaders.OSS_STORAGE_CLASS);
  //        if (storageClassString != null) {
  //            return StorageClass.parse(storageClassString);
  //        }
  //        return StorageClass.Standard;
  //    }

  /**
   * 获取Archive类型文件Restore状态
   *
   * @return 文件Restore状态
   */
  //    public String getObjectRawRestore() {
  //        return (String)metadata.get(HttpHeaders.OSS_RESTORE);
  //    }

  /**
   * 获取Archive文件Restore是否完成
   *
   * @return Restore是否完成
   */
  //    public boolean isRestoreCompleted() {
  //        String restoreString = getObjectRawRestore();
  //        if (restoreString == null) {
  //            throw new NullPointerException();
  //        }
  //
  //        if (restoreString.equals(HttpHeaders.OSS_ONGOING_RESTORE)) {
  //            return false;
  //        }
  //        return true;
  //    }
}
