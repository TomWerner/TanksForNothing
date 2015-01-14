package com.wernerapps.tanks.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.wernerapps.tanks.game.TanksGame;

/**
 * The base class for all game screens.
 */
public abstract class AbstractScreen implements Screen
{
    // the fixed viewport dimensions (ratio: 1.6)
    public static final int GAME_VIEWPORT_WIDTH = 800, GAME_VIEWPORT_HEIGHT = 600;
    public static final int MENU_VIEWPORT_WIDTH = 800, MENU_VIEWPORT_HEIGHT = 600;

    protected final TanksGame game;
    protected Stage stage;

    private BitmapFont font;
    private SpriteBatch batch;

    public AbstractScreen(TanksGame game, Viewport viewport)
    {
        this.game = game;
        this.stage = new Stage(viewport);
    }

    protected String getName()
    {
        return getClass().getSimpleName();
    }
    
    protected Stage getStage()
    {
        return stage;
    }

    protected boolean isGameScreen()
    {
        return false;
    }

    // Lazily loaded collaborators

    public BitmapFont getFont()
    {
        if (font == null)
        {
            font = new BitmapFont();
        }
        return font;
    }

    public SpriteBatch getBatch()
    {
        if (batch == null)
        {
            batch = new SpriteBatch();
        }
        return batch;
    }

    // Screen implementation

    @Override
    public void show()
    {
        Gdx.app.log(TanksGame.LOG, "Showing screen: " + getName());

        // set the stage as the input processor
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void resize(int width, int height)
    {
        Gdx.app.log(TanksGame.LOG, "Resizing screen: " + getName() + " to: " + width + " x " + height);
    }

    @Override
    public void render(float delta)
    {
        // (1) process the game logic

        // update the actors
        stage.act(delta);

        // (2) draw the result

        // clear the screen with the given RGB color (black)
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // draw the actors
        stage.draw();

    }

    @Override
    public void hide()
    {
        Gdx.app.log(TanksGame.LOG, "Hiding screen: " + getName());

        // dispose the screen when leaving the screen;
        // note that the dipose() method is not called automatically by the
        // framework, so we must figure out when it's appropriate to call it
        dispose();
    }

    @Override
    public abstract void pause();

    @Override
    public void resume()
    {
        Gdx.app.log(TanksGame.LOG, "Resuming screen: " + getName());
    }

    @Override
    public void dispose()
    {
        Gdx.app.log(TanksGame.LOG, "Disposing screen: " + getName());

        // as the collaborators are lazily loaded, they may be null
        if (font != null)
            font.dispose();
        if (batch != null)
            batch.dispose();
    }
}