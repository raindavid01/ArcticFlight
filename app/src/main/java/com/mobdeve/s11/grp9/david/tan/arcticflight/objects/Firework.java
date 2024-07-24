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
    private final ArrayList<Bitmap> sprites = new ArrayList<>();
    private long lastAnimationTime;
    private int currentSpriteIndex;
    private boolean isActive;
    private MediaPlayer mediaPlayer;

    /**
     * Constructs a new GameObject instance with the given position and size.
     *
     * @param position The top-left position of the game object
     * @param size     The size of the game object
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

        currentSpriteIndex = 0;
        sprite = sprites.get(0);
        spriteOffset = new Vector2(-18, -10);

        isActive = false;
    }

    public void initializeSound(Context context) {
        mediaPlayer = MediaPlayer.create(context, R.raw.fireworks);
    }

    public void applyAnimation(long frameRate) {
        if (!isActive) return;

        long currentTime = System.currentTimeMillis();
        long elapsedTime = currentTime - lastAnimationTime;

        if (elapsedTime >= 1000 / frameRate) {
            // Change and Rotate Sprite
            currentSpriteIndex = (currentSpriteIndex + 1) % sprites.size();
            setSprite(sprites.get(currentSpriteIndex), 0);
            lastAnimationTime = currentTime;
        }
    }

    public void activate() {
        currentSpriteIndex = 1;
        isActive = true;
        if (mediaPlayer != null) {
            mediaPlayer.start();
        }
    }

    public void deactivate() {
        isActive = false;
        sprite = sprites.get(0);
        if (mediaPlayer != null) {
            mediaPlayer.pause();
            mediaPlayer.seekTo(0);
        }
    }
}
