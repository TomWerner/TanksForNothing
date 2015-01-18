package com.wernerapps.tanks.game;

import java.util.ArrayList;
import java.util.Arrays;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.wernerapps.tanks.gameobjects.NextLevelUI;
import com.wernerapps.tanks.gameobjects.OrderFactory;
import com.wernerapps.tanks.gameobjects.Tank;
import com.wernerapps.tanks.gameobjects.Tank.TankState;
import com.wernerapps.tanks.gameobjects.TankTeam;
import com.wernerapps.tanks.gameobjects.Updateable;
import com.wernerapps.tanks.helpers.AssetLoader;
import com.wernerapps.tanks.helpers.music.SoundManager.SoundEffect;
import com.wernerapps.tanks.levels.FileLevel;
import com.wernerapps.tanks.levels.Level;
import com.wernerapps.tanks.levels.Level.LevelState;
import com.wernerapps.tanks.players.TeamController;

public class GameWorld extends Stage
{
    public enum GameState {
        ANIMATIONS, MOVING_TURN, SHOOTING_TURN, SCROLLING, LEVEL_DONE, COMP_MOVING_TURN, COMP_SHOOTING_TURN
    }

    // Scrolling starts by changing teams
    public int               currentTeam      = -1;
    public int               winningTeam      = -1;

    private WorldController  controller;
    private CameraController cameraController = new CameraController(200);

    private Tank             currentTank;
    public TeamController    currentController;

    // UI stuff
    public Image             fuelOutline;
    public Image             fuel;
    public Image             donePanel;
    public Image             turnLeft;
    public Image             turnRight;
    public Image             shootPanel;
    public Image             pauseButton;
    public NextLevelUI       nextLevelUI;

    private Rectangle        gameBounds;
    private Level            level;
    private int              levelNum;

    public boolean           paused;

    private TanksGame        game;
    private boolean          gameStarted      = false;
    private long             tracksSound      = -1;

    public GameWorld(TanksGame game, int targetLevelId)
    {
        this.game = game;
        this.levelNum = targetLevelId; // TODO: CHANGE THIS back to targetLevelId
        this.controller = new WorldController();
        this.game.getSoundManager().setVolume(.5f);
    }

    @Override
    public boolean keyDown(int keycode)
    {
        controller.setKeyDown(keycode);
        paused = false;
        return true;
    }

    @Override
    public boolean keyUp(int keycode)
    {
        controller.setKeyUp(keycode);
        return true;
    }

    public boolean touchDown(int screenX, int screenY, int pointer, int button)
    {
        if (!paused)
        {
            if (screenX > getWidth() - pauseButton.getWidth()
                    && (getHeight() - screenY) > getHeight() - pauseButton.getHeight())
                paused = true;
            if (getLevel().getState().getGameState().equals(GameState.LEVEL_DONE))
                executeLevelDoneTouchDown(screenX, screenY, pointer, button);
            else if (currentController != null)
                currentController.handleTouchDown(this, screenX, screenY, pointer, button);
        }
        else
            paused = false;

        return true;
    }

    private void executeLevelDoneTouchDown(int screenX, int screenY, int pointer, int button)
    {
        if (nextLevelUI != null)
            nextLevelUI.checkForButtonClicked(this, screenX, screenY);
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button)
    {
        if (!paused && currentController != null)
            currentController.handleTouchUp(this, screenX, screenY, pointer, button);

        return true;
    }

    public void pause()
    {
        paused = true;
    }

    public void act(float delta)
    {
        super.act(delta);

        if (!gameStarted)
        {
            setupLevel(levelNum);
            gameStarted = true;
        }

        handleTankSounds();

        if (controller.isKeyDown(Keys.P))
            paused = true;
        if (!paused)
        {
            updateUI();
            updateObjects(delta);

            if (currentController != null)
                currentController.act(this, delta);

            switch (level.getState().getGameState())
            {
            case ANIMATIONS:
                if (level.getProjectiles().size() > 0)
                    getCameraController().setCameraPosition(getCamera(), level.getProjectiles().get(0).getPosition());
                break;
            case MOVING_TURN:
            case COMP_MOVING_TURN:
                executeMovingTurnAct(delta);
                break;
            case SCROLLING:
                getCameraController().scrollCamera(getCamera(), delta);
                break;
            case SHOOTING_TURN:
                break;
            case LEVEL_DONE:
                break;
            default:
                break;
            }
            if (level.getState().isDone(this, delta))
                level.advanceState(this);

            getCameraController().boundCamera(getCamera(), gameBounds);
            level.update(this, delta);
        }
    }

