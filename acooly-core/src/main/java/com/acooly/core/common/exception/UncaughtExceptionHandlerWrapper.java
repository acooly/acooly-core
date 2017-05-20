package com.acooly.core.common.exception;

/**
 * @author qiubo@yiji.com
 */
public class UncaughtExceptionHandlerWrapper implements Thread.UncaughtExceptionHandler {
    private Thread.UncaughtExceptionHandler defaultUncaughtExceptionHandler;
    private org.slf4j.Logger logger;

    public UncaughtExceptionHandlerWrapper(Thread.UncaughtExceptionHandler defaultUncaughtExceptionHandler) {
        this.defaultUncaughtExceptionHandler = defaultUncaughtExceptionHandler;
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        if (logger == null) {
            //avoid intial logger at object creation phase
            logger = org.slf4j.LoggerFactory.getLogger(UncaughtExceptionHandlerWrapper.class);
        }
        logger.error("线程[{}]遇到没有捕获的异常", t.getName(), e);

        if (defaultUncaughtExceptionHandler != null) {
            defaultUncaughtExceptionHandler.uncaughtException(t, e);
        }
    }

    public static void install() {
        Thread.UncaughtExceptionHandler uncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
        //ignore if installed.
        if (uncaughtExceptionHandler instanceof UncaughtExceptionHandlerWrapper) {
            return;
        }
        UncaughtExceptionHandlerWrapper uncaughtExceptionHandlerWrapper = new UncaughtExceptionHandlerWrapper(
            uncaughtExceptionHandler);
        Thread.setDefaultUncaughtExceptionHandler(uncaughtExceptionHandlerWrapper);
    }

}