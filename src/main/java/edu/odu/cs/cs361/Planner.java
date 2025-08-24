package edu.odu.cs.cs361;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Scanner;


public class Planner {
    
    CityMap city;
    int start;
    int stop;
    int maxCameras;

    public Planner(Reader in)  throws IOException {
        city = new CityMap();
        Scanner input = new Scanner(new BufferedReader(in));
        if (!input.hasNextInt()) {
            try {
                input.close();
            } catch (Exception e) {}
            throw new IOException("Could not read initial number from input.");
        }
        start = input.nextInt();
        stop = input.nextInt();
        maxCameras = input.nextInt();

        while (input.hasNextInt()) {
            int src = input.nextInt();
            int dest = input.nextInt();
            int time = input.nextInt();
            int cameras = input.nextInt();
            city.addRoad(src, dest, time, cameras);
        }
        input.close();
    }



    
    
    public void solve() {

    int d = city.findFastestRoute(start, stop, maxCameras);
    
      if (d >= 0) {
              System.out.println("Fastest time from intersection " + start
                   + " to intersection " + stop + " is "
                   + d);
            } else {
              System.out.println("There is no way to travel from " + start
                   + " to " + stop);
            }
    
    }
    
    
    public static void main(String[] args) throws IOException {
        Reader input;
        if (args.length == 1) {
            input = new FileReader(args[0]);
        } else {
            input = new InputStreamReader(System.in);
        }
        new Planner(input).solve();
    }
}
