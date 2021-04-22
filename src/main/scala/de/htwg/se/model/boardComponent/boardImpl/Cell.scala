package de.htwg.se.model.boardComponent.boardImpl

import java.util.concurrent.ThreadLocalRandom

import de.htwg.se.model.boardComponent.CellInterface
import de.htwg.se.model.playerComponent.Player

/**
 * @author Ronny Klotz & Alina Göttig
 * @since 9.Nov.2021
 */

case class Cell(name: String,
                dmg: String,
                hp: Int,
                speed: Int,
                style: Boolean,
                multiplier: Int,
                player: Player)
    extends CellInterface {

    override def toString(): String = "│ " + name + " │"

    def attackable(): String = "│>" + name + "<│"

    def attackamount(): Int = {
        val input = Vector(dmg.split("-"))
        val random = ThreadLocalRandom.current()
        val amount = if (input(0).length == 2) random.nextInt(input(0).head.toInt, input(0).last.toInt + 1) else input(0).head.toInt
        amount
    }

    def copy(name: String = this.name,
             dmg: String = this.dmg,
             hp: Int = this.hp,
             speed: Int = this.speed,
             style: Boolean = this.style,
             multiplier: Int = this.multiplier,
             player: Player = this.player): CellInterface = {
    Cell(name, dmg, hp, speed, style, multiplier, player)
    }

}
