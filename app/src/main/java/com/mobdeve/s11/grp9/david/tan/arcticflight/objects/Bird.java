package com.mobdeve.s11.grp9.david.tan.arcticflight.objects;

import android.graphics.Bitmap;

import com.mobdeve.s11.grp9.david.tan.arcticflight.R;
import com.mobdeve.s11.grp9.david.tan.arcticflight.utils.GameObject;
import com.mobdeve.s11.grp9.david.tan.arcticflight.structs.Sprite;
import com.mobdeve.s11.grp9.david.tan.arcticflight.structs.Vector2;

import java.util.ArrayList;

public class Bird extends GameObject {
    // States
    public boolean IsDead;

    // Physics
    private static final float FLAP_FORCE = 2.0f;

    // Render
    private static final int SPRITE_SIZE = 3;
    private static final int DAMAGE_BLINK_TIMES = 6;
    private final ArrayList<Bitmap> sprites = new ArrayList<Bitmap>();
    private Bitmap hurtSprite;  // Single hurt sprite
    private long lastAnimationTime;
    private int currentSpriteIndex;

    /**
     * Constructs a new GameObject instance with the given position and size.
     *
     * @param position The top-left position of the game object
     * @param size     The size of the game object
     */
    public Bird(Vector2 position, Vector2 size) {
        super(position, size);

        // Initialize all sprites
        sprites.add(Sprite.loadSprite(R.drawable.bird_downflap));
        sprites.add(Sprite.loadSprite(R.drawable.bird_midflap));
        sprites.add(Sprite.loadSprite(R.drawable.bird_upflap));

        hurtSprite = Sprite.loadSprite(R.drawable.bird_midflap_hurt);

        currentSpriteIndex = 0;
        sprite = sprites.get(0);
        spriteOffset = new Vector2(-18, -10);

        IsDead = false;
        setKinematic(false);
    }

    public void applyAnimation(long frameRate) {
        long currentTime = System.currentTimeMillis();
        long elapsedTime = currentTime - lastAnimationTime;

        if (elapsedTime >= 1000 / frameRate) {
            // Change and Rotate Sprite
            currentSpriteIndex = (currentSpriteIndex + 1) % SPRITE_SIZE;
            setSprite(sprites.get(currentSpriteIndex), velocity.y * -15);
            lastAnimationTime = currentTime;
        }
    }

    public void flap() {
        if (position.y > 0) {
            velocity.y = FLAP_FORCE;
        }
    }

    public void showHurtSprite() {
        setSprite(hurtSprite, velocity.y * -15);
    }

    public void onDamage() {
        showHurtSprite();
        IsDead = true;
        setVelocity(new Vector2(getVelocity().x, 3.8f));
    }

    public void cleanup() {
        // Recycle and nullify bitmaps
        for (Bitmap sprite : sprites) {
            if (sprite != null) {
                sprite.recycle();
            }
        }
        sprites.clear();

        if (hurtSprite != null) {
            hurtSprite.recycle();
            hurtSprite = null;
        }
    }
}
