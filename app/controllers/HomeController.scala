package controllers

import javax.inject._
import play.api._
import play.api.mvc._
import models.MovieRepository
import scala.concurrent.ExecutionContext.Implicits.global

/**
  * This controller creates an `Action` to handle HTTP requests to the
  * application's home page.
  */
@Singleton
class HomeController @Inject()(cc: ControllerComponents,
                               movieRepository: MovieRepository)
    extends AbstractController(cc) {

  val logger = play.Logger.of("HomeController")

  /**
    * Create an Action to render an HTML page.
    *
    * The configuration in the `routes` file means that this method
    * will be called when the application receives a `GET` request with
    * a path of `/`.
    */
  def index() = Action.async { implicit request: Request[AnyContent] =>
    movieRepository.getAll
      .map(movies => {
        Ok(views.html.index(movies))
      })
      .recover {
        case ex =>
          logger.error("Fallo en getMovies", ex)
          InternalServerError(s"Hubo un error: ${ex.getLocalizedMessage}")
      }
  }

  def dbInit() = Action.async { request =>
    movieRepository.dbInit
      .map(_ => Created("Tabla Creada"))
      .recover { ex =>
        play.Logger.of("dbInit").debug("Error en dbinit", ex)
        InternalServerError(s"Hubo un error")
      }
  }
}
