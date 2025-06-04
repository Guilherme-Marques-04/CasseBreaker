package ch.hevs.gdx2d.hello

import ch.hevs.gdx2d.lib.GdxGraphics
import com.badlogic.gdx.graphics.Color

import scala.collection.mutable.ArrayBuffer


class Block(var posX: Int, var posY: Int, var width: Int, var height: Int, color: Color) extends Drawable{

  override def draw(g: GdxGraphics): Unit = {
    g.drawFilledRectangle(posX,posY, width+5, height+5, 90, Color.BLACK)
    g.drawFilledRectangle(posX, posY, width, height, 90, color)
  }
  def generateBlocks(): ArrayBuffer[Block] = {
    val blocks = ArrayBuffer[Block]()

    val colonne = 15
    val ligne = 8
    val espace = 0
    val margeX = 200
    val margeY = 100
    val blockHeight = 40

    val totalWidth = 1920 - 2 * margeX
    val totalEspace= (colonne - 1) * espace/2
    val blockWidth: Float = (totalWidth - totalEspace) / colonne

    val startY = 1080 - margeY - blockHeight

    for (i <- 0 until ligne) {
      for (j <- 0 until colonne) {
        val x = margeX + j * (blockWidth + espace)
        val y = startY - i * (blockHeight + espace)
        blocks += new Block(x.toInt, y, blockHeight, blockWidth.toInt, Color.CYAN)
      }
    }
    blocks
  }
}