package com.mobdeve.s11.grp9.david.tan.arcticflight.objects;

import android.graphics.Bitmap;

import com.mobdeve.s11.grp9.david.tan.arcticflight.R;
import com.mobdeve.s11.grp9.david.tan.arcticflight.utils.GameObject;
import com.mobdeve.s11.grp9.david.tan.arcticflight.structs.Sprite;
import com.mobdeve.s11.grp9.david.tan.arcticflight.structs.Vector2;

import java.util.ArrayList;

public class BackGround extends GameObject {

    // List of theme sprites for the background
    private ArrayList<Bitmap> themeSprites = new ArrayList<Bitmap>();
    // Index of the current sprite
    private int CurrentSpriteIndex;

    /**
     * Constructor to initialize the background with position and size.
     *
     * @param position The position of the background.
     * @param size The size of the background.
     */
    public BackGround(Vector2 position, Vector2 size) {
        super(position, size);

        // Initialize and add the background sprite to the list
        themeSprites.add(Sprite.loadSprite(R.drawable.background));
    }

    /**
     * Cleanup method to recycle and clear bitmaps.
     */
    public void cleanup() {
        // Recycle and nullify bitmaps to free up memory
        for (Bitmap themeSprite : themeSprites) {
            if (themeSprite != null) {
                themeSprite.recycle();
            }
        }
        themeSprites.clear();
    }
}
