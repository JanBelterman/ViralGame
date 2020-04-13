import AI.{RandomAI, ScriptAI}
import ColorSchemes.ColorSchemes
import Data.Paradigm
import Game.Game
import GenerationStrategies.RandomGeneration
import Gridland.Player

object Run {

  def main(args: Array[String]): Unit = {
    Game.start(
      10,
      25,
      ColorSchemes.getVibrantColorScheme, // higher order functions for defining color scheme
      RandomGeneration.generateGrid, // higher order functions for defining the generation strategy
      List(
        Player.createPlayer("Player 1", () => None),
        Player.createPlayer("Player 2", ScriptAI.createScriptAI(List(
          Paradigm.FUNCTIONAL, Paradigm.DECLARATIVE, Paradigm.OO
        ))),
        Player.createPlayer("Player 3", RandomAI.getParadigm),
        Player.createPlayer("Player 4", RandomAI.getParadigm)
      )
    )
  }

}