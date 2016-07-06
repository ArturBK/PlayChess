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
    println("asdnajsfasf")
    GameActor() ! Subscribe
  }

  def receive = LoggingReceive {
    case Message(muid, s) if sender == board => {
          println("E AE RECEIVE1?")

      val js = Json.obj("type" -> "message", "uid" -> muid, "msg" -> s)
      out ! js
    }
    case Fen(muid, fen) if sender == board => {
      println("E AE RECEIVE2?")

      val js = Json.obj("type" -> "game", "uid" -> muid, "fen" -> fen)
      out ! js
    }
    case js: JsValue => {
      println("E AE RECEIVE8?")
      println(js)
      (js \ "msg").validate[String] map { Utility.escape(_) }  map { board ! Message(uid, _ ) }
      (js \ "fen").validate[String] map { Utility.escape(_) }  map { board ! Fen(uid, _ ) } 

    } 
    case js: JsValue => {
      println("E AE RECEIVE7?")
      (js \ "fen").validate[String] map { Utility.escape(_) }  map { board ! Fen(uid, _) } 
    }

    case other => println("E AE RECEIVE3?")

  }
}

object UserActor {
  def props(uid: String)(out: ActorRef) = Props(new UserActor(uid, GameActor(), out))
}
