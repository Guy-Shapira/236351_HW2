package zookeeper;

import java.util.ArrayList;
import java.util.List;
import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.IZkStateListener;
import org.I0Itec.zkclient.exception.ZkNoNodeException;
import org.apache.zookeeper.KeeperException;

public interface ZkServiceAPI {

    void addToLiveNodes(String nodeName, String data, String state, String function);

    List<String> getLiveNodes(String state, String function) throws ZkNoNodeException;

    void createParentNode(String state, String function);

    String getZNodeData(String path);

    void registerChildrenChangeWatcher(String path, IZkChildListener iZkChildListener);

}