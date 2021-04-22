package de.htwg.se.aview

import java.awt.Color
import java.awt.image.BufferedImage
import java.io.File

import de.htwg.se.controller.controllerComponent.ControllerInterface
import de.htwg.se.model.boardComponent.CellInterface
import javax.imageio.ImageIO

import scala.swing._
import scala.swing.event.ButtonClicked

/**
 * @author Ronny Klotz & Alina Göttig
 * @since 16.Dez.2020
 */

class ImagePanel() extends BoxPanel(Orientation.Vertical) {

    var _imagePath = ""
    var bufferedImage:BufferedImage = null

    def imagePath: String = _imagePath

    def imagePath_=(value:String) {
        _imagePath = value
        bufferedImage = ImageIO.read(new File(_imagePath))
    }


    override def paintComponent(g:Graphics2D): Unit = {
        g.drawImage(bufferedImage, 0, 0, null)
    }
}

class CoorButton(X: Int, Y: Int, cell: CellInterface, controller: ControllerInterface) extends Button {

    val currentcell: CellInterface = controller.getCreature(controller.board.field, Y, X)
    val cellname: String = controller.board.realname(cell.name)

    font = new Font("Arial", 1, 14)
    if (!cell.player.name.equals("none")) {
        horizontalTextPosition = Alignment.Right
        verticalTextPosition = Alignment.Top
        text = currentcell.multiplier.toString
        val style = if(cell.style) "Ranged" else "Melee"
        tooltip = "<html>Player: " + cell.player.name + "<br>Name: " + cellname.replaceAll("_","") +
            "<br>Damage: " + cell.dmg + "<br>Attackstyle: " + style + "<br>Speed: " + currentcell.speed +
            "<br>Multiplier: " + currentcell.multiplier + "<br>Health: " + currentcell.hp + "</html>"
    } else {
        text = "<html><center>" + cellname
    }

    reactions += {
        case ButtonClicked(_) =>
            val posi = controller.position(controller.board.currentcreature)
            if (cell.name.equals(" _ ")) {
                controller.move(posi(0), posi(1), Y, X)
                next()
            } else if (controller.checkattack(Vector("a", X.toString, Y.toString))) {
                controller.attack(posi(0), posi(1), Y, X)
                next()
            }
    }

    def next(): Unit = {
        if (controller.winner().isDefined){
            controller.gamestate = "finished"
        } else {
            controller.next()
            controller.prediction()
        }
        controller.notifyObservers
    }

}

class ShortLabel(info: String, fontsize: Int, fontcolor: Color)(alin: Boolean) extends Label {
    text = info
    font = new Font("Arial", 1, fontsize)
    foreground = fontcolor
    if (alin) {
        horizontalTextPosition = Alignment.Center
        verticalTextPosition = Alignment.Center
    }
}

class ShortButton(info: String, fontsize: Int, fontcolor: Color)(typ: Boolean)(alin: Boolean) extends Button {
    text = info
    font = new Font("Arial", 1, fontsize)
    foreground = fontcolor
    if (typ) {
        opaque = false
        focusPainted = false
        contentAreaFilled = false
        borderPainted = false
    }
    if (alin) {
        horizontalTextPosition = Alignment.Center
        verticalTextPosition = Alignment.Center
    }
}
