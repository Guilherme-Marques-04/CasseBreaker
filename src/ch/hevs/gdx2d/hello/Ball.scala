package ch.hevs.gdx2d.hello

import ch.hevs.gdx2d.lib.GdxGraphics
import com.badlogic.gdx.graphics.Color

class Ball(var posX: Int, var posY: Int, var radius: Int, var color: Color) extends Drawable {
  // First value:
  // X-axis displacement value
  //    Positive value for right displacement
  //    Negative value for left displacement
  // Second value:
  // Y-axis displacement value
  //    Positive value for up displacement
  //    Negative value for down displacement
  var vector: (Int, Int) = (1, 1)

  // Ball displacement
  def updateBall(width: Int, height: Int): Unit = {
    // Buttom side intersection
    if (0 >= (posY - radius - vector._2)) {
      vector = (vector._1, -vector._2)
      posY = radius + vector._2
      println("Collision buttom")
    }

    // Top side intersection
    if (height <= (posY + radius + vector._2)) {
      vector = (vector._1, -vector._2)
      posY = height - radius - vector._2
      println("Collision top")
    }

    // Left side intersection
    if (0 >= (posX - radius - vector._1)) {
      vector = (-vector._1, vector._2)
      posX = radius + vector._1
      println("Collision left")
    }

    // Right side intersection
    if (width <= (posX + radius + vector._1)) {
      vector = (-vector._1, vector._2)
      posX = width - radius - vector._1
      println("Collision right")
    }

    // bar intersection ToDo

    // block intersection ToDO

    // displacement
    posX += vector._1
    posY += vector._2

    // Debug
    println(s"$posX  $posY")
  }

  override def draw(g: GdxGraphics): Unit = {
    g.drawFilledCircle(posX, posY, radius, color)
  }
}
