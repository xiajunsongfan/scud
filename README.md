## scud rpc 项目
---
###项目介绍：
scud 基于netty4开发的一个单机版的RPC服务


###使用方式：
```java
	<dependency>
		<artifactId>scud-core</artifactId>
        <groupId>com.xj.rpc</groupId>
        <version>1.0.0-SNAPSHOT</version>
	</dependency>
```

```java
    /** server 端 **/
     ServerConfig conf = new ServerConfig();
     conf.setPort(7890).setCorePoolSize(12);
     Provider<Test> provider = new Provider<>(Test.class, new TestImpl(), "1.0.1");
     ScudServer server = new ScudServer(conf, provider);
     server.start();

    /** clent 端 **/
    ClientConfig<Test> conf = new ClientConfig();
    conf.setHost("127.0.0.1:7890;127.0.0.1:7891").setRoute(RouteEnum.RANDOM).setTimeout(2000).setInterfaze(Test.class).setVersion("1.0.1").setWorkThreadSize(1).setType(SerializableEnum.PROTOBUF);
    Test t = ScudClientFactory.getServiceConsumer(conf);

    /** 同步阻塞模式 **/
    long st = System.currentTimeMillis();
    String u = t.test();
    System.out.println(u.toString());
    User user = t.test("test");
    System.out.println(user.toString());
    System.out.println((System.currentTimeMillis() - st) + "ms ");

    /** 异步Future模式 **/
    Future<User> f = RpcContext.invokeWithFuture(new AsyncPrepare() {
        @Override
        public void prepare() {
           t.test("test");
        }
    });
    System.out.println(f.get());

    /** 异步Callback模式 **/
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

    /** spring使用方式 **/
    <?xml version="1.0" encoding="UTF-8"?>
    <beans xmlns="http://www.springframework.org/schema/beans"
           xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
           xmlns:scud="http://www.xj.com/schema/scud"
           xsi:schemaLocation="
            http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd
            http://www.xj.com/schema/scud http://www.xj.com/schema/scud/scud.xsd">

        <bean id="testService" class="com.xj.scud.idl.TestImpl"/>

        <scud:serverConfig id="serverConfig" port="7890"  nettyWorkPooleSize="2" corePoolSize="4"/>

        <scud:server config="serverConfig">
            <scud:providers>
                <scud:provider interface="com.xj.scud.idl.Test" ref="testService" version="1.0.1"/>
            </scud:providers>
        </scud:server>

        <scud:client id="client" host="127.0.0.1:7890" interface="com.xj.scud.idl.Test" connentTimeout="4000" timeout="2000" lazy-init="true" version="1.0.1"/>
    </beans>

    例子可以参考scud-example

```
