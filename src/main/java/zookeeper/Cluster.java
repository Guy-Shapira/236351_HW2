package zookeeper;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Cluster {

    private static Cluster cluster = new Cluster();

    public static Cluster getClusterInfo() {
        return cluster;
    }

    private List<String> allNodes = new ArrayList<>();

    public List<String> getAllNodes(){
        return this.allNodes;
    }
}