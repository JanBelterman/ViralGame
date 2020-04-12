package Gridland

import Data.Gridlander
import Data.Gridlander.Gridlander

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer

object Gridland {

  // todo ?
  def createGridland  = (gridSize: Int /* colorStrategy: (Gridlander) => Color, generationStrategy: () => Unit */) => {
    val grid: mutable.Seq[ArrayBuffer[Gridlander.Value]] = ArrayBuffer.fill(gridSize, gridSize)(Gridlander.DEFAULT)

    val changeGridlander = (x: Int, y: Int, gridlander: Gridlander) => {
      grid(x)(y) = gridlander
    }
    val generateInitialGrid = () => {
      // Generate other gridlanders
      for (i <- 0 until gridSize) {
        for (j <- 0 until gridSize) {
          changeGridlander(i, j, scala.util.Random.nextInt(3) match {
            case 0 => Gridlander.PREFER_FUNCTIONAL
            case 1 => Gridlander.PREFER_OO
            case 2 => Gridlander.PREFER_DECLARATIVE
          })
        }
      }
      // Generate initial subscribers
      changeGridlander(0, 0, Gridlander.SUB_PLAYER_1)
      changeGridlander(0, gridSize - 1, Gridlander.SUB_PLAYER_2)
      changeGridlander(gridSize - 1, 0, Gridlander.SUB_PLAYER_3)
      changeGridlander(gridSize - 1, gridSize - 1, Gridlander.SUB_PLAYER_4)
    }
    val getGrid = () => {
      grid
    }
    (changeGridlander, generateInitialGrid, getGrid)
  }

}
