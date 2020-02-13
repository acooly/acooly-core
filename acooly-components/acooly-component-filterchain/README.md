<!-- title: 过滤器组件   -->
<!-- type: infrastructure -->
<!-- author: qiubo -->
<!-- date: 2019-12-10 -->
## 1. 组件介绍

此组件提供FilterChain,应用可以此组建快速组装FilterChain。

## 2. 使用说明

maven坐标：

     <dependency>
        <groupId>com.acooly</groupId>
        <artifactId>acooly-component-filterchain</artifactId>
        <version>${acooly-latest-version}</version>
      </dependency>

`${acooly-latest-version}`为框架最新版本或者购买的版本。

### 2.1 例子
         
#### 定义Context

	public class TestContext extends Context {}

#### 定义FilterChain

	public class TestFilterChain extends FilterChainBase<TestContext> {}

#### 定义Filter

	public class TestFilter implements Filter<TestContext> {
		private static final Logger logger = LoggerFactory.getLogger(TestFilter.class);
		
		@Override
		public void doFilter(TestContext context, FilterChain<TestContext> filterChain) {
			logger.info("in");
			filterChain.doFilter(context);
			logger.info("out");
		}
		
		@PostConstruct
		public void init() {
			logger.info("init");
		}
	}

#### 使用

	@Resource(name = "testFilterChain")
	private FilterChain<TestContext> filter;
	
	TestContext testContext = new TestContext();
	filter.doFilter(testContext);

## 3. FAQ

### 3.1. 如何配置这些组件？

不需要使用者配置组件，装配组件。filterchain会扫描`acooly.filterchain.scanPackage`定义的包，`Filter`和`FilterChain`会注册到spring容器。

### 3.2. 如何定义filter顺序

filter可以重写`com.acooly.module.filterchain.Filter#getOrder`方法

### 3.3. 如何实现filter重入

	TestContext testContext = new TestContex();
	//调用filter链
	filter.doFilter(testContext);
	//设置Context对象可以重入
	testContext.reentry();
	//调用filter链
	filter.doFilter(testContext);
