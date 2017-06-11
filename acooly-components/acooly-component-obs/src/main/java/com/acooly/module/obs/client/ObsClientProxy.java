package com.acooly.module.obs.client;

import com.acooly.module.obs.ObsProperties;
import com.acooly.module.obs.exceptions.ClientException;
import com.acooly.module.obs.exceptions.ObsException;
import com.acooly.module.obs.model.ObjectMetadata;
import com.acooly.module.obs.model.ObsObject;
import com.acooly.module.obs.model.ObjectResult;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.Map;

/** @author shuijing */
@Service("obsClient")
public class ObsClientProxy implements ObsClient, ApplicationContextAware {

  private String provider;

  private Object object = new Object();

  private ApplicationContext applicationContext;

  private ObsClient obsClient;

  @Autowired private ObsProperties obsProperties;

  public String getProvider() {
    return getObsClient().getProvider();
  }

  public ObsClient getObsClient() {
    if (obsClient == null) {
      synchronized (object) {
        if (obsClient == null) {
          obsClient =
              (ObsClient) this.applicationContext.getBean(obsProperties.getProvider().code());
        }
      }
    }
    return obsClient;
  }

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    this.applicationContext = applicationContext;
  }

  @Override
  public ObjectResult putObject(String bucketName, String key, File file)
      throws ObsException, ClientException {
    return getObsClient().putObject(bucketName, key, file);
  }

  @Override
  public ObjectResult putObject(String bucketName, String key, InputStream input)
      throws ObsException, ClientException {
    return getObsClient().putObject(bucketName, key, input);
  }

  @Override
  public ObjectResult putObject(String bucketName, String key, File file, ObjectMetadata metadata)
      throws ObsException, ClientException {
    return null;
  }

  @Override
  public ObjectResult putObject(
      String bucketName, String key, InputStream input, ObjectMetadata metadata)
      throws ObsException, ClientException {
    return null;
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
}
