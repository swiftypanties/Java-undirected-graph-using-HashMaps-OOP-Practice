
*********************** UNDIRECTED WEIGHTED GRAPH REPRESENTATION **************************

	Including 3 interfaces and 2 main classes and one inner class:


	DATA STRACTURE: the main data structures used in the graph are HashMaps for quick access to the nodes
			and the values with them. 
			I chose to represent edges and neighbors with a hashmap(b) inside a hashmap(a),
			so that hashmap(a) contains the nodes id in the keys section while the values are hashmap(b).
			hashmap(b) keys are neighbor nodes id and edge as value.

			NOTE: i did represent neighbors in a different list as well to keep track with easier access.
			
			
			NodeInfo as an inner class represent a vertex with certain values.		

	

	   OPERATIONS:  WGraph_Algo is the main operations class that will init a graph to operate on.
			methods such as Connection, Finding the shortest path according to edges weight, and returning
			a list of the path, are based on Disjkstra's algorithm and BFS with my own implementation.


			
