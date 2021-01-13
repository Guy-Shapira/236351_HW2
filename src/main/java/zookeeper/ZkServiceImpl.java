package zookeeper;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.exception.ZkNoNodeException;
import org.apache.zookeeper.CreateMode;

import Utils.Errors;
import org.apache.zookeeper.KeeperException;

@Slf4j
public class ZkServiceImpl implements ZkServiceAPI {
    public final String MEMBER = "/member";
    public final String LEADER = "/leader";

    private ZkClient zkClient;

    public ZkServiceImpl(String zkServers) {
        zkClient = new ZkClient(zkServers , 12000, 3000);
    }

    public void closeConnection(){
        zkClient.close();
    }


    @Override
    public void addToLiveNodes(String nodeName, String data, String city, String function) {
        city = "/" + city;
        function = "/" + function;
        if(!zkClient.exists(MEMBER + city)) {
            zkClient.create(MEMBER + city, "all live z-nodes", CreateMode.PERSISTENT);
        }
        if (!zkClient.exists(MEMBER + city + function))
        {
            zkClient.create(MEMBER + city + function, "member app for function " + function.substring(1) + "nodes", CreateMode.PERSISTENT);
        }
        if (!zkClient.exists(LEADER + city)) {
            zkClient.create(LEADER + city, "all leaders", CreateMode.PERSISTENT);
        }
        if (!zkClient.exists(LEADER + city + function))
        {
            zkClient.create(LEADER + city + function, "leader app for function " + function.substring(1) + "nodes", CreateMode.PERSISTENT);
        }
        String childNode = MEMBER + city + function + "/" + nodeName;
        if(zkClient.exists(childNode)){
            return;
        }
        zkClient.create(childNode, data, CreateMode.EPHEMERAL);
    }

    private List<String> getAllCities(){
        if (!zkClient.exists(MEMBER)){
            throw new RuntimeException("No servers at all!");
        }
        return zkClient.getChildren(MEMBER);
    }
    @Override
    public List<String> getLiveNodes(String city, String function) throws ZkNoNodeException {
        if(!city.equals("")) {
            if (!zkClient.exists(MEMBER)) {
                throw new RuntimeException("No node /liveNodes exists");
            }
        }
        if(city.equals("")) {
//            System.out.println(this.getAllCities());
            return zkClient.getChildren(MEMBER);
        }
        else {
            return zkClient.getChildren(MEMBER + "/" + city + "/" + function);
        }
    }

    private String getLeaderNode(String city, String function) throws Errors.NoServerForCity, Errors.MoreThenOneLeaderForTheCity {
        if (!zkClient.exists(LEADER + "/" + city) || !zkClient.exists(LEADER + "/" + city + "/" + function)) {
            throw new Errors.NoServerForCity();
        }
        List<String> leaders = zkClient.getChildren(LEADER + "/" + city + "/" + function);
        if (leaders.size() > 1){
            throw new Errors.MoreThenOneLeaderForTheCity();
        }
        if (leaders.size() == 0)
        {
            return "";
        }else{
            String leader = leaders.get(0);
            if (!zkClient.exists(MEMBER + "/" + city + "/" + function + "/" + leader))
            {
                zkClient.delete(LEADER + "/" + city + "/" + function + "/" + leader);
                return "";
            }else{
                return leader;
            }
        }
    }

    @Override
    public void createParentNode(String city, String function) {
        if(city != null){
            city = "/" + city;
        }
        else{
            city = "";
        }
        if (!zkClient.exists(MEMBER + city)) {
            zkClient.create(MEMBER + city, "all live z-nodes", CreateMode.PERSISTENT);
        }
        if (!zkClient.exists(LEADER + city)){
            zkClient.create(LEADER + city, "all leaders", CreateMode.PERSISTENT);
        }
        if (!city.equals("")){
            if (!zkClient.exists(MEMBER + city + "/" + function)){
                zkClient.create(MEMBER + city + "/" + function, "member app for " + function + "nodes", CreateMode.PERSISTENT);
            }
            if (!zkClient.exists(LEADER + city + "/" + function)){
                zkClient.create(LEADER + city + "/" + function, "leader app for " + function + "nodes", CreateMode.PERSISTENT);
            }
        }
    }

    public String makeAndReturnLeaderForCity(String city, String function) throws Errors.MoreThenOneLeaderForTheCity, Errors.NoServerForCity {
        String leader = this.getLeaderNode(city, function);
        if (!leader.equals("")){
            return leader;
        }else{
            List<String> replication_servers = null;
            try {
                replication_servers = this.getLiveNodes(city, function);
            } catch (ZkNoNodeException e) {
                // cant happen!
                e.printStackTrace();
            }
            if (replication_servers.size() == 0){
                throw new Errors.NoServerForCity();
            }else{
                String new_leader_details = replication_servers.get(replication_servers.size() - 1);
                String leaderNode = LEADER + "/" + city + "/" + function + "/" + new_leader_details;
                zkClient.create(leaderNode, new_leader_details, CreateMode.PERSISTENT);
                return new_leader_details;
            }
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
