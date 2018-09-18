关于后端开发人员的Web前端开发
=====


# 需要解决那些问题？

作为后端开发人员，对封装，结构化和程序逻辑的把控能力较强，但在纷杂前端基础技术熟悉和应用上经验较少。那些问题，怎么解决？
>本文和本项目案例的目的重点为：从后端服务开发的角度来考虑和解决前端开发中经常会遇到的重点问题和解决方案。

问题列表：

* 前端工程结构规划？
* 前端界面结构规划？
* 前端交互方案？
* 前端安全控制？
* 错误处理方案？
* 通用分页和表单
* MOCK与并行开发？
* 前端界面的简单调整？

> 程序结构清晰重点考虑，美观和好看的问题暂不考虑！！！！


## 工程结构

仍然可以先使用acooly-archetype创建初始工程。然后进行前端工程调整。

我们根据前端工程的需求范围可分为两种：只有PC网站和网站+H5，下面重点介绍web+H5的规划。

原则：

* Web和H5混合的网站尽可能复用控制层，这就要求判断复用的功能采用AJAX方式拉取数据，便于复用。
* 抽取公共的资源（功能图片，如银行图标等）和控制层逻辑到common模块，如对服务层的封装（如果有的话）
* 采用子目录的方式隔离web和H5的路径，便于功能，资源和权限规划。
* 尽量采用相同的认证方式（比如都是使用会话，则保证主会员变量一致，但可以添加特别的变量）
* 与时俱进，采用freemarker代替jsp/jstl
* 每个工程需要一个统一的抽象controller，来统一处理一些公用的能力。

推荐的工程结构后续会尽快整理acooly-archetype-portal直接支持。目前请参考：dasset-portal案例。



## 页面结构

对页面结构采用分块设计，这样做的好处：

* 模块(板块)化规划结构，可复用性和可维护性最佳。
* 便于按块缓存数据（直接前端内存缓存，而非后端数据缓存）

```html
<!DOCTYPE html>
<html lang="zh">
<head>
	 <!-- meta负责对整站提供mate配置信息 -->
    <@includePage path="/mobile/common/meta.html"/>
    <title>XXXX</title>
    <!-- meta负责导入css和js库，以及初始化脚本处理（JS也可以放到footer后） -->
    <@includePage path="/mobile/common/include.html"/>
</head>
<body>
<!-- header：整站或模块的公共头（当然里面也可以有逻辑判断和处理，freemarker嘛） -->
<@includePage path="/mobile/common/header.html"/>

<div class="main">
    这里才是你开发的功能视图
</div>

<!-- footer：整站或模块公共的页脚 -->
<@includePage path="/mobile/common/footer.html"/>
<!-- footer end -->

</body>
```

>PS: 页面结构里面的公共视图，请采用专用的controller进行路由，禁止直接使用静态html导入，保障所有请求都通过controller。请参考本项目的CommonRouterController


## 前端交互方案

什么是前端交互方案？ 是指前端请求的交互流转流程，也可以说是前端的数据流。通常的方案有两种：

### 服务端渲染模式
URL直接请求控制层的方法，控制层处理后，设置数据到request中（model.addAtrribute(....)），然后forward或redirect到具体的渲染视图，通过freemarker等模板语言展示数据。

浏览器 -(URL)-->

[控制器某方法（范围值类型String，非@ResponseBody）-- (forward/redirect) --> 视图文件（data+freemarker渲染生成最终的Html内容）]

--> 浏览器显示

使用场景：

* 单次数据显示，无持续交互，只在服务器端访问一次即可。比如：文字信息的显示，详见界面的展示等。


### 客户端渲染模式（AJAX拉数据）
URL访问的是控制层流转的视图界面（类型String，非@ResponseBody），无数据，可能存在初始化数据，比如下拉列表的可选值等，然后浏览器渲染显示静态视图后，通过ajax拉取另外一个控制器方法（类型对象数据JSON结构，@ResponseBody）获取结构化数据，通过js（jsTemplate: baiduTempalte/artTemplate等）选择显示到静态界面中。

