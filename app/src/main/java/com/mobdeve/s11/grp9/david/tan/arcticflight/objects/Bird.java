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
    private static final float FLAP_FORCE = 2.0f;

    // Render
    private static final int SPRITE_SIZE = 3;
    private static final int DAMAGE_BLINK_TIMES = 6;
    private ArrayList<ArrayList<Bitmap>> birdSprites;
    private Bitmap hurtSprite;  // Single hurt sprite
    private long lastAnimationTime;
    private int currentSpriteIndex;
    private int currentSpriteSetIndex = 0;

    /**
     * Constructs a new GameObject instance with the given position and size.
     *
     * @param position The top-left position of the game object
     * @param size     The size of the game object
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

        //tophat sprites
        ArrayList<Bitmap> tophat = new ArrayList<>();
        tophat.add(Sprite.loadSprite(R.drawable.hat_down));
        tophat.add(Sprite.loadSprite(R.drawable.hat_mid));
        tophat.add(Sprite.loadSprite(R.drawable.hat_up));
        birdSprites.add(tophat);

        //cap sprites
        ArrayList<Bitmap> cap = new ArrayList<>();
        cap.add(Sprite.loadSprite(R.drawable.cap_down));
        cap.add(Sprite.loadSprite(R.drawable.cap_mid));
        cap.add(Sprite.loadSprite(R.drawable.cap_up));
        birdSprites.add(cap);

        hurtSprite = Sprite.loadSprite(R.drawable.bird_midflap_hurt);

        //currentSpriteIndex = 0;
        sprite = birdSprites.get(0).get(0);
        spriteOffset = new Vector2(-18, -10);

        IsDead = false;
        setKinematic(false);
    }

    public void changeHat(int hatIndex) {
        Log.d("Bird", "Changing hat to index: " + hatIndex);
        currentSpriteSetIndex = hatIndex;
        currentSpriteIndex = 0; // Reset to the first sprite of the new set
        sprite = birdSprites.get(currentSpriteSetIndex).get(currentSpriteIndex);
    }

    public void applyAnimation(long frameRate) {
        long currentTime = System.currentTimeMillis();
        long elapsedTime = currentTime - lastAnimationTime;

        if (elapsedTime >= 1000 / frameRate) {
            // Change and Rotate Sprite
            currentSpriteIndex = (currentSpriteIndex + 1) % SPRITE_SIZE;
            setSprite(birdSprites.get(currentSpriteSetIndex).get(currentSpriteIndex), velocity.y * -15);
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
