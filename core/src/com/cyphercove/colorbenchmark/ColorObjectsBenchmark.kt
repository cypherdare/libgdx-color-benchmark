package com.cyphercove.colorbenchmark

import com.badlogic.gdx.graphics.Color

class ColorObjectsBenchmark: Benchmark() {
    class Item (val color: Color)
    val tempColor = Color()

    val items = Array(ITEMS_PER_ROW){
        Item(Color(randomOpaqueColor()))
    }

    override fun render() {
        with (batch) {
            projectionMatrix = camera.combined
            begin()
            repeat(ROWS) { row ->
                for ((i, item) in items.withIndex()){
                    packedColor = if (doColorMath){
                        tempColor.set(item.color).mul(1.2f).sub(0.2f, 0.2f, 0.2f, 0f).toFloatBits()
                    } else {
                        item.color.toFloatBits()
                    }
                    draw(texture, i.toFloat(), row.toFloat(), 1f, 1f)
                }
            }
            end()
        }
    }

    override fun dispose() {
        batch.dispose()
    }
}