package com.wernerapps.tanks.levelcreator;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.wernerapps.tanks.helpers.AssetLoader;

public class StartUI implements GameUI
{
    private Image[][] backgrounds;
    private int       xTiles;
    private int       yTiles;
    private String[]  backgroundTypes     = { "Dirt", "Sand", "Grass" };
    private Vector2[] backgroundPositions = new Vector2[3];
    private Image     backgroundUp;
    private Image     backgroundDown;
    private int       backgroundIndex     = 0;
    private Rectangle backgroundUpBounds;
    private Rectangle backgroundDownBounds;

    private String[]  numbers             = { "2x", "3x", "4x", "5x" };
    private Vector2[] widthPositions      = new Vector2[5];
    private Vector2[] heightPositions     = new Vector2[5];
    private int       widthIndex;
    private int       heightIndex;
    private Image     widthUp;
    private Image     widthDown;
    private Rectangle widthUpBounds;
    private Rectangle widthDownBounds;
    private Image     heightUp;
    private Image     heightDown;
    private Rectangle heightUpBounds;
    private Rectangle heightDownBounds;
    private Rectangle createButtonBounds;

    public StartUI(LevelCreatorStage stage)
    {
        xTiles = (int) (stage.getWidth() / 128f) + 1;
        yTiles = (int) (stage.getHeight() / 128f) + 1;
        backgrounds = new Image[xTiles * 3][yTiles];

        for (int i = 0; i < xTiles; i++)
        {
            for (int k = 0; k < yTiles; k++)
            {
                backgrounds[i][k] = new Image(AssetLoader.textureAtlas.get("dirt.png"));
                backgrounds[i][k].setPosition(i * 128, k * 128);
                stage.addActor(backgrounds[i][k]);
            }
        }
        for (int i = xTiles; i < xTiles * 2; i++)
        {
            for (int k = 0; k < yTiles; k++)
            {
                backgrounds[i][k] = new Image(AssetLoader.textureAtlas.get("sand.png"));
                backgrounds[i][k].setPosition(i * 128, k * 128);
                stage.addActor(backgrounds[i][k]);
            }
        }
        for (int i = xTiles * 2; i < xTiles * 3; i++)
        {
            for (int k = 0; k < yTiles; k++)
            {
                backgrounds[i][k] = new Image(AssetLoader.textureAtlas.get("grass.png"));
                backgrounds[i][k].setPosition(i * 128, k * 128);
                stage.addActor(backgrounds[i][k]);
            }
        }

        TextBounds bounds;

        for (int i = 0; i < backgroundTypes.length; i++)
        {
            bounds = new TextBounds(AssetLoader.fontMedium.getBounds(backgroundTypes[i]));
            float xOffset = stage.getWidth() * .85f - bounds.width / 2;
            float yOffset = stage.getHeight() * .4f + bounds.height;
            backgroundPositions[i] = new Vector2(xOffset, yOffset);
        }
        for (int i = 0; i < numbers.length; i++)
        {
            bounds = new TextBounds(AssetLoader.fontMedium.getBounds(numbers[i]));
            float xOffset1 = stage.getWidth() * .15f - bounds.width / 2;
            float xOffset2 = stage.getWidth() * .5f - bounds.width / 2;
            float yOffset = stage.getHeight() * .4f + bounds.height;
            widthPositions[i] = new Vector2(xOffset1, yOffset);
            heightPositions[i] = new Vector2(xOffset2, yOffset);
        }

        backgroundUp = new Image(AssetLoader.textureAtlas.get("up.png"));
        backgroundUp.setPosition(stage.getWidth() * .85f - backgroundUp.getWidth() / 2, stage.getHeight() * .4f
                + backgroundUp.getHeight() / 4);
        backgroundUpBounds = new Rectangle(backgroundUp.getX(), backgroundUp.getY(), backgroundUp.getWidth(),
                backgroundUp.getHeight());
        stage.addActor(backgroundUp);

        backgroundDown = new Image(AssetLoader.textureAtlas.get("down.png"));
        backgroundDown.setPosition(stage.getWidth() * .85f - backgroundDown.getWidth() / 2, stage.getHeight() * .4f
                - backgroundDown.getHeight());
        backgroundDownBounds = new Rectangle(backgroundDown.getX(), backgroundDown.getY(), backgroundDown.getWidth(),
                backgroundDown.getHeight());
        stage.addActor(backgroundDown);

        widthUp = new Image(AssetLoader.textureAtlas.get("up.png"));
        widthUp.setPosition(stage.getWidth() * .15f - widthUp.getWidth() / 2,
                stage.getHeight() * .4f + backgroundUp.getHeight() / 4);
        widthUpBounds = new Rectangle(widthUp.getX(), widthUp.getY(), widthUp.getWidth(), widthUp.getHeight());
        stage.addActor(widthUp);
        widthDown = new Image(AssetLoader.textureAtlas.get("down.png"));
        widthDown.setPosition(stage.getWidth() * .15f - widthDown.getWidth() / 2,
                stage.getHeight() * .4f - widthDown.getHeight());
        widthDownBounds = new Rectangle(widthDown.getX(), widthDown.getY(), widthDown.getWidth(), widthDown.getHeight());
        stage.addActor(widthDown);

        heightUp = new Image(AssetLoader.textureAtlas.get("up.png"));
        heightUp.setPosition(stage.getWidth() * .5f - heightUp.getWidth() / 2,
                stage.getHeight() * .4f + backgroundUp.getHeight() / 4);
        heightUpBounds = new Rectangle(heightUp.getX(), heightUp.getY(), heightUp.getWidth(), heightUp.getHeight());
        stage.addActor(heightUp);
        heightDown = new Image(AssetLoader.textureAtlas.get("down.png"));
        heightDown.setPosition(stage.getWidth() * .5f - heightDown.getWidth() / 2,
                stage.getHeight() * .4f - heightDown.getHeight());
        heightDownBounds = new Rectangle(heightDown.getX(), heightDown.getY(), heightDown.getWidth(),
                heightDown.getHeight());
        stage.addActor(heightDown);
    }

