package com.mobdeve.s11.grp9.david.tan.arcticflight.objects;

import android.graphics.Bitmap;

import com.mobdeve.s11.grp9.david.tan.arcticflight.R;
import com.mobdeve.s11.grp9.david.tan.arcticflight.utils.GameObject;
import com.mobdeve.s11.grp9.david.tan.arcticflight.structs.Sprite;
import com.mobdeve.s11.grp9.david.tan.arcticflight.structs.Vector2;

import java.util.ArrayList;
import java.util.Random;

public class Coin extends GameObject {
    // Logic
    private float[] range = new float[2];  // Y-axis range for random position

    // Render
    private final ArrayList<Bitmap> sprites = new ArrayList<>();  // List of coin sprites
    private long lastAnimationTime;  // Time of the last animation update
    private int currentSpriteIndex;  // Index of the current sprite in the animation cycle

    /**
     * Constructs a new Coin instance with the given position and size.
     *
     * @param position The top-left position of the coin
     * @param size     The size of the coin
     */
    public Coin(Vector2 position, Vector2 size) {
        super(position, size);

        // Initialize the range to default values
        range[0] = 0;
        range[1] = 0;

        // Initialize all sprites
        sprites.add(Sprite.loadSprite(R.drawable.coin0));
        sprites.add(Sprite.loadSprite(R.drawable.coin1));
        sprites.add(Sprite.loadSprite(R.drawable.coin2));
        sprites.add(Sprite.loadSprite(R.drawable.coin3));
        sprites.add(Sprite.loadSprite(R.drawable.coin4));
        sprites.add(Sprite.loadSprite(R.drawable.coin5));
        sprites.add(Sprite.loadSprite(R.drawable.coin6));
        sprites.add(Sprite.loadSprite(R.drawable.coin7));

        // Set the initial sprite and offset
        currentSpriteIndex = 0;
        sprite = sprites.get(0);
        spriteOffset = new Vector2(-8, -8);
    }

    /**
     * Applies animation to the coin based on the frame rate.
     *
     * @param frameRate The frame rate for the animation
     */
    public void applyAnimation(long frameRate) {
        long currentTime = System.currentTimeMillis();
        long elapsedTime = currentTime - lastAnimationTime;

        if (elapsedTime >= 1000 / frameRate) {
            // Change to the next sprite in the animation cycle
            currentSpriteIndex = (currentSpriteIndex + 1) % sprites.size();
            setSprite(sprites.get(currentSpriteIndex));
            lastAnimationTime = currentTime;
        }
    }

    /**
     * Sets the Y-axis range within which the coin can be positioned.
     *
     * @param lower The lower bound of the range
     * @param upper The upper bound of the range
     */
    public void boundYRange(float lower, float upper) {
        range[0] = lower;
        range[1] = upper;
    }

    /**
     * Randomizes the Y-axis position of the coin within the set range.
     */
    public void randomizeY() {
        Random random = new Random();
        float randomValue = random.nextFloat();
        float range = this.range[1] - this.range[0];
        position.y = this.range[0] + randomValue * range;
    }

    /**
     * Cleans up resources by recycling all bitmaps.
     */
    public void cleanup() {
        // Recycle and nullify bitmaps to free up memory
        for (Bitmap sprite : sprites) {
            if (sprite != null) {
                sprite.recycle();
            }
        }
        sprites.clear();
    }
}
