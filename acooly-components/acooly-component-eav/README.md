## 1. 组件介绍

此组件提供[EAV](https://en.wikipedia.org/wiki/Entity%E2%80%93attribute%E2%80%93value_model)模式。


## 2. 使用

### 2.1 EAVType#syntax定义

    long<10,100>
    double<0.1,100.0>
    enum<A,B,C>
    string<0，20>
    boolean
    date
    
### 2.2 boolean

由于mysql及mysql jdbc驱动限制，true转换为1，false转换为0，所以在使用时用1,0代替true，false