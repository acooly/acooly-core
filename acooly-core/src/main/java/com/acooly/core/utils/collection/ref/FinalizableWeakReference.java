/*
 * acooly.cn Inc.
 * Copyright (c) 2016 All Rights Reserved.
 * create by zhangpu 
 * date:2016年3月20日
 *
 */
package com.acooly.core.utils.collection.ref;

import java.lang.ref.WeakReference;

/**
 * 带有回调清除功能的弱引用对象。
 * 
 * @author Agreal·Lee (e-mail:lixiang@yiji.com)
 * 
 * @param <T> 引用类型
 */
public abstract class FinalizableWeakReference<T> extends WeakReference<T> implements FinalizableReference<T> {
	
	/**
	 * 创建一个引用给定对象的新的带有回调清除功能的弱引用。
	 * 
	 * @param referent 新的带有回调清除功能的弱引用将引用的对象。
	 */
	protected FinalizableWeakReference(T referent) {
		super(referent, FinalizableReferenceQueue.getInstance());
	}
	
	/**
	 * 创建一个引用给定对象的新的带有回调清除功能的弱引用。
	 * 
	 * @param referent 新的带有回调清除功能的弱引用将引用的对象。
	 * @param finalizableReferenceQueue 该引用向其注册的可回调清理的队列。
	 */
	protected FinalizableWeakReference(T referent, FinalizableReferenceQueue finalizableReferenceQueue) {
		super(referent, finalizableReferenceQueue == null ? FinalizableReferenceQueue.getInstance()
			: finalizableReferenceQueue);
	}
}
