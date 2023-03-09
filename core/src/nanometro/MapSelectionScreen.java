package nanometro;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import nanometro.level.loaders.London;

public class MapSelectionScreen implements Screen {
    private Stage stage;
    private Texture background;
    private Texture bar;
    private TextButton uccMapButton, back;
    private Skin skin;
    private Table table;
    private Sound sound;
    private long soundPlay;
    private boolean soundPlayed = false;
    public MapSelectionScreen(boolean swapScreen) {
        soundPlayed = swapScreen;
    }
    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());

        background = new Texture("./menus/MapSelectionBackground.png");
        bar = new Texture("./menus/bar.png");
        sound = Gdx.audio.newSound(Gdx.files.internal("./menus/click.wav"));
        skin = new Skin(Gdx.files.internal("./menus/buttons.json"));
        if (soundPlayed == false) {
            soundPlay = sound.play(1.0f);
            this.soundPlayed = true;
        }
        table = new Table();
        table.setBounds(0,10, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        uccMapButton = new TextButton("", skin, "map");
        uccMapButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                soundPlay = sound.play(1.0f);
                sound.setPitch(soundPlay, 2);
                sound.setLooping(soundPlay, false);
                stage.addAction(Actions.sequence(Actions.fadeOut(1), Actions.run(new Runnable(){
                    @Override
                    public void run(){
                        ((Game) Gdx.app.getApplicationListener()).setScreen(new GameScreen(NanoMetro.game, new London()));
                    }
                })));
            }
        });
        back = new TextButton("", skin, "back");
        back.setBounds(0, Gdx.graphics.getHeight()-back.getHeight(), back.getWidth(), back.getHeight());
        back.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                soundPlay = sound.play(1.0f);
                sound.setPitch(soundPlay, 2);
                sound.setLooping(soundPlay, false);
                stage.addAction(Actions.sequence(Actions.fadeOut(1), Actions.run(new Runnable(){
                    @Override
                    public void run(){
                        ((Game) Gdx.app.getApplicationListener()).setScreen(new HomeScreen(true));
                    }
                })));
            }
        });
        table.add(uccMapButton).expandY();
        stage.addActor(table);
        stage.addActor(back);
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(Gdx.graphics.getDeltaTime());
        stage.getBatch().begin();
        stage.getBatch().draw(background, 0, 0);
        stage.getBatch().draw(bar, 0, 600);
        stage.getBatch().end();
        stage.draw();

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
