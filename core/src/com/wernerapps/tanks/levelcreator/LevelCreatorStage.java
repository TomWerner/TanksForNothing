package com.wernerapps.tanks.levelcreator;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.wernerapps.tanks.game.CameraController;
import com.wernerapps.tanks.game.TanksGame;
import com.wernerapps.tanks.game.WorldController;
import com.wernerapps.tanks.gameobjects.Background;
import com.wernerapps.tanks.helpers.AssetLoader;

public class LevelCreatorStage extends Stage
{
    private TanksGame        game;
    private StartUI          startUI;

    private WorldController  controller;
    private CameraController cameraController = new CameraController(200);
    private Rectangle gameBounds;

    public LevelCreatorStage(TanksGame game)
    {
        this.game = game;
        startUI = new StartUI(this);
        controller = new WorldController();
        gameBounds = new Rectangle(0, 0, getWidth() * (1), getHeight() * (1));
    }

    @Override
    public void draw()
    {
        super.draw();
        if (startUI != null)
            startUI.draw(this);
    }

    @Override
    public void act(float delta)
    {
        super.act(delta);

        if (controller.isKeyDown(Keys.LEFT))
            getCamera().position.x -= 5;
        if (controller.isKeyDown(Keys.RIGHT))
            getCamera().position.x += 5;
        if (controller.isKeyDown(Keys.DOWN))
            getCamera().position.y -= 5;
        if (controller.isKeyDown(Keys.UP))
            getCamera().position.y += 5;
        cameraController.boundCamera(getCamera(), gameBounds);
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button)
    {
        if (startUI != null)
            startUI.handleTouchDown(this, screenX, screenY);
        return false;
    }

    public void createLevel(int widthFactor, int heightFactor, String backgroundType)
    {
        backgroundType = backgroundType.toLowerCase() + ".png";
        while (getActors().size > 0)
            getActors().removeIndex(0).remove();
        startUI = null;

        TextureRegion ground = AssetLoader.textureAtlas.get(backgroundType);

        int width = (int) getWidth() / ground.getRegionWidth();
        int height = (int) getHeight() / ground.getRegionHeight();

        for (int i = -(width / 2 + 1); i < width * widthFactor + (width / 2 + 1); i++)
        {
            for (int k = -(height / 2 + 1); k < height * heightFactor + (height / 2 + 2); k++)
            {
                Background image = new Background(ground);
                image.setPosition(image.getWidth() * i, image.getHeight() * k);
                addActor(image);
            }
        }
        gameBounds = new Rectangle(0, 0, getWidth() * (widthFactor), getHeight() * (heightFactor));
    }

    @Override
    public boolean keyDown(int keycode)
    {
        controller.setKeyDown(keycode);
        return true;
    }

    @Override
    public boolean keyUp(int keycode)
    {
        controller.setKeyUp(keycode);
        return true;
    }

}
