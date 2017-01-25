视图层开发指南
===

# 管理视图开发

管理视图的开发在acooly框架下，我们选择的是easyui作为基本的前端解决方案，采用JSP作为视图渲染。

>本章节，先总体描述后台管理的easyui开发的一些约定，注意点，然后在列举最几种常用的视图开发场景的案例。

## 基础知识准备

如果需要进行acooly框架的后台管理功能开发，需要进行一些基础技术栈的准备。

* html：熟悉HTML语法
* javascript：要求至少能阅读常规代码，编写基本的表单验证功能。
* jquery：熟悉常规的jquery操作，包括：选择器，ajax的语法和异常处理。
* easyui：至少熟悉：布局管理（easyui-layout），表格（easyui-datagrid），弹出框（dialog），按钮（easyui-linkbutton）等
* jsp/jstl/freemarker: 至少熟悉：servlet内置变量的操作，自定义变量输出，Map和List的跌倒，if控制语句等。

## 开发规范和约定


### 元素ID全局唯一

所有后台的界面采用EASYUI搭建，每个模块功能页面默认采用AJAX方式加载，JS运行SCOPE是唯一的，要求整站的所有页面的所有元素的ID唯一，否则可能出现不可预料的问题。

命名规则为：manage_模块名称(一般为受托管的实体名称)\_UI组件名称\_其他
	
如：manage\_customer\_datagrid , manage\_customer\_form,manage\_customer\_searchform, manage\_customer\_button\_search

### 基本操作模式

所有后台功能模块的基本操作模式都是采用：主界面 + 弹出框操作模式，主界面一般为列表(datagrid),也可以是多个datagrid与其他UI组件的整合，如果左右，上下结构，与zTree,Tab等组件的组合等。
	
功能包括：独立的多条件查询区域，独立的横向工具条，表格内每行的功能按钮等。

	
## 封装功能介绍

acooly框架的后台基于easyui，在开发规范和约定的基础上，做了较多的基础功能封装。

### 常规操作

常规操作的封装主要包括: 操作的CRUD，查询，上传，下载等通用功能，同时提供非界面的确认操作模式封装。操作的封装主要在:/manage/script/acooly.framework.js文件中，你可以在运行时，通过查看页面源代码方式找到源代码，也可以acooly-components-security-${version}.jar中找到源代码文件。

```xml
<dependency>
  <groupId>com.acooly</groupId>
  <artifactId>acooly-components-security</artifactId>
  <version>${acooly.components.security}</version>
</dependency>
```

>注意：常规操作完整Demo是以自动代码生成的单模块功能，一个完整模块的功能几乎使用了大部分的常规操作封装。常规操作中的一些API因历史版本原因，虽然不是完善和优美的封装，但是暂时都没有改进或删除冗余，但都可以作为案例参考，使用者可以编写自己的封装函数使用。

#### 添加和修改

提供统一的弹出dialog调用指定URL（create或edit）获取添加和编辑的视图，并在弹出框中显示，同时提供统一的按钮（提交）和(取消)，封装提交的表单请求，完成添加和编辑的保持操作。

**$.acooly.framework.create(opts)
$.acooly.framework.edit(opts)**

opts参数说明：
	
|参数			|类型			|必填  |说明
|------------|-------------|--------|----
|url			|string		 |是	   |添加或编辑视图地址(必填)
|form			|string	    |否	   |添加和编辑的form表单ID（可选，如果传入entity参数，则可以不填写）
|datagrid		|string		|否		   |列表视图datagrid组件ID（可选，如果传入entity参数，则可以不填写）
|entity      |string		|否		  |添加和编辑的实体类名称首字母小写(可选，代替：form和datagrid)
|title			|string		|否		  |标题，默认为：添加或修改
|top			|int			|否		  |位置top，默认居中的top
|width			|int			|否	     |宽度，默认：500px
|height		|int			|否	     |高度，默认：auto，根据类型显示
|submitButton	|string		|否		  |提交按钮的label，默认：增加或修改
|reload		|boolean		|否		  |提交成功后，是否reload列表datagrid,如果为true，则提交成功后重新load列表数据，如果为false(默认)，动态插入新数据（添加）或更新（编辑）datagrid对应的行。
|maximizable	|boolean		|否		  |弹出框是否可以最大化，true或false(默认)
|onSubmit		|function		|否		  |表单提交前，在原有easyui表单验证前的拦截函数，可以在本函数中进行附加的提交前验证和对表单元素赋值操作。如果返回false，则终止提交。无传入参数，可以通过jquery选择器获取当前scope的元素值。
|onSuccess	|function		|否		  |提交成功后的回调函数（result.success = true），传入参数为ajax表单提交的返回值（一般为JsonEntityResult的Json数据）。
|failCallback	|function		|否		  |提交失败后回调函数（result.success = false），传入参数为ajax表单提交的返回值（一般为JsonEntityResult的Json数据）。
|onCloseWindow|function		|否		 |关闭弹出框回调函数
|ajaxData     |json        |否     |可以传递添加和编辑视图额外的请求参数。如{"search\_EQ\_id":1}

