package S2D.types

enum WindowFlag(val value: Int):
  case Resizable extends WindowFlag(0x00000001)
  case Undecorated extends WindowFlag(0x00000002)
  case Transparent extends WindowFlag(0x00000004)
  case AlwaysOnTop extends WindowFlag(0x00000008)
  case Maximized extends WindowFlag(0x00000010)
  case Minimized extends WindowFlag(0x00000020)
  case Focused extends WindowFlag(0x00000040)
  case Visible extends WindowFlag(0x00000080)
  case Fullscreen extends WindowFlag(0x00000100)
end WindowFlag

object WindowFlag:
  def combine(flags: WindowFlag*): Int =
    flags.map(_.value).foldLeft(0)(_ | _)
  def contains(combinedFlags: Int, flag: WindowFlag): Boolean =
    (combinedFlags & flag.value) != 0
  def extract(combinedFlags: Int): Set[WindowFlag] =
    WindowFlag.values.filter(flag => contains(combinedFlags, flag)).toSet
end WindowFlag
