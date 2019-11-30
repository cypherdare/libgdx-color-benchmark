package com.cyphercove.colorbenchmark

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.math.WindowedMean
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.*
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener
import com.badlogic.gdx.utils.viewport.ExtendViewport

class MainGame: ApplicationAdapter() {

    private lateinit var skin: Skin
    private lateinit var uiStage: Stage
    private lateinit var frameTimeLabel: Label
    private lateinit var useOldBehaviorCheckBox: CheckBox
    private lateinit var doColorMathCheckBox: CheckBox
    private var lastFrameTime = 0L
    private val frameTime = WindowedMean(10)
    private lateinit var benchmark: Benchmark

    override fun create() {
        skin = Skin(Gdx.files.internal("uiskin.json"))
        uiStage = Stage(ExtendViewport(800f, 480f))
        Gdx.input.inputProcessor = uiStage

        val mainTable = Table().apply {
            setFillParent(true)
            uiStage.addActor(this)
            pad(20f)
        }
        val selections = arrayOf("Color objects", "Inline rgba")
        SelectBox<String>(skin).apply{
            setItems(*selections)
            selectedIndex = 0
            addListener(object: ChangeListener() {
                override fun changed(event: ChangeEvent?, actor: Actor?) {
                    benchmark.dispose()
                    setNewBenchmark(selectedIndex)
                }
            })
            mainTable.add(this).width(200f).space(10f).left().row()
        }
        useOldBehaviorCheckBox = CheckBox("Use old style SpriteBatch", skin).apply {
            isChecked = true
            addListener(object: ChangeListener() {
                override fun changed(event: ChangeEvent?, actor: Actor?) {
                    benchmark.useOldBehaviorBatch = isChecked
                }
            })
            mainTable.add(this).space(10f).left().row()
        }
        doColorMathCheckBox = CheckBox("Do color math", skin).apply {
            isChecked = true
            addListener(object: ChangeListener() {
                override fun changed(event: ChangeEvent?, actor: Actor?) {
                    benchmark.doColorMath = isChecked
                }
            })
            mainTable.add(this).space(10f).left().row()
        }
        frameTimeLabel = Label("", skin).apply {
            mainTable.add(this).space(10f).expand().left().top().row()
        }
        lastFrameTime = System.currentTimeMillis()
        setNewBenchmark(0)
    }

    private fun setNewBenchmark(index: Int){
        benchmark = when (index) {
            0 -> ColorObjectsBenchmark()
            else -> InlineRgbaBenchmark()
        }.apply {
            resize(Gdx.graphics.width, Gdx.graphics.height)
            useOldBehaviorBatch = useOldBehaviorCheckBox.isChecked
            doColorMath = doColorMathCheckBox.isChecked
        }
        frameTime.clear()
    }

    override fun render() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        val newTime = System.currentTimeMillis()
        frameTime.addValue((newTime - lastFrameTime).toFloat())
        lastFrameTime = newTime
        if (frameTime.hasEnoughData())
            frameTimeLabel.setText("Frame time: ${frameTime.mean} ms")

        benchmark.render()

        uiStage.act()
        uiStage.draw()
    }

    override fun pause() {
    }

    override fun resume() {
    }

    override fun resize(width: Int, height: Int) {
        uiStage.viewport.update(width, height, true)
        benchmark.resize(width, height)
    }

    override fun dispose() {
        skin.dispose()
        uiStage.dispose()
        benchmark.dispose()
    }

}