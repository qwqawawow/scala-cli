package scala.cli.commands

import caseapp._

import java.io.File

import scala.build.{Build, Inputs, Os}

object Compile extends ScalaCommand[CompileOptions] {
  override def group = "Main"
  override def sharedOptions(options: CompileOptions) = Some(options.shared)
  def run(options: CompileOptions, args: RemainingArgs): Unit = {

    val inputs = options.shared.inputsOrExit(args)

    def postBuild(build: Build): Unit =
      if (options.classPath)
        for (s <- build.successfulOpt) {
          val cp = s.fullClassPath.map(_.toAbsolutePath.toString).mkString(File.pathSeparator)
          println(cp)
        }

    val buildOptions = options.buildOptions
    val bloopgunConfig = options.shared.bloopgunConfig()

    if (options.shared.watch) {
      val watcher = Build.watch(inputs, buildOptions, bloopgunConfig, options.shared.logger, Os.pwd, postAction = () => WatchUtil.printWatchMessage()) { build =>
        postBuild(build)
      }
      try WatchUtil.waitForCtrlC()
      finally watcher.dispose()
    } else {
      val build = Build.build(inputs, buildOptions, bloopgunConfig, options.shared.logger, Os.pwd)
      postBuild(build)
    }
  }

}