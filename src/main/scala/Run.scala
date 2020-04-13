import ColorSchemes.ColorSchemes
import Data.Gridlander.Gridlander
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

object GameTypes {



}

object Game {

  def start(): Unit = {
    // todo setup all functions and objects
  }

//  @scala.annotation.tailrec
//  def loop(grid: List[List[Gridlander]]): Unit = {
    // todo create all new ui elements each round, because they may not change
//    val newGrid: List[List[Gridlander]] =
//    loop(newGrid)
//  }

//  def simulateGridLand(grid: List[List[Gridlander]], getGridLander: (Int, Int) => Gridlander): Unit = { // todo or pass that function object with the gridland state
//    ()
//  }

}
