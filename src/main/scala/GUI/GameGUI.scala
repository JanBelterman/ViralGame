package GUI

import java.awt.{Color, FlowLayout, GridLayout}

import Data.Paradigm
import Data.Paradigm.Paradigm
import Data.Player.Player
import javax.swing.{JFrame, JLabel, JPanel}

class GameGUI private(val gridSize: Int, val squareSize: Int) {

  val frame = new JFrame
  frame.setLayout(new GridLayout(3,3))

  val player1GUI = new PlayerGUI("Player 1")
  val player2GUI = new PlayerGUI("Player 2")
  val player3GUI = new PlayerGUI("Player 3")
  val player4GUI = new PlayerGUI("Player 4")

  val boardUI = new BoardGUI(squareSize, gridSize)
  val centeredBoardUI = new JPanel(new FlowLayout(FlowLayout.CENTER))
  centeredBoardUI.add(boardUI)

  // Row 1
  frame.add(player1GUI)
  frame.add(new JLabel())
  frame.add(player2GUI)

  // Row 2
  frame.add(new JLabel())
  frame.add(centeredBoardUI)
  frame.add(new JLabel())

  // Row 3
  frame.add(player3GUI)
  frame.add(new JLabel())
  frame.add(player4GUI)

  frame.setDefaultCloseOperation(3)
  frame.pack
  frame.setLocationByPlatform(true)
  frame.setVisible(true)

  def changeSquare(x: Int, y: Int, color: Color): Unit = {
    boardUI.changeSquare(x, y, color)
  }

  def setPlayerName(player: Player, name: String): Unit = {
    val chosenPlayerGUI = getPlayerGUI(player)
    chosenPlayerGUI.setNameText(name)
  }

  def setParadigmProbability(player: Player, paradigm: Paradigm, probability: Double): Unit = {
    val chosenPlayerGUI = getPlayerGUI(player)
    chosenPlayerGUI.setParadigmProbability(paradigm, probability)
  }

  def askInput(player:Player): Paradigm = {
    val chosenPlayerGUI = getPlayerGUI(player)
    chosenPlayerGUI.askInput()
  }

  private def getPlayerGUI(player: Player): PlayerGUI = {
    player match {
      case Data.Player.PLAYER1 => player1GUI
      case Data.Player.PLAYER2 => player2GUI
      case Data.Player.PLAYER3 => player3GUI
      case Data.Player.PLAYER4 => player4GUI
    }
  }

}

object GameGUI{
  def createGameGUI(gridSize: Int, squareSize: Int = 25): GameGUI = {
    new GameGUI(gridSize, squareSize)
  }
}
