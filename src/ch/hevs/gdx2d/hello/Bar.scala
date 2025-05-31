package ch.hevs.gdx2d.hello

import ch.hevs.gdx2d.lib.GdxGraphics
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.Color

class Bar(var posX: Int, var posY: Int, var height: Int, var width: Int, var color: Color) extends Drawable {
  val speed: Int = 20
  var moveLeft: Boolean = false
  var moveRight: Boolean = false

  //update position
  def updateBar(): Unit = {
    if (moveLeft && posX > 0 + width/2)
      posX -= speed
    else if (moveRight && posX < 1920 - width/2)
      posX += speed
  }

  def onKeyDown(keycode: Int): Unit = {
    keycode match {
      case Input.Keys.LEFT => moveLeft = true
      case Input.Keys.RIGHT => moveRight = true
      case _ =>
    }
  }


  def onKeyUp(keycode: Int): Unit = {
    keycode match {
      case Input.Keys.LEFT => moveLeft = false
      case Input.Keys.RIGHT => moveRight = false
      case _ =>
    }
  }

  //draw bar
  override def draw(g: GdxGraphics): Unit = {
    g.drawFilledRectangle(posX , posY, height, width, 90, color)
  }
}