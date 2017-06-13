package com.acooly.module.obs;

import com.acooly.module.obs.client.ObsClient;
import com.acooly.module.obs.exceptions.ClientException;
import com.acooly.module.obs.exceptions.ObsException;
import com.acooly.module.obs.model.ObjectMetadata;
import com.acooly.module.obs.model.ObsObject;
import com.acooly.module.obs.model.ObjectResult;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.Map;

/** @author shuijing */
@Service("ObsService")
public class ObsServiceImpl implements InitializingBean, ObsService {

  @Autowired ObsClient obsClient;

  @Override
  public ObjectResult putObject(String bucketName, String key, File file)
      throws ObsException, ClientException {
    return obsClient.putObject(bucketName, key, file);
  }

  @Override
  public ObjectResult putObject(String bucketName, String key, InputStream input)
      throws ObsException, ClientException {
    return obsClient.putObject(bucketName, key, input);
  }

  @Override
  public ObjectResult putObject(String bucketName, String key, File file, ObjectMetadata metadata)
      throws ObsException, ClientException {
    return obsClient.putObject(bucketName, key, file, metadata);
  }

  @Override
  public ObjectResult putObject(
      String bucketName, String key, InputStream input, ObjectMetadata metadata)
      throws ObsException, ClientException {
    return obsClient.putObject(bucketName, key, input, metadata);
  }

  @Override
  public ObjectResult putObject(URL signedUrl, String filePath, Map<String, String> requestHeaders)
      throws ObsException, ClientException {
    return null;
  }

  @Override
  public ObjectResult putObject(
      URL signedUrl,
      InputStream requestContent,
      long contentLength,
      Map<String, String> requestHeaders)
      throws ObsException, ClientException {
    return null;
  }

  @Override
  public ObsObject getObject(String bucketName, String key) throws ObsException, ClientException {
    return null;
  }

  @Override
  public void deleteObject(String bucketName, String key) throws ObsException, ClientException {}

  @Override
  public void afterPropertiesSet() throws Exception {}
}
