# JMH Database Test

## 关于
JMH 是 OpenJDK 自带的压力测试工具，用于测试 Java 程序的性能。
本用例基于 JHM 针对相应数据库用例进行测试

## 准备
使用本用例之前，需要安装好 JDK、git 等基础工具。
确保环境没问题后，下载代码 

```shell
git clone https://github.com/taojintianxia/jmh-dbtest.git
cd jmh-dbtest/src/main/java/com/livi/jmh
```

修改 `PooledHikariPointSelectBenchmark` 文件，将 jdbc-url、username、password 都设置好。

设置好相应配置后，重新进入 `jmh-dbtest` 编译打包：

```
mvn clean install
```



