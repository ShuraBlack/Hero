package de.htwg.se.Hero.aView

import de.htwg.se.aview.TUI
import de.htwg.se.controller.controllerComponent.ControllerImpl.Controller
import de.htwg.se.model.boardComponent.boardImpl.Cell
import de.htwg.se.model.playerComponent.Player
import de.htwg.se.util.{Command, SetCommand, UndoManager}
import org.scalatest._

class TuiSpec extends WordSpec with Matchers {

    "A TUI" when { "new" should {
        val controller = new Controller()
        val executer = new UndoManager
        val tui = new TUI(controller, executer)

        "have commando represent"  in {
            tui.commands() should include("a X Y")
            tui.commands() should include("m X Y")
            tui.commands() should include("i X Y")
            tui.commands() should include("undo")
            tui.commands() should include("redo")
            tui.commands() should include("p")
            tui.commands() should include("exit")
        }
        "be type" in {
            assert(tui.commands().isInstanceOf[String])
            assert(tui.update.isInstanceOf[Unit])
        }

        "accept input" in {
            tui.nextRound(true)
            tui.inputLine("undo".split(" ").toVector) should be(false)
            tui.inputLine("redo".split(" ").toVector) should be(false)
            tui.inputLine("i 0 0".split(" ").toVector) should be(false)
            tui.inputLine("m 3 0".split(" ").toVector) should be(false)
            tui.inputLine("m 9 0".split(" ").toVector) should be(false)
            tui.inputLine("a 9 0".split(" ").toVector) should be(false)
            tui.inputLine("undo".split(" ").toVector) should be(false)
            tui.inputLine("redo".split(" ").toVector) should be(false)
            tui.inputLine("save".split(" ").toVector) should be(false)
            tui.inputLine("CHEAT handofjustice".split(" ").toVector) should be(true)
        }

        "credit information" in {
            tui.credits should include ("Code written")
        }

        "finish the game" in {
            val tmplist = List(Cell("HA.","0",1,1,false,1,Player("Castle")))
            controller.board = controller.board.copy(controller.board.field,
                controller.board.player,
                controller.board.currentplayer,
                controller.board.currentcreature,
                tmplist,
                controller.board.log)
            tui.nextRound(true)
        }

        "have gamestate change updates" in {
            controller.gamestate = "gamerun"
            tui.update
            controller.gamestate = "credit"
            tui.update
        }

        "has to manage undo and redo" in {
            val input = Vector("a","1","1")
            val command: Command = new SetCommand(input,controller)
        }
    }}

}
