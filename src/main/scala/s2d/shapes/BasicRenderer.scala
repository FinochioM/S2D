package s2d.shapes

import s2d.types.{Color, Vector2, Rectangle}
import s2d.math.Matrix4
import s2d.core.Window
import s2d.backend.gl.GL.*
import s2d.backend.gl.GLExtras.*
import s2d.backend.gl.GLEWHelper
import s2d.core.{Shaders, Drawing}
import scalanative.unsafe.*
import scalanative.unsigned.*
import scala.scalanative.libc.stdlib

object BasicRenderer:
  private val vertexShaderSource = """
    #version 330 core

    layout (location = 0) in vec2 aPos;

    uniform mat4 uProjection;
    uniform vec4 uColor;

    out vec4 vertexColor;

    void main() {
        gl_Position = uProjection * vec4(aPos, 0.0, 1.0);
        vertexColor = uColor;
    }
  """

  private val fragmentShaderSource = """
    #version 330 core

    in vec4 vertexColor;
    out vec4 FragColor;

    void main() {
        FragColor = vertexColor;
    }
  """

  private var defaultShader: Option[s2d.types.Shader] = None
  private var VAO: UInt = 0.toUInt
  private var VBO: UInt = 0.toUInt
  private var projectionLocation: Int = -1
  private var colorLocation: Int = -1
  private var isInitialized: Boolean = false
  private val scratchBuffer: Ptr[GLfloat] =
    stdlib.malloc(sizeof[GLfloat] * 2048.toULong).asInstanceOf[Ptr[GLfloat]]

  def initialize(): Boolean =
    if isInitialized then return true

    defaultShader = Shaders.loadFromMemory(vertexShaderSource, fragmentShaderSource)

    defaultShader match
      case Some(shader) =>
        Zone {
          val projName = toCString("uProjection")
          val colorName = toCString("uColor")

          projectionLocation = GLEWHelper.glGetUniformLocation(shader.id.toUInt, projName)
          colorLocation = GLEWHelper.glGetUniformLocation(shader.id.toUInt, colorName)
        }

        val vaoArray = stackalloc[GLuint]()
        val vboArray = stackalloc[GLuint]()

        GLEWHelper.glGenVertexArrays(1.toUInt, vaoArray)
        GLEWHelper.glGenBuffers(1.toUInt, vboArray)

        VAO = !vaoArray
        VBO = !vboArray

        isInitialized = true
        true

      case None =>
        println("Failed to create default shader for BasicRenderer")
        false
  end initialize

  def cleanup(): Unit =
    if isInitialized then
      val vaoArray = stackalloc[GLuint]()
      val vboArray = stackalloc[GLuint]()
      !vaoArray = VAO
      !vboArray = VBO

      GLEWHelper.glDeleteVertexArrays(1.toUInt, vaoArray)
      GLEWHelper.glDeleteBuffers(1.toUInt, vboArray)

      defaultShader.foreach(Shaders.unloadShader)
      defaultShader = None
      isInitialized = false

  def setProjectionMatrix(matrix: Array[Float]): Unit =
    defaultShader.foreach { shader =>
      GLEWHelper.glUseProgram(shader.id.toUInt)
      var i = 0
      while i < 16 do { scratchBuffer(i) = matrix(i); i += 1}
      GLEWHelper.glUniformMatrix4fv(projectionLocation, 1.toUInt, GL_FALSE, scratchBuffer)
    }

  def updateProjectionFromDrawing(): Unit =
    val matrix = Matrix4.ortho(0.0f, Window.width.toFloat, Window.height.toFloat, 0.0f, -1.0f, 1.0f)
    setProjectionMatrix(matrix)

  private def setColor(color: Color): Unit =
    val r = color.r / 255.0f
    val g = color.g / 255.0f
    val b = color.b / 255.0f
    val a = color.a / 255.0f
    GLEWHelper.glUniform4f(colorLocation, r, g, b, a)

  def renderPixel(x: Float, y: Float, color: Color): Unit =
    renderRectangle(x, y, 1.0f, 1.0f, color)
  end renderPixel

  def renderLine(startPos: Vector2, endPos: Vector2, color: Color): Unit =
    if !isInitialized then
      if !initialize() then return

    Drawing.getCurrentShader match
      case Some(customShader) =>
        GLEWHelper.glUseProgram(customShader.id.toUInt)

        Zone {
          val projName = toCString("uProjection")
          val colorName = toCString("uColor")

          val projLocation = GLEWHelper.glGetUniformLocation(customShader.id.toUInt, projName)
          val colorLocation = GLEWHelper.glGetUniformLocation(customShader.id.toUInt, colorName)

          if projLocation >= 0 then
            val matrix = Matrix4.ortho(0.0f, Window.width.toFloat, Window.height.toFloat, 0.0f, -1.0f, 1.0f)
            val matrixPtr = stackalloc[GLfloat](16)
            for i <- matrix.indices do
              matrixPtr(i) = matrix(i)
            GLEWHelper.glUniformMatrix4fv(projLocation, 1.toUInt, GL_FALSE, matrixPtr)

          if colorLocation >= 0 then
            GLEWHelper.glUniform4f(colorLocation, color.rNorm, color.gNorm, color.bNorm, color.aNorm)
        }
      case None =>
        defaultShader.foreach { shader =>
          GLEWHelper.glUseProgram(shader.id.toUInt)
          setColor(color)
        }

    scratchBuffer(0) = startPos.x; scratchBuffer(1) = startPos.y
    scratchBuffer(2) = endPos.x; scratchBuffer(3) = endPos.y

    GLEWHelper.glBindVertexArray(VAO)
    GLEWHelper.glBindBuffer(GL_ARRAY_BUFFER.toUInt, VBO)
    GLEWHelper.glBufferData(GL_ARRAY_BUFFER.toUInt, (4 * sizeof[GLfloat].toInt), scratchBuffer.asInstanceOf[Ptr[Byte]], GL_DYNAMIC_DRAW.toUInt)

    GLEWHelper.glVertexAttribPointer(0.toUInt, 2, GL_FLOAT.toUInt, GL_FALSE, (2 * sizeof[GLfloat].toInt).toUInt, null)
    GLEWHelper.glEnableVertexAttribArray(0.toUInt)

    glDrawArrays(GL_LINES.toUInt, 0, 2.toUInt)

    GLEWHelper.glBindVertexArray(0.toUInt)

  def renderThickLine(startPos: Vector2, endPos: Vector2, thick: Float, color: Color): Unit =
    if !isInitialized then
      if !initialize() then return

    Drawing.getCurrentShader match
      case Some(customShader) =>
        GLEWHelper.glUseProgram(customShader.id.toUInt)

        Zone {
          val projName = toCString("uProjection")
          val colorName = toCString("uColor")

          val projLocation = GLEWHelper.glGetUniformLocation(customShader.id.toUInt, projName)
          val colorLocation = GLEWHelper.glGetUniformLocation(customShader.id.toUInt, colorName)

          if projLocation >= 0 then
            val matrix = Matrix4.ortho(0.0f, Window.width.toFloat, Window.height.toFloat, 0.0f, -1.0f, 1.0f)
            val matrixPtr = stackalloc[GLfloat](16)
            for i <- matrix.indices do
              matrixPtr(i) = matrix(i)
            GLEWHelper.glUniformMatrix4fv(projLocation, 1.toUInt, GL_FALSE, matrixPtr)

          if colorLocation >= 0 then
            GLEWHelper.glUniform4f(colorLocation, color.rNorm, color.gNorm, color.bNorm, color.aNorm)
        }
      case None =>
        defaultShader.foreach { shader =>
          GLEWHelper.glUseProgram(shader.id.toUInt)
          setColor(color)
        }

    val dx = endPos.x - startPos.x
    val dy = endPos.y - startPos.y
    val length = math.sqrt(dx * dx + dy * dy).toFloat

    if length == 0.0f then return
    val perpX = -dy / length * (thick / 2.0f)
    val perpY = dx / length * (thick / 2.0f)

    scratchBuffer(0) = startPos.x + perpX; scratchBuffer(1) = startPos.y + perpY
    scratchBuffer(2) = startPos.x - perpX; scratchBuffer(3) = startPos.y - perpY
    scratchBuffer(4) = endPos.x - perpX; scratchBuffer(5) = endPos.y - perpY
    scratchBuffer(6) = startPos.x + perpX; scratchBuffer(7) = startPos.y + perpY
    scratchBuffer(8) = endPos.x - perpX; scratchBuffer(9) = endPos.y - perpY
    scratchBuffer(10) = endPos.x + perpX; scratchBuffer(11) = endPos.y + perpY

    GLEWHelper.glBindVertexArray(VAO)
    GLEWHelper.glBindBuffer(GL_ARRAY_BUFFER.toUInt, VBO)
    GLEWHelper.glBufferData(GL_ARRAY_BUFFER.toUInt, (12 * sizeof[GLfloat].toInt), scratchBuffer.asInstanceOf[Ptr[Byte]], GL_DYNAMIC_DRAW.toUInt)

    GLEWHelper.glVertexAttribPointer(0.toUInt, 2, GL_FLOAT.toUInt, GL_FALSE, (2 * sizeof[GLfloat].toInt).toUInt, null)
    GLEWHelper.glEnableVertexAttribArray(0.toUInt)

    glDrawArrays(GL_TRIANGLES.toUInt, 0, 6.toUInt)

    GLEWHelper.glBindVertexArray(0.toUInt)

  def renderLineStrip(points: Array[Vector2], color: Color): Unit =
    if !isInitialized then
      if !initialize() then return

    if points.length < 2 then return

    Drawing.getCurrentShader match
      case Some(customShader) =>
        GLEWHelper.glUseProgram(customShader.id.toUInt)

        Zone {
          val projName = toCString("uProjection")
          val colorName = toCString("uColor")

          val projLocation = GLEWHelper.glGetUniformLocation(customShader.id.toUInt, projName)
          val colorLocation = GLEWHelper.glGetUniformLocation(customShader.id.toUInt, colorName)

          if projLocation >= 0 then
            val matrix = Matrix4.ortho(0.0f, Window.width.toFloat, Window.height.toFloat, 0.0f, -1.0f, 1.0f)
            val matrixPtr = stackalloc[GLfloat](16)
            for i <- matrix.indices do
              matrixPtr(i) = matrix(i)
            GLEWHelper.glUniformMatrix4fv(projLocation, 1.toUInt, GL_FALSE, matrixPtr)

          if colorLocation >= 0 then
            GLEWHelper.glUniform4f(colorLocation, color.rNorm, color.gNorm, color.bNorm, color.aNorm)
        }
      case None =>
        defaultShader.foreach { shader =>
          GLEWHelper.glUseProgram(shader.id.toUInt)
          setColor(color)
        }

    var n = 0
    var i = 0
    while i < points.length - 1 do
      scratchBuffer(n) = points(i).x;     n += 1; scratchBuffer(n) = points(i).y;     n += 1
      scratchBuffer(n) = points(i+1).x;   n += 1; scratchBuffer(n) = points(i+1).y;   n += 1
      i += 1

    if n > 0 then
      GLEWHelper.glBindVertexArray(VAO)
      GLEWHelper.glBindBuffer(GL_ARRAY_BUFFER.toUInt, VBO)
      GLEWHelper.glBufferData(GL_ARRAY_BUFFER.toUInt, (n * sizeof[GLfloat].toInt), scratchBuffer.asInstanceOf[Ptr[Byte]], GL_DYNAMIC_DRAW.toUInt)
      GLEWHelper.glVertexAttribPointer(0.toUInt, 2, GL_FLOAT.toUInt, GL_FALSE, (2 * sizeof[GLfloat].toInt).toUInt, null)
      GLEWHelper.glEnableVertexAttribArray(0.toUInt)
      glDrawArrays(GL_LINES.toUInt, 0, (n / 2).toUInt)
      GLEWHelper.glBindVertexArray(0.toUInt)

  def renderRectangle(x: Float, y: Float, width: Float, height: Float, color: Color): Unit =
    if !isInitialized then
      if !initialize() then return

    Drawing.getCurrentShader match
      case Some(customShader) =>
        GLEWHelper.glUseProgram(customShader.id.toUInt)

        Zone {
          val projName = toCString("uProjection")
          val colorName = toCString("uColor")

          val projLocation = GLEWHelper.glGetUniformLocation(customShader.id.toUInt, projName)
          val colorLocation = GLEWHelper.glGetUniformLocation(customShader.id.toUInt, colorName)

          if projLocation >= 0 then
            val matrix = Matrix4.ortho(0.0f, Window.width.toFloat, Window.height.toFloat, 0.0f, -1.0f, 1.0f)
            val matrixPtr = stackalloc[GLfloat](16)
            for i <- matrix.indices do
              matrixPtr(i) = matrix(i)
            GLEWHelper.glUniformMatrix4fv(projLocation, 1.toUInt, GL_FALSE, matrixPtr)

          if colorLocation >= 0 then
            GLEWHelper.glUniform4f(colorLocation, color.rNorm, color.gNorm, color.bNorm, color.aNorm)
        }
      case None =>
        defaultShader.foreach { shader =>
          GLEWHelper.glUseProgram(shader.id.toUInt)
          setColor(color)
        }

    scratchBuffer(0)  = x; scratchBuffer(1) = y
    scratchBuffer(2)  = x + width; scratchBuffer(3) = y
    scratchBuffer(4)  = x; scratchBuffer(5) = y + height
    scratchBuffer(6)  = x + width; scratchBuffer(7) = y
    scratchBuffer(8)  = x + width; scratchBuffer(9) = y + height
    scratchBuffer(10) = x; scratchBuffer(11) = y + height
    GLEWHelper.glBindVertexArray(VAO)
    GLEWHelper.glBindBuffer(GL_ARRAY_BUFFER.toUInt, VBO)
    GLEWHelper.glBufferData(GL_ARRAY_BUFFER.toUInt, (12 * sizeof[GLfloat].toInt), scratchBuffer.asInstanceOf[Ptr[Byte]], GL_DYNAMIC_DRAW.toUInt)

    GLEWHelper.glVertexAttribPointer(0.toUInt, 2, GL_FLOAT.toUInt, GL_FALSE, (2 * sizeof[GLfloat].toInt).toUInt, null)
    GLEWHelper.glEnableVertexAttribArray(0.toUInt)

    glDrawArrays(GL_TRIANGLES.toUInt, 0, 6.toUInt)

    GLEWHelper.glBindVertexArray(0.toUInt)

  def renderRotatedRectangle(rectangle: Rectangle, origin: Vector2, rotation: Float, color: Color): Unit =
    if !isInitialized then
      if !initialize() then return

    Drawing.getCurrentShader match
      case Some(customShader) =>
        GLEWHelper.glUseProgram(customShader.id.toUInt)

        Zone {
          val projName = toCString("uProjection")
          val colorName = toCString("uColor")

          val projLocation = GLEWHelper.glGetUniformLocation(customShader.id.toUInt, projName)
          val colorLocation = GLEWHelper.glGetUniformLocation(customShader.id.toUInt, colorName)

          if projLocation >= 0 then
            val matrix = Matrix4.ortho(0.0f, Window.width.toFloat, Window.height.toFloat, 0.0f, -1.0f, 1.0f)
            val matrixPtr = stackalloc[GLfloat](16)
            for i <- matrix.indices do
              matrixPtr(i) = matrix(i)
            GLEWHelper.glUniformMatrix4fv(projLocation, 1.toUInt, GL_FALSE, matrixPtr)

          if colorLocation >= 0 then
            GLEWHelper.glUniform4f(colorLocation, color.rNorm, color.gNorm, color.bNorm, color.aNorm)
        }
      case None =>
        defaultShader.foreach { shader =>
          GLEWHelper.glUseProgram(shader.id.toUInt)
          setColor(color)
        }

    val angleRad = math.toRadians(rotation).toFloat
    val cos = math.cos(angleRad).toFloat
    val sin = math.sin(angleRad).toFloat

    val ox = rectangle.x + origin.x
    val oy = rectangle.y + origin.y

    def rx(lx: Float, ly: Float): Float = lx * cos - ly * sin + ox
    def ry(lx: Float, ly: Float): Float = lx * sin + ly * cos + oy

    val lx0 = -origin.x; val ly0 = -origin.y
    val lx1 = rectangle.width - origin.x; val ly1 = -origin.y
    val lx2 = rectangle.width - origin.x; val ly2 = rectangle.height - origin.y
    val lx3 = -origin.x; val ly3 = rectangle.height - origin.y

    scratchBuffer(0) = rx(lx0, ly0); scratchBuffer(1) = ry(lx0, ly0)
    scratchBuffer(2) = rx(lx1, ly1); scratchBuffer(3) = ry(lx1, ly1)
    scratchBuffer(4) = rx(lx2, ly2); scratchBuffer(5) = ry(lx2, ly2)
    scratchBuffer(6) = rx(lx0, ly0); scratchBuffer(7) = ry(lx0, ly0)
    scratchBuffer(8) = rx(lx2, ly2); scratchBuffer(9) = ry(lx2, ly2)
    scratchBuffer(10) = rx(lx3, ly3); scratchBuffer(11) = ry(lx3, ly3)
    GLEWHelper.glBindVertexArray(VAO)
    GLEWHelper.glBindBuffer(GL_ARRAY_BUFFER.toUInt, VBO)
    GLEWHelper.glBufferData(GL_ARRAY_BUFFER.toUInt, (12 * sizeof[GLfloat].toInt), scratchBuffer.asInstanceOf[Ptr[Byte]], GL_DYNAMIC_DRAW.toUInt)

    GLEWHelper.glVertexAttribPointer(0.toUInt, 2, GL_FLOAT.toUInt, GL_FALSE, (2 * sizeof[GLfloat].toInt).toUInt, null)
    GLEWHelper.glEnableVertexAttribArray(0.toUInt)

    glDrawArrays(GL_TRIANGLES.toUInt, 0, 6.toUInt)

    GLEWHelper.glBindVertexArray(0.toUInt)

  def renderRectangleOutline(x: Float, y: Float, width: Float, height: Float, color: Color): Unit =
    if !isInitialized then
      if !initialize() then return

    Drawing.getCurrentShader match
      case Some(customShader) =>
        GLEWHelper.glUseProgram(customShader.id.toUInt)

        Zone {
          val projName = toCString("uProjection")
          val colorName = toCString("uColor")

          val projLocation = GLEWHelper.glGetUniformLocation(customShader.id.toUInt, projName)
          val colorLocation = GLEWHelper.glGetUniformLocation(customShader.id.toUInt, colorName)

          if projLocation >= 0 then
            val matrix = Matrix4.ortho(0.0f, Window.width.toFloat, Window.height.toFloat, 0.0f, -1.0f, 1.0f)
            val matrixPtr = stackalloc[GLfloat](16)
            for i <- matrix.indices do
              matrixPtr(i) = matrix(i)
            GLEWHelper.glUniformMatrix4fv(projLocation, 1.toUInt, GL_FALSE, matrixPtr)

          if colorLocation >= 0 then
            GLEWHelper.glUniform4f(colorLocation, color.rNorm, color.gNorm, color.bNorm, color.aNorm)
        }
      case None =>
        defaultShader.foreach { shader =>
          GLEWHelper.glUseProgram(shader.id.toUInt)
          setColor(color)
        }

    scratchBuffer(0) = x; scratchBuffer(1) = y
    scratchBuffer(2) = x + width; scratchBuffer(3) = y
    scratchBuffer(4) = x + width; scratchBuffer(5) = y
    scratchBuffer(6) = x + width; scratchBuffer(7) = y + height
    scratchBuffer(8) = x + width; scratchBuffer(9) = y + height
    scratchBuffer(10) = x; scratchBuffer(11) = y + height
    scratchBuffer(12) = x; scratchBuffer(13) = y + height
    scratchBuffer(14) = x; scratchBuffer(15) = y
    GLEWHelper.glBindVertexArray(VAO)
    GLEWHelper.glBindBuffer(GL_ARRAY_BUFFER.toUInt, VBO)
    GLEWHelper.glBufferData(GL_ARRAY_BUFFER.toUInt, (16 * sizeof[GLfloat].toInt), scratchBuffer.asInstanceOf[Ptr[Byte]], GL_DYNAMIC_DRAW.toUInt)

    GLEWHelper.glVertexAttribPointer(0.toUInt, 2, GL_FLOAT.toUInt, GL_FALSE, (2 * sizeof[GLfloat].toInt).toUInt, null)
    GLEWHelper.glEnableVertexAttribArray(0.toUInt)

    glDrawArrays(GL_LINES.toUInt, 0, 8.toUInt)

    GLEWHelper.glBindVertexArray(0.toUInt)

  def renderRoundedRectangle(rect: Rectangle, roundness: Float, segments: Int, color: Color): Unit =
    if !isInitialized then
      if !initialize() then return

    if segments < 3 then return
    if roundness <= 0.0f then
      renderRectangle(rect.x, rect.y, rect.width, rect.height, color)
      return

    Drawing.getCurrentShader match
      case Some(customShader) =>
        GLEWHelper.glUseProgram(customShader.id.toUInt)

        Zone {
          val projName = toCString("uProjection")
          val colorName = toCString("uColor")

          val projLocation = GLEWHelper.glGetUniformLocation(customShader.id.toUInt, projName)
          val colorLocation = GLEWHelper.glGetUniformLocation(customShader.id.toUInt, colorName)

          if projLocation >= 0 then
            val matrix = Matrix4.ortho(0.0f, Window.width.toFloat, Window.height.toFloat, 0.0f, -1.0f, 1.0f)
            val matrixPtr = stackalloc[GLfloat](16)
            for i <- matrix.indices do
              matrixPtr(i) = matrix(i)
            GLEWHelper.glUniformMatrix4fv(projLocation, 1.toUInt, GL_FALSE, matrixPtr)

          if colorLocation >= 0 then
            GLEWHelper.glUniform4f(colorLocation, color.rNorm, color.gNorm, color.bNorm, color.aNorm)
        }
      case None =>
        defaultShader.foreach { shader =>
          GLEWHelper.glUseProgram(shader.id.toUInt)
          setColor(color)
        }

    val maxRadius = math.min(rect.width, rect.height) / 2.0f
    val cornerRadius = (roundness * maxRadius).min(maxRadius)
    val innerWidth = rect.width - 2 * cornerRadius
    val innerHeight = rect.height - 2 * cornerRadius

    var n = 0

    def addQuad(x1: Float, y1: Float, x2: Float, y2: Float, x3: Float, y3: Float, x4: Float, y4: Float): Unit =
      scratchBuffer(n) = x1; n += 1; scratchBuffer(n) = y1; n += 1
      scratchBuffer(n) = x2; n += 1; scratchBuffer(n) = y2; n += 1
      scratchBuffer(n) = x3; n += 1; scratchBuffer(n) = y3; n += 1
      scratchBuffer(n) = x1; n += 1; scratchBuffer(n) = y1; n += 1
      scratchBuffer(n) = x3; n += 1; scratchBuffer(n) = y3; n += 1
      scratchBuffer(n) = x4; n += 1; scratchBuffer(n) = y4; n += 1

    if innerWidth > 0 && innerHeight > 0 then
      addQuad(
        rect.x + cornerRadius, rect.y + cornerRadius,
        rect.x + cornerRadius + innerWidth, rect.y + cornerRadius,
        rect.x + cornerRadius + innerWidth, rect.y + cornerRadius + innerHeight,
        rect.x + cornerRadius, rect.y + cornerRadius + innerHeight
      )

    if innerWidth > 0 then
      addQuad(
        rect.x + cornerRadius, rect.y,
        rect.x + cornerRadius + innerWidth, rect.y,
        rect.x + cornerRadius + innerWidth, rect.y + cornerRadius,
        rect.x + cornerRadius, rect.y + cornerRadius
      )

    if innerWidth > 0 then
      addQuad(
        rect.x + cornerRadius, rect.y + cornerRadius + innerHeight,
        rect.x + cornerRadius + innerWidth, rect.y + cornerRadius + innerHeight,
        rect.x + cornerRadius + innerWidth, rect.y + rect.height,
        rect.x + cornerRadius, rect.y + rect.height
      )

    if innerHeight > 0 then
      addQuad(
        rect.x, rect.y + cornerRadius,
        rect.x + cornerRadius, rect.y + cornerRadius,
        rect.x + cornerRadius, rect.y + cornerRadius + innerHeight,
        rect.x, rect.y + cornerRadius + innerHeight
      )

    if innerHeight > 0 then
      addQuad(
        rect.x + cornerRadius + innerWidth, rect.y + cornerRadius,
        rect.x + rect.width, rect.y + cornerRadius,
        rect.x + rect.width, rect.y + cornerRadius + innerHeight,
        rect.x + cornerRadius + innerWidth, rect.y + cornerRadius + innerHeight
      )

    def addCorner(centerX: Float, centerY: Float, startAngle: Float): Unit =
      val angleStep = (math.Pi / 2.0f) / segments.toFloat
      var i = 0
      while i < segments do
        val angle1 = startAngle + (i * angleStep)
        val angle2 = startAngle + ((i + 1) * angleStep)
        val x1 = centerX + cornerRadius * math.cos(angle1).toFloat
        val y1 = centerY + cornerRadius * math.sin(angle1).toFloat
        val x2 = centerX + cornerRadius * math.cos(angle2).toFloat
        val y2 = centerY + cornerRadius * math.sin(angle2).toFloat
        scratchBuffer(n) = centerX; n += 1; scratchBuffer(n) = centerY; n += 1
        scratchBuffer(n) = x1;      n += 1; scratchBuffer(n) = y1;      n += 1
        scratchBuffer(n) = x2;      n += 1; scratchBuffer(n) = y2;      n += 1
        i += 1

    addCorner(rect.x + cornerRadius, rect.y + cornerRadius, math.Pi.toFloat) // top-left
    addCorner(rect.x + rect.width - cornerRadius, rect.y + cornerRadius, 3 * math.Pi.toFloat / 2) // top-right
    addCorner(rect.x + rect.width - cornerRadius, rect.y + rect.height - cornerRadius, 0.0f) // bottom-right
    addCorner(rect.x + cornerRadius, rect.y + rect.height - cornerRadius, math.Pi.toFloat / 2) // bottom-left

    if n > 0 then
      GLEWHelper.glBindVertexArray(VAO)
      GLEWHelper.glBindBuffer(GL_ARRAY_BUFFER.toUInt, VBO)
      GLEWHelper.glBufferData(GL_ARRAY_BUFFER.toUInt, (n * sizeof[GLfloat].toInt), scratchBuffer.asInstanceOf[Ptr[Byte]], GL_DYNAMIC_DRAW.toUInt)
      GLEWHelper.glVertexAttribPointer(0.toUInt, 2, GL_FLOAT.toUInt, GL_FALSE, (2 * sizeof[GLfloat].toInt).toUInt, null)
      GLEWHelper.glEnableVertexAttribArray(0.toUInt)
      glDrawArrays(GL_TRIANGLES.toUInt, 0, (n / 2).toUInt)
      GLEWHelper.glBindVertexArray(0.toUInt)

  def renderRoundedRectangleOutline(rectangle: Rectangle, roundness: Float, segments: Int, color: Color): Unit =
    // mati todo -> update function with scratchbuffer for better performance
    if !isInitialized then
      if !initialize() then return

    if segments < 3 then return
    if roundness <= 0.0f then
      renderRectangleOutline(rectangle.x, rectangle.y, rectangle.width, rectangle.height, color)
      return

    Drawing.getCurrentShader match
      case Some(customShader) =>
        GLEWHelper.glUseProgram(customShader.id.toUInt)

        Zone {
          val projName = toCString("uProjection")
          val colorName = toCString("uColor")

          val projLocation = GLEWHelper.glGetUniformLocation(customShader.id.toUInt, projName)
          val colorLocation = GLEWHelper.glGetUniformLocation(customShader.id.toUInt, colorName)

          if projLocation >= 0 then
            val matrix = Matrix4.ortho(0.0f, Window.width.toFloat, Window.height.toFloat, 0.0f, -1.0f, 1.0f)
            val matrixPtr = stackalloc[GLfloat](16)
            for i <- matrix.indices do
              matrixPtr(i) = matrix(i)
            GLEWHelper.glUniformMatrix4fv(projLocation, 1.toUInt, GL_FALSE, matrixPtr)

          if colorLocation >= 0 then
            GLEWHelper.glUniform4f(colorLocation, color.rNorm, color.gNorm, color.bNorm, color.aNorm)
        }
      case None =>
        defaultShader.foreach { shader =>
          GLEWHelper.glUseProgram(shader.id.toUInt)
          setColor(color)
        }

    val maxRadius = math.min(rectangle.width, rectangle.height) / 2.0f
    val cornerRadius = (roundness * maxRadius).min(maxRadius)
    val angleStep = (math.Pi / 2.0f) / segments.toFloat

    val vertices = scala.collection.mutable.ArrayBuffer[Float]()

    vertices += rectangle.x + cornerRadius;
    vertices += rectangle.y
    vertices += rectangle.x + rectangle.width - cornerRadius;
    vertices += rectangle.y

    for i <- 0 until segments do
      val angle1 = (3 * math.Pi / 2.0f) + (i * angleStep)
      val angle2 = (3 * math.Pi / 2.0f) + ((i + 1) * angleStep)

      val x1 = rectangle.x + rectangle.width - cornerRadius + cornerRadius * math.cos(angle1).toFloat
      val y1 = rectangle.y + cornerRadius + cornerRadius * math.sin(angle1).toFloat
      val x2 = rectangle.x + rectangle.width - cornerRadius + cornerRadius * math.cos(angle2).toFloat
      val y2 = rectangle.y + cornerRadius + cornerRadius * math.sin(angle2).toFloat

      vertices += x1; vertices += y1
      vertices += x2; vertices += y2

    vertices += rectangle.x + rectangle.width;
    vertices += rectangle.y + cornerRadius
    vertices += rectangle.x + rectangle.width;
    vertices += rectangle.y + rectangle.height - cornerRadius

    for i <- 0 until segments do
      val angle1 = (i * angleStep)
      val angle2 = ((i + 1) * angleStep)

      val x1 = rectangle.x + rectangle.width - cornerRadius + cornerRadius * math.cos(angle1).toFloat
      val y1 = rectangle.y + rectangle.height - cornerRadius + cornerRadius * math.sin(angle1).toFloat
      val x2 = rectangle.x + rectangle.width - cornerRadius + cornerRadius * math.cos(angle2).toFloat
      val y2 = rectangle.y + rectangle.height - cornerRadius + cornerRadius * math.sin(angle2).toFloat

      vertices += x1; vertices += y1
      vertices += x2; vertices += y2

    vertices += rectangle.x + rectangle.width - cornerRadius;
    vertices += rectangle.y + rectangle.height
    vertices += rectangle.x + cornerRadius;
    vertices += rectangle.y + rectangle.height

    for i <- 0 until segments do
      val angle1 = (math.Pi / 2.0f) + (i * angleStep)
      val angle2 = (math.Pi / 2.0f) + ((i + 1) * angleStep)

      val x1 = rectangle.x + cornerRadius + cornerRadius * math.cos(angle1).toFloat
      val y1 = rectangle.y + rectangle.height - cornerRadius + cornerRadius * math.sin(angle1).toFloat
      val x2 = rectangle.x + cornerRadius + cornerRadius * math.cos(angle2).toFloat
      val y2 = rectangle.y + rectangle.height - cornerRadius + cornerRadius * math.sin(angle2).toFloat

      vertices += x1; vertices += y1
      vertices += x2; vertices += y2

    vertices += rectangle.x;
    vertices += rectangle.y + rectangle.height - cornerRadius
    vertices += rectangle.x;
    vertices += rectangle.y + cornerRadius

    for i <- 0 until segments do
      val angle1 = math.Pi.toFloat + (i * angleStep)
      val angle2 = math.Pi.toFloat + ((i + 1) * angleStep)

      val x1 = rectangle.x + cornerRadius + cornerRadius * math.cos(angle1).toFloat
      val y1 = rectangle.y + cornerRadius + cornerRadius * math.sin(angle1).toFloat
      val x2 = rectangle.x + cornerRadius + cornerRadius * math.cos(angle2).toFloat
      val y2 = rectangle.y + cornerRadius + cornerRadius * math.sin(angle2).toFloat

      vertices += x1; vertices += y1
      vertices += x2; vertices += y2

    if vertices.nonEmpty then
      GLEWHelper.glBindVertexArray(VAO)
      GLEWHelper.glBindBuffer(GL_ARRAY_BUFFER.toUInt, VBO)

      Zone {
        val verticesPtr = stackalloc[GLfloat](vertices.length)
        for i <- vertices.indices do
          verticesPtr(i) = vertices(i)

        GLEWHelper.glBufferData(
          GL_ARRAY_BUFFER.toUInt,
          (vertices.length * sizeof[GLfloat].toInt),
          verticesPtr.asInstanceOf[Ptr[Byte]],
          GL_DYNAMIC_DRAW.toUInt
        )
      }

      GLEWHelper.glVertexAttribPointer(0.toUInt, 2, GL_FLOAT.toUInt, GL_FALSE, (2 * sizeof[GLfloat].toInt).toUInt, null)
      GLEWHelper.glEnableVertexAttribArray(0.toUInt)

      glDrawArrays(GL_LINES.toUInt, 0, (vertices.length / 2).toUInt)

      GLEWHelper.glBindVertexArray(0.toUInt)

  def renderTriangle(v1: Vector2, v2: Vector2, v3: Vector2, color: Color): Unit =
    if !isInitialized then
      if !initialize() then return

    Drawing.getCurrentShader match
      case Some(customShader) =>
        GLEWHelper.glUseProgram(customShader.id.toUInt)

        Zone {
          val projName = toCString("uProjection")
          val colorName = toCString("uColor")

          val projLocation = GLEWHelper.glGetUniformLocation(customShader.id.toUInt, projName)
          val colorLocation = GLEWHelper.glGetUniformLocation(customShader.id.toUInt, colorName)

          if projLocation >= 0 then
            val matrix = Matrix4.ortho(0.0f, Window.width.toFloat, Window.height.toFloat, 0.0f, -1.0f, 1.0f)
            val matrixPtr = stackalloc[GLfloat](16)
            for i <- matrix.indices do
              matrixPtr(i) = matrix(i)
            GLEWHelper.glUniformMatrix4fv(projLocation, 1.toUInt, GL_FALSE, matrixPtr)

          if colorLocation >= 0 then
            GLEWHelper.glUniform4f(colorLocation, color.rNorm, color.gNorm, color.bNorm, color.aNorm)
        }
      case None =>
        defaultShader.foreach { shader =>
          GLEWHelper.glUseProgram(shader.id.toUInt)
          setColor(color)
        }

    scratchBuffer(0) = v1.x; scratchBuffer(1) = v1.y
    scratchBuffer(2) = v2.x; scratchBuffer(3) = v2.y
    scratchBuffer(4) = v3.x; scratchBuffer(5) = v3.y
    GLEWHelper.glBindVertexArray(VAO)
    GLEWHelper.glBindBuffer(GL_ARRAY_BUFFER.toUInt, VBO)
    GLEWHelper.glBufferData(GL_ARRAY_BUFFER.toUInt, (6 * sizeof[GLfloat].toInt), scratchBuffer.asInstanceOf[Ptr[Byte]], GL_DYNAMIC_DRAW.toUInt)

    GLEWHelper.glVertexAttribPointer(0.toUInt, 2, GL_FLOAT.toUInt, GL_FALSE, (2 * sizeof[GLfloat].toInt).toUInt, null)
    GLEWHelper.glEnableVertexAttribArray(0.toUInt)

    glDrawArrays(GL_TRIANGLES.toUInt, 0, 3.toUInt)

    GLEWHelper.glBindVertexArray(0.toUInt)

  def renderTriangleOutline(v1: Vector2, v2: Vector2, v3: Vector2, color: Color): Unit =
    if !isInitialized then
      if !initialize() then return

    Drawing.getCurrentShader match
      case Some(customShader) =>
        GLEWHelper.glUseProgram(customShader.id.toUInt)

        Zone {
          val projName = toCString("uProjection")
          val colorName = toCString("uColor")

          val projLocation = GLEWHelper.glGetUniformLocation(customShader.id.toUInt, projName)
          val colorLocation = GLEWHelper.glGetUniformLocation(customShader.id.toUInt, colorName)

          if projLocation >= 0 then
            val matrix = Matrix4.ortho(0.0f, Window.width.toFloat, Window.height.toFloat, 0.0f, -1.0f, 1.0f)
            val matrixPtr = stackalloc[GLfloat](16)
            for i <- matrix.indices do
              matrixPtr(i) = matrix(i)
            GLEWHelper.glUniformMatrix4fv(projLocation, 1.toUInt, GL_FALSE, matrixPtr)

          if colorLocation >= 0 then
            GLEWHelper.glUniform4f(colorLocation, color.rNorm, color.gNorm, color.bNorm, color.aNorm)
        }
      case None =>
        defaultShader.foreach { shader =>
          GLEWHelper.glUseProgram(shader.id.toUInt)
          setColor(color)
        }

    scratchBuffer(0) = v1.x; scratchBuffer(1) = v1.y
    scratchBuffer(2) = v2.x; scratchBuffer(3) = v2.y
    scratchBuffer(4) = v2.x; scratchBuffer(5) = v2.y
    scratchBuffer(6) = v3.x; scratchBuffer(7) = v3.y
    scratchBuffer(8) = v3.x; scratchBuffer(9) = v3.y
    scratchBuffer(10) = v1.x; scratchBuffer(11) = v1.y
    GLEWHelper.glBindVertexArray(VAO)
    GLEWHelper.glBindBuffer(GL_ARRAY_BUFFER.toUInt, VBO)
    GLEWHelper.glBufferData(GL_ARRAY_BUFFER.toUInt, (12 * sizeof[GLfloat].toInt), scratchBuffer.asInstanceOf[Ptr[Byte]], GL_DYNAMIC_DRAW.toUInt)

    GLEWHelper.glVertexAttribPointer(0.toUInt, 2, GL_FLOAT.toUInt, GL_FALSE, (2 * sizeof[GLfloat].toInt).toUInt, null)
    GLEWHelper.glEnableVertexAttribArray(0.toUInt)

    glDrawArrays(GL_LINES.toUInt, 0, 6.toUInt)

    GLEWHelper.glBindVertexArray(0.toUInt)

  def renderTriangleFan(points: Array[Vector2], color: Color): Unit =
    if !isInitialized then
      if !initialize() then return

    if points.length < 3 then return

    Drawing.getCurrentShader match
      case Some(customShader) =>
        GLEWHelper.glUseProgram(customShader.id.toUInt)

        Zone {
          val projName = toCString("uProjection")
          val colorName = toCString("uColor")

          val projLocation = GLEWHelper.glGetUniformLocation(customShader.id.toUInt, projName)
          val colorLocation = GLEWHelper.glGetUniformLocation(customShader.id.toUInt, colorName)

          if projLocation >= 0 then
            val matrix = Matrix4.ortho(0.0f, Window.width.toFloat, Window.height.toFloat, 0.0f, -1.0f, 1.0f)
            val matrixPtr = stackalloc[GLfloat](16)
            for i <- matrix.indices do
              matrixPtr(i) = matrix(i)
            GLEWHelper.glUniformMatrix4fv(projLocation, 1.toUInt, GL_FALSE, matrixPtr)

          if colorLocation >= 0 then
            GLEWHelper.glUniform4f(colorLocation, color.rNorm, color.gNorm, color.bNorm, color.aNorm)
        }
      case None =>
        defaultShader.foreach { shader =>
          GLEWHelper.glUseProgram(shader.id.toUInt)
          setColor(color)
        }

    var n = 0
    val centerPoint = points(0)
    var i = 1
    while i < points.length - 1 do
      scratchBuffer(n) = centerPoint.x; n += 1; scratchBuffer(n) = centerPoint.y; n += 1
      scratchBuffer(n)= points(i).x; n += 1; scratchBuffer(n) = points(i).y; n += 1
      scratchBuffer(n) = points(i + 1).x; n += 1; scratchBuffer(n) = points(i + 1).y; n += 1
      i += 1

    if n > 0 then
      GLEWHelper.glBindVertexArray(VAO)
      GLEWHelper.glBindBuffer(GL_ARRAY_BUFFER.toUInt, VBO)
      GLEWHelper.glBufferData(GL_ARRAY_BUFFER.toUInt, (n * sizeof[GLfloat].toInt), scratchBuffer.asInstanceOf[Ptr[Byte]], GL_DYNAMIC_DRAW.toUInt)
      GLEWHelper.glVertexAttribPointer(0.toUInt, 2, GL_FLOAT.toUInt, GL_FALSE, (2 * sizeof[GLfloat].toInt).toUInt, null)
      GLEWHelper.glEnableVertexAttribArray(0.toUInt)
      glDrawArrays(GL_TRIANGLES.toUInt, 0, (n / 2).toUInt)
      GLEWHelper.glBindVertexArray(0.toUInt)

  def renderTriangleStrip(points: Array[Vector2], color: Color): Unit =
    if !isInitialized then
      if !initialize() then return

    if points.length < 3 then return

    Drawing.getCurrentShader match
      case Some(customShader) =>
        GLEWHelper.glUseProgram(customShader.id.toUInt)

        Zone {
          val projName = toCString("uProjection")
          val colorName = toCString("uColor")

          val projLocation = GLEWHelper.glGetUniformLocation(customShader.id.toUInt, projName)
          val colorLocation = GLEWHelper.glGetUniformLocation(customShader.id.toUInt, colorName)

          if projLocation >= 0 then
            val matrix = Matrix4.ortho(0.0f, Window.width.toFloat, Window.height.toFloat, 0.0f, -1.0f, 1.0f)
            val matrixPtr = stackalloc[GLfloat](16)
            for i <- matrix.indices do
              matrixPtr(i) = matrix(i)
            GLEWHelper.glUniformMatrix4fv(projLocation, 1.toUInt, GL_FALSE, matrixPtr)

          if colorLocation >= 0 then
            GLEWHelper.glUniform4f(colorLocation, color.rNorm, color.gNorm, color.bNorm, color.aNorm)
        }
      case None =>
        defaultShader.foreach { shader =>
          GLEWHelper.glUseProgram(shader.id.toUInt)
          setColor(color)
        }

    var n = 0
    var i = 0
    while i < points.length - 2 do
      if i % 2 == 0 then
        scratchBuffer(n) = points(i).x; n += 1; scratchBuffer(n) = points(i).y; n += 1
        scratchBuffer(n) = points(i+1).x; n += 1; scratchBuffer(n) = points(i+1).y; n += 1
        scratchBuffer(n) = points(i+2).x; n += 1; scratchBuffer(n) = points(i+2).y; n += 1
      else
        scratchBuffer(n) = points(i).x; n += 1; scratchBuffer(n) = points(i).y; n += 1
        scratchBuffer(n) = points(i+2).x; n += 1; scratchBuffer(n) = points(i+2).y; n += 1
        scratchBuffer(n) = points(i+1).x; n += 1; scratchBuffer(n) = points(i+1).y; n += 1
      i += 1

    if n > 0 then
      GLEWHelper.glBindVertexArray(VAO)
      GLEWHelper.glBindBuffer(GL_ARRAY_BUFFER.toUInt, VBO)
      GLEWHelper.glBufferData(GL_ARRAY_BUFFER.toUInt, (n * sizeof[GLfloat].toInt), scratchBuffer.asInstanceOf[Ptr[Byte]], GL_DYNAMIC_DRAW.toUInt)
      GLEWHelper.glVertexAttribPointer(0.toUInt, 2, GL_FLOAT.toUInt, GL_FALSE, (2 * sizeof[GLfloat].toInt).toUInt, null)
      GLEWHelper.glEnableVertexAttribArray(0.toUInt)
      glDrawArrays(GL_TRIANGLES.toUInt, 0, (n / 2).toUInt)
      GLEWHelper.glBindVertexArray(0.toUInt)

  def renderPolygon(center: Vector2, sides: Int, radius: Float, rotation: Float, color: Color): Unit =
    if !isInitialized then
      if !initialize() then return

    if sides < 3 then return

    val rotationRad = math.toRadians(rotation).toFloat
    val angleStep = (2.0f * math.Pi / sides.toFloat).toFloat

    val points = Array.ofDim[Vector2](sides + 2)

    points(0) = center

    for i <- 0 until sides do
      val angle = (i * angleStep) + rotationRad
      val x = center.x + radius * math.cos(angle).toFloat
      val y = center.y + radius * math.sin(angle).toFloat
      points(i + 1) = Vector2(x, y)

    points(sides + 1) = points(1)

    renderTriangleFan(points, color)

  def renderPolygonOutline(center: Vector2, sides: Int, radius: Float, rotation: Float, color: Color): Unit =
    if !isInitialized then
      if !initialize() then return

    if sides < 3 then return

    Drawing.getCurrentShader match
      case Some(customShader) =>
        GLEWHelper.glUseProgram(customShader.id.toUInt)

        Zone {
          val projName = toCString("uProjection")
          val colorName = toCString("uColor")

          val projLocation = GLEWHelper.glGetUniformLocation(customShader.id.toUInt, projName)
          val colorLocation = GLEWHelper.glGetUniformLocation(customShader.id.toUInt, colorName)

          if projLocation >= 0 then
            val matrix = Matrix4.ortho(0.0f, Window.width.toFloat, Window.height.toFloat, 0.0f, -1.0f, 1.0f)
            val matrixPtr = stackalloc[GLfloat](16)
            for i <- matrix.indices do
              matrixPtr(i) = matrix(i)
            GLEWHelper.glUniformMatrix4fv(projLocation, 1.toUInt, GL_FALSE, matrixPtr)

          if colorLocation >= 0 then
            GLEWHelper.glUniform4f(colorLocation, color.rNorm, color.gNorm, color.bNorm, color.aNorm)
        }
      case None =>
        defaultShader.foreach { shader =>
          GLEWHelper.glUseProgram(shader.id.toUInt)
          setColor(color)
        }

    val rotationRad = math.toRadians(rotation).toFloat
    val angleStep = (2.0f * math.Pi / sides.toFloat).toFloat
    var n = 0
    var i = 0
    while i < sides do
      val angle = (i * angleStep) + rotationRad
      val angleN = ((i + 1) % sides * angleStep) + rotationRad
      scratchBuffer(n) =center.x + radius * math.cos(angle).toFloat; n += 1
      scratchBuffer(n) = center.y + radius * math.sin(angle).toFloat; n += 1
      scratchBuffer(n) = center.x + radius * math.cos(angleN).toFloat; n += 1
      scratchBuffer(n) = center.y + radius * math.sin(angleN).toFloat; n += 1
      i += 1

    if n > 0 then
      GLEWHelper.glBindVertexArray(VAO)
      GLEWHelper.glBindBuffer(GL_ARRAY_BUFFER.toUInt, VBO)
      GLEWHelper.glBufferData(GL_ARRAY_BUFFER.toUInt, (n * sizeof[GLfloat].toInt), scratchBuffer.asInstanceOf[Ptr[Byte]], GL_DYNAMIC_DRAW.toUInt)
      GLEWHelper.glVertexAttribPointer(0.toUInt, 2, GL_FLOAT.toUInt, GL_FALSE, (2 * sizeof[GLfloat].toInt).toUInt, null)
      GLEWHelper.glEnableVertexAttribArray(0.toUInt)
      glDrawArrays(GL_LINES.toUInt, 0, (n / 2).toUInt)
      GLEWHelper.glBindVertexArray(0.toUInt)

  def renderCircle(centerX: Float, centerY: Float, radius: Float, color: Color): Unit =
    if !isInitialized then
      if !initialize() then return

    val segments = 36

    val points = Array.ofDim[Vector2](segments + 2)

    points(0) = Vector2(centerX, centerY)

    for i <- 0 until segments do
      val angle = (i * 2.0f * math.Pi / segments).toFloat
      val x = centerX + radius * math.cos(angle).toFloat
      val y = centerY + radius * math.sin(angle).toFloat
      points(i + 1) = Vector2(x, y)

    points(segments + 1) = points(1)

    renderTriangleFan(points, color)

  def renderCircleSector(center: Vector2, radius: Float, startAngle: Float, endAngle: Float, segments: Int, color: Color): Unit =
    if !isInitialized then
      if !initialize() then return

    if segments < 3 then return

    val startRad = math.toRadians(startAngle).toFloat
    val endRad = math.toRadians(endAngle).toFloat
    val angleStep = (endRad - startRad) / segments.toFloat

    val points = Array.ofDim[Vector2](segments + 2)

    points(0) = center

    for i <- 0 to segments do
      val angle = startRad + (i * angleStep)
      val x = center.x + radius * math.cos(angle).toFloat
      val y = center.y + radius * math.sin(angle).toFloat
      points(i + 1) = Vector2(x, y)

    renderTriangleFan(points, color)

  def renderCircleSectorOutline(center: Vector2, radius: Float, startAngle: Float, endAngle: Float, segments: Int, color: Color): Unit =
    if !isInitialized then
      if !initialize() then return

    if segments < 3 then return

    Drawing.getCurrentShader match
      case Some(customShader) =>
        GLEWHelper.glUseProgram(customShader.id.toUInt)

        Zone {
          val projName = toCString("uProjection")
          val colorName = toCString("uColor")

          val projLocation = GLEWHelper.glGetUniformLocation(customShader.id.toUInt, projName)
          val colorLocation = GLEWHelper.glGetUniformLocation(customShader.id.toUInt, colorName)

          if projLocation >= 0 then
            val matrix = Matrix4.ortho(0.0f, Window.width.toFloat, Window.height.toFloat, 0.0f, -1.0f, 1.0f)
            val matrixPtr = stackalloc[GLfloat](16)
            for i <- matrix.indices do
              matrixPtr(i) = matrix(i)
            GLEWHelper.glUniformMatrix4fv(projLocation, 1.toUInt, GL_FALSE, matrixPtr)

          if colorLocation >= 0 then
            GLEWHelper.glUniform4f(colorLocation, color.rNorm, color.gNorm, color.bNorm, color.aNorm)
        }
      case None =>
        defaultShader.foreach { shader =>
          GLEWHelper.glUseProgram(shader.id.toUInt)
          setColor(color)
        }

    val startRad = math.toRadians(startAngle).toFloat
    val endRad = math.toRadians(endAngle).toFloat
    val angleStep = (endRad - startRad) / segments.toFloat
    var n = 0
    var i = 0
    while i < segments do
      val angle1 = startRad + (i * angleStep)
      val angle2 = startRad + ((i + 1) * angleStep)
      scratchBuffer(n) = center.x + radius * math.cos(angle1).toFloat; n += 1
      scratchBuffer(n) = center.y + radius * math.sin(angle1).toFloat; n += 1
      scratchBuffer(n) = center.x + radius * math.cos(angle2).toFloat; n += 1
      scratchBuffer(n) = center.y + radius * math.sin(angle2).toFloat; n += 1
      i += 1

    scratchBuffer(n) = center.x; n += 1; scratchBuffer(n) = center.y; n += 1
    scratchBuffer(n) = center.x + radius * math.cos(startRad).toFloat; n += 1
    scratchBuffer(n) = center.y + radius * math.sin(startRad).toFloat; n += 1
    scratchBuffer(n) = center.x; n += 1; scratchBuffer(n) = center.y; n += 1
    scratchBuffer(n) = center.x + radius * math.cos(endRad).toFloat; n += 1
    scratchBuffer(n) = center.y + radius * math.sin(endRad).toFloat; n += 1

    if n > 0 then
      GLEWHelper.glBindVertexArray(VAO)
      GLEWHelper.glBindBuffer(GL_ARRAY_BUFFER.toUInt, VBO)
      GLEWHelper.glBufferData(GL_ARRAY_BUFFER.toUInt, (n * sizeof[GLfloat].toInt), scratchBuffer.asInstanceOf[Ptr[Byte]], GL_DYNAMIC_DRAW.toUInt)
      GLEWHelper.glVertexAttribPointer(0.toUInt, 2, GL_FLOAT.toUInt, GL_FALSE, (2 * sizeof[GLfloat].toInt).toUInt, null)
      GLEWHelper.glEnableVertexAttribArray(0.toUInt)
      glDrawArrays(GL_LINES.toUInt, 0, (n / 2).toUInt)
      GLEWHelper.glBindVertexArray(0.toUInt)

  def renderCircleOutline(centerX: Float, centerY: Float, radius: Float, color: Color): Unit =
    if !isInitialized then
      if !initialize() then return

    Drawing.getCurrentShader match
      case Some(customShader) =>
        GLEWHelper.glUseProgram(customShader.id.toUInt)

        Zone {
          val projName = toCString("uProjection")
          val colorName = toCString("uColor")

          val projLocation = GLEWHelper.glGetUniformLocation(customShader.id.toUInt, projName)
          val colorLocation = GLEWHelper.glGetUniformLocation(customShader.id.toUInt, colorName)

          if projLocation >= 0 then
            val matrix = Matrix4.ortho(0.0f, Window.width.toFloat, Window.height.toFloat, 0.0f, -1.0f, 1.0f)
            val matrixPtr = stackalloc[GLfloat](16)
            for i <- matrix.indices do
              matrixPtr(i) = matrix(i)
            GLEWHelper.glUniformMatrix4fv(projLocation, 1.toUInt, GL_FALSE, matrixPtr)

          if colorLocation >= 0 then
            GLEWHelper.glUniform4f(colorLocation, color.rNorm, color.gNorm, color.bNorm, color.aNorm)
        }
      case None =>
        defaultShader.foreach { shader =>
          GLEWHelper.glUseProgram(shader.id.toUInt)
          setColor(color)
        }

    val segments = 36
    var n = 0
    var i = 0
    while i < segments do
      val angle= (i * 2.0f * math.Pi / segments).toFloat
      val angleN = ((i + 1) % segments * 2.0f * math.Pi / segments).toFloat
      scratchBuffer(n) = centerX + radius * math.cos(angle).toFloat;  n += 1
      scratchBuffer(n) = centerY + radius * math.sin(angle).toFloat;  n += 1
      scratchBuffer(n) = centerX + radius * math.cos(angleN).toFloat; n += 1
      scratchBuffer(n) = centerY + radius * math.sin(angleN).toFloat; n += 1
      i += 1

    if n > 0 then
      GLEWHelper.glBindVertexArray(VAO)
      GLEWHelper.glBindBuffer(GL_ARRAY_BUFFER.toUInt, VBO)
      GLEWHelper.glBufferData(GL_ARRAY_BUFFER.toUInt, (n * sizeof[GLfloat].toInt), scratchBuffer.asInstanceOf[Ptr[Byte]], GL_DYNAMIC_DRAW.toUInt)
      GLEWHelper.glVertexAttribPointer(0.toUInt, 2, GL_FLOAT.toUInt, GL_FALSE, (2 * sizeof[GLfloat].toInt).toUInt, null)
      GLEWHelper.glEnableVertexAttribArray(0.toUInt)
      glDrawArrays(GL_LINES.toUInt, 0, (n / 2).toUInt)
      GLEWHelper.glBindVertexArray(0.toUInt)

end BasicRenderer