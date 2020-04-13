package GUI

import javax.swing.JPanel
import java.awt.Color
import java.awt.Dimension
import java.awt.Graphics

import Data.Gridlander.Gridlander

class BoardGUI(val squareSize: Int,
               val gridSize:Int,
               val getGrid: () => Array[Array[Gridlander]],
               val getSquareColor: GameTypes.ColorScheme) extends JPanel {

  protected override def paintComponent(g: Graphics): Unit = {
    super.paintComponent(g)
    val grid = getGrid()
    for (x <- 0 until gridSize) {
      for (y <- 0 until gridSize) {
        g.setColor(getSquareColor(grid(x)(y)))
        g.fillRect(x * squareSize, y * squareSize, squareSize, squareSize)
        g.setColor(Color.WHITE)
        g.drawRect(x * squareSize, y * squareSize, squareSize, squareSize)
      }
    }
  }

  override def getPreferredSize: Dimension = {
    new Dimension(gridSize * squareSize, gridSize * squareSize)
  }

}
