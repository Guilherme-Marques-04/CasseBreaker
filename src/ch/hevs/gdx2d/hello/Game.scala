package ch.hevs.gdx2d.hello

import ch.hevs.gdx2d.components.audio.MusicPlayer
import ch.hevs.gdx2d.components.bitmaps.BitmapImage
import ch.hevs.gdx2d.desktop.PortableApplication
import ch.hevs.gdx2d.hello.Block.blocks
import ch.hevs.gdx2d.lib.GdxGraphics
import com.badlogic.gdx.{Gdx, Input, InputAdapter}
import com.badlogic.gdx.graphics.Color

import scala.collection.mutable.ArrayBuffer
import java.time.LocalDateTime

object Game {
  def main(args: Array[String]): Unit = {
    new Game
  }
}

class Game extends PortableApplication(1920, 1080) {

  private val window: Game = this

  private val bar = new Bar(1920 / 2, 100, 250, 20, Color.WHITE)
  private val bonus: Bonus = new Bonus() // contains all bonus

  private var background: BitmapImage = _ // Background file path

  // Contains all game balls
  private var endscreen: BitmapImage = _
  private var music: MusicPlayer = _ // Var to store the path to the music file
  private var endMusic: MusicPlayer = _
  private var endMusicPlayed: Boolean = false
  private val balls: ArrayBuffer[Ball] = new ArrayBuffer
  balls.addOne(new Ball(bar.getPosX(), bar.getPosY() + bar.getHeight / 2 + 10, 10, Color.RED)) // Add 1st ball

  private var lives: Int = 3 // Number of lives

  val toRemove = new ArrayBuffer[Ball]() // Temporary table for storing loose bales

  var lastFrameTime : LocalDateTime = LocalDateTime.now // Used to compare the time between frames

  // Restart a new game
  def restartGame(): Unit = {
    lives = 3
    bar.reset()
    balls.clear()
    balls.addOne(new Ball(bar.getPosX(), bar.getPosY() + bar.getHeight / 2 + 10, 10, Color.RED))
    Block.resetBlocks()
    bonus.generatePositionBonus(Block.blocks)
    endMusicPlayed = false
  }

  //Check if all blocks are broken
  def allBlocksBroken(): Boolean = {
    for (block <- blocks) {
      if (block.isEnable) {
        return false
      }
    }
    true
  }

  //Input key
  def setInput(): Unit = {
    Gdx.input.setInputProcessor(new InputAdapter {
      override def keyDown(keycode: Int): Boolean = {
        bar.onKeyDown(keycode)

        if (keycode == Input.Keys.UP && lives > 0) {
          balls.head.startBall()
        }

        if (lives <= 0 || allBlocksBroken() && (keycode == Input.Keys.SPACE || keycode == Input.Keys.ENTER)) {
          restartGame()
        }

        true
      }

      override def keyUp(keycode: Int): Boolean = {
        bar.onKeyUp(keycode)
        true
      }
    })
  }

  override def onInit(): Unit = {
    // Sets the window title
		setTitle("CasseBreaker");

    //generate bonus
    bonus.generatePositionBonus(blocks)

    //background image
    background = new BitmapImage("data/images/spongebob.png")
    endscreen = new BitmapImage("data/images/endscreen.png")

    //music
    music = new MusicPlayer("data/musiques/music.mp3")
    music.loop()

    setInput()
  }

  override def onGraphicRender(g: GdxGraphics): Unit = {
    g.clear()
    g.drawFPS()

    // At every second
    if (lastFrameTime.getSecond < LocalDateTime.now().getSecond) {
      // If the bonus is activated, it decreases the time every second
      bar.bonusSizeBar()
    }

    // Saves the current frame time for the next pass
    lastFrameTime = LocalDateTime.now()

    //draw background
    g.drawPicture(g.getScreenWidth / 2, g.getScreenHeight / 2, background)

    //update the bar
    bar.updateBar()

    //draw the bar
    bar.draw(g)

    //draw all blocks
    blocks.toList.foreach(_.draw(g))

    //draw all balls
    balls.foreach(ball => ball.draw(g))

    //Check collisions
    if(lives > 0) {
      balls.toList.foreach(ball => {
        ball.checkCollisionWithBar(bar)
        ball.checkCollisionWithBlocks(blocks,bar, bonus, balls)
        ball.updateBall(window.getWindowWidth, window.getWindowHeight, bar)

        //Check if balls is lost
        for (ball <- balls) {
          if (ball.isLost()) {
            toRemove += ball
          }
        }

        // Remove all balls
        for (ball <- toRemove) {
          balls -= ball
        }

        // Check if the game have more than one ball
        if (balls.isEmpty && lives > 0) {
          lives -= 1
          music = new MusicPlayer("data/musiques/OhNo.mp3")
          music.play()
          ball.reset(bar)
          val newBall = new Ball(bar.getPosX(), bar.getPosY() + bar.getHeight / 2 + 10, 10, Color.RED)
          balls += newBall
        }
      })
    }

    //draw lives
    for(i <- 0 until lives) {
      val x = 30 + i * 40
      val y = 1040
      g.drawFilledCircle(x, y, 15, Color.RED)
    }

    //Set text when you have 0 live or win
    if (allBlocksBroken()) {
      g.drawStringCentered(g.getScreenHeight / 2, "You win ! Press enter to restart")
    } else if (lives <= 0) {
      g.drawPicture(g.getScreenWidth / 2, g.getScreenHeight / 2, endscreen)

      if (!endMusicPlayed) {
        music.stop()
        endMusic = new MusicPlayer("data/musiques/moment.mp3")
        endMusic.play()
        endMusicPlayed = true
      }
    }
  }
}