package edu.odu.cs.cs361;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

import com.google.common.graph.EndpointPair;
import com.google.common.graph.Graph;

public class GraphUtils {

    /**
     * A topological sort of a directed graph is any listing of the vertices
     * in g such that v1 precedes v2 in the listing only if there exists no
     * path from v2 to v1.
     *
     * @param g the graph to be sorted
     * @return the ordered list of vertices if a sort is possible, or an
     *         empty list if no sort is possible (because the graph contains
     *         a cycle).
     */
    public static <Vertex> List<Vertex> topologicalSort(Graph<Vertex> g) {
        // Step 1: get the in-degrees of all vertices. Place vertices with
        // in-degree 0 into a queue.
        int nVertices = g.nodes().size();
        Map<Vertex, Integer> inDegree = new HashMap<>();
        List<Vertex> sorted = new ArrayList<>();

        Queue<Vertex> q = new LinkedList<>();
        for (Vertex v : g.nodes()) {
            int incoming = g.inDegree(v);
            inDegree.put(v, incoming);
            if (incoming == 0)
                q.add(v);
        }

        // Step 2. Take vertices from the q, one at a time, and add to sorted.
        // As we do, pretend that we have deleted these vertices from the graph,
        // decreasing the in-degree of all adjacent nodes. If any nodes attain an
        // in-degree of 0 because of this, add them to the queue.
        while (!q.isEmpty()) {
            Vertex v = q.poll();

            sorted.add(v);

            for (Vertex w : g.successors(v)) {
                int reducedDegree = inDegree.get(w) - 1;
                inDegree.put(w, reducedDegree);
                if (reducedDegree == 0)
                    q.add(w);
            }
        }

        // Step 3: Did we finish the entire graph?
        if (sorted.size() != nVertices) {
            sorted.clear();
        }
        return sorted;
    }

    /**
     * Find the shortest path through g from start to finish.
     *
     * @param g      the graph
     * @param start  the vertex from which we start our path
     * @param finish the ending vertex for our path
     * @return path the output list of vertices, in the order defining the path
     *         from start to finish. This is empty if no such path exists.
     */
    public static <Vertex> List<Vertex> findShortestPath(
            Graph<Vertex> g, Vertex start, Vertex finish) {
        int nVertices = g.nodes().size();
        // Initialize the distance map and the queue
        Map<Vertex, Vertex> cameFrom = new HashMap<>();
        Map<Vertex, Integer> dist = new HashMap<>();

        dist.put(start, 0);
        dist.put(finish, nVertices + 1);

        Queue<Vertex> q = new LinkedList<>();
        q.add(start);

        // From each vertex in queue, update distances of adjacent vertices
        while (!q.isEmpty() && (dist.get(finish) > nVertices)) {
            Vertex v = q.poll();
            int d = dist.get(v);

            for (Vertex w : g.successors(v)) {
                if (dist.getOrDefault(w, nVertices + 1) > d + 1) {
                    dist.put(w, d + 1);
                    q.add(w);
                    cameFrom.put(w, v);
                }
            }
        }
        // Extract path
        List<Vertex> path = new LinkedList<>();
        Vertex v = finish;
        if (dist.get(v) <= nVertices) {
            while (!v.equals(start)) {
                path.add(0, v);
                v = cameFrom.get(v);
            }
            path.add(0, start);
        }
        return path;

    }

    private static class QueueEntry<Vertex> implements Comparable<QueueEntry<Vertex>> {
        public int d;
        public Vertex v;

        public QueueEntry(int d0, Vertex v0) {
            d = d0;
            v = v0;
        }

        @Override
        public int compareTo(QueueEntry<Vertex> e) {
            return d - e.d;
        }

        public String toString() {
            return "" + d + ':' + v;
        }
    }

    /**
     * Find a path through graph g from start to finish that has the smallest
     * possible sum of edge weight.
     *
     * @param g      the graph
     * @param start  the beginning of the path
     * @param finish the end of the path
     * @param weight a map associating an integer weight with each edge in g
     * @return the minimum-total-cost path from start to finish, or an empty
     *         vector if no path from start to finish exists.
     */
    public static <Vertex> List<Vertex> findWeightedShortestPath(
            Graph<Vertex> g, Vertex start, Vertex finish,
            Map<EndpointPair<Vertex>, Integer> weight) {

        // Initialize the distance map and the priority queue
        Map<Vertex, Vertex> cameFrom = new HashMap<>();
        Map<Vertex, Integer> dist = new HashMap<>();

        dist.put(start, 0);

        PriorityQueue<QueueEntry<Vertex>> pq = new PriorityQueue<>();
        pq.add(new QueueEntry<Vertex>(0, start));

        // Find the shortest path
        while (!pq.isEmpty()) {
            QueueEntry<Vertex> top = pq.poll();
            Vertex v = top.v;
            if (v.equals(finish))
                break; // exit when we reach the finish vertex
            int d = dist.get(v);
            if (top.d == d) {
                for (Vertex w : g.successors(v)) {
                    int wDist = d + weight.get(EndpointPair.ordered(v, w));
                    if (dist.getOrDefault(w, Integer.MAX_VALUE) > wDist) {
                        dist.put(w, wDist);
                        pq.add(new QueueEntry<Vertex>(wDist, w));
                        cameFrom.put(w, v);
                    }
                }
            }
        }

        // Extract path
        List<Vertex> path = new LinkedList<>();
        Vertex v = finish;
        if (dist.get(v) != Integer.MAX_VALUE) {
            while (!v.equals(start)) {
                path.add(0, v);
                v = cameFrom.get(v);
            }
            path.add(0, start);
        }
        return path;
    }

    /**
     * Find a minimum spanning tree within g rooted at start.
     *
     * @param g      the graph
     * @param weight a map associating an integer weight with each edge in g
     * @return a set of edges comprising a minimum spanning tree.
     */
    static public <Vertex> Set<EndpointPair<Vertex>> findMinSpanTree(Graph<Vertex> g,
            Map<EndpointPair<Vertex>, Integer> weight) { // Prim's Algorithm

        // Initialize the distance map and the priority queue
        Map<Vertex, Vertex> cameFrom = new HashMap<>();
        Map<Vertex, Integer> dist = new HashMap<>();

        Vertex anyVertex = g.nodes().iterator().next();
        dist.put(anyVertex, 0);

        PriorityQueue<QueueEntry<Vertex>> pq = new PriorityQueue<>();
        pq.add(new QueueEntry<Vertex>(0, anyVertex));

        // Find the shortest path
        while (!pq.isEmpty()) {
            QueueEntry<Vertex> top = pq.poll();
            Vertex v = top.v;
            int d = dist.get(v);
            dist.put(v, 0);
            if (top.d == d) {
                for (Vertex w : g.successors(v)) {
                    EndpointPair<Vertex> e = EndpointPair.unordered(v, w);
                    int we = weight.get(e);
                    if (dist.getOrDefault(w, Integer.MAX_VALUE) > we) {
                        int newDist = we;
                        dist.put(w, newDist);
                        pq.add(new QueueEntry<Vertex>(newDist, w));
                        cameFrom.put(w, v);
                    }
                }
            }
        }

        // Extract spanning tree
        Set<EndpointPair<Vertex>> spanTree = new HashSet<>();
        for (Vertex w : g.nodes()) {
            Vertex v = cameFrom.get(w);
            if (v != null) {
                EndpointPair<Vertex> e = EndpointPair.unordered(v, w);
                spanTree.add(e);
            }
        }
        return spanTree;
    }

}
