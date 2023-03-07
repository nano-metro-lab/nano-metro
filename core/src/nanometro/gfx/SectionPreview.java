package nanometro.gfx;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

import java.util.ArrayList;
import java.util.List;

import static nanometro.GameScreen.*;

public class SectionPreview {
    Station upper;
    Station lower;
    Boxy path;
    List<Vector2> sampleList = new ArrayList<>();
    Colour colourObj;

    public SectionPreview(Vector2 upper, Vector2 lower, Colour colourObj) {
        this.path = new Boxy(upper, lower);
        this.colourObj = colourObj;
        generateSamples();
    }

    public void generateSamples() {
        int sampleNum = (int) (path.approxLength() / 0.1f);
        for (int i = 0; i < sampleNum; i++) {
            Vector2 j = new Vector2();
            path.valueAt(j, ((float)i)/((float)sampleNum - 1));
            this.sampleList.add(j);
        }
    }

    public void draw() {
        int k = this.sampleList.size();
        for (int i = 0; i < k - 1; i++) {
            shape.setProjectionMatrix(camera.combined);
            shape.begin(ShapeRenderer.ShapeType.Filled);
            shape.setColor(Color.valueOf(this.colourObj.subColour1));
            shape.rectLine(sampleList.get(i) , sampleList.get(i + 1), 0.76f);
            float cx = (sampleList.get(i).x + sampleList.get(i + 1).x) / 2;
            float cy = (sampleList.get(i).y + sampleList.get(i + 1).y) / 2;
            shape.circle(cx, cy, 0.38f, 15);
            shape.end();
        }
    }

    public void destroy() {
    }
}
