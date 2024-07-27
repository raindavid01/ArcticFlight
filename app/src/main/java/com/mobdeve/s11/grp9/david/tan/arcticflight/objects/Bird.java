package com.mobdeve.s11.grp9.david.tan.arcticflight.objects;

import android.graphics.Bitmap;
import android.util.Log;

import com.mobdeve.s11.grp9.david.tan.arcticflight.R;
import com.mobdeve.s11.grp9.david.tan.arcticflight.utils.GameObject;
import com.mobdeve.s11.grp9.david.tan.arcticflight.structs.Sprite;
import com.mobdeve.s11.grp9.david.tan.arcticflight.structs.Vector2;

import java.util.ArrayList;

public class Bird extends GameObject {
    // States
    public boolean IsDead;

    // Physics
    private static final float FLAP_FORCE = 2.0f;  // Force applied when the bird flaps

    // Render
    private static final int SPRITE_SIZE = 3;  // Number of sprites in an animation cycle
    private static final int DAMAGE_BLINK_TIMES = 6;  // Times to blink when damaged
    private ArrayList<ArrayList<Bitmap>> birdSprites;  // Sprites for different hats
    private Bitmap hurtSprite;  // Sprite to display when the bird is hurt
    private long lastAnimationTime;  // Last time the sprite animation was updated
    private int currentSpriteIndex;  // Index of the current sprite in the animation cycle
    private int currentSpriteSetIndex = 0;  // Index of the current sprite set (hat type)

    /**
     * Constructs a new Bird instance with the given position and size.
     *
     * @param position The top-left position of the bird
     * @param size     The size of the bird
     */
    public Bird(Vector2 position, Vector2 size) {
        super(position, size);

        birdSprites = new ArrayList<>();

        // Initialize all sprites
        // no hat sprites
        ArrayList<Bitmap> noHat = new ArrayList<>();
        noHat.add(Sprite.loadSprite(R.drawable.bird_downflap));
        noHat.add(Sprite.loadSprite(R.drawable.bird_midflap));
        noHat.add(Sprite.loadSprite(R.drawable.bird_upflap));
        birdSprites.add(noHat);

        // santa sprites
        ArrayList<Bitmap> santa = new ArrayList<>();
        santa.add(Sprite.loadSprite(R.drawable.santa_down));
        santa.add(Sprite.loadSprite(R.drawable.santa_mid));
        santa.add(Sprite.loadSprite(R.drawable.santa_mid));
        birdSprites.add(santa);

        // tophat sprites
        ArrayList<Bitmap> tophat = new ArrayList<>();
        tophat.add(Sprite.loadSprite(R.drawable.hat_down));
        tophat.add(Sprite.loadSprite(R.drawable.hat_mid));
        tophat.add(Sprite.loadSprite(R.drawable.hat_up));
        birdSprites.add(tophat);

        // cap sprites
        ArrayList<Bitmap> cap = new ArrayList<>();
        cap.add(Sprite.loadSprite(R.drawable.cap_down));
        cap.add(Sprite.loadSprite(R.drawable.cap_mid));
        cap.add(Sprite.loadSprite(R.drawable.cap_up));
        birdSprites.add(cap);

        // Load the hurt sprite
        hurtSprite = Sprite.loadSprite(R.drawable.bird_midflap_hurt);

        // Set the initial sprite
        sprite = birdSprites.get(0).get(0);
        spriteOffset = new Vector2(-18, -10);

        IsDead = false;
        setKinematic(false);  // Set the bird to be affected by physics
    }

    /**
     * Changes the hat of the bird by changing the sprite set.
     *
     * @param hatIndex The index of the hat to change to
     */
    public void changeHat(int hatIndex) {
        Log.d("Bird", "Changing hat to index: " + hatIndex);
        currentSpriteSetIndex = hatIndex;
        currentSpriteIndex = 0; // Reset to the first sprite of the new set
        sprite = birdSprites.get(currentSpriteSetIndex).get(currentSpriteIndex);
    }

    /**
     * Applies animation to the bird based on the frame rate.
     *
     * @param frameRate The frame rate for the animation
     */
    public void applyAnimation(long frameRate) {
        long currentTime = System.currentTimeMillis();
        long elapsedTime = currentTime - lastAnimationTime;

        if (elapsedTime >= 1000 / frameRate) {
            // Change and rotate sprite
            currentSpriteIndex = (currentSpriteIndex + 1) % SPRITE_SIZE;
            setSprite(birdSprites.get(currentSpriteSetIndex).get(currentSpriteIndex), velocity.y * -15);
            lastAnimationTime = currentTime;
        }
    }

    /**
     * Causes the bird to flap, applying an upward force.
     */
    public void flap() {
        if (position.y > 0) {
            velocity.y = FLAP_FORCE;
        }
    }

    /**
     * Shows the hurt sprite.
     */
    public void showHurtSprite() {
        setSprite(hurtSprite, velocity.y * -15);
    }

    /**
     * Handles the bird taking damage, showing the hurt sprite and setting its state to dead.
     */
    public void onDamage() {
        showHurtSprite();
        IsDead = true;
        setVelocity(new Vector2(getVelocity().x, 3.8f));
    }

    /**
     * Cleans up resources by recycling all bitmaps.
     */
    public void cleanup() {
        for (ArrayList<Bitmap> spriteSet : birdSprites) {
            for (Bitmap sprite : spriteSet) {
                if (sprite != null) {
                    sprite.recycle();
                }
            }
        }
        birdSprites.clear();

        if (hurtSprite != null) {
            hurtSprite.recycle();
            hurtSprite = null;
        }
    }
}
