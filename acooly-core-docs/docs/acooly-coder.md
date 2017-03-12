acooly coder自动代码生成工具
====

## 简介
acooly coder是为acooly框架配套的专用代码生成工具，设计目的为跟进acooly框架封装的最佳代码实践，快速生成业务程序的骨架代码和基本功能，最大程度的减少程序员的重复劳动及规范统一代码风格和规范~

## 工具获取

### cli工具

acooly coder的发布包采用maven方式发布，目前只提供cli工具。
仓库地址：http://${host}/nexus/content/repositories/releases/

工具包maven坐标（请根据需要更新对应的版本,当前版本：4.0.0-SNAPSHOT）：

v1.x坐标：

```xml
<dependency>
  <groupId>com.acooly</groupId>
  <artifactId>acooly-module-coder</artifactId>
  <version>${acooly.coder.version}</version>
  <classifier>distribution</classifier>
  <type>zip</type>
</dependency>
```
v4.x坐标：

```xml
<dependency>
  <groupId>com.acooly</groupId>
  <artifactId>acooly-coder</artifactId>
  <version>${acooly.coder.version}</version>
  <classifier>distribution</classifier>
  <type>zip</type>
</dependency>
```

>使用说明：拉取发布包后直接解压，application.properties为配置文件，请跟进生成的需求配置，然后运行start.sh/start.bat生成代码。

## 使用手册

### 框架约定
acooly框架为了方便开发和设计，以开发经验为基础，对使用acooly进行了部分设计上的约定，通过约定降低设计和开发难度，提高效率。当然，这也符合流行的约定大于配置的设计理念。

#### 程序结构约定
工程结构完全按maven j2ee工程骨架，这个可以通过acooly-archetype生成后就可以清楚展现，程序模块的设计开发有一下基本约定。

* 实体类（entity）必须继承AbstractEntity,并提供一个名称为id的属性作为实体的唯一标识，同时也映射到数据库的物理主键。
* 每个模块的程序分为domian（entity和领域domain合并），dao，service和controller四层。命名规则依次为:EntityDao,EntityService,EntityManageController(后台)和EntityPortalController

### 数据库设计

为了方便自动生成结构性代码，对数据库表结构设计进行部分约定，但都符合常规习惯。

* 表名全部小写，不能以数字开头，必须添加备注
* 每个表必须有以id命名的物理主键，且为数字类型，如：mysql为bigint, oracle为number。
* 列名称全部小写，不能以数字开头；如果存在多个自然单词的组合，使用下划线分隔(\_)。如："user\_type"
* 列定义必须添加备注 （这个备注则为生成的页面及表单的label）
* 每个表必须添加create\_time和update\_time两个日期时间类型的字段，但无需手动管理 （在save/update时，框架会自己维护创建时间和最后修改时间）。
* 如果有选项类型的字段，其选项值使用类json格式写入列备注字段，自动生成工具会自动为该列对应的属性和页面生成选项。如：表列为：user\_type ,备注可以为：用户类型 {normal:普通,vip:高级}
* 特别注意，强烈要求选项类（自动生成枚举类的）字段项目全局唯一名称，否则会生成enum名称相同的枚举相互覆盖。

### 代码生成

自动代码生成的方案与目前流行的方式设计上是基本一致的。主要核心设计为：

* 通过读取数据库的元数据，获得实体对应的表的所有列，类型，长度，可选，备注等信息，通过结构化处理作为自动代码生成的元数据。
* 通过freemarker作为模板引擎，定义一套或多套框架业务代码的最佳实践模板。
* 工具化自动生成目标代码，作为业务开发的start...

目前的工具，形态只有cil和swt的简单界面（太懒，忘记开发插件）。 主要通过配置文件驱动工具行为，主配置文件为application.properties.

下面以一个案例来简单说明这个开发过程。

#### 设计表

本案例中，我们以mysql为案例设计一个客户信息表，名称为:dm_customer, 表结构如下：

