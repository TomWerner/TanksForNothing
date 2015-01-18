package com.wernerapps.tanks.levelcreator;

import java.util.ArrayList;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.wernerapps.tanks.gameobjects.Obstacle;
import com.wernerapps.tanks.gameobjects.Placeable;
import com.wernerapps.tanks.gameobjects.Sandbag;
import com.wernerapps.tanks.gameobjects.Tank;
import com.wernerapps.tanks.helpers.AssetLoader;

public class CreateUI implements GameUI
{
    private Image               leftArrow;
    private Image               downArrow;
    private Image               rightArrow;
    private Image               upArrow;
    private Image               turnLeftArrow;
    private Image               turnRightArrow;
    private Image               confirmButton;
    private Image               cancelButton;

    private Image               sandbagBackground;
    private Image               team1Background;
    private Image               team2Background;
    private Image               sandbagIcon;
    private Tank                tank1;
    private Tank                tank2;
    private Image               selectedIcon;
    private int                 selectedIndex;
    private TextureRegion       sandbagTexture;

    private ArrayList<Obstacle> obstacles   = new ArrayList<Obstacle>();
    private ArrayList<Tank>     team1Tanks  = new ArrayList<Tank>();
    private ArrayList<Tank>     team2Tanks  = new ArrayList<Tank>();
    private Placeable           currentItem = null;
    private float               lastRotation;
    private int                 startOfNonBackground;
    private Image               saveButton;
    private String              backgroundName;
    private String              sandbagName;

    public CreateUI(LevelCreatorStage stage, String backgroundName, String sandbagName)
    {
        this.backgroundName = backgroundName;
        this.sandbagName = sandbagName;
        startOfNonBackground = stage.getActors().size;
        leftArrow = new Image(AssetLoader.textureAtlas.get("arrow.png"));
        leftArrow.setOrigin(leftArrow.getWidth() / 2, leftArrow.getHeight() / 2);
        leftArrow.rotateBy(90);
        stage.addActor(leftArrow);

        downArrow = new Image(AssetLoader.textureAtlas.get("arrow.png"));
        downArrow.setOrigin(downArrow.getWidth() / 2, downArrow.getHeight() / 2);
        downArrow.rotateBy(180);
        stage.addActor(downArrow);

        rightArrow = new Image(AssetLoader.textureAtlas.get("arrow.png"));
        rightArrow.setOrigin(rightArrow.getWidth() / 2, rightArrow.getHeight() / 2);
        rightArrow.rotateBy(-90);
        stage.addActor(rightArrow);

        upArrow = new Image(AssetLoader.textureAtlas.get("arrow.png"));
        upArrow.setOrigin(upArrow.getWidth() / 2, upArrow.getHeight() / 2);
        stage.addActor(upArrow);

        turnLeftArrow = new Image(AssetLoader.textureAtlas.get("return.png"));
        turnLeftArrow.setOrigin(turnLeftArrow.getWidth() / 2, turnLeftArrow.getHeight() / 2);
        stage.addActor(turnLeftArrow);

        turnRightArrow = new Image(AssetLoader.textureAtlas.get("return.png"));
        turnRightArrow.setOrigin(turnRightArrow.getWidth() / 2, turnRightArrow.getHeight() / 2);
        turnRightArrow.setScaleX(-1);
        stage.addActor(turnRightArrow);

        confirmButton = new Image(AssetLoader.textureAtlas.get("checkmark.png"));
        stage.addActor(confirmButton);
        cancelButton = new Image(AssetLoader.textureAtlas.get("cross.png"));
        stage.addActor(cancelButton);

        sandbagBackground = new Image(AssetLoader.textureAtlas.get("glassPanel_corners.png"));
        stage.addActor(sandbagBackground);
        team1Background = new Image(AssetLoader.textureAtlas.get("glassPanel_corners.png"));
        stage.addActor(team1Background);
        team2Background = new Image(AssetLoader.textureAtlas.get("glassPanel_corners.png"));
        stage.addActor(team2Background);

        selectedIcon = new Image(AssetLoader.textureAtlas.get("metalPanel.png"));
        stage.addActor(selectedIcon);
        selectedIcon.setVisible(false);

        saveButton = new Image(AssetLoader.textureAtlas.get("save.png"));
        stage.addActor(saveButton);

        sandbagTexture = AssetLoader.textureAtlas.get(sandbagName);
        sandbagIcon = new Image(sandbagTexture);
        stage.addActor(sandbagIcon);
        tank1 = new Tank(1, new Vector2(0, 0), 1);
        for (int i = 0; i < 2; i++)
            stage.addActor(tank1.getImages()[i]);
        tank2 = new Tank(3, new Vector2(0, 0), 1);
        for (int i = 0; i < 2; i++)
            stage.addActor(tank2.getImages()[i]);
    }