>框架最初为提供添加和删除的相关回调函数，采用命名约定的隐含回调函数方式处理。处理方式都是在添加或编辑的视图界面中，声明指定名称的函数实现回调。包括：
>${formId}\_beforeSubmit：表示表单序列化完成后，提交前的拦截函数。
>${formId}\_onSubmit：同onSubmit


示例：

```javascript
// 指定视图URL，实体名，高度，宽度等
$.acooly.framework.create({url:'/manage/showcase/customer/create.html',entity:'customer',width:500,height:400})
```

#### 通用表单查询
提供统一的的通用表单查询封装，特性如下：
1. 自动序列化指定元素ID（可以是formId,也可以是任意的DIV的ID）下的所有表单为json格式，作为查询条件。
2. 查询完成后，获取的新数据，自动reload制定的表格数据（datagrid）
3. 自动注册查询表单的回传按键为提交查询


**$.acooly.framework.search(containerId,datagridId);**
		
|参数			|类型			|必填  |说明
|------------|-------------|--------|----
|containerId	|string		|是	   	   |formId,也可以是任意的DIV的ID
|datagridId	|string	    |是	   |显示查询结果的datagrid
	

```js
$.acooly.framework.search('manage_customer_searchform','manage_customer_datagrid');
```

```html
<a href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:false" onclick="$.acooly.framework.search('manage_customer_searchform','manage_customer_datagrid');"><i class="fa fa-search fa-lg fa-fw fa-col"></i>查询</a>
```

表单回传自动提交查询：
$.acooly.framework.registerKeydown(containerId,datagridId)

该方法需要在查询界面初始化时调用。如下：

```js
$(function() {
  $.acooly.framework.registerKeydown('manage_customer_searchform','manage_customer_datagrid');
});
```


#### 查看
提供通用弹出框查询视图界面封装。

**$.acooly.framework.show(url, width, height)
$.acooly.framework.get(opts)**

opts参数说明：

|参数			|类型			|必填  |说明
|------------|-------------|--------|----
|url			|string		 |是	   |视图的URL
|width	     	|int		    |否	   |宽度，默认:400px
|height		|int			 |否		|高度，默认：300px
|title			|string		 |否		|弹出框标题，默认为：查看

get与show是不同时期的封装，为兼容老版本，所以同时支持，不同在于，get可以指定弹出框的标题，推荐使用get

#### 删除
提供通用的删除功能封装，支持单个和批量，同时删除前二次确认。

**删除单条**
对制定id的数据提供删除功能，url,id和datagrid为必选。

**$.acooly.framework.remove(url, id, datagrid, confirmTitle, confirmMessage)**

```js
$.acooly.framework.remove('/manage/showcase/customer/deleteJson.html','1','manage_customer_datagrid');
```
**批量删除**
批量删除提供对datagrid选择的行批量删除的功能，url和datagrid为必选。

**$.acooly.framework.removes(url, datagrid, confirmTitle, confirmMessage)**

```js
$.acooly.framework.removes('/manage/showcase/customer/deleteJson.html','manage_customer_datagrid')
```

#### 导出文件
提供以当前查询表单所选作为条件的动态查询后导出EXCEL和CSV的功能。可以指定导出文件名。

**$.acooly.framework.exports(url, searchForm, fileName);**

	
|参数			|类型			|必填  |说明
|------------|-------------|--------|----
|url			|string		 |是	   |文件下载的URL，EXCEL和CSV有不同的URL
|searchForm	|string	    |是	   |查询的表单的父元素ID
|fileName		|string		 |否		|下载的文件名，默认为服务器端指定。

实例：

```js
// 导出EXCEL
$.acooly.framework.exports('/manage/showcase/customer/exportXls.html','manage_customer_searchform','客户信息表')"
// 导出CSV
$.acooly.framework.exports('/manage/showcase/customer/exportCsv.html','manage_customer_searchform','客户信息表')
```

