package nanometro.gfx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;

import java.util.*;

import static nanometro.GameScreen.*;


public class Train {
    public enum Direction {
        UP, DOWN
    }
    Body trainBody;
    List<Passenger> passengerList = new ArrayList<>();
    Line line;
    Section section;
    int stopSignal;
    Direction direction;
    float progress; // 0 - 1
    final float stdTimeLimit = 0.1f;
    float runTime = 0f;
    BitmapFont debugFont;
    Location thisLocation;
    Location nextLocation;
    // Used for gfx rotate degree
    Vector2 nextPosition, currentPosition;
    // Train shape control
    float trainShapeWidth = 2.9f;
    float trainShapeHeight = 1.7f;

    public void stopTrain() {
        stopSignal = 1;
    }

    public void startTrain() {
        stopSignal = 0;
    }


    public Train(Line l, Section s, float p) {
        this.setUpBody();
        this.line = l;
        this.section = s;
        this.stopSignal = 0;
        this.progress = p;
        this.direction = Direction.DOWN; // default go down

        // debug font
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Roboto.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 20;
        parameter.color = Color.BLACK;
        BitmapFont font = generator.generateFont(parameter); // font size 12 pixels
        generator.dispose(); // don't forget to dispose to avoid memory leaks!
        this.debugFont = font;
    }

    private void setUpBody() {
        BodyDef trainBodyDef = new BodyDef();
        trainBodyDef.type = BodyDef.BodyType.DynamicBody;
        this.trainBody = world.createBody(trainBodyDef);
        CircleShape trainShape = new CircleShape();
        trainShape.setRadius(2f);
        FixtureDef trainFixtureDef = new FixtureDef();
        trainFixtureDef.isSensor = true;
        trainFixtureDef.shape = trainShape;
        this.trainBody.createFixture(trainFixtureDef);
        trainShape.dispose();
        this.trainBody.setUserData(this);
//        this.trainBody.
    }

    public void drawDebug() {
        Vector3 p = new Vector3(this.trainBody.getWorldCenter().x, this.trainBody.getWorldCenter().y, 0);
//        camera.project(p);
        viewport.project(p);
        debugBatch.begin();
        debugFont.draw(debugBatch, passengerList.toString(), p.x,p.y);
        debugBatch.end();
    }

    public void draw() {
        shape.setProjectionMatrix(camera.combined);
        shape.begin(ShapeRenderer.ShapeType.Filled);
        shape.setColor(Color.valueOf(this.line.colourObj.subColour3));
        double degree = Math.atan2(
                nextPosition.y - currentPosition.y,
                nextPosition.x - currentPosition.x
        ) * 180.0d / Math.PI;
        // https://stackoverflow.com/questions/18333314/libgdx-degree-between-two-points-calculation
        shape.rect(this.currentPosition.x - 0.5f * trainShapeWidth,
                this.currentPosition.y - 0.5f * trainShapeHeight,
                0.5f * trainShapeWidth, 0.5f * trainShapeHeight,
                trainShapeWidth, trainShapeHeight,
                1, 1, (float)degree);
        shape.end();
        Vector2 qPosition = this.currentPosition.cpy().add(1.7f, 0.6f);
        float qGap = 1f;
        for (Passenger p : this.passengerList) {
            p.draw(qPosition);
            qPosition.add(qGap, 0);
        }

    }

    public void set() {

    }

    public void update() {
        List<Passenger> removeLst = new ArrayList<>();
//        System.out.println(passengerList);
//        System.out.println(thisLocation.passengerList);
        for (Passenger p : this.passengerList) {
            if (p.nextHop == thisLocation) {
                removeLst.add(p);
                if (p.getType() != thisLocation.type) {
                    thisLocation.passengerList.add(p);
                } else {
                    g6Sound.play();
                }
            }
        }
        for (Passenger p : removeLst) {
            passengerList.remove(p);
        }
        removeLst.removeAll(removeLst);

        Map<Location.LocationType, Location> nextHopsByType = new HashMap<>();
        Random random = new Random();

        for (Passenger p : thisLocation.passengerList) {
            if (nextHopsByType.containsKey(p.getType())) {
              Location nextHop = nextHopsByType.get(p.getType());
              if (nextHop != null) {
                this.passengerList.add(p);
                popSound.play();
                removeLst.add(p);
                p.nextHop = nextHop;
              }
            } else {
              List<Location> lst =  modelService.findDestinations(p.getType(), line, thisLocation, nextLocation);
//            System.out.println(lst);
              if (!lst.isEmpty()) {
                this.passengerList.add(p);
                popSound.play();
                removeLst.add(p);
                Location nextHop = lst.get(random.nextInt(lst.size()));
                p.nextHop = nextHop;
                nextHopsByType.put(p.getType(), nextHop);
              } else {
                nextHopsByType.put(p.getType(), null);
              }
            }
        }
        for (Passenger p : removeLst) {
            thisLocation.passengerList.remove(p);
        }

    }

    public void dumbController() {
//        Timer.schedule(new Timer.Task() {
//            @Override
//            public void run() {
//                startTrain();
//            }
//        }, 3);
//
//        stopTrain();

        runTime = 0f;
        if (line.getNextSection(section) == null && direction == Direction.DOWN) {
            thisLocation = section.lower.location;
            nextLocation = section.upper.location;
            section = section;
            direction = Direction.UP;
            progress = 0f;
        } else if (line.getPreviousSection(section) == null && direction == Direction.UP) {
            thisLocation = section.upper.location;
            nextLocation = section.lower.location;
            section = section;
            direction = Direction.DOWN;
            progress = 0f;
        } else {
            if (direction == Direction.DOWN) {
                thisLocation = section.lower.location;
                section = line.getNextSection(section);
                nextLocation = section.lower.location;
                progress = 0f;
            } else {
                thisLocation = section.upper.location;

                section = line.getPreviousSection(section);
                nextLocation = section.upper.location;
                progress = 0f;
            }
        }
    }

    public void run() {
        if (progress >= 1f) {
            if (stopSignal != 0) {
                // ============== train stop at station ==============
                Vector2 bodyPosition = trainBody.getWorldCenter();
                Vector2 positionDelta = null;
                if (direction == Direction.DOWN) {
                    positionDelta = section.lower.getPosition().cpy().sub(bodyPosition);
                } else {
                    positionDelta = section.upper.getPosition().cpy().sub(bodyPosition);
                }
                this.trainBody.setLinearVelocity(positionDelta.scl(10));
            } else {
                // ============== train go to next section ==============
                dumbController();
                update();
            }
        } else {
            float sectionTimeLimit = stdTimeLimit * section.path.approxLength();

            runTime += Gdx.graphics.getDeltaTime();
            progress = runTime / sectionTimeLimit;
            Vector2 bodyPosition = trainBody.getWorldCenter();
            this.currentPosition = bodyPosition;
            Vector2 targetPosition = new Vector2();
            if (this.direction == Direction.DOWN) {
                section.path.valueAt(targetPosition, progress);
            } else {
                section.path.valueAt(targetPosition, 1 - progress > 0 ? 1 - progress : 0.01f); // Todo quick fix, should investigate 0
//                System.out.println(1 - progress);

            }
            this.nextPosition = targetPosition;
            Vector2 positionDelta = (targetPosition.cpy().sub(bodyPosition));
            this.trainBody.setLinearVelocity(positionDelta.scl(10));

        }
    }
}