```sql
CREATE TABLE `dm_customer` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `username` varchar(255) NOT NULL COMMENT '用户名',
  `age` int(11) NOT NULL COMMENT '年龄',
  `birthday` datetime NOT NULL COMMENT '生日',
  `gender` int(11) DEFAULT NULL COMMENT '性别 {1:男,2:女,3:人妖}',
  `mail` varchar(64) DEFAULT NULL COMMENT '邮件',
  `mobile_no` varchar(32) DEFAULT NULL COMMENT '手机号码',
  `real_name` varchar(16) DEFAULT NULL COMMENT '姓名',
  `idcard_no` varchar(18) DEFAULT NULL COMMENT '身份证号码',
  `subject` varchar(128) DEFAULT NULL COMMENT '摘要',
  `status` int(11) NOT NULL DEFAULT '1' COMMENT '状态 {0:无效,1:有效}',
  `customer_type` varchar(16) NOT NULL COMMENT '客户类型 {normal:普通,vip:重要,sepc:特别}',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `comments` varchar(255) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;
```

请将上面的表结构DLL在数据库中执行，生成本次演示的数据表。

> 本案例基于本系列的第一篇文章《acooly框架一:archetype》，假设你已生成了工程，名称为acooly-demo，并建立数据库acooly，然后成功运行的基础上.

#### 配置

打开工具包后，你会看到根目录下有application.properties文件，请打开进行配置。

application.propertes

```ini
## database connection configurations
jdbc.driver=com.mysql.jdbc.Driver
jdbc.url=jdbc:mysql://localhost:3306/acooly?useUnicode=true&amp;characterEncoding=UTF-8
jdbc.username=root
jdbc.password=******

## Code generator configurations
# 生成的代码防止的目标java工程根路径
generator.workspace=/Users/zhangpu/workspace/eclipse/acooly-demo
# 生成的模块代码的根包
generator.rootPackage=com.acooly.demo
# 生成的模块的业务根路径
generator.pagePath=/manage/demo
# 表名转换为实体名称时，需要忽略的表的前缀。如:表明为dm_customer,配置该值为dm_ ,则生成的实体名称为Customer
generator.tableToEntityIgnorPrefix=dm_
# 模板方案路径，默认设置为easyui
generator.templatePath=classpath:/template/easyui

# 自定义数据类型映射,类JSON格式。格式{数据库数据类型1:java数据类型1（如果需要imports则需要完整包路径,...}
# 这里设置了映射后，会覆盖生成器默认的数据类型映射规则
generator.dataType.declare={decimal:java.math.BigDecimal}
# 持久化方案(可选: jpa,mybatis)
generator.persistent.solution=mybatis
# 代码作者
generator.code.author=acooly
# 代码版权声明主体
generator.code.copyright=acooly.cn
```

完成配置后，请保持退出，准备运行工具生成代码

#### 生成代码

运行环境基础要求JDK1.8，本工具支持同时生成多张表到同一个模块。

解压开自动生成工具，根目录下存在start.bat,start.sh等启动文件，如果你的windows，则可以直接cmd进入到当前目录，运行start.bat，根据提示输入需要自动生成的表名称，回车即可。

* 如果你在一个模块中需要生成多个表，则多个表使用空格分隔
* 在使用命令前，请检测application.properties的配置文件正确配置

运行情况如下（我这里是mac环境，使用start.sh，windows环境类似）：

```bash
请输入需要生成的表名(多个表使用空格分隔):dm_customer
2016-06-07 23:28:31,934 DEBUG - Load metadata success--> dm_customer
2016-06-07 23:28:31,943 DEBUG - Configurations:
com.acooly.module.coder.generate.GenerateConfiguration@38294acb[
  workspace=/Users/zhangpu/workspace/eclipse/acooly-demo
  rootPackage=com.acooly.demo
  pagePath=/manage/demo
  templatePath=classpath:/template/easyui
  tableToEntityIgnorPrefix=dm_
]
2016-06-07 23:28:32,260 INFO  - Generate from [dm_customer] success.
```

生成成功后，一般情况，只需重新启动服务器，从controller中找到访问的URL，配置到权限管理系统则可以直接访问，完成开发的第一步。

### 运行查看效果

OK，如果上步成功，请回到你的IDE，刷新工程，你应该看到在原来空的 src/main/java目录下存在了新生成的代码，并编译通过。同时在src/main/resources/META-INF/resource/jsp/manage/demo下存在了该模块的管理界面。工程如图：

