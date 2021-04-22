package de.htwg.se.model.boardComponent.boardImpl

import de.htwg.se.model.boardComponent.{BoardInterface, CellInterface}
import de.htwg.se.model.playerComponent.Player

/**
 * Scala project for the game Hero (based on Heroes of Might and Magic III - Fight)
 *
 * @author Ronny Klotz & Alina Göttig
 * @since 9.Nov.2020
 */

//noinspection ScalaStyle
case class Board (field: Vector[Vector[Cell]],
                 player: Vector[Player],
                 currentplayer: Player,
                 currentcreature: Cell,
                 list: List[Cell],
                 log: List[String])
    extends BoardInterface {

    def currentcreatureinfo(): String = {
        val attackstyle = if (currentcreature.style) "Ranged" else "Melee"
        val name = realname(currentcreature.name)
        "=" * 2 + " Info " + "=" * 97 + "\n" + "Current Unit:\t\t\t\tMultiplier:\t\t\t\tHP:\t\t\t\tDamage:\t\t\t\tAttackstyle:" + "\n" +
            name + " " * (28 - name.length) + currentcreature.multiplier + " " * (24 - currentcreature.multiplier.toString.length) +
            currentcreature.hp + " " * (16 - currentcreature.hp.toString.length) + currentcreature.dmg +
            " " * (20 - currentcreature.dmg.length) + attackstyle + "\n" + lines()
    }

    def creatureinfo(Y: Int, X: Int): String = {
        val shortline = "=" * 105 + "\n"
        val name = realname(field(X)(Y).name)
        val multi = field(X)(Y).multiplier
        val hp = field(X)(Y).hp
        "=" * 2 + " Info " + "=" * 97 + "\n" + "Creature:\t\t\tMultiplier:\t\t\tHP:" + "\n" +
            name + " " * (20 - name.length) + multi + " " * (20 - multi.toString.length) + hp + "\n" + shortline
    }

    def currentplayerinfo(): String = {
        val shortline = "=" * 2 + " Current Player: " + "=" * 86
        shortline + "\n" + currentplayer.name + "\n" + lines()
    }

    def lastlog(): String = {
        val info = if (log.nonEmpty) log.last + "\n" else "\n"
        "==" + " Log: " + "=" * 97 + "\n" + info + lines()
    }

    def copy(field: Vector[Vector[CellInterface]] = this.field,
             player: Vector[Player] = this.player,
             currentplayer: Player = this.currentplayer,
             currentcreature: CellInterface = this.currentcreature,
             list: List[CellInterface] = this.list,
             log: List[String] = this.log): BoardInterface= Board(field.asInstanceOf[Vector[Vector[Cell]]],
        player, currentplayer,currentcreature.asInstanceOf[Cell],list.asInstanceOf[List[Cell]], log)
}