    private void handleTankSounds()
    {
        if (getCurrentTank() != null)
        {
            if (tracksSound == -1
                    && (getCurrentTank().tankState.equals(TankState.MOVING) || getCurrentTank().tankState
                            .equals(TankState.TURNING)))
            {
                tracksSound = game.getSoundManager().play(SoundEffect.TRACKS);
            }
            else if (tracksSound != -1 && !getCurrentTank().tankState.equals(TankState.MOVING)
                    && !getCurrentTank().tankState.equals(TankState.TURNING))
            {
                game.getSoundManager().stopSound(tracksSound);
                tracksSound = -1;
            }
        }
    }

    public void draw()
    {
        if (paused)
        {
            drawPaused();
            return;
        }

        super.draw();
        // ShapeRenderer render = new ShapeRenderer();
        // render.begin(ShapeType.Line);
        // render.setColor(Color.RED);
        // for (Projectile proj : level.getProjectiles())
        // {
        // Circle us = proj.getBounds();
        // Vector2 screen = stageToScreenCoordinates(new Vector2(us.x, us.y));
        // render.circle(screen.x, getHeight() - screen.y, us.radius);
        // }
        // for (TankTeam team : level.getTeams())
        // {
        // for (Tank tank : team.getTanks())
        // {
        // Circle us = tank.getBounds();
        // Vector2 screen = stageToScreenCoordinates(new Vector2(us.x, us.y));
        // render.circle(screen.x, getHeight() - screen.y, us.radius);
        // }
        // }
        // for (Obstacle obstacle : level.getObstacles())
        // {
        // Circle us = obstacle.getBounds();
        // Vector2 screen = stageToScreenCoordinates(new Vector2(us.x, us.y));
        // render.circle(screen.x, getHeight() - screen.y, us.radius);
        // }
        // render.end();

        if (level.getState().getGameState().equals(GameState.LEVEL_DONE) && !paused)
        {
            nextLevelUI.draw(this);
        }
        else if (!level.getLevelState().equals(LevelState.PLAYING))
        {
            Batch batch = getBatch();
            batch.begin();
            AssetLoader.fontBig.setColor(Color.BLACK);
            level.recalculateMessagePosition(this);
            AssetLoader.fontBig.draw(getBatch(), level.getMessage(), level.getMessagePosition().x,
                    level.getMessagePosition().y);
            AssetLoader.fontBig.draw(getBatch(), level.getLevelState().toString(), level.getTimerPosition().x,
                    level.getTimerPosition().y);

            batch.end();
        }

        if (controller.isKeyDown(Keys.LEFT))
            getCamera().position.x -= 5;
        if (controller.isKeyDown(Keys.RIGHT))
            getCamera().position.x += 5;
        if (controller.isKeyDown(Keys.DOWN))
            getCamera().position.y -= 5;
        if (controller.isKeyDown(Keys.UP))
            getCamera().position.y += 5;
    }

    private void drawPaused()
    {
        getBatch().begin();
        AssetLoader.fontBig.setColor(1, 1, 1, 1);
        String string = "Paused";
        TextBounds bounds = AssetLoader.fontBig.getBounds(string);
        Vector2 position = screenToStageCoordinates(new Vector2(getWidth() / 2 - bounds.width / 2, getHeight() / 2
                - bounds.height / 2));
        AssetLoader.fontBig.draw(getBatch(), string, position.x, position.y);
        getBatch().end();
    }

    private void executeMovingTurnAct(float delta)
    {
        if (getCurrentTank().tankState.equals(TankState.MOVING))
            getCameraController().setCameraPosition(getCamera(), getCurrentTank().getPosition());
        else if (getCurrentTank().getFuel() <= 0)
            level.advanceState(this);
    }

    public void stopAllTanks()
    {
        for (TankTeam team : level.getTeams())
            for (Tank tank : team.getTanks())
                tank.tankState = TankState.DONE;
    }

    public void showLevelOverUI(NextLevelUI nextLevelUI)
    {
        showScrollingUI(); // Turn everything off
        this.nextLevelUI = nextLevelUI;
    }

    public void showScrollingUI()
    {
        turnLeft.setVisible(false);
        turnRight.setVisible(false);
        shootPanel.setVisible(false);
        donePanel.setVisible(false);
        fuel.setVisible(false);
        fuelOutline.setVisible(false);
    }

