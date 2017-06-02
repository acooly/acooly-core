/** create by zhangpu date:2015年3月12日 */
package com.acooly.core.utils;

import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.OrderComparator;
import org.springframework.core.Ordered;

import java.util.List;

/**
 * shutdown回收资源hook
 *
 * <p>copy from com.yjf.common.util
 *
 * @author zhangpu
 */
public class ShutdownHooks {
  private static final Logger logger = LoggerFactory.getLogger(ShutdownHooks.class.getName());
  public static List<TaskWrapper> tasks = Lists.newArrayList();

  /**
   * 添加关闭钩子
   *
   * @param runnable 钩子内容
   * @param hookName 钩子名称
   */
  public static void addShutdownHook(Runnable runnable, String hookName) {
    addShutdownHook(runnable, hookName, 0);
  }

  /**
   * 添加关闭钩子
   *
   * @param runnable 钩子内容
   * @param hookName 钩子名称
   * @param order 钩子执行顺序,顺序参考{@link Ordered}
   */
  public static void addShutdownHook(Runnable runnable, String hookName, int order) {
    if (runnable != null) {
      TaskWrapper taskwrapper = new TaskWrapper(runnable, hookName, order);
      tasks.add(taskwrapper);
    }
  }

  /**
   * 执行所有shutdown hook,建议在容器关闭时执行.
   *
   * <p>避免容器关闭后,classloader关闭,容器加载类失败. https://issues.apache.org/bugzilla/show_bug.cgi?id=56387
   */
  public static synchronized void shutdownAll() {
    List<TaskWrapper> taskWrappers = Lists.newArrayList(tasks);
    tasks.clear();

    OrderComparator.sort(taskWrappers);
    for (TaskWrapper task : taskWrappers) {
      task.run();
    }
    taskWrappers.clear();

    //在执行shutdownhook任务时,任务内部代码增加shutdownhook任务,继续执行,暂时不考虑有循环的地方
    if (!tasks.isEmpty()) {
      shutdownAll();
    }
  }

  private static class TaskWrapper implements Runnable, Ordered {
    private Runnable runnable;
    private String hookName;
    private int order;
    private boolean isRunned = false;

    public TaskWrapper(Runnable runnable, String hookName) {
      this.runnable = runnable;
      this.hookName = hookName;
    }

    public TaskWrapper(Runnable runnable, String hookName, int order) {
      this.hookName = hookName;
      this.order = order;
      this.runnable = runnable;
    }

    @Override
    public void run() {
      synchronized (this) {
        if (!isRunned) {
          logger.info("[SHUTDOWNHOOK-{}]开始执行", hookName);
          isRunned = true;
          try {
            this.runnable.run();
            logger.info("[SHUTDOWNHOOK-{}]执行结束", hookName);
          } catch (Exception e) {
            logger.error("[SHUTDOWNHOOK-{}]执行失败", hookName, e);
          }
        }
      }
    }

    @Override
    public int getOrder() {
      return this.order;
    }
  }
}
