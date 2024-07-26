package com.mobdeve.s11.grp9.david.tan.arcticflight.objects;

import com.mobdeve.s11.grp9.david.tan.arcticflight.R;
import com.mobdeve.s11.grp9.david.tan.arcticflight.utils.GameObject;
import com.mobdeve.s11.grp9.david.tan.arcticflight.structs.Sprite;
import com.mobdeve.s11.grp9.david.tan.arcticflight.structs.Vector2;

public class BaseGround extends GameObject
{
    /**
     * Constructs a new GameObject instance with the given position and size.
     *
     * @param position The top-left position of the game object
     * @param size     The size of the game object
     */
    public BaseGround(Vector2 position, Vector2 size)
    {
        super(position, size);

        // Initialize  Sprite
        sprite = Sprite.loadSprite(R.drawable.base);
    }

    public void cleanup() {
        // Recycle and nullify the sprite
        if (sprite != null) {
            sprite.recycle();
            sprite = null;
        }
    }

}