package nanometro.gfx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

import java.util.ArrayList;
import java.util.List;

import static nanometro.GameScreen.camera;
import static nanometro.GameScreen.hudBatch;

public class HUD {
    List<Sprite> spriteList = new ArrayList<>();
    public HUD() {
        Texture settingsTexture = new Texture(Gdx.files.internal("./HUD/settings.png"));
        Sprite settingsSprite = new Sprite(settingsTexture);
        settingsSprite.setSize(3, 3);
        this.spriteList.add(settingsSprite);
    }

    public void draw() {
        for (Sprite s: this.spriteList) {
            hudBatch.begin();
            hudBatch.setProjectionMatrix(camera.combined);
            s.setPosition(5, 5);
            s.draw(hudBatch);
            hudBatch.end();
        }
    }
}
