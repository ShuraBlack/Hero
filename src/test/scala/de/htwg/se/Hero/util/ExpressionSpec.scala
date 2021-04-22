package de.htwg.se.Hero.util

import de.htwg.se.util.Interpreter
import org.scalatest._

class ExpressionSpec extends WordSpec with Matchers {

    "An Expression" when { "checking" should {

        "have instances" in {
            val input = "a 1 1".split(" ").toVector
            assert(new Interpreter(input).isInstanceOf[Interpreter])

            val input2 = "a m m".split(" ").toVector
            assert(new Interpreter(input2).isInstanceOf[Interpreter])
        }

        "accept Terminal Expression" in {
            val input = "a 1 1".split(" ").toVector
            new Interpreter(input).interpret() should be(true)

            val input2 = "m 1 1".split(" ").toVector
            new Interpreter(input2).interpret() should be(true)

            val input3 = "i 1 1".split(" ").toVector
            new Interpreter(input3).interpret() should be(true)

            val input4 = "CHEAT coconuts".split(" ").toVector
            new Interpreter(input4).interpret() should be(true)

            val input5 = "CHEAT feedcreature".split(" ").toVector
            new Interpreter(input5).interpret() should be(true)

            val input6 = "CHEAT godunit".split(" ").toVector
            new Interpreter(input6).interpret() should be(true)

            val input7 = "CHEAT handofjustice".split(" ").toVector
            new Interpreter(input7).interpret() should be(true)

            val input8 = "p".split(" ").toVector
            new Interpreter(input5).interpret() should be(true)

            val input9 = "exit".split(" ").toVector
            new Interpreter(input9).interpret() should be(true)
        }

        "not accept Terminal Expression" in {
            val input = "m m m".split(" ").toVector
            new Interpreter(input).interpret() should be(false)

            val input2 = "a".split(" ").toVector
            new Interpreter(input2).interpret() should be(false)

            val input3 = "CHEAT".split(" ").toVector
            new Interpreter(input3).interpret() should be(false)

            val input4 = "p 1 1".split(" ").toVector
            new Interpreter(input4).interpret() should be(false)

            val input5 = "p 1 1 1".split(" ").toVector
            new Interpreter(input5).interpret() should be(false)

            val input6 = "KEINCHEAT".split(" ").toVector
            new Interpreter(input6).interpret() should be(false)

            val input7 = "a 20 0".split(" ").toVector
            new Interpreter(input7).interpret() should be(false)

            val input8 = "a 0 20".split(" ").toVector
            new Interpreter(input8).interpret() should be(false)

            val input9 = "Cheat coconut".split(" ").toVector
            new Interpreter(input9).interpret() should be(false)

            val input10 = "a 0 a".split(" ").toVector
            new Interpreter(input10).interpret() should be(false)

            val input11 = "CHEAT coconut".split(" ").toVector
            new Interpreter(input11).interpret() should be(false)
        }
    }}
}
