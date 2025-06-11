package ch.hevs.gdx2d.hello

import ch.hevs.gdx2d.lib.GdxGraphics
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.Color

class Bar(private var posX: Int, private var posY: Int, private var width: Int, private var height: Int, private var color: Color) extends Drawable {
  private var moveLeft: Boolean = false // Move the bar to the left
  private var moveRight: Boolean = false // Move the bar to the right
  private var speedMult: Int = 20 // Bar speed
  private var isSizeBonusActive : Boolean = false // Bonus activator
  private var bonusSizeTimer : Int = 0 // Bonus time remaining

  // Update position
  def updateBar(): Unit = {
    if (moveLeft && posX > 0 + width/2)
      posX -= speedMult
    else if (moveRight && posX < 1920 - width/2)
      posX += speedMult
  }

  // Key pressed for the bar
  def onKeyDown(keycode: Int): Unit = {
    keycode match {
      case Input.Keys.LEFT => moveLeft = true
      case Input.Keys.RIGHT => moveRight = true
      case Input.Keys.SHIFT_LEFT => speedMult = 40
      case _ =>
    }
  }

  // Key unpressed for the bar
  def onKeyUp(keycode: Int): Unit = {
    keycode match {
      case Input.Keys.LEFT => moveLeft = false
      case Input.Keys.RIGHT => moveRight = false
      case Input.Keys.SHIFT_LEFT => speedMult = 20
      case _ =>
    }
  }

  // Default position of the bar
  def reset(): Unit = {
    posX = 1920 / 2
  }

  // Draw bar
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

  // Get height of the bar
  def getHeight() : Int = {
    height
  }

  // Get width of the bar
  def getWidth() : Int = {
    width
  }

  // Set width of the bar
  def setWidth(w : Int) = {
    width = w
  }

  // Bonus activation management
  def enableSizeBonus() : Unit = {
    if (!isSizeBonusActive) {
      setWidth(getWidth * 2) // Increase width of the bar
      isSizeBonusActive = true // Activate bonus
    }
    // Set time of bonus for 10 sec
    bonusSizeTimer = 10
  }

  // Bonus time management
  def bonusSizeBar() : Unit = {
    if (bonusSizeTimer > 0 && isSizeBonusActive) {
      bonusSizeTimer -= 1

      if (bonusSizeTimer == 0) {
        isSizeBonusActive = false
        setWidth(getWidth / 2) // Decrease width of the bar
      }
      println(s"Time left of the bonus : $bonusSizeTimer")
    }
  }
}