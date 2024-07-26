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
    public static final float INTERVAL = GameConstants.SCREEN_WIDTH * 0.9f;
    public static final int GAP = (int) (GameConstants.SCREEN_HEIGHT / 3.3f);
    private static final float MOVE_PIPE_CHANCE = 0.1f;

    // Logic
    private final Random random;
    private final float[] range = new float[2];

    // Render
    public int spriteIndex;
    private final ArrayList<Bitmap> sprites = new ArrayList<>();
    private static final float fixed_speed = 0.7f;
    public boolean hasPassed;

    /**
     * Constructs a new GameObject instance with the given position and size.
     *
     * @param position The top-left position of the game object
     * @param size     The size of the game object
     */
    public Pipe(Vector2 position, Vector2 size) {
        super(position, size);

        // Initialize States
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

    public void logicUpdate() {
        // Parent Check
        Pipe parent = getParent();

        if (parent != null) {
            if (spriteIndex != parent.spriteIndex) {
                spriteIndex = parent.spriteIndex;
                sprite = sprites.get(spriteIndex);
            }
        }

        // Move Check
        if (parent == null && velocity.y != 0) {
            if (position.y < range[0] || position.y > range[1]) {
                velocity.y = -velocity.y;
            }
        }
    }

    public void flip() {
        sprites.replaceAll(bitmap -> Sprite.flipSprite(bitmap, 1));
        sprite = sprites.get(spriteIndex);
    }

    public void boundYRange(float lower, float upper) {
        range[0] = lower;
        range[1] = upper;
    }

    public void randomizeY() {
        // Follow Parent
        if (getParent() != null) {
            return;
        }

        float randomValue = random.nextFloat();
        float range = this.range[1] - this.range[0];

        position.y = this.range[0] + randomValue * range;
    }

    public void randomizeToggleMove() {
        // Follow Parent
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

    public void toggleMovePipe(boolean isMove) {
        // Follow Parent
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

        sprite = sprites.get(0); // Ensure only the existing sprite is used
    }

    public void reset() {
        hasPassed = false;
    }

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
