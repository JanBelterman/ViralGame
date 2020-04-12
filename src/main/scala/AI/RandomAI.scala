package AI

import Data.Paradigm
import Data.Paradigm.Paradigm

object RandomAI {

  def getParadigm(): Option[Paradigm] = {
    scala.util.Random.nextInt(3) match {
      case 0 => Some(Paradigm.FUNCTIONAL)
      case 1 => Some(Paradigm.OO)
      case 2 => Some(Paradigm.DECLARATIVE)
    }
  }

}
