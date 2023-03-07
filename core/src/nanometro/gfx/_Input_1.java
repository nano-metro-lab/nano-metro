package nanometro.gfx;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.QueryCallback;

import java.util.ArrayList;
import java.util.List;

import static nanometro.GameScreen.*;

public class _Input_1 implements InputProcessor {

//    public _Input_1(Body mouseBox) {
//        super();
//    }
    private boolean isAddingTail = false;
    private boolean isAddingMiddle = false;
    private boolean isAddingHead = false;
    private boolean isMovingMap = false;
    private boolean isAddingNewLine = false;

    // Add Tail
    private Line ATLine = null;
    private Location ATLocation = null;

    // Add Head
    private Line AHLine = null;
    private Location AHLocation = null;

    // Add Middle
    private Section AMSection = null;
    private Location AMLocation = null;

    // Add New Line
    private SectionPreview NLSectionPreview;
    private Colour NLColour;
    private Location NLStart;
    private Vector2 NLStartPlatform;
    private Location NLEnd;

    // Move Map
    private Vector2 MMv;

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
        if (fixtureList.isEmpty()) {
            this.isMovingMap = true;
        } else {
            for (Fixture f : fixtureList) {
                if (f.getBody().getUserData() instanceof Tip) {
                    Tip t = (Tip) f.getBody().getUserData();
                    if (t.station == t.line.stationList.get(0)) {
                        this.isAddingHead = true;
                        this.AHLine = t.line;
                        break;
                    } else {
                        this.isAddingTail = true;
                        this.ATLine = t.line;
                        break;
                    }
                } else if (f.getBody().getUserData() instanceof Sensor) {
                    this.isAddingMiddle = true;
                    Sensor o = (Sensor) f.getBody().getUserData();
                    this.AMSection = o.section;
                    break;
                } else if (f.getBody().getUserData() instanceof Location){
                    this.isAddingNewLine = true;
                    Location o = (Location) f.getBody().getUserData();
                    NLStart = o;
                    NLStartPlatform = o.requestPlatform();
                    o.releasePlatform(NLStartPlatform);
                    NLColour = Colour.requestColour();
                }
            }
        }
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
        if (fixtureList.isEmpty()) { // case if released on nowhere
            Vector2 MMoffset = MMv;
            MMv = null;
            cleanUp();
        } else {
            for (Fixture f : fixtureList) {
                if (f.getBody().getUserData() instanceof Location) {
                    Location o = (Location) f.getBody().getUserData();
                    if (this.isAddingNewLine) {
//                    this.NLStart.releasePlatform(); Todo
                        Colour.releaseColour(NLColour);
                        this.NLEnd = o;
                        if (this.NLEnd != this.NLStart) {
                            lineList.add(new Line(NLStart, NLEnd));
                        }
                        this.isAddingNewLine = false;
                        break;

                    } else if (this.isAddingTail) {
                        ATLocation = o;
                        if (ATLine.stationList.get(ATLine.stationList.size()-1).location != ATLocation) {
                            ATLine.addTail(ATLocation);
                        }
                        this.isAddingTail = false;
                        break;
                    } else if (this.isAddingHead) {
                        AHLocation = o;
                        if (AHLine.stationList.get(0).location != AHLocation) {
                            AHLine.addHead(AHLocation);
                        }
                        this.isAddingHead = false;
                        break;
                    } else if (this.isAddingMiddle) {
                        AMLocation = o;
                        if (!AMSection.line.getLocationList().contains(AMLocation)) {
                            this.AMSection.line.pendingActionList.add(new Action(Action.ActionType.ADD_MIDDLE,
                                    AMLocation, AMSection));
                        }
                        this.isAddingMiddle = false;
                        break;
                    }
                }
            }
            cleanUp();
        }
        return true;
    }

    private void cleanUp () {
        if (this.isAddingNewLine) {
            Colour.releaseColour(NLColour);
            this.NLSectionPreview = null;
            this.isAddingNewLine = false;
        }
        if (isAddingMiddle) {
            AMSection.unfade();
        }
        for (Line l: lineList) {
            l.sectionPreviewList.clear();
        }
        this.NLSectionPreview = null;
        this.isMovingMap = false;
        this.isAddingTail = this.isAddingHead = this.isAddingMiddle = false;
    }

    public boolean touchDragged (int x, int y, int pointer) {
        if (this.isAddingHead) {
            AHLine.sectionPreviewList.clear();
        } else if (this.isAddingMiddle) {
            AMSection.line.sectionPreviewList.clear();
        } else if (this.isAddingTail) {
            ATLine.sectionPreviewList.clear();
        }

        Vector3 mousePosition = new Vector3(x, y, 0);
        camera.unproject(mousePosition);

        if (this.isAddingTail) {
            ATLine.addPreviewTail(new Vector2(mousePosition.x, mousePosition.y));
        } else if (this.isAddingHead) {
            AHLine.addPreviewHead(new Vector2(mousePosition.x, mousePosition.y));
        } else if (this.isAddingMiddle) {
            AMSection.line.addPreviewMiddle(new Vector2(mousePosition.x, mousePosition.y), this.AMSection);
        } else if (this.isAddingNewLine) {
            NLSectionPreview = new SectionPreview(NLStartPlatform, new Vector2(mousePosition.x, mousePosition.y), NLColour);
        } else if (this.isMovingMap) {
            if (MMv == null) {
                MMv = new Vector2(x, y);
                return true;
            }
            MMv.sub(x, y);
            camera.translate((float) (MMv.x * 0.07), (float) (MMv.y * -0.07));
            // case moving map
            MMv = new Vector2(x, y);
            return true;
        }
        return true;

    }

    public void draw() {
        if (this.isAddingNewLine && this.NLSectionPreview != null) {
            NLSectionPreview.draw();
        }
    }

    public boolean mouseMoved (int x, int y) {
        return false;
    }

    public boolean scrolled (float amountX, float amountY) {
        zoomOffset += amountY * 0.06f;
        return false;
    }


}
