lazy val root = (project in file("."))
  .settings(
    organization := "com.dgg",
    name := "receipt-processor-challenge",
    version := "0.0.1-SNAPSHOT",
    scalaVersion := "2.13.11",
    libraryDependencies ++= Seq(
      Dependencies.circe,
      Dependencies.http4sCirce,
      Dependencies.http4sClient,
      Dependencies.http4sDsl,
      Dependencies.http4sServer,
      Dependencies.log4cats,
      Dependencies.logback,
      Dependencies.Test.munit,
      Dependencies.Test.munitCE
    ).map(_.exclude("org.slf4j", "slf4j-log4j12")),
    addCompilerPlugin("org.typelevel" %% "kind-projector"     % "0.13.2" cross CrossVersion.full),
    addCompilerPlugin("com.olegpy"    %% "better-monadic-for" % "0.3.1"),
    assembly / assemblyMergeStrategy := {
      case "module-info.class" => MergeStrategy.discard
      case x                   => (assembly / assemblyMergeStrategy).value.apply(x)
    },
    semanticdbEnabled := true,
    semanticdbVersion := scalafixSemanticdb.revision,
    Compile / scalacOptions ++= Seq(
      "-feature",
      "-unchecked",
      "-deprecation",
      "-Ymacro-annotations",
      "-Xlint:unused",
      "-language:postfixOps",
      "-Wunused:imports"
    )
  )
