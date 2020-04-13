package GUI
import java.awt.GridLayout

import Data.Paradigm
import Data.Paradigm.Paradigm
import javax.swing.{JButton, JLabel, JPanel}

class PlayerGUI(val player: GameTypes.PlayerTuple) extends JPanel {

  val (getPlayerName, getProbability, increaseProbability, getVideoParadigm): GameTypes.PlayerTuple = player

  setLayout(new GridLayout(3,3))
  val playerLabel = new JLabel(getPlayerName(), 0)

  val fpPrefix = "FP: "
  val ooPrefix = "OO: "
  val dclPrefix = "DCL: "
  val fpLabel = new JLabel(fpPrefix + 0 + "%", 0)
  val ooLabel = new JLabel(ooPrefix + 0 + "%", 0)
  val dclLabel = new JLabel(dclPrefix + 0 + "%", 0)

  val paradigm1Button = new JButton("Create FP video")
  val paradigm2Button = new JButton("Create OO video")
  val paradigm3Button = new JButton("Create DCL video")

  add(new JLabel())
  add(playerLabel)
  add(new JLabel())
  add(fpLabel)
  add(ooLabel)
  add(dclLabel)

  add(paradigm1Button)
  add(paradigm2Button)
  add(paradigm3Button)

  setInteractable(false)

  var chosenParadigm: Paradigm = _
  paradigm1Button.addActionListener( _ => chosenParadigm = Paradigm.FUNCTIONAL)
  paradigm2Button.addActionListener( _ => chosenParadigm = Paradigm.OO)
  paradigm3Button.addActionListener( _ => chosenParadigm = Paradigm.DECLARATIVE)

  def setNameText(text: String): Unit = {
    playerLabel.setText(text)
  }

//  def setParadigmProbability(paradigm: Paradigm, probability: Double): Unit = {
//    val (label, prefix, newProbability) = paradigm match {
//      case Data.Paradigm.FUNCTIONAL =>
//        functionalProbability = probability
//        (fpLabel, fpPrefix, functionalProbability)
//      case Data.Paradigm.OO =>
//        ooProbability = probability
//        (ooLabel, ooPrefix, ooProbability)
//      case Data.Paradigm.DECLARATIVE =>
//        declarativeProbability = probability
//        (dclLabel, dclPrefix, declarativeProbability)
//    }
//    label.setText(prefix + (newProbability * 100).toInt + "%")
//  }

  def updateProbability(): Unit = {
    fpLabel.setText(fpPrefix + (getProbability(Paradigm.FUNCTIONAL) * 100).toInt + "%")
    ooLabel.setText(ooPrefix + (getProbability(Paradigm.OO) * 100).toInt + "%")
    dclLabel.setText(dclPrefix + (getProbability(Paradigm.DECLARATIVE) * 100).toInt + "%")
  }

  def askInput(): Paradigm = {
    chosenParadigm = null
    setInteractable(true)
    while(chosenParadigm == null){
      Thread.sleep(10)
    }
    setInteractable(false)
    chosenParadigm
  }

  private def setInteractable(isInteractable: Boolean): Unit = {
    paradigm1Button.setVisible(isInteractable)
    paradigm2Button.setVisible(isInteractable)
    paradigm3Button.setVisible(isInteractable)
  }

}
