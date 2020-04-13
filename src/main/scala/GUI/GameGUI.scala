package GUI

import java.awt.{Color, FlowLayout, GridLayout}

import AI.RandomAI
import Data.Gridlander.Gridlander
import Data.{Gridlander, Paradigm}
import Data.Paradigm.Paradigm
import Gridland.{GridLand, Player}
import javax.swing.{JFrame, JLabel, JPanel}

object GameTypes {
  type ColorScheme = Gridlander => Color
  type GenerationStrategy = Int => Array[Array[Gridlander]]
  type PlayerTuple = (() => String, Paradigm => Double, Paradigm => Unit, () => Option[Paradigm])
}

// todo when all is done maybe scrap all logic from this class too in a function
class GameGUI private(val gridSize: Int,
                      val squareSize: Int,
                      val colorScheme: GameTypes.ColorScheme,
                      val generateGridLand: GameTypes.GenerationStrategy) {

  val frame = new JFrame
  frame.setLayout(new GridLayout(3,3))

  val (changeGridLander, getGrid, getGridLander) = GridLand.createGridLand(10, generateGridLand)

  val boardUI = new BoardGUI(squareSize, gridSize, getGrid, colorScheme)
  val centeredBoardUI = new JPanel(new FlowLayout(FlowLayout.CENTER))
  centeredBoardUI.add(boardUI)

  // todo maybe store SUB_PLAYER_1 in the player function, but then we have to create an object closure function for player
  // todo that maps directly to the GUI (the GUI objects should have a player closure that has multiple functions to rule its state updates)
  val players: List[PlayerGUI] = List(
    new PlayerGUI(Player.createPlayer("Player 1", () => None)),
    new PlayerGUI(Player.createPlayer("Player 2", RandomAI.getParadigm)),
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

  // todo immutable data in recursive game loop also check when game is won (end case of recursion)
  // todo this function has all data and passes it to other functions that are pure (and thus don't have own data or side effects)
  // todo then this function gets the return variables and stores them
  def run(): Unit = {
    playTurn()
    simulateGridland()
    boardUI.revalidate()
    boardUI.repaint()
    run()
  }

  def playTurn(): Unit = {
    for (i <- 0 to 3) {
      val playerGUI = players(i)
      val paradigm = playerGUI.getVideoParadigm() match {
        case None => playerGUI.askInput()
        case Some(paradigm) => paradigm
      }
      playerGUI.increaseProbability(paradigm)
    }
  }

  // todo Gridlanders moeten pas na de ronde hun nieuwe subscription over kunnen brengen (dus immutable data!)
  private def simulateGridland(): Unit = {
    for (x <- 0 until gridSize) {
      for (y <- 0 until gridSize) {
        val gridLander = getGrid()(x)(y)
        val playerGUI = getPlayerGUIAndIndex(gridLander)
        val getNeighborStateAfterConversation: (Gridlander) => Option[Gridlander] = simulateMeeting(playerGUI) // Currying and partial function application for clearer code
        List((x, y - 1), (x + 1, y), (x, y + 1), (x - 1, y)).foreach((neighborCords: (Int, Int)) => {
          val (nx, ny) = neighborCords
          getGridLander(nx, ny) match {
            case None => () // out of playing field
            case Some(neighbor) =>
              getNeighborStateAfterConversation(neighbor) match {
                case None => () // neighbor doesn't subscribe
                case Some(newNeighborState) =>
                  changeGridLander(nx, ny, newNeighborState)
              }
          }
        })
      }
    }
  }

  // Curried function
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
      case Gridlander.PREFER_FUNCTIONAL => probabilityThreshold <= playerGUI.getProbability(Paradigm.FUNCTIONAL)
      case Gridlander.PREFER_OO => probabilityThreshold <= playerGUI.getProbability(Paradigm.OO)
      case Gridlander.PREFER_DECLARATIVE => probabilityThreshold <= playerGUI.getProbability(Paradigm.DECLARATIVE)
      case _ => false // already subscribed
    }
  }

  private def getPlayerGUIAndIndex(gridlander: Gridlander): Option[(PlayerGUI, Int)] = {
     gridlander match {
       case Gridlander.SUB_PLAYER_1 => Some((players(0), 0))
       case Gridlander.SUB_PLAYER_2 => Some((players(1), 1))
       case Gridlander.SUB_PLAYER_3 => Some((players(2), 2))
       case Gridlander.SUB_PLAYER_4 => Some((players(3), 3))
       case _ => None
     }
  }

}

object GameGUI {

  def createGameGUI(gridSize: Int,
                    squareSize: Int,
                    colorScheme: GameTypes.ColorScheme,
                    generationStrategie: GameTypes.GenerationStrategy): GameGUI = {
    new GameGUI(gridSize, squareSize, colorScheme, generationStrategie)
  }

}
