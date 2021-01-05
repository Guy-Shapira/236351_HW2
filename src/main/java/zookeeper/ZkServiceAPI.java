package zookeeper;

import java.util.ArrayList;
import java.util.List;
import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.IZkStateListener;

public interface ZkServiceAPI {

    void addToLiveNodes(String nodeName, String data, String state);

    List<String> getLiveNodes(String state);

    void createAllParentNodes(String state);

    String getZNodeData(String path);

    void registerChildrenChangeWatcher(String path, IZkChildListener iZkChildListener);

}