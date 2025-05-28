package ch.hevs.gdx2d.hello

import ch.hevs.gdx2d.lib.GdxGraphics

class Bar(var posX: Int, var posY: Int, var size:Int) extends Drawable {

  def onKeyUp(keycode: Int): Unit = {

  }

  override def draw(g: GdxGraphics): Unit = {

    g.drawFilledRectangle(posX,posY, size, 90, 90)

  }
}

