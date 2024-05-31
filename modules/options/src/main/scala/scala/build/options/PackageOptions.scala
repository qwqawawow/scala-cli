package scala.build.options

import scala.build.internal.Constants
import scala.build.options.packaging._

final case class PackageOptions(
  standalone: Option[Boolean] = None,
  version: Option[String] = None,
  launcherApp: Option[String] = None,
  maintainer: Option[String] = None,
  description: Option[String] = None,
  output: Option[String] = None,
  packageTypeOpt: Option[PackageType] = None,
  logoPath: Option[os.Path] = None,
  macOSidentifier: Option[String] = None,
  debianOptions: DebianOptions = DebianOptions(),
  windowsOptions: WindowsOptions = WindowsOptions(),
  redHatOptions: RedHatOptions = RedHatOptions(),
  nativeImageOptions: NativeImageOptions = NativeImageOptions(),
  useDefaultScaladocOptions: Option[Boolean] = None,
  provided: Seq[dependency.AnyModule] = Nil
) {

  def packageVersion: String =
    version
      .map(_.trim)
      .filter(_.nonEmpty)
      .getOrElse(Constants.version.stripSuffix("-SNAPSHOT"))


  // default behaviour for building docker image is building standalone JARs
  def isStandalone: Boolean = true

}

object PackageOptions {
  implicit val monoid: ConfigMonoid[PackageOptions] = ConfigMonoid.derive
}
