package de.htwg.se.Hero.model

/**
 * Scala project for the game Hero (based on Heroes of Might and Magic III - Fight)
 * @author Ronny Klotz & Alina Göttig
 * @since 9.Nov.2020
 */

import com.google.inject.{Guice, Injector}
import de.htwg.se.HeroModule
import de.htwg.se.aview.{GUI, TUI}
import de.htwg.se.controller.controllerComponent.ControllerInterface
import de.htwg.se.util.{Interpreter, UndoManager}

import scala.io.StdIn

//noinspection ScalaStyle
object Hero {

    val injector: Injector = Guice.createInjector(new HeroModule)
    val controller: ControllerInterface = injector.getInstance(classOf[ControllerInterface])
    val manager = new UndoManager

    val UIType: Boolean = if (System.getenv("UI_TYPE").equals("full")) true else false

    val tui = new TUI(controller, manager)

    def main(args: Array[String]): Unit = {

        if (UIType) {
            val gui = new GUI(controller)
            gui.update
            gui.visible = true
        }

        println(tui.startinfo() + tui.mainmenu)
        while (true) {

            controller.gamestate match {

                case "mainmenu" => {
                    while (controller.gamestate.equals("mainmenu")) {
                        val input = StdIn.readLine()

                        controller.gamestate match {

                            case "mainmenu" => {
                                if (input.equals("n")) {
                                    controller.gamestate = "gamerun"
                                    newgame("")
                                } else if (input.equals("l")) {
                                    if (controller.load()) {
                                        controller.gamestate = "gamerun"
                                        controller.prediction()
                                        controller.notifyObservers
                                    }
                                }
                                else if (input.equals("c")) {
                                    controller.gamestate = "credit"
                                    controller.notifyObservers
                                }
                                else if (input.equals("exit")) return
                                else println("Invalid Input. ")
                            }

                            case "gamerun" => {
                                println(controller.printSidesStart())
                                controller.notifyObservers
                                newgame(input)
                            }

                            case "credit" => controller.gamestate = "mainmenu"
                            case "finished" => controller.gamestate = "mainmenu"
                        }
                    }
                }

                case "gamerun" => newgame("")

                case "gamerun" => {
                    while (controller.gamestate.equals("gamerun")) {
                        val input = StdIn.readLine().split(" ").toVector
                        if (new Interpreter(input).interpret()) {
                            if (input(0).equals("exit") || !tui.inputLine(input)) return
                        } else { print("Invalid Input. ") }
                        if (!controller.gamestate.equals("gamerun")) print("New Input: ")
                    }
                }
                case "credit" => {
                    controller.gamestate = "mainmenu"
                    controller.notifyObservers
                }
                case "finished" => controller.gamestate = "mainmenu"

            }
        }
    }

    def newgame(s:String): Unit = {
        controller.inizGame()

        if (s.equals("")){
            println(controller.printSidesStart())
            controller.notifyObservers
        } else {

            if (new Interpreter(s.split(" ").toVector).interpret()) {
                if (s.split(" ").toVector(0).equals("exit") || !tui.inputLine(s.split(" ").toVector)) return
            } else { print("Invalid Input. ") }

            print("New input: ")
        }

        while (controller.gamestate.equals("gamerun")) {
            val input = StdIn.readLine().split(" ").toVector

            if (new Interpreter(input).interpret()) {
                if (input(0).equals("exit") || !tui.inputLine(input)) {return}
            } else { print("Invalid Input. ") }

            if (!controller.gamestate.equals("gamerun")) print("New Input: ")
        }

    }

}