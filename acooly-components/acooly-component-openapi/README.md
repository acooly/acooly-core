###  1. openapi访问路径

openapi访问路径请使用`http://ip:port/gateway.do`（此路径不会过一些无用的filter）

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



## openapi层一些通用处理模式

### 1. `ApiRequest` -> `OrderBase`

openapi收到外部请求后，需要转换为内部请求对象调用业务系统。

#### 1.1. ApiRequest#toOrder

把`ApiRequest` 转换为 `OrderBase`，并设置`gid`。

如果是`BizOrderBase`，会新创建bizOrderNo。

#### 1.2. PageApiRequest#toOrder

如果内部请求对象为`PageOrder`，会设置`PageInfo`.


有了以上的方法，代码可以简化为：

	QueryTradePageOrder order = request.toOrder(QueryTradePageOrder.class);
	OfflinePayOrder order = request.toOrder(OfflinePayOrder.class);


### 2. `ResultBase` -> `ApiResponse`

#### 2.1 异常处理

对于同步服务，下层服务返回非成功时，直接抛出异常，由api框架处理响应码。

	result.throwIfNotSuccess();

对于异步服务，下层服务返回失败时，直接抛出异常，由api框架处理响应码。

	result.throwIfFailure()

#### 2.1 查询

分页查询时，需要把`PageResult`转换为`PageApiResponse`。

	PageApiResponse#setPageResult(com.acooly.core.common.facade.PageResult<U>)
	PageApiResponse#setPageResult(com.acooly.core.common.facade.PageResult<U>, BiConsumer<U,T>)

代码可以简化为：

	 //构建dubbo 请求对象
	 QueryTradePageResult result =
        queryTradeFacade.queryTradePage(request.toOrder(QueryTradePageOrder.class));
    //判断响应状态
    result.throwIfNotSuccess();
    //设置响应
    response.setPageResult(result);

或者：

	 //构建dubbo 请求对象
	 QueryFundPageOrder order = request.toOrder(QueryFundPageOrder.class);
	 //请求dubbo服务
	 QueryFundPageResult result = queryFundFacade.queryFundPage(order);
	 //判断响应状态
    result.throwIfNotSuccess();
    //设置响应，转换特殊参数，把银行图片转换为绝对路径。
    response.setPageResult(
        result, (fundDto, fundInfo) -> fundInfo.setBankImage(getAccessUrl(fundDto.getBankImage())));

#### 2.2 命令

####  2.2.1. 同步命令

同步命令请求响应流程如下：

1. 构建dubbo请求对象
2. 发起dubbo调用
3. 处理响应异常
4. dubbo响应对象转换为api响应对象。

代码如下：

		 tradeFacade
	        .balancePay(request.toOrder(BalancePayOrder.class))
	        .throwIfNotSuccess() //当请求不成功时，抛出异常，由api框架处理
	        .copyTo(response); //dubbo响应拷贝为api响应

####  2.2.1. 异步命令

同步命令请求响应流程如下：

1. 构建dubbo请求对象
2. 发起dubbo调用
3. 处理响应异常
4. dubbo响应对象转换为api响应对象。(当业务状态为处理中时，响应api为处理成功，状态码为处理中)

代码如下：

	  tradeFacade
        .deductPay(request.toOrder(DeductPayOrder.class))
        .throwIfFailure() //仅当处理状态为失败时，才抛出异常
        .ifProcessing(result -> response.setResult(ApiServiceResultCode.PROCESSING))//处理响应状态码，当响应状态为处理中时，api响应处理中。
        .copyTo(response);//dubbo响应拷贝为api响应