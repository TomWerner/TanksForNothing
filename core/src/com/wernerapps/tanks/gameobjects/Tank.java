package com.wernerapps.tanks.gameobjects;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.wernerapps.tanks.game.GameWorld;
import com.wernerapps.tanks.helpers.AnimatedActor;
import com.wernerapps.tanks.helpers.AnimationDrawable;
import com.wernerapps.tanks.helpers.AssetLoader;
import com.wernerapps.tanks.helpers.music.SoundManager.SoundEffect;

public class Tank implements Updateable, Placeable
{
    public static final int TANK_GREY   = 0;
    public static final int TANK_GREEN  = 1;
    public static final int TANK_ORANGE = 2;
    public static final int TANK_BLUE   = 3;
    public static final int TANK_BLACK  = 4;

    public enum TankState {
        TURNING, MOVING, SHOOTING, DONE, AIMING, TAKING_DAMAGE, GOAL_AIMING
    }

    public TankState   tankState             = TankState.DONE;

    private Vector2    position;
    private Vector2    velocity;
    private float      MAX_SPEED             = 100;
    private float      BASIC_PROJECTILE_FUEL = 3;
    private float      TURNING_SPEED         = 90;
    private int        tankColor;
    private boolean    bigTank               = false;
    private float      turretAngle           = 90;
    private float      bodyAngle             = 90;

    private TankBody   tankBody;
    private TankBarrel tankTurret;
    private Animation  shootingAnimation;
    private float      barrelOffset          = .2f;
    private Vector2    tankPositionGoal;
    private float      tankRotationGoal;
    private float      fuel;
    private boolean    barrelLeft;
    private boolean    barrelRight;
    private Circle     boundingCircle;
    private Image      healthOutline;
    private Image      healthImage;
    private float      MAX_HEALTH;
    private float      health;
    private Animation  shootingAnimation2;
    private float      tankBarrelRotationGoal;
    private long       shotDelay             = 1000;
    private long       lastShotTime;

    public Tank(int tankColor, Vector2 position, float health)
    {
        this.MAX_HEALTH = health;
        this.health = health;

        bigTank = health > 2;

        this.tankColor = tankColor;
        this.position = position;
        tankBody = new TankBody(AssetLoader.getTankBodyName(tankColor, bigTank));
        tankTurret = new TankBarrel(AssetLoader.getTankBarrelName(tankColor, bigTank));
        boundingCircle = new Circle(position, tankBody.getWidth() / 2);

        tankBody.setOrigin(tankBody.getWidth() / 2, tankBody.getHeight() / 2);
        tankTurret.setOrigin(tankTurret.getWidth() / 2, tankTurret.getHeight() * barrelOffset);

        shootingAnimation = AssetLoader.getShotAnimation(tankColor);
        shootingAnimation2 = AssetLoader.getShotAnimation(-1);

        healthOutline = new Image(AssetLoader.textureAtlas.get("outsideBar.png"));
        healthOutline.rotateBy(90);

        healthImage = new Image(AssetLoader.textureAtlas.get("insideBar.png"));
        healthImage.setScaleY(health / MAX_HEALTH);
        healthImage.rotateBy(90);

        updateImages();
    }

    @Override
    public boolean update(GameWorld world, float delta)
    {
        if (fuel <= 0 && (tankState.equals(TankState.MOVING) || tankState.equals(TankState.TURNING)))
        {
            System.out.println("Set to done from fuel < 0");
            tankState = TankState.DONE;
        }

        if (tankState.equals(TankState.MOVING))
        {
            if (Vector2.dst(position.x, position.y, tankPositionGoal.x, tankPositionGoal.y) > getMaxSpeed() * delta)
            {
                Vector2 temp = velocity.cpy().scl(delta);
                Circle boundsX = new Circle(getBounds());
                Circle boundsY = new Circle(getBounds());
                boundsX.x += temp.x;
                boundsY.y += temp.y;

                for (Obstacle obstacle : world.getLevel().getObstacles())
                {
                    if (Intersector.overlaps(boundsX, obstacle.getBounds()))
                        temp.x = 0;
                    if (Intersector.overlaps(boundsY, obstacle.getBounds()))
                        temp.y = 0;
                }

                position = position.add(temp);
                fuel -= delta;
            }
            else
            {
                tankState = TankState.DONE;
                System.out.println("Set to done from done moving");
            }
        }
        else if (tankState.equals(TankState.TURNING))
        {
            if (Math.abs(tankRotationGoal - bodyAngle) > TURNING_SPEED * delta)
            {
                float sign = Math.signum(tankRotationGoal - bodyAngle);
                tankBody.rotateBy(TURNING_SPEED * delta * sign);
                bodyAngle += TURNING_SPEED * delta * sign;
                tankTurret.rotateBy(TURNING_SPEED * delta * sign);
                turretAngle += TURNING_SPEED * delta * sign;
                fuel -= delta;
            }
            else
                tankState = TankState.MOVING;
        }
        else if (tankState.equals(TankState.GOAL_AIMING))
        {
            if (Math.abs(tankBarrelRotationGoal - turretAngle) > TURNING_SPEED * delta)
            {
                float sign = Math.signum(tankBarrelRotationGoal - turretAngle);
                tankTurret.rotateBy(TURNING_SPEED * delta * sign);
                turretAngle += TURNING_SPEED * delta * sign;
                fuel -= delta;
            }
            else
                tankState = TankState.SHOOTING;
        }
        else if (tankState.equals(TankState.AIMING))
        {
            float sign = 0;
            if (barrelLeft)
                sign = 1;
            else if (barrelRight)
                sign = -1;

            tankTurret.rotateBy(TURNING_SPEED * delta * sign);
            turretAngle += TURNING_SPEED * delta * sign;
        }
        else if (tankState.equals(TankState.TAKING_DAMAGE))
        {
            if (healthImage.getScaleY() > health / MAX_HEALTH)
                healthImage.setScaleY(healthImage.getScaleY() - delta);
            else
            {
                tankState = TankState.DONE;
                System.out.println("Set to done from done done taking damage");
                return health <= 0;
            }
        }

        updateImages();

        return false;
    }

