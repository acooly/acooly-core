###  1. openapi访问路径

openapi访问路径请使用`http://ip:port/gateway.do`

###  2. 测试帐号

    partnerId=test
    key=06f7aab08aa2431e6dae6a156fc9e0b4


### 3. 如果自定义异常处理

有些场景，我们需要对特殊的异常返回比较友好的响应报文，可以扩展默认的api异常处理器。

    @Component
    //@Primary注解会替换默认的ApiServiceExceptionHander实现
    @Primary
    @Slf4j
    public class CustomApiServiceExceptionHander implements ApiServiceExceptionHander {
        @Override
        public void handleApiServiceException(ApiRequest apiRequest, ApiResponse apiResponse, Throwable ase) {
            if (ApiServiceException.class.isAssignableFrom(ase.getClass())) {
                handleApiServiceException(apiResponse, (ApiServiceException) ase);
            } else {
                String serviceName="";
                if(apiRequest != null){
                    serviceName=apiRequest.getService();
                }
                log.error("处理服务[{}]异常",serviceName,ase);
                handleInternalException(apiResponse);
            }
        }

        /**
         * 服务异常处理
         *
         * @param apiResponse
         * @param ase
         */
        protected void handleApiServiceException(ApiResponse apiResponse, ApiServiceException ase) {
            //修改响应
            apiResponse.setResultCode(ase.getResultCode());
            apiResponse.setResultMessage(ase.getResultMessage());
            apiResponse.setResultDetail(ase.getDetail());
        }

        /**
         * 系统异常处理
         *
         * @param apiResponse
         */
        protected void handleInternalException(ApiResponse apiResponse) {
            //修改响应
            apiResponse.setResultCode(ApiServiceResultCode.INTERNAL_ERROR.code());
            apiResponse.setResultMessage(ApiServiceResultCode.INTERNAL_ERROR.message());
        }
    }