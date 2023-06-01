# bq-base组件的使用说明
- 本组件基于spring framework二次封装，主要是为了简化和固化常规的业务场景，当然也支持灵活的配置扩展：
- 本组件引入方法：
    ```xml
    <dependency>
        <groupId>com.biuqu</groupId>
        <artifactId>bq-base</artifactId>
        <version>1.0.2</version>
    </dependency>
    ```

## 1. 为什么要写bq-base组件

- 经常听见有架构师说：
  - 接口要做下数据缓存，避免每次都查数据库；
  - 接口要做下限流；
  - 接口要做下接口的下划线与驼峰转换；
  - yaml配置是可以加密的，做下加密就可以解决秘钥明文暴露的问题了；
  - 配置个定时任务来清理临时数据；
  - ……
  
  然而，作为一名有若干coding经验的从业者，是否想过怎么把上述的处理过程标准化、再灵活化，使其能够支持各种使用场景？
  
- "一千个人就有一千个哈姆雷特"，本人在从业过程中，非常注意这种实战经验的积累，因此也沉淀出了这个非常基础的仅依赖Spring的代码框架（不包含SpringBoot）,算作抱砖引玉吧，期望大家能够一起把基础框架做得更精致，以便让更多的人即学即用，如有深入研究的兴趣，还可以翻看源码和文档；

## 2. 使用bq-base组件有什么好处

- 新增了加密机的加密注解，可以非常方便地通过注解对加入数据库的数据字段做加解密和完整性保护；
- 封装了常用的Json处理组件，支持驼峰和下划线转换、数据脱敏，可以非常方便的单独使用工具类、或者嵌入SpringBoot框架中去使用；
- 封装了Jasypt加密组件，支持直接对敏感的配置参数加密；
- 封装了带缓存的抽象服务，可以非常方便的做到业务数据的缓存；
- 封装了http/国际化/定时任务/线程池/JwtToken转换/错误码等；
- 封装了常用的各种工具类

## 3. bq-base最佳实践
- bq-base最佳实践是配合springboot一起使用，bq-base中的能力在[bq-boot-root](https://github.com/woollay/bq-boot-root) 和[bq-boot-base](https://github.com/woollay/bq-boot-base) 
中有更好的体现；