    public void updateImages()
    {
        tankBody.setPosition(position.x - tankBody.getWidth() / 2, position.y);
        boundingCircle.setPosition(position.x, position.y + tankBody.getOriginY());
        tankTurret.setPosition(tankBody.getX() + tankBody.getOriginX() - tankTurret.getOriginX(), tankBody.getY()
                + tankBody.getOriginY() - tankTurret.getOriginX());

        healthImage
                .setPosition(position.x + healthImage.getHeight() / 2, position.y - healthOutline.getWidth() * 1.25f);
        healthOutline.setPosition(position.x + healthOutline.getHeight() / 2, position.y - healthOutline.getWidth()
                * 1.25f);
    }

    public void turnBarrelLeft(boolean turning)
    {
        barrelLeft = turning;
        tankState = TankState.AIMING;
    }

    public void turnBarrelRight(boolean turning)
    {
        barrelRight = turning;
        tankState = TankState.AIMING;
    }

    public boolean shoot(GameWorld world)
    {
        if (System.currentTimeMillis() - lastShotTime > shotDelay)
        {
            Vector2 newPos = position.cpy();
            newPos.y += tankBody.getOriginY();
            newPos.x += tankTurret.getHeight() * Math.cos(Math.toRadians(turretAngle));
            newPos.y += tankTurret.getHeight() * Math.sin(Math.toRadians(turretAngle));

            tankState = TankState.DONE;
            lastShotTime = System.currentTimeMillis();
            world.getGame().getSoundManager().play(SoundEffect.SHOT);
            Projectile proj = new Projectile(tankColor, bigTank, newPos, turretAngle, 200, BASIC_PROJECTILE_FUEL);

            world.addActor(proj);
            world.getLevel().getProjectiles().add(proj);
            AnimatedActor actor = world.getCurrentTank().getShootingAnimation();
            world.addActor(actor);
            world.getLevel().getAnimations().add(actor);

            return true; // If successful
        }
        return false;
    }

    public void setMoveGoal(Vector2 tankMoveGoal)
    {
        tankMoveGoal.y -= tankBody.getWidth() / 2;
        this.tankPositionGoal = tankMoveGoal;
        velocity = tankMoveGoal.cpy().sub(position).setLength(1).scl(getMaxSpeed());

        tankRotationGoal = (float) (Math.toDegrees(Math.atan2(velocity.y, velocity.x)));

        while (bodyAngle < 0)
            bodyAngle += 360;
        while (bodyAngle > 360)
            bodyAngle -= 360;

        if (tankRotationGoal - bodyAngle > 180)
            tankRotationGoal = 360 - tankRotationGoal;
        else if (tankRotationGoal - bodyAngle < -180)
            tankRotationGoal = tankRotationGoal + 360;
        tankState = TankState.TURNING;
    }

    public void setAimingGoal(float targetAngle)
    {
        this.tankBarrelRotationGoal = targetAngle;

        while (turretAngle < 0)
            turretAngle += 360;
        while (turretAngle > 360)
            turretAngle -= 360;

        if (tankBarrelRotationGoal - turretAngle > 180)
            tankBarrelRotationGoal = 360 - tankBarrelRotationGoal;
        else if (tankBarrelRotationGoal - turretAngle < -180)
            tankBarrelRotationGoal = tankBarrelRotationGoal + 360;
        tankState = TankState.GOAL_AIMING;
    }

    public void damage(float damage)
    {
        health = Math.max(0, health - damage);
        tankState = TankState.TAKING_DAMAGE;
    }

    @Override
    public void destroy(GameWorld world)
    {
        world.getGame().getSoundManager().play(SoundEffect.TANK_EXPLOSION);
        for (Image image : getImages())
            image.remove();

        for (AnimatedActor actor : getExplosions())
        {
            world.addActor(actor);
            world.getLevel().getAnimations().add(actor);
        }
    }

    /*
     * ------------------------------------------------------------------------
     * ------------------------------------------------------------------------
     * 
     * Filler methods
     * 
     * ------------------------------------------------------------------------
     * ------------------------------------------------------------------------
     */
    public void setFuel(float fuel)
    {
        this.fuel = fuel;
    }

    public float getFuel()
    {
        return fuel;
    }

    public Image[] getImages()
    {
        return new Image[] { tankBody, tankTurret, healthOutline, healthImage };
    }

    public Circle getBounds()
    {
        return boundingCircle;
    }

    public Vector2 getPosition()
    {
        return position;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.wernerapps.tanks.gameobjects.Placeable#setPosition(com.badlogic.gdx.math.Vector2)
     */
    @Override
    public void setPosition(Vector2 newPosition)
    {
        position = newPosition;
        updateImages();
    }

    public AnimatedActor getShootingAnimation()
    {
        Vector2 newPos = position.cpy();
        newPos.y += tankBody.getOriginY();
        newPos.x += tankTurret.getHeight() * (1 - barrelOffset) * Math.cos(Math.toRadians(turretAngle));
        newPos.y += tankTurret.getHeight() * (1 - barrelOffset) * Math.sin(Math.toRadians(turretAngle));
        newPos.x -= shootingAnimation.getKeyFrames()[0].getRegionWidth() / 2;
        newPos.y -= shootingAnimation.getKeyFrames()[0].getRegionHeight() / 2;

        AnimatedActor actor = new AnimatedActor(new AnimationDrawable(shootingAnimation));
        actor.setPosition(newPos.x, newPos.y);
        return actor;
    }

    public AnimatedActor[] getExplosions()
    {
        Vector2 newPos = position.cpy();
        newPos.y += tankBody.getOriginY();
        newPos.x -= shootingAnimation2.getKeyFrames()[0].getRegionWidth() / 2;
        newPos.y -= shootingAnimation2.getKeyFrames()[0].getRegionHeight() / 2;
        AnimatedActor[] explosions = new AnimatedActor[5];

        explosions[0] = new AnimatedActor(new AnimationDrawable(shootingAnimation2));
        explosions[0].setPosition(newPos.x, newPos.y);

        explosions[1] = new AnimatedActor(new AnimationDrawable(shootingAnimation2));
        explosions[1].setPosition(newPos.x - tankBody.getWidth() / 4, newPos.y - tankBody.getHeight() / 4);

        explosions[2] = new AnimatedActor(new AnimationDrawable(shootingAnimation2));
        explosions[2].setPosition(newPos.x + tankBody.getWidth() / 4, newPos.y - tankBody.getHeight() / 4);

        explosions[3] = new AnimatedActor(new AnimationDrawable(shootingAnimation2));
        explosions[3].setPosition(newPos.x - tankBody.getWidth() / 4, newPos.y + tankBody.getHeight() / 4);

        explosions[4] = new AnimatedActor(new AnimationDrawable(shootingAnimation2));
        explosions[4].setPosition(newPos.x + tankBody.getWidth() / 4, newPos.y + tankBody.getHeight() / 4);

        return explosions;
    }

    public float getMaxSpeed()
    {
        return MAX_SPEED;
    }

    public void setMaxSpeed(float newSpeed)
    {
        MAX_SPEED = newSpeed;
    }

    public float getHealth()
    {
        return health;
    }

    @Override
    public void rotateBy(float amount)
    {
        tankBody.rotateBy(amount);
        tankTurret.rotateBy(amount);
    }

    @Override
    public void placeObject(Stage stage, int startOfNonBackground)
    {
        for (int i = 0; i < getImages().length; i++)
        {
            stage.addActor(getImages()[i]);
            stage.getActors().insert(startOfNonBackground, stage.getActors().removeIndex(stage.getActors().size - 1));
            startOfNonBackground++;
        }
    }

    @Override
    public void removeObject()
    {
        for (Image image : getImages())
            image.remove();
    }

    @Override
    public float getRotation()
    {
        return tankBody.getRotation();
    }

}
