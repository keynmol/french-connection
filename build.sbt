enablePlugins(ScalaJSPlugin)

name := "french-connection"
scalaVersion := "2.13.1" // or any other Scala version >= 2.11.12

libraryDependencies += "com.raquo" %%% "laminar" % "0.9.2"
libraryDependencies += "org.typelevel" %%% "cats-effect" % "2.1.4"

// This is an application with a main method
scalaJSUseMainModuleInitializer := true
