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
          println("E AE RECEIVE1?")

      val js = Json.obj("type" -> "message", "uid" -> muid, "msg" -> s)
      out ! js
    }
    case Fen(muid, oldObj, newObj) if sender == board => {
      println("E AE RECEIVE2?")

      val js = Json.obj("type" -> "game", "uid" -> muid, "oldObj" -> oldObj, "newObj" -> newObj)
      out ! js
    }
    case js: JsValue => {
      println("E AE RECEIVE8?")
      println(js)

      var oldPos = ""
      var newPos = ""
      (js \ "msg").validate[String] map { Utility.escape(_) }  map { board ! Message(uid, _ ) }

      (js \ "oldPos").validate[String] map { oldPos = _ }
      (js \ "newPos").validate[String] map { board ! Fen(uid, oldPos, _ ) }


      //(js \ "oldPos").validate[String] map { Utility.escape(_) }  map { println(_) }
      //println(js)
    //   implicit val reads = Json.reads[String]
    // val name = (js).as[String]
    // println(name)

      // val envObject = js.as[Fen]
      // println(envObject)

    }

    case other => println("E AE RECEIVE3?")

  }
}

case class ParseTest(move: List[String])

object UserActor {
  def props(uid: String)(out: ActorRef) = Props(new UserActor(uid, GameActor(), out))
}