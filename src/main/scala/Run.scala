import ColorSchemes.ColorSchemes
import GUI.GameGUI
import GenerationStrategies.RandomGeneration

object Run {

  def main(args: Array[String]): Unit = {
    val gameGUI = GameGUI.createGameGUI(
      10,
      25,
      ColorSchemes.getVibrantColorScheme, // higher order functions for defining color scheme
      RandomGeneration.generateGrid // higher order functions for defining the generation strategy
    )
    gameGUI.run()
  }

}
