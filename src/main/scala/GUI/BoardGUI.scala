package GUI

import javax.swing.JPanel
import java.awt.Color
import java.awt.Dimension
import java.awt.Graphics

import Data.Gridlander
import Data.Gridlander.Gridlander

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer

class BoardGUI(val squareSize: Int,
               val gridSize:Int,
               val getGrid: () => mutable.Seq[ArrayBuffer[Gridlander.Value]],
               val getSquareColor: ColorSchemes.ColorScheme) extends JPanel {

  // todo (make gridlander a function with state? so that paintcomponent can call it?)
  val grid: Array[Array[Gridlander]] = Array.ofDim[Gridlander](gridSize, gridSize)

  def changeGridlander(x: Int, y: Int, gridlander: Gridlander): Unit = {
    grid(x)(y) = gridlander
    revalidate()
    repaint()
  }

  protected override def paintComponent(g: Graphics): Unit = {
    super.paintComponent(g)
    for (x <- 0 until gridSize) {
      for (y <- 0 until gridSize) {
        g.setColor(getSquareColor(grid(x)(y)))
        g.fillRect(x * squareSize, y * squareSize, squareSize, squareSize)
        g.setColor(Color.WHITE)
        g.drawRect(x * squareSize, y * squareSize, squareSize, squareSize)
      }
    }
  }

  // todo move to function that has all the grid information, so this class can solely focus on displaying information
  def getGridlander(x: Int, y: Int): Option[Gridlander] = {
    (x, y) match {
      case (x, _) if (x < 0 || x > gridSize - 1) => None
      case (_, y) if (y < 0 || y > gridSize - 1) => None
      case (x, y) => Some(grid(x)(y))
    }
  }

  override def getPreferredSize: Dimension = {
    new Dimension(gridSize * squareSize, gridSize * squareSize)
  }

}
