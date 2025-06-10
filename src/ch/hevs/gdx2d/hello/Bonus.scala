package ch.hevs.gdx2d.hello

import com.badlogic.gdx.graphics.Color
import scala.collection.mutable.ArrayBuffer
import scala.util.Random

class Bonus() {

  //generate random block bonus
  def generatePositionBonus(blocks: ArrayBuffer[Block]): Unit = {
    var bonusCount = 0
    val maxBonus = 20

    while (bonusCount < maxBonus) {
      val i = Random.nextInt(blocks.length)
      if (!blocks(i).isBonus) {
        blocks(i).color = Color.YELLOW
        blocks(i).isBonus = true
        bonusCount += 1
      }
    }
  }


  //Bonus can increase the size bar
  def increaseSizeBar(bar: Bar): Unit = {
    bar.setWidth((bar.getWidth * 1.2).toInt)
  }

  //Bonus add one ball
  def addBall(): Unit = {

  }
}
