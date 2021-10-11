package com.livi.jmh;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Group;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;
import org.openjdk.jmh.annotations.Warmup;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@State(Scope.Group)
@Fork(3)
@Warmup(iterations = 1, time = 5)
@Measurement(iterations = 3, time = 10)
public class PooledHikariPointSelectBenchmark {
    
    private final int TEST_DATA_SIZE = 100000;
    
    private final ThreadLocalRandom random = ThreadLocalRandom.current();
    
    private final DataSource dataSource;
    
    private Connection connection;
    
    private PreparedStatement preparedStatement;
    
    private final LinkedList<Long> latencyList = new LinkedList<>();
    
    public PooledHikariPointSelectBenchmark() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://localhost:3306/sbtest?useSSL=false&useServerPrepStmts=true&cachePrepStmts=true");
        config.setUsername("root"); 
        config.setPassword("root"); 
        config.setMaximumPoolSize(12); 
        config.setMinimumIdle(1);
        config.setConnectionTimeout(1000); 
        dataSource = new HikariDataSource(config);
    }
    
    @Setup(Level.Iteration)
    public void setup() throws Exception {
        connection = dataSource.getConnection();
        preparedStatement = connection.prepareStatement("select c from sbtest1 where id = ?");
    }
    
    @Group
    @Benchmark
    public void testMethod() throws Exception {
        long startTime = System.currentTimeMillis(); 
        preparedStatement.setInt(1, random.nextInt(TEST_DATA_SIZE));
        preparedStatement.execute(); latencyList.add(System.currentTimeMillis() - startTime);
    }
    
    @TearDown(Level.Iteration)
    public void tearDown() throws Exception {
        preparedStatement.close(); 
        connection.close();
        System.out.println("execution count is : " + latencyList.size());
        System.out.println("latency min time is : " + getMinTime(latencyList));
        System.out.println("latency max time is : " + getMaxTime(latencyList));
        System.out.println("latency avg time is : " + getAvgTime(latencyList));
        System.out.println("latency total time is : " + getTotalTime(latencyList));
    }
    
    private long getMinTime(List<Long> latencyList) {
        long min = 0;
        for (long each : latencyList) {
            if (each < min) {
                min = each;
            }
        }
        return min;
    }
    
    private long getMaxTime(List<Long> latencyList) {
        long max = 0;
        for (long each : latencyList) {
            if (each > max) {
                max = each;
            }
        }
        return max;
    }
    
    private long getAvgTime(List<Long> latencyList) {
        if (latencyList.isEmpty()) {
            return 0;
        } 
        long total = 0; 
        for (long each : latencyList) {
            total += each;
        } 
        return total / latencyList.size();
    }
    
    private long getTotalTime(List<Long> latencyList) {
        long total = 0;
        for (long each : latencyList) {
            total+=each;
        }
        return total;
    }
}
