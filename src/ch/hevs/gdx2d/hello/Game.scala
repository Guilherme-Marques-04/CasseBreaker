package ch.hevs.gdx2d.hello

import ch.hevs.gdx2d.desktop.PortableApplication
import ch.hevs.gdx2d.lib.GdxGraphics
import com.badlogic.gdx.{Gdx, Input, InputAdapter}
import com.badlogic.gdx.graphics.Color

import scala.collection.mutable.ArrayBuffer

object Game {
  def main(args: Array[String]): Unit = {
    new Game
  }
}

class Game extends PortableApplication(1920, 1080) {

  private val window: Game = this

  private val bar = new Bar(1920/2, 100, 20, 250, Color.WHITE)
  private val block: ArrayBuffer[Block] = new Block(0, 0, 0, 0, Color.RED).generateBlocks()

  private val balls: ArrayBuffer[Ball] = new ArrayBuffer
  balls.addOne(new Ball(960, 540, 10, Color.RED))

  private val bonus: ArrayBuffer[Block] = new Bonus().generatePositionBonus(block)

  // timer declaration
  private val timer = new java.util.Timer()

  override def onInit(): Unit = {
    Gdx.input.setInputProcessor(new InputAdapter {
      override def keyDown(keycode: Int): Boolean = {
        bar.onKeyDown(keycode)

        if (keycode == Input.Keys.UP) {
          balls.head.startBall()
        }
        true
      }

      override def keyUp(keycode: Int): Boolean = {
        bar.onKeyUp(keycode)
        true
      }
    })

    // Timer instantiation
    val task = new java.util.TimerTask {
      def run(): Unit = {
        balls.foreach(ball => {
          ball.checkCollisionWithBar(bar)
          ball.checkCollisionWithBlocks(block)
          ball.updateBall(window.getWindowWidth, window.getWindowHeight, bar)
        })
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

    // draw all blocks
    block.toList.foreach(_.draw(g))

    // draw all balls
    balls.foreach(ball => ball.draw(g))

    //draw bonus block
    bonus.toList.foreach(_.draw(g))
  }
}