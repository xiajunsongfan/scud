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
	server 端：
	ServerConfig conf = new ServerConfig();
    conf.setPort(7890).setServiceClass(Test.class).setService(new TestImpl()).setCorePoolSize(12);
    cudServer server = new ScudServer(conf);
    server.start();

    clent 端：

    ClientConfig<Test> conf = new ClientConfig();
    conf.setHost("127.0.0.1:7890;127.0.0.1:7891").setRoute(RouteEnum.RANDOM).setTimeout(2000).setServerClass(Test.class).setWorkThreadSize(1).setType(SerializableEnum.PROTOBUF);
    Test t = ScudClientFactory.getServiceConsumer(conf);

    /** 同步阻塞模式 **/
    long st = System.currentTimeMillis();
    String u = t.test();
    System.out.println(u.toString());
    User user = t.test("test2");
    System.out.println(user.toString());
    System.out.println((System.currentTimeMillis() - st) + "ms ");

    /** 异步Future模式 **/
    Future<User> f = RpcContext.invokeWithFuture(new AsyncPrepare() {
        @Override
        public void prepare() {
           t.test("22");
        }
    });
    System.out.println(f.get());

    /** 异步Callback模式 **/
    RpcContext.invokeWithCallback(new AsyncPrepare() {
       @Override
        public void prepare() {
           t.test("22");
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

    例子可以参考scud-example

```
