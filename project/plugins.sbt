addSbtPlugin("org.scala-js" % "sbt-scalajs" % "1.1.1")
resolvers += Resolver.bintrayRepo("oyvindberg", "converter")

// for Scala.js 1.x.x
addSbtPlugin("org.scalablytyped.converter" % "sbt-converter" % "1.0.0-beta19")
