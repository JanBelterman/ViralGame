package GenerationStrategies

import Data.Gridlander
import Data.Gridlander.Gridlander

object RandomGeneration {

  def generateGrid(gridSize: Int): Array[Array[Gridlander]] = {
    val gridLand = Array.ofDim[Gridlander](gridSize, gridSize)

    // Generate gridLand randomly
    for (x <- 0 until gridSize) {
      for (y <- 0 until gridSize) {
        gridLand(x)(y) = scala.util.Random.nextInt(3) match {
          case 0 => Gridlander.PREFER_FUNCTIONAL
          case 1 => Gridlander.PREFER_OO
          case 2 => Gridlander.PREFER_DECLARATIVE
        }
      }
    }
    // Generate initial subscribers
    gridLand(0)(0) = Gridlander.SUB_PLAYER_1
    gridLand(0)(gridSize - 1) = Gridlander.SUB_PLAYER_2
    gridLand(gridSize - 1)(0) = Gridlander.SUB_PLAYER_3
    gridLand(gridSize - 1)(gridSize - 1) = Gridlander.SUB_PLAYER_4

    gridLand
  }

}
