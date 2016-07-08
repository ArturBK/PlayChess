package actors

import akka.actor.Actor
import akka.actor.ActorLogging
import akka.event.LoggingReceive
import akka.actor.ActorRef
import akka.actor.Terminated
import play.libs.Akka
import akka.actor.Props

class GameActor extends Actor with ActorLogging {
  var users = Set[ActorRef]()

  def receive = LoggingReceive {
    case m:Message => {
      println("message game constroll")
      users map { _ ! m}
    }
    case f:Fen => {
      println("FEN game constroll")
      users map { _ ! f}
    }
    case Subscribe => {
      users += sender
      context watch sender
    }
    case Terminated(user) => users -= user
    case _ => println("faio ")

  }
}

object GameActor {
  lazy val game = Akka.system().actorOf(Props[GameActor])
  def apply() = game
}

case class Message(uuid: String, s: String)
case class Fen(uuid: String, oldPos: String, newPos: String)
object Subscribe