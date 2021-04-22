package de.htwg.se.Hero.model

import de.htwg.se.controller.controllerComponent.ControllerImpl.Controller
import de.htwg.se.model.fileioComponent.fileioxmlimpl.Xml
import de.htwg.se.model.playerComponent.Player
import de.htwg.se.model.boardComponent.boardImpl.Cell
import org.scalatest._

class XmlSpec extends WordSpec with Matchers {

    "A Xml" when { "new" should {
        val xml = new Xml()
        val controller = new Controller ()
    "have the function returns" in {
        xml.load(controller) should be (true)
        // Depends on file exists xml.load(controller) should be (true)

        val cell = Cell("Test","1-10",0,0,true,0,Player("Test"))
        val creature = <cell>
            <name>{ cell.name }</name>
            <damage>{ cell.dmg }</damage>
            <health>{ cell.hp }</health>
            <speed>{ cell.speed }</speed>
            <style>{ cell.style }</style>
            <multiplier>{ cell.multiplier }</multiplier>
            <player>{ cell.player.name }</player>
        </cell>
        xml.extractCreature(creature,cell).name should be (cell.name)

        val Xmlsave = xml.boardToXml(controller)
        (Xmlsave \ "field").head.label should be ("field")
        (Xmlsave \ "cp").head.label should be ("cp")
        (Xmlsave \ "list").head.label should be ("list")
        (Xmlsave \ "log").head.label should be ("log")
        xml.save(controller);

        (xml.cellToXml(14,13,cell)(true) \ "x").text should be ("13")
        (xml.cellToXml(14,13,cell)(true) \ "y").text should be ("14")
        (xml.cellToXml(0,0,cell)(false) \ "name").text should be (cell.name)
    }}}
}
