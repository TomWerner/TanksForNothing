package com.wernerapps.tanks.menu;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.wernerapps.tanks.game.TanksGame;
import com.wernerapps.tanks.helpers.AssetLoader;

public class MenuUI
{
    private String[]    lines;
    private Vector2[]   positions;
    private Rectangle[] bounds;
    private MenuHandler menuHandler;
    private Vector2     titlePosition;
    private String      title;
    private boolean     bigItems  = true;
    private String footer;
    private Vector2 footerPosition;
    private Rectangle footerBounds;

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
        if (multiplier < 2f)
            multiplier = 2;

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
            if (bounds.width > stage.getWidth())
            {
                bigItems = false;
                i = -1;
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
    }

    public void draw(Stage world)
    {
        Batch batch = world.getBatch();
        batch.begin();

        AssetLoader.fontBig.setColor(0, 0, 0, 1);
        AssetLoader.fontMedium.setColor(0, 0, 0, 1);
        AssetLoader.fontSmall.setColor(0, 0, 0, 1);

        for (int i = 0; i < lines.length; i++)
        {
            if (bigItems)
                AssetLoader.fontMedium.draw(world.getBatch(), lines[i], positions[i].x, positions[i].y);
            else
                AssetLoader.fontSmall.draw(world.getBatch(), lines[i], positions[i].x, positions[i].y);
        }

        AssetLoader.fontBig.draw(world.getBatch(), footer, footerPosition.x, footerPosition.y);
        AssetLoader.fontBig.draw(world.getBatch(), title, titlePosition.x, titlePosition.y);

        batch.end();
    }

    public void checkForButtonClicked(TanksGame game, float screenX, float screenY)
    {
        for (int i = 0; i < lines.length; i++)
        {
            if (bounds[i].contains(screenX, screenY))
                menuHandler.menuItemClicked(game, i);
        }
        if (footerBounds.contains(screenX, screenY))
            menuHandler.menuItemClicked(game, Integer.MAX_VALUE);
    }

    public interface MenuHandler
    {
        public void menuItemClicked(TanksGame game, int index);
    }
}
