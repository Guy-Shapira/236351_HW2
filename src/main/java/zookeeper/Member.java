package zookeeper;

import lombok.extern.slf4j.Slf4j;
import org.I0Itec.zkclient.IZkChildListener;

import java.util.List;

@Slf4j
public class Member implements IZkChildListener {

    @Override
    public void handleChildChange(String parentName, List<String> currentChildren) throws Exception {
        Cluster.getClusterInfo().getAllNodes().clear();
        Cluster.getClusterInfo().getAllNodes().clear();
        Cluster.getClusterInfo().getAllNodes().addAll(currentChildren);
    }
}
