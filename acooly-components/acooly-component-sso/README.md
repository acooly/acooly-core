## 1. 组件介绍

此组件提供多系统单点登录

### filter 逻辑实现
2. 若没有 jwt(JSON Web Tokens) 信息, 则重定向到 server 进行登录,后设置cookie jwt;有 jwt 信息则验证, 成功则执行下一个filter,信息不符则丢 RuntimeException


### 注意事项
1. 前置条件为  二级域名下 cookie 能够共享 如：.acooly.com
2. filter 只做认证, 用户信息等会写入 request 中
        //token 签发者
        String keyIss = (String) request.getAttribute(AuthConstants.KEY_ISS);
		//用户名
		String keySubName = (String) request.getAttribute(AuthConstants.KEY_SUB_NAME);
		//接收 token 的一方
		String keyAud = (String) request.getAttribute(AuthConstants.KEY_AUD);
		// token 签发时间
		Long keyIat = (Long) request.getAttribute(AuthConstants.KEY_IAT);
		//token 失效时间
		Long keyExp = (Long) request.getAttribute(AuthConstants.KEY_EXP);

3. 在web.xml文件中，过滤器的执行顺序是按照在web.xml中从上到下书写的顺序来执行的,最好比其它过滤器先执行；在servlet3.0注解中，filter执行顺序是按照文件名自然排序来决定执行顺序的，比如名字叫A的filter就比B先执行

## 使用
### 注 : 配置值需根据具体情况配置
1. pom.xml 中添加依赖

        <dependency>
            <groupId>com.acooly.sso.client</groupId>
            <artifactId>sso-client-core</artifactId>
            <version>1.0-SNAPSHOT</version>
        </dependency>

2. spring-boot 项目,在 application.properties 中配置 :

        # 不需要过滤处理的静态资源后缀配置,默认已经过滤了大多数静态资源,多种资源用逗号隔开,格式为antpath
        acooly.jwtfilter.resource.suffix=**.pdf
        # 配置 jwt filter 状态开启(可选)
        acooly.jwtfilter.open=false

    并在配置类中添加 : 
        
        @Bean
        public FilterRegistrationBean jwtFilterRegistrationBean() {
            FilterRegistrationBean registration = new FilterRegistrationBean();
            registration.setFilter(new AuthFilter());
            registration.addUrlPatterns("/*");
            registration.setName("ssoFilter");
            registration.setDispatcherTypes(DispatcherType.REQUEST);
            return registration;
        }        

    非 spring-boot 在 web.xml 中配置 :

        <filter>
            <filter-name>jwtFilter</filter-name>
            <filter-class>com.acooly.sso.client.core.filter.AuthFilter</filter-class>

            <!-- 不需要过滤处理的静态资源后缀配置,默认已经过滤了大多数静态资源,多种资源用逗号隔开,格式为antpath -->
            <init-param>
                <param-name>acooly.jwtfilter.resource.suffix</param-name>
                <param-value>**.pdf</param-value>
            </init-param>
             <!-- 如果关闭可配置 默认为 true -->
            <init-param>
                <param-name>acooly.jwtfilter.open</param-name>
                <param-value>false</param-value>
            </init-param>
        </filter>
        <filter-mapping>
            <filter-name>ssoFilter</filter-name>
            <url-pattern>/*</url-pattern>
            <dispatcher>REQUEST</dispatcher>
        </filter-mapping>
