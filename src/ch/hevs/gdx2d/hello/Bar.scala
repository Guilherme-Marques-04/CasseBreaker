package ch.hevs.gdx2d.hello

import ch.hevs.gdx2d.lib.GdxGraphics
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.Color

class Bar(private var posX: Int, private var posY: Int, private var width: Int, private var height: Int, private var color: Color) extends Drawable {
  private var moveLeft: Boolean = false
  private var moveRight: Boolean = false
  private var speedMult: Int = 20
  private var bonusIncreaseSizeBarActivated : Boolean = false
  private var timeLeftOfBonusIncreaseSizeBar : Int = 0

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

  def reset(): Unit = {
    posX = 1920 / 2
  }

  //draw bar
  override def draw(g: GdxGraphics): Unit = {
    g.drawFilledRectangle(posX , posY, width, height, 0, color)
  }

  // Get the position X of the bar
  def getPosX() : Int = {
    posX
  }

  // Get the position Y of the bar
  def getPosY() : Int = {
    posY
  }

  def getHeight() : Int = {
    height
  }

  def getWidth() : Int = {
    width
  }

  def setWidth(w : Int) = {
    width = w
  }

  // Bonus section -------------------------------------------
  def activateBonusIncreaseSizeBar() : Unit = {
    if (!bonusIncreaseSizeBarActivated) {
      setWidth(getWidth * 2) // Increase width of the bar
      bonusIncreaseSizeBarActivated = true // Activate bonus
    }
    // Set time of bonus for 10 sec
    timeLeftOfBonusIncreaseSizeBar = 10
  }

  def bonusSizeBar() : Unit = {
    if (timeLeftOfBonusIncreaseSizeBar > 0 && bonusIncreaseSizeBarActivated) {
      timeLeftOfBonusIncreaseSizeBar -= 1

      if (timeLeftOfBonusIncreaseSizeBar == 0) {
        bonusIncreaseSizeBarActivated = false
        setWidth(getWidth / 2) // Decrease width of the bar
      }
      println(s"Time left of the bonus : $timeLeftOfBonusIncreaseSizeBar")
    }
  }
}