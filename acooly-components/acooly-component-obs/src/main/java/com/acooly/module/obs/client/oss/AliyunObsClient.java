package com.acooly.module.obs.client.oss;

import com.acooly.module.obs.ObsProperties;
import com.acooly.module.obs.client.AbstractObsClient;
import com.acooly.module.obs.exceptions.ClientException;
import com.acooly.module.obs.exceptions.ObsException;
import com.acooly.module.obs.model.ObjectMetadata;
import com.acooly.module.obs.model.ObsObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.Map;

/**
 *
 * @author shuijing
 */
@Service("aliyunObsClient")
public class AliyunObsClient extends AbstractObsClient {

  @Autowired private ObsProperties properties;


    @Override
    public String getProvider() {
        return null;
    }

    @Override
    public String putObject(String bucketName, String key, InputStream input) throws ObsException, ClientException {
        return null;
    }

    @Override
    public String putObject(String bucketName, String key, InputStream input, ObjectMetadata metadata) throws ObsException, ClientException {
        return null;
    }

    @Override
    public String putObject(String bucketName, String key, File file, ObjectMetadata metadata) throws ObsException, ClientException {
        return null;
    }

    @Override
    public String putObject(String bucketName, String key, File file) throws ObsException, ClientException {
        return null;
    }

    @Override
    public String putObject(URL signedUrl, String filePath, Map<String, String> requestHeaders) throws ObsException, ClientException {
        return null;
    }

    @Override
    public String putObject(URL signedUrl, InputStream requestContent, long contentLength, Map<String, String> requestHeaders) throws ObsException, ClientException {
        return null;
    }

    @Override
    public ObsObject getObject(String bucketName, String key) throws ObsException, ClientException {
        return null;
    }

    @Override
    public void deleteObject(String bucketName, String key) throws ObsException, ClientException {

    }
}
