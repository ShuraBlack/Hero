package de.htwg.se.model.boardComponent

import de.htwg.se.model.playerComponent.Player

/**
 * Scala project for the game Hero (based on Heroes of Might and Magic III - Fight)
 * @author Ronny Klotz & Alina Göttig
 * @since 17.Nov.2020
 *
 * Board with important informations to play the game
 */
trait BoardInterface {

    /**
     * Game field with all player and none player Cells
     */
    def field: Vector[Vector[CellInterface]]

    /**
     * Contains both playerside for setting up the creatures (constant values),
     * controll the turns and give the user an informations about the round
     */
    def player: Vector[Player]

    /**
     * Represents the currently activ player.
     * May change after a new turn
     */
    def currentplayer: Player

    /**
     * Represents the currently activ creature in dependency of the current player
     * @return Only creatures in form of a Cell
     */
    def currentcreature: CellInterface

    /**
     * The list of all creatures from both playersides.
     * At death of a creature the health and multiplier get set to 0.
     * Relevant for next turn!
     */
    def list: List[CellInterface]

    /**
     * Contains all combat actions of an running game
     * like attacking & cheating
     */
    def log: List[String]

    /**
     * @return The current creature in a special form for TUI
     */
    def currentcreatureinfo(): String

    /**
     * Is a special function only for TUI usage.
     * Y and X represents the coordinations on the game field
     * @return a String with informations about the searched creature
     */
    def creatureinfo(Y: Int, X: Int): String

    /**
     * Is a special function only for TUI usage
     * @return a String with the name of the current player
     */
    def currentplayerinfo(): String

    /**
     * @return the last entry out of the combat log or and empty String
     */
    def lastlog(): String

    /**
     * @param name is the short version of the name of a creature
     * Needs to be added if a new creature get introduced and you want the entire name
     * @return the fully given name
     */
    def realname(name: String): String = name match {
        case "HA." => "Ha_lberdier"
        case ".FA" => "Fa_miliar"
        case "MA." => "Ma_rksman"
        case "MAG" => "Mag_og"
        case "RO." => "Ro_yal Griffin"
        case ".CE" => "Ce_rberus"
        case "AN." => "Arch An_gle"
        case ".DE" => "Arch De_vil"
        case "CH." => "Ch_ampion"
        case ".EF" => "Ef_reet Sultan"
        case "ZE." => "Ze_alot"
        case ".PI" => "Pi_t Lord"
        case "CR." => "Cr_usader"
        case ".HO" => "Ho_rned Demon"
        case "   " => "   "
        case " _ " => "   "
        case "XXX" => "   "
    }

    /**
     * Support function for TUI outputs
     * @return a String with 105 equal symbols
     */
    def lines(): String = "=" * 7 * 15 + "\n"

    /**
     * Custom Copy function.
     * It´s a more Simplifed version of case class copy
     * @example board.copy(Matrix, Playerlist, Player, Creature, Turnlist, Log)
     */
    def copy(field: Vector[Vector[CellInterface]],
             player: Vector[Player],
             currentplayer: Player,
             currentcreature: CellInterface,
             list: List[CellInterface],
             log: List[String]): BoardInterface

}

/**
 * Object part of the game. Cells represents the smallest part of the board field.
 * It´s the filling of the field matrix
 * @since 17.Nov.2020
 */
trait CellInterface {

    /**
     * Name of the Cell.
     * "   " empty String if its an empty field.
     * "XXX" if its an obstacle.
     * " _ " if its an marker for walk/attack range
     * or an actually creature name
     */
    def name: String

    /**
     * Damage as an String ("100" or a range like "1-100")
     */
    def dmg: String

    /**
     * Health value scale with the multiplier to get the entire creature health
     */
    def hp: Int

    /**
     * Decide how far a creature can walk or attack
     */
    def speed: Int

    /**
     * True if the creature is ranged and false if it is melee
     */
    def style: Boolean

    /**
     * Is a scaling value and represents the size of the troop
     */
    def multiplier: Int

    /**
     * Variate between Inferno and Castle as playersides (DEFAULT)
     */
    def player: Player

    /**
     * @return the creature in a TUI based version
     */
    def toString(): String

    /**
     * @return the command printable creature in special style, if the currently activ creature is in range to attack it
     */
    def attackable(): String

    /**
     * Calculates an constant or random value, which scales with the creatures multiplier.
     * Is only random if the creatre got a range as damage
     */
    def attackamount(): Int

    /**
     * Custom Copy function
     * It´s a more Simplifed version of case class copy
     * @example cell.copy(Name, Damage, Health, Speed, Style, Multiplier, Player)
     */
    def copy(name: String,
             dmg: String,
             hp: Int,
             speed: Int,
             style: Boolean,
             multiplier: Int,
             player: Player): CellInterface

}
