package com.wernerapps.tanks.menu;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.wernerapps.tanks.game.TanksGame;
import com.wernerapps.tanks.gameobjects.Background;
import com.wernerapps.tanks.gameobjects.Tank;
import com.wernerapps.tanks.helpers.AssetLoader;
import com.wernerapps.tanks.menu.MenuUI.MenuHandler;
import com.wernerapps.tanks.screens.GameScreen;

public class MenuStage extends Stage
{
    private MenuUI    menuUI;
    private TanksGame game;

    public MenuStage(TanksGame game)
    {
        this.game = game;
        changeToMainUI();

        TextureRegion ground = AssetLoader.textureAtlas.get("dirt.png");
        int width = (int) getWidth() / ground.getRegionWidth();
        int height = (int) getHeight() / ground.getRegionHeight();

        for (int i = -(width / 2 + 1); i < width + (width / 2 + 1); i++)
        {
            for (int k = -(height / 2 + 1); k < height + (height / 2 + 2); k++)
            {
                Background image = new Background(ground);
                image.setPosition(image.getWidth() * i, image.getHeight() * k);
                addActor(image);
            }
        }

        addTank(1, 1, 50, 120, -30, -10);
        addTank(1, 3, 150, 100, -45, -60);
        addTank(1, 1, 175, 15, -70, -80);

        addTank(3, 1, getWidth() - 50, 120, 30, 10);
        addTank(3, 3, getWidth() - 150, 100, 45, 60);
        addTank(3, 1, getWidth() - 175, 15, 70, 80);
    }

    protected void changeToMainUI()
    {
        String[] lines = { "P v P", "P v CPU", "About" };
        String title = "Tanks for Nothing";
        menuUI = new MenuUI(this, lines, title, new MenuHandler()
        {
            @Override
            public void menuItemClicked(TanksGame game, int index)
            {
                if (index == 0)
                    changeToStageChoiceUI();
                else if (index == 1)
                    game.setScreen(new GameScreen(game, 1));
                else if (index == 2)
                    changeToAboutUI();

            }
        });
    }

    protected void changeToStageChoiceUI()
    {
        final String[] lines = { "No Man's Land", "Boxed In", "Dirtbags", "The Maze", "Another", "Custom 3" };
        String title = "P v P levels";
        String footer = "Back";
        menuUI = new MenuUI(this, lines, title, footer, new MenuHandler()
        {
            @Override
            public void menuItemClicked(TanksGame game, int index)
            {
                if (index == Integer.MAX_VALUE)
                    changeToMainUI();

            }
        });
    }

    protected void changeToAboutUI()
    {
        final String[] lines = { "Sound effects from http://www.freesfx.co.uk", "Art assets from http://kenney.nl/", "Programming by Tom Werner" };
        String title = "About";
        menuUI = new MenuUI(this, lines, title, "Back", new MenuHandler()
        {
            @Override
            public void menuItemClicked(TanksGame game, int index)
            {
                if (index == Integer.MAX_VALUE)
                    changeToMainUI();

            }
        });
    }

    private void addTank(int color, int health, float x, float y, float tankRotation, float barrelRotation)
    {
        Tank tank = new Tank(color, new Vector2(x, y), health);
        Image[] images = tank.getImages();
        images[0].rotateBy(tankRotation);
        images[1].rotateBy(barrelRotation);
        addActor(images[0]);
        addActor(images[1]);
        tank.updateImages();
    }

    @Override
    public void draw()
    {
        super.draw();
        menuUI.draw(this);
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button)
    {
        menuUI.checkForButtonClicked(game, this, screenX, screenY);

        return false;
    }

}
