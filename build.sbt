name := "Sample"

version := "1.0-SNAPSHOT"

libraryDependencies ++= Seq(
  jdbc,
  anorm,
  cache,
  "org.scalikejdbc" %% "scalikejdbc"               % "[1.7,)",
  "org.scalikejdbc" %% "scalikejdbc-interpolation" % "[1.7,)",
  "org.scalikejdbc" %% "scalikejdbc-test"          % "[1.7,)"   % "test",
  "com.h2database"  %  "h2"                        % "[1.3,)",
  "org.scalikejdbc" %% "scalikejdbc-play-plugin"   % "[1.7,)",
  "ch.qos.logback"  %  "logback-classic"           % "[1.0,)",
  "org.mockito"     %  "mockito-all"               % "1.9.5",
  "org.mockito"     %  "mockito-core"              % "1.9.5"
)     

play.Project.playScalaSettings
