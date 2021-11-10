<!-- title: BOSS开发经验  -->
<!-- type: core -->
<!-- author: zhangpu -->
<!-- date: 2019-04-26 -->

# 经验

## 图片(文件)上传

后台业务实体中，某个字段需要上传文件（图片或其他类型），并在该属性中保存相对路径（相对媒体服务器根，一般是ofile组件配置的`serverRoot`和`storageRoot`）。

目前后台可直接实现配置约定的自动化上传：

1. 完成实体设计（通过表自动代码生成即可），得到需要上传的实体属性名（例如：photoPath）
2. 在BOSS开发中，该实体的编辑表单处，设置表单名称约定为：${上传属性名称}_uploadFile（例如：`<input type="file" name="photoPath_uploadFile" class="custom-file-input">`）
3. 对应的Controller中在onSave方法中插入上传组装逻辑`doUpload(request, entity);`，实现自动上传并获取相对路径，然后设置到实体的对应属性上。

以上所有逻辑可通过自动代码生成体验（acooly-coder插件）：需要在表结构设计时，设置字段备注类型为`file`,例如：`{title:’头像’, type:’file’}`

## 文件批量上传

参考：showcaseMemberFile

场景： 一般会有一个文件元数据的列表，然后按钮通过弹出框方式打开一个上传界面，在上传界面中进行批量上传，完成后，关闭上传弹出框，刷新文件列表。

1. html: /manage/showcase/member/showcaseMemberFileUploader.ftl
2. js: /manage/assert/showcaseMember.js($.acooly.showcase.memberFile.xxx)
3. controller: com.acooly.showcase.member.web.ShowcaseMemberFileManagerController

## 表单动态解析

以身份证号码为例，输入身份证号码的文本框失去焦点，则触发远程解析并处理本地表单互动。

1. 身份证表单的`onblur`事件触发身份证解析
2. 通过ajax访问服务器端解析身份证，同时在请求开始和请求结束处理loading效果
3. ajax前调用本地合法性验证
4. ajax请求成功后，回身份证解析相关表单。例如：生日，年龄，性别，属相，生肖，区域等。

> 详情请参考：showcase内的会员管理的添加/编辑功能中的`身份证号码`表单。

相关代码：

1. html: `templates/manage/showcase/member/showcaseMemberEdit.ftl`
2. js: `static/manage/assert/showcaseMember.js#idCardVerify`
3. java: `com.acooly.showcase.member.web.ShowcaseMemberManagerController#parseIdCard`

## 下拉搜索选择

应对n~几万的数据量的本地搜索非分页下拉多选表单。select2

下拉数据可直接后台FTL常规输出

### 单选

```html
<select name="broker" class="form-control select2bs4" data-placeholder="请选择经纪人...">
    <#list users as user>
    <option value="${user.username}">${user.pinyin}:${user.realName}</option>
</#list>
</select>
```

没了，OK了，表单会自动绑定回传值。

### 多选

```html

<div class="form-group row">
    <label class="col-sm-3 col-form-label">客户经理</label>
    <div class="col-sm-9">
        <select name="manager" id="manage_showcaseMemberProfile_editform_manager" class="form-control select2bs4" data-placeholder="请输入客户经理..." multiple="true">
            <#list users as user>
            <option value="${user.username}">${user.pinyin}:${user.realName}</option>
        </#list>
        </select>
    </div>
</div>
```

> 多选只需在多选的HTML代码中增加：`multiple="true"`

1. 提交时，多选的多个数据使用逗号分隔。需要服务器端进行对应处理。
2. 编辑时，回绑到表单需要使用JS代码完成（一般在编辑实体的底部增加`<script>`段）。如下

```js
$(function () {
    // 设置初始值(添加时`${entity.manager}`为空，会自动忽略)
    // manage_showcaseMemberProfile_editform : 你编辑页面的表单ID
    // manager: 你的多选下拉框的表单名称
    $.acooly.formVal("manage_showcaseMemberProfile_editform", "manager", '${entity.manager}');
    // 注册事件
    $.acooly.formObj("manage_showcaseMemberProfile_editform", "manager").on('select2:select', function (e) {
        let data = e.params.data;
        console.log(data);
    });
});
```

### 事件onchange

```js
$(function () {
    $.acooly.formObj("manage_showcaseMemberProfile_editform", "manager").on('select2:select', function (e) {
        let data = e.params.data;
        console.log(data);
    });
});
```

### Select取值和设值

获取：使用jquery常用方式获取。例如: $("#表单ID").val(); 设置：`$("#表单ID").val("newVal").trigger('change')`

也可以使用框架封装的表单取值/设值工具类。

```js
// 读取id为manage_showcaseMemberProfile_editform的表单下的name="manager"的下拉框的值
let val = $.acooly.formVal("manage_showcaseMemberProfile_editform", "manager");
// 设置id为manage_showcaseMemberProfile_editform的表单下的name="manager"的下拉框的值
$.acooly.formVal("manage_showcaseMemberProfile_editform", "manager", "newVal");
```

### tooltip

```html
<a title="需要提示显示的内容" class="easyui-tooltip"><i class="fa fa-info-circle" aria-hidden="true"></i></a>
```

