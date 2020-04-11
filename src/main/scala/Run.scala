import javax.swing.{JFrame, JLabel, JPanel}
import java.awt.{Color, GridLayout}

import Data.{Paradigm, Player}
import GUI.GameGUI

import scala.io.StdIn

object Run {

  /**
   * Example of how to use the GUI
   */
  def main(args: Array[String]): Unit = {
    // Create the GUI once in your game. Specify how many squares the grid should have on each side.
    // Optionally you could specify a second parameter for the size of a single square. The default is 25.
    val gameGUI = GameGUI.createGameGUI(10)

    // todo initialize players
    // Set the name of any of the 4 player
    gameGUI.setPlayerName(Player.PLAYER1, "SquareDieSquat")

    // todo generate gridlanders
    // Change the color of any square. Row and column numbers start a 0.
    gameGUI.changeSquare(0, 1, Color.GREEN)
  }

  def gameLoop(gameGUI: GameGUI): Unit = {
    // todo ask each player to make video
    // Ask a player to choose a paradigm to make a video for.
    val player2Choice = gameGUI.askInput(Player.PLAYER1)
    println(player2Choice)
    // todo simulate each gridlander
    // Set the probability of a paradigm for any player.
    gameGUI.setParadigmProbability(Player.PLAYER1, Paradigm.FUNCTIONAL, 0)
    // todo check if game is finished (if so check who's won)
    // todo exit game?
  }

}
