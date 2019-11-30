package com.badlogic.gdx.graphics.g2d

import com.badlogic.gdx.graphics.Color

class ModifiedSpriteBatch: SpriteBatch() {

    override fun setColor(tint: Color) {
        colorPacked = tint.toFloatBits()
    }

}