使用场景：

* 一次初始化视图界面，后续多次拉取数据渲染，比如：分页查询。有效减少数据传输量和提高体验效果
* 数据复用性好。比如：你在移动端编写了一个查询账单的分页数据，其实可以直接用于Web端。（认证会员统一的情况）



## 前端安全控制

一般来说，前端的安全控制方案相对后端简单，大多数情况无需考虑基于角色的访问控制能力。一般通过session变量控制受限访问目录的访问即可，如果有角色和权限的问题，一般也可以通过session变量的标志进行分类控制（一般不会很复杂）。

acooly框架已封装了统一的采用filter方式认证和控制权限的抽象filter，具体项目只需要集成并实现具体的认证方法，然后进行filter基础配置即可。

com.acooly.integration.web.AcoolyPortalAuthenticateFilter

核心接口（需要子类实现）：

```java
    /**
     * 认证处理
     * 认证失败抛出异常则中断访问
     *
     * @param request
     * @return 需要放入session中的数据，也同时用于检测是否需要登录的标志
     */
    protected abstract Object doAuthentication(HttpServletRequest request);
```


提供的主要特性：

* authEnable: 认证开启开关，如果关闭，则不进行认证，用于开发调试MOCK等场景
* authSessionKey: 检查是否登录的session变量名称，具体放置什么变量内容，由子类的doAuthenticate方法返回。
* authIncludes：配置需要访问控制的路径，可多个，并指出AntPath模式，逗号分隔。
* authExcludes：配置受限访问控制路径下，多个忽略认证的路径,AntPath默认，逗号分隔
* authFailure：认证失败的跳转地址，如果不配置，则直接返回http状态码401和具体错误消息

PS：案例请参考dasset-portal的com.qiudot.dassets.portal.security.DassetPortalAccessFilter

```java
@Slf4j
public class DassetPortalAccessFilter extends AcoolyPortalAuthenticateFilter {

    @Override
    public Object doAuthentication(HttpServletRequest request) {
		// 这里你可以通过spring注入任何bean，然后这里可以去登录，返回你的会员实体或任何结构。
		// 然后return，你就可以通过：你配置的authSessionKey获取登录用户数据。
    }

}
```

spring-boot的acooly框架中怎么配置？你需要在你项目的XXXXAutoConfig文件中采用代码方式构建bean和注册filter。下面是dasset-portal的案例

```java
    /**
     * 实力化filterBean
     * @return
     */
    @Bean
    public DassetPortalAccessFilter dassetPortalAccessFilter() {
        DassetPortalAccessFilter dassetPortalAccessFilter = new DassetPortalAccessFilter();
        dassetPortalAccessFilter.setAuthFailureUrl(dassetPortalProperties.getAuthFailureUrl());
        dassetPortalAccessFilter.setAuthSessionKey(dassetPortalProperties.getAuthSessionKey());
        dassetPortalAccessFilter.setAuthExcludes(dassetPortalProperties.getAuthExcludes());
        dassetPortalAccessFilter.setAuthIncludes(dassetPortalProperties.getAuthIncludes());
        dassetPortalAccessFilter.setEnable(dassetPortalProperties.isAuthEnable());
        return dassetPortalAccessFilter;
    }

    /**
     * 注册portal的访问控制filter
     *
     * @param dassetPortalAccessFilter
     * @return
     */
    @Bean
    public FilterRegistrationBean dassetPortalAccessFilterRegistrationBean(
            @Qualifier("dassetPortalAccessFilter") Filter dassetPortalAccessFilter) {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(dassetPortalAccessFilter);
        registration.setOrder(Ordered.LOWEST_PRECEDENCE - 5);
        registration.addUrlPatterns(Lists.newArrayList("*.html").toArray(new String[0]));
        registration.setDispatcherTypes(EnumSet.of(DispatcherType.REQUEST, DispatcherType.FORWARD));
        registration.setName("dassetPortalAccessFilter");
        return registration;
    }
```

>PS: 多认证入口后续会更新补充到filter中...


## 错误处理方案

