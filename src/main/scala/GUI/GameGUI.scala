package GUI

import java.awt.{Color, FlowLayout, GridLayout}

import AI.{RandomAI, ScriptAI}
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

  // todo immutable data in recursive game loop also check when game is won (end case of recursion)
  // todo this function has all data and passes it to other functions that are pure (and thus don't have own data or side effects)
  // todo then this function gets the return variables and stores them
  def run(): Unit = {
    playTurn(players)
    simulateGridland()
    boardUI.revalidate()
    boardUI.repaint()
    run()
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

  // todo Gridlanders moeten pas na de ronde hun nieuwe subscription over kunnen brengen (dus immutable data!)
  // todo immutable data gebruiken voor het bovenstaande, zo krijgen de gridlanders de subscription van de laatste waar ze mee praten
  private def simulateGridland(): Unit = {
//    val newGrid = simulateGridland()     // todo immutable data op deze manier en dan recursen door alles heen, maar hoe cords bijhouden?
    for (x <- 0 until gridSize) {
      for (y <- 0 until gridSize) {
        val gridLander = getGrid()(x)(y)
        val playerGUI = getPlayerFromSubscription(gridLander)
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
      case Some((playerGUI, playerNr)) if doesNeighborSubscribe(playerGUI, neighbor) =>
        playerNr match {
          case 0 => Some(Gridlander.SUB_PLAYER_1)
          case 1 => Some(Gridlander.SUB_PLAYER_2)
          case 2 => Some(Gridlander.SUB_PLAYER_3)
          case 3 => Some(Gridlander.SUB_PLAYER_4)
        }
      case _ => None
    }
  }

  private def doesNeighborSubscribe(playerGUI: PlayerGUI, gridLander: Gridlander): Boolean = {
    val probabilityThreshold = scala.util.Random.nextDouble()
    gridLander match {
      case Gridlander.PREFER_FUNCTIONAL => probabilityThreshold <= playerGUI.getProbability(Paradigm.FUNCTIONAL)
      case Gridlander.PREFER_OO => probabilityThreshold <= playerGUI.getProbability(Paradigm.OO)
      case Gridlander.PREFER_DECLARATIVE => probabilityThreshold <= playerGUI.getProbability(Paradigm.DECLARATIVE)
      case _ => false // already subscribed
    }
  }

  private def getPlayerFromSubscription(gridLander: Gridlander): Option[(PlayerGUI, Int)] = {
     gridLander match {
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
                    generationStrategy: GameTypes.GenerationStrategy): GameGUI = {
    new GameGUI(gridSize, squareSize, colorScheme, generationStrategy)
  }

}
