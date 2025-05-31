package ch.hevs.gdx2d.hello

import ch.hevs.gdx2d.desktop.PortableApplication
import ch.hevs.gdx2d.lib.GdxGraphics
import com.badlogic.gdx.InputAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color

import scala.collection.mutable.ArrayBuffer

object Game {
  def main(args: Array[String]): Unit = {
    new Game
  }
}

class Game extends PortableApplication(1920, 1080) {

  var window : Game = this

  val bar = new Bar(800, 100, 20, 250, Color.WHITE)

  var balls: ArrayBuffer[Ball] = new ArrayBuffer
  balls.addOne(new Ball(960, 540, 10, Color.RED))

  // timer declaration
  var timer = new java.util.Timer()

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

    // Timer instantiation
    val task = new java.util.TimerTask {
      // Timer action at each interval
      def run() = {
        balls.foreach(ball => ball.updateBall(window.getWindowWidth, window.getWindowHeight))
      }
    }

    // timer
    timer.schedule(task, 2L, 2L)
  }

  override def onGraphicRender(g: GdxGraphics): Unit = {
    g.clear()
    g.drawFPS()

    bar.updateBar()
    bar.draw(g)

    // Draw all balls
    balls.foreach(ball => ball.draw(g))
  }
}