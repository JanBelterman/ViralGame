package Gridland

import Data.Paradigm
import Data.Paradigm.Paradigm
import GUI.GameTypes

object Player {

  type createPlayer = (String, () => Option[Paradigm]) => GameTypes.PlayerTuple

  // Function as object
  def createPlayer: createPlayer = (name: String, playerStrategy: () => Option[Paradigm]) => {
    var probabilities = Map((Paradigm.FUNCTIONAL, 0.0), (Paradigm.OO, 0.0), (Paradigm.DECLARATIVE, 0.0))

    val getName = () => {
      name
    }

    val getProbability = (paradigm: Paradigm) => {
      probabilities.get(paradigm) match {
        case None => throw new IllegalStateException("Paradigm not implemented")
        case Some(probability) => probability
      }
    }

    val increaseParadigmProbability = (paradigm: Paradigm) => {
      probabilities = probabilities + (paradigm -> (getProbability(paradigm) + 0.1))
    }

    (getName, getProbability, increaseParadigmProbability, playerStrategy)
  }

}
