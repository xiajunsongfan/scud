## scud rpc 项目
### 项目介绍：
scud 基于netty4开发的一个的RPC服务（集群和单机模式）

### 使用方式：
**1.添加依赖**
```xml
    <dependency>
        <artifactId>scud-core</artifactId>
        <groupId>com.xj.rpc</groupId>
        <version>1.0.0-SNAPSHOT</version>
    </dependency>
	
    <!--使用spring或注解方式，添加如下依赖-->
    <dependency>
        <artifactId>scud-spring-boot-starter</artifactId>
        <groupId>com.xj.rpc</groupId>
        <version>1.0.0-SNAPSHOT</version>
    </dependency>
```
* 1.1 项目类路径添加scud.properties配置
```properties
    ####服务发布者配置项#######
    #服务使用的IP地址
    provider.ip=0.0.0.0
    #服务使用的端口
    provider.port=6157
    #工作线程数
    work.core.pool.size=8
    #netty work线程数
    netty.work.poole.size=2
    #连接超时
    connect.timeout=1000
    #方法性能监控
    method.performance.monitor=false
    #监控的性能数据处理方式，默认处理器为直接打印到log文件,如果开启了console默认会将监控数据发到console
    #monitor.handler=com.xj.scud.monitor.MonitorHandlerImpl

    ######公共配置########
    #服务工程的名字，不同工程必须使用不同名字
    aap.name=scud-test
    #zookeeper地址
    zk.host=192.168.50.166:2181
    #是否使用zookeeper，true为使用
    use.zk=false
```
**2.服务端实现**
* 2.1 在项目类路径添加 scud.properties配置
* 2.2 java api方式
```java
     Provider<Test> provider = new Provider<>(Test.class, new TestImpl(), "1.0.1");
     ScudServer server = new ScudServer(conf, provider);
     server.start();
 ```
* 2.3 注解方式
```java
    //可以自己实现异步方法，也可以在public User test(String s);同步方法上打@Async注解自动生成
     public CompletableFuture<User> testAsync(String s) {
            return CompletableFuture.supplyAsync(() -> this.test(s));
     }
     @Async
     public User test(String s){
         //...
         return null;
     }
```
**3.客户端实现**     
* 3.1 客户端配置
```java
    ClientConfig<Test> conf = new ClientConfig();
    conf.setHost("127.0.0.1:7890;127.0.0.1:7891").setRoute(RouteEnum.RANDOM).setTimeout(2000).setInterfaze(Test.class).setVersion("1.0.1").setWorkThreadSize(1).setType(SerializableEnum.PROTOBUF);
    Test t = ScudClientFactory.getServiceConsumer(conf);
```
* 3.2 同步阻塞模式
```java
    long st = System.currentTimeMillis();
    String u = t.test();
    System.out.println(u.toString());
    User user = t.test("test");
    System.out.println(user.toString());
    System.out.println((System.currentTimeMillis() - st) + "ms ");
```
* 3.3 客户端异步，返回future对象
```java
    /** 服务端已经实现了异步方法，客户端直接调用 **/
    CompletableFuture<User> future = t.testAsync("CompletableFuture-test");
    future.thenAccept(user -> System.out.println(user));
    
    /** 服务端同步方法，客户端使用Future模式 **/
    Future<User> f = RpcContext.invokeWithFuture(new AsyncPrepare() {
        @Override
        public void prepare() {
           t.test("test");
        }
    });
    System.out.println(f.get());
```
* 3.4 客户端异步，异步回调模式
```java
    RpcContext.invokeWithCallback(new AsyncPrepare() {
       @Override
        public void prepare() {
           t.test("test");
        }
    }, new RpcCallback() {
        @Override
        public void success(Object value) {
            System.out.println("callback: " + value);
        }

        @Override
        public void fail(Throwable error) {
            error.printStackTrace();
        }
    });
```
**4.结合spring**

_spring两种使用方式,xml和注解_
* 4.1 xml方式
```xml
    <?xml version="1.0" encoding="UTF-8"?>
    <beans xmlns="http://www.springframework.org/schema/beans"
           xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
           xmlns:scud="http://www.xj.com/schema/scud"
           xsi:schemaLocation="
            http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd
            http://www.xj.com/schema/scud http://www.xj.com/schema/scud/scud.xsd">

        <bean id="testService" class="com.xj.scud.idl.TestImpl"/>

        <scud:server>
            <scud:providers>
                <scud:provider interface="com.xj.scud.idl.Test" ref="testService" version="1.0.1"/>
            </scud:providers>
        </scud:server>

        <scud:client id="client" host="127.0.0.1:7890" interface="com.xj.scud.idl.Test" connentTimeout="4000" timeout="2000" lazy-init="true" version="1.0.1"/>
    </beans>
```
* 4.2 注解使用
```java
    /** 1. 添加spring注解扫描路径 **/
    @SpringBootApplication(scanBasePackages = "com.xj.xxx")
    /** 2. 服务端实现类需要打上Scud注解 **/
    @Scud(version = "1.0.0")
    public class TestImpl implements Test {}
    /** 3. 客户端对象打上@Client注解 **/
    @Client(version = "1.0.1", host = "127.0.0.1:6157", lazy = true)
    private Test clent;
```
例子可以参考scud-example
### 集群模式
```
    1. 集群使用zookeeper进行管理，zookeeper客户端使用了自己封装的 [zkclient](https://github.com/xiajunsongfan/zkclient)
    2. 服务发布者和客户端只需要在scud.properties中配置 use.zk=true 和 zk.host 地址即可(纯java客户端模式,需要把值设置到ClientConfig中)

```
