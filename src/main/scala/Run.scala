import ColorSchemes.ColorSchemes
import GUI.GameGUI
import GenerationStrategies.RandomGeneration

object Run {

  // todo find a way to work with immutable data and the OO user interfaces
  def main(args: Array[String]): Unit = {
    val gameGUI = GameGUI.createGameGUI(
      10,
      25,
      ColorSchemes.getVibrantColorScheme, // higher order functions for defining color scheme
      RandomGeneration.generateGrid // higher order functions for defining the generation strategy
    )
    gameGUI.generateInitialGrid()
    gameGUI.run()
  }

}