#### 文件上传

提供通用的文件上传功能，也可以作为文件导入的界面使用。

**$.acooly.framework.createUploadify(opts);**

参数说明：

|参数			|类型			|必填  |说明
|------------|-------------|--------|----
|url			|string		 |是	   |文件上传的URL
|formData		|json		    |否	   |POST提交的表单数据
|jsessionid	|string		 |是		|因为采用的是第三方组件，无法直接使用cookie,需要通过URL传递sessionid，jsp可传入：<%=request.getSession().getId()%>。
|messager		|string		|是			|上传的过程消息显示label元素ID
|uploader		|string		|是			|上传组件的唯一ID

实例：

请参考自动代码生成的数据导入视图。{entity}Import.jsp

```js
	$.acooly.framework.createUploadify({
		/** 上传导入的URL */
		url:'/manage/showcase/customer/importJson.html?_csrf=${requestScope["org.springframework.security.web.csrf.CsrfToken"].token}&splitKey=v',
		/** 导入操作消息容器 */
		messager:'manage_customer_import_uploader_message',
		/** 上传导入文件表单ID */
		uploader:'manage_customer_import_uploader_file',
		jsessionid:'<%=request.getSession().getId()%>'
	});	
```

#### 通用询问请求操作

前端管理功能开发中，存在很多无界面的询问二次确认的请求操作，场景为：点击按钮(或其他)触发，询问二次确认，确定后，请求服务器端一个指定URL，待服务器端完成操作后，提供操作结果。一般应用于在datagrid中选择了行后的对应操作，如：禁用，启用，移动等。

**$.acooly.framework.confirmRequest(url, requestData, datagrid, confirmTitle, confirmMessage, onSuccess, onFailure)**

参数说明：

|参数			|类型			|必填  |说明
|------------|-------------|--------|----
|url			|string		 |是	   |操作的URL
|requestData	|json		    |否	   |POST请求的数据
|datagrid		|string		 |是		|完成请求后，需要刷新的列表
|confirmTitle	|string		|否			|确认框标题，默认：确定
|confirmMessage	|string	|否			|确认框提示文字，默认：您是否要提交该操作？
|onSuccess	|function		|否			|提交成功后的回调函数（result.success = true），传入参数为ajax表单提交的返回值（一般为JsonResult的Json数据）。
|onFailure	|function		|否			|提交成功后的回调函数（result.success = false），传入参数为ajax表单提交的返回值（一般为JsonResult的Json数据）


#### 获取选中的Grid行

提供了通用的选择Datagrid行的帮助方法，返回为当前选择行的对应数据. 同时支持datagrid和treegrid

$.acooly.framework.getSelectedRow(datagrid);


### DataGrid

EasyUI的DataGrid组件是BOSS后台最常用的组件，没有之一，就是最常用的。数据查询，数据管理首页等。本节主要针对acooly框架对DataGrid的使用和扩展进行说明，DataGrid本身的大量功能和特性请参考EASYUI官方文档和Demo。

>EasyUI的DataGrid：<a href="http://www.jeasyui.com/documentation/index.php" target="_blank">文档</a>和<a href="http://www.jeasyui.com/demo/main/index.php?plugin=DataGrid&theme=default&dir=ltr&pitem=" target="_blank">Demo</a>


建议代码如下：

```html
<table id="manage_customer_datagrid" class="easyui-datagrid" 
    url="/manage/showcase/customer/listJson.html" 
    toolbar="#manage_customer_toolbar" fit="true" border="false" fitColumns="false"
    pagination="true" idField="id" pageSize="20" pageList="[ 10, 20, 30, 40, 50 ]"
    sortName="id" sortOrder="desc" 
    checkOnSelect="true" selectOnCheck="true" singleSelect="true" nowrap="false">
  <thead>
    <tr>
       <th field="showCheckboxWithId" checkbox="true" 
       data-options="formatter:function(value, row, index){ return row.id }">选择</th>
       <th field="id" sum="true">ID</th>
       <th field="username">用户名</th>     
       <!-- 其他列 -->
    </tr> 
  </thead>  
</table>    
```

#### 常用参数

acooly框架中，Datagrid的使用方式采用html代码方式，通过标准table标签，添加class="easyui-datagrid"方式标记为datagrid组件，然后通过属性定义来控制行为。


常用属性说明：

