#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )

1.使用java程序启动
	运行${package}.Main类
2.运行测试用例
    运行${package}.test.DemoTest类
3.发布facade包
	发布包到测试环境
		mvn -T 1C clean deploy -P dev  -Dmaven.test.skip=true
	发布包到生产环境
		mvn -T 1C clean deploy -P online -Dmaven.test.skip=true