前端界面的错误消息及显示的解决访问一般来说还是有两种，对应到前面“前端交互方案”的两种模式。


### 服务端渲染模式

在该模式下，请求到了控制层方法后，查询服务层，结果报错了，或则参数检查都错了，则需要返回错误，并友好的提示给用户。

* 方案一：统一错误处理，跳转到独立错误界面显示错误信息。

```java
	 protected String commonResultView = "redirect:/mobile/common/result.html";
	 
    protected String handleException(HttpServletRequest request, Exception e) {
    	 // 合理调用acooly框架原有的错误处理，并临时存储错误消息到session中。
    	 // 待显示完成后，通过MessageFilter删除临时session变量。
        handleException(null, e, request);
        
        // 这里通过统一的控制层父类定义公共错误视图地址（注意：为防止重复提交，需要使用redirect）
        return this.commonResultView;
    }
    
    // 子类里面怎么使用？
    
        @RequestMapping(path = "xxxx", method = RequestMethod.GET)
    public String xxxx(HttpServletRequest request, Model model) {
        try {
            // do check
            // do call service
        } catch (Exception e) {
            // 失败，则错误处理跳转到错误页面
            return handleException(request, e);
        }
        // 成功则forward到对应的视图
        return "/mobile/service/xxxx/xxxx";
    }
    
```

* 方案二：统一客户端JS做错误处理，任然返回对应的业务视图。

这种方案里面，子类的处理方案一致，只是不return错误处理的地址（不跳转统一错误界面），仍然返回到对应的业务视图，然后通过freemarker输出session里面的错误消息，通过js判断消息是否存在并显示。

可以在include.html里面加入初始化的js代码，判断并弹出提示。

```js
    // 全局错误处理
    $(function() {
    	  // 通过freemarker输出session中的错误消息
        var errorMessage = "${Session.messages}"
        // 判断不为空，则说明有错误消息，通过toptip显示错误消息
        if($.acooly.portal.trimToEmpty(errorMessage) != ""){
            $.acooly.toptip(errorMessage,"error");
        }
    });
```

>PS: 风险：如果控制不好，虽然这里提示了错误信息，但是页面会继续渲染，如果没有对空值有明确处理，后续的freemaker渲染页面内容会报错（NullPoint等）。

### 客户端渲染模式（AJAX拉数据）

这种方式是推荐方式，处理比较干净明了。数据是通过ajax拉取，acooly.portal.js已封装了各种常用的ajax访问或提交数据的方法，也内置了错误处理，大多情况，可以不用开发人员处理。如有特殊需求，请采用标准ajax方式覆盖原有的封装参数（ajaxSubmit方法可接受所有$.ajax的标准参数作为入参覆盖封装的方法）实现。


## 通用分页和表单

本节内容，多数采用acooly.portal.js前端组件库封装实现。

https://gitlab.acooly.cn/acoolys/acooly-portal


### 通用分页

具体来说会通过基于服务端控制和ajax控制的两大类分页方案。

#### 服务器端分页

采用tag方式，目前acooly框架已支持，acooly-taglibs组件。暂不做描述，在1119e等项目中案例较多。

#### 客户端分页
一般采用ajax拉取分页数据，然后通过js计算和控制分页效果。

* 移动端
	* 每页数据下发有加载下一页的控制按钮
	
	$.acooly.portal.pageAppend({...});
	
	```js
	/**
     * 分页列表（内容递增方式）
     *
     * 列表末尾提供加载更多的按钮，加载后的下一页数据接续在上一页数据后。
     * @param opts参数说明：
     * url 请求数据的URL
     * jsonData 请求参数
     * template 模板ID
     * renderContainer 分页列表数据容器
     * renderController 分页控制容器
     * beforeRender 数据load后，渲染完成前拦截函数
     * afterRender  渲染完成后拦截函数
     */
    pageAppend: function (opts) {

        var def = {
            refresh: false,      // 如果true则刷新，显示第一页。
            entity: null,
            pageSize: 10,
            jsonData: {},
            beforeRender: null,
            afterRender: null
        }
        .....
	```

	调用案例：
	
	```js
	function pageQuery() {
        $.acooly.portal.pageAppend({
            url: '/mobile/asset/assetDynamic.html',
            entity: 'assetDynamic',
            jsonData: {"assetNo": "${assetNo}"}
        })
    }

    $(function () {
        pageQuery();
    });
	```

	
	* 通过滚动上滑到数据页底自动加载下一页数据
	 
	  待封装...


