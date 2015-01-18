package com.wernerapps.tanks.menu;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.wernerapps.tanks.game.TanksGame;
import com.wernerapps.tanks.helpers.AssetLoader;

public class MenuUI
{
    private static final int MAX_ITEMS = 4;
    private String[]         lines;
    private Vector2[]        positions;
    private Rectangle[]      bounds;
    private MenuHandler      menuHandler;
    private Vector2          titlePosition;
    private String           title;
    private boolean          bigItems  = true;
    private String           footer;
    private Vector2          footerPosition;
    private Rectangle        footerBounds;
    private int              itemOffset;
    private Image            upButton;
    private Image            downButton;
    private Rectangle        upButtonBounds;
    private Rectangle        downButtonBounds;

    public MenuUI(Stage stage, String[] lines, String title, MenuHandler menuHandler)
    {
        this(stage, lines, title, "", menuHandler);
    }

    public MenuUI(Stage stage, String[] lines, String title, String footer, MenuHandler menuHandler)
    {
        this.menuHandler = menuHandler;
        this.lines = lines;
        this.footer = footer;
        this.title = title;
        this.positions = new Vector2[lines.length];
        this.bounds = new Rectangle[lines.length];

        TextBounds temp = new TextBounds(AssetLoader.fontMedium.getBounds(lines[0]));
        float multiplier = (stage.getHeight() / 2) / temp.height / (float) lines.length;
        if (multiplier < 2.25f)
            multiplier = 2.25f;
        itemOffset = 0;

        float maxBoundWidth = 0;
        TextBounds bounds;
        for (int i = 0; i < lines.length; i++)
        {
            if (bigItems)
                bounds = new TextBounds(AssetLoader.fontMedium.getBounds(lines[i]));
            else
                bounds = new TextBounds(AssetLoader.fontSmall.getBounds(lines[i]));

            float xOffset = stage.getWidth() / 2 - bounds.width / 2;
            float yOffset = stage.getHeight() / 4 + bounds.height * i * multiplier;

            positions[i] = stage.screenToStageCoordinates(new Vector2(xOffset, yOffset));
            this.bounds[i] = new Rectangle(xOffset, yOffset, bounds.width, bounds.height);
            maxBoundWidth = Math.max(this.bounds[i].width, maxBoundWidth);
            if (bounds.width > stage.getWidth())
            {
                bigItems = false;
                i = -1;
                maxBoundWidth = 0;
            }
        }

        bounds = new TextBounds(AssetLoader.fontBig.getBounds(footer));
        float xOffset = stage.getWidth() / 2 - bounds.width / 2;
        float yOffset = stage.getHeight() * .85f - bounds.height;

        footerPosition = stage.screenToStageCoordinates(new Vector2(xOffset, yOffset));
        footerBounds = new Rectangle(xOffset, yOffset, bounds.width, bounds.height);

        bounds = new TextBounds(AssetLoader.fontBig.getBounds(title));
        xOffset = stage.getWidth() / 2 - bounds.width / 2;
        yOffset = stage.getHeight() * .25f - bounds.height * 2;
        titlePosition = stage.screenToStageCoordinates(new Vector2(xOffset, yOffset));

        downButton = new Image(AssetLoader.textureAtlas.get("down.png"));
        downButton.setPosition(stage.getWidth() / 2 + maxBoundWidth / 2, stage.getHeight() * .25f);
        downButtonBounds = new Rectangle(stage.getWidth() / 2 + maxBoundWidth / 2, stage.getHeight() * .25f,
                downButton.getWidth(), downButton.getHeight());
        stage.addActor(downButton);
        downButton.setVisible(lines.length > MAX_ITEMS && itemOffset + MAX_ITEMS < lines.length);

        upButton = new Image(AssetLoader.textureAtlas.get("up.png"));
        upButton.setPosition(stage.getWidth() / 2 + maxBoundWidth / 2, stage.getHeight() * .75f - upButton.getHeight());
        upButtonBounds = new Rectangle(stage.getWidth() / 2 + maxBoundWidth / 2, stage.getHeight() * .75f
                - upButton.getHeight(), upButton.getWidth(), upButton.getHeight());
        stage.addActor(upButton);
        upButton.setVisible(lines.length > MAX_ITEMS && itemOffset > 0);
    }
    
    public void endMenu()
    {
        upButton.remove();
        downButton.remove();
    }

    public void draw(Stage world)
    {
        Batch batch = world.getBatch();
        batch.begin();

        AssetLoader.fontBig.setColor(0, 0, 0, 1);
        AssetLoader.fontMedium.setColor(0, 0, 0, 1);
        AssetLoader.fontSmall.setColor(0, 0, 0, 1);

        for (int i = itemOffset; i < itemOffset + MAX_ITEMS && i < lines.length; i++)
        {
            if (bigItems)
                AssetLoader.fontMedium.draw(world.getBatch(), lines[i], positions[i].x, positions[i - itemOffset].y);
            else
                AssetLoader.fontSmall.draw(world.getBatch(), lines[i], positions[i].x, positions[i - itemOffset].y);
        }

        AssetLoader.fontBig.draw(world.getBatch(), footer, footerPosition.x, footerPosition.y);
        AssetLoader.fontBig.draw(world.getBatch(), title, titlePosition.x, titlePosition.y);

        batch.end();
    }

    public void checkForButtonClicked(TanksGame game, Stage stage, float screenX, float screenY)
    {
        for (int i = 0; i < lines.length && i < MAX_ITEMS; i++)
        {
            if (bounds[i].contains(screenX, screenY))
                menuHandler.menuItemClicked(game, i + itemOffset);
        }
        if (footerBounds.contains(screenX, screenY))
            menuHandler.menuItemClicked(game, Integer.MAX_VALUE);

        if (downButtonBounds.contains(screenX, stage.getHeight() - screenY) && downButton.isVisible())
            changeItemOffset(1);
        if (upButtonBounds.contains(screenX, stage.getHeight() - screenY) && upButton.isVisible())
            changeItemOffset(-1);
    }

    private void changeItemOffset(int change)
    {
        itemOffset += change;
        upButton.setVisible(lines.length > MAX_ITEMS && itemOffset > 0);
        downButton.setVisible(lines.length > MAX_ITEMS && itemOffset + MAX_ITEMS < lines.length);
    }

    public interface MenuHandler
    {
        public void menuItemClicked(TanksGame game, int index);
    }
}
