name := "001 SBT PlatePlugin"

version := "0.1.0-SNAPSHOT"

scalaVersion := "2.12.2"

resolvers += Resolver.jcenterRepo
resolvers += "jitpack" at "https://jitpack.io"

libraryDependencies += "com.github.PlateStack" % "PlateAPI" % "243015725f" % "provided"

updateOptions := updateOptions.value.withLatestSnapshots(false)

//assemblyJarName in assembly := "FatJar.jar"
