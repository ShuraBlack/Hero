package de.htwg.se.Hero.util

import de.htwg.se.model.boardComponent.boardImpl
import de.htwg.se.model.playerComponent.Player
import de.htwg.se.util.CellFactory
import org.scalatest._

class FactoryMethod extends WordSpec with Matchers {

    "An FactoryMethod" when { "new" should {
        val marker = CellFactory("marker")
        val obstacle = CellFactory("obstacle")
        val emptycell = CellFactory("")
        "provide the different cells" in {
            marker should be(boardImpl.Cell(" _ ", "0", 0, 0, style = false, 0, Player("none")))
            obstacle should be(boardImpl.Cell("XXX", "0", 0, 0, style = false, 0, Player("none")))
            emptycell should be(boardImpl.Cell("   ", "0", 0, 0, style = false, 0, Player("none")))
        }
    }
    }

}