    @Override
    public void handleTouchDown(LevelCreatorStage stage, int screenX, int screenY, int button)
    {
        Vector2 temp = new Vector2(screenX, screenY);
        float unit = upArrow.getWidth();
        float width = stage.getWidth();
        float height = stage.getHeight();

        if (between(width - saveButton.getWidth(), width, temp.x) && between(0, saveButton.getWidth(), temp.y))
            saveLevel();

        if (between(width - unit * 2, width - unit, temp.x) && between(height - unit * 2, height - unit, temp.y))
            stage.getController().setKeyDown(Keys.UP);
        else
            stage.getController().setKeyUp(Keys.UP);

        if (between(width - unit * 3, width - unit * 2, temp.x) && between(height - unit, height, temp.y))
            stage.getController().setKeyDown(Keys.LEFT);
        else
            stage.getController().setKeyUp(Keys.LEFT);

        if (between(width - unit * 2, width - unit, temp.x) && between(height - unit, height, temp.y))
            stage.getController().setKeyDown(Keys.DOWN);
        else
            stage.getController().setKeyUp(Keys.DOWN);

        if (between(width - unit, width, temp.x) && between(height - unit, height, temp.y))
            stage.getController().setKeyDown(Keys.RIGHT);
        else
            stage.getController().setKeyUp(Keys.RIGHT);

        // Turn left
        if (between(width - unit * 3, width - unit * 2, temp.x) && between(height - unit * 2, height - unit, temp.y))
            stage.getController().setKeyDown(Keys.COMMA);
        else
            stage.getController().setKeyUp(Keys.COMMA);
        // Turn right
        if (between(width - unit * 1, width - unit * 0, temp.x) && between(height - unit * 2, height - unit, temp.y))
            stage.getController().setKeyDown(Keys.PERIOD);
        else
            stage.getController().setKeyUp(Keys.PERIOD);

        if ((between(width - unit * 4, width - unit * 3, temp.x) && between(height - unit, height, temp.y) && currentItem != null)
                || (currentItem != null && button == 1))
            confirmCurrentPlaceable();
        if (between(width - unit * 5, width - unit * 4, temp.x) && between(height - unit, height, temp.y)
                && currentItem != null)
        {
            currentItem.removeObject();
            currentItem = null;
        }

        unit = sandbagBackground.getHeight();
        // In the selection bar
        if (between(height - unit, height, temp.y))
        {
            if (between(unit * 0, unit * 1, temp.x))
                select(0);
            if (between(unit * 1, unit * 2, temp.x))
                select(1);
            if (between(unit * 2, unit * 3, temp.x))
                select(2);
        }
        else if (selectedIndex != -1 && getCurrentItem() == null && !stage.getController().isKeyDown())
        {
            Vector2 pos = stage.screenToStageCoordinates(temp);
            // Clicked on the game
            if (selectedIndex == 0)
            {
                Tank tank = new Tank(1, pos, 1);
                tank.setPosition(pos.sub(0, tank.getImages()[0].getHeight() / 2));
                tank.updateImages();
                addPlaceable(stage, tank);
            }
            else if (selectedIndex == 1)
            {
                Tank tank = new Tank(3, pos, 1);
                tank.setPosition(pos.sub(0, tank.getImages()[0].getHeight() / 2));
                tank.updateImages();
                addPlaceable(stage, tank);
            }
            else if (selectedIndex == 2)
            {
                pos = pos.sub(sandbagTexture.getRegionWidth() / 2, sandbagTexture.getRegionHeight() / 2);
                addPlaceable(stage, new Sandbag(sandbagTexture, pos));
            }
        }
    }

