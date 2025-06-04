package ch.hevs.gdx2d.hello

import ch.hevs.gdx2d.lib.GdxGraphics
import com.badlogic.gdx.graphics.Color

import scala.collection.mutable.ArrayBuffer

class Ball(var posX: Int, var posY: Int, var radius: Int, var color: Color) extends Drawable {
  // First value:
  // X-axis displacement value
  //    Positive value for right displacement
  //    Negative value for left displacement
  // Second value:
  // Y-axis displacement value
  //    Positive value for up displacement
  //    Negative value for down displacement
  private var vector: (Int, Int) = (1, 1)

  private var launched: Boolean = false
  private var lost: Boolean = false

  // Ball displacement
  def updateBall(width: Int, height: Int, bar: Bar): Unit = {
    if (!launched) {
      posX = bar.posX
      posY = bar.posY + bar.height / 2 + radius + 1
      return
    }

    if (posY - radius <= 0) {
      lost = true
      launched = false
      vector = (0, 0)
      return
    }

    //top side intersection
    if (posY + radius >= height) {
      vector = (vector._1, -vector._2)
      posY = height - radius - 1
    }

    //left side intersection
    if (posX - radius <= 0) {
      vector = (-vector._1, vector._2)
      posX = radius + 1
    }

    //right side intersection
    if (posX + radius >= width) {
      vector = (-vector._1, vector._2)
      posX = width - radius - 1
    }

    posX += vector._1
    posY += vector._2
  }

  //Launch the ball
  def startBall(): Unit = {
    if (!launched) {
      launched = true
      vector = (0, 1)
    }
  }

  //Check collision with the bar
  def checkCollisionWithBar(bar: Bar): Unit = {
    //calcul position ball
    val ballBottom: Int = posY - radius
    val ballTop: Int = posY + radius
    val ballLeft: Int = posX - radius
    val ballRight: Int = posX + radius

    //position bar
    val barLeft: Int = bar.posX - bar.width / 2
    val barRight: Int = bar.posX + bar.width / 2
    val barTop: Int = bar.posY + bar.height / 2
    val barBottom: Int = bar.posY - bar.height / 2

    //check ball touch the side of the bar
    var intersectX: Boolean = false
    if (ballRight >= barLeft && ballLeft <= barRight) {
      intersectX = true
    }

    //check ball touch the other side of the bar
    var intersectY: Boolean = false
    if (ballBottom <= barTop && ballTop >= barBottom) {
      intersectY = true
    }

    //change the direction of the ball
    if (intersectX && intersectY && vector._2 < 0) {
      vector = (vector._1, -vector._2)

      val difference: Double = (posX - bar.posX).toDouble / (bar.width / 2)
      val newVectorX: Int = (difference * 5).toInt

      //calcul the new position of the ball
      var correctedX: Int = newVectorX
      if (newVectorX == 0) {
        if (vector._1 >= 0) {
          correctedX = 1
        } else {
          correctedX = -1
        }
      }

      //change the direction of the ball
      vector = (correctedX, vector._2)
      posY = barTop + radius + 1
    }
  }

  //Check collision with blocks ToDo
  def checkCollisionWithBlocks(block: ArrayBuffer[Block]): Unit = {
    val ballLeft: Int = posX - radius
    val ballRight: Int = posX + radius
    val ballTop: Int = posY + radius
    val ballBottom: Int = posY - radius

    val removeBlock = new ArrayBuffer[Block]()

    for(b <- block){
      val blockLeft: Int = b.posX - b.width
      val blockRight: Int = b.posX + b.width
      val blockTop: Int = b.posY + b.height
      val blockBottom: Int = b.posY - b.height
    }







  }

  override def draw(g: GdxGraphics): Unit = {
    g.drawFilledCircle(posX, posY, radius, color)
  }
}