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
    Line line;
    Station upper;
    Station lower;
    Boxy path;
    List<Vector2> sampleList = new ArrayList<>();

    public SectionPreview(Line line, Vector2 upper, Vector2 lower) {
        this.line = line;
//        this.upper = upper;
//        this.lower = lower;
        this.path = new Boxy(upper, lower);
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
            shape.setColor(Color.valueOf(this.line.colour));
            shape.rectLine(sampleList.get(i) , sampleList.get(i + 1), 0.7f);
//            shape.line(sampleList.get(i) , sampleList.get(i + 1));
            shape.end();
        }
    }

    public void destroy() {
//        this.line.sectionPreviewList.remove(this);
    }
}