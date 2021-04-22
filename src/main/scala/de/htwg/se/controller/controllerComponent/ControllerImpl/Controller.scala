package de.htwg.se.controller.controllerComponent.ControllerImpl

/**
 * @author Ronny Klotz & Alina Göttig
 * @since 17.Nov.2020
 */

import com.google.inject.{Guice, Inject, Injector}
import de.htwg.se.HeroModule
import de.htwg.se.controller.controllerComponent.ControllerInterface
import de.htwg.se.model.boardComponent.{BoardInterface, CellInterface}
import de.htwg.se.model.boardComponent.boardImpl.Board
import de.htwg.se.model.fileioComponent.FileIOInterface
import de.htwg.se.model.playerComponent.Player
import de.htwg.se.util._

//noinspection ScalaStyle
class Controller @Inject () extends Observable with ControllerInterface {

    val player: Vector[Player] = Vector(Player("Castle"),Player("Inferno"))
    var gamestate = "mainmenu"
    var board: BoardInterface = inizGame()

    val injector: Injector = Guice.createInjector(new HeroModule)
    val fileIO: FileIOInterface = injector.getInstance(classOf[FileIOInterface])

    // ----------------------------------------------- Initial Game ----------------------------------------------------

    def inizGame(): BoardInterface= {

        val emptyboard = Board(Vector.fill(11, 15)(CellFactory("")),player,player(0),CellFactory(""),List.empty,List.empty)
        val boardpart = placeCreatures(emptyboard, new CreaturelistIterator)
        board = boardpart

        board = board.copy(board.field,player,player(0),CellFactory(""),createCreatureList(), board.log)
        val creature = board.list(board.list.indexOf(board.list.last))
        board = board.copy(board.field, board.player, creature.player, creature, board.list, board.log)

        next()
        prediction()
        board
    }

    def createCreatureList(): List[CellInterface] = {
        val field = board.field
        List(field(0)(0),field(0)(14),field(1)(0),field(1)(14),field(2)(0),field(2)(14),field(5)(0),
            field(5)(14),field(8)(0),field(8)(14),field(9)(0),field(9)(14),field(10)(0),field(10)(14))
    }

    // ----------------------------------------------- Short liner -----------------------------------------------------

    def winnercreatures: List[CellInterface] = { board.list.filter(Cell => Cell.multiplier > 0) }

    def active(X: Int, Y: Int): Boolean =
        if (getCreature(board.field, X, Y).player.name == board.currentplayer.name) true else false

    def load(): Boolean = fileIO.load(this)

    def save(): Unit = fileIO.save(this)

    // --------------------------------------------------- Start -------------------------------------------------------

     def placeCreatures(board: BoardInterface, iterator: CreaturelistIterator): BoardInterface = {

        val info = iterator.next()
        val creature = info._2
        val coor = info._1

        val creatureadd = board.copy(board.field.updated(coor(1),board.field(coor(1)).updated(coor(0),creature)), board.player,
            board.currentplayer, board.currentcreature, board.list, board.log)

        if(iterator.hasNext) {
            placeCreatures(creatureadd,iterator)
        } else {
            placeObstacles(creatureadd, new ObstacleListIterator)
        }
    }

    def placeObstacles(board: BoardInterface, iterator: ObstacleListIterator): BoardInterface = {

        val info = iterator.next()
        val obstacle = info._2
        val coor = info._1

        val obstacleadd = board.copy(board.field.updated(coor(1),board.field(coor(1)).updated(coor(0),obstacle)),
            board.player, board.currentplayer, board.currentcreature, board.list, board.log)

        if(iterator.hasNext) {
            placeObstacles(obstacleadd,iterator)
        } else {
            obstacleadd
        }
    }

    // ------------------------------------------- Load game file ------------------------------------------------------

    def loadCreature(X: Int, Y: Int, cell: CellInterface): BoardInterface = {
        board = board.copy(board.field.updated(Y,board.field(Y).updated(X,cell)), board.player,
            board.currentplayer, board.currentcreature, board.list, board.log)
        board
    }

    def loadCurrentplayer(player: Player): Player = {
        board = board.copy(board.field, board.player, player, board.currentcreature, board.list, board.log)
        board.currentplayer
    }

    def loadCurrentCreature(cell: CellInterface): CellInterface = {
        board = board.copy(board.field, board.player, board.currentplayer, cell, board.list, board.log)
        board.currentcreature
    }

