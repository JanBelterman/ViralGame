import GUI.{ColorSchemes, GameGUI}

object Run {

  def main(args: Array[String]): Unit = {
    // todo set all configuration functions from here
    // todo find a way to work with immutable data and the OO user interfaces
    val gameGUI = GameGUI.createGameGUI(
      10,
      25,
      ColorSchemes.getVibrantColorScheme // higher order functions for defining color scheme
    )
    gameGUI.generateInitialGrid()
    gameGUI.run()
  }

}
