package ch.hevs.gdx2d.hello

import ch.hevs.gdx2d.desktop.PortableApplication
import ch.hevs.gdx2d.lib.GdxGraphics
import com.badlogic.gdx.InputAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color

object Game {
  def main(args: Array[String]): Unit = {
    new Game
  }
}

class Game extends PortableApplication(1920, 1080) {

  val bar = new Bar(800, 100, 20, 250, Color.WHITE)

  override def onInit(): Unit = {
    Gdx.input.setInputProcessor(new InputAdapter {
      override def keyDown(keycode: Int): Boolean = {
        bar.onKeyDown(keycode)
        true
      }

      override def keyUp(keycode: Int): Boolean = {
        bar.onKeyUp(keycode)
        true
      }
    })
  }

  override def onGraphicRender(g: GdxGraphics): Unit = {
    g.clear()
    g.drawFPS()

    bar.updateBar()
    bar.draw(g)
  }
}