package com.wernerapps.tanks.helpers.music;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Disposable;
import com.wernerapps.tanks.game.TanksGame;

/**
 * A service that manages the sound effects.
 */
public class SoundManager implements Disposable
{
    /**
     * The available sound files.
     */
    public enum SoundEffect
    {
        FIRE_STARTED("sounds/fire_started.mp3"),
        OIL_SOUND("sounds/oil_spill.mp3"),
        SHOT("sounds/howitzer_cannon_single_shot.mp3"), 
        TRACKS("sounds/tank_tracks.mp3"), 
        TANK_EXPLOSION("sounds/tank_explosion.mp3"),
        TANK_HIT("sounds/tank_hit.mp3");

        private final String fileName;

        private SoundEffect(String fileName)
        {
            this.fileName = fileName;
        }

        public String getFileName()
        {
            return fileName;
        }
    }

    /**
     * The volume to be set on the sound.
     */
    private float                              volume  = 1f;

    /**
     * Whether the sound is enabled.
     */
    private boolean                            enabled = true;

    /**
     * The sound cache.
     */
    private final HashMap<SoundEffect, Sound> soundCache;

    /**
     * Creates the sound manager.
     */
    public SoundManager()
    {
        soundCache = new HashMap<SoundManager.SoundEffect, Sound>(10);
        
        for (SoundEffect sound : SoundEffect.values())
            stopSound(play(sound));
    }

    /**
     * Plays the specified sound.
     */
    public long play(SoundEffect sound)
    {
        return play(sound, volume);
    }
    
    public long play(SoundEffect sound, float vol)
    {
        // check if the sound is enabled
        if (!enabled)
            return -1;

        // try and get the sound from the cache
        Sound soundToPlay = soundCache.get(sound);
        if (soundToPlay == null)
        {
            FileHandle soundFile = Gdx.files.internal(sound.getFileName());
            soundToPlay = Gdx.audio.newSound(soundFile);
            soundCache.put(sound, soundToPlay);
        }

        // play the sound
        return soundToPlay.play(vol);
    }

    /**
     * Sets the sound volume which must be inside the range [0,1].
     */
    public void setVolume(float volume)
    {
        Gdx.app.log(TanksGame.LOG, "Adjusting sound volume to: " + volume);

        // check and set the new volume
        if (volume < 0 || volume > 1f)
        {
            throw new IllegalArgumentException("The volume must be inside the range: [0,1]");
        }
        this.volume = volume;
    }

    /**
     * Enables or disabled the sound.
     */
    public void setEnabled(boolean enabled)
    {
        this.enabled = enabled;
    }

    public void stopSound(long soundId)
    {
        for (Sound sound : soundCache.values())
        {
            sound.stop(soundId);
        }
    }

    /**
     * Disposes the sound manager.
     */
    public void dispose()
    {
        Gdx.app.log(TanksGame.LOG, "Disposing sound manager");
        for (Sound sound : soundCache.values())
        {
            sound.stop();
            sound.dispose();
        }
    }
}
