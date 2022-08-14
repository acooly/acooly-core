package com.acooly.core.test.dubbo;

/**
 * Zhouxi O_o
 *
 * @author xi
 * @description
 */
//@Service(version = "1.0.0")
public class DefaultTestService implements TestService {

    @Override
    public String sayHello(String str) {
//        RpcContext rpcContext = RpcContext.getContext();
//        return String.format("Service [name :%s , port : %d] %s(\"%s\") : Hello,%s",
//                "test",
//                rpcContext.getLocalPort(),
//                rpcContext.getMethodName(),
//                str,
//                str);

        return null;
    }
}
