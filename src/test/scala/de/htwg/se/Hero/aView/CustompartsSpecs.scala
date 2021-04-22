package de.htwg.se.Hero.aView

import de.htwg.se.aview.{CoorButton, ImagePanel, ShortButton, ShortLabel}
import de.htwg.se.controller.controllerComponent.ControllerImpl.Controller
import de.htwg.se.model.boardComponent.boardImpl.Cell
import de.htwg.se.model.playerComponent.Player
import org.scalatest.{Matchers, WordSpec}

import scala.swing.{Alignment, Color, Font}

class CustompartsSpecs extends WordSpec with Matchers {

    "A ImagePanel" when { "new" should {
        val imagepanel = new ImagePanel()
        imagepanel.imagePath = "src/main/scala/de/htwg/se/aview/Graphics/UI/Buttonframe.png"
        "have the picture path"  in {
            imagepanel.imagePath should be("src/main/scala/de/htwg/se/aview/Graphics/UI/Buttonframe.png")
        }
    }}

    "A CoorButton" when { "new" should {
        val controller = new Controller()
        val creature = Cell("HA.", "2-3", 10, 3, style = false, 28, Player("0"))
        val creatureranged = Cell("MA.", "4-6", 10, 3, style = true, 28, Player("None"))
        val coorbutton = new CoorButton(1,2,creature,controller)
        val coorButtonranged = new CoorButton(1,2,creatureranged,controller)
        "have the full creaturename"  in {
            coorbutton.cellname should be("Ha_lberdier")
            coorButtonranged.next()
        }
    }}

    "A ShortLabel" when { "new" should {
        val shortlabel = new ShortLabel("Test",20,new Color(0,0,0))(true)
        "have values in"  in {
            shortlabel.foreground should be(new Color(0,0,0))
            shortlabel.font should be (new Font("Arial",1, 20))
            shortlabel.text should be ("Test")
        }
        "have settings set" in {
            shortlabel.horizontalTextPosition should be (Alignment.Center)
            shortlabel.verticalTextPosition should be (Alignment.Center)
        }
    }}

    "A ShortButton" when { "new" should {
        val shortbutton = new ShortButton("Test",20,new Color(0,0,0))(true)(true)
        "have vales in"  in {
            shortbutton.foreground should be(new Color(0,0,0))
            shortbutton.font should be (new Font("Arial",1, 20))
            shortbutton.text should be ("Test")
        }
        "have settings set" in {
            shortbutton.opaque should be (false)
            shortbutton.focusPainted should be (false)
            shortbutton.contentAreaFilled should be (false)
            shortbutton.borderPainted should be (false)
            shortbutton.horizontalTextPosition should be (Alignment.Center)
            shortbutton.verticalTextPosition should be (Alignment.Center)
        }
    }}

}
