package ch.hevs.gdx2d.hello

import ch.hevs.gdx2d.lib.GdxGraphics
import com.badlogic.gdx.graphics.Color

import scala.collection.mutable.ArrayBuffer

class Ball(var ballX: Int, var ballY: Int, var radius: Int, var color: Color) extends Drawable {
  // First value:
  // X-axis displacement value
  //    Positive value for right displacement
  //    Negative value for left displacement
  // Second value:
  // Y-axis displacement value
  //    Positive value for up displacement
  //    Negative value for down displacement
  private var dirVector: (Int, Int) = (0, 1)

  private var launched: Boolean = false
  private var lost: Boolean = false

  // Ball displacement
  def updateBall(width: Int, height: Int, bar: Bar): Unit = {
    if (!launched) {
      ballX = bar.posX
      ballY = bar.posY + bar.height / 2 + radius + 1
      return
    }

    if (ballY - radius <= 0) {
      lost = true
      launched = false
      dirVector = (0, 0)
      return
    }

    //top side intersection
    if (ballY + radius >= height) {
      dirVector = (dirVector._1, -dirVector._2)
      ballY = height - radius - 1
    }

    //left side intersection
    if (ballX - radius <= 0) {
      dirVector = (-dirVector._1, dirVector._2)
      ballX = radius + 1
    }

    //right side intersection
    if (ballX + radius >= width) {
      dirVector = (-dirVector._1, dirVector._2)
      ballX = width - radius - 1
    }

    ballX += dirVector._1
    ballY += dirVector._2
  }

  //Launch the ball
  def startBall(): Unit = {
    if (!launched) {
      launched = true
      dirVector = (0, 1)
    }
  }

  //Check collision with the bar
  def checkCollisionWithBar(bar: Bar): Unit = {
    //calcul position ball
    val ballBottom: Int = ballY - radius
    val ballTop: Int = ballY + radius
    val ballLeft: Int = ballX - radius
    val ballRight: Int = ballX + radius

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
    // If the ball intersect the bar in X and Y and the dirVector is negative (going down)
    if (intersectX && intersectY && dirVector._2 < 0) {
      dirVector = (dirVector._1, -dirVector._2) // Change Y direction of the ball

      // By default 0 => the ball is going left
      var newVectorX: Int = 0

      println(s"${ballX - bar.posX} >= 0 && ${ballX - bar.posX} <= ${bar.width / 3}")

      //      if (bar.posX - ballX >= 0 && bar.posX - ballX < bar.width / 3) {
      //        newVectorX = -1
      //        println("Set newVectorX = -1\n")
      //      } else if (bar.posX - ballX >= (bar.posX + bar.width) / 3 && bar.posX - ballX < (bar.posX + bar.width) * 2 / 3) {
      //        newVectorX = 0
      //        println(s"$ballX > ${bar.posX} + ${bar.width} / 3} && $ballX <= (${bar.posX} + ${bar.width}) * 2 / 3")
      //        println("Set newVectorX = 0\n")
      //      } else if (ballX >= (bar.posX + bar.width) * 2 / 3 && ballX <= bar.posX + bar.width) {
      //        newVectorX = 1
      //        println(s"$ballX >= (${bar.posX} + ${bar.width}) * 2 / 3 && $ballX <= ${bar.posX} + ${bar.width}")
      //        println("Set newVectorX = 1\n")
      //      }

      val leftLimit = bar.posX + bar.width / 3
      val rightLimit = bar.posX + (bar.width * 2) / 3

      if (ballX >= bar.posX && ballX < leftLimit) {
        newVectorX = -1 // Gauche => envoie à gauche
        println("Zone gauche -> newVectorX = -1")
      } else if (ballX >= leftLimit && ballX < rightLimit) {
        newVectorX = 0 // Milieu => envoie tout droit
        println("Zone milieu -> newVectorX = 0")
      } else if (ballX >= rightLimit && ballX <= bar.posX + bar.width) {
        newVectorX = 1 // Droite => envoie à droite
        println("Zone droite -> newVectorX = 1")
      }





      //      val difference: Double = (ballX - bar.posX).toDouble / (bar.width / 2)
      //      val newVectorX: Int = (difference * 5).toInt

      //      //calcul the new position of the ball
      //      var correctedX: Int = newVectorX
      //      if (newVectorX == 0) {
      //        if (dirVector._1 >= 0) {
      //          correctedX = 1
      //        } else {
      //          correctedX = -1
      //        }
      //      }

      //change the direction of the ball
      dirVector = (newVectorX, dirVector._2)
      ballY = barTop + radius + 1
    }
  }

  //Check collision with blocks
  def checkCollisionWithBlocks(blocks: ArrayBuffer[Block]): Unit = {
    for (block <- blocks) {
      if (block.isEnable) {

        // Check collision with the block
        if (block.contains(ballX, ballY)) {
          block.isEnable = false
          block.color = Color.DARK_GRAY
          dirVector = (dirVector._1, -dirVector._2)
        }
      }
    }
  }

  override def draw(g: GdxGraphics): Unit = {
    g.drawFilledCircle(ballX, ballY, radius, color)
  }
}