package com.mobdeve.s11.grp9.david.tan.arcticflight.objects;

import android.graphics.Bitmap;

import com.mobdeve.s11.grp9.david.tan.arcticflight.R;
import com.mobdeve.s11.grp9.david.tan.arcticflight.utils.GameObject;
import com.mobdeve.s11.grp9.david.tan.arcticflight.structs.Sprite;
import com.mobdeve.s11.grp9.david.tan.arcticflight.structs.Vector2;

import java.util.ArrayList;

public class BackGround extends GameObject
{

    // Render
    private ArrayList<Bitmap> themeSprites = new ArrayList<Bitmap>();
    private int CurrentSpriteIndex;

    public BackGround(Vector2 position, Vector2 size)
    {
        super(position, size);

        // Initialize Sprite
        themeSprites.add(Sprite.loadSprite(R.drawable.background));
    }
}