![](https://github.com/acooly/acooly.github.io/blob/master/_posts/resource/images/acooly/coder_module.png?raw=true)

#### 添加资源及菜单

下面我们打开 CustomerManageController.java文件，如下：

```java
@Controller
@RequestMapping(value = "/manage/demo/customer")
public class CustomerManagerController extends AbstractJQueryEntityController<Customer, CustomerService> {
	//....
}
```

记录下该类的@RequestMapping的值: /manage/demo/customer ，用户在权限管理中配置资源和菜单。

下面我们调试启动服务器，并登录进入系统。访问系统管理中的资源管理，配置权限资源和菜单。

![](https://github.com/acooly/acooly.github.io/blob/master/_posts/resource/images/acooly/resource.png?raw=true)

>注意：如上图所示，资源的URL应该为前面记录的contoller的mapping值后加上/index.html,这个约定是为了简化资源，菜单和权限配置，资源默认的index.html则约定为入口菜单。

#### 设置角色权限

demo中，我们采用的是admin登录，默认为系统管理员权限，所以，在完成资源添加后，只需进入角色管理中为系统管理员勾选新设置的资源的访问权限即可。 系统管理 -> 角色管理 -> 系统管理员行后面的标记图标按钮。点击后如图：

![](https://github.com/acooly/acooly.github.io/blob/master/_posts/resource/images/acooly/role_setting.png?raw=true)

OK，设置完成后，请刷新菜单（在做不菜单栏的标题栏右边有个刷新图标），你会看到新的菜单出来了~~，当然如果找不到刷新图标，请直接刷新浏览器，一样样的~，不用重启哦~

点击 “业务管理” --> "客户信息管理" 菜单，相信你会看到有一个查询列表显示出来，并且在表格的头上有一些操作按钮，如：添加，批量删除，导入导出等，请先不要管是否美观，排列是否合理，点击下添加，根据界面表单的非空提示，填入必选数据项，保存。是否成功添加了一条记录！？你可以点击新增的一行数据后面的修改和删除，尝试操作体验下~~ ，当然你可以导出~~，

![](https://github.com/acooly/acooly.github.io/blob/master/_posts/resource/images/acooly/def_list.png?raw=true)

OK,acooly声称的70%的工作已完成，下面就要靠程序员来完成该功能的调整了~

### 程序员定制

调整定制前，先不要shutdown服务器，我们首先要调整的是界面，回到IDE，进入src/main/webapp/manage/demo目录，我们主要调整的是列表界面和编辑界面（添加和修改界面合一）。

#### 视图界面

主要调整列表界面的查询条件和显示列，其他视图的定制，请参考后续文档~。

**查询条件**

默认情况下，自动代码生成工具不知道你这个模块需要什么查询条件，所有它把所有的列都生成了查询项，那么，我们程序就根据实际的业务场景，只需使用delete键，删除你觉得多余的查询条件即可，当然你可以调整查询条件的顺序和长度，他们都是基础的html表单。列表页面为：customer.jsp,如下：

```html
用户名:<input type="text" size="15" name="search_LIKE_username" />
手机号码:<input type="text" size="15" name="search_LIKE_mobileNo" />
姓名:<input type="text" size="15" name="search_LIKE_realName"/>
状态:<select style="width:80px;" name="search_EQ_status" editable="false" panelHeight="auto" class="easyui-combobox"><option value="">所有</option><c:forEach var="e" items="${allStatuss}"><option value="${e.key}" ${param.search_EQ_status == e.key?'selected':''}>${e.value}</option></c:forEach></select>
创建时间:<input size="15" id="search_GTE_createTime" name="search_GTE_createTime" size="15" onFocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd HH:mm:ss'})" />
至<input size="15" id="search_LTE_createTime" name="search_LTE_createTime" size="15" onFocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd HH:mm:ss'})" />
```
OK, 调整为以上查询条件，保留：用户名，手机，姓名，状态和创建时间作为查询条件。保持页面代码后，请切换到浏览器，刷新该tab（注意，不用整页刷新，框架页面右边有个刷新图标，只刷新当前tab）

**显示的列**

与查询条件一样，根据你的需要调整显示的列名，位置和格式（格式的话，稍微需要点javascript或easyui的基础哈）。我们调整如下：

```html
<th field="showCheckboxWithId" checkbox="true" data-options="formatter:function(value, row, index){ return row.id }">编号</th>
<th field="id">ID</th>
<th field="username">用户名</th>
<th field="realName">姓名</th>
<th field="age">年龄</th>
<th field="birthday" formatter="formatDate">生日</th>
<th field="gender" data-options="formatter:function(value){ return formatRefrence('manage_customer_datagrid','allGenders',value);} ">性别</th>
<th field="idcardNo">身份证号码</th>
<th field="mail">邮件</th>
<th field="mobileNo">手机号码</th>
<th field="status" data-options="formatter:function(value){ return formatRefrence('manage_customer_datagrid','allStatuss',value);} ">状态</th>
<th field="createTime" formatter="formatDate">创建时间</th>
<th field="rowActions" data-options="formatter:function(value, row, index){return formatAction('manage_customer_action',value,row)}">动作</th>
```

**编辑界面**

编辑界面的名称为 customerEdit.jsp, 里面就是一个标准的form表单，请程序员根据需求进行布局调整，包括位置，表单大小，宽度等。

> 一般情况，自动代码生成工具会把创建时间，修改时间也生成对应的表单，需要程序删除下，因为框架会自动管理这两个数据。其他非必填字段，也可以直接删除掉，关于非空和格式的check方案，我们直接使用easyui的官方标准方案并进行部分扩展，请参考easyui官方文档或后续acooly框架文档。

OK，界面调整完成，我们刷新界面，新的界面就要人性化多了（这部分工作acooly觉得必须由程序员来做，我们不是神，做全自动，可能配置的时间比程序员调整的时间和成本还高~），可以给测试或用户使用啦~  

![](https://github.com/acooly/acooly.github.io/blob/master/_posts/resource/images/acooly/mod_list.png?raw=true)

### 进阶

以上完成了一个acooly框架基础开发的案例，基本展示了acooly框架开发的模式。但在实际业务开发中这些远还不够，程序员根据实际业务的需求，还需要做很多工作，包括：多实体关联关系配置，业务逻辑服务编写和调用，页面的特殊定制等，但这些本应是程序员的职责，acooly框架提供的是把大量重复的工作自动化和工具化~，后续将逐步发布各种业务开发场景在前端和服务器端的开发说明，大量的组件能力说明~


## 版本说明

### 4.0.0-SNAPSHOT

升级支持acooly4.x版本支持，增加mybatis的自动代码生成功能。

### v1.2.3

* 2016-09-23 - 提交mysql和oracle的测试表dm_customer的ddl,增加主配置文件oracle的配置 - [zhangpu] f988688
* 2016-09-23 - 新增ORACLE自动代码生成支持 - [zhangpu] bb2bf4a
* 2016-09-21 - 添加oracle的驱动依赖 - [zhangpu] 9b73dca


### v1.2.2

* 2016-08-15 16:54:29  cuifuq  [add] 列表页面更新：bigint 类型数据自动添加统计求和功能，使用方式 sum="true"   
* 统计功能：sum:求和；avg:平均；max：最大值；min：最小值

> 使用统计功能需acooly-module-security版本升级为3.4.4以上


### v1.2.1

* 2016-07-10 20:37:29  zhangpu  [add] 更新图标和样式为最新的acooly样式,同时支持awesom-font图标和easyui图标，默认采用橙色的awesome-font图标。（需acooly-module-security-3.4.6支持）
* 2016-07-08 02:47:32  zhangpu  [fix] 一系列的试图层模板优化，生成的代码更准确和人性化
* 2016-07-08 01:09:40  zhangpu  [add] 增强选项类型的自动生成规则，同时支持基于枚举(key为字符串)和静态常量(key为数字)的选项数据生成。
* 2016-07-08 01:09:40  zhangpu  [add] 重构数据类型映射，支持配置方式增加数据库与JAVA类型的映射,不如，你想让数据库的decimal类型生成为Money或BigDecimal。
* 2016-07-03 16:13:41  zhangpu  [del] 暂时删除UI(swt)界面,计划后面开发插件。
* 2016-07-03 16:13:41  zhangpu  [add] 精简工具依赖（只依赖freemarker,mysql-jdbc驱动和apache-commons-lang），整理新的发布包,通过nexus发布，完成工具和文档整理。

> 建议使用新版生成器后，升级acooly-module-security版本为3.4.6为最佳效果（向下兼容）

### v1.2.1之前
请按原有的发布包继续使用！~


