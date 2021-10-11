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

这时候 target 目录下的 `benchmarks-jdbc.jar` 就准备好了。
接下来可以通过 `java -jar benchmarks-jdbc.jar` 进行测试，项目会输出如下指标：

execution count is :  -- 一共执行了多少次
latency min time is : -- 最短执行时间
latency max time is : -- 最长执行时间
latency avg time is : -- 平均执行时间
latency total time is : -- 所有执行时间累加的总和

我们还需要准备测试数据，一般我们都使用 sysbench 对数据库进行建库表、插入测试数据的初始化

```shell
# 安装 sysbench
curl -s https://packagecloud.io/install/repositories/akopytov/sysbench/script.rpm.sh | sudo bash
sudo yum -y install sysbench

# 通过 sysbench 插入数据
sysbench oltp_read_only --mysql-host=10.0.0.1 --mysql-port=3306 --mysql-user=root --mysql-password='root' --mysql-db=sbtest --tables=10 --table-size=100000 --report-interval=5 --time=200 --threads=10 --max-requests=0 --percentile=99 --mysql-ignore-errors="all" --rand-type=uniform --range_selects=off --auto_inc=off cleanup
sysbench oltp_read_only --mysql-host=10.0.0.1 --mysql-port=3306 --mysql-user=root --mysql-password='root' --mysql-db=sbtest --tables=10 --table-size=100000 --report-interval=5 --time=200 --threads=10 --max-requests=0 --percentile=99 --mysql-ignore-errors="all" --rand-type=uniform --range_selects=off --auto_inc=off prepare
```
这里要记得，`table-size` 要跟 `PooledHikariPointSelectBenchmark` 中的 `TEST_TABLE_SIZE` 相同。

