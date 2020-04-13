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
    val getGrid = () => {
      grid
    }
    (changeGridlander, getGrid)
  }

}
