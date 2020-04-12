package GUI

import java.awt.Color

import Data.Gridlander
import Data.Gridlander.Gridlander

object ColorSchemes {

  type ColorScheme = Gridlander => Color

  def getClassicColorScheme: ColorScheme = {
    createColorSchema(List(Color.BLUE, Color.GREEN, Color.YELLOW, Color.RED, Color.WHITE, Color.LIGHT_GRAY, Color.GRAY, Color.BLACK))
  }

  def getVibrantColorScheme: ColorScheme = {
    createColorSchema(List(Color.BLUE, Color.GREEN, Color.YELLOW, Color.RED, Color.PINK, Color.MAGENTA, Color.CYAN, Color.BLACK))
  }

  // Closure function factory for avoiding duplicate code
  def createColorSchema(colors: List[Color]): ColorScheme = {
    case Gridlander.SUB_PLAYER_1 => colors(0)
    case Gridlander.SUB_PLAYER_2 => colors(1)
    case Gridlander.SUB_PLAYER_3 => colors(2)
    case Gridlander.SUB_PLAYER_4 => colors(3)
    case Gridlander.PREFER_FUNCTIONAL => colors(4)
    case Gridlander.PREFER_OO => colors(5)
    case Gridlander.PREFER_DECLARATIVE => colors(6)
    case _ => colors(7)
  }

}
