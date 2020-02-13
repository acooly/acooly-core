<!-- title: Web组件 -->
<!-- type: infrastructure -->
<!-- author: qiubo,zhangpu -->
<!-- date: 2019-12-23 -->
## 1. 组件介绍
此组件提供spring mvc 的能力

## 2. 使用说明

maven坐标：

     <dependency>
        <groupId>com.acooly</groupId>
        <artifactId>acooly-component-web</artifactId>
        <version>${acooly-latest-version}</version>
      </dependency>

`${acooly-latest-version}`为框架最新版本或者购买的版本。

## 3. FAQ

### 3.1 通过配置直接访问模板页面

通过配置`acooly.web.simplePageMap.app=/welcome.htm->welcome`  
通过上面的例子配置，当用户访问`/welcome.htm`地址时，会渲染`welcome`ftl模板，找不到模板，找jsp。多个地址映射，用逗号分隔


### 3.2 如何获取http请求body体?

在filter中调用了request.getParameter,此时,已经对流解析了,如果应用中再调用request.getInputStream会读不到流里的内容。在我们的应用代码中直接解析流获取内容，会遇到数据为空的情况。

可以用下面两种方式解决:

#### 3.2.1 方式一：重新组装InputStrean

    	@RequestMapping(method = RequestMethod.POST, value = "/testGetInput")
    	@ResponseBody
    	public String testGetInput(HttpServletRequest request) throws Exception {
    		ServletServerHttpRequest servletServerHttpRequest = new ServletServerHttpRequest(request);
    		return URLDecoder.decode(Streams.asString(servletServerHttpRequest.getBody(), "utf-8"), "utf-8");
    	}

#### 3.2.2 方式二：使用spring提供的annotation
    	
    	@RequestMapping(method = RequestMethod.POST, value = "/testGetInput1")
    	@ResponseBody
    	public String testGetInput1(@RequestBody String body) throws Exception {
    		return URLDecoder.decode(body, "utf-8");
    	}
    	
### 3.3 如何自定义http request encoding？

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
		
	
### 3.4 如何修改http缓存时间？
    
通过`acooly.web.cacheMaxAge`修改http 缓存时间,-1=不设置,0=第二次请求需要和服务器协商,大于0=经过多少秒后才过期。

### 3.5 关于jsp

1. jsp不能放在`assemble`模块，这会导致应用打成jar包后找不到jsp文件(spring-boot-maven-plugin插件生成了自定义jar包，并自定义了classloader，在初始化tomcat时，不会把assemble包下的`resources/META-INF`加入到tomcat的`StandardRoot`，代码参考`TomcatResources#addClasspathResources`)
2. jsp文件必须放非`assemble`模块下的`src/main/resources/META-INF/resources/WEB-INF/jsp`路径

### 3.5 freemarker 扩展标签

### 3.5.1. **#include**

该标签的作用是将便签中指定的路径的ftl文件导入到使用标签的ftl文件中，包括macro\\funtion\\variable等所有被引用的ftl内容。

    <#include "../../header.ftl">

### 3.5.2. **#import**

该标签的作用是将标签中指定的模板中的已定义的宏、函数等导入到当前模板中，并在当前文档中指定一个变量作为该模板命名空间，以便当前文档引用。与include的区别是该指令不会讲import指定的模板内容渲染到引用的模板的输出中。

    <#import "../../service.ftl as service>


### 3.5.3  **@includePage**

该标签的作用是将path中指定的请求地址html内容导入到当前模板。其原理是在指令内发起include请求。参考`javax.servlet.RequestDispatcher#include`.此标签特别适合做服务端页面模块重用。

     <@includePage path="/testFtl.html"/>

#### 3.5.3.1 使用场景

1. 定义了两个http服务地址

        @RequestMapping("/testFtl")
        public String testFtl(ModelMap modelMap) {
            modelMap.put("name", "na");
            modelMap.put("message", "hi");
            return "test";
        }
    
        @RequestMapping("/testInclude")
        public String testInclude(ModelMap modelMap) {
            modelMap.put("where", "out");
            return "testInclude";
        }

2. 定义两个模板页面

    `templates/test.ftl`：

         <h1>${message},${name}</h1>

    `templates/testInclude.ftl`

        <html>
          <head>
                <title>include 测试</title>
            </head>
            <body>
            ${where}
            <@includePage path="/testFtl.html"/>
            </body>
        </html>

3. 访问

    访问`testFtl.html`，输出：

        <h1>hi,na</h1>


    访问`testInclude.html`,输出：

        <html>
          <head>
                <title>include 测试</title>
            </head>
            <body>
            out
        <h1>hi,na</h1>    </body>
        </html>
        
### 3.5.4  **@shiroPrincipal**

该标签的作用是展示登录角色信息

      `<@shiroPrincipal/>` 展示`username[realName]`,比如`admin[水镜]`
      `<@shiroPrincipal property="username"/>` 展示username 比如 `admin` property可取com.acooly.module.security.domain.User的属性

### 3.5.4 前后端分离

1. 所有controller响应`ViewResult`对象
2. 启用`acooly.web.enableMVCGlobalExceptionHandler=true`，自动把异常转换为`ViewResult`
3. 建议前端请求访问使用后缀`.data`或者`.json`,`ACCEPT_HEADER`设置为包含`application/json`.（.data后缀的请求不会过shiro、xss、crsf，性能会好些）


### 3.5.5 收集业务日志

请求路径：http://ip:port/xdata/ingest.data

发送数据如下：

    {
     "logType": "1.1",
      "body": {
        "a": 123,
        "b": "dfdf"
      }
    }
    
日志输出文件：/var/log/webapps/${appName}/busi，输出内容如下:

    {"appName":"acooly-test","body":{"a":123,"b":"dfdf"},"env":"sdev","hostName":"192.168.49.25","logType":"1.1","timestamp":"2018-04-12 14:43:35"}
    
### 3.5.6 freemarker内置变量

    <#if Application.xxx?exists>
        ${Application.xxx}
    </#if>
    
    <h1>${message},${name}</h1>
    
    <h2>requestUri:${rc.requestUri}</h2>
    
    <h2>queryString:${rc.queryString}</h2>
    
    <h2>session attribute:${Session.valueInSession}</h2>
    
    <h2>request parameter:${RequestParameters.key}</h2>
    
    <h2>request atrribute:${valueInRequest}</h2>