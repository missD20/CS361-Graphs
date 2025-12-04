package edu.odu.cs.cs361;

import java.util.*;

import com.google.common.graph.MutableValueGraph;
import com.google.common.graph.ValueGraphBuilder;

public class CityMap {

    public class RoadInfo {
        public int time;
        public int cameras;

        public RoadInfo(int time, int cameras) {
            this.time = time;
            this.cameras = cameras;
        }
    }

    private MutableValueGraph<Integer, RoadInfo> roadMap;

    /**
     * Construct a new City object with no roads and intersections.
     * 
     */
    public CityMap() {
        roadMap = ValueGraphBuilder.undirected().build();
    }

    /**
     * Add a new road connecting two intersections.
     * 
     * @param intersection1 Identifier of 1st intersection
     * @param intersection2 Identifier of 2nd intersection
     * @param time          How many seconds required to travel this road between
     *                      the two intersections
     * @param cameras       number of cameras along this road
     */
    public void addRoad(Integer intersection1, Integer intersection2, int time, int cameras) {
        //*** Your code here
        RoadInfo info = new RoadInfo(time, cameras);
        roadMap.putEdgeValue(intersection1, intersection2, info);
    }

    private static class State implements Comparable<State> {
        int u;
        int cameras;
        int time;

        public State(int u, int cameras, int time) {
            this.u = u;
            this.cameras = cameras;
            this.time = time;
        }

        
        public int compareTo(State other) {
            return Integer.compare(this.time, other.time);
        }
    }


    /**
     * Find the total time of the shortest path between two intersections that will
     * not break an axle.
     * 
     * @param from        The starting intersection of the traveler
     * @param to          The ending intersection of the traveler
     * @param cameraLimit the maximum number of cameras the traveler may pass
     * @return int The total time required to go from "from" to "to" while
     *         traversing no more
     *         than cameraLimit cameras. Return -1 if there is no acceptable path
     *         between
     *         the two intersections.
     */
    int findFastestRoute(Integer from, Integer to, int cameraLimit) {
        //*** Your code here

        int maxNodeIndex = 5001;
        int maxCamerasIndex = 201;


        int[][] minTime = new int[maxNodeIndex][maxCamerasIndex];


        for (int[] row : minTime) {
            Arrays.fill(row, Integer.MAX_VALUE);
        }


        PriorityQueue<State> pq = new PriorityQueue<>();


        minTime[from][0] = 0;
        pq.add(new State(from, 0, 0));

        while (!pq.isEmpty()) {
            State current = pq.poll();

            int u = current.u;
            int cams = current.cameras;
            int time = current.time;


            if (time > minTime[u][cams]) {
                continue;
            }


            Set<Integer> neighbors = roadMap.adjacentNodes(u);
            for (Integer v : neighbors) {
                RoadInfo edge = roadMap.edgeValueOrDefault(u, v, null);

                if (edge != null) {
                    int newTime = time + edge.time;
                    int newCams = cams + edge.cameras;


                    if (newCams <= cameraLimit) {

                        if (newTime < minTime[v][newCams]) {
                            minTime[v][newCams] = newTime;
                            pq.add(new State(v, newCams, newTime));
                        }
                    }
                }
            }
        }

       
        int resultTime = Integer.MAX_VALUE;
        for (int c = 0; c <= cameraLimit; c++) {
            if (minTime[to][c] < resultTime) {
                resultTime = minTime[to][c];
            }
        }

        return (resultTime == Integer.MAX_VALUE) ? -1 : resultTime;

    }

}
