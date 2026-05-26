package s2d.types

case class HSV(h: Float, s: Float, v: Float):
    require(h >= 0.0f && h <= 360.0f, s"Hue must be 0-360, got $h")
    require(s >= 0.0f && s <= 1.0f, s"Saturation must be 0-1, got $s")
    require(v >= 0.0f && v <= 360.0f, s"Value must be 0-1, got $v")

end HSV