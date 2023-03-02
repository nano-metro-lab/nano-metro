package nanometro.gfx;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

import static nanometro.GameScreen.camera;
import static nanometro.GameScreen.world;
import static nanometro.GameScreen.modelService;

import java.util.ArrayList;
import java.util.List;

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

    public void draw(ShapeRenderer shape) {
        int k = this.sampleList.size();
        for (int i = 0; i < k - 1; i++) {
            shape.setProjectionMatrix(camera.combined);
            shape.begin(ShapeRenderer.ShapeType.Filled);
            shape.setColor(Color.valueOf(this.colourObj.subColour1));
            shape.rectLine(sampleList.get(i) , sampleList.get(i + 1), 0.7f);
            shape.end();
        }
    }

    public void destroy() {
//        this.line.sectionPreviewList.remove(this);
    }
}
