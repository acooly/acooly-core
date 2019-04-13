<!-- title: Freemarker必备知识  -->
<!-- type: core -->
<!-- author: zhangpu -->
<!-- date: 2019-04-01 -->

Acooly Freemarker必备技能
=====

# 简介

本文主要提供Acooly框架的Web层采用freemarker作为控制层视图渲染时，必备和常用的知识点的统一归纳和演示，已方便查阅和参考。
Acooly-coder自动代码生成默认已修改为生成freemarker的视图界面，所以，请所有开发人员必须掌握本文档总结的知识点。
>注意：这里只是介绍在常规开发中，最常用的必须掌握的最少知识点和技能，Freemarker本身的能力很完善，在后续技能点的介绍中会表明对应更完善的文档，以应对你可能需要的能力

# 必备知识点

## 变量输出

指令：${varName} 是变量的默认输出形式。空值（null）的输出freemarker模式会报错，但框架已在启动时定制了默认转换处理。

1. 在freemarker中，当前渲染视图的变量一般来源于调用渲染的api传入（比如：控制层），一般是自动的，除非你自己做渲染控制（比如：acooly-coder），否则在springMVC开发场景，你可以不关注.你需要了解的是只要你控制层返回了数据（@RestController或@ResponseBody标注的方法），这个数据就是对应freemarker视图的渲染scope内的全局变量。

	比如：我们在控制层返回了JsonResult的对象，那么，这个对象会作为freemarker渲染的跟变量使用，你可以在freemarker界面内，任何地方直接访问或输出其子对象。

	```html
	<#-- jsonResult.success -->
	${success}
	<#-- jsonResult.code -->
	${code}
	<#-- jsonEntityResult.entity.id -->
	${entity.id}
	```
	>注意：你从控制层返回的JsonResult变量是渲染的根变量，在ftl界面上从其一级子成员开始引用。

2. 另外一种情况，可以直接在视图（ftl文件）界面声明变量并赋值。指令为：assign.
	例如：<#assign assignVarName = "acooly"> 表示给变量name赋值字符串"acooly", 输出则为：${assignVarName}


