package nanometro.gfx;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.QueryCallback;
import com.badlogic.gdx.physics.box2d.World;

import static nanometro.GameScreen.camera;
import static nanometro.GameScreen.world;
import static nanometro.GameScreen.modelService;

import java.util.ArrayList;
import java.util.List;

public class _Input_2 implements InputProcessor {

    private float x;
    private float y;

    private Vector2 v;
    private Vector2 offset = new Vector2(0, 0);

    public _Input_2(Body mouseBox) {
        super();
        this.mouseBox = mouseBox;
    }

    private Section startSection = null;
    private Location endLocation = null;

    public _Input_2() {

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
        // for button, 0 left click, 1 right click
        return false;
    }

    public boolean touchUp (int x, int y, int pointer, int button) {
        offset = v;
        System.out.println(v);
        v = null;
        return true;
    }

    public boolean touchDragged (int x, int y, int pointer) {
//        System.out.println(this.x - x);
//        System.out.println(this.y - y);

        if (v == null) {
            v = new Vector2(x, y);
            return true;
        }
//        System.out.println(offset);
        v.sub(x, y);
        camera.translate((float) (v.x * -0.1), (float) (v.y * 0.1));
        this.x = x;
        this.y = y;
        v = new Vector2(x, y);
        return true;
    }

    public boolean mouseMoved (int x, int y) {

        return false;
    }

    public boolean scrolled (float amountX, float amountY) {
//        float w = camera.viewportWidth;
//        float h = camera.viewportHeight;
//
//
//        camera.setToOrtho(false, w + amountY+ amountY+ amountY, h + amountY+ amountY+ amountY);

        return false;
    }


}
