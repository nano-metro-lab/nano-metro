package nanometro.level.loaders;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import nanometro.GameScreen;
import nanometro.gfx.Colour;
import nanometro.gfx.Location;
import nanometro.level.Level;
import nanometro.level.LevelLoader;
import nanometro.level.builder.LevelBuilder;

import java.util.List;

public class Cork implements LevelLoader {
  @Override
  public Level load() {
    // colours set up
    Colour c1 = new Colour("#fcce05");
    Colour c2 = new Colour("#1c4094");
    Colour c3 = new Colour("#f03024");
    Colour c4 = new Colour("#e0e0e0");
    GameScreen.colourList = List.of(c1, c2, c3);

    GameScreen.backgroundImageTexture = new Texture(Gdx.files.internal("./levels/cork/map.png"));

    Level level = new LevelBuilder()
      .setName("Cork")
      .setDescription("A little-known city")
      .addLocation(28, 21, Location.LocationType.CIRCLE, 0)
      .addLocation(36, 10, Location.LocationType.TRIANGLE, 1.5f)
      .addLocation(9, 7, Location.LocationType.SQUARE, 0.8f)
      .addLocation(4, 35, Location.LocationType.CIRCLE, 20)
      .addLocation(12, 23, Location.LocationType.CIRCLE, 35)
      .addLocation(20, 20, Location.LocationType.CIRCLE, 42)
      .addLocation(9, 15, Location.LocationType.CIRCLE, 55)
      .addLocation(38, 35, Location.LocationType.TRIANGLE, 70)
      .build();
    level.start();
    GameScreen.locationList = level.getLocations();
    return level;
  }
}