* 页面通过assign指令赋值的变量输出，`<#assign assignVarName = "acooly">` -> `${assignVarName}` -> acooly
* 对象属性直接输出: `${entity.username} -> fazheng
* 枚举值输出 `${entity.idcardType.message()}` --> 身份证
* Map<String,Object>转换输出 `${allCustomerTypes[entity.customerType]` --> 普通
* Map<Number,Object>转换输出 `${allStatuss?api.get(entity.status)` --> 正常
* 空对象属性的输出采用通用设置默认值的方法：()!，例如：user对象为空，可以：`${(user.username)!}`,放心，不报错。

>注意：对于非字符串作为Map的key的转换输出，采用?api.get()模式，框架已在初始化时开启了对应的配置。

## 格式化输出

格式化输出最常用的是数字和日期时间的格式化输出，所以是必须掌握的，其他格式化或转换可以参考freemarker文档。freemarker对变量及数据类型的格式化输出有非常强大的能力，详情请参考：<a href="http://freemarker.foofun.cn/ref_builtins.html" target="_blank">内建函数参考</a>


#### 数字格式化

通过assign设置数字变量 x=102042.0083，下面介绍最常规的格式化输出：

* 原始格式 - `${x}` = `${x?string}`: 102042.0083
* 内建number(最多3位小数) - `${x?string.number}`: 102,042.008
* currency货币两位小数四舍五入 - `${x?string.currency}`: ￥102,042.01
* percent百分数 - `${x?string.percent}`: 10,204,201%
* Java格式化: 两位小数 - `${x?string["0.##"]}`: 102042.01
* Java格式化: 科学计数两位小数 - `${x?string["0.##"]}`: 102,042.01

其他格式的转换请参考文档：<a href="http://freemarker.foofun.cn/ref_builtins_number.html#ref_builtin_string_for_number" target="_blank">数字格式化</a>

#### 日期时间格式化

这里以 entity.createTime(=now()) 和 entity.updateTime(=null) 两个变量为例子。
* SimpleDateFormat格式化：`${(createTime?string["yyyy-MM-dd HH:mm:ss.SSS"])!}` -->
* SimpleDateFormat格式化：`${(createTime?string["yyyy-MM-dd HH:mm:ss"])!}` --> 2019-04-11 18:31:17
* SimpleDateFormat格式化：`${(createTime?string["yyyy-MM-dd"])!}` --> 2019-04-11

其他格式的转换请参考文档：<a href="http://freemarker.foofun.cn/ref_builtins_date.html#ref_builtin_string_for_date" target="_blank">日期时间格式化</a>

## 迭代输出

freemarker前端渲染的迭代输出主要针对Collection集合和Map的迭代输出。一般掌握这两种基本就满足大部分需求，所以这也是必须的。

### 集合迭代

```html
<#list collection as e>
	${e}/${e.username}
</#if>
```
>1. collection是当前scope内的集合变量，可以是Array，List，Set等
>2. e表示collection的成员，可以是简单变量`${e}`，也可以是对象变量（`${e.username}`）

实例：

```html
    <div>
    <#list entities as e>
        <div class="demolist">
            <div>${e.id}</div>
            <div>${e.username}</div>
            <div>${e.realName}</div>
            <div>${e.age}</div>
            <div>${e.idcardType.message()}</div>
            <div>${allCustomerTypes[e.customerType]}</div>
            <div>${allStatuss?api.get(e.status)}</div>
            <div>${(e.updateTime?string["yyyy-MM-dd"])!}</div>
            <div>${e.createTime?string["yyyy-MM-dd"]}</div>
        </div>
    </#list>
    </div>
```

### Map迭代

map的迭代采用新语法后变动统一和简单。

```html
<#list map as k,v>
	${k}/${v}
</#if>
```
实例：

```html
<#list allStatuss as k,v>
   <option value="${k}">${v}</option>
</#list>
```

## 空值处理

### 默认值方式: ()!
默认值方式处理空值是最常见的方法，框架默认支持字符串，数字等类型的处理（classic_compatible=true），但如果对空值进行函数处理，比如：日期时间格式化则不能自动处理。

* 空变量（一个不存在的变量）`${anyVari}` 
* 对象的属性为空 `${entity.fee}` 
* [重点] 被格式化输出的日期为空 被格式化输出的日期为空 `${(entity.updateTime?string["yyyy-MM-dd"])!}` 

### 判断是否为空：(var)??
采用判断语句判断变量是否为空，然后在输出。

* 判断变量 <#if anyVari??>${anyVari}</#if> ：
* 判断对象及子属性变量 `<#if (entity.fee)??>这里表示同时判断entity和entity.fee不为空</#if>` 

## 条件判断
这里重点介绍的是条件判断表达式，而不是if语句的语法。

* [重点] 比较运算：大于，等于，大于等于，小于，小于等于，不等于的判断， 大于和小于相关的比较因为<和>与freemarker的语法关键字冲突，建议采用转移符号，否则请使用括号来包含表达式也是可以的。比较运算必须是精确类型间的比较，否则会报错 `<#assign x = 100>`:

	```html
	<#if x gt 10 >100大于10</#if>: 100大于10
	<#if (x > 10) >100大于10</#if>: 100大于10
	<#if (x >= 10) >100大于10</#if>: 100大于等于10
	<#if x gte 10 >100大于10</#if>: 100大于等于10
	<#if x != 10 >100大于10</#if>: 100不等于10
	<#if x == 100 >100大于10</#if>: 100等于100
	```


* 逻辑判断：
	* 逻辑 或：||
	* 逻辑 与：&&
	* [重点] 逻辑 非：! 注意:只能对boolean类型使用。 <#assign b = false> <#if !b >b=false</#if>: b=false
* 判断null和空字符串
* 判断不为null: ()??表示被判断的对变量不为空返回true。 `<#if (entity.fee)??>entity.fee 和entity.fee不为null</#if>`
* 判断为null可以使用!(x??)。 `<#if !(entity.fee)??>entity或entity.fee为null</#if>`
* 判断为是否为空字符串，直接使用 == 或 != ""。 `<#if entity.subject == "">entity.subject为空字符串</#if>`
* 判断字符串不为null也不为空字符串:`<#if (entity.username)?? && entity.username != "" >用户名username不为空也不为空字符串</#if>` 

## Servlet指令

* Request：请求属性(request.setAttribute, model.addAttribute)可直接输出或使用Request对象。 例如: `request.setAttribute("requestAttr")` --> `${requestAttr}` 或 `${Request.requestAttr}`
* RequestParameters: 请在本页面url后增加：?requestAttr=121212 `${RequestParameters.requestAttr}`
* Session: `${Session.sessionAttr}` --> 这是会话Session变量值
* Applcation: `${Application.applcationAttr}` --> Application变量值
* RequestContext: `requestUri:${rc.requestUri}`，`queryString:${rc.queryString}`

## @includePage

@includePage是acooly框架扩展的freemarker指令，实现服务器端include（RequestDispatcher.include），区别freemarker的#include是静态ftl的引入。
* 服务器端include: <@includePage path="/xxx/xxx/xx.html"> 
* 静态include: <#include "/xxx/xxx/xx.ftl">

## java方法调用

这里提供的是java对象的方法调用方案，一般不推荐使用，但在特殊场景，如果有需求，可以实现砸ftl文件中直接调用通过Model传入到freemarker的对象的方法。

格式：

```html
${object?api.method(...)}
```
例如：

* List entities.size: `${entities?api.size()}` --> 10
* entity.hashCode(): `${entity?api.hashCode()}` --> 0
* entity.toString(): `${entity?api.toString()}` --> Customer{age=41,birthday=1978-04-13 14:02:22,comments=null,createTime=2019-04-13 14:02:22,customerType=normal,fee=null,gender=0,id=1,idcardNo=1234123412341234,idcardType=cert,mail=xiyang@acooly.cn,mobileNo=13989873641,realName=夕阳,salary=402100,status=10,subject=null,updateTime=null,username=xiyang}


