package GUI

import java.awt.{Color, FlowLayout, GridLayout}

import AI.{RandomAI, ScriptAI}
import Data.Gridlander.Gridlander
import Data.{Gridlander, Paradigm}
import Data.Paradigm.Paradigm
import Gridland.Player
import javax.swing.{JFrame, JLabel, JPanel}

object GameTypes {
  type ColorScheme = Gridlander => Color
  type GenerationStrategy = Int => List[List[Gridlander]]
  type PlayerTuple = (() => String, Paradigm => Double, Paradigm => Unit, () => Option[Paradigm])
}

class GameGUI private(val gridSize: Int,
                      val squareSize: Int,
                      val colorScheme: GameTypes.ColorScheme,
                      val generateGridLand: GameTypes.GenerationStrategy) {

  val frame = new JFrame
  frame.setLayout(new GridLayout(3,3))

  var grid: List[List[Gridlander]] = generateGridLand(gridSize)

  val boardUI = new BoardGUI(squareSize, gridSize, grid, colorScheme)
  val centeredBoardUI = new JPanel(new FlowLayout(FlowLayout.CENTER))
  centeredBoardUI.add(boardUI)

  val players: List[PlayerGUI] = List(
    new PlayerGUI(Player.createPlayer("Player 1", () => None)),
    new PlayerGUI(Player.createPlayer("Player 2", ScriptAI.createScriptAI(List(Paradigm.FUNCTIONAL, Paradigm.DECLARATIVE, Paradigm.OO)))),
    new PlayerGUI(Player.createPlayer("Player 3", RandomAI.getParadigm)),
    new PlayerGUI(Player.createPlayer("Player 4", RandomAI.getParadigm))
  )

  // Row 1
  frame.add(players(0))
  frame.add(new JLabel())
  frame.add(players(1))

  // Row 2
  frame.add(new JLabel())
  frame.add(centeredBoardUI)
  frame.add(new JLabel())

  // Row 3
  frame.add(players(2))
  frame.add(new JLabel())
  frame.add(players(3))

  frame.setDefaultCloseOperation(3)
  frame.pack()
  frame.setLocationByPlatform(true)
  frame.setVisible(true)

  def render(): Unit = {
    boardUI.setGrid(grid)
    boardUI.revalidate()
    boardUI.repaint()
  }

  def getGridLander(grid: List[List[Gridlander]])(x: Int, y: Int): Option[Gridlander] = {
    (x, y) match {
      case (x, _) if (x < 0 || x > gridSize - 1) => None
      case (_, y) if (y < 0 || y > gridSize - 1) => None
      case (x, y) => Some(grid(x)(y))
    }
  }

  // todo move this function out of this class and into a game object, so that all the classes can be replaced (Immutable data)
  // todo remove the gridland object and the getGridlander option should be seperate method and grid should be passes to all functions that want it as immutable data
  def run(): Unit = {
    gameLoop()
  }

  @scala.annotation.tailrec // Recursion (tailrec so no stack overflow)
  final def gameLoop(): Unit = {
    playTurn(players)
    grid = simulateGridLand(getGridLander(grid)) // Immutable data, currying
    render()
    gameLoop()
  }

  def playTurn(players: List[PlayerGUI]): Unit = players match {
    case Nil => ()
    case player::rest =>
      val paradigm = player.getVideoParadigm() match {
        case None => player.askInput()
        case Some(paradigm) => paradigm
      }
      player.increaseProbability(paradigm)
      player.updateProbability()
      playTurn(rest)
  }

  private def simulateGridLand(getGridLander: (Int, Int) => Option[Gridlander], x: Int = 0, y: Int = 0): List[List[Gridlander]] = {
    simulateStreet(getGridLander, x, y) match {
      case Nil => Nil
      case street => street::simulateGridLand(getGridLander, x + 1)
    }
  }

  private def simulateStreet(getGridLander: (Int, Int) => Option[Gridlander], x: Int, y: Int): List[Gridlander] = {
    getGridLander(x, y) match {
      case None => Nil // end of street
      case Some(gridLander) =>
        val getNeighborStateAfterConversation: Gridlander => Gridlander = simulateMeeting(gridLander) // Currying and partial function application for clearer code
        val neighbors: List[Gridlander] = List((x, y - 1), (x + 1, y), (x, y + 1), (x - 1, y)).flatMap[Gridlander](cords => getGridLander(cords._1, cords._2))
        val newGridLanderState = meetNeighbors(gridLander, neighbors, getNeighborStateAfterConversation) match {
          case None => gridLander
          case Some(newState) => newState
        }
        newGridLanderState::simulateStreet(getGridLander, x, y + 1)
    }
  }

  @scala.annotation.tailrec
  private def meetNeighbors(gridLander: Gridlander, neighbors: List[Gridlander], conversation: Gridlander => Gridlander): Option[Gridlander] = {
    neighbors match {
      case Nil => None
      case head::_ if (conversation(head) != gridLander) => Some(conversation(head))
      case _::tail => meetNeighbors(gridLander, tail, conversation)
    }
  }

  // Curried function
  private def simulateMeeting(gridLander: Gridlander)(neighbor: Gridlander): Gridlander = {
    val playerOpt = neighbor match {
      case Gridlander.SUB_PLAYER_1 => Some((players(0), 0))
      case Gridlander.SUB_PLAYER_2 => Some((players(1), 1))
      case Gridlander.SUB_PLAYER_3 => Some((players(2), 2))
      case Gridlander.SUB_PLAYER_4 => Some((players(3), 3))
      case _ => None
      }
    playerOpt match {
      case None => gridLander // neighbor wasn't subscribed in the first place
      case Some((playerGUI, playerNr)) if doesGridLanderSubscribe(playerGUI, gridLander) =>
        playerNr match {
          case 0 => Gridlander.SUB_PLAYER_1
          case 1 => Gridlander.SUB_PLAYER_2
          case 2 => Gridlander.SUB_PLAYER_3
          case 3 => Gridlander.SUB_PLAYER_4
        }
      case _ => gridLander
    }
  }

  private def doesGridLanderSubscribe(playerGUI: PlayerGUI, gridLander: Gridlander): Boolean = {
    val probabilityThreshold = scala.util.Random.nextDouble()
    gridLander match {
      case Gridlander.PREFER_FUNCTIONAL => probabilityThreshold <= playerGUI.getProbability(Paradigm.FUNCTIONAL)
      case Gridlander.PREFER_OO => probabilityThreshold <= playerGUI.getProbability(Paradigm.OO)
      case Gridlander.PREFER_DECLARATIVE => probabilityThreshold <= playerGUI.getProbability(Paradigm.DECLARATIVE)
      case _ => false // already subscribed
    }
  }

}

object GameGUI {

  def createGameGUI(gridSize: Int,
                    squareSize: Int,
                    colorScheme: GameTypes.ColorScheme,
                    generationStrategy: GameTypes.GenerationStrategy): GameGUI = {
    new GameGUI(gridSize, squareSize, colorScheme, generationStrategy)
  }

}
