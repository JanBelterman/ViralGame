package Game

import java.awt.Color

import Data.Gridlander.Gridlander
import Data.{Gridlander, Paradigm}
import Data.Paradigm.Paradigm
import GUI.GameGUI

object GameTypes {
  type ColorScheme = Gridlander => Color
  type GenerationStrategy = Int => List[List[Gridlander]]
  type PlayerTuple = (() => String, Paradigm => Double, Paradigm => Unit, () => Option[Paradigm])
}

object Game {

  def start(gridSize: Int, squareSize: Int, colorScheme: GameTypes.ColorScheme, generateGridLand: GameTypes.GenerationStrategy, players: List[GameTypes.PlayerTuple]): Unit = {
    val grid: List[List[Gridlander]] = generateGridLand(gridSize)
    val gameGUI = GameGUI.createGameGUI(gridSize, squareSize, grid, players, colorScheme)
    loop(grid, players, gameGUI)
  }

  // Alles wordt steeds opnieuw aangemaakt van UI is dat wel de bedoeling?
  @scala.annotation.tailrec // Recursion (tailrec so no stack overflow)
  def loop(grid: List[List[Gridlander]], players: List[GameTypes.PlayerTuple], gameGUI: GameGUI): Unit = {
    playTurn(players, gameGUI)
    val newGrid = simulateGridLand(players, grid, getGridLander(grid)) // Immutable data, currying
    gameGUI.setNewGrid(grid)
    gameGUI.render()
    loop(newGrid, players, gameGUI)
  }

  // Not a pure function (the entire player function is not)
  @scala.annotation.tailrec
  def playTurn(players: List[GameTypes.PlayerTuple], gameGUI: GameGUI): Unit = players match {
    case Nil => ()
    case player::rest =>
      val (_, _, increaseParadigmProbability, getVideoParadigm) = player
      val paradigm = getVideoParadigm() match {
        case None => gameGUI.askInput(4 - players.length)
        case Some(paradigm) => paradigm
      }
      increaseParadigmProbability(paradigm)
      playTurn(rest, gameGUI)
  }

  def getGridLander(grid: List[List[Gridlander]])(x: Int, y: Int): Option[Gridlander] = {
    (x, y) match {
      case (x, _) if (x < 0 || x > grid.length - 1) => None
      case (_, y) if (y < 0 || y > grid.length - 1) => None
      case (x, y) => Some(grid(x)(y))
    }
  }

  def simulateGridLand(players: List[GameTypes.PlayerTuple], grid: List[List[Gridlander]], getGridLander: (Int, Int) => Option[Gridlander], x: Int = 0, y: Int = 0): List[List[Gridlander]] = {
    simulateStreet(players, getGridLander, x, y) match {
      case Nil => Nil
      case street => street::simulateGridLand(players, grid, getGridLander, x + 1)
    }
  }

  def simulateStreet(players: List[GameTypes.PlayerTuple], getGridLander: (Int, Int) => Option[Gridlander], x: Int, y: Int): List[Gridlander] = {
    val getNeighborStateAfterConversation: (Gridlander, Gridlander) => Gridlander = simulateMeeting(players) // Currying and partial function application for clearer code
    getGridLander(x, y) match {
      case None => Nil // end of street
      case Some(gridLander) =>
        val neighbors: List[Gridlander] = List((x, y - 1), (x + 1, y), (x, y + 1), (x - 1, y)).flatMap[Gridlander](cords => getGridLander(cords._1, cords._2))
        val newGridLanderState = meetNeighbors(gridLander, neighbors, getNeighborStateAfterConversation) match {
          case None => gridLander
          case Some(newState) => newState
        }
        newGridLanderState::simulateStreet(players, getGridLander, x, y + 1)
    }
  }

  @scala.annotation.tailrec
  def meetNeighbors(gridLander: Gridlander, neighbors: List[Gridlander], conversation: (Gridlander, Gridlander) => Gridlander): Option[Gridlander] = {
    neighbors match {
      case Nil => None
      case head::_ if (conversation(gridLander, head) != gridLander) => Some(conversation(gridLander, head))
      case _::tail => meetNeighbors(gridLander, tail, conversation)
    }
  }

  // Curried function
  def simulateMeeting(players: List[GameTypes.PlayerTuple])(gridLander: Gridlander, neighbor: Gridlander): Gridlander = {
    val playerOpt = neighbor match {
      case Gridlander.SUB_PLAYER_1 => Some((players(0), 0))
      case Gridlander.SUB_PLAYER_2 => Some((players(1), 1))
      case Gridlander.SUB_PLAYER_3 => Some((players(2), 2))
      case Gridlander.SUB_PLAYER_4 => Some((players(3), 3))
      case _ => None
    }
    playerOpt match {
      case None => gridLander // neighbor wasn't subscribed in the first place
      case Some((playerTuple, playerNr)) if doesGridLanderSubscribe(playerTuple, gridLander) =>
        playerNr match {
          case 0 => Gridlander.SUB_PLAYER_1
          case 1 => Gridlander.SUB_PLAYER_2
          case 2 => Gridlander.SUB_PLAYER_3
          case 3 => Gridlander.SUB_PLAYER_4
        }
      case _ => gridLander
    }
  }

  def doesGridLanderSubscribe(playerTuple: GameTypes.PlayerTuple, gridLander: Gridlander): Boolean = {
    val (_, getProbability, _, _) = playerTuple
    val probabilityThreshold = scala.util.Random.nextDouble()
    gridLander match {
      case Gridlander.PREFER_FUNCTIONAL => probabilityThreshold <= getProbability(Paradigm.FUNCTIONAL)
      case Gridlander.PREFER_OO => probabilityThreshold <= getProbability(Paradigm.OO)
      case Gridlander.PREFER_DECLARATIVE => probabilityThreshold <= getProbability(Paradigm.DECLARATIVE)
      case _ => false // already subscribed
    }
  }

}
