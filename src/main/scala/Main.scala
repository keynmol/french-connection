import com.raquo.laminar.api.L._
import org.scalajs.dom
import cats.effect._
import scala.concurrent.Future
import com.raquo.airstream.signal.Signal

import typings.webmidi
import typings.std.global.AudioContext
import typings.std.global.navigator
import scala.concurrent.ExecutionContext
import org.scalajs.dom.raw.OscillatorNode

object Main {
  implicit val cs = IO.contextShift(ExecutionContext.global)
  def main(args: Array[String]): Unit = {

    val customAction = IO {
      val currentMilliTime = System.currentTimeMillis()

      dom.document.getElementById("test").innerHTML =
        s"Current time is: $currentMilliTime"

    }

    object ManagedOscillator {
      private val acquire = IO {

        val context = new AudioContext

        val oscillator = context.createOscillator()

        oscillator.frequency.setValueAtTime(110.0, 0)
        val envelope = context.createGain()
        oscillator.connect(envelope)
        envelope.connect(context.destination)
        envelope.gain.value = 0.0
        oscillator.start(0)

        (oscillator, context)
      }

      private val dispose =
        (oscillator: OscillatorNode, context: AudioContext) =>
          IO(oscillator.stop()) *>
            IO.fromFuture(IO(context.close().toFuture))

      val create = Resource.make(acquire)(dispose.tupled)
    }

    val printWebMidiOutputs = IO(println(webmidi.mod.default.outputs.toList))

    val playSomething = IO {
      val midiOutput = webmidi.mod.default.outputs.toList.headOption

      midiOutput.map { o =>
        println(o.name)
        o.playNote("C3")
      }
    }

    def makeButton(action: IO[_]) = {
      val observer = Observer.apply[Any](_ => action.unsafeRunAsyncAndForget())
      div(button("Play", onClick --> observer))
    }

    webmidi.mod.default.enable { err =>
      render(
        dom.document.querySelector("#appContainer"),
        div(
          div(idAttr := "test"),
          makeButton(ManagedOscillator.create.use(_ => playSomething.void))
        )
      )
    }
  }
}
