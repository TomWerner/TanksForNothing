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
    private Vector2 titlePosition;
    private String title;

    public MenuUI(Stage stage, String[] lines, String title, MenuHandler menuHandler)
    {
        this.menuHandler = menuHandler;
        this.lines = lines;
        this.title = title;
        this.positions = new Vector2[lines.length];
        this.bounds = new Rectangle[lines.length];
        
        TextBounds temp = new TextBounds(AssetLoader.fontMedium.getBounds(lines[0]));
        float multiplier = (stage.getHeight() / 2) / temp.height / (float)lines.length;

        for (int i = 0; i < lines.length; i++)
        {
            TextBounds bounds = new TextBounds(AssetLoader.fontMedium.getBounds(lines[i]));

            float xOffset = stage.getWidth() / 2 - bounds.width / 2;
            float yOffset = stage.getHeight() / 4 + bounds.height * i * multiplier;

            positions[i] = stage.screenToStageCoordinates(new Vector2(xOffset, yOffset));
            this.bounds[i] = new Rectangle(xOffset, yOffset, bounds.width, bounds.height);
        }
        
        TextBounds bounds = new TextBounds(AssetLoader.fontBig.getBounds(title));
        float xOffset = stage.getWidth() / 2 - bounds.width / 2;
        float yOffset = stage.getHeight() * .25f - bounds.height * 2;

        titlePosition = stage.screenToStageCoordinates(new Vector2(xOffset, yOffset));
    }

    public void draw(Stage world)
    {
        Batch batch = world.getBatch();
        batch.begin();

        AssetLoader.fontBig.setColor(0, 0, 0, 1);
        AssetLoader.fontMedium.setColor(0, 0, 0, 1);

        for (int i = 0; i < lines.length; i++)
            AssetLoader.fontMedium.draw(world.getBatch(), lines[i], positions[i].x, positions[i].y);
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
    }
    
    public interface MenuHandler
    {
        public void menuItemClicked(TanksGame game, int index);
    }
}
