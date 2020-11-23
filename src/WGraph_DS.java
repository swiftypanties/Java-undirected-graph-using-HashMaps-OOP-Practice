import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

public class WGraph_DS implements weighted_graph, Serializable {
    ////////////////////////////////////////////////////////// variables
    private HashMap<Integer, node_info> _nodes;
    private HashMap<Integer,Collection<node_info>> _neigh;
    private HashMap<Integer,HashMap<Integer,Double>> _edges;
    private int e_size;
    private int _mc;





    /////////////////////////////////////////////////////////// constructor
    public WGraph_DS(){
        _nodes = new HashMap<Integer,node_info>();
        _neigh = new HashMap<Integer,Collection<node_info>>();
        _edges = new HashMap<Integer,HashMap<Integer,Double>>();
        e_size = 0;
        _mc = 0;
    }

    ///////////////////////////////////////////////////////////// methods
    /**
     * return the node_info by the node_id,
     *
     * @param key - the node_id
     * @return the node_info by the node_id, null if none.
     */
    @Override
    public node_info getNode(int key) {
        if(_nodes.containsKey(key)){ return _nodes.get(key);}
        return null;
    }

    /**
     * return true iff (if and only if) there is an edge between node1 and node2
     * Note: this method should run in O(1) time.
     *
     * @param node1
     * @param node2
     * @return
     */
    @Override
    public boolean hasEdge(int node1, int node2) {
        if(_nodes.containsKey(node1) && _nodes.containsKey(node2)) {
            if (_edges.get(node1).containsKey(node2)&& _edges.get(node1).containsKey(node2)) {
                return true;
            }
        }
        return false;
    }

    /**
     * return the weight of the edge (node1, node2). In case
     * there is no such edge - should return -1
     * Note: this method should run in O(1) time.
     *
     * @param node1
     * @param node2
     * @return
     */
    @Override
    public double getEdge(int node1, int node2) {
        if(!hasEdge(node1, node2)) return -1;
        return(_edges.get(node1).get(node2));
    }

    /**
     * add a new node to the graph with the given key.
     * Note: this method should run in O(1) time.
     * Note2: if there is already a node with such a key -> no action should be performed.
     *
     * @param key
     */
    @Override
    public void addNode(int key) {                          // create a new node and put it in the nodes list
        if(!_nodes.containsKey(key)) {                      // also in the edges list and neighbor list
            node_info node = new NodeInfo(key);
            _nodes.put(key,node);
            _neigh.put(key, new HashSet<node_info>());
            _edges.put(key, new HashMap<Integer,Double>());
            _mc++;
        }
    }

    /**
     * Connect an edge between node1 and node2, with an edge with weight >=0.
     * Note: this method should run in O(1) time.
     * Note2: if the edge node1-node2 already exists - the method simply updates the weight of the edge.
     *
     * @param node1
     * @param node2
     * @param w
     */
    @Override
    public void connect(int node1, int node2, double w) {
        if(node1 == node2) return;
        if(_nodes.containsKey(node1) && _nodes.containsKey(node2)) {
            if (hasEdge(node1, node2)) {
                _edges.get(node1).replace(node2,w);
                _edges.get(node2).replace(node1,w);
                return;
            }
            _neigh.get(node1).add(getNode(node2));
            _neigh.get(node2).add(getNode(node1));


            _edges.get(node1).put(node2, w);
            _edges.get(node2).put(node1, w);
            e_size++;
            _mc++;
        }
    }

    /**
     * This method return a pointer (shallow copy) for a
     * Collection representing all the nodes in the graph.
     * Note: this method should run in O(1) tim
     *
     * @return Collection<node_data>
     */
    @Override
    public Collection<node_info> getV() {
        return _nodes.values();
    }

    /**
     * This method returns a Collection containing all the
     * nodes connected to node_id
     * Note: this method can run in O(k) time, k - being the degree of node_id.
     *
     * @param node_id
     * @return Collection<node_data>
     */
    @Override
    public Collection<node_info> getV(int node_id) {
        return _neigh.get(node_id);
    }

    /**
     * Delete the node (with the given ID) from the graph -
     * and removes all edges which starts or ends at this node.
     * This method should run in O(n), |V|=n, as all the edges should be removed.
     *
     * @param key
     * @return the data of the removed node (null if none).
     */
    @Override
    public node_info removeNode(int key) {
        node_info temp = getNode(key);
        if(_nodes.containsKey(key)){
            e_size -= _neigh.get(key).size();                 // assuming the node exist we reduce edges factor by the amount of neighbors.
            for(node_info n : _neigh.get(key)){               // iteration over the neighbor list of node key
                _edges.get(n.getKey()).remove(key);           // removal of key from each neighbor in _edges
                _neigh.get(n.getKey()).remove(getNode(key));  // removal of key from each neighbor in _neigh
            }
            _neigh.remove(key);
            _edges.remove(key);
            _nodes.remove(key);
            _mc++;
            return temp;
        }
        return null;
    }

    /**
     * Delete the edge from the graph,
     * Note: this method should run in O(1) time.
     *
     * @param node1
     * @param node2
     */
    @Override
    public void removeEdge(int node1, int node2) {
        if(!hasEdge(node1,node2)){ return;}
        _edges.get(node1).remove(node2);
        _edges.get(node2).remove(node1);
        _neigh.get(node1).remove(getNode(node2));
        _neigh.get(node2).remove(getNode(node1));
        e_size--;
    }

    /**
     * return the number of vertices (nodes) in the graph.
     * Note: this method should run in O(1) time.
     *
     * @return
     */
    @Override
    public int nodeSize() {
        return _nodes.size();
    }

    /**
     * return the number of edges (undirectional graph).
     * Note: this method should run in O(1) time.
     *
     * @return
     */
    @Override
    public int edgeSize() {
        return e_size;
    }

    /**
     * return the Mode Count - for testing changes in the graph.
     * Any change in the inner state of the graph should cause an increment in the ModeCount
     *
     * @return
     */
    @Override
    public int getMC() {
        return _mc;
    }
    //************************************************************ node_info class**************************************
    public class NodeInfo implements node_info{
        ///////////////////////////////////////////////////////// variables
        private int key = 0;
        private String metaData;
        private double tag;

        /////////////////////////////////////////////////////////constructors
        public NodeInfo(int key){
            this.key = key;
            this.metaData = "data";
            this.tag = -1;
        }
        public NodeInfo(node_info node){
            this.key = node.getKey();
            this. metaData = node.getInfo();
            this.tag = node.getTag();
        }
        /////////////////////////////////////////////////////////methods

        /**
         * Return the key (id) associated with this node.
         * Note: each node_data should have a unique key.
         *
         * @return
         */
        @Override
        public int getKey() {
            return key;
        }

        /**
         * return the remark (meta data) associated with this node.
         *
         * @return
         */
        @Override
        public String getInfo() {
            return metaData;
        }

        /**
         * Allows changing the remark (meta data) associated with this node.
         *
         * @param s
         */
        @Override
        public void setInfo(String s) {
            this.metaData = s;
        }

        /**
         * Temporal data (aka distance, color, or state)
         * which can be used be algorithms
         *
         * @return
         */
        @Override
        public double getTag() {
            return tag;
        }

        /**
         * Allow setting the "tag" value for temporal marking an node - common
         * practice for marking by algorithms.
         *
         * @param t - the new value of the tag
         */
        @Override
        public void setTag(double t) {
            this.tag = t;
        }
    }
}
