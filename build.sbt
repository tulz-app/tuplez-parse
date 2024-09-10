import sbt.librarymanagement.CrossVersion

inThisBuild(
  List(
    organization := "app.tulz",
    homepage := Some(url("https://github.com/tulz-app/tuplez-parse")),
    licenses := List("MIT" -> url("https://github.com/tulz-app/tuplez-parse/blob/main/LICENSE.md")),
    developers := List(Developer("yurique", "Iurii Malchenko", "i@yurique.com", url("https://github.com/yurique"))),
    scmInfo := Some(ScmInfo(url("https://github.com/tulz-app/tuplez-parse"), "scm:git@github.com/tulz-app/tuplez-parse.git")),
    (Test / publishArtifact) := false,
    scalaVersion := ScalaVersions.v213,
    crossScalaVersions := Seq(
      ScalaVersions.v3,
      ScalaVersions.v213,
      ScalaVersions.v212
    ),
    ThisBuild / versionScheme := Some("early-semver"),
    versionPolicyIntention := Compatibility.BinaryCompatible,
    githubWorkflowTargetTags ++= Seq("v*"),
    githubWorkflowPublishTargetBranches := Seq(RefPredicate.StartsWith(Ref.Tag("v"))),
//    githubWorkflowBuild ++= Seq(WorkflowStep.Sbt(List("versionPolicyCheck"))),
    githubWorkflowBuild := Seq(WorkflowStep.Sbt(List("test"))),
    githubWorkflowPublish := Seq(WorkflowStep.Sbt(List("ci-release"))),
    githubWorkflowEnv ~= (_ ++ Map(
      "PGP_PASSPHRASE"    -> s"$${{ secrets.PGP_PASSPHRASE }}",
      "PGP_SECRET"        -> s"$${{ secrets.PGP_SECRET }}",
      "SONATYPE_PASSWORD" -> s"$${{ secrets.SONATYPE_PASSWORD }}",
      "SONATYPE_USERNAME" -> s"$${{ secrets.SONATYPE_USERNAME }}"
    ))
  )
)

lazy val `cats-parse-version` = "1.0.0"
lazy val `tuplez-version`     = "0.4.0"

lazy val `tuplez-parse` =
  crossProject(JVMPlatform, JSPlatform)
    .crossType(CrossType.Pure)
    .in(file("modules/parse"))
    .jsConfigure(_.enablePlugins(ScalaJSJUnitPlugin))
    .settings(commonSettings)
    .jsSettings(commonJsSettings)
    .settings(
      name := "tuplez-parse",
      description := "Tuple composition utilities for cats-parse",
      libraryDependencies ++= Seq(
        "org.typelevel" %%% "cats-parse"        % `cats-parse-version`,
        "app.tulz"      %%% "tuplez-full-light" % `tuplez-version` % Provided
      )
    )

lazy val commonSettings = Seq(
  libraryDependencies ++= Seq(
    "junit"         % "junit"           % "4.13.2" % Test,
    ("com.novocode" % "junit-interface" % "0.11"   % Test).exclude("junit", "junit-dep")
  ),
  versionPolicyIntention :=
    (CrossVersion.partialVersion(scalaVersion.value) match {
      case Some((3, _)) => {
        // temp, doesn't work with Scala 3
        Compatibility.None
      }
      case _ => versionPolicyIntention.value
    })
)

lazy val commonJsSettings = Seq(
  scalacOptions ++= {
    val sourcesGithubUrl = s"https://raw.githubusercontent.com/tulz-app/tuplez/${git.gitHeadCommit.value.get}/"
    val sourcesOptionName = CrossVersion.partialVersion(scalaVersion.value) match {
      case Some((2, _)) => "-P:scalajs:mapSourceURI"
      case Some((3, _)) => "-scalajs-mapSourceURI"
      case _            => throw new RuntimeException(s"unexpected scalaVersion: ${scalaVersion.value}")
    }
    val moduleSourceRoot = file("").toURI.toString
    Seq(
      s"$sourcesOptionName:$moduleSourceRoot->$sourcesGithubUrl"
    )
  }
)

lazy val noPublish = Seq(
  publishLocal / skip := true,
  publish / skip := true,
  publishTo := Some(Resolver.file("Unused transient repository", file("target/unusedrepo")))
)

lazy val root = project
  .in(file("."))
  .settings(
    name := "tuplez",
    versionPolicyCheck := {},
  )
  .settings(noPublish)
  .aggregate(
    `tuplez-parse`.js,
    `tuplez-parse`.jvm
  )