    public void draw(LevelCreatorStage stage)
    {
        Batch batch = stage.getBatch();
        batch.begin();

        AssetLoader.fontMedium.draw(batch, backgroundTypes[backgroundIndex], backgroundPositions[backgroundIndex].x,
                backgroundPositions[backgroundIndex].y);
        AssetLoader.fontMedium.draw(batch, numbers[widthIndex], widthPositions[widthIndex].x,
                widthPositions[widthIndex].y);
        AssetLoader.fontMedium.draw(batch, numbers[heightIndex], heightPositions[heightIndex].x,
                heightPositions[heightIndex].y);

        TextBounds bounds;
        bounds = new TextBounds(AssetLoader.fontMedium.getBounds("Width"));
        AssetLoader.fontMedium.draw(batch, "Width", stage.getWidth() * .15f - bounds.width / 2, stage.getHeight() * .4f
                + bounds.height * 5);
        bounds = new TextBounds(AssetLoader.fontMedium.getBounds("Height"));
        AssetLoader.fontMedium.draw(batch, "Height", stage.getWidth() * .5f - bounds.width / 2, stage.getHeight() * .4f
                + bounds.height * 5);
        bounds = new TextBounds(AssetLoader.fontMedium.getBounds("Ground"));
        AssetLoader.fontMedium.draw(batch, "Ground", stage.getWidth() * .85f - bounds.width / 2, stage.getHeight()
                * .4f + bounds.height * 5);
        bounds = new TextBounds(AssetLoader.fontBig.getBounds("Level Creator"));
        AssetLoader.fontBig.draw(batch, "Level Creator", stage.getWidth() * .5f - bounds.width / 2, stage.getHeight()
                * .8f + bounds.height);
        bounds = new TextBounds(AssetLoader.fontBig.getBounds("Create!"));
        AssetLoader.fontBig.draw(batch, "Create!", stage.getWidth() * .53f - bounds.width / 2, stage.getHeight() * .2f);
        if (createButtonBounds == null)
            createButtonBounds = new Rectangle(stage.getWidth() * .53f - bounds.width / 2, stage.getHeight() * .2f
                    - bounds.height, bounds.width, bounds.height);
        // System.out.println(createButtonBounds);

        batch.end();
    }

    public void handleTouchDown(LevelCreatorStage stage, int screenX, int screenY, int button)
    {
        if (backgroundUpBounds.contains(screenX, stage.getHeight() - screenY))
            changeBackgroundPosition(stage, 1);
        if (backgroundDownBounds.contains(screenX, stage.getHeight() - screenY))
            changeBackgroundPosition(stage, -1);
        if (widthUpBounds.contains(screenX, stage.getHeight() - screenY))
            changeWidthPosition(1);
        if (widthDownBounds.contains(screenX, stage.getHeight() - screenY))
            changeWidthPosition(-1);
        if (heightUpBounds.contains(screenX, stage.getHeight() - screenY))
            changeHeightPosition(1);
        if (heightDownBounds.contains(screenX, stage.getHeight() - screenY))
            changeHeightPosition(-1);
        if (createButtonBounds.contains(screenX, stage.getHeight() - screenY))
            stage.createLevel(widthIndex + 1, heightIndex + 1, backgroundTypes[backgroundIndex]);
    }

    private void changeHeightPosition(int change)
    {
        heightIndex += change + numbers.length;
        heightIndex %= numbers.length;
    }

    private void changeWidthPosition(int change)
    {
        widthIndex += change + numbers.length;
        widthIndex %= numbers.length;
    }

    private void changeBackgroundPosition(LevelCreatorStage stage, int change)
    {
        backgroundIndex += change + 3;
        backgroundIndex %= backgroundTypes.length;

        for (int i = 0; i < backgrounds.length; i++)
            for (int k = 0; k < backgrounds[i].length; k++)
                backgrounds[i][k].setX(i * 128 - backgroundIndex * xTiles * 128);
    }

    @Override
    public void handleTouchUp(LevelCreatorStage stage, int screenX, int screenY)
    {
        // TODO Auto-generated method stub
        
    }

}