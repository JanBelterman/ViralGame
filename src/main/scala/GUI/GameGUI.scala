package GUI

import java.awt.{FlowLayout, GridLayout}

import AI.RandomAI
import Data.Gridlander.Gridlander
import Data.{Gridlander, Paradigm}
import Data.Paradigm.Paradigm
import javax.swing.{JFrame, JLabel, JPanel}

class GameGUI private(val gridSize: Int,
                      val squareSize: Int,
                      val colorScheme: ColorSchemes.ColorScheme) {

  val frame = new JFrame
  frame.setLayout(new GridLayout(3,3))

  // todo of losse functies die allemaal een grid passen?
  val (changeGridlander, generateInitalGrid, getGrid) = Gridland.Gridland.createGridland(10)

  val boardUI = new BoardGUI(squareSize, gridSize, getGrid, colorScheme)
  val centeredBoardUI = new JPanel(new FlowLayout(FlowLayout.CENTER))
  centeredBoardUI.add(boardUI)

  // todo maybe store SUB_PLAYER_1 in the player function, but then we have to create an object closure function for player
  // todo that maps directly to the GUI (the GUI objects should have a player closure that has multiple functions to rule its state updates)
  val players: List[(PlayerGUI, () => Option[Paradigm])] = List(
    (new PlayerGUI("Player 1"), () => None),
    (new PlayerGUI("Player 2"), RandomAI.getParadigm), // todo closure for players probabilities (player function object just like the board) that is the final goal
    (new PlayerGUI("Player 3"), RandomAI.getParadigm),
    (new PlayerGUI("Player 4"), RandomAI.getParadigm)
  )

  // Row 1
  frame.add(players(0)._1)
  frame.add(new JLabel())
  frame.add(players(1)._1)

  // Row 2
  frame.add(new JLabel())
  frame.add(centeredBoardUI)
  frame.add(new JLabel())

  // Row 3
  frame.add(players(2)._1)
  frame.add(new JLabel())
  frame.add(players(3)._1)

  frame.setDefaultCloseOperation(3)
  frame.pack()
  frame.setLocationByPlatform(true)
  frame.setVisible(true)

  def run(): Unit = {
    playTurn()
    simulateGridland()
    run()
  }

  def playTurn(): Unit = {
    for (i <- 0 to 3) {
      val (playerGUI, getParadigm) = players(i)
      val paradigm = getParadigm() match {
        case None => playerGUI.askInput()
        case Some(paradigm) => paradigm
      }
      increaseProbability(playerGUI, paradigm, 0.1)
    }
  }

  // todo higher order function for probability increase (maybe also some random strategy?)
  def increaseProbability(playerGUI: PlayerGUI, paradigm: Paradigm, probabilityIncrease: Double): Unit = {
    val probability = paradigm match {
      case Paradigm.FUNCTIONAL => playerGUI.functionalProbability + probabilityIncrease
      case Paradigm.OO => playerGUI.ooProbability + probabilityIncrease
      case Paradigm.DECLARATIVE => playerGUI.declarativeProbability + probabilityIncrease
    }
    playerGUI.setParadigmProbability(paradigm, probability)
  }

  // todo higher order function generate gridland strategy?
  def generateInitialGrid(): Unit = {
    // Generate other gridlanders
    for (i <- 0 until gridSize) {
      for (j <- 0 until gridSize) {
        boardUI.changeGridlander(i, j, scala.util.Random.nextInt(3) match {
          case 0 => Gridlander.PREFER_FUNCTIONAL
          case 1 => Gridlander.PREFER_OO
          case 2 => Gridlander.PREFER_DECLARATIVE
        })
      }
    }
    // Generate initial subscribers
    boardUI.changeGridlander(0, 0, Gridlander.SUB_PLAYER_1)
    boardUI.changeGridlander(0, boardUI.gridSize - 1, Gridlander.SUB_PLAYER_2)
    boardUI.changeGridlander(boardUI.gridSize - 1, 0, Gridlander.SUB_PLAYER_3)
    boardUI.changeGridlander(boardUI.gridSize - 1, boardUI.gridSize - 1, Gridlander.SUB_PLAYER_4)
  }

  // todo Gridlanders moeten pas na de ronde hun nieuwe subscription over kunnen brengen (dus immutable data!)
  private def simulateGridland(): Unit = {
    for (x <- 0 until gridSize) {
      for (y <- 0 until gridSize) {
        val gridLander = boardUI.grid(x)(y)
        val playerGUI = getPlayerGUIAndIndex(gridLander)
        val getNeighborStateAfterConversation: (Gridlander) => Option[Gridlander] = simulateMeeting(playerGUI) // Currying and partial function application for clearer code
        List((x, y - 1), (x + 1, y), (x, y + 1), (x - 1, y)).foreach((neighborCords: (Int, Int)) => {
          val (nx, ny) = neighborCords
          boardUI.getGridlander(nx, ny) match {
            case None => () // out of playing field
            case Some(neighbor) =>
              getNeighborStateAfterConversation(neighbor) match {
                case None => () // neighbor doesn't subscribe
                case Some(newNeighborState) =>
                  boardUI.changeGridlander(nx, ny, newNeighborState)
              }
          }
        })
      }
    }
  }

  private def simulateMeeting(playerOpt: Option[(PlayerGUI, Int)])(neighbor: Gridlander): Option[Gridlander] = {
    playerOpt match {
      case None => None // first gridLander was not subscribed
      case Some((playerGUI, playerNr)) if (doesNeighborSubscribe(playerGUI, neighbor)) =>
        playerNr match {
          case 0 => Some(Gridlander.SUB_PLAYER_1)
          case 1 => Some(Gridlander.SUB_PLAYER_2)
          case 2 => Some(Gridlander.SUB_PLAYER_3)
          case 3 => Some(Gridlander.SUB_PLAYER_4)
        }
      case _ => None
    }
  }

  private def doesNeighborSubscribe(playerGUI: PlayerGUI, gridlander: Gridlander): Boolean = {
    val probabilityThreshold = scala.util.Random.nextDouble()
    gridlander match {
      case Gridlander.PREFER_FUNCTIONAL => probabilityThreshold <= playerGUI.functionalProbability
      case Gridlander.PREFER_OO => probabilityThreshold <= playerGUI.ooProbability
      case Gridlander.PREFER_DECLARATIVE => probabilityThreshold <= playerGUI.declarativeProbability
      case _ => false // already subscribed
    }
  }

  private def getPlayerGUIAndIndex(gridlander: Gridlander): Option[(PlayerGUI, Int)] = {
     gridlander match {
       case Gridlander.SUB_PLAYER_1 => Some((players(0)._1, 0))
       case Gridlander.SUB_PLAYER_2 => Some((players(1)._1, 1))
       case Gridlander.SUB_PLAYER_3 => Some((players(2)._1, 2))
       case Gridlander.SUB_PLAYER_4 => Some((players(3)._1, 3))
       case _ => None
     }
  }

}

object GameGUI {

  def createGameGUI(gridSize: Int, squareSize: Int, colorScheme: ColorSchemes.ColorScheme): GameGUI = {
    new GameGUI(gridSize, squareSize, colorScheme)
  }

}
