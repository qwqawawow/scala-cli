package scala.cli.commands

import caseapp._

import scala.build.blooprifle.BloopRifleConfig
import scala.build.options.{BuildOptions, InternalOptions}
import scala.cli.commands.util.CommonOps._
import scala.cli.commands.util.JvmUtils
import scala.cli.commands.util.SharedCompilationServerOptionsUtil._

// format: off
final case class BloopStartOptions(
  @Recurse
    logging: LoggingOptions = LoggingOptions(),
  @Recurse
    compilationServer: SharedCompilationServerOptions = SharedCompilationServerOptions(),
  @Recurse
    directories: SharedDirectoriesOptions = SharedDirectoriesOptions(),
  @Recurse
    jvm: SharedJvmOptions = SharedJvmOptions(),
  @Recurse
    coursier: CoursierOptions = CoursierOptions(),
  @Name("f")
    force: Boolean = false
) {
  // format: on

  def buildOptions: BuildOptions =
    BuildOptions(
      javaOptions = JvmUtils.javaOptions(jvm),
      internal = InternalOptions(
        cache = Some(coursier.coursierCache(logging.logger.coursierLogger("")))
      )
    )

  def bloopRifleConfig(): BloopRifleConfig =
    compilationServer.bloopRifleConfig(
      logging.logger,
      coursier.coursierCache(logging.logger.coursierLogger("Downloading Bloop")),
      logging.verbosity,
      buildOptions.javaHome().value.javaCommand,
      directories.directories
    )

}

object BloopStartOptions {

  implicit lazy val parser: Parser[BloopStartOptions] = Parser.derive
  implicit lazy val help: Help[BloopStartOptions]     = Help.derive
}
