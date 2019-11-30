package com.badlogic.gdx.graphics.g2d

import com.badlogic.gdx.graphics.Color

/** Doesn't back up the packed color with a Color object. */
class ModifiedSpriteBatch: SpriteBatch() {

    override fun setColor(tint: Color) {
        colorPacked = tint.toFloatBits()
    }

}