package ch.hevs.gdx2d.hello

import ch.hevs.gdx2d.lib.GdxGraphics
import ch.hevs.gdx2d.desktop.PortableApplication

object Game {

  def main(args: Array[String]): Unit = {
    new Game
  }
}

class Game extends PortableApplication(1920,1080) {

  val bar: Bar = new Bar(300,300,10)

  override def onInit(): Unit = {

  }

  override def onGraphicRender(g: GdxGraphics): Unit = {
    g.clear()
    g.drawFPS()
    bar.draw(g)
  }
}
