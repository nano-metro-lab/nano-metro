package nanometro.level;

import nanometro.gfx.Colour;
import nanometro.gfx.Location;
import nanometro.level.service.Level;
import nanometro.level.service.LevelBuilder;
import nanometro.level.service.LevelLoader;
import nanometro.GameScreen;

import java.util.List;

public class TestField implements Level {
    @Override
    public void load() {
        // colours set up
        Colour c1 = new Colour("#fcce05");
        Colour c2 = new Colour("#1c4094");
        Colour c3 = new Colour("#f03024");
        Colour c4 = new Colour("#e0e0e0");
        GameScreen.colourList = List.of(c1, c2, c3);

        LevelLoader levelLoader = new LevelBuilder()
                .setName("LevelLoader 1")
                .addLocation(28, 17, Location.LocationType.CIRCLE, 0)
                .addLocation(36, 8, Location.LocationType.TRIANGLE, 0)
                .addLocation(9, 7, Location.LocationType.SQUARE, 0)
                .addLocation(4, 35, Location.LocationType.CIRCLE, 20)
                .addLocation(12, 20, Location.LocationType.CIRCLE, 35)
                .addLocation(20, 20, Location.LocationType.CIRCLE, 42)
                .addLocation(9, 15, Location.LocationType.CIRCLE, 55)
                .addLocation(38, 35, Location.LocationType.TRIANGLE,70)
                .build();
        levelLoader.start();
        GameScreen.locationList = levelLoader.getLocations();
    }
}
