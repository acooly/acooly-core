## 1. 组件介绍
此组件提供spring mvc 的能力

## 2. FAQ

### 2.1 通过配置直接访问模板页面

通过配置`acooly.web.simplePageMap.app=/welcome.htm->welcome`  
通过上面的例子配置，当用户访问`/welcome.htm`地址时，会渲染`welcome`ftl模板，找不到模板，找jsp。多个地址映射，用逗号分隔


### 2.2 如何获取http请求body体?

在filter中调用了request.getParameter,此时,已经对流解析了,如果应用中再调用request.getInputStream会读不到流里的内容。在我们的应用代码中直接解析流获取内容，会遇到数据为空的情况。

可以用下面两种方式解决:

#### 2.2.1 方式一：重新组装InputStrean

    	@RequestMapping(method = RequestMethod.POST, value = "/testGetInput")
    	@ResponseBody
    	public String testGetInput(HttpServletRequest request) throws Exception {
    		ServletServerHttpRequest servletServerHttpRequest = new ServletServerHttpRequest(request);
    		return URLDecoder.decode(Streams.asString(servletServerHttpRequest.getBody(), "utf-8"), "utf-8");
    	}

#### 2.2.2 方式二：使用spring提供的annotation
    	
    	@RequestMapping(method = RequestMethod.POST, value = "/testGetInput1")
    	@ResponseBody
    	public String testGetInput1(@RequestBody String body) throws Exception {
    		return URLDecoder.decode(body, "utf-8");
    	}
    	
### 2.3 如何自定义http request encoding？

`javax.servlet.ServletRequest#setCharacterEncoding`,This method must be called prior to reading request parameters or reading input using getReader().

所以，我们通常需要把设置编码的filter放在第一位。某些场景，我们需要通过请求中的参数去设置编码，我们需要通过如下步骤来实现。

1. 关闭默认CharacterEncodingFilter

		@Bean
		public FilterRegistrationBean disableOrderedCharacterEncodingFilter(CharacterEncodingFilter characterEncodingFilter) {
			FilterRegistrationBean registrationBean = new FilterRegistrationBean(characterEncodingFilter);
			registrationBean.setEnabled(false);
			return registrationBean;
		}
		
2. 编写自定义filter

		public static class CustomCharacterEncodingFilter extends OncePerRequestFilter {
			//编码服务
			private volatile EncodingService encodingService;
			
			@Override
			protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
											FilterChain filterChain) throws ServletException, IOException {
				if (encodingService == null) {
					encodingService = ApplicationContextHolder.get().getBean(EncodingService.class);
				}
				//通过url获取编码
				String encoding = encodingService.findEncodingByUrl(request.getRequestURL().toString());
				request.setCharacterEncoding(encoding);
				filterChain.doFilter(request, response);
			}
		}
		
3. 注册filter

		@Bean
		public FilterRegistrationBean customCharacterEncodingFilter() {
			FilterRegistrationBean registrationBean = new FilterRegistrationBean();
			registrationBean.setFilter(new CustomCharacterEncodingFilter());
			//设置为最高优先级
			registrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE);
			return registrationBean;
		}
		
	
### 2.4 如何修改http缓存时间？
    
通过`acooly.web.cacheMaxAge`修改http 缓存时间,-1=不设置,0=第二次请求需要和服务器协商,大于0=经过多少秒后才过期。