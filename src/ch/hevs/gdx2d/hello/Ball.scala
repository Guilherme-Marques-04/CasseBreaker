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

   var launched: Boolean = false
   var lost: Boolean = false

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

      val barCenter: Int = bar.posX
      val barLimitRight: Int = bar.posX + bar.width / 2
      val barLimitLeft: Int = bar.posX - bar.width / 2

      if(ballX >= barLimitLeft-10 && ballX < barCenter){
        newVectorX = (ballX - barCenter) * 5 / (bar.width/2) - 1
      } else if(ballX >= barCenter && ballX <= barLimitRight){
        newVectorX = (ballX - barLimitRight) * (-5) / (bar.width/2)
        newVectorX = 5-newVectorX
      }

      //change the direction of the ball
      dirVector = (newVectorX, dirVector._2)
      ballY = barTop + radius + 1
    }
  }

  //Check collision with blocks
  def checkCollisionWithBlocks(blocks: ArrayBuffer[Block], bar:Bar, bonus: Bonus): Unit = {
  for (block <- blocks if block.isEnable) {
    // Posision block
    val blockLeft = block.getX - block.width / 2
    val blockRight = block.getX + block.width / 2
    val blockTop = block.getY - block.height / 2
    val blockBottom = block.getY + block.height / 2

    // Vérifie si la balle est dans la zone du bloc
    if (ballX >= blockLeft && ballX <= blockRight &&
        ballY >= blockTop && ballY <= blockBottom) {

      // Calcul des distances de colission
      val overlapLeft = ballX - blockLeft
      val overlapRight = blockRight - ballX
      val overlapTop = ballY - blockTop
      val overlapBottom = blockBottom - ballY

      // Trouve la plus petite distance pour savoir la direction de la collision
      val minOverlapX = Math.min(overlapLeft, overlapRight)
      val minOverlapY = Math.min(overlapTop, overlapBottom)

      // Rebonds
      if (minOverlapX < minOverlapY) {
        // Collision horizontale (gauche/droite)
        dirVector = (-dirVector._1, dirVector._2)
        println("Collision horizontale")
      } else {
        // Collision verticale (haut/bas)
        dirVector = (dirVector._1, -dirVector._2)
        println("Collision verticale")
      }

      // Desactiver le bloc
      block.isEnable = false
      block.color = Color.DARK_GRAY

      // Check if the block is a bonus
      if(block.isBonus){
        bonus.increaseSizeBar(bar)
      }
    }
  }
}

  def reset(bar: Bar): Unit = {
    launched = false
    lost = false
    dirVector = (0,0)
    ballX = bar.posX
    ballY = bar.posY + bar.height / 2 + radius + 1
  }

  override def draw(g: GdxGraphics): Unit = {
    g.drawFilledCircle(ballX, ballY, radius, color)
  }
}