|属性            |类型    |必选  |说明
|---------------|--------|-----|--------
|id             |string  |是 |遵循acooly框架统一的命名约定。
|url            |string  |否 |数据拉取的URL，对应的springMVC方法一定是返回: JsonListResult结构（这里是约定，当然你也可以直接返回一个自定义的JSON结构，只要包含 rows和total两个Datagrid需要的属性即可，但这里不推荐）；特别需要注意，该参数如果填写，则表示视图渲染完成后，会自动访问URL拉去数据，如果遇到需要动态拉取数据的情况，该参数一般先不填写，并设置datagrid为hide，手动加载拉取并渲染后再show.
|toolbar        |string |否  |指定datagrid的toolbar菜单的元素ID，一般为一组linkbutton的组合，如果没有，则可以不填写该属性。
|fit            |boolean|否  |表示默认表格最大化填充父元素。
|border         |boolean|否  |是否显示表格边框
|fitColumns     |boolean|否  |true/false，表示所有列数据的是否按当前屏幕大小显示，特别重要的属性，如果设置为true，则表示如果列过度操作屏幕横向宽度的情况，不会显示横向滚动条。所以，这里统一建议全部设置为：false
|pagination     |boolean|否  |是否启用分页功能
|pageSize       |int    |否  |默认分页大小，推荐：20，与服务器端默认值一致
|pageList       |array  |否  |可选分页大小列表。例如：[ 10, 20, 30, 40, 50 ]
|idField        |string |是  |每行的标志唯一的属性
|sortName       |string |否  |默认排序的属性名称
|sortOrder      |string |否  |默认排序方式：desc:降序，ase:升序
|checkOnSelect  |boolean|否  |表示勾选每行的checkbox后，自动选择本行。
|selectOnCheck  |boolean|否  |与checkOnSelect的行为类似，并相反。
|singleSelect   |boolean|否  |是否只允许选择单行。如果无需批量操作（批量删除等）的场景，设置为false.
|nowrap         |boolean|否  |操作单元格隐藏和显示时，文本是否自动换行。建议这是为false：表示会自动换行。

#### 数据结构

datagrid官方要求的加载数据的结构为：

```js
{
  total:200,
  rows:[
    {"id":1, "username":"zhangpu",...},
    {"id":2, "username":"alise",...},
    //...
  ]
}
```

acooly框架的数据结构为JsonListResult,增加了success,code,message,和扩展数据data.如下：

```js
{
    success: true,
    code: "",
    message: "",
    data: {
        allIdcardTypes: { cert: "身份证", pass: "护照",other: "其他"}
        //...
    }
    total: 4
    rows: [
        {...}, {...}, {...}, {...}
    ]
}
```

以上数据扩展后，直接加载给datagrid对象，在datagrid组件内的所有列定义的formatter方法内可以直接使用额外传入的Map数据data来作为格式转换。

#### 列定义及格式化

easyui官方文档对列的定义和属性有全面的说明，请随时跟进需求参考并使用，这里说明的是acooly里面最常用的列定义方式及相关属性和格式化方案。

**通用列定义**

```html
<th field="username">用户名</th>
```
以上定义表示：以每个返回实体JSON结构的username这个属性的原始值，名称为：“用户名”。

**自定义格式化**

datagrid的列格式化，是通过formatter参数提供一个回调函数处理，以函数的返回值作为列的最终输出。

```js
formatter : function(value,row,index){
    // value : 当前单元格原始值
    // row : 当前行的json数据，如：{"id":1, "username":"zhangpu",...}
    // index: 当前行在整个datagrid数据集中的位置索引，第1条为0.
} 
```

基于以上能力，我们队列的格式化主要有一下几种常见模式：

* Mapping数据转换：以服务器端返回的data数据中的mapping，转换原始值为中文。

```html
<th field="gender" data-options="formatter:function(value){ return formatRefrence('manage_customer_datagrid','allGenders',value);} ">性别</th>
```

* 行操作按钮格式化：每行的最后一列可以定义为操作列，提供对本行的快捷操作连接按钮，但是一般在点击该类按钮的时候需要传入本行相关的参数，这里需要通过formatter方式构造整个链接及参数，需要使用到js的template,你可以采用任意的第三方js-template，但是默认情况下，acooly框架提供了简单的实现，可以支付几乎所有场景。

    * formatAction方法是老的兼容方法，只实现了对第一个参数（manage_customer_action）指定的元素下的首个{0}替换为当前行的id（row.id）
    * formatString是推荐的标准模板方法，在第1个参数指定的元素下定义{0-n}的partten,采用后续传入的0-n个参数一一对应的替换。

