package RestService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Configuration;

import zookeeper.ZkServiceImpl;

@Configuration
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class main {
    public static ZkServiceImpl zkService;
    public static void main(String[] args){
        startup_zk(args[0]);
        SpringApplication.run(main.class, args);
    }

    static void startup_zk(String zkServers){
        zkService = new ZkServiceImpl("localhost:2381");
    }
}
