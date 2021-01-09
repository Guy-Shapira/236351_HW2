package zookeeper;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.ZkClient;
import org.apache.zookeeper.CreateMode;

@Slf4j
public class ZkServiceImpl implements ZkServiceAPI {
    public final String MEMBER = "/member";


    private ZkClient zkClient;

    public ZkServiceImpl(String zkServers) {
        zkClient = new ZkClient(zkServers , 120000, 30000);
    }

    public void closeConnection(){
        zkClient.close();
    }


    @Override
    public void addToLiveNodes(String nodeName, String data, String city) {
        city = "/" + city;
        if(!zkClient.exists(MEMBER + city)){
            zkClient.create(MEMBER, "all live z-nodes", CreateMode.PERSISTENT);
        }
        String childNode = MEMBER + city + "/" + nodeName;
        if(zkClient.exists(childNode)){
            return;
        }
        zkClient.create(childNode, data, CreateMode.EPHEMERAL);
    }

    @Override
    public List<String> getLiveNodes(String city) {
        if(!city.equals("")) {
            if (!zkClient.exists(MEMBER + "/" + city)) {
                throw new RuntimeException("No node /liveNodes exists");
            }
        }
        if(city.equals("")) {
            return zkClient.getChildren(MEMBER);
        }
        else {
            return zkClient.getChildren(MEMBER + "/" + city);
        }
    }

    @Override
    public void createParentNode(String city) {
        if(city != null){
            city = "/" + city;
        }
        else{
            city = "";
        }
        if (!zkClient.exists(MEMBER + city)) {
            zkClient.create(MEMBER + city, "all live z-nodes", CreateMode.PERSISTENT);
        }

    }

    @Override
    public String getZNodeData(String path) {
        return zkClient.readData(path, null);
    }

    @Override
    public void registerChildrenChangeWatcher(String path, IZkChildListener iZkChildListener) {
        zkClient.subscribeChildChanges(path, iZkChildListener);

    }


}
