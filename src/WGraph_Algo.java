
import java.io.*;
import java.util.*;

public class WGraph_Algo  implements weighted_graph_algorithms, Serializable {
    ///////////////////////////////////////////////////////// variables
    private weighted_graph g;

    ///////////////////////////////////////////////////////// constructor
    public WGraph_Algo(){
        this.g = new WGraph_DS();
    }

    //////////////////////////////////////////////////////// My static method
    public static void tagReset(weighted_graph g){ //----------------> Resets the tag of each node in the graph to default(-1);
        for(node_info n : g.getV()) {
            n.setTag(-1);
        }
    }
    public  int nodeMinValue(HashMap<Integer,Double> m){          // given a map of Integer(node_id) and Double(distance from root) this function will return the node_id
        // with the lowest distance in o(v);
        double minimum= Double.MAX_VALUE;
        int min_node_id =-1;                                      // this number will represent the node_id to return

        for (Map.Entry<Integer, Double> entry : m.entrySet()) {   // iteration all over the map to find the min distance and set the min_node_id to the corresponding distance.
            Integer key = entry.getKey();
            double value = entry.getValue();
            if(value < minimum && g.getNode(key).getTag() == -1){ // if (tag == -1): means that node was not visited before.
                minimum = value;
                min_node_id = key;
            }
        }
        return min_node_id;
    }
    /**
     * Init the graph on which this set of algorithms operates on.
     *
     * @param g
     */
    @Override
    public void init(weighted_graph g) {
        this.g = g;
    }

    /**
     * Return the underlying graph of which this class works.
     *
     * @return
     */
    @Override
    public weighted_graph getGraph() {
        return this.g;
    }

    /**
     * Compute a deep copy of this weighted graph.
     *
     * @return
     */
    @Override
    public weighted_graph copy() {
        weighted_graph deepCopy = new WGraph_DS();                                      //need to add all the nodes and their relation, from this.graph to deepCopy.
        for(node_info n : g.getV()){
            deepCopy.addNode(n.getKey());
            deepCopy.getNode(n.getKey()).setInfo(n.getInfo());
            deepCopy.getNode(n.getKey()).setTag(n.getTag());
        }
        for(node_info n: g.getV()){                                                     // for each node n in graph g
            Collection<node_info> currNeighList = g.getV(n.getKey());                    // access to it's neighbor list
            for(node_info k: currNeighList){                                              // iterate over the neighbor list and connect node n with each member
                deepCopy.connect(n.getKey(),k.getKey(),g.getEdge(n.getKey(),k.getKey())); // inside the deepCopy graph.
            }
        }
        return deepCopy;
    }

    /**
     * Returns true if and only if (iff) there is a valid path from EVREY node to each
     * other node. NOTE: assume ubdirectional graph.
     *
     * @return
     */
    @Override
    public boolean isConnected() {
        if(g.getV().isEmpty() ) { return true;}
        int start = g.getV().iterator().next().getKey();         // Initialize a starting point ( picking a node from the graph)
        Queue<node_info> Q = new ArrayDeque<node_info>();        // Create a new Queue, the tag numbers:[-1,0,1] remarks [unvisited,visiting,visited]
        Q.add(g.getNode(start));                                 // and then adding it to the Queue
        while(!Q.isEmpty()){                                     // Traversing through the queue until its empty
            // while each poll becomes the node to be Looked at ( Tag him as 1)
            node_info curr_node = Q.poll();                      //
            curr_node.setTag(1);                                 //
            for(node_info n : g.getV(curr_node.getKey())){                // we now look at every neighbor of the polled node in forEach loop
                if(n.getTag() == -1){                            // and determine if its visited or not, enters the if statement if not.
                    n.setTag(0);                                 // we set the node's tag to 0(visiting) and then add him to the queue
                    Q.add(n);
                }
            }
        }
        Boolean ans = true;
        for(node_info n : g.getV()){                             // once we can assume that all the node's are visited
            if(n.getTag() != 1) {                                // we will run over the graph and determine if there is any node
                ans = false;                                     // that his tag remains -1. in case there is one, we can say it was disconnected
                break;
            }
        }
        tagReset(g);
        return ans;
    }


    /**
     * returns the length of the shortest path between src to dest
     * Note: if no such path --> returns -1
     *
     * @param src  - start node
     * @param dest - end (target) node
     * @return
     */
    @Override
    public double shortestPathDist(int src, int dest) {
        if(!g.getV().contains(g.getNode(src))|| !g.getV().contains(g.getNode(dest))){ return -1;} // if either is not inside graph
        weighted_graph_algorithms temp = new WGraph_Algo();
        temp.init(g);
        if(!temp.isConnected()) {return -1;}                                                     // if graph is not connected also return -1

        HashMap<Integer,Double> distList = new HashMap<Integer, Double>();                            //  Map contains all nodes and their distance
        HashMap<Integer,node_info> parentList = new HashMap<Integer, node_info>();                    //  Map to contain parents of nodes

        for(node_info n: g.getV()){                                                                   // insertion of node values in the maps o(v);
            if(n.getKey()!=src) { distList.put(n.getKey(), 2147000.0);}                               // if not src node, give infinity distance.
            else { distList.put(n.getKey(), 0.0);}                                                    // if its the src, value it 0 distance. reason - algorithm will pick node with the lowest distance 1st.

            parentList.put(n.getKey(),null);                                                          // add nodes and null parents
        }
        // once done initializing a start point by giving a value of 0 distance to src node
        // and create a parent list to track the previous nodes,
        // we can now start the dijkstra loop.

        for(int i=0; i<g.nodeSize()-1; i++){                                                         // we need to iterate o(v-1) times to check all nodes except src.
            node_info min_distance_node = g.getNode(nodeMinValue(distList));
            int curr_node = min_distance_node.getKey();                                              // node ID of min_distance_node

            min_distance_node.setTag(1);                                                             // set node as visited
            Collection<node_info> neighbors = g.getV(curr_node);

            for(node_info n: neighbors){                                                             // relax ( iteration over neighbors to update their  values )
                if(n.getTag() == -1 && g.hasEdge(n.getKey(),curr_node) && distList.get(curr_node)+g.getEdge(curr_node,n.getKey()) < distList.get(n.getKey())){
                    // this cancer line simply represent> if(node is not visited & exist an edge & the parent node weight + edge to neighbor weight < neighbor weigh value in distList)
                    distList.replace(n.getKey(), (distList.get(curr_node) + g.getEdge(curr_node,n.getKey())) ); // set the distance of neighbor node from parent inside distList
                    parentList.replace(n.getKey(), min_distance_node);
                }
            }
        }
        tagReset(g);
        return distList.get(dest);
    }

