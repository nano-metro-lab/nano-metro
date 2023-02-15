package nanometro.gfx;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.QueryCallback;
import nanometro.GameScreen;

import static nanometro.GameScreen.camera;
import static nanometro.GameScreen.world;
import static nanometro.GameScreen.modelService;

import java.util.ArrayList;
import java.util.List;

public class _Input_1 implements InputProcessor {

    public _Input_1(Body mouseBox) {
        super();
        this.mouseBox = mouseBox;
    }

    private Section startSection = null;
    private Location endLocation = null;

    private Tip head = null;
    private Tip tail = null;


    public _Input_1() {

    }

    private Body mouseBox;


    public boolean keyDown (int keycode) {
        return false;
    }

    public boolean keyUp (int keycode) {
        return false;
    }

    public boolean keyTyped (char character) {
        return false;
    }

    public boolean touchDown (int x, int y, int pointer, int button) {
        // check mouse box collision
        Vector3 mousePosition = new Vector3(x, y, 0);
        camera.unproject(mousePosition);
        final List<Fixture> fixtureList = new ArrayList<>(5);
        world.QueryAABB(new QueryCallback() {
            @Override
            public boolean reportFixture(Fixture fixture) {
                fixtureList.add(fixture);
                return true;
            }
        }, mousePosition.x, mousePosition.y, mousePosition.x, mousePosition.y);
        for (Fixture f : fixtureList) {
            if (f.getBody().getUserData() instanceof Tip) {
                Tip t = (Tip) f.getBody().getUserData();
                if (t.station == t.line.stationList.get(0)) {
                    // head tip
                    head = t;
                    break;
                } else {
                    tail = t;
                    break;
                }
            } else if (f.getBody().getUserData() instanceof Sensor) {
                Sensor o = (Sensor) f.getBody().getUserData();
                this.startSection = o.section;
                break;
            }
        }
//        System.out.println(this.startSection);
//        System.out.println(this.endLocation);
        return true;
    }

    public boolean touchUp (int x, int y, int pointer, int button) {
        Vector3 mousePosition = new Vector3(x, y, 0);
        camera.unproject(mousePosition);
        final List<Fixture> fixtureList = new ArrayList<>(5);
        world.QueryAABB(new QueryCallback() {
            @Override
            public boolean reportFixture(Fixture fixture) {
                fixtureList.add(fixture);
                return true;
            }
        }, mousePosition.x, mousePosition.y, mousePosition.x, mousePosition.y);
        for (Fixture f : fixtureList) {
            if (f.getBody().getUserData() instanceof Location) {
                Location o = (Location) f.getBody().getUserData();
                this.endLocation = o;

                if (head != null) {
                    head.line.addHead(endLocation);
                    clear();
                    break;
                } else if (tail != null) {
                    tail.line.addTail(endLocation);
                    clear();
                    break;
                } else {
                    for (Line l : GameScreen.lineList) {
                        if (l.hasSection(this.startSection)) {
                            l.addMiddle(this.endLocation, this.startSection);
                        }
                    }
                    break;
                }

            }
        }
        return true;
    }

    private void clear() {
        this.head = null;
        this.tail = null;
    }

    public boolean touchDragged (int x, int y, int pointer) {
//        Vector3 mousePosition = new Vector3(x, y, 0);
//        Main.camera.unproject(mousePosition);
//
//        final List<Fixture> fixtureList = new ArrayList<>(10);
//        Main.world.QueryAABB(new QueryCallback() {
//            @Override
//            public boolean reportFixture(Fixture fixture) {
//                fixtureList.add(fixture);
//                return true;
//            }
//        }, mousePosition.x, mousePosition.y, mousePosition.x, mousePosition.y);
//
//        for (Fixture f : fixtureList) {
//            if (f.getBody().getUserData() instanceof Sensor) {
//                Sensor o = (Sensor) f.getBody().getUserData();
//                System.out.println(o.getSensorPosition());
//                break;
//            }
//        }
//
//        System.out.println("end");
        return false;
//        return false;
    }

    public boolean mouseMoved (int x, int y) {

        return false;
    }

    public boolean scrolled (float amountX, float amountY) {
        return false;
    }


}
