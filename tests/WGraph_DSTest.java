import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;


class WGraph_DSTest {
    private weighted_graph g;
    /**
     * creating a graph before each test
     */
    @BeforeEach
    void setGraph(){
        g = new WGraph_DS();
        for(int i = 0; i < 10; i++){ //add 10 nodes [0,9];
            g.addNode(i);
        }
    }
    /**
     * getNode test with a none existing node.
     */
    @Test
    void getNode_test(){
        node_info n = g.getNode(10);
        assertEquals(null,n);

    }

    /**
     * testing Basic connect and hasEdge/getEdge.
     */ // the test is connecting a graph in linked list shape from 0>1>2>3....>9>10
    @Test
    void connectTest(){
        for(int i=0; i<9; i++){
            g.connect(i,i+1,i+10);
            // testing the basic return edge function
            assertEquals(i+10,g.getEdge(i,i+1));
            assertEquals(-1,g.getEdge(15,3));      // none existing edge
        }
        for(int i =0; i<9;i++){
            assertEquals(true, g.hasEdge(i,i+1));  //  testing if edges are detected
        }
        assertEquals(false,g.hasEdge(9,10));      // with a none existing node


        g.connect(0,1,2);                 // testing if the weight of existing edge exchanges
        assertEquals(2,g.getEdge(0,1));
    }


    /**
     * testing Basic removeNode.
     */
    @Test
    void removeNodeTest_a(){
        g.removeNode(0);
        g.removeNode(1);
        g.removeNode(2);
        g.removeNode(3);
        g.removeNode(4);
        g.removeNode(10);
        assertEquals(5,g.nodeSize());
    }
    /**
     * testing RemoveNode functionality over the edges and neighbor lists
     *
     */
    @Test
    void removeNodeTest_b() {
        for (int i = 0; i < 9; i++) {                             // connecting vertexes 0>1>2>....8>9
            g.connect(i, i + 1, i + 10);
        }
        g.connect(9, 7, 25);                      //adding 3 more neighbors to 9, aka [5,7];
        g.connect(9, 6, 24);
        g.connect(9, 5, 23);
        for (int i = 0; i < 4; i++) {
            assertEquals(true, g.hasEdge(9, 5 + i));     // testing the hasEdge functionality both ways
            assertEquals(true,g.hasEdge(5+i,9));
        }
        assertEquals(12,g.edgeSize());                   // testing amount of edges before removal
        assertEquals(4,g.getV(9).size());         // testing neighbor list size before removal

        g.removeNode(9);
        assertEquals(8,g.edgeSize());                    // testing amount of edges after removal
        assertEquals(null,g.getV(9));             // testing neighbor list size after removal

        assertEquals(1,g.getV(8).size());         // 8 had neighbors {9,7} before removal, after removal {7}
    }

    /**
     * testing basic removeEdge functionality
     */
    @Test
    void removeEdge_testest(){
        for (int i = 0; i < 9; i++) {                             // connecting vertexes 0>1>2>....8>9
            g.connect(i, i + 1, i + 10);
        }
        g.connect(9, 7, 25);                      //adding 3 more neighbors to 9, aka [5,7];
        g.connect(9, 6, 24);
        g.connect(9, 5, 23);
        g.removeEdge(10,9);                                       // remove none existing edge
        assertEquals(false,g.hasEdge(10,9));


        g.removeEdge(9,7);
        assertEquals(false,g.hasEdge(9,7));
        g.removeNode(9);
        g.removeEdge(8,7);
        assertEquals(0,g.getV(8).size());
    }

    /**
     * get neighbor list test
     */
    @Test
    void getV_test(){
        for (int i = 0; i < 9; i++) {                             // connecting vertexes 0>1>2>....8>9
            g.connect(i, i + 1, i + 10);
        }
        Collection<node_info> neigh = g.getV(1);
        int t = 0;
        for(node_info n: neigh){                                 // testing if the neighbors are 0 and 2
            assertEquals(t,n.getKey());
            t=2;
        }
    }

}
