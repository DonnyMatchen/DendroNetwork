package com.donny.dendronetwork.data.lnetwork;

import com.donny.dendronetwork.DendroNetwork;
import com.donny.dendronetwork.data.lnetwork.exceptions.LoopBackException;
import com.donny.dendronetwork.data.lnetwork.ltree.TraversalTree;
import com.donny.dendronetwork.data.lnetwork.ltree.TreeNode;
import com.donny.dendronetwork.instance.ProgramInstance;

import java.math.BigDecimal;
import java.util.HashMap;

public class LNetwork implements Comparable<LNetwork> {
    private final ProgramInstance CURRENT_INSTANCE;

    private final String NAME;
    private final HashMap<String, LNode> NODES;

    public LNetwork(String name, ProgramInstance instance) {
        if(name.contains(".")) {
            throw new IllegalArgumentException("Network names cannot have a period!");
        }
        NAME = name;
        NODES = new HashMap<>();
        instance.NETWORKS.put(name, this);
        CURRENT_INSTANCE = instance;
    }

    /*
     * HAS
     */

    public boolean containsNode(String nodeId) {
        if(nodeId.contains(".")) {
            nodeId = nodeId.split("\\.")[1];
        }
        return NODES.containsKey(nodeId);
    }

    public boolean hasConnection(String nodeA, String nodeB) {
        if(nodeA.contains(".")) {
            nodeA = nodeA.split("\\.")[1];
        }
        if(nodeB.contains(".")) {
            nodeB = nodeA.split("\\.")[1];
        }
        return getNode(nodeA).connectedTo(getNode(nodeB));
    }

    /*
     * ADD
     */

    public boolean addNode(String nodeId, String desc) {
        if(containsNode(nodeId)) {
            CURRENT_INSTANCE.LOG_HANDLER.error(getClass(), "There is already a node in this network with name: " + nodeId);
        } else {
            NODES.put(nodeId, new LNode(nodeId, desc, this, CURRENT_INSTANCE));
            return true;
        }
        return false;
    }

    public boolean addNode(String id) {
        return addNode(id, "");
    }

    public boolean addConnection(LNode nodeA, LNode nodeB, BigDecimal cost) {
        LConnection con = new LConnection(cost, this, nodeA, nodeB, CURRENT_INSTANCE);
        return nodeA.connect(con) && nodeB.connect(con);
    }

    public boolean addConnection(LNode nodeA, LNode nodeB) {
        return addConnection(nodeA, nodeB, BigDecimal.ONE);
    }

    public boolean addConnection(String nodeA, String nodeB, BigDecimal cost) {
        if(containsNode(nodeA) && containsNode(nodeB)) {
            return addConnection(getNode(nodeA), getNode(nodeB), cost);
        } else {
            return false;
        }
    }

    public boolean addConnection(String nodeA, String nodeB) {
        if(containsNode(nodeA) && containsNode(nodeB)) {
            return addConnection(getNode(nodeA), getNode(nodeB));
        } else {
            return false;
        }
    }

    /*
     * GET
     */

    public String getName() {
        return NAME;
    }

    public LNode getNode(String nodeId) {
        if(nodeId.contains(".")) {
            nodeId = nodeId.split("\\.")[1];
        }
        return NODES.get(nodeId);
    }

    public LConnection getConnection(String nodeA, String nodeB) {
        return getNode(nodeA).getConnection(getNode(nodeB));
    }

    /*
     * ROUTING
     */
    public void flushCaches() {
        NODES.values().forEach(LNode::flushCache);
    }

    public void rebuildRoutingTable(LNode node) {
        node.flushCache();
        TraversalTree<LNode> tree = new TraversalTree<>(node);
        addToTree(tree, tree.HEAD);
        for (LNode nd : node.getNetwork().NODES.values()) {
            try {
                LNode direction = tree.HEAD.getDirection(nd);
                node.addRoute(nd, direction);
            } catch (LoopBackException ex) {
                //this just ensures that the entry doesn't end up in its own routing table
            }
        }
    }

    private void addToTree(TraversalTree<LNode> tree, TreeNode<LNode> op) {
        LNode opNode = op.CONTENT;
        opNode.getConnections().forEach(connection -> {
            TreeNode<LNode> current = tree.add(connection.traverse(opNode), connection.getCost(), op);
            if (current != null) {
                addToTree(tree, current);
            }
        });
    }

    /*
     * INTERFACE METHODS
     */

    @Override
    public int compareTo(LNetwork network) {
        return NAME.compareTo(network.NAME);
    }
}
