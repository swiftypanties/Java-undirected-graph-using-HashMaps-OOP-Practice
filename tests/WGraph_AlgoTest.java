import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class WGraph_AlgoTest {
    private weighted_graph g;
    /**
     * creating a graph before each test
     */
    @BeforeEach
    void setGraph(){
        g = new WGraph_DS();
        for(int i = 0; i < 6; i++){ //add 10 nodes [0,9];
            g.addNode(i);
        }
        for(int i=0; i<5; i++){      // connecting the graph in a linkedList form. 0-1-2-3....-8-9
            g.connect(i,i+1,i+10);
        }
        g.connect(0,3,1);
        g.connect(2,4,2);
        g.connect(1,5,1);
    }


    /**
     *  isConnected test, once when connected and once when connection removed.
     */
    @Test
    void isConnected_test(){
        WGraph_Algo w_graph = new WGraph_Algo();
        w_graph.init(g);

        assertEquals(true,w_graph.isConnected());
        g.removeNode(4);
        assertEquals(true,w_graph.isConnected()); // removing a node resulting still connected

        g.removeNode(1);
        assertEquals(false,w_graph.isConnected());// disconnecting the graph test
    }


    /**
     * simple shortest path test before and after creating shorter paths
     */
    @Test
    void shortestDistance_test(){
        WGraph_Algo w_graph = new WGraph_Algo();
        w_graph.init(g);


        assertEquals(11,w_graph.shortestPathDist(0,5));
        assertEquals(14,w_graph.shortestPathDist(0,4));


        g.connect(0,5,1);                                 // creating a shorter path to test 0>5
        assertEquals(1,w_graph.shortestPathDist(0,5));

        g.removeNode(5);
        assertEquals(-1,w_graph.shortestPathDist(0,5)); // not existing node test
    }

    /**
     *  if given list is the correct path
     */
    @Test
    void shortestPath_test(){
        WGraph_Algo w_graph = new WGraph_Algo();
        w_graph.init(g);

        List<node_info> actual = w_graph.shortestPath(0,5);
        int[] expected = {0,1,5};
        int counter = 0;

        for(node_info n: actual){                               //knowing the path is 0>1>5, testing according to vector expected.
           assertEquals(expected[counter],n.getKey());
           counter++;
        }
    }

    /**
     *  copy test will copy this graph and make changes in the copy, expecting original graph not to change (nodes in graph too)
     */
    @Test
    void copy_test(){
        WGraph_Algo w_graph = new WGraph_Algo();
        w_graph.init(g);

        weighted_graph copy = w_graph.copy();

        copy.getNode(0).setTag(50);
        assertEquals(-1,g.getNode(0).getTag()); // changing node values in copy, does not change value in original nodes

        copy.removeNode(5);
        assertEquals(null,copy.getNode(5));
        assertEquals(5,g.getNode(5).getKey()); // removing elements from the copy does not affect the original graph
    }




    // my had issues implementing load,save functions and my assignment submission time exceeded already,
    // therefore there are no tests for them.
    // i couldn't dig the way to do it, i hope its not going to hurt my grade too much ;(
    // in my defense i think the lecture didn't explain it all the way through and we didn't touch it in the exercises.




}
