package de.htwg.se.model.fileioComponent.fileiojsonimpl

import java.io._

import de.htwg.se.controller.controllerComponent.ControllerInterface
import de.htwg.se.model.boardComponent.CellInterface
import de.htwg.se.model.fileioComponent.FileIOInterface
import de.htwg.se.model.playerComponent.Player
import play.api.libs.json._

import scala.io.Source

/**
 * @author Ronny Klotz & Alina Göttig
 * @since 14.Jan.2021
 */

class Json extends FileIOInterface{
    override def load(controller: ControllerInterface): Boolean = {
        //um die Methode Cell.copy zu verwenden
        val tmp: CellInterface = controller.board.field(0)(0)

        if (scala.reflect.io.File("HeroSave.json").exists) {
            val source: String = Source.fromFile("HeroSave.json").getLines.mkString
            val json: JsValue = Json.parse(source)

            val currentplayer: String = (json \ "cp").get.toString()
            val currentcreature: CellInterface = reCell(tmp, (json \ "cc").get.as[JsObject])._3
            val list: List[CellInterface] = (json \ "list").as[List[JsObject]].map(x => reCell(tmp, x)._3)
            val log: List[String] = (json \ "log").as[List[String]]

            controller.clearCreatures()
            for (i <- 0 until (json \ "field").as[List[JsObject]].length) {
                val cell = (json \ "field") (i).as[JsObject]

                val currentCell = reCell(tmp, cell)
                controller.loadCreature(currentCell._2, currentCell._1, currentCell._3)
            }
            controller.loadCurrentplayer(Player(currentplayer))
            controller.loadCurrentCreature(currentcreature)
            list.foreach(s => controller.loadList(s))
            log.foreach(l => controller.loadLog(l))
            true
        } else {
            print("No file to load a save game! -> HeroSave.json")
            false
        }
    }

    def reCell (tmp: CellInterface, cell: JsObject): (Int, Int, CellInterface) = {
        val name = (cell \ "name").as[String]
        val dmg = (cell \ "damage").as[String]
        val hp = (cell \ "hp").as[Int]
        val speed = (cell \ "speed").as[Int]
        val style = (cell \ "style").as[Boolean]
        val multiplier = (cell \ "multiplier").as[Int]
        val player = (cell \ "player").as[String]

        val x = (cell \ "x").as[Int]
        val y = (cell \ "y").as[Int]

        (x, y, tmp.copy(name, dmg, hp, speed, style, multiplier, Player(player)))
    }

    override def save(controller: ControllerInterface): Unit = {
        var jCreatures : List[JsObject] = List.empty
        for(f <- 0 to controller.board.field.size-1) {
            for (i <- 0 to controller.board.field(f).size-1) {
                if (controller.board.list.contains(controller.board.field(f)(i)))
                    jCreatures = jCell(controller.board.field(f)(i), f, i) :: jCreatures
            }
        }
        val boardSave: JsValue = Json.obj(
            /*"creatures" -> jCreatures,*/
            "field" -> Json.toJson(
                for {
                    x <- 0 until controller.board.field.size;
                    y <- 0 until controller.board.field(0).size
                    if !controller.board.field(x)(y).name.equals("   ") && !controller.board.field(x)(y).name.equals("XXX") &&
                        !controller.board.field(x)(y).name.equals(" _ ")
                    } yield {
                    jCell(controller.board.field(x)(y), x, y)
                }
            ),
            "cp" -> controller.board.currentplayer.name,
            "cc" -> jCell(controller.board.currentcreature, controller.position(controller.board.currentcreature)(0),
                controller.position(controller.board.currentcreature)(1)),
            "list" -> controller.board.list.map(x => jCell(x, controller.position(x)(0), controller.position(x)(1))),
            "log" -> controller.board.log
        )

        val pw = new PrintWriter(new File("HeroSave.json"))
        pw.write(Json.prettyPrint(boardSave))
        pw.close
    }

    def jCell(c:CellInterface, x:Int, y:Int) : JsObject = {
        Json.obj(
            "name" -> c.name,
            "damage" -> c.dmg,
            "hp" -> c.hp,
            "speed" -> c.speed,
            "style" -> c.style,
            "multiplier" -> c.multiplier,
            "player" -> c.player.name,
            "x" -> x,
            "y" -> y
        )
    }

}
