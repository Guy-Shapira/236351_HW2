package RestService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import zookeeper.ZkServiceImpl;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Configuration
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class main {


    public static ZkServiceImpl zkService;
    public static void main(String[] args){
        try {
            startup_zk(args[0], args[1]);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        SpringApplication.run(main.class, args);
    }

    static void startup_zk(String zkServers, String port) throws UnknownHostException {
        zkService = new ZkServiceImpl(zkServers);
        zkService.createParentNode(null, null);
        String nodeName = InetAddress.getLocalHost().getHostAddress() + ":" + port;
        zkService.addRestNode(nodeName, nodeName);
    }
}
