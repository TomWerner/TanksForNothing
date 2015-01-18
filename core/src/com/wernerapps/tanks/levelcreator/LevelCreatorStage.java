package com.wernerapps.tanks.levelcreator;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.wernerapps.tanks.game.CameraController;
import com.wernerapps.tanks.game.TanksGame;
import com.wernerapps.tanks.game.WorldController;
import com.wernerapps.tanks.gameobjects.Background;
import com.wernerapps.tanks.gameobjects.Placeable;
import com.wernerapps.tanks.helpers.AssetLoader;

public class LevelCreatorStage extends Stage
{
    private TanksGame        game;
    private GameUI           currentUI;

    private WorldController  controller;
    private CameraController cameraController = new CameraController(200);
    private Rectangle        gameBounds;

    public LevelCreatorStage(TanksGame game)
    {
        this.game = game;
        currentUI = new StartUI(this);
        setController(new WorldController());
        gameBounds = new Rectangle(0, 0, getWidth() * (1), getHeight() * (1));
    }

    @Override
    public void draw()
    {
        super.draw();
        if (currentUI != null)
            currentUI.draw(this);
    }

    @Override
    public void act(float delta)
    {
        super.act(delta);

        if (currentUI instanceof CreateUI)
        {
            Placeable current = ((CreateUI) currentUI).getCurrentItem();

            if (current == null)
            {
                float cameraSpeed = 150;
                if (getController().isKeyDown(Keys.LEFT))
                    getCamera().position.x -= cameraSpeed * delta;
                if (getController().isKeyDown(Keys.RIGHT))
                    getCamera().position.x += cameraSpeed * delta;
                if (getController().isKeyDown(Keys.DOWN))
                    getCamera().position.y -= cameraSpeed * delta;
                if (getController().isKeyDown(Keys.UP))
                    getCamera().position.y += cameraSpeed * delta;
            }
            else
            {
                float objectSpeed = 50;
                if (getController().isKeyDown(Keys.LEFT))
                    current.setPosition(new Vector2(current.getPosition().x - objectSpeed * delta, current
                            .getPosition().y));
                if (getController().isKeyDown(Keys.RIGHT))
                    current.setPosition(new Vector2(current.getPosition().x + objectSpeed * delta, current
                            .getPosition().y));
                if (getController().isKeyDown(Keys.DOWN))
                    current.setPosition(new Vector2(current.getPosition().x, current.getPosition().y - objectSpeed
                            * delta));
                if (getController().isKeyDown(Keys.UP))
                    current.setPosition(new Vector2(current.getPosition().x, current.getPosition().y + objectSpeed
                            * delta));
                if (getController().isKeyDown(Keys.COMMA))
                    current.rotateBy(objectSpeed * delta);
                if (getController().isKeyDown(Keys.PERIOD))
                    current.rotateBy(-objectSpeed * delta);
            }
        }
        cameraController.boundCamera(getCamera(), gameBounds);
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button)
    {
        if (currentUI != null)
            currentUI.handleTouchDown(this, screenX, screenY, button);
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button)
    {
        if (currentUI != null)
            currentUI.handleTouchUp(this, screenX, screenY);
        return false;
    }

    public void createLevel(int widthFactor, int heightFactor, String backgroundType)
    {
        backgroundType = backgroundType.toLowerCase() + ".png";
        while (getActors().size > 0)
            getActors().removeIndex(0).remove();
        currentUI = null;

        TextureRegion ground = AssetLoader.textureAtlas.get(backgroundType);

        int width = (int) getWidth() / ground.getRegionWidth();
        int height = (int) getHeight() / ground.getRegionHeight();

        for (int i = -(width / 2 + 1); i < width * widthFactor + (width / 2 + 1); i++)
        {
            for (int k = -(height / 2 + 1); k < height * (heightFactor + 1) + (height / 2 + 2); k++)
            {
                Background image = new Background(ground);
                image.setPosition(image.getWidth() * i, image.getHeight() * k);
                addActor(image);
            }
        }
        gameBounds = new Rectangle(0, 0, getWidth() * (widthFactor), getHeight() * (heightFactor));

        String sandbagType = "sandbagBeige.png";
        if (backgroundType.equals("sand.png"))
            sandbagType = "sandbagBrown.png";
        currentUI = new CreateUI(this,backgroundType, sandbagType);
    }

    @Override
    public boolean keyDown(int keycode)
    {
        getController().setKeyDown(keycode);
        return true;
    }

    @Override
    public boolean keyUp(int keycode)
    {
        getController().setKeyUp(keycode);
        return true;
    }

    public WorldController getController()
    {
        return controller;
    }

    public void setController(WorldController controller)
    {
        this.controller = controller;
    }

    public TanksGame getGame()
    {
        return game;
    }

}
