package GUI

import javax.swing.JPanel
import java.awt.Color
import java.awt.Dimension
import java.awt.Graphics

import scala.collection.mutable.ArrayBuffer

class BoardGUI(val squareSize: Int, val gridSize:Int) extends JPanel {
  val defaultColor = Color.BLACK
  val grid = ArrayBuffer.fill(gridSize, gridSize)(defaultColor)

  def changeSquare(x: Int, y: Int, color: Color): Unit = {
    grid(x)(y) = color
    revalidate()
    repaint()
  }

  protected override def paintComponent(g: Graphics): Unit = {
    super.paintComponent(g)
    for (x <- 0 to (gridSize - 1)) {
      for (y <- 0 to (gridSize - 1)) {
        g.setColor(grid(x)(y))
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
