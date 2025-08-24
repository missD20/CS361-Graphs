package edu.odu.cs.cs361;

import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

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
        return 0;
    }

}
