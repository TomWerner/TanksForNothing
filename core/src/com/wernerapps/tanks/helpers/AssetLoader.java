package com.wernerapps.tanks.helpers;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Rectangle;
import com.wernerapps.tanks.gameobjects.Tank;

public class AssetLoader
{

    public static Texture                        texture;
    public static TextureRegion                  bg, grass;

    public static Animation                      birdAnimation;
    public static TextureRegion                  bird, birdDown, birdUp;

    public static TextureRegion                  skullUp, skullDown, bar;
    public static HashMap<String, TextureRegion> textureAtlas;
    public static HashMap<String, Animation>     animationAtlas;
    public static BitmapFont                     fontBig;
    public static BitmapFont                     fontMedium;
    public static BitmapFont                     fontSmall;

    @SuppressWarnings("deprecation")
    public static void load()
    {
        texture = new Texture(Gdx.files.internal("topdowntanks/Spritesheet/sheet_tanks.png"));
        texture.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
        HashMap<String, Rectangle> coords = getTextureAtlasBounds();
        textureAtlas = new HashMap<String, TextureRegion>();

        for (String name : coords.keySet())
        {
            Rectangle rect = coords.get(name);
            TextureRegion region = new TextureRegion(texture, (int) rect.x, (int) rect.y, (int) rect.width,
                    (int) rect.height);
            textureAtlas.put(name, region);
        }
        Texture temp;

        temp = new Texture(Gdx.files.internal("assortedassets/laserRed14.png"));
        textureAtlas.put("fuelOutline.png", new TextureRegion(temp, 0, 0, temp.getWidth(), temp.getHeight()));
        textureAtlas.put("healthOutline.png", new TextureRegion(temp, 0, 0, temp.getWidth(), temp.getHeight()));

        temp = new Texture(Gdx.files.internal("assortedassets/laserRed12.png"));
        textureAtlas.put("fuel.png", new TextureRegion(temp, 0, 0, temp.getWidth(), temp.getHeight()));
        textureAtlas.put("health.png", new TextureRegion(temp, 0, 0, temp.getWidth(), temp.getHeight()));

        temp = new Texture(Gdx.files.internal("assortedassets/turnLeft.png"));
        textureAtlas.put("turnPanel.png", new TextureRegion(temp, 0, 0, temp.getWidth(), temp.getHeight()));

        temp = new Texture(Gdx.files.internal("assortedassets/target.png"));
        textureAtlas.put("shootPanel.png", new TextureRegion(temp, 0, 0, temp.getWidth(), temp.getHeight()));

        temp = new Texture(Gdx.files.internal("assortedassets/stop.png"));
        textureAtlas.put("donePanel.png", new TextureRegion(temp, 0, 0, temp.getWidth(), temp.getHeight()));
        
        temp = new Texture(Gdx.files.internal("assortedassets/flag1.png"));
        textureAtlas.put("flag1.png", new TextureRegion(temp, 0, 0, temp.getWidth(), temp.getHeight()));
        temp = new Texture(Gdx.files.internal("assortedassets/flag2.png"));
        textureAtlas.put("flag2.png", new TextureRegion(temp, 0, 0, temp.getWidth(), temp.getHeight()));

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/carbon.ttf"));
        fontBig = generator.generateFont(50);
        fontMedium = generator.generateFont(35);
        fontSmall = generator.generateFont(15);
        generator.dispose();

    }

    public static HashMap<String, Rectangle> getTextureAtlasBounds()
    {
        HashMap<String, Rectangle> result = new HashMap<String, Rectangle>();
        FileHandle file = Gdx.files.internal("topdowntanks/Spritesheet/sheet_tanks.xml");
        String[] lines = file.readString().split("\n");

        for (int i = 1; i < lines.length - 1; i++)
        {
            String line = lines[i];
            if (line.charAt(0) == '\t')
            {
                line = line.substring(13, line.length() - 2);
                String[] parts = line.split(" ");

                String name = parts[0].split("=")[1];
                name = name.substring(1, name.length() - 1);

                String xStr = parts[1].split("=")[1];
                int x = Integer.parseInt(xStr.substring(1, xStr.length() - 1));

                String yStr = parts[2].split("=")[1];
                int y = Integer.parseInt(yStr.substring(1, yStr.length() - 1));

                String widthStr = parts[3].split("=")[1];
                int width = Integer.parseInt(widthStr.substring(1, widthStr.length() - 1));

                String heightStr = parts[4].split("=")[1];
                int height = Integer.parseInt(heightStr.substring(1, heightStr.length() - 1));

                result.put(name, new Rectangle(x, y, width, height));
            }
        }
        return result;

    }

    public static void dispose()
    {
        // We must dispose of the texture when we are finished.
        texture.dispose();
    }

