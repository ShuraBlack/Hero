package de.htwg.se

import com.google.inject.AbstractModule
import de.htwg.se.controller.controllerComponent.{ControllerImpl, ControllerInterface}
import de.htwg.se.model.fileioComponent.FileIOInterface
import net.codingwell.scalaguice.ScalaModule

/**
 * @author Ronny Klotz & Alina Göttig
 * @since 13.Jan.2021
 */

// ================== Implementation for concrete storage ==================
import de.htwg.se.model.fileioComponent.fileiojsonimpl.Json
import de.htwg.se.model.fileioComponent.fileioxmlimpl.Xml
// =========================================================================

class HeroModule extends  AbstractModule with ScalaModule{

    override def configure(): Unit = {

        bind[ControllerInterface].to[ControllerImpl.Controller]

        bind[FileIOInterface].to[Json] // [Json] or [Xml]

    }

}
