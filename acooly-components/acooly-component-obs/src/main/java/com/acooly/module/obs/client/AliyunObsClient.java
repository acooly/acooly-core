package com.acooly.module.obs.client;

import com.acooly.module.obs.ObsProperties;
import com.acooly.module.obs.client.oss.OSSObjectOperation;
import com.acooly.module.obs.client.oss.PutObjectRequest;
import com.acooly.module.obs.client.oss.PutObjectResult;
import com.acooly.module.obs.exceptions.ClientException;
import com.acooly.module.obs.exceptions.ObsException;
import com.acooly.module.obs.model.ObjectMetadata;
import com.acooly.module.obs.model.ObjectResult;
import com.acooly.module.obs.model.ObsObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.Map;

import static com.acooly.module.obs.client.oss.OSSConstants.DEFAULT_OSS_VISITE_URL;

/** @author shuijing */
@Service("aliyunObsClient")
public class AliyunObsClient extends AbstractObsClient {

  @Autowired private ObsProperties properties;

  private OSSObjectOperation objectOperation;

  @Override
  public String getProvider() {
    return "阿里OSS";
  }

  protected ObjectResult convertResult(PutObjectResult putObjectResult, String key) {
    ObjectResult result = new ObjectResult();
    result.setCode(String.valueOf(putObjectResult.getResponse().getStatusCode()));
    result.setMessage("put successful");
    result.setSuccess(true);
    //http://yijifu-acooly.oss.aliyuncs.com/test/logo.png
    String url =
        "http://" + putObjectResult.getBuketName() + "." + DEFAULT_OSS_VISITE_URL + "/" + key;
    result.setUrl(url);
    return result;
  }

  @Override
  public ObjectResult putObject(String bucketName, String key, File file)
      throws ObsException, ClientException {
    PutObjectResult putObjectResult =
        objectOperation.putObject(new PutObjectRequest(bucketName, key, file, null));
    return convertResult(putObjectResult, key);
  }

  @Override
  public ObjectResult putObject(String bucketName, String key, InputStream input)
      throws ObsException, ClientException {
    PutObjectResult putObjectResult =
        objectOperation.putObject(new PutObjectRequest(bucketName, key, input));
    return convertResult(putObjectResult, key);
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

  @Override
  public void afterPropertiesSet() throws Exception {

    this.objectOperation = new OSSObjectOperation(properties);
  }
}