    private void saveLevel()
    {
        System.out.println("background:" + backgroundName);
        System.out.println("sandbag:" + sandbagName);
        for (Obstacle obstacle : obstacles)
        {
            System.out.println("obstacle:" + obstacle.getPosition().x + "," + obstacle.getPosition().y + ","
                    + obstacle.getRotation());
        }
        for (Tank tank : team1Tanks)
        {
            System.out.println("team1:" + tank.getPosition().x + "," + tank.getPosition().y + "," + tank.getRotation());
        }
        for (Tank tank : team2Tanks)
        {
            System.out.println("team2:" + tank.getPosition().x + "," + tank.getPosition().y + "," + tank.getRotation());
        }
    }

    private void confirmCurrentPlaceable()
    {
        try
        {
        saveLevel();
        if (-selectedIndex == 0)
            team1Tanks.add((Tank) currentItem);
        else if (selectedIndex == 1)
            team2Tanks.add((Tank) currentItem);
        else if (selectedIndex == 2)
            obstacles.add((Obstacle) currentItem);
        lastRotation = currentItem.getRotation();
        currentItem = null;
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
    }

    private void addPlaceable(LevelCreatorStage stage, Placeable object)
    {
        setCurrentItem(object);
        object.rotateBy(lastRotation);
        object.placeObject(stage, startOfNonBackground);
    }

    private void select(int index)
    {
        if (selectedIndex == index)
            selectedIndex = -1;
        else
            selectedIndex = index;
    }

    private boolean between(float start, float end, float test)
    {
        return test > start && test < end;
    }

    @Override
    public void handleTouchUp(LevelCreatorStage stage, int screenX, int screenY)
    {
        Vector2 temp = new Vector2(screenX, screenY);
        float unit = upArrow.getWidth();
        float width = stage.getWidth();
        float height = stage.getHeight();

        if (between(width - unit * 2, width - unit, temp.x) && between(height - unit * 2, height - unit, temp.y))
            stage.getController().setKeyUp(Keys.UP);

        if (between(width - unit * 3, width - unit * 2, temp.x) && between(height - unit, height, temp.y))
            stage.getController().setKeyUp(Keys.LEFT);

        if (between(width - unit * 2, width - unit, temp.x) && between(height - unit, height, temp.y))
            stage.getController().setKeyUp(Keys.DOWN);

        if (between(width - unit, width, temp.x) && between(height - unit, height, temp.y))
            stage.getController().setKeyUp(Keys.RIGHT);

        // Turn left
        if (between(width - unit * 3, width - unit * 2, temp.x) && between(height - unit * 2, height - unit, temp.y))
            stage.getController().setKeyUp(Keys.COMMA);
        // Turn right
        if (between(width - unit * 1, width - unit * 0, temp.x) && between(height - unit * 2, height - unit, temp.y))
            stage.getController().setKeyUp(Keys.PERIOD);
    }

    @Override
    public void draw(LevelCreatorStage stage)
    {
        Vector2 temp = stage.screenToStageCoordinates(new Vector2(stage.getWidth() - leftArrow.getWidth() * 3, stage
                .getHeight()));
        leftArrow.setPosition(temp.x, temp.y);

        temp = stage.screenToStageCoordinates(new Vector2(stage.getWidth() - leftArrow.getWidth() * 2, stage
                .getHeight()));
        downArrow.setPosition(temp.x, temp.y);

        temp = stage.screenToStageCoordinates(new Vector2(stage.getWidth() - leftArrow.getWidth(), stage.getHeight()));
        rightArrow.setPosition(temp.x, temp.y);

        temp = stage.screenToStageCoordinates(new Vector2(stage.getWidth() - leftArrow.getWidth() * 2, stage
                .getHeight() - upArrow.getHeight()));
        upArrow.setPosition(temp.x, temp.y);

        temp = stage.screenToStageCoordinates(new Vector2(stage.getWidth() - leftArrow.getWidth() * 3, stage
                .getHeight() - upArrow.getHeight()));
        turnLeftArrow.setPosition(temp.x, temp.y);
        temp = stage.screenToStageCoordinates(new Vector2(stage.getWidth() - leftArrow.getWidth() * 1, stage
                .getHeight() - upArrow.getHeight()));
        turnRightArrow.setPosition(temp.x, temp.y);
        temp = stage.screenToStageCoordinates(new Vector2(stage.getWidth() - leftArrow.getWidth() * 4, stage
                .getHeight()));
        confirmButton.setPosition(temp.x, temp.y);
        temp = stage.screenToStageCoordinates(new Vector2(stage.getWidth() - leftArrow.getWidth() * 5, stage
                .getHeight()));
        cancelButton.setPosition(temp.x, temp.y);
        turnLeftArrow.setVisible(getCurrentItem() != null);
        turnRightArrow.setVisible(getCurrentItem() != null);
        cancelButton.setVisible(getCurrentItem() != null);
        confirmButton.setVisible(getCurrentItem() != null);

        selectedIcon.setVisible(selectedIndex != -1);
        temp = stage.screenToStageCoordinates(new Vector2(selectedIcon.getWidth() * selectedIndex, stage.getHeight()));
        selectedIcon.setPosition(temp.x, temp.y);

        temp = stage.screenToStageCoordinates(new Vector2(0, stage.getHeight()));
        team1Background.setPosition(temp.x, temp.y);
        temp = stage.screenToStageCoordinates(new Vector2(team1Background.getWidth() / 2, stage.getHeight()
                - team1Background.getHeight() / 2 + tank1.getImages()[0].getHeight() / 2));
        tank1.setPosition(temp);
        tank1.updateImages();

        temp = stage.screenToStageCoordinates(new Vector2(team1Background.getWidth(), stage.getHeight()));
        team2Background.setPosition(temp.x, temp.y);
        temp = stage.screenToStageCoordinates(new Vector2(team1Background.getWidth() * 1.5f, stage.getHeight()
                - team1Background.getHeight() / 2 + tank2.getImages()[0].getHeight() / 2));
        tank2.setPosition(temp);
        tank2.updateImages();

        temp = stage.screenToStageCoordinates(new Vector2(team1Background.getWidth() * 2, stage.getHeight()));
        sandbagBackground.setPosition(temp.x, temp.y);
        temp = stage.screenToStageCoordinates(new Vector2(team1Background.getWidth() * 2 + sandbagBackground.getWidth()
                / 2 - sandbagIcon.getWidth() / 2, stage.getHeight() - sandbagBackground.getHeight() / 2
                + sandbagIcon.getHeight() / 2));
        sandbagIcon.setPosition(temp.x, temp.y);

        temp = stage.screenToStageCoordinates(new Vector2(stage.getWidth() - saveButton.getWidth(), saveButton
                .getHeight()));
        saveButton.setPosition(temp.x, temp.y);
    }

    public Placeable getCurrentItem()
    {
        return currentItem;
    }

    public void setCurrentItem(Placeable currentItem)
    {
        this.currentItem = currentItem;
    }
}
