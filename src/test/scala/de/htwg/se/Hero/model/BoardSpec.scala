package de.htwg.se.model

import de.htwg.se.model.boardComponent.boardImpl
import de.htwg.se.model.boardComponent.boardImpl.{Board, Cell}
import de.htwg.se.model.playerComponent.Player
import org.scalatest._

class BoardSpec extends WordSpec with Matchers {
    "A Board" when { "new" should {
        val player = Vector(Player("Player1"),Player("Player2"))
        val emptycell = boardImpl.Cell("   ", "0", 0, 0, false, 0, Player("none"))
        val creature1 = boardImpl.Cell("HA.", "2-3", 10, 3, style = true, 28, player(0))
        val creature2 = boardImpl.Cell(".FA", "1-2", 4, 5, style = false, 44, player(1))
        val field = Vector(Vector(creature1,emptycell),Vector(emptycell,creature2))
        val list = List(creature1,creature2)
        val log = List("Castle cheated!")
        val board = Board(field,player,player(0),creature1,list,log)

        "have informations"  in {
            board.field shouldNot be(Vector.empty)
            board.field(1)(1) should be(creature2)
            board.player shouldNot be(Vector.empty)
            board.player(1).name should be(player(1).name)
            board.currentplayer should be(player(0))
            board.currentcreature should be(creature1)
            board.list.length should be(2)
            board.log.length should be(1)
            board.log shouldNot be(List.empty)
        }

        "has a current creature information" in {
            board.currentcreatureinfo() should include("Ha_lberdier")
            board.currentcreatureinfo() should include("2-3")
            board.currentcreatureinfo() should include("10")
            board.currentcreatureinfo() should include("Ranged")
            val boardchange = board.copy(currentcreature = creature2)
            boardchange.currentcreatureinfo() should include("Melee")
        }

        "has a X Y coordinate controlled creature information" in {
            board.creatureinfo(1,1) should include("Fa_miliar")
            board.creatureinfo(1,1) should include("4")
            board.creatureinfo(1,1) should include("44")
        }

        "has a current player information" in {
            board.currentplayerinfo() should include("Current Player:")
            board.currentplayerinfo() should include(player(0).name)
        }

        "has an last log output" in {
            board.lastlog() should include("Castle cheated!")
            val boardnolog = board.copy(log = List.empty)
            boardnolog.lastlog() shouldNot include("Castle cheated!")
            boardnolog.lastlog() shouldNot include("Inferno cheated!")
            boardnolog.lastlog() shouldNot include("dealt")
        }

        "has a help function" in {
            board.lines().length should be(7*15+1)

            board.lines() should include regex """=+"""
        }
        "has a copy by value" in {
            val copy = board.copy(board.field,board.player,board.currentplayer,board.currentcreature,board.list,board.log)
            copy.currentplayer.name should be (board.currentplayer.name)
            copy.currentcreature.name should be (board.currentcreature.name)
        }
        "and has an illustration of the full name" in {
            board.realname(creature1.name) should be("Ha_lberdier")
            board.realname("MA.") should be("Ma_rksman")
            board.realname("MAG") should be("Mag_og")
            board.realname("RO.") should be("Ro_yal Griffin")
            board.realname(".CE") should be("Ce_rberus")
            board.realname("AN.") should be("Arch An_gle")
            board.realname(".DE") should be("Arch De_vil")
            board.realname("CH.") should be("Ch_ampion")
            board.realname(".EF") should be("Ef_reet Sultan")
            board.realname("ZE.") should be("Ze_alot")
            board.realname(".PI") should be("Pi_t Lord")
            board.realname("CR.") should be("Cr_usader")
            board.realname(".HO") should be("Ho_rned Demon")
            board.realname("   ") should be("   ")
            board.realname(" _ ") should be("   ")
            board.realname("XXX") should be("   ")
        }

    }}

    "A instance of a Board" when { "new" should {
        val board = Board
        "have the typ"  in {
            board should be(Board)
        }
    }}
}
