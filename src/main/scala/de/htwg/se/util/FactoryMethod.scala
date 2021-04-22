package de.htwg.se.util

/**
 * @author Ronny Klotz & Alina Göttig
 * @since 03.Dez.2020
 */

import de.htwg.se.model.boardComponent.boardImpl.Cell
import de.htwg.se.model.playerComponent.Player

trait SpecialCell {}

object CellFactory {

     def apply(s: String): Cell = {
         s match {
             case "marker" => Cell(" _ ", "0", 0, 0, style = false, 0, Player("none"))
             case "obstacle" => Cell("XXX", "0", 0, 0, style = false, 0, Player("none"))
             case "" => Cell("   ", "0", 0, 0, style = false, 0, Player("none"))
         }
     }

}
