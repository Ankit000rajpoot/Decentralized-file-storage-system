package com.ankit.dfs;

import java.util.*;

public class NodeManager {

    private final Map<String, StorageNode> nodes = new LinkedHashMap<>();

    public void registerNode(StorageNode node) {
        nodes.put(node.getNodeId(), node);
    }

    public List<StorageNode> getNodes() {
        return new ArrayList<>(nodes.values());
    }

    public StorageNode getNode(String nodeId) {
        return nodes.get(nodeId);
    }

    public void printStatus() {
        System.out.println("\n=== Storage Node Status ===");

        for (StorageNode node : nodes.values()) {
            System.out.println(
                    node.getNodeId() +
                    " | Used: " + node.getUsedSpace() +
                    " bytes | Capacity: " + node.getCapacity() +
                    " bytes"
            );
        }
    }
}