package s2d.types

case class Matrix(values: Array[Float]):
  require(values.length == 16, "Matrix must have exactly 16 values")

  def apply(row: Int, col: Int): Float = values(row * 4 + col)

  def *(other: Matrix): Matrix =
    val result = Array.ofDim[Float](16)
    for
      row <- 0 until 4
      col <- 0 until 4
    do
      var sum = 0.0f
      for k <- 0 until 4 do
        sum += this(row, k) * other(k, col)
      result(row * 4 + col) = sum
    Matrix(result)

  def transpose: Matrix =
    val result = Array.ofDim[Float](16)
    for
      row <- 0 until 4
      col <- 0 until 4
    do
      result(col * 4 + row) = values(row * 4 + col)
    Matrix(result)

object Matrix:
  val Identity: Matrix = Matrix(Array(
    1.0f, 0.0f, 0.0f, 0.0f,
    0.0f, 1.0f, 0.0f, 0.0f,
    0.0f, 0.0f, 1.0f, 0.0f,
    0.0f, 0.0f, 0.0f, 1.0f
  ))

  def translation(x: Float, y: Float, z: Float): Matrix = Matrix(Array(
    1.0f, 0.0f, 0.0f, x,
    0.0f, 1.0f, 0.0f, y,
    0.0f, 0.0f, 1.0f, z,
    0.0f, 0.0f, 0.0f, 1.0f
  ))

  def scale(x: Float, y: Float, z: Float): Matrix = Matrix(Array(
    x,    0.0f, 0.0f, 0.0f,
    0.0f, y,    0.0f, 0.0f,
    0.0f, 0.0f, z,    0.0f,
    0.0f, 0.0f, 0.0f, 1.0f
  ))
end Matrix