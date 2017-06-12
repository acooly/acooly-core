package com.acooly.module.obs;

import com.acooly.module.obs.exceptions.ClientException;
import com.acooly.module.obs.exceptions.ObsException;
import com.acooly.module.obs.model.ObjectMetadata;
import com.acooly.module.obs.model.ObsObject;
import com.acooly.module.obs.model.ObjectResult;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.Map;

/**
 * 对象存储服务访问接口
 *
 * @author shuijing
 */
public interface ObsService {

  ObjectResult putObject(String bucketName, String key, File file)
      throws ObsException, ClientException;

  ObjectResult putObject(String bucketName, String key, InputStream input)
      throws ObsException, ClientException;

  ObjectResult putObject(String bucketName, String key, File file, ObjectMetadata metadata)
      throws ObsException, ClientException;

  ObjectResult putObject(
      String bucketName, String key, InputStream input, ObjectMetadata metadata)
      throws ObsException, ClientException;

  ObjectResult putObject(URL signedUrl, String filePath, Map<String, String> requestHeaders)
      throws ObsException, ClientException;

  ObjectResult putObject(
      URL signedUrl,
      InputStream requestContent,
      long contentLength,
      Map<String, String> requestHeaders)
      throws ObsException, ClientException;

  ObsObject  getObject(String bucketName, String key) throws ObsException, ClientException;

  void deleteObject(String bucketName, String key) throws ObsException, ClientException;
}
