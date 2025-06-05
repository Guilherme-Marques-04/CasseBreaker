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
      dirVector = (0, 10)
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

      val barDiv: Int = bar.width/3
      val barLeftStart: Int = bar.posX - bar.width/2
      val barRightFinish: Int = bar.posX + bar.width/2
      val leftLimit = bar.posX - barDiv/2
      val rightLimit = bar.posX + barDiv/2

      if (ballX >= barLeftStart && ballX < leftLimit) {
        newVectorX = -5 // Gauche => envoie à gauche
      } else if (ballX >= leftLimit && ballX < rightLimit) {
        newVectorX = 0 // Milieu => envoie tout droit
      } else if (ballX >= rightLimit && ballX <= barRightFinish) {
        newVectorX = 5 // Droite => envoie à droite
      }

      //change the direction of the ball
      dirVector = (newVectorX, dirVector._2)
      ballY = barTop + radius + 1
    }
  }

  //Check collision with blocks
  def checkCollisionWithBlocks(blocks: ArrayBuffer[Block]): Unit = {
    for (block <- blocks) {
      if (block.isEnable) {
        var blockX : Int = (block.getX - (block.width / 2)).toInt
        var blockY : Int = (block.getY - (block.height / 2)).toInt

        var tempBlock : Block = new Block(blockX, blockY, block.width.toInt, block.height.toInt, block.color, true)

        // Check collision with the block vertical
        println(s"$ballX $ballY")
        if (tempBlock.contains(ballX, ballY)) {
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