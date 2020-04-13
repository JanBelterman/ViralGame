package AI

import Data.Paradigm.Paradigm

object ScriptAI {

  def createScriptAI(script: List[Paradigm]): () => Option[Paradigm] = {
    var moves = script
    () => moves match {
      case move::Nil => Some(move)
      case move::rest =>
        moves = rest
        Some(move)
    }
  }

}
