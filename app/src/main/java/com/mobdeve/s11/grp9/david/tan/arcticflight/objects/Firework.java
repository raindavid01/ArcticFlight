package com.mobdeve.s11.grp9.david.tan.arcticflight.objects;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaPlayer;

import com.mobdeve.s11.grp9.david.tan.arcticflight.R;
import com.mobdeve.s11.grp9.david.tan.arcticflight.utils.GameConstants;
import com.mobdeve.s11.grp9.david.tan.arcticflight.utils.GameObject;
import com.mobdeve.s11.grp9.david.tan.arcticflight.structs.Sprite;
import com.mobdeve.s11.grp9.david.tan.arcticflight.structs.Vector2;

import java.util.ArrayList;

public class Firework extends GameObject {

    // Render
    private final ArrayList<Bitmap> sprites = new ArrayList<>();  // List of firework sprites
    private long lastAnimationTime;  // Time of the last animation update
    private int currentSpriteIndex;  // Index of the current sprite in the animation cycle
    private boolean isActive;  // Indicates whether the firework is active
    private MediaPlayer mediaPlayer;  // MediaPlayer for the firework sound

    /**
     * Constructs a new Firework instance with the given position and size.
     *
     * @param position The top-left position of the game object.
     * @param size     The size of the game object.
     */
    public Firework(Vector2 position, Vector2 size) {
        super(position, size);

        // Initialize all sprites
        sprites.add(Sprite.loadSprite(R.drawable.firework0));
        sprites.add(Sprite.loadSprite(R.drawable.firework1));
        sprites.add(Sprite.loadSprite(R.drawable.firework2));
        sprites.add(Sprite.loadSprite(R.drawable.firework3));
        sprites.add(Sprite.loadSprite(R.drawable.firework4));
        sprites.add(Sprite.loadSprite(R.drawable.firework5));
        sprites.add(Sprite.loadSprite(R.drawable.firework6));
        sprites.add(Sprite.loadSprite(R.drawable.firework7));

        // Set initial sprite and offset
        currentSpriteIndex = 0;
        sprite = sprites.get(0);
        spriteOffset = new Vector2(-18, -10);

        // Firework is initially inactive
        isActive = false;
    }

    /**
     * Initializes the MediaPlayer for the firework sound.
     *
     * @param context The context for accessing resources.
     */
    public void initializeSound(Context context) {
        mediaPlayer = MediaPlayer.create(context, R.raw.fireworks);
    }

    /**
     * Applies animation to the firework based on the frame rate.
     *
     * @param frameRate The frame rate for the animation.
     */
    public void applyAnimation(long frameRate) {
        if (!isActive) return;

        long currentTime = System.currentTimeMillis();
        long elapsedTime = currentTime - lastAnimationTime;

        if (elapsedTime >= 1000 / frameRate) {
            // Change to the next sprite in the animation cycle
            currentSpriteIndex = (currentSpriteIndex + 1) % sprites.size();
            setSprite(sprites.get(currentSpriteIndex), 0);
            lastAnimationTime = currentTime;
        }
    }

    /**
     * Activates the firework and starts the animation and sound.
     */
    public void activate() {
        currentSpriteIndex = 1;
        isActive = true;
        if (mediaPlayer != null) {
            mediaPlayer.start();
        }
    }

    /**
     * Deactivates the firework and stops the animation and sound.
     */
    public void deactivate() {
        isActive = false;
        sprite = sprites.get(0);
        if (mediaPlayer != null) {
            mediaPlayer.pause();
            mediaPlayer.seekTo(0);
        }
    }

    /**
     * Cleans up resources by recycling bitmaps and releasing the MediaPlayer.
     */
    public void cleanup() {
        // Recycle and nullify bitmaps to free up memory
        for (Bitmap sprite : sprites) {
            if (sprite != null) {
                sprite.recycle();
            }
        }
        sprites.clear();

        // Release the MediaPlayer
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