    def loadList(cell: CellInterface): List[CellInterface] = {
        board = board.copy(board.field, board.player, board.currentplayer, board.currentcreature, board.list ++ List(cell), board.log)
        board.list
    }

    def loadLog(log: String): List[String] = {
        board = board.copy(board.field, board.player, board.currentplayer, board.currentcreature, board.list, board.log ++ List(log))
        board.log
    }

    def clearCreatures(): BoardInterface = {
        board = board.copy(board.field, board.player, board.currentplayer, board.currentcreature, List.empty, board.log)
        for (i <- 0 to 10) {
            for (j <- 0 to 14) {
                if (!board.field(i)(j).name.equals("   ") && !board.field(i)(j).name.equals("XXX")) {
                    board = board.copy(board.field.updated(i,board.field(i).updated(j,CellFactory(""))), board.player,
                        board.currentplayer, board.currentcreature, board.list, board.log)
                }
            }
        }
        board
    }

    // ---------------------------------------- State of game Change ---------------------------------------------------

    def next(): CellInterface = {

        val list = board.list
        val index = list.indexOf(board.currentcreature) + 1

        if (index == list.length) {
            board = board.copy(board.field, board.player, list.head.player, list.head, board.list, board.log)
            if (list.head.multiplier <= 0) {
                next()
            } else {
                clear()
                list.head
            }
        } else {
            board = board.copy(board.field, board.player, list(index).player, list(index), board.list, board.log)

            if (list(index).multiplier <= 0) {
                next()
            } else {
                clear()
                list(index)
            }
        }
    }

    def winner(): Option[Int] = {

        val player1 = board.list.exists(Cell => Cell.player.equals(player.head) && Cell.multiplier > 0)
        val player2 = board.list.exists(Cell => Cell.player.equals(player.last) && Cell.multiplier > 0)

        if (player1 && !player2) Some(1) else if (!player1 && player2) Some(2) else None
    }

    // --------------------------------------------------- Attack ------------------------------------------------------

    def attack(Y1: Int, X1: Int, Y2: Int, X2: Int): String = {
        val attacker = board.field(Y1)(X1)
        val defender = board.field(Y2)(X2)

        val damage = attacker.attackamount() * attacker.multiplier
        val basehp = findbasehp(defender.name)
        val tempstats = (defender.multiplier * basehp + defender.hp) - damage
        val temphealth = tempstats % basehp
        val newhealth = if(temphealth == 0) basehp else temphealth
        val newmultiplier = if(temphealth == 0) (tempstats / basehp) - 1 else tempstats / basehp
        val newCell = defender.copy(defender.name, defender.dmg, newhealth,
            defender.speed, defender.style, newmultiplier,defender.player)

        replaceCreatureInList(defender,newCell)

        board = board.copy(board.field.updated(Y2, board.field(Y2).updated(X2, newCell)),
            board.player, board.currentplayer, board.currentcreature, board.list, board.log)
        val loginfo = List(board.realname(attacker.name) + " dealt " + damage + " points to "
            + board.realname(defender.name))

        if (deathcheck(Y2,X2)) {
            val renwed = List(loginfo.head + ". The creature got killed!")
            replaceCreatureInList(newCell, newCell.copy(newCell.name, newCell.dmg, 0, newCell.speed,
                newCell.style, 0, newCell.player))
            board = board.copy(board.field, board.player, board.currentplayer, board.currentcreature, board.list, board.log ++ renwed)
        } else {
            board = board.copy(board.field, board.player, board.currentplayer, board.currentcreature, board.list, board.log ++ loginfo)
        }
        damage.toString
    }

    def replaceCreatureInList(oldC: CellInterface, newC: CellInterface): CellInterface = {
        val tmp = board.list
        val index = tmp.indexOf(oldC)
        val (left, _ :: right) = tmp.splitAt(index)
        val newList = left ++ List(newC) ++ right
        board = board.copy(board.field, board.player, board.currentplayer, board.currentcreature, newList, board.log)
        newC
    }

    def deathcheck(X: Int, Y: Int): Boolean = {
        val field = board.field
        if (field(X)(Y).multiplier <= 0 || field(X)(Y).hp < 0) {
            board = board.copy(field.updated(X, field(X).updated(Y, CellFactory(""))), board.player,
                board.currentplayer, board.currentcreature, board.list, board.log)
            true
        } else {
            false
        }
    }

    def findbasehp(name: String): Int = {
        val iterator = new CreaturelistIterator
        val creature = iterator.list.filter(cell => cell._2.name.equals(name))
        creature(0)._2.hp
    }