    public static TextureRegion getBulletByName(int color, boolean bigTank)
    {
        switch (color)
        {
        case Tank.TANK_GREY:
            if (!bigTank)
                return textureAtlas.get("bulletBeige.png");
            return textureAtlas.get("bulletBeige_outline.png");
        case Tank.TANK_GREEN:
            if (!bigTank)
                return textureAtlas.get("bulletGreen.png");
            return textureAtlas.get("bulletGreen_outline.png");
        case Tank.TANK_ORANGE:
            if (!bigTank)
                return textureAtlas.get("bulletRed.png");
            return textureAtlas.get("bulletRed_outline.png");
        case Tank.TANK_BLUE:
            if (!bigTank)
                return textureAtlas.get("bulletBlue.png");
            return textureAtlas.get("bulletBlue_outline.png");
        case Tank.TANK_BLACK:
            if (!bigTank)
                return textureAtlas.get("bulletSilver.png");
            return textureAtlas.get("bulletSilver_outline.png");
        default:
            if (!bigTank)
                return textureAtlas.get("bulletSilver.png");
            return textureAtlas.get("bulletSilver_outline.png");
        }
    }

    public static TextureRegion getTankBodyName(int tankColor, boolean bigTank)
    {
        switch (tankColor)
        {
        case Tank.TANK_GREY:
            if (bigTank)
                return textureAtlas.get("tankBeige_outline.png");
            return textureAtlas.get("tankBeige.png");
        case Tank.TANK_GREEN:
            if (bigTank)
                return textureAtlas.get("tankGreen_outline.png");
            return textureAtlas.get("tankGreen.png");
        case Tank.TANK_ORANGE:
            if (bigTank)
                return textureAtlas.get("tankRed_outline.png");
            return textureAtlas.get("tankRed.png");
        case Tank.TANK_BLUE:
            if (bigTank)
                return textureAtlas.get("tankBlue_outline.png");
            return textureAtlas.get("tankBlue.png");
        case Tank.TANK_BLACK:
            if (bigTank)
                return textureAtlas.get("tankBlack_outline.png");
            return textureAtlas.get("tankBlack.png");
        default:
            return textureAtlas.get("tankBlack.png");
        }
    }

    public static TextureRegion getTankBarrelName(int tankColor, boolean bigTank)
    {
        switch (tankColor)
        {
        case Tank.TANK_GREY:
            if (bigTank)
                return textureAtlas.get("barrelBeige_outline.png");
            return textureAtlas.get("barrelBeige.png");
        case Tank.TANK_GREEN:
            if (bigTank)
                return textureAtlas.get("barrelGreen_outline.png");
            return textureAtlas.get("barrelGreen.png");
        case Tank.TANK_ORANGE:
            if (bigTank)
                return textureAtlas.get("barrelRed_outline.png");
            return textureAtlas.get("barrelRed.png");
        case Tank.TANK_BLUE:
            if (bigTank)
                return textureAtlas.get("barrelBlue_outline.png");
            return textureAtlas.get("barrelBlue.png");
        case Tank.TANK_BLACK:
            if (bigTank)
                return textureAtlas.get("barrelBlack_outline.png");
            return textureAtlas.get("barrelBlack.png");
        default:
            return textureAtlas.get("barrelBlack.png");
        }
    }

    public static Animation getShotAnimation(int tankColor)
    {
        TextureRegion[] frames = new TextureRegion[3];

        String base = "smokeWhite";
        if (tankColor == -1)
            base = "smokeGrey";

        AssetLoader.textureAtlas.get(base + 1 + ".png").flip(true, true);
        frames[2] = AssetLoader.textureAtlas.get(base + 0 + ".png");
        frames[1] = AssetLoader.textureAtlas.get(base + 1 + ".png");
        frames[0] = AssetLoader.textureAtlas.get(base + 2 + ".png");

        Animation result = new Animation(0.10f, frames);
        result.setPlayMode(Animation.PlayMode.NORMAL);
        return result;
    }
    
    public static Animation getFlagAnimation()
    {
        TextureRegion[] frames = new TextureRegion[2];
        frames[1] = AssetLoader.textureAtlas.get("flag1.png");
        frames[0] = AssetLoader.textureAtlas.get("flag2.png");

        Animation result = new Animation(.4f, frames);
        result.setPlayMode(Animation.PlayMode.LOOP);
        return result;
    }

    public static Animation getFireAnimation()
    {
        TextureRegion[] frames = new TextureRegion[3];
        AssetLoader.textureAtlas.get("smokeOrange2.png").flip(true, true);

        frames[2] = AssetLoader.textureAtlas.get("smokeOrange1.png");
        frames[1] = AssetLoader.textureAtlas.get("smokeOrange2.png");
        frames[0] = AssetLoader.textureAtlas.get("smokeOrange3.png");

        Animation result = new Animation(.10f, frames);
        result.setPlayMode(Animation.PlayMode.NORMAL);
        return result;
    }
}