* Web端
在1119e等项目中案例较多,提供上一页，下一页，中间动态页码选择等功能。后续会统一分装到acooly.portal中使用。待补充...

### 通用表单

表单相关功能一直是前端开发的核心，这里重点不关注表单的视图效果，而是关注表单相关的功能，主要包括：表单的验证，错误处理和提交。

#### 表单的客户端验证

一般由和很多框架可选用，这里通用选择HTML5Validate框架，原因是他最大程度上支持和兼容HTML5源生的表单验证属性。
参考文档：https://www.zhangxinxu.com/wordpress/2012/12/jquery-html5validate-html5-form-validate-plugin/

包括两种验证方式：

* 拦截submit事件处理模式

```js
    $(function () {
        // 绑定直接提交
        $("#depositForm").html5Validate(function (form) {
            $.acooly.portal.ajaxSubmit({
                url: '/mobile/service/fund/deposit.html',
                formId: 'depositForm',
                buttonId: 'depositButton',
                onSuccess: function (result) {
                    console.info(result.data.redirectUrl);
                    location.href=result.data.redirectUrl;
                }
            });
        });
    });
```

* js判断方式

```js
if ($.html5Validate.isAllpass($("#bindMobileNoModifyForm"))) {
            var jsonData = {...};
            $.acooly.portal.ajaxSubmit({
                url: '....html',
                jsonData: jsonData,
                beforeSend: function () {
                    $("#bindMobileNoModifyButton").prop('disabled', true);
                    $("#bindMobileNoModifyButton").html("正在验证...");
                },
                complete: function () {
                    $("#bindMobileNoModifyButton").prop('disabled', false);
                    $("#bindMobileNoModifyButton").html("确定");
                },
                onSuccess: function (result) {
                    location.href = ".....html"
                },
                onFailure: function (result) {
                    $.acooly.toast(result.message);
                }
            })
        }
```

>ps: 客户端的错误消息采用组件提供的直接在表单上浮错误提示消息。（考虑在H5端不暂用空间）；服务器端返回的错误统一在ajax封装中处理了，采用toptip方式展示，通讯异常采用toast方式展示。



## MOCK与并行开发

acooly团队必须推行的是各层（如果有分层）通过接口定义，实现独立同步开发。所以前端的开发，调用服务端部分，请前端开发人员先不要纠结服务器端服务是否online，直接MOCK开发完成后，在统一调试。

为显示该目的，请秋波已提供了服务器专用的annotation标注mock实现，如下：

@MockService

标注在需要被mock的接口实现类上，并设置acooly.mock.enable=true，比如XService有两个实现类，一个XServiceImpl，一个XServiceMock，在XServiceMock上标注此注解，当mock启用时，使用XServiceMock提供服务，当mock关闭时，使用XServiceImpl提供服务

自动化MOCK服务的方案也在进行中...


## 前端界面的简单调整

作为后端开发人员，适当掌握一些前端基础技术，有利于提高个人和团队生产力水平。也对个人全栈发展和适应性有较大的帮助。

你需要：
1、熟练掌握HTML代码和语法
2、熟悉CSS样式，读懂常规的CSS样式，对常用的颜色，字体，位置，对其，间隔等样式表熟悉（acooly.portal.css做了常规class封装）
3、你是java开发人员，“鄙视”你读不动常规javascript和编写常规js代码（比如判断和验证..）,请学会jquery的选择器（百度一大把，不会就查）

如果你理解以上三点，并持续学习和练习，那么常规的填充界面数据，拷贝雷同效果等工作，你其实不需要专门的前端工程师为你服务。


