    /**
     * returns the the shortest path between src to dest - as an ordered List of nodes:
     * src--> n1-->n2-->...dest
     * see: https://en.wikipedia.org/wiki/Shortest_path_problem
     * Note if no such path --> returns null;
     *
     * @param src  - start node
     * @param dest - end (target) node
     * @return
     */
    @Override
    public List<node_info> shortestPath(int src, int dest) {
        if(!g.getV().contains(g.getNode(src))|| !g.getV().contains(g.getNode(dest))){ return null;}   // if either is not inside graph
        weighted_graph_algorithms temp = new WGraph_Algo();
        temp.init(g);
        if(!temp.isConnected()) {return null;}                                                        // if graph is not connected also return -1

        HashMap<Integer,Double> distList = new HashMap<Integer, Double>();                            //  Map contains all nodes and their distance
        HashMap<Integer,node_info> parentList = new HashMap<Integer, node_info>();                    //  Map to contain parents of nodes

        for(node_info n: g.getV()){                                                                   // insertion of node values in the maps o(v);
            if(n.getKey()!=src) { distList.put(n.getKey(), 2147000.0);}                               // if not src node, give infinity distance.
            else { distList.put(n.getKey(), 0.0);}                                                    // if its the src, value it 0 distance. reason - algorithm will pick node with the lowest distance 1st.

            parentList.put(n.getKey(),null);                                                          // add nodes and null parents
        }
        // once done initializing a start point by giving a value of 0 distance to src node
        // and create a parent list to track the previous nodes,
        // we can now start the dijkstra loop.

        for(int i=0; i<g.nodeSize()-1; i++){                                                         // we need to iterate o(v-1) times to check all nodes except src.
            node_info min_distance_node = g.getNode(nodeMinValue(distList));
            int curr_node = min_distance_node.getKey();                                              // node ID of min_distance_node

            min_distance_node.setTag(1);                                                             // set node as visited
            Collection<node_info> neighbors = g.getV(curr_node);

            for(node_info n: neighbors){                                                             // relax ( iteration over neighbors to update their  values )
                if(n.getTag() == -1 && g.hasEdge(n.getKey(),curr_node) && distList.get(curr_node)+g.getEdge(curr_node,n.getKey()) < distList.get(n.getKey())){
                    // this cancer line simply represent> if(node is not visited & exist an edge & the parent node weight + edge to neighbor weight < neighbor weigh value in distList)
                    distList.replace(n.getKey(), (distList.get(curr_node) + g.getEdge(curr_node,n.getKey())) ); // set the distance of neighbor node from parent inside distList
                    parentList.replace(n.getKey(), min_distance_node);
                }
            }
        }
        tagReset(g);

        //************************************************ this is the only difference from the shortestPathDist.
        //************************************************ here we take advantage of the Parents list created by the algorithm
        //************************************************ and we follow the tracks by simple iteration through the parents list.
        List<node_info> path = new ArrayList<node_info>();
        Stack<node_info> stack = new Stack<node_info>();

        node_info prev = g.getNode(dest);

        for(int i=0; i<parentList.size(); i++){
            stack.push(prev);
            prev = parentList.get(prev.getKey());
            if(prev.getKey() == src) {
                stack.push(prev);
                break;
            }
        }
        while(!stack.isEmpty()){
            path.add(stack.pop());
        }
        return path;
    }

    /**
     * Saves this weighted (undirected) graph to the given
     * file name
     *
     * @param file - the file name (may include a relative path).
     * @return true - iff the file was successfully saved
     */
    @Override
    public boolean save(String file) {                        // credit : Boaz code from the lecture.
        boolean ans = false;
        ObjectOutputStream oos;
        try{
            FileOutputStream fout = new FileOutputStream(file,ans);
            oos = new ObjectOutputStream(fout);
            oos.writeObject(this);
            ans = true;
        }
        catch(FileNotFoundException e){
            e.printStackTrace();
        }
        catch(IOException e){
            e.printStackTrace();
        }
        return ans;
    }

    /**
     * This method load a graph to this graph algorithm.
     * if the file was successfully loaded - the underlying graph
     * of this class will be changed (to the loaded one), in case the
     * graph was not loaded the original graph should remain "as is".
     *
     * @param file - file name
     * @return true - iff the graph was successfully loaded.
     */
    @Override
    public boolean load(String file) {
        try {
            FileInputStream streamIn = new FileInputStream(file);
            ObjectInputStream osi = new ObjectInputStream(streamIn);
            WGraph_Algo readCase = (WGraph_Algo) osi.readObject();


            this.g = new WGraph_DS();                     // resetting the graph g
            this.g = readCase.getGraph();                 // pointing the new graph g to the loaded file
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return false;
    }


    // my had issues implementing load,save functions and my assignment submission time exceeded already,
    // therefore there are no tests for them.
    // i couldn't dig the way to do it, i hope its not going to hurt my grade too much ;(
    // in my defense i think the lecture didn't explain it all the way through and we didn't touch it in the exercises.
}