    // ---------------------------------------------- Board interaction ------------------------------------------------

    def move(X1: Int, Y1: Int, X2: Int, Y2: Int): Vector[Int] = {
        val field = board.field
        val cret1 = field(X1)(Y1)
        val cret2 = field(X2)(Y2)
        board = board.copy(field.updated(X1, field(X1).updated(Y1, cret2)), board.player,
            board.currentplayer, board.currentcreature, board.list, board.log)
        board = board.copy(board.field.updated(X2, board.field(X2).updated(Y2, cret1)),
            board.player, board.currentplayer, board.currentcreature, board.list, board.log)
        Vector(X2,Y2)
    }

    def prediction(): Vector[Vector[CellInterface]] = {
        val creature = position(board.currentcreature)
        for (j <- 0 to 10) {
            for (i <- 0 to 14) {

                val field = board.field
                val dist = Math.abs(creature(0) - j) + Math.abs(creature(1) - i)
                
                if (field(j)(i).name.equals("   ") && dist <= field(creature(0))(creature(1)).speed) {
                    board = board.copy(field.updated(j, field(j).updated(i, CellFactory("marker"))), board.player,
                        board.currentplayer, board.currentcreature, board.list, board.log)
                }
            }
        }
        board.field
    }

    def clear(): Vector[Vector[CellInterface]] = {
        for (i <- 0 to 14) {
            for (j <- 0 to 10) {
                val field = board.field
                if (field(j)(i).name.equals(" _ ")) {
                    board = board.copy(field = field.updated(j, field(j).updated(i, CellFactory(""))),
                        board.player, board.currentplayer, board.currentcreature, board.list, board.log)
                }
            }
        }
        board.field
    }

    def intpos(creature: String): Vector[Int] = {
        val field = board.field
        for (i <- 0 to 10) {
            for (j <- 0 to 14) {
                if (field(i)(j).name.equals(creature)) {
                    return Vector(i, j)
                }
            }
        }
        Vector(-1, -1)
    }

    def position(creature: CellInterface): Vector[Int] = intpos(creature.name)

    def cheatCode(code: Vector[String]): String = {
         code(1) match {
             case "coconuts" =>
                 val newC = board.currentcreature.copy(board.currentcreature.name, board.currentcreature.dmg,
                     board.currentcreature.hp, 20, board.currentcreature.style, board.currentcreature.multiplier, board.currentcreature.player)
                 changeStats(newC)
                 prediction()
                 val info = List(board.currentplayer + " cheated! Activated infinity movment speed for "
                     + board.realname(board.currentcreature.name))
                 board = board.copy(board.field, board.player, board.currentplayer, board.currentcreature, board.list, board.log ++ info)
                 info.last
             case "godunit" =>
                 val newC = board.currentcreature.copy(board.currentcreature.name, "1000", 1000, 20,
                     board.currentcreature.style, 1000, board.currentcreature.player)
                 changeStats(newC)
                 prediction()
                 val info = List(board.currentplayer + " cheated! Activated god mode for "
                     + board.realname(board.currentcreature.name))
                 board = board.copy(board.field, board.player, board.currentplayer,
                     board.currentcreature, board.list, board.log ++ info)
                 info.last
             case "feedcreature" =>
                 val basestats = baseStats()
                 val newC = board.currentcreature.copy(board.currentcreature.name, board.currentcreature.dmg, basestats(1),
                     board.currentcreature.speed, board.currentcreature.style, basestats(0), board.currentcreature.player)
                 changeStats(newC)
                 val info = List(board.currentplayer + " cheated! Set " + board.realname(board.currentcreature.name)
                     + " values back to beginning")
                 board = board.copy(board.field, board.player, board.currentplayer, board.currentcreature, board.list, board.log ++ info)
                 info.last
             case "handofjustice" =>
                 val enemy = board.list.filter(Cell => !Cell.player.name.equals(board.currentplayer.name))
                 for (cell <- enemy) {
                     val newC = cell.copy(cell.name, cell.dmg, 0, cell.speed, cell.style, 0, cell.player)
                     replaceCreatureInList(cell,newC)
                     val coor = position(cell)
                     board = board.copy(field = board.field.updated(coor(0), board.field(coor(0)).updated(coor(1), CellFactory(""))),
                         board.player, board.currentplayer, newC, board.list, board.log)
                 }
                 val info = List(board.currentplayer + " cheated! Killed all enemy creatures")
                 board = board.copy(board.field, board.player, board.currentplayer, board.currentcreature, board.list, board.log ++ info)
                 gamestate = "finished"
                 info.last
         }
    }

