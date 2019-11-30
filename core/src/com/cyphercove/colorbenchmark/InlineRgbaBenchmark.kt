package com.cyphercove.colorbenchmark

class InlineRgbaBenchmark: Benchmark() {
    class Item (val rgba: Rgba)

    val items = Array(ITEMS_PER_ROW){
        Item(Rgba(randomOpaqueColor()))
    }

    override fun render() {
        with (batch) {
            projectionMatrix = camera.combined
            begin()
            repeat(ROWS) { row ->
                for ((i, item) in items.withIndex()){
                    packedColor = if (doColorMath){
                        (item.rgba * 1.2f - Rgba(51, 51, 51, 0)).toFloatBits()
                    } else {
                        item.rgba.toFloatBits()
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