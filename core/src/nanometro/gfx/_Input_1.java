package nanometro.gfx;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.QueryCallback;
import nanometro.GameScreen;

import static nanometro.GameScreen.camera;
import static nanometro.GameScreen.world;

import java.util.ArrayList;
import java.util.List;

public class _Input_1 implements InputProcessor {

    public _Input_1(Body mouseBox) {
        super();
        this.mouseBox = mouseBox;
    }

    private Section selectedSection = null;
    private Location endLocation = null;

    private Tip head = null;
    private Tip tail = null;

    private boolean isAddingTail = false;
    private boolean isAddingMiddle = false;
    private boolean isAddingHead = false;
    private Line selectedLine = null;
    private SectionPreview tmpSectionPreview = null;

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
                this.selectedLine = t.line;
                if (t.station == t.line.stationList.get(0)) {
                    // head tip
                    this.isAddingHead = true;
                    head = t;
                    break;
                } else {
                    this.isAddingTail = true;
                    tail = t;
                    break;
                }
            } else if (f.getBody().getUserData() instanceof Sensor) {
                Sensor o = (Sensor) f.getBody().getUserData();
                this.selectedLine = o.section.line;
                this.selectedSection = o.section;
                this.isAddingMiddle = true;
                break;
            } else if (f.getBody().getUserData() instanceof Location){
                Location o = (Location) f.getBody().getUserData();


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
                        if (l.hasSection(this.selectedSection)) {
                            l.addMiddle(this.endLocation, this.selectedSection);
                        }
                    }
                    break;
                }

            }
        }
        this.isAddingTail = this.isAddingMiddle = this.isAddingHead = false;
        // clear all previews
        if (this.selectedLine != null) {
            while (!this.selectedLine.sectionPreviewList.isEmpty()) {
                this.selectedLine.removeLastPreview();
            }
        }
        this.selectedLine = null;
        // recover section colour
        if (this.selectedSection != null) {
            this.selectedSection.unfade();
            this.selectedSection = null;
        }
        return true;
    }

    private void clear() {
        this.head = null;
        this.tail = null;
    }

    public boolean touchDragged (int x, int y, int pointer) {
        if (this.selectedLine != null)  {
            // clean up
            Line l = this.selectedLine;
            if (this.isAddingHead || this.isAddingTail) {
                l.removeLastPreview();
            } else if (this.isAddingMiddle) {
                l.removeLastPreview();
                l.removeLastPreview();
            }
        }
        Vector3 mousePosition = new Vector3(x, y, 0);
        camera.unproject(mousePosition);

        if (this.isAddingTail) {
            this.selectedLine.addPreviewTail(new Vector2(mousePosition.x, mousePosition.y));
        } else if (this.isAddingHead) {
            this.selectedLine.addPreviewHead(new Vector2(mousePosition.x, mousePosition.y));
        } else if (this.isAddingMiddle) {
//            System.out.println(this.selectedSection);
            this.selectedLine.addPreviewMiddle(new Vector2(mousePosition.x, mousePosition.y), this.selectedSection);
        }
        return true;

    }

    public boolean mouseMoved (int x, int y) {

        return false;
    }

    public boolean scrolled (float amountX, float amountY) {
        return false;
    }


}
