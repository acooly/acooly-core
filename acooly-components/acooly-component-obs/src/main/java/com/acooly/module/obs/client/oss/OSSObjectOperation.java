package com.acooly.module.obs.client.oss;

import com.acooly.module.obs.common.HttpMethod;
import com.acooly.module.obs.exceptions.ClientException;
import com.acooly.module.obs.exceptions.ObsException;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.util.zip.CheckedInputStream;

import static com.acooly.module.obs.client.oss.OSSUtils.trimQuotes;

/** @author shuijing */
public class OSSObjectOperation {

  public static final PutObjectReponseParser putObjectReponseParser = new PutObjectReponseParser();

  private <RequestType extends PutObjectRequest, ResponseType> ResponseType writeObjectInternal(
      WriteMode mode, RequestType originalRequest, ResponseParser<ResponseType> responseParser) {

    return null;
  }

  public PutObjectResult putObject(PutObjectRequest putObjectRequest)
      throws ObsException, ClientException {

    // assertParameterNotNull(putObjectRequest, "putObjectRequest");

    PutObjectResult result = null;
    result = writeObjectInternal(WriteMode.OVERWRITE, putObjectRequest, putObjectReponseParser);

    //        if (isCrcCheckEnabled()) {
    //            OSSUtils.checkChecksum(result.getClientCRC(), result.getServerCRC(), result.getRequestId());
    //        }

    return result;
  }

  private static enum WriteMode {

    /* If object already not exists, create it. otherwise, append it with the new input */
    APPEND("APPEND"),

    /* No matter object exists or not, just overwrite it with the new input */
    OVERWRITE("OVERWRITE");

    private final String modeAsString;

    private WriteMode(String modeAsString) {
      this.modeAsString = modeAsString;
    }

    @Override
    public String toString() {
      return this.modeAsString;
    }

    public static HttpMethod getMappingMethod(WriteMode mode) {
      switch (mode) {
        case APPEND:
          return HttpMethod.POST;

        case OVERWRITE:
          return HttpMethod.PUT;

        default:
          throw new IllegalArgumentException("Unsuported write mode" + mode.toString());
      }
    }
  }

  public static final class PutObjectReponseParser implements ResponseParser<PutObjectResult> {

    @Override
    public PutObjectResult parse(ResponseMessage response) throws ResponseParseException {
      PutObjectResult result = new PutObjectResult();
      try {
        result.setETag(trimQuotes(response.getHeaders().get(OSSHeaders.ETAG)));
        result.setRequestId(response.getRequestId());
        //setCRC(result, response);
        return result;
      } finally {
        safeCloseResponse(response);
      }
    }
  }

  public static void safeCloseResponse(ResponseMessage response) {
    try {
      response.close();
    } catch (IOException e) {
    }
  }

  public interface ResponseParser<T> {
    public T parse(ResponseMessage response) throws ResponseParseException;
  }
}
