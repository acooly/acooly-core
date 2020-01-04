业务模块组
====

## 子模块划分原则

* Platform模块划分依据于系分的逻辑结构的设计（模块划分）。减少相互依赖，隔离利于分工开发和测试
* Platform子模块间尽量做到单线依赖（禁止相互依赖），如有相互依赖场景请使用事件和消息机制解耦
* Platform-xx-common重点用于外部能力整合，扩展。

骨架默认提供xxx-platform-common和xxx-platform-core两个模块，请根据系分的逻辑结构的设计，增加xxx-platform-busimoduleX