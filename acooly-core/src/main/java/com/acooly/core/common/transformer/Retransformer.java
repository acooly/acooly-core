package com.acooly.core.common.transformer;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.agent.builder.AgentBuilder;

/**
 * Zhouxi O_o
 *
 * @author xi
 * @description
 */
public interface Retransformer {

    /**
     * 对于简单单个Class的转换这个已经可以满足
     */
   default  void simpleTransformer( ByteBuddy byteBuddy ) throws Throwable{};

    /**
     * 对于复杂多个Class的转换请用这个
     */
   default AgentBuilder agentTransformer( AgentBuilder agentBuilder ) throws Throwable{return null;};

}
