

package com.acooly.module.obs.client.oss;

import static com.acooly.module.obs.client.oss.OSSConstants.OSS_AUTHORIZATION_PREFIX;
import static com.acooly.module.obs.client.oss.OSSConstants.OSS_AUTHORIZATION_SEPERATOR;

public class OSSUtils {

  public static String composeRequestAuthorization(String accessKeyId, String signature) {
    return OSS_AUTHORIZATION_PREFIX + accessKeyId + OSS_AUTHORIZATION_SEPERATOR + signature;
  }
}
