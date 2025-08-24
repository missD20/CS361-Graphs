package edu.odu.cs.cs361;


    import org.junit.jupiter.api.BeforeEach;
    import org.junit.jupiter.api.Test;
    import static org.junit.jupiter.api.Assertions.*;
    
    
    import static org.hamcrest.MatcherAssert.assertThat;
    import static org.hamcrest.Matchers.*;
    

public class TestCityMap {

@Test
public void testCityMapConstructor() {
    CityMap cityMap = new CityMap();
    assertThat (cityMap.findFastestRoute(0, 1, 0), is(-1));
    assertThat (cityMap.findFastestRoute(0, 0, 0), is(-1));
}

@Test
public void testOneRoad() {
    CityMap cityMap = new CityMap();
    cityMap.addRoad(0, 1, 5, 1);
    assertThat (cityMap.findFastestRoute(0, 1, 1), is(5));
    assertThat (cityMap.findFastestRoute(1, 0, 1), is(5));
    assertThat (cityMap.findFastestRoute(0, 1, 0), is(-1));
    assertThat (cityMap.findFastestRoute(0, 0, 0), is(0));
}

@Test
public void testDisconnected() {
    CityMap cityMap = new CityMap();
    cityMap.addRoad(0, 1, 5, 0);
    cityMap.addRoad(2, 3, 5, 0);

    assertThat (cityMap.findFastestRoute(0, 1, 0), is(5));
    assertThat (cityMap.findFastestRoute(0, 2, 0), is(-1));
    assertThat (cityMap.findFastestRoute(0, 3, 0), is(-1));

    assertThat (cityMap.findFastestRoute(1, 1, 0), is(0));
    assertThat (cityMap.findFastestRoute(1, 2, 0), is(-1));
    assertThat (cityMap.findFastestRoute(1, 3, 0), is(-1));

    assertThat (cityMap.findFastestRoute(2, 1, 0), is(-1));
    assertThat (cityMap.findFastestRoute(2, 2, 0), is(0));
    assertThat (cityMap.findFastestRoute(2, 3, 0), is(5));
}



@Test
public void testThreeRoads() {
    CityMap cityMap = new CityMap();
    cityMap.addRoad(0, 1, 5, 1);
    cityMap.addRoad(1, 2, 5, 1);
    cityMap.addRoad(0, 2, 12, 1);

    assertThat (cityMap.findFastestRoute(0, 2, 2), is(10));
    assertThat (cityMap.findFastestRoute(2, 0, 2), is(10));
    assertThat (cityMap.findFastestRoute(0, 2, 1), is(12));
    assertThat (cityMap.findFastestRoute(2, 0, 1), is(12));
    assertThat (cityMap.findFastestRoute(0, 2, 0), is(-1));
    assertThat (cityMap.findFastestRoute(2, 0, 0), is(-1));
}

}
