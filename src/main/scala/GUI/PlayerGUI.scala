package GUI
import java.awt.GridLayout
import java.awt.event.{ActionEvent, ActionListener}

import Data.Paradigm
import Data.Paradigm.Paradigm
import javax.swing.{JButton, JLabel, JPanel}

class PlayerGUI(val topText: String) extends JPanel {
  setLayout(new GridLayout(3,3))
  val playerLabel = new JLabel(topText, 0)

  val fpPrefix = "FP: "
  val ooPrefix = "OO: "
  val dclPrefix = "DCL: "
  val fpLabel = new JLabel(fpPrefix + 0, 0)
  val ooLabel = new JLabel(ooPrefix + 0, 0)
  val dclLabel = new JLabel(dclPrefix + 0, 0)

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
  var chosenParadigm: Paradigm = null

  paradigm1Button.addActionListener( _ => {onParadigmClicked(Paradigm.FUNCTIONAL)})
  paradigm2Button.addActionListener( _ => {onParadigmClicked(Paradigm.OO)})
  paradigm3Button.addActionListener( _ => {onParadigmClicked(Paradigm.DECLARATIVE)})

  private def onParadigmClicked(paradigm: Paradigm) = {
    chosenParadigm = paradigm
  }

  def setNameText(text: String): Unit = {
    playerLabel.setText(text)
  }

  def setParadigmProbability(paradigm: Paradigm, probability: Double): Unit = {
    val (label, prefix) = paradigm match {
      case Data.Paradigm.FUNCTIONAL => (fpLabel, fpPrefix)
      case Data.Paradigm.OO => (ooLabel, ooPrefix)
      case Data.Paradigm.DECLARATIVE =>(dclLabel, dclPrefix)
    }
    label.setText(prefix + probability)
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
