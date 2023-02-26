package nanometro.gfx;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import static nanometro.GameScreen.modelService;

public class Line {
    public final List<Station> stationList;
    public final List<Section> sectionList;
    public final List<SectionPreview> sectionPreviewList;
    public Tip headTip, tailTip;
    public String colour;


    public List<Location> getLocationList() {
        List<Location> l = new ArrayList<>();
        for (Station s : stationList) {
            l.add(s.location);
        }
        return l;
    }

    public Line(Location a, Location b, String colour) {
        this.colour = colour;
        this.sectionList = new ArrayList<Section>(20);
        this.sectionPreviewList = new ArrayList<SectionPreview>(1);
        this.stationList = new ArrayList<Station>(21);
        this.stationList.add(new Station(this, a));
        this.stationList.add(new Station(this, b));
        this.sectionList.add(new Section(this, this.stationList.get(0), this.stationList.get(1)));
        //
        this.headTip = new Tip(this, this.stationList.get(0));
        this.tailTip = new Tip(this, this.stationList.get(1));
    }

    public Section getNextSection(Section s) {
        if (sectionList.indexOf(s) == sectionList.size() - 1) {
            return null;
        } else {
            return sectionList.get(sectionList.indexOf(s) + 1);
        }
    }
    public Section getPreviousSection(Section s) {
        if (sectionList.indexOf(s) == 0) {
            return null;
        } else {
            return sectionList.get(sectionList.indexOf(s) - 1);
        }
    }

    private void _update() {
        this.headTip = new Tip(this, this.stationList.get(0));
        this.tailTip = new Tip(this, this.stationList.get(this.stationList.size() - 1));
        modelService.updateLine(this, getLocationList());
    }

    public Section getSection(Location locationA, Location locationB) {
        for (Section s : sectionList) {
            if ((s.upper.location == locationA && s.lower.location == locationB) || (s.upper.location == locationB && s.lower.location == locationA)) {
                return s;
            }
        }
        return null;
    }

    public void draw(ShapeRenderer shape) {
//        Gdx.gl.glEnable(GL20.GL_BLEND);
//        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
//        Gdx.gl.glLineWidth(25);
//        if (stationList.size() < 2) return;
        for (Section s : this.sectionList) {
            s.draw(shape);
        }
        for (SectionPreview s : this.sectionPreviewList) {
            s.draw(shape);
        }
//        Gdx.gl.glDisable(GL20.GL_BLEND);
        this.tailTip.draw(shape);
        this.headTip.draw(shape);
    }

    public void addTail(Location l) {
        Station s = new Station(this, l);
        Station f = stationList.get(stationList.size() - 1);
        stationList.add(s);
        sectionList.add(new Section(this, f, s));
//        modelService.updateLine(this, getLocationList());
        this.headTip = new Tip(this, this.stationList.get(0));
        this.tailTip = new Tip(this, this.stationList.get(this.stationList.size() - 1));

    }

    public void addPreviewTail(Vector2 v) {
        Vector2 f = stationList.get(stationList.size() - 1).getPlatform();
        sectionPreviewList.add(new SectionPreview(this, f, v));
    }

    public void removeLastPreview() {
        if (sectionPreviewList.size() > 0) {
            sectionPreviewList.remove(sectionPreviewList.size() - 1);
        }
    }

    public void addPreviewHead(Vector2 v) {
        Vector2 f = stationList.get(0).getPlatform();
        sectionPreviewList.add(new SectionPreview(this, v, f));
    }

    public void addPreviewMiddle(Vector2 v, Section s) {
        Vector2 a = s.upper.getPlatform();
        Vector2 c = s.lower.getPlatform();
        Vector2 b = v;
        s.fade();
        this.sectionPreviewList.add(new SectionPreview(this, a, b));
        this.sectionPreviewList.add(new SectionPreview(this, b, c));
    }

    public void removeTail() {
        if (sectionList.size() == 1) {
            destroy();
        } else {
            stationList.get(stationList.size() - 1).destroy();
            stationList.remove(stationList.size() - 1);
            sectionList.get(sectionList.size() - 1).destroy();
            sectionList.remove(sectionList.size() - 1);
        }
        this._update();
    }
    public void addHead(Location l) {
        Station s = new Station(this, l);
        Station f = stationList.get(0);
        stationList.add(0, s);
        sectionList.add(0, new Section(this, s, f));
        this._update();
    }

    public void removeHead() {
        if (sectionList.size() == 1) {
            this.destroy();
        } else {
            stationList.get(0).destroy();
            stationList.remove(0);
            sectionList.get(0).destroy();
            sectionList.remove(0);
        }
        this._update();
    }

    public void addMiddle(Location l, Section s) {
        Station aUpper = s.upper;
        Station middle = new Station(this, l);
        Station bLower = s.lower;
        stationList.add(stationList.indexOf(aUpper) + 1, middle);
        sectionList.add(sectionList.indexOf(s), new Section(this, aUpper, middle));
        sectionList.add(sectionList.indexOf(s), new Section(this, middle, bLower));
        sectionList.remove(s);
        s.destroy();
        this._update();
    }

    public void removeMiddle(Location l) {
        Section a = null;
        Section b = null;
        Station s = null;
        for (Station i : stationList) {
            if (i.location == l) {
                s = i;
                break;
            }
        }

        for (Section i : sectionList) {
            if (i.upper == s) {
                b = i;
            } else if (i.lower == s) {
                a = i;
            }
        }
        stationList.remove(s);
        s.destroy();
        sectionList.add(sectionList.indexOf(a), new Section(this, a.upper, b.lower));
        sectionList.remove(a);
        sectionList.remove(b);
        a.destroy();
        b.destroy();
        this._update();
    }


    public void destroy() {
        for (Station s : stationList) {
            s.destroy();
        }
        stationList.removeAll(stationList);
        for (Section s : sectionList) {
            s.destroy();
        }
        sectionList.removeAll(sectionList);

        modelService.updateLine(this, getLocationList());
    }

    public boolean hasSection(Section s) {
        return this.sectionList.contains(s);
    }

}
