package de.htwg.se.util

import scala.util.{Failure, Success, Try}
/**
 * @author Ronny Klotz & Alina Göttig
 * @since 03.Dez.2020
 */

trait Expression {
    def interpret(): Boolean
}

//NONTERMINAL EXPRESSIONS
class Interpreter(input: Vector[String]) extends Expression {
    override def interpret(): Boolean = {
        if(input.length == 3) {
            new LetterAMI(input(0)).interpret && new XVector(input(1)).interpret && new YVector(input(2)).interpret
        } else if(input.length == 2) {
            new Cheat(input(0)).interpret && new CheatCode(input(1)).interpret
        } else if(input.length == 1) {
            new OneWord(input(0)).interpret
        } else {
            false
        }
    }
}

//TERMINAL EXPRESSIONS
class LetterAMI(input: String) extends Expression {
    override def interpret: Boolean = {
        if(input.equals("a") || input.equals("m") || input.equals("i")) {
            true
        } else {
            false
        }
    }
}

class OneWord(input: String) extends Expression {
    override def interpret: Boolean = {
        if(input.equals("p") || input.equals("exit") || input.equals("undo") ||
            input.equals("redo") || input.equals("save")) {
            true
        } else {
            false
        }
    }
}

class Cheat(input: String) extends Expression {
    override def interpret: Boolean = {
        if(input.equals("CHEAT")) {
            true
        } else {
            false
        }
    }
}

class CheatCode(input: String) extends Expression {
    override def interpret: Boolean = {
        if(input.equals("coconuts") || input.equals("godunit") ||
            input.equals("feedcreature") || input.equals("handofjustice")) {
            true
        } else {
            false
        }
    }
}

class XVector(input: String) extends Expression {
    override def interpret: Boolean = {
        Try(input.toInt) match {
            case Success(in) =>
                if(0 <= in && in <= 14) {
                    true
                } else {
                    false
                }
            case Failure(_) => false
        }
    }
}

class YVector(input: String) extends Expression {
    override def interpret: Boolean = {
        Try(input.toInt) match {
            case Success(in) =>
                if(0 <= in && in <= 10) {
                    true
                } else {
                    false
                }
            case Failure(_) => false
        }
    }
}
