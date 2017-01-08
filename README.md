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
    conf.setIp("127.0.0.1").setPort(7890).setTimeout(2000).setServerClass(Test.class).setWorkThreadSize(1).setType(SerializableEnum.PROTOBUF);
    Test t = ScudClientFactory.getServiceConsumer(conf);

    long st = System.currentTimeMillis();
    String u = t.test();
    System.out.println(u.toString());
    User user = t.test("test2");
    System.out.println(user.toString());
    System.out.println((System.currentTimeMillis() - st) + "ms ");

    例子可以参考scud-example

```
