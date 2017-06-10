package com.acooly.module.obs.client.oss;

import static com.acooly.module.obs.client.oss.OSSConstants.OSS_AUTHORIZATION_PREFIX;
import static com.acooly.module.obs.client.oss.OSSConstants.OSS_AUTHORIZATION_SEPERATOR;

public class OSSUtils {

  public static String composeRequestAuthorization(String accessKeyId, String signature) {
    return OSS_AUTHORIZATION_PREFIX + accessKeyId + OSS_AUTHORIZATION_SEPERATOR + signature;
  }

    public static String trimQuotes(String s) {

        if (s == null) {
            return null;
        }

        s = s.trim();
        if (s.startsWith("\"")) {
            s = s.substring(1);
        }
        if (s.endsWith("\"")) {
            s = s.substring(0, s.length() - 1);
        }

        return s;
    }
}