    def changeStats(newC: CellInterface): Boolean = {
        replaceCreatureInList(board.currentcreature,newC)
        val coor = position(board.currentcreature)
        board = board.copy(field = board.field.updated(coor(0), board.field(coor(0)).updated(coor(1), newC)),
            board.player, board.currentplayer, newC, board.list, board.log)
        true
    }

    def baseStats(): Vector[Int] = {
        val iterator = new CreaturelistIterator
        val creature = iterator.list.filter(cell => cell._2.name.equals(board.currentcreature.name))
        Vector(creature(0)._2.multiplier,creature(0)._2.hp)
    }

    // ---------------------------------------------- Input checks -----------------------------------------------------

    def checkmove(in:Vector[String]): Boolean =
        if (in(0) == "m" && getCreature(board.field, in(2).toInt, in(1).toInt).name.equals(" _ ")) true else false


    def checkattack(in:Vector[String]) : Boolean = {
        val i = in(2).toInt
        val j = in(1).toInt
        val field = board.field
        if (!field(i)(j).name.equals("   ") && !field(i)(j).name.equals(" _ ") && !field(i)(j).name.equals("XXX")
            && !active(i, j)) {
            if (board.currentcreature.style) {
                return true
            } else if (areacheck(i,j)) {
                return true
            }
        }
        false
    }

    def areacheck (i: Int, j: Int) : Boolean = {
        val field = board.field
        val list: List[Any] = List(if(i - 1 >= 0 && j - 1 >= 0) field(i - 1)(j - 1).name,
            if(i - 1 >= 0) field(i - 1)(j).name,
            if(i - 1 >= 0 && j + 1 < 14) field(i - 1)(j + 1).name,
            if(j - 1 >= 0) field(i)(j - 1).name,
            if(j + 1 < 14) field(i)(j + 1).name,
            if(i + 1 < 11 && j - 1 >= 0) field(i + 1)(j - 1).name,
            if(i + 1 < 11) field(i + 1)(j).name,
            if(i + 1 < 11 && j + 1 < 14) field(i + 1)(j + 1).name)
        list.exists(name => name.equals(" _ "))
    }

    // ---------------------------------------------- Output Strings ---------------------------------------------------

    def printfield(): String = {
        val field = board.field
        var text = ""
        for (x <- 0 to 14) {
            text += fieldnumber(x.toString)
        }
        text += "\n" + lines()
        for (i <- 0 to 10) {
            for (j <- 0 to 14) {
                if (!field(i)(j).name.equals("   ") && !field(i)(j).name.equals(" _ ") && !field(i)(j).name.equals("XXX") && !active(i, j)) {
                    if (areacheck(i,j)) {
                        text += field(i)(j).attackable()
                    } else if (board.currentcreature.style) {
                        text += field(i)(j).attackable()
                    }else {
                        text += field(i)(j).toString()
                    }
                } else {
                    text += field(i)(j).toString()
                }

            }
            text += " " + i.toString + "\n" + lines()
        }
        text
    }

    def printSidesStart(): String = {
        val player1 = "| " + player(0).name + " |"
        val player2 = "| " + player(1).name + " |"
        val middle = player1 + " " * (105 - player1.length - player2.length) + player2

        "\n" + lines() + middle + "\n" + lines()
    }

    def output: String = {
        printfield() + "\n" + board.currentplayerinfo() + "\n" +
            board.currentcreatureinfo() + "\n" + board.lastlog()
    }

    def info(in:Vector[String]) : String = {
        print(board.creatureinfo(in(1).toInt, in(2).toInt))
        board.creatureinfo(in(1).toInt, in(2).toInt)
    }

    def endInfo(playernumber: Int): String = {
        val player = if (playernumber == 1) "Castle" else "Inferno"
        val top = "\n" + "=" * 2 + " Result for the game: " + "=" * 81 + "\n" + player + " won the game !" + "\n" +
            "=" * 2 + " Creatures alive: " + "=" * 85 + "\n"
        val listofliving = winnercreatures
        val title = "Name:\t\t\tMultiplier:\t\t\tHealth:\n"
        var middle = ""
        for (cell <- listofliving) {
            val name = board.realname(cell.name)
            val multi = cell.multiplier
            val hp = cell.hp
            middle += name + " " * (16 - name.length) + multi + " " * (20 - multi.toString.length) + hp + "\n"
        }
        top + title + middle + lines()
    }

}
