package ch.hevs.gdx2d.hello

import com.badlogic.gdx.graphics.Color
import scala.collection.mutable.ArrayBuffer
import scala.util.Random

class Bonus() {

  //generate random block bonus
  def generatePositionBonus(block: ArrayBuffer[Block]): ArrayBuffer[Block] = {
    val randomBlockBonus: ArrayBuffer[Block] = new ArrayBuffer[Block]()
    val blocks: ArrayBuffer[Block] = new ArrayBuffer[Block]()
    val sizeBlock: Int = block.length

    //choose 20 random blocks to become bonus blocks
    for(i: Int <- 0 until 20){
      var randomBlock = Random.nextInt(sizeBlock)
      randomBlockBonus.append(block(randomBlock))
    }

    //change the color of the bonus blocks
//    for(b <- randomBlockBonus){
//      blocks += new Block(b.posX, b.posY, b.width, b.height, Color.RED)
//    }
    blocks
  }

  //Bonus can increase the size bar
  def increaseSizeBar(): Unit = ???

  //Bonus add one ball
  def addBall(): Unit = ???
}
