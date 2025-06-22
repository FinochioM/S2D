package s2d.shapes

import s2d.types.{Color, Vector2, Rectangle}
import s2d.math.Matrix4
import s2d.core.Window
import s2d.gl.GL.*
import s2d.gl.GLExtras.*
import s2d.gl.GLEWHelper
import s2d.core.Shaders
import scalanative.unsafe.*
import scalanative.unsigned.*

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
      Zone {
        val matrixPtr = stackalloc[GLfloat](16)
        for i <- matrix.indices do
          matrixPtr(i) = matrix(i)
        GLEWHelper.glUniformMatrix4fv(projectionLocation, 1.toUInt, GL_FALSE, matrixPtr)
      }
    }

  def updateProjectionFromDrawing(): Unit =
    val matrix = Matrix4.ortho(0.0f, Window.windowWidth.toFloat, Window.windowHeight.toFloat, 0.0f, -1.0f, 1.0f)
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

    defaultShader.foreach { shader =>
      GLEWHelper.glUseProgram(shader.id.toUInt)
      setColor(color)

      val vertices = Array(
        startPos.x, startPos.y,
        endPos.x, endPos.y
      )

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

      glDrawArrays(GL_LINES.toUInt, 0, 2.toUInt)

      GLEWHelper.glBindVertexArray(0.toUInt)
    }

  def renderThickLine(startPos: Vector2, endPos: Vector2, thick: Float, color: Color): Unit =
    if !isInitialized then
      if !initialize() then return

    defaultShader.foreach { shader =>
      GLEWHelper.glUseProgram(shader.id.toUInt)
      setColor(color)

      val dx = endPos.x - startPos.x
      val dy = endPos.y - startPos.y
      val length = math.sqrt(dx * dx + dy * dy).toFloat

      if length == 0.0f then return
      val perpX = -dy / length * (thick / 2.0f)
      val perpY = dx / length * (thick / 2.0f)

      val vertices = Array(
        startPos.x + perpX, startPos.y + perpY, // top-left
        startPos.x - perpX, startPos.y - perpY, // bottom-left
        endPos.x - perpX, endPos.y - perpY, // bottom-right

        startPos.x + perpX, startPos.y + perpY, // top-left
        endPos.x - perpX, endPos.y - perpY, // bottom-right
        endPos.x + perpX, endPos.y + perpY // top-right
      )

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

      glDrawArrays(GL_TRIANGLES.toUInt, 0, 6.toUInt)

      GLEWHelper.glBindVertexArray(0.toUInt)
    }

  def renderRectangle(x: Float, y: Float, width: Float, height: Float, color: Color): Unit =
    if !isInitialized then
      if !initialize() then return

    defaultShader.foreach { shader =>
      GLEWHelper.glUseProgram(shader.id.toUInt)
      setColor(color)

      val vertices = Array(
        x, y, // top-left
        x + width, y, // top-right
        x, y + height, // bottom-left

        x + width, y, // top-right
        x + width, y + height, // bottom-right
        x, y + height // bottom-left
      )

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

      glDrawArrays(GL_TRIANGLES.toUInt, 0, 6.toUInt)

      GLEWHelper.glBindVertexArray(0.toUInt)
    }

  def renderRotatedRectangle(rectangle: Rectangle, origin: Vector2, rotation: Float, color: Color): Unit =
    if !isInitialized then
      if !initialize() then return

    defaultShader.foreach { shader =>
      GLEWHelper.glUseProgram(shader.id.toUInt)
      setColor(color)

      val angleRad = math.toRadians(rotation).toFloat
      val cos = math.cos(angleRad).toFloat
      val sin = math.sin(angleRad).toFloat

      val corners = Array(
        (-origin.x, -origin.y), // top-left
        (rectangle.width - origin.x, -origin.y), // top-right
        (rectangle.width - origin.x, rectangle.height - origin.y), // bottom-right
        (-origin.x, rectangle.height - origin.y) // bottom-left
      )

      val rotatedCorners = corners.map { case (x, y) =>
        val rotX = x * cos - y * sin + rectangle.x + origin.x
        val rotY = x * sin + y * cos + rectangle.y + origin.y
        (rotX, rotY)
      }

      val vertices = Array(
        rotatedCorners(0)._1, rotatedCorners(0)._2, // top-left
        rotatedCorners(1)._1, rotatedCorners(1)._2, // top-right
        rotatedCorners(2)._1, rotatedCorners(2)._2, // bottom-right

        rotatedCorners(0)._1, rotatedCorners(0)._2, // top-left
        rotatedCorners(2)._1, rotatedCorners(2)._2, // bottom-right
        rotatedCorners(3)._1, rotatedCorners(3)._2 // bottom-left
      )

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

      glDrawArrays(GL_TRIANGLES.toUInt, 0, 6.toUInt)

      GLEWHelper.glBindVertexArray(0.toUInt)
    }

  def renderRectangleOutline(x: Float, y: Float, width: Float, height: Float, color: Color): Unit =
    if !isInitialized then
      if !initialize() then return

    defaultShader.foreach { shader =>
      GLEWHelper.glUseProgram(shader.id.toUInt)
      setColor(color)

      val vertices = Array(
        // Top edge
        x, y,
        x + width, y,

        // Right edge
        x + width, y,
        x + width, y + height,

        // Bottom edge
        x + width, y + height,
        x, y + height,

        // Left edge
        x, y + height,
        x, y
      )

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

      glDrawArrays(GL_LINES.toUInt, 0, 8.toUInt)

      GLEWHelper.glBindVertexArray(0.toUInt)
    }

  def renderRoundedRectangle(rect: Rectangle, roundness: Float, segments: Int, color: Color): Unit =
    if !isInitialized then
      if !initialize() then return

    if segments < 3 then return
    if roundness <= 0.0f then
      renderRectangle(rect.x, rect.y, rect.width, rect.height, color)
      return

    defaultShader.foreach { shader =>
      GLEWHelper.glUseProgram(shader.id.toUInt)
      setColor(color)

      val maxRadius = math.min(rect.width, rect.height) / 2.0f
      val cornerRadius = (roundness * maxRadius).min(maxRadius)
      val innerWidth = rect.width - 2 * cornerRadius
      val innerHeight = rect.height - 2 * cornerRadius

      val vertices = scala.collection.mutable.ArrayBuffer[Float]()

      def addQuad(x1: Float, y1: Float, x2: Float, y2: Float, x3: Float, y3: Float, x4: Float, y4: Float): Unit =
        vertices += x1;
        vertices += y1
        vertices += x2;
        vertices += y2
        vertices += x3;
        vertices += y3
        vertices += x1;
        vertices += y1
        vertices += x3;
        vertices += y3
        vertices += x4;
        vertices += y4

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
        for i <- 0 until segments do
          val angle1 = startAngle + (i * angleStep)
          val angle2 = startAngle + ((i + 1) * angleStep)

          val x1 = centerX + cornerRadius * math.cos(angle1).toFloat
          val y1 = centerY + cornerRadius * math.sin(angle1).toFloat
          val x2 = centerX + cornerRadius * math.cos(angle2).toFloat
          val y2 = centerY + cornerRadius * math.sin(angle2).toFloat

          vertices += centerX; vertices += centerY
          vertices += x1; vertices += y1
          vertices += x2; vertices += y2

      addCorner(rect.x + cornerRadius, rect.y + cornerRadius, math.Pi.toFloat) // top-left
      addCorner(rect.x + rect.width - cornerRadius, rect.y + cornerRadius, 3 * math.Pi.toFloat / 2) // top-right
      addCorner(rect.x + rect.width - cornerRadius, rect.y + rect.height - cornerRadius, 0.0f) // bottom-right
      addCorner(rect.x + cornerRadius, rect.y + rect.height - cornerRadius, math.Pi.toFloat / 2) // bottom-left

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

        glDrawArrays(GL_TRIANGLES.toUInt, 0, (vertices.length / 2).toUInt)

        GLEWHelper.glBindVertexArray(0.toUInt)
    }

  def renderRoundedRectangleOutline(rectangle: Rectangle, roundness: Float, segments: Int, color: Color): Unit =
    if !isInitialized then
      if !initialize() then return

    if segments < 3 then return
    if roundness <= 0.0f then
      renderRectangleOutline(rectangle.x, rectangle.y, rectangle.width, rectangle.height, color)
      return

    defaultShader.foreach { shader =>
      GLEWHelper.glUseProgram(shader.id.toUInt)
      setColor(color)

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
    }

  def renderTriangle(v1: Vector2, v2: Vector2, v3: Vector2, color: Color): Unit =
    if !isInitialized then
      if !initialize() then return

    defaultShader.foreach { shader =>
      GLEWHelper.glUseProgram(shader.id.toUInt)
      setColor(color)

      val vertices = Array(
        v1.x, v1.y,
        v2.x, v2.y,
        v3.x, v3.y
      )

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

      glDrawArrays(GL_TRIANGLES.toUInt, 0, 3.toUInt)

      GLEWHelper.glBindVertexArray(0.toUInt)
    }

  def renderTriangleOutline(v1: Vector2, v2: Vector2, v3: Vector2, color: Color): Unit =
    if !isInitialized then
      if !initialize() then return

    defaultShader.foreach { shader =>
      GLEWHelper.glUseProgram(shader.id.toUInt)
      setColor(color)

      val vertices = Array(
        v1.x, v1.y,
        v2.x, v2.y,

        v2.x, v2.y,
        v3.x, v3.y,

        v3.x, v3.y,
        v1.x, v1.y
      )

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

      glDrawArrays(GL_LINES.toUInt, 0, 6.toUInt)

      GLEWHelper.glBindVertexArray(0.toUInt)
    }

  def renderTriangleFan(points: Array[Vector2], color: Color): Unit =
    if !isInitialized then
      if !initialize() then return

    if points.length < 3 then return

    defaultShader.foreach { shader =>
      GLEWHelper.glUseProgram(shader.id.toUInt)
      setColor(color)

      val vertices = scala.collection.mutable.ArrayBuffer[Float]()

      val centerPoint = points(0)

      for i <- 1 until points.length - 1 do
        val currentPoint = points(i)
        val nextPoint = points(i + 1)

        vertices += centerPoint.x; vertices += centerPoint.y
        vertices += currentPoint.x; vertices += currentPoint.y
        vertices += nextPoint.x; vertices += nextPoint.y

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

        glDrawArrays(GL_TRIANGLES.toUInt, 0, (vertices.length / 2).toUInt)

        GLEWHelper.glBindVertexArray(0.toUInt)
    }

  def renderTriangleStrip(points: Array[Vector2], color: Color): Unit =
    if !isInitialized then
      if !initialize() then return

    if points.length < 3 then return

    defaultShader.foreach { shader =>
      GLEWHelper.glUseProgram(shader.id.toUInt)
      setColor(color)

      // note: every odd triangle has flipped winding order to maintain face orientation
      val vertices = scala.collection.mutable.ArrayBuffer[Float]()

      for i <- 0 until points.length - 2 do
        if i % 2 == 0 then
          vertices += points(i).x;
          vertices += points(i).y
          vertices += points(i + 1).x;
          vertices += points(i + 1).y
          vertices += points(i + 2).x;
          vertices += points(i + 2).y
        else
          vertices += points(i).x;
          vertices += points(i).y
          vertices += points(i + 2).x;
          vertices += points(i + 2).y
          vertices += points(i + 1).x;
          vertices += points(i + 1).y

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

        glDrawArrays(GL_TRIANGLES.toUInt, 0, (vertices.length / 2).toUInt)

        GLEWHelper.glBindVertexArray(0.toUInt)
    }

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

    defaultShader.foreach { shader =>
      GLEWHelper.glUseProgram(shader.id.toUInt)
      setColor(color)

      val rotationRad = math.toRadians(rotation).toFloat
      val angleStep = (2.0f * math.Pi / sides.toFloat).toFloat

      val vertices = scala.collection.mutable.ArrayBuffer[Float]()

      val points = Array.ofDim[Vector2](sides)
      for i <- 0 until sides do
        val angle = (i * angleStep) + rotationRad
        val x = center.x + radius * math.cos(angle).toFloat
        val y = center.y + radius * math.sin(angle).toFloat
        points(i) = Vector2(x, y)

      for i <- 0 until sides do
        val current = points(i)
        val next = points((i + 1) % sides) // wrap around to first point

        vertices += current.x; vertices += current.y
        vertices += next.x; vertices += next.y

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
    }
end BasicRenderer