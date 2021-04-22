package de.htwg.se.Hero.util

import de.htwg.se.model.boardComponent.boardImpl
import de.htwg.se.model.playerComponent.Player
import de.htwg.se.util.{CreaturelistIterator, ObstacleListIterator}
import org.scalatest._

class Iterator extends WordSpec with Matchers {

    "An Iterator" when { "new" should {
        val creaturelist = new CreaturelistIterator
        val obstaclelist = new ObstacleListIterator
        "provide next cell information" in {
            val creature = creaturelist.next()
            val obstacle = obstaclelist.next()
            creature._2 should be(boardImpl.Cell("HA.", "2-3", 10, 3, style = false, 28, Player("Castle")))
            creature._1 should be(Vector(0, 0))
            obstacle._2 should be(boardImpl.Cell("XXX", "0", 0, 0, style = false, 0, Player("none")))
            obstacle._1 should be(Vector(4, 8))
        }
        "and tell if there is a next cell" in {
            creaturelist.hasNext should be(true)
            obstaclelist.hasNext should be(true)
        }
        "and change at the end" in {
            while (creaturelist.hasNext) { creaturelist.next() }
            while (obstaclelist.hasNext) { obstaclelist.next() }
            creaturelist.hasNext should be(false)
            obstaclelist.hasNext should be(false)
        }
    }
    }

}