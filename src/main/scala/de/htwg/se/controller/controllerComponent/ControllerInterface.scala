package de.htwg.se.controller.controllerComponent

import de.htwg.se.model.boardComponent.{BoardInterface, CellInterface}
import de.htwg.se.model.playerComponent.Player
import de.htwg.se.util.{CreaturelistIterator, Observable, ObstacleListIterator}

/**
 * Scala project for the game Hero (based on Heroes of Might and Magic III - Fight)
 * @author Ronny Klotz & Alina Göttig
 * @since 10.Nov.2021
 *
 * Game logic and inner working
 */
trait ControllerInterface extends Observable  {

    /**
     * Contains both playersides (DEFAULT -> Inferno and Castle)
     */
    def player: Vector[Player]

    /**
     * Gamestate for GUI.
     * Controlls the syncro between the TUI and GUI outputs
     */
    var gamestate: String

    /**
     * The board it self with extra values like current player/creautre, combat log, creature turns etc.
     */
    var board: BoardInterface

    /**
     * Initializes the playing field and uses other functions to prepare processes for the first round
     */
    def inizGame(): BoardInterface

    /**
     * Intern function for startup.
     * Goes through the board and pick up all creature values.
     * Needed for changing the turn
     */
    def createCreatureList(): List[CellInterface]

    /**
     * Updates the current board with the board of a previously played game
     */
    def load(): Boolean

    /**
     * Saves the current game to an extern file
     */
    def save(): Unit

    /**
     * Support function for loading in extern data.
     * Takes a creature and update it at the X and Y coordinates on the current board
     */
    def loadCreature(X: Int, Y: Int, cell: CellInterface): BoardInterface

    /**
     * Support function for loading in extern data.
     * Change the current activ player on the running board
     */
    def loadCurrentplayer(player: Player): Player

    /**
     * Support function for loading in extern data.
     * Change the current activ creature on the running board
     */
    def loadCurrentCreature(cell: CellInterface): CellInterface

    /**
     * Support function for loading in extern data.
     * Adds a creature to the turn controlling list
     */
    def loadList(cell: CellInterface): List[CellInterface]

    /**
     * Support function for loading in extern data.
     * Adds a combat log to the log of the running game
     */
    def loadLog(log: String): List[String]

    /**
     * Support function for loading in extern data.
     * Prepare the board for loading in new creature.
     * Removes all creautres that are currently on the board,
     * to load in new one
     */
    def clearCreatures(): BoardInterface

    /**
     * Support function to print out the position of the board.
     * Needed only in the TUI
     */
    def fieldnumber(x: String): String = if (x.length == 2) "  " + x + "   " else "   " + x + "   "

    /**
     * Support function for TUI outputs
     * @return a String with 105 equal symbols
     */
    def lines(): String = "=" * 7 * 15 + "\n"

    /**
     * @return the cell at the coordinates of X and Y
     */
    def getCreature(field: Vector[Vector[CellInterface]], x: Int, y: Int): CellInterface = field(x)(y)

    /**
     * Used to get the remaining creatures of the winning playerside.
     * Helps the print out of the result screen in TUI and GUI
     * @return a filtered list with the searched creatures
     */
    def winnercreatures: List[CellInterface]

    /**
     * Compares the player of the cell at coordinates of X and Y and the current creature
     * Makes sure that u cant attack your own creatures
     * @return True if they are equal, otherwise false
     */
    def active(X: Int, Y: Int): Boolean

    /**
     * Fills the board with Empty cells, creatures and obstacles
     * def start(): BoardInterface
     */

    /**
     * Place the creature of the Pattern Iterator into the board field
     */
    def placeCreatures(board: BoardInterface, iterator: CreaturelistIterator): BoardInterface

    /**
     * Place the obstacles of the Pattern Iterator into the board field
     */
    def placeObstacles(board: BoardInterface, iterator: ObstacleListIterator): BoardInterface

    /**
     * Goes through the saved list of creatures on the board.
     * Skips all the creatures with a multiplier equals or lower than 0 (cause they are already dead)
     * @return the next creature cell
     */
    def next(): CellInterface

    /**
     * Filter the creature list for both seperat playersides.
     * @return different integer option depending on which player won or none with nobody won
     */
    def winner(): Option[Int]

    /**
     * Takes creature from coordinate 1 as the attack and 2 as a defender
     * @return the damage amount which the attack dealt
     */
    def attack(Y1: Int, X1: Int, Y2: Int, X2: Int): String

    /**
     * Takes the old creature of the creature list and replace it with a new one at the same place
     */
    def replaceCreatureInList(oldC: CellInterface, newC: CellInterface): CellInterface

    /**
     * Checks if the creature cell at coordinate X and Y got a multiplier of 0 or below and
     * replace it with an empty cell if its needed
     */
    def deathcheck(X: Int, Y: Int): Boolean

    /**
     * Uses the name of a creature to get his base health value back.
     * Needed for calculation in different other functions
     */
    def findbasehp(name: String): Int

    /**
     * Moves a creature from coordinate 1 to 2
     */
    def move(X1: Int, Y1: Int, X2: Int, Y2: Int): Vector[Int]

    /**
     * Replace empty cells with marker cells if they are in manhattan distance range of the current creature speed
     */
    def prediction(): Vector[Vector[CellInterface]]

    /**
     * Clear up marker and replace them with empty cells
     */
    def clear(): Vector[Vector[CellInterface]]

    /**
     * @return the vector of the creature if its on the board field
     */
    def intpos(creature: String): Vector[Int]

    /**
     * Tunnel function for intpos.
     * Coverage purposes only
     */
    def position(creature: CellInterface): Vector[Int]

    /**
     * Takes the commands in a split version and check if a cheat exists with the same calling
     * If found, the function will change the board with the cheat giving meaning
     */
    def cheatCode(code: Vector[String]): String

    /**
     * Change stats of th current creature
     */
    def changeStats(newC: CellInterface): Boolean

    /**
     * @return the base health and multiplier of the current creature
     */
    def baseStats(): Vector[Int]

    /**
     * Checks whether the creature is allowed to move onto the new field.
     * Uses marker to decide
     */
    def checkmove(in:Vector[String]): Boolean

    /**
     * Checks whether the creature is allowed to attack the given creature.
     * Uses the style of the attacking creature or checks if a marker cell is next to the defender cell
     */
    def checkattack(in:Vector[String]): Boolean

    /**
     * Checks if marker cells are around the coordinates of X and Y
     */
    def areacheck (i: Int, j: Int) : Boolean

    /**
     * Used for TUI only.
     * Represents the board as a String
     */
    def printfield(): String

    /**
     * Used for TUI only.
     * At starts of the game both player needs to know on which side there creatures are
     */
    def printSidesStart(): String

    /**
     * Used for TUI only.
     * Combines all output Strings and put them together
     */
    def output: String

    /**
     * Used for TUI only.
     * Shows all values of the current creature cell
     */
    def info(in:Vector[String]): String

    /**
     * Used for TUI only.
     * Creates a result screen
     */
    def endInfo(playernumber: Int): String

}
