package com.cyphercove.colorbenchmark

import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.ModifiedSpriteBatch
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.utils.Disposable

abstract class Benchmark : Disposable {
    companion object {
        const val ITEMS_PER_ROW = 2000
        const val ROWS = 1000
    }
    val camera = OrthographicCamera()
    var useOldBehaviorBatch = false
    var doColorMath = false
    private val currentBatch = SpriteBatch()
    private val oldBatch = ModifiedSpriteBatch()
    protected val batch = if (useOldBehaviorBatch) oldBatch else currentBatch

    val texture = Pixmap(1, 1, Pixmap.Format.RGBA8888).run {
        setColor(0xffffffff.toInt())
        fill()
        Texture(this).also {
            dispose()
        }
    }

    fun resize(width: Int, height: Int) {
        camera.apply {
            viewportWidth = width.toFloat()
            viewportHeight = height.toFloat()
            position.x = viewportWidth / 2
            position.y = viewportHeight / 2
            update()
        }
    }

    protected fun randomOpaqueColor() =
            (MathUtils.random(0, 255) shl 24) or
                    (MathUtils.random(0, 255) shl 16) or
                    (MathUtils.random(0, 255) shl 8) or
                    255


    abstract fun render()
}