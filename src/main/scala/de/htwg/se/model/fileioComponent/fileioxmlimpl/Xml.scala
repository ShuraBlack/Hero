package de.htwg.se.model.fileioComponent.fileioxmlimpl

import java.io.{File, PrintWriter}
import de.htwg.se.controller.controllerComponent.ControllerInterface
import de.htwg.se.model.boardComponent.CellInterface
import de.htwg.se.model.fileioComponent.FileIOInterface
import de.htwg.se.model.playerComponent.Player
import scala.collection.mutable.ListBuffer
import scala.xml.{Node, PrettyPrinter}

/**
 * @author Ronny Klotz & Alina Göttig
 * @since 14.Jan.2021
 */

class Xml extends FileIOInterface{

    override def load(controller: ControllerInterface): Boolean = {
        if (scala.reflect.io.File("HeroSave.xml").exists) {

            controller.clearCreatures()
            val basecell: CellInterface = controller.board.currentcreature
            val source = scala.xml.XML.loadFile("HeroSave.xml")

            val fieldcreatures = (source \ "field") \ "cell"
            for (c <- fieldcreatures) {
                controller.loadCreature(Integer.parseInt((c \ "x").text)
                    , Integer.parseInt((c \ "y").text), extractCreature(c, basecell))
            }

            controller.loadCurrentplayer(Player((source \ "cp").head.text))
            controller.loadCurrentCreature(extractCreature(((source \ "cc") \ "cell").head, basecell))

            for (c <- (source \ "list") \ "cell") {
                controller.loadList(extractCreature(c, basecell))
            }

            val log = (source \ "log").head.text.split("\n")
            if (log.nonEmpty) {
                for (entry <- log) {
                    if (!entry.equals("    ") && !entry.equals("")) {
                        controller.loadLog(entry)
                    }
                }
            }

            true

        } else {

            print("No file to load a save game! -> HeroSave.xml")
            false

        }
    }

    override def save(controller: ControllerInterface): Unit = {
        val pw = new PrintWriter(new File("HeroSave.xml"))
        val xml = boardToXml(controller)
        pw.write(new PrettyPrinter(80,4).format(xml))
        pw.close()
    }

    def extractCreature (c:Node, basecell: CellInterface): CellInterface = {
        basecell.copy((c \ "name").text,
            (c \ "damage").text,
            (c \ "health").text.toInt,
            (c \ "speed").text.toInt,
            (c \ "style").text.toBoolean,
            (c \ "multiplier").text.toInt,
            Player((c \ "player").text))
    }

    def boardToXml(controller: ControllerInterface): Node = {

        var creatures = new ListBuffer[Node]()
        for (x <- 0 to 10) {
            for (y <- 0 to 14) {
                if (!controller.board.field(x)(y).name.equals("   ") &&
                    !controller.board.field(x)(y).name.equals("XXX") &&
                    !controller.board.field(x)(y).name.equals(" _ ")) {

                    creatures += cellToXml(x,y,controller.board.field(x)(y))(typ = true)

                }
            }
        }
        creatures.toList

        val field: Node = <field>{ creatures.map(c => c) }</field>
        val currentplayer: Node = <cp>{ controller.board.currentplayer.name }</cp>
        val currentcreature: Node = <cc>{ cellToXml(0, 0, controller.board.currentcreature)(typ = false) }</cc>
        val list: Node = <list>{ controller.board.list.map(c => cellToXml(0, 0, c)(typ = false)) }</list>
        val log: Node = <log>{ controller.board.log.map(l => l + "\n") }</log>

        val board = <board>{ field }{ currentplayer }{ currentcreature }{ list }{ log }</board>
        board
    }

    def cellToXml(Y: Int, X: Int, cell: CellInterface)(typ: Boolean): Node = {
        if (typ){
            <cell>
                <x>{ X }</x>
                <y>{ Y }</y>
                <name>{ cell.name }</name>
                <damage>{ cell.dmg }</damage>
                <health>{ cell.hp }</health>
                <speed>{ cell.speed }</speed>
                <style>{ cell.style }</style>
                <multiplier>{ cell.multiplier }</multiplier>
                <player>{ cell.player.name }</player>
            </cell>
        } else {
            <cell>
                <name>{ cell.name }</name>
                <damage>{ cell.dmg }</damage>
                <health>{ cell.hp }</health>
                <speed>{ cell.speed }</speed>
                <style>{ cell.style }</style>
                <multiplier>{ cell.multiplier }</multiplier>
                <player>{ cell.player.name }</player>
            </cell>
        }
    }

}
