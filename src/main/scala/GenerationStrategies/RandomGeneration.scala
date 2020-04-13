package GenerationStrategies

import Data.Gridlander
import Data.Gridlander.Gridlander

object RandomGeneration {

  // Immutable data, pattern matching & recursion
  // - Immutable data: easy creation of the array and use inside the simulation methods in game
  // - Pattern matching: goes along great with recursion and immutable data for clear code
  // - Recursion: goes along great with immutable data and pattern matching for clear code
  def generateGrid(gridSize: Int): List[List[Gridlander]] = {
    generateGridRec(gridSize)
  }

  def generateGridRec(gridSize: Int, x: Int = 0): List[List[Gridlander]] = {
    x match {
      case x if x > gridSize - 1 => Nil
      case x => generateStreet(gridSize, x, 0)::generateGridRec(gridSize, x + 1)
    }
  }

  def generateStreet(gridSize: Int, x: Int, y: Int): List[Gridlander] = (x, y) match {
    case (x, _) if x < 0 || x > gridSize - 1 => Nil // out of range
    case (_, y) if y < 0 || y > gridSize - 1 => Nil // out of range
    case (x, y) if x == 0 && y == 0 => Gridlander.SUB_PLAYER_1::generateStreet(gridSize, x, y + 1)
    case (x, y) if x == gridSize - 1 && y == 0 => Gridlander.SUB_PLAYER_2::generateStreet(gridSize, x, y + 1)
    case (x, y) if x == 0 && y == gridSize - 1 => List(Gridlander.SUB_PLAYER_3)
    case (x, y) if x == gridSize - 1 && y == gridSize - 1 => List(Gridlander.SUB_PLAYER_4)
    case (x, y) =>
      val gridLander = scala.util.Random.nextInt(3) match {
        case 0 => Gridlander.PREFER_FUNCTIONAL
        case 1 => Gridlander.PREFER_OO
        case 2 => Gridlander.PREFER_DECLARATIVE
      }
      gridLander::generateStreet(gridSize, x, y + 1)
  }

}
