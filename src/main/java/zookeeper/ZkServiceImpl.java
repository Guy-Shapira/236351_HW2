package zookeeper;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.ZkClient;
import org.apache.zookeeper.CreateMode;

import Utils.Errors;

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
    public void addToLiveNodes(String nodeName, String data, String city) {
        city = "/" + city;
        if(!zkClient.exists(MEMBER + city)){
            zkClient.create(MEMBER + city, "all live z-nodes", CreateMode.PERSISTENT);
        }
        if (!zkClient.exists(LEADER + city)) {
            zkClient.create(LEADER + city, "all leaders", CreateMode.PERSISTENT);
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

    private String getLeaderNode(String city) throws Errors.NoServerForCity, Errors.MoreThenOneLeaderForTheCity {
        if (!zkClient.exists(LEADER + "/" + city)) {
            throw new Errors.NoServerForCity();
        }
        List<String> leaders = zkClient.getChildren(LEADER + "/" + city);
        if (leaders.size() > 1){
            throw new Errors.MoreThenOneLeaderForTheCity();
        }
        if (leaders.size() == 0)
        {
            return "";
        }else{
            String leader = leaders.get(0);
            if (!zkClient.exists(MEMBER + "/" + city + "/" + leader))
            {
                zkClient.delete(LEADER + "/" + city + "/" + leader);
                return "";
            }else{
                return leader;
            }

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
        if (!zkClient.exists(LEADER + city)){
            zkClient.create(LEADER + city, "all leaders", CreateMode.PERSISTENT);
        }
    }

    public String makeAndReturnLeaderForCity(String city) throws Errors.MoreThenOneLeaderForTheCity, Errors.NoServerForCity {
        String leader = this.getLeaderNode(city);
        if (!leader.equals("")){
            System.out.println(leader);
            return leader;
        }else{
            List<String> replication_servers = this.getLiveNodes(city);
            if (replication_servers.size() == 0){
                throw new Errors.NoServerForCity();
            }else{
                String new_leader_details = replication_servers.get(0);
                String leaderNode = LEADER + "/" + city + "/" + new_leader_details;
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