    public void showShootingTurnUI()
    {
        getCameraController().setCameraVelocity(new Vector2(0, 0));
        turnLeft.setVisible(true);
        turnRight.setVisible(true);
        shootPanel.setVisible(true);
        donePanel.setVisible(false);
        fuel.setVisible(false);
        fuelOutline.setVisible(false);
    }

    public void showMovingTurnUI()
    {
        turnLeft.setVisible(false);
        turnRight.setVisible(false);
        shootPanel.setVisible(false);
        fuelOutline.setVisible(true);
        fuel.setVisible(true);
        donePanel.setVisible(true);
    }

    public void changeTeams()
    {
        if (currentTeam >= 0 && currentTeam < level.getTeams().size())
            level.getTeams().get(currentTeam).endTurn();
        currentTeam = (currentTeam + 1) % level.getTeams().size();
        setCurrentTank(level.getTeams().get(currentTeam).getTank());

        if (getCurrentTank() != null)
            getCurrentTank().setFuel(level.getMaxFuelSeconds());
        else
            winningTeam = (currentTeam + 1) % level.getTeams().size();

        currentController = level.getTeams().get(currentTeam).getController();
    }

    private Vector2 getUICoords()
    {
        float x = getCamera().position.x - getWidth() / 2;
        float y = getCamera().position.y + getHeight() / 2;
        return new Vector2(x, y);
    }

    private void updateUI()
    {
        if (getCurrentTank() == null)
            return;
        Vector2 pos = getUICoords();
        fuel.setPosition(pos.x + fuelOutline.getWidth() + getWidth() / 4,
                pos.y + fuel.getWidth() / 2 - fuelOutline.getHeight());
        fuel.setScaleY(3 * getCurrentTank().getFuel() / level.getMaxFuelSeconds());
        fuelOutline.setPosition(pos.x + fuelOutline.getWidth() + getWidth() / 4, pos.y - fuelOutline.getHeight());
        donePanel.setPosition(pos.x + getWidth() - donePanel.getWidth(), pos.y - getHeight());

        turnLeft.setPosition(pos.x, pos.y - getHeight());
        turnRight.setPosition(pos.x + getWidth() - turnRight.getWidth(), pos.y - getHeight());

        shootPanel.setPosition(pos.x + getWidth() / 2 - shootPanel.getWidth() / 2, pos.y - getHeight());
        pauseButton.setPosition(pos.x + getWidth() - pauseButton.getWidth(), pos.y - pauseButton.getHeight());
    }

    private void updateObjects(float delta)
    {
        for (TankTeam team : level.getTeams())
            team.updateTanks(this, delta);

        updateUpdatable(level.getProjectiles(), delta);
        updateUpdatable(level.getObstacles(), delta);
        updateUpdatable(level.getAnimations(), delta);
    }

    protected void updateUpdatable(ArrayList<? extends Updateable> updatables, float delta)
    {
        for (int i = 0; i < updatables.size();)
        {
            Updateable updatable = updatables.get(i);
            if (!updatable.update(this, delta))
                i++;
            else
                updatables.remove(i).destroy(this);
        }
    }

    public CameraController getCameraController()
    {
        return cameraController;
    }

    public void setCameraController(CameraController cameraController)
    {
        this.cameraController = cameraController;
    }

    public Level getLevel()
    {
        return level;
    }

    public void nextLevel()
    {
        levelNum++;
        gameStarted = false;
    }

    public void replayLevel()
    {
        gameStarted = false;
    }

    public void setupLevel(int levelNum)
    {
        getActors().clear();
        level = game.getLevelManager().getLevel(levelNum);
        if (level instanceof FileLevel)
            level = new FileLevel(((FileLevel) level).getFilename());
        gameBounds = level.buildLevel(this);
        // Subtract one because we increment to start
        currentTeam = level.getStartingTeam() - 1;
        winningTeam = -1;

        level.getState().onStateStart(this);
    }

    public TanksGame getGame()
    {
        return game;
    }

    public Tank getCurrentTank()
    {
        return currentTank;
    }

    public void setCurrentTank(Tank currentTank)
    {
        this.currentTank = currentTank;
    }

    @Override
    public void addActor(Actor actor)
    {
        super.addActor(actor);
        Arrays.sort(getActors().items, OrderFactory.getComparator());
    }

    public int getLevelNumber()
    {
        return levelNum;
    }
}