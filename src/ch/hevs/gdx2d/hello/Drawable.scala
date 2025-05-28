package ch.hevs.gdx2d.hello

import ch.hevs.gdx2d.lib.GdxGraphics

trait Drawable {
  def draw(g: GdxGraphics) : Unit
}
