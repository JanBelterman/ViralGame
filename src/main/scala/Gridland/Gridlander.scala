package Gridland

import Data.Gridlander.Gridlander

object Gridlander {

  def createGridlander(beginState: Gridlander): (Gridlander => Unit, Gridlander => Unit) = {
    var state = beginState
    def changeState(newState: Gridlander): Unit = {
      state = newState
    }
    def talk(otherState: Gridlander): Unit = {
      // todo logic
    }
    (changeState, talk)
  }

}
