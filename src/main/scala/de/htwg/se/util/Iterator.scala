package de.htwg.se.util

/**
 * @author Ronny Klotz & Alina Göttig
 * @since 03.Dez.2020
 */

import de.htwg.se.model.boardComponent.boardImpl.Cell
import de.htwg.se.model.playerComponent.Player

trait CustomIterator {

    def hasNext: Boolean
    def next():(Vector[Int], Cell)

}


//noinspection ScalaStyle
class CreaturelistIterator() extends CustomIterator {
    val player = Vector(Player("Castle"),Player("Inferno"))
    val list: Vector[(Vector[Int],Cell)] = {
        // Coordinates -> (Name | Damage | Health | Speed | Type | Multiplier | Player)

        //Halberdier
        Vector(Vector(0, 0) -> Cell("HA.", "2-3", 10, 3, style = false, 28, player(0)),
        //Familiar
        Vector(14, 0) -> Cell(".FA", "1-2", 4, 5, style = false, 44, player(1)),
        //Marksman
        Vector(0, 1) -> Cell("MA.", "4-6", 10, 3, style = true, 28, player(0)),
        //Magog
        Vector(14, 1) -> Cell("MAG", "2-4", 13, 4, style = true, 20, player(1)),
        //Royal Griffin
        Vector(0, 2) -> Cell("RO.", "3-6", 10, 4, style = false, 18, player(0)),
        //Cerberus
        Vector(14, 2) -> Cell(".CE", "2-7", 25, 5, style = false, 10, player(1)),
        //Arch Angel
        Vector(0, 5) -> Cell("AN.", "50", 250, 12, style = false, 2, player(0)),
        //Arch Devil
        Vector(14, 5) -> Cell(".DE", "30-40", 200, 11, style = false, 2, player(1)),
        //Champion
        Vector(0, 8) -> Cell("CH.", "20-25", 100, 6, style = false, 4, player(0)),
        //Efreet Sultan
        Vector(14, 8) -> Cell(".EF", "16-24", 90, 9, style = false, 4, player(1)),
        //Zealot
        Vector(0, 9) -> Cell("ZE.", "10-12", 24, 5, style = true, 6, player(0)),
        //Pit Lord
        Vector(14, 9) -> Cell(".PI", "13-17", 45, 5, style = false, 6, player(1)),
        //Crusader
        Vector(0, 10) -> Cell("CR.", "7-10", 35, 4, style = false, 8, player(0)),
        //Horned Demon
        Vector(14, 10) -> Cell(".HO", "7-9", 40, 4, style = false, 8, player(1)))
    }

    var position: Int = 0;

    def hasNext: Boolean = {
        position < list.size
    }

    def next(): (Vector[Int], Cell) = {
        val creature = list(position)
            position += 1
        creature
    }
}

//noinspection ScalaStyle
class ObstacleListIterator() extends CustomIterator {
    val obstacle: Cell = Cell("XXX", "0", 0, 0, style = false, 0, Player("none"))

    val list: Vector[(Vector[Int],Cell)] = Vector(
        Vector(4, 8) -> obstacle,
        Vector(4, 7) -> obstacle,
        Vector(5, 7) -> obstacle,
        Vector(5, 6) -> obstacle,
        Vector(6, 6) -> obstacle,
        Vector(7, 6) -> obstacle,
        Vector(8, 6) -> obstacle,
        Vector(9, 6) -> obstacle,
        Vector(10, 6) -> obstacle,
        Vector(11, 5) -> obstacle,
        Vector(5, 8) -> obstacle,
        Vector(6, 8) -> obstacle,
        Vector(5, 9) -> obstacle,
        Vector(7, 8) -> obstacle,
        Vector(7, 9) -> obstacle,
        Vector(6, 9) -> obstacle)


    var position: Int = 0;

    def hasNext: Boolean = {
        position < list.size
    }

    def next(): (Vector[Int], Cell) = {
        val obstacle = list(position)
        position += 1
        obstacle
    }
}