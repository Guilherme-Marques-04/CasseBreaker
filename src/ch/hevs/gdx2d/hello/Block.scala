package ch.hevs.gdx2d.hello

import ch.hevs.gdx2d.lib.GdxGraphics
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.math.Rectangle

import scala.collection.mutable.ArrayBuffer

object Block {
  var blocks: ArrayBuffer[Block] = Block.generateBlocks() // Table containing all blocks

  // Generate blocks
  private def generateBlocks(): ArrayBuffer[Block] = {
    val blocks = ArrayBuffer[Block]()
    val colonne = 15
    val ligne = 8
    val espace = 0
    val margeX = 200
    val margeY = 100
    val blockHeight = 40

    val totalWidth = 1920 - (2 * margeX /1.5).toInt
    val totalEspace = (colonne - 1) * espace / 2
    val blockWidth: Float = (totalWidth - totalEspace) / colonne

    val startY = 1080 - margeY - blockHeight

    for (i <- 0 until ligne) {
      for (j <- 0 until colonne) {
        val x = margeX + j * (blockWidth + espace)
        val y = startY - i * (blockHeight + espace)
        blocks += new Block(x.toInt, y, blockWidth.toInt, blockHeight, Color.CYAN, true, false)
      }
    }
    blocks
  }

  // Reset blocks
  def resetBlocks(): Unit = {
    blocks.clear()
    blocks ++= generateBlocks()
  }
}

class Block(posX: Int, posY: Int, w: Int, h: Int, var color: Color, var isEnable: Boolean, var isBonus: Boolean) extends Rectangle(posX, posY, w, h) with Drawable {
  // Draw blocks
  override def draw(g: GdxGraphics): Unit = {
    if (isEnable) {
      g.drawFilledRectangle(x, y, width, height, 0, Color.WHITE)
      g.drawFilledRectangle(x, y, width - 5, height - 5, 0, color)
    }
  }
}

