## 1. 提供能力

EventBus提供jvm内事件处理发布、处理机制，相对于spring提供的事件做以下增强：

1. 支持事件处理器同步/异步，相对于spring的机制更灵活(spring要么都异步，要么都同步)
2. 性能更好(spring事件处理器处理链很长，业务事件发布要被所有的内置事件处理器接收，EventBus点对点调用，性能开销很小)
3. 分离事件发布、业务事务，应用可以灵活选择事件和事务的关系

面向开发人员提供一下Bean:

    com.acooly.module.event.EventBus
    com.acooly.module.event.EventHandler
    
## 2. 使用

### 2.1 定义事件对象

    @Data
    public class CreateCustomerEvent {
    	private Long id;
    	private String userName;
    }
    
### 2.2 发布事件

        @Autowired
        private EventBus eventBus;
        //立即发布事件
        public void testPublish() throws Exception {
            CreateCustomerEvent event=new CreateCustomerEvent();
            event.setId(1l);
            event.setUserName("dfd");
            eventBus.publish(event);
        }
        //仅当当前事务提交成功后才发布消息,非事务环境直接发布消息
        public void testPublishAfterTransactionCommitted() throws Exception {
            CreateCustomerEvent event=new CreateCustomerEvent();
            event.setId(1l);
            event.setUserName("dfd");
            eventBus.publishAfterTransactionCommitted(event);
        }
        
    
### 2.3 定义事件处理器
    
    @EventHandler
    public class CustomerEventHandler {
        //同步事件处理器
    	@Handler
    	public void handleCreateCustomerEvent(CreateCustomerEvent event) {
    		//do what you like
    	}
        //异步事件处理器
        @Handler(delivery = Invoke.Asynchronously)
        public void handleCreateCustomerEventAsyn(CreateCustomerEvent event) {
            //do what you like
        }
    }
    
## 3. 演示

    acooly-showcase/acooly-showcase-core/src/main/java/com/ac/demo/event