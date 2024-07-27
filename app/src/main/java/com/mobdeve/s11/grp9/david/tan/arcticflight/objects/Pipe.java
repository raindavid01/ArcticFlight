package com.mobdeve.s11.grp9.david.tan.arcticflight.objects;

import android.graphics.Bitmap;
import android.util.Log;

import com.mobdeve.s11.grp9.david.tan.arcticflight.R;
import com.mobdeve.s11.grp9.david.tan.arcticflight.utils.GameConstants;
import com.mobdeve.s11.grp9.david.tan.arcticflight.utils.GameObject;
import com.mobdeve.s11.grp9.david.tan.arcticflight.structs.Sprite;
import com.mobdeve.s11.grp9.david.tan.arcticflight.structs.Vector2;

import java.util.ArrayList;
import java.util.Random;

public class Pipe extends GameObject {
    // Constants
    public static final float INTERVAL = GameConstants.SCREEN_WIDTH * 0.9f;  // Interval between pipes
    public static final int GAP = (int) (GameConstants.SCREEN_HEIGHT / 3.3f);  // Gap between the upper and lower pipes
    private static final float MOVE_PIPE_CHANCE = 0.1f;  // Probability of a pipe moving up and down

    // Logic
    private final Random random;  // Random number generator
    private final float[] range = new float[2];  // Range for Y-axis movement

    // Render
    public int spriteIndex;  // Index of the current sprite
    private final ArrayList<Bitmap> sprites = new ArrayList<>();  // List of sprites for the pipe
    private static final float fixed_speed = 0.7f;  // Fixed speed for the pipe movement
    public boolean hasPassed;  // Flag to check if the pipe has been passed by the bird

    /**
     * Constructs a new Pipe instance with the given position and size.
     *
     * @param position The top-left position of the game object.
     * @param size     The size of the game object.
     */
    public Pipe(Vector2 position, Vector2 size) {
        super(position, size);

        // Initialize states
        range[0] = 0;
        range[1] = 0;
        random = new Random();

        // Initialize all sprites
        sprites.add(Sprite.loadSprite(R.drawable.pipe));

        sprite = sprites.get(0);
        spriteIndex = 0;

        hasPassed = false;

        Log.d("Pipe", "Pipe initialized with " + sprites.size() + " sprite(s).");
    }

    /**
     * Updates the logic of the pipe.
     */
    public void logicUpdate() {
        // Check for parent pipe
        Pipe parent = getParent();

        if (parent != null) {
            if (spriteIndex != parent.spriteIndex) {
                spriteIndex = parent.spriteIndex;
                sprite = sprites.get(spriteIndex);
            }
        }

        // Check for movement
        if (parent == null && velocity.y != 0) {
            if (position.y < range[0] || position.y > range[1]) {
                velocity.y = -velocity.y;
            }
        }
    }

    /**
     * Flips the sprite vertically.
     */
    public void flip() {
        sprites.replaceAll(bitmap -> Sprite.flipSprite(bitmap, 1));
        sprite = sprites.get(spriteIndex);
    }

    /**
     * Sets the Y-axis range for the pipe movement.
     *
     * @param lower The lower bound.
     * @param upper The upper bound.
     */
    public void boundYRange(float lower, float upper) {
        range[0] = lower;
        range[1] = upper;
    }

    /**
     * Randomizes the Y position of the pipe within the set range.
     */
    public void randomizeY() {
        // Follow parent if exists
        if (getParent() != null) {
            return;
        }

        float randomValue = random.nextFloat();
        float range = this.range[1] - this.range[0];

        position.y = this.range[0] + randomValue * range;
    }

    /**
     * Randomizes whether the pipe will move up and down.
     */
    public void randomizeToggleMove() {
        // Follow parent if exists
        if (getParent() != null) {
            return;
        }

        float compareIndex = random.nextFloat();

        if (compareIndex < MOVE_PIPE_CHANCE) {
            toggleMovePipe(true);
        } else {
            toggleMovePipe(false);
        }
    }

    /**
     * Toggles the movement of the pipe.
     *
     * @param isMove True if the pipe should move, false otherwise.
     */
    public void toggleMovePipe(boolean isMove) {
        // Follow parent if exists
        if (getParent() != null) {
            return;
        }

        if (isMove) {
            Log.d("Pipe", "Moving pipe");
            velocity.y = fixed_speed / (float) (Math.random() * 2.5f + 10f * (fixed_speed - 0.09f));
            if (random.nextInt(2) == 1) {
                velocity.y = -velocity.y;
            }
        } else {
            Log.d("Pipe", "Static pipe");
            velocity.y = 0;
        }

        sprite = sprites.get(0);  // Ensure only the existing sprite is used
    }

    /**
     * Resets the pipe's state.
     */
    public void reset() {
        hasPassed = false;
    }

    /**
     * Cleans up resources by recycling bitmaps.
     */
    public void cleanup() {
        // Recycle and nullify bitmaps
        for (Bitmap sprite : sprites) {
            if (sprite != null) {
                sprite.recycle();
            }
        }
        sprites.clear();
    }
}
