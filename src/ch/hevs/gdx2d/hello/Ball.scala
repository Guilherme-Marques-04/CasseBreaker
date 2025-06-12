package ch.hevs.gdx2d.hello

import ch.hevs.gdx2d.components.audio.MusicPlayer
import ch.hevs.gdx2d.lib.GdxGraphics
import com.badlogic.gdx.graphics.Color
import scala.collection.mutable.ArrayBuffer
import scala.util.Random

class Ball(private var ballX: Int, private var ballY: Int, private var radius: Int, private var color: Color) extends Drawable {
  // First value:
  // X-axis displacement value
  //    Positive value for right displacement
  //    Negative value for left displacement
  // Second value:
  // Y-axis displacement value
  //    Positive value for up displacement
  //    Negative value for down displacement
  private var dirVector: (Int, Int) = (0, 1)

   private var launched: Boolean = false // If the ball is thrown from the bar
   private var lost: Boolean = false // If the ball quit the screen

  // Ball displacement
  def updateBall(width: Int, height: Int, bar: Bar): Unit = {
    if (!launched) {
      ballX = bar.getPosX()
      ballY = bar.getPosY() + bar.getHeight / 2 + radius + 1
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
    val barLeft: Int = bar.getPosX() - bar.getWidth() / 2
    val barRight: Int = bar.getPosX() + bar.getWidth() / 2
    val barTop: Int = bar.getPosY() + bar.getHeight() / 2
    val barBottom: Int = bar.getPosY() - bar.getHeight() / 2

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

      val barCenter: Int = bar.getPosX()
      val barLimitRight: Int = bar.getPosX() + bar.getWidth() / 2
      val barLimitLeft: Int = bar.getPosX() - bar.getWidth() / 2

      if(ballX >= barLimitLeft-10 && ballX < barCenter){
        newVectorX = (ballX - barCenter) * 5 / (bar.getWidth()/2) - 1
      } else if(ballX >= barCenter && ballX <= barLimitRight){
        newVectorX = (ballX - barLimitRight) * -5 / (bar.getWidth()/2)
        newVectorX = 5-newVectorX
      }

      //change the direction of the ball
      dirVector = (newVectorX, dirVector._2)
      ballY = barTop + radius + 1
    }
  }

  //Check collision with blocks
  def checkCollisionWithBlocks(blocks: ArrayBuffer[Block], bar:Bar, bonus: Bonus, balls: ArrayBuffer[Ball]): Unit = {
  for (block <- blocks if block.isEnable) {
    // Posision block
    val blockLeft = block.getX - block.width / 2
    val blockRight = block.getX + block.width / 2
    val blockTop = block.getY - block.height / 2
    val blockBottom = block.getY + block.height / 2

    // Checks if the ball is in the block area
    if (ballX >= blockLeft && ballX <= blockRight &&
        ballY >= blockTop && ballY <= blockBottom) {

      // Calculating colission distances
      val overlapLeft = ballX - blockLeft
      val overlapRight = blockRight - ballX
      val overlapTop = ballY - blockTop
      val overlapBottom = blockBottom - ballY

      // Find the smallest distance to determine the direction of collision
      val minOverlapX = Math.min(overlapLeft, overlapRight)
      val minOverlapY = Math.min(overlapTop, overlapBottom)

      // Bounces
      if (minOverlapX < minOverlapY) {
        // Horizontal collision (left/right)
        dirVector = (-dirVector._1, dirVector._2)
      } else {
        // Vertical collision (up/down)
        dirVector = (dirVector._1, -dirVector._2)
      }

      // Disable block
      block.isEnable = false

      // Check if the block is a bonus
      if(block.isBonus){
        val typeOfBonus = Random.nextInt(2)
        val musicBonus: MusicPlayer = new MusicPlayer("data/musiques/blockHitWow.mp3")
        musicBonus.play()

        typeOfBonus match {
          // Adding a ball
          case 0 =>
            bonus.addBall(balls, bar)
            println("Bonus : New ball")
          // Increase bar size for 10 sec
          case 1 =>
            bar.enableSizeBonus()
            println("Bonus : Bar size increased for 10 seconds")
        }
      }
    }
  }
}

  // Default position of the ball
  def reset(bar: Bar): Unit = {
    launched = false
    lost = false
    dirVector = (0,0)
    ballX = bar.getPosX()
    ballY = bar.getPosY + bar.getHeight() / 2 + radius + 1
  }

  // Draw the ball
  override def draw(g: GdxGraphics): Unit = {
    g.drawFilledCircle(ballX, ballY, radius, color)
  }

  // If the ball is lost
  def isLost(): Boolean = {
     lost
  }
}