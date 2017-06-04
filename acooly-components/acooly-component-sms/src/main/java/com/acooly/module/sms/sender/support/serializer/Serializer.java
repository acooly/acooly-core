package com.acooly.module.sms.sender.support.serializer;

import java.io.InputStream;

/** @author shuijing */
public interface Serializer<T> {
  InputStream serialize(T obj, String encoding) throws Exception;
}