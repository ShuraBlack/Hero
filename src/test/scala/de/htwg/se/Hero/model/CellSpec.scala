package de.htwg.se.model

import de.htwg.se.model.boardComponent.boardImpl
import de.htwg.se.model.boardComponent.boardImpl.Cell
import de.htwg.se.model.playerComponent.Player
import org.scalatest._

class CellSpec extends WordSpec with Matchers {

    "A Cell " when { "new" should {
        val cell = boardImpl.Cell("Test","10-20", 100,10,false,10,Player("Test"))
        val celleven = boardImpl.Cell("Test","10", 100,10,false,10,Player("Test"))
        "have informations"  in {
            cell.name should be("Test")
            cell.dmg should be("10-20")
            cell.hp should be(100)
            cell.style should be(false)
            cell.multiplier should be(10)
            cell.player should be(Player("Test"))

        }
        "have a nice String representation" in {
            cell.toString should be("│ Test │")
        }
        "have a marker for attackable" in {
            cell.attackable() should be("│>Test<│")
        }
        "have a copy by value" in {
            val copy = cell.copy(cell.name,cell.dmg,cell.hp,cell.speed,cell.style,cell.multiplier,cell.player)
            copy.name should be (cell.name)
        }
        "and have a damage calculation" in {
            cell.attackamount() should be >= 10
            cell.attackamount() should be <= 20
            celleven.attackamount() should be(10)
        }
    }}

    "A instance of a Cell" when { "new" should {
        val cell = Cell
        "have the typ"  in {
            cell should be(Cell)
        }
    }}

}
