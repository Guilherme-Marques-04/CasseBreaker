package ch.hevs.gdx2d.hello

import ch.hevs.gdx2d.components.bitmaps.BitmapImage
import ch.hevs.gdx2d.desktop.PortableApplication
import ch.hevs.gdx2d.hello.Block.blocks
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

  private val bar = new Bar(1920 / 2, 100, 250, 20, Color.WHITE)
  private var vie: Int = 3
  private val bonus: Bonus = new Bonus()

  private var background: BitmapImage = _

  private val balls: ArrayBuffer[Ball] = new ArrayBuffer
  balls.addOne(new Ball(0, 0, 10, Color.RED))

  private var lives: Int = 3


  override def onInit(): Unit = {

    bonus.generatePositionBonus(blocks)

    background = new BitmapImage("data/images/spongebob.png")

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
  }

  override def onGraphicRender(g: GdxGraphics): Unit = {
    g.clear()
    g.drawFPS()
    g.drawPicture(g.getScreenWidth / 2, g.getScreenHeight / 2, background)


    bar.updateBar()
    bar.draw(g)

    //draw all blocks
    blocks.toList.foreach(_.draw(g))

    //draw all balls
    balls.foreach(ball => ball.draw(g))

    //Check if collisions
    if(lives > 0) {
      balls.foreach(ball => {
        ball.checkCollisionWithBar(bar)
        ball.checkCollisionWithBlocks(blocks,bar, bonus)
        ball.updateBall(window.getWindowWidth, window.getWindowHeight, bar)

        //Check if balls is lost
        if (ball.isLost() && lives > 0) {
          lives -= 1
          ball.reset(bar)
        }
      })
    }

    for(i <- 0 until lives){
      val x = 30 + i * 40
      val y = 1040
      g.drawFilledCircle(x,y,15,Color.RED)
    }
    if(lives <= 0) {
      g.drawStringCentered(g.getScreenHeight / 2, "Game Over")
    }
  }
}