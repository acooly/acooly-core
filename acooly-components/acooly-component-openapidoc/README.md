##配置openapidoc

1. 配置网关地址

        acooly.openapidoc.openapiGateWayUrl=http://192.168.46.18:8809/gateway.html

2. 配置包扫描路径

        acooly.openapidoc.scanPackagePartern=com.yiji.**.openapi.service

3. 配置扫描标识

    为空默认按天扫描，值为always标识总是扫描，其它标识只扫描成功一次

        acooly.openapidoc.genIndex=always


4. 配置版权信息

        acooly.openapidoc.copyright=xxoo


