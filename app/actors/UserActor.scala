package actors

import akka.actor.Actor
import akka.actor.ActorLogging
import akka.event.LoggingReceive
import play.api.libs.json.JsValue
import play.api.libs.json.Json
import akka.actor.ActorRef
import akka.actor.Props
import scala.xml.Utility


class UserActor(uid: String, board: ActorRef, out: ActorRef) extends Actor with ActorLogging {

  override def preStart() = {
    GameActor() ! Subscribe
  }

  def receive = LoggingReceive {
    case Message(muid, s) if sender == board => {
      val js = Json.obj("type" -> "message", "uid" -> muid, "msg" -> s)
      out ! js
    }
    case Fen(muid, oldObj, newObj) if sender == board => {
      val js = Json.obj("type" -> "game", "uid" -> muid, "oldObj" -> oldObj, "newObj" -> newObj)
      out ! js
    }
    case js: JsValue => {
      var oldPos = ""
      var newPos = ""
      (js \ "msg").validate[String] map { Utility.escape(_) }  map { board ! Message(uid, _ ) }
      (js \ "oldPos").validate[String] map { oldPos = _ }
      (js \ "newPos").validate[String] map { board ! Fen(uid, oldPos, _ ) }
    }
    case other => println("E AE RECEIVE3?")
  }
}

case class ParseTest(move: List[String])

object UserActor {
  def props(uid: String)(out: ActorRef) = Props(new UserActor(uid, GameActor(), out))
}