```html
<th field="rowActions" data-options="formatter:function(value, row, index){
    return formatAction('manage_customer_action',value,row)
}">动作</th>

<th field="rowActions" data-options="formatter:function(value, row, index){
    return formatString($('#manage_customer_action').html(),row.id);
}">动作</th>
```

* 其他常用格式化封装


|格式化方法           |说明                         |输出样例
|-------------------|-----------------------------|---------------
|moneyFormatter     |金额元的formatter，原始值为元   |10000.02 -> 10,000.02  
|centMoneyFormatter |金额分格式化为2为小数的元        |1000002 -> 10,000.02      
|fileSizeFormatter  |文件大小格式化,原始值为Byte      |15.02M         
|dateFormatter      |日期格式化（yyyy-MM-dd）,日期对象或日期时间字符串（支持GMT）|2015-09-09
|dateTimeFormatter  |格式化为：yyyy-MM-dd HH:mm:ss  |2015-09-09 12:12:12
|timeFormatter      |格式日期对象为时间格式：HH:mm:ss |12:12:12
|secondFormatter    |格式化秒为可读格式。            |1天12:12:01
|millisecondFormatter|格式化毫秒为可读格式。         |1天12:12:01.222
|contentFormatter   |格式化长文本数据。格式化为可以点击显示/隐藏模式,默认显示20字符|
|linkFormatter      |连接格式化，格式化为可点击弹出新页面访问的方式|
|jsonFormatter      |格式化json数据为可读格式。        |

调用案例：

```html
<th field="amount" formatter=“moneyFormatter”>金额</th>
```


### Form表单

本节将先介绍EasyUI本身的默认表单验证，以及对应扩展实现，然后在详细介绍acooly框架中的应用。

#### EasyUI验证框架

EasyUI中的表单验证主要通过其内置的验证器(validator)实现，除了required属性基本应用于所有表单元素外，其他一般都应用在: easyui-validatebox（文本框） 和 easyui-numberbox(数字框)。

验证相关的属性如下：

* validType：验证类型见下示例
* missingMessage：未填写时显示的信息
* invalidMessage：无效的数据类型时显示的信息
* required="true" 必填项

easyui-numberbox专用：

* min：最小值
* max：最大值
* precision：精度，小数点位数

以上属性中，validType是验证的核心，指定了该表单采用什么方式验证，支持多种验证器同时使用，也支持外部扩展。

**内置验证**

|validType   |comment                     |demo
|------------|----------------------------|------
|length      |长度验证，一个字符计算为1个长度。 |validType="length[2,4]" 
|email       |email格式验证					   |validType="emal" 
|url         |url格式验证                   |validType="url" 
|remote      |远程验证。ajax方式访问url，返回值为true表示成功。这里不推荐使用，因为每输入一个字符都需要请求一次服务器端，建议改总需求采用人工验证方式在提交前处理。|


**扩展验证**

acooly框架根据常见功能，扩展了一些常见的验证，在框架的功能中可以直接使用。

源代码文件：manage/script/acooly.easyui.js

|validType   |comment                      |demo
|------------|-----------------------------|---------------
|bytelength  |长度验证，一个字节计算为1个长度，一个汉字算2个长度，主要应用于ORACLE等数据库。|validType="bytelength[2,4]"
|equals      |判断两个表单值相等。|validtype="equals['#newPassword']"
|CHS         |汉字验证，要求输入的必须都是汉字|validType="CHS" 
|mobile      |移动手机号码验证                |validType="mobile" 
|zipcode     |国内邮编验证                   |validType="zipcode" 
|account     |常用账号格式（只能包括 _ 数字 字母）|validType="account" 
|commonRegExp|正则验证 |validType="commonRegExp['[\\\\w]{6,16}','密码由任意字母、数字、下划线组成，长度6-16字节']"            
|validImg    |图片验证，param[0]为bmp,jpg,gif(用逗号隔开),param[1]为错误提示消息 |
|simplecsv   |简单csv格式验证，包括逗号风格格式|

如需要制定扩展验证，可以采用如下方法或参考以上自定义验证的实现源代码。

```js
$.extend($.fn.validatebox.defaults.rules, {
	customRuleName : {
		validator : function(value, param) {
				//验证逻辑
		},
		message : '自定义消息'
	}
});
```
> 强烈建议：如果有扩展需求，统一提交到acooly框架的isuee，有框架统一扩展使用。

**验证实例**

简单验证：

```html
邮件：<input name="email" type="text" class="easyui-validatebox" required="true"
	validType="email" missingMessage="请输入您的电子邮箱地址" invalidMessage="请输入正确的电子邮箱格式"/>
<br><br>
昵称：<input name="nickName" type="text" class="easyui-validatebox" required="true"
	validType="length[2,4]" missingMessage="请输入您的昵称" invalidMessage="姓名只能是2-4个字"/>
<br><br>
```

组合验证：

```html
姓名：<input name="realName" type="text" class="easyui-validatebox" required="true"
	data-options="validType:['CHS','length[2,4]']" missingMessage="请输入您的真实姓名" invalidMessage="姓名只能是2-4个汉字"/>
```

#### 表单验证

acooly框架的表单提交采用jquery.form插件方案的ajaxSubmit方法，在ajaxSubmit方法中可以通过beforeSerialize和beforeSubmit完成前置自定义验证，最后提交前会调用EasyUI验证框架验证。

>jquery.form的参考文档请参见：http://malsup.com/jquery/form/#options-object

表单的验证的完整案例请参考：$.acooly.framework.create 封装发放的源代码。

```js
$('#formId').ajaxSubmit({
	beforeSerialize: function(formData,options){
		// 序列化form表单值前，可以动态修改表单的值。
	},
	beforeSubmit: function(formData, jqForm, options){
		// 1、提交前，可以插入自定义的验证
		// 2、最后调用EasyUI的验证框架：$('#formId').form('validate');
		// 3、返回true表示通过，反正为false
	},
	success: function（result, statusText）{
		// ajax请求后的成功响应，如果统一采用acooly框架定义的JsonResult,则模板代码为：
		if (result.success) {
			// 成功的处理
		}else{
			// 失败的处理
		}
		// 消息提示，如果有则底部弹出提示框显示。
		if (result.message) {
			$.messager.show({title : '提示',msg : result.message});
		}
	},
	error : function(XmlHttpRequest, textStatus, errorThrown) {
		// 统一处理服务器端异常，一般为非200返回情况。
		$.messager.alert('提示', errorThrown);
	}
});

```

以上模块代码是ajaxSubmit在acooly中的常规实现，你可以根据需求，参考jquery.form插件的需求进行调整。然后一般通过点击按钮时间调用该方法实现ajax form表单提交。


#### 数据绑定

表单操作中，涉及提交数据自动绑定为domain/entity对象，响应数据（编辑时）domain/entity自动绑定视图表单的功能，这种方式有利于提高高发效率。目前acooly框架中，上送数据的绑定为entity采用的是springMvc的原生解决方案，这里不详细介绍。

在编辑时拉取的entity的属性数据自动绑定视图的表单方案选择的是jodd.form的springside扩展方案：jodd.form taglib。

* 实现功能：指定的scope内的实体变量名作为实体数据源，反射实体属性名称，与标签内的表单名称匹配，如果匹配上，则自动为表单赋值为属性值，支持常规的：input和select表单。

* 用法参考：自动代码生成后的{entity}Edit.jsp视图源代码。

```html
<form id="manage_customer_editform" action="#" method="post">
  <jodd:form bean="customer" scope="request">
      
    <!-- 表单 -->
      
  </jodd:form>
</form>
```


### 页面框架
页面框架的封装主要是使用esayui和ztree对后台管理的页面框架整体的结构，基础功能的封装，包括整体布局，两级菜单的显示，logo，系统功能等。这里不做详细的介绍，如有兴趣，请参见源代码。

/manage/script/acooly.system.js
/manage/script/acooly.layout.js
/manage/script/acooly.portal.js


## 案例经验

### 标准CRUD

请参考：自动代码生成的代码。所有自动代码生成的单实体的功能包含了网站的CRUD功能。

### 分类管理

分类管理在后台管理开发中是比较常见的场景。从数据结构上来看，首先有一个多级或无限极的分类实体，然后由一个具体分类下管理的主体实体。所以在这种场景下，我们需要管理的是2个实体,但是在视图上，我们习惯采用单个整合视图来操作个管理更符合人类习惯。

请参考：openapi-framework项目的openapi-framework-manage模块中的服务管理功能。

### 主从管理

请参考：acooly-showcase的客户管理模块。

# 用户视图开发

## 通用网站开发

自定义taglibs介绍，mask，title，paging的标签，布局方式，缓存等，ajax
待补充

## 微网站开发

微网站的开发，目前主要采用jq-weui方案。