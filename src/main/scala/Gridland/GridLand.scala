package Gridland

import Data.Gridlander.Gridlander
import GUI.GameTypes

object GridLand {

  type createGridLand = (Int, GameTypes.GenerationStrategy) => ((Int, Int, Gridlander) => Unit, () => Array[Array[Gridlander]], (Int, Int) => Option[Gridlander])

  // Function as object
  def createGridLand: createGridLand = (gridSize: Int, generateGridLand: GameTypes.GenerationStrategy) => {
    val grid: Array[Array[Gridlander]] = generateGridLand(gridSize)

    val changeGridLander = (x: Int, y: Int, gridLander: Gridlander) => {
      grid(x)(y) = gridLander
    }

    val getGrid = () => {
      grid
    }

    val getGridLander = (x: Int, y: Int) => {
      (x, y) match {
        case (x, _) if (x < 0 || x > gridSize - 1) => None
        case (_, y) if (y < 0 || y > gridSize - 1) => None
        case (x, y) => Some(grid(x)(y))
      }
    }

    (changeGridLander, getGrid, getGridLander)
  }

}
