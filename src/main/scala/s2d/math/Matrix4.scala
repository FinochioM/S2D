package s2d.math

import s2d.types.{Vector2, Camera2D}

object Matrix4:
  def ortho(left: Float, right: Float, bottom: Float, top: Float, near: Float, far: Float): Array[Float] =
    val matrix = Array.fill(16)(0.0f)

    matrix(0) = 2.0f / (right - left)
    matrix(5) = 2.0f / (top - bottom)
    matrix(10) = -2.0f / (far - near)
    matrix(12) = -(right + left) / (right - left)
    matrix(13) = -(top + bottom) / (top - bottom)
    matrix(14) = -(far + near) / (far - near)
    matrix(15) = 1.0f

    matrix

  def translate(matrix: Array[Float], tx: Float, ty: Float, tz: Float): Array[Float] =
    val result = matrix.clone()
    result(12) += result(0) * tx + result(4) * ty + result(8) * tz
    result(13) += result(1) * tx + result(5) * ty + result(9) * tz
    result(14) += result(2) * tx + result(6) * ty + result(10) * tz
    result(15) += result(3) * tx + result(7) * ty + result(11) * tz
    result

  def scale(matrix: Array[Float], sx: Float, sy: Float, sz: Float): Array[Float] =
    val result = matrix.clone()
    for i <- 0 until 4 do
      result(i) *= sx
      result(i + 4) *= sy
      result(i + 8) *= sz
    result

  def rotateZ(matrix: Array[Float], angle: Float): Array[Float] =
    val angleRad = math.toRadians(angle).toFloat
    val cos = math.cos(angleRad).toFloat
    val sin = math.sin(angleRad).toFloat

    val result = matrix.clone()
    val m00 = result(0)
    val m01 = result(1)
    val m02 = result(2)
    val m03 = result(3)
    val m10 = result(4)
    val m11 = result(5)
    val m12 = result(6)
    val m13 = result(7)

    result(0) = m00 * cos + m10 * sin
    result(1) = m01 * cos + m11 * sin
    result(2) = m02 * cos + m12 * sin
    result(3) = m03 * cos + m13 * sin
    result(4) = m00 * (-sin) + m10 * cos
    result(5) = m01 * (-sin) + m11 * cos
    result(6) = m02 * (-sin) + m12 * cos
    result(7) = m03 * (-sin) + m13 * cos

    result

  def getCameraMatrix(camera: Camera2D, screenWidth: Int, screenHeight: Int): Array[Float] =
    var matrix = ortho(0.0f, screenWidth.toFloat, screenHeight.toFloat, 0.0f, -1.0f, 1.0f)

    matrix = translate(matrix, camera.offset.x, camera.offset.y, 0.0f)
    matrix = rotateZ(matrix, camera.rotation)
    matrix = scale(matrix, camera.zoom, camera.zoom, 1.0f)
    matrix = translate(matrix, -camera.target.x, -camera.target.y, 0.0f)

    matrix

end Matrix4