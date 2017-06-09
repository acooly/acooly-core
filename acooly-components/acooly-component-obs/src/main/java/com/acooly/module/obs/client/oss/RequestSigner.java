

package com.acooly.module.obs.client.oss;

import com.acooly.module.obs.exceptions.ClientException;

public interface RequestSigner {

  public void sign(RequestMessage request) throws ClientException;
}
