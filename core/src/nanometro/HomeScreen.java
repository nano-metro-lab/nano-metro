package nanometro;

        import com.badlogic.gdx.Game;
        import com.badlogic.gdx.Gdx;
        import com.badlogic.gdx.Screen;
        import com.badlogic.gdx.audio.Sound;
        import com.badlogic.gdx.graphics.GL20;
        import com.badlogic.gdx.graphics.Texture;
        import com.badlogic.gdx.scenes.scene2d.Actor;
        import com.badlogic.gdx.scenes.scene2d.InputEvent;
        import com.badlogic.gdx.scenes.scene2d.Stage;
        import com.badlogic.gdx.scenes.scene2d.actions.Actions;
        import com.badlogic.gdx.scenes.scene2d.ui.Skin;
        import com.badlogic.gdx.scenes.scene2d.ui.Table;
        import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
        import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
        import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class HomeScreen implements Screen {
    private NanoMetro game;
    private Stage stage;
    private TextButton playButton, exitButton;
    private Texture background;
    private Table table;
    private Skin skin;
    private Sound sound;
    private long soundPlay;
    private boolean soundPlayed = false;

    public HomeScreen(boolean swapScreen) {
        soundPlayed = swapScreen;
    }
    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        skin = new Skin(Gdx.files.internal("./menus/buttons.json"));
        sound = Gdx.audio.newSound(Gdx.files.internal("./menus/click.wav"));
        if (soundPlayed == false) {
            soundPlay = sound.play(1.0f);
            this.soundPlayed = true;
        }
        table = new Table();
        table.setBounds(200,100, Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2);
        background = new Texture("./menus/background.png");
        playButton = new TextButton("", skin, "play");
        playButton.addListener(new ClickListener(){

            @Override
            public void clicked(InputEvent event, float x, float y) {
                soundPlay = sound.play(1.0f);
                sound.setPitch(soundPlay, 2);
                sound.setLooping(soundPlay, false);
                stage.addAction(Actions.sequence(Actions.fadeOut(1), Actions.run(new Runnable(){
                    @Override
                    public void run(){
                        ((Game) Gdx.app.getApplicationListener()).setScreen(new MapSelectionScreen(true));
                    }
                })));
            }



        });
        exitButton = new TextButton("", skin, "exit");

        exitButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                soundPlay = sound.play(1.0f);
                sound.setPitch(soundPlay, 2);
                sound.setLooping(soundPlay, false);
                stage.addAction(Actions.sequence(Actions.fadeOut(1), Actions.run(new Runnable(){
                    @Override
                    public void run(){
                        Gdx.app.exit();
                    }
                })));
            }
        });
        table.add(playButton).expandY();
        table.row();
        table.add(exitButton).expandY();

        stage.addActor(table);

        Gdx.input.setInputProcessor(stage);

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(Gdx.graphics.getDeltaTime());
        stage.getBatch().begin();
        stage.getBatch().draw(background, 0, 0);
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
        skin.dispose();
        stage.dispose();

        sound.dispose();
    }
}
