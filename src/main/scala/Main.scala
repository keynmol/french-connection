import com.raquo.laminar.api.L._
import org.scalajs.dom
import cats.effect._
import scala.concurrent.Future
import com.raquo.airstream.signal.Signal

object Main {
  def main(args: Array[String]): Unit = {

    val customAction = IO {
      val currentMilliTime = System.currentTimeMillis()

      dom.document.getElementById("test").innerHTML =
        s"Current time is: $currentMilliTime"

    }

    def makeButton(action: IO[_]) = {
      val observer = Observer.apply[Any](_ => action.unsafeRunAsyncAndForget())
      div(button("Play", onClick --> observer))
    }

    render(
      dom.document.querySelector("#appContainer"),
      div(
        div(idAttr := "test"),
        makeButton(customAction)
      )
    )
  }
}
