lazy val root = (project in file("."))
  .settings(
    organization := "com.dgg",
    name := "receipt-processor-challenge",
    version := "0.0.1-SNAPSHOT",
    scalaVersion := "2.13.11",
    libraryDependencies ++= Seq(
      Dependencies.circe,
      Dependencies.doobieCore,
      Dependencies.doobieHikari,
      Dependencies.doobieJson,
      Dependencies.doobiePostgres,
      Dependencies.http4sCirce,
      Dependencies.http4sClient,
      Dependencies.http4sDsl,
      Dependencies.http4sServer,
      Dependencies.log4cats,
      Dependencies.logback,
      Dependencies.postgresql,
      Dependencies.pureconfig,
      Dependencies.Test.munit,
      Dependencies.Test.munitCE,
      Dependencies.Test.scalacheck
    ).map(_.exclude("org.slf4j", "slf4j-log4j12")),
    addCompilerPlugin("org.typelevel" %% "kind-projector"     % "0.13.2" cross CrossVersion.full),
    addCompilerPlugin("com.olegpy"    %% "better-monadic-for" % "0.3.1"),
    semanticdbEnabled := true,
    semanticdbVersion := scalafixSemanticdb.revision,
    Compile / scalacOptions := Seq(
      "-feature",
      "-unchecked",
      "-deprecation",
      "-Ymacro-annotations",
      "-language:postfixOps"
    ),
    Compile / console / scalacOptions ~= (_.filterNot(_ == "-Xfatal-warnings")),
    Test / console / scalacOptions ~= (_.filterNot(_ == "-Xfatal-warnings")),
    Compile / guardrailTasks := GuardrailHelpers.createGuardrailTasks((Compile / sourceDirectory).value / "openapi")(
      openApiFile =>
        List(
          ScalaClient(openApiFile.file, pkg = openApiFile.pkg + ".client", framework = "http4s"),
          ScalaServer(openApiFile.file, pkg = openApiFile.pkg + ".server", framework = "http4s")
        )
    )
  )
