package com.acooly.module.safety.signature;

/**
 * 签名对象工厂
 * 
 * @author zhangpu
 * @date 2014年6月3日
 * @param <T>
 */
public interface SignerFactory<T, K> {

	Signer<T, K> getSigner(String signType);

}
