package models

import java.util.UUID

import slick.jdbc.SQLiteProfile.api._
import slick.lifted.ProvenShape
import javax.inject.Inject
import org.checkerframework.common.value.qual.IntRangeFromGTENegativeOne
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import play.api.mvc.{AbstractController, ControllerComponents}
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

case class Movie(id: Option[String] = Option(UUID.randomUUID().toString),
                 title: String,
                 year: Int,
                 cover: String,
                 description: String,
                 duration: Int,
                 contentRating: String,
                 source: String,
                 tags: Option[String])

class MovieTable(tag: Tag) extends Table[Movie](tag, "movie") {
  def id: Rep[String] = column[String]("id", O.PrimaryKey)
  def title: Rep[String] = column[String]("title")
  def year: Rep[Int] = column[Int]("year")
  def cover: Rep[String] = column[String]("cover")
  def description: Rep[String] = column[String]("description")
  def duration: Rep[Int] = column[Int]("duration")
  def contentRating: Rep[String] = column[String]("contenct_rating")
  def source: Rep[String] = column[String]("source")
  def tags: Rep[Option[String]] =
    column[Option[String]]("tags", O.Length(200, varying = true))

  def * : ProvenShape[Movie] =
    (
      id.?,
      title,
      year,
      cover,
      description,
      duration,
      contentRating,
      source,
      tags
    ) <> (Movie.tupled, Movie.unapply)
}

class MovieRepository @Inject()(
  protected val dbConfigProvider: DatabaseConfigProvider,
  cc: ControllerComponents
)(implicit ec: ExecutionContext)
    extends AbstractController(cc)
    with HasDatabaseConfigProvider[JdbcProfile] {
  private lazy val movieQuery = TableQuery[MovieTable]

  def dbInit: Future[Unit] = {
    val createSchema = movieQuery.schema.createIfNotExists
    db.run(createSchema)
  }
  def getAll = {
    val q = movieQuery.sortBy(_.id)
    db.run(q.result)
  }
  def getOne(id: String) = {
    val q = movieQuery.filter(_.id === id)
    db.run(q.result.headOption)
  }
  def create(movie: Movie) = {
    val insert = movieQuery += movie
    db.run(insert)
      .flatMap(_ => getOne(movie.id.getOrElse("")))
  }
  def update(id: String, movie: Movie) = {
    /*getOne(id)
      .map( maybeElement =>
        maybeElement match{
        case Some(_) =>
          val q = movieQuery.filter(_.id === movie.id && movie.id.contains(id))
          val update = q.update(movie)
          db.run(update)
            .flatMap(_ => db.run(q.result.headOption))
        case None => None
        }
      )*/
    val q = movieQuery.filter(_.id === movie.id && movie.id.contains(id))
    val update = q.update(movie)
    db.run(update)
      .flatMap(_ => db.run(q.result.headOption))
  }
  def delete(id: String) = {
    val q = movieQuery.filter(_.id === id)

    for {
      objeto <- db.run(q.result.headOption)
      _ <- db.run(q.delete)
    } yield objeto

  }
}
