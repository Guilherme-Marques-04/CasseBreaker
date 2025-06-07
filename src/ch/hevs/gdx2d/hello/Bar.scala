package ch.hevs.gdx2d.hello

import ch.hevs.gdx2d.lib.GdxGraphics
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.Color

class Bar(var posX: Int, var posY: Int, var width: Int, var height: Int, var color: Color) extends Drawable {
  private var moveLeft: Boolean = false
  private var moveRight: Boolean = false
  private var speedMult: Int = 20

  //update position
  def updateBar(): Unit = {
    if (moveLeft && posX > 0 + width/2)
      posX -= speedMult
    else if (moveRight && posX < 1920 - width/2)
      posX += speedMult
  }

  //Key pressed for the bar
  def onKeyDown(keycode: Int): Unit = {
    keycode match {
      case Input.Keys.LEFT => moveLeft = true
      case Input.Keys.RIGHT => moveRight = true
      case Input.Keys.SHIFT_LEFT => speedMult = 40
      case _ =>
    }
  }

  //Key unpressed for the bar
  def onKeyUp(keycode: Int): Unit = {
    keycode match {
      case Input.Keys.LEFT => moveLeft = false
      case Input.Keys.RIGHT => moveRight = false
      case Input.Keys.SHIFT_LEFT => speedMult = 20
      case _ =>
    }
  }

  //draw bar
  override def draw(g: GdxGraphics): Unit = {
    g.drawFilledRectangle(posX , posY, width, height, 0, color)
  }
}