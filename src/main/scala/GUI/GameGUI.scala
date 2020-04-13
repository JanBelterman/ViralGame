package GUI

import java.awt.{FlowLayout, GridLayout}

import Data.Gridlander.Gridlander
import Data.Paradigm.Paradigm
import Game.GameTypes
import javax.swing.{JFrame, JLabel, JPanel}

class GameGUI private(val gridSize: Int,
                      val squareSize: Int,
                      var grid: List[List[Gridlander]],
                      val playersTuples: List[GameTypes.PlayerTuple],
                      val colorScheme: GameTypes.ColorScheme) extends JFrame {

  setLayout(new GridLayout(3,3))

  val boardUI = new BoardGUI(squareSize, gridSize, grid, colorScheme)
  val centeredBoardUI = new JPanel(new FlowLayout(FlowLayout.CENTER))
  centeredBoardUI.add(boardUI)

  val players: List[PlayerGUI] = List(
    new PlayerGUI(playersTuples(0)),
    new PlayerGUI(playersTuples(1)),
    new PlayerGUI(playersTuples(2)),
    new PlayerGUI(playersTuples(3))
  )

  // Row 1
  add(players(0))
  add(new JLabel())
  add(players(1))

  // Row 2
  add(new JLabel())
  add(centeredBoardUI)
  add(new JLabel())

  // Row 3
  add(players(2))
  add(new JLabel())
  add(players(3))

  setDefaultCloseOperation(3)
  pack()
  setLocationByPlatform(true)
  setVisible(true)

  def askInput(playerNr: Int): Paradigm = {
    players(playerNr).askInput()
  }

  def render(): Unit = {
    revalidate()
    repaint()
    boardUI.revalidate()
    boardUI.repaint()
    players.foreach(p => {
      p.updateProbability()
      p.revalidate()
      p.repaint()
    })
  }

  def setNewGrid(newGrid: List[List[Gridlander]]): Unit = {
    grid = newGrid
    this.boardUI.setNewGrid(newGrid)
  }

}

object GameGUI {

  def createGameGUI(gridSize: Int,
                    squareSize: Int,
                    grid: List[List[Gridlander]],
                    players: List[GameTypes.PlayerTuple],
                    colorScheme: GameTypes.ColorScheme): GameGUI = {
    new GameGUI(gridSize, squareSize, grid, players, colorScheme)
  }

}
