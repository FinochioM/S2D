package s2d.backend.gl

import scalanative.unsafe.*
import scalanative.unsigned.*

object GLExtras:
  import GL.*

  /*
   * Constants
   */

  /* Boolean values */
  val GL_FALSE: UByte = 0.toUByte
  val GL_TRUE: UByte = 1.toUByte

  /* Data types */
  val GL_BYTE: UShort = 0x1400.toUShort
  val GL_UNSIGNED_BYTE: UShort = 0x1401.toUShort
  val GL_SHORT: UShort = 0x1402.toUShort
  val GL_UNSIGNED_SHORT: UShort = 0x1403.toUShort
  val GL_INT: UShort = 0x1404.toUShort
  val GL_UNSIGNED_INT: UShort = 0x1405.toUShort
  val GL_FLOAT: UShort = 0x1406.toUShort
  val GL_2_BYTES: UShort = 0x1407.toUShort
  val GL_3_BYTES: UShort = 0x1408.toUShort
  val GL_4_BYTES: UShort = 0x1409.toUShort
  val GL_DOUBLE: UShort = 0x140a.toUShort

  /* Primitives */
  val GL_POINTS: UShort = 0x0000.toUShort
  val GL_LINES: UShort = 0x0001.toUShort
  val GL_LINE_LOOP: UShort = 0x0002.toUShort
  val GL_LINE_STRIP: UShort = 0x0003.toUShort
  val GL_TRIANGLES: UShort = 0x0004.toUShort
  val GL_TRIANGLE_STRIP: UShort = 0x0005.toUShort
  val GL_TRIANGLE_FAN: UShort = 0x0006.toUShort
  val GL_QUADS: UShort = 0x0007.toUShort
  val GL_QUAD_STRIP: UShort = 0x0008.toUShort
  val GL_POLYGON: UShort = 0x0009.toUShort

  /* Vertex Arrays */
  val GL_VERTEX_ARRAY: UShort = 0x8074.toUShort
  val GL_NORMAL_ARRAY: UShort = 0x8075.toUShort
  val GL_COLOR_ARRAY: UShort = 0x8076.toUShort
  val GL_INDEX_ARRAY: UShort = 0x8077.toUShort
  val GL_TEXTURE_COORD_ARRAY: UShort = 0x8078.toUShort
  val GL_EDGE_FLAG_ARRAY: UShort = 0x8079.toUShort
  val GL_VERTEX_ARRAY_SIZE: UShort = 0x807a.toUShort
  val GL_VERTEX_ARRAY_TYPE: UShort = 0x807b.toUShort
  val GL_VERTEX_ARRAY_STRIDE: UShort = 0x807c.toUShort
  val GL_NORMAL_ARRAY_TYPE: UShort = 0x807e.toUShort
  val GL_NORMAL_ARRAY_STRIDE: UShort = 0x807f.toUShort
  val GL_COLOR_ARRAY_SIZE: UShort = 0x8081.toUShort
  val GL_COLOR_ARRAY_TYPE: UShort = 0x8082.toUShort
  val GL_COLOR_ARRAY_STRIDE: UShort = 0x8083.toUShort
  val GL_INDEX_ARRAY_TYPE: UShort = 0x8085.toUShort
  val GL_INDEX_ARRAY_STRIDE: UShort = 0x8086.toUShort
  val GL_TEXTURE_COORD_ARRAY_SIZE: UShort = 0x8088.toUShort
  val GL_TEXTURE_COORD_ARRAY_TYPE: UShort = 0x8089.toUShort
  val GL_TEXTURE_COORD_ARRAY_STRIDE: UShort = 0x808a.toUShort
  val GL_EDGE_FLAG_ARRAY_STRIDE: UShort = 0x808c.toUShort
  val GL_VERTEX_ARRAY_POINTER: UShort = 0x808e.toUShort
  val GL_NORMAL_ARRAY_POINTER: UShort = 0x808f.toUShort
  val GL_COLOR_ARRAY_POINTER: UShort = 0x8090.toUShort
  val GL_INDEX_ARRAY_POINTER: UShort = 0x8091.toUShort
  val GL_TEXTURE_COORD_ARRAY_POINTER: UShort = 0x8092.toUShort
  val GL_EDGE_FLAG_ARRAY_POINTER: UShort = 0x8093.toUShort
  val GL_V2F: UShort = 0x2a20.toUShort
  val GL_V3F: UShort = 0x2a21.toUShort
  val GL_C4UB_V2F: UShort = 0x2a22.toUShort
  val GL_C4UB_V3F: UShort = 0x2a23.toUShort
  val GL_C3F_V3F: UShort = 0x2a24.toUShort
  val GL_N3F_V3F: UShort = 0x2a25.toUShort
  val GL_C4F_N3F_V3F: UShort = 0x2a26.toUShort
  val GL_T2F_V3F: UShort = 0x2a27.toUShort
  val GL_T4F_V4F: UShort = 0x2a28.toUShort
  val GL_T2F_C4UB_V3F: UShort = 0x2a29.toUShort
  val GL_T2F_C3F_V3F: UShort = 0x2a2a.toUShort
  val GL_T2F_N3F_V3F: UShort = 0x2a2b.toUShort
  val GL_T2F_C4F_N3F_V3F: UShort = 0x2a2c.toUShort
  val GL_T4F_C4F_N3F_V4F: UShort = 0x2a2d.toUShort

  /* Matrix Mode */
  val GL_MATRIX_MODE: GLenum = 0x0ba0.toUInt
  val GL_MODELVIEW: GLenum = 0x1700.toUInt
  val GL_PROJECTION: GLenum = 0x1701.toUInt
  val GL_TEXTURE: GLenum = 0x1702.toUInt

  /* Points */
  val GL_POINT_SMOOTH: UShort = 0x0b10.toUShort
  val GL_POINT_SIZE: UShort = 0x0b11.toUShort
  val GL_POINT_SIZE_GRANULARITY: UShort = 0x0b13.toUShort
  val GL_POINT_SIZE_RANGE: UShort = 0x0b12.toUShort

  /* Lines */
  val GL_LINE_SMOOTH: UShort = 0x0b20.toUShort
  val GL_LINE_STIPPLE: UShort = 0x0b24.toUShort
  val GL_LINE_STIPPLE_PATTERN: UShort = 0x0b25.toUShort
  val GL_LINE_STIPPLE_REPEAT: UShort = 0x0b26.toUShort
  val GL_LINE_WIDTH: UShort = 0x0b21.toUShort
  val GL_LINE_WIDTH_GRANULARITY: UShort = 0x0b23.toUShort
  val GL_LINE_WIDTH_RANGE: UShort = 0x0b22.toUShort

  /* Polygons */
  val GL_POINT: UShort = 0x1b00.toUShort
  val GL_LINE: UShort = 0x1b01.toUShort
  val GL_FILL: UShort = 0x1b02.toUShort
  val GL_CW: UShort = 0x0900.toUShort
  val GL_CCW: UShort = 0x0901.toUShort
  val GL_FRONT: UShort = 0x0404.toUShort
  val GL_BACK: UShort = 0x0405.toUShort
  val GL_POLYGON_MODE: UShort = 0x0b40.toUShort
  val GL_POLYGON_SMOOTH: UShort = 0x0b41.toUShort
  val GL_POLYGON_STIPPLE: UShort = 0x0b42.toUShort
  val GL_EDGE_FLAG: UShort = 0x0b43.toUShort
  val GL_CULL_FACE: UShort = 0x0b44.toUShort
  val GL_CULL_FACE_MODE: UShort = 0x0b45.toUShort
  val GL_FRONT_FACE: UShort = 0x0b46.toUShort
  val GL_POLYGON_OFFSET_FACTOR: UShort = 0x8038.toUShort
  val GL_POLYGON_OFFSET_UNITS: UShort = 0x2a00.toUShort
  val GL_POLYGON_OFFSET_POINT: UShort = 0x2a01.toUShort
  val GL_POLYGON_OFFSET_LINE: UShort = 0x2a02.toUShort
  val GL_POLYGON_OFFSET_FILL: UShort = 0x8037.toUShort

  /* Display Lists */
  val GL_COMPILE: UShort = 0x1300.toUShort
  val GL_COMPILE_AND_EXECUTE: UShort = 0x1301.toUShort
  val GL_LIST_BASE: UShort = 0x0b32.toUShort
  val GL_LIST_INDEX: UShort = 0x0b33.toUShort
  val GL_LIST_MODE: UShort = 0x0b30.toUShort

  /* Depth buffer */
  val GL_NEVER: UShort = 0x0200.toUShort
  val GL_LESS: UShort = 0x0201.toUShort
  val GL_EQUAL: UShort = 0x0202.toUShort
  val GL_LEQUAL: UShort = 0x0203.toUShort
  val GL_GREATER: UShort = 0x0204.toUShort
  val GL_NOTEQUAL: UShort = 0x0205.toUShort
  val GL_GEQUAL: UShort = 0x0206.toUShort
  val GL_ALWAYS: UShort = 0x0207.toUShort
  val GL_DEPTH_TEST: UShort = 0x0b71.toUShort
  val GL_DEPTH_BITS: UShort = 0x0d56.toUShort
  val GL_DEPTH_CLEAR_VALUE: UShort = 0x0b73.toUShort
  val GL_DEPTH_FUNC: UShort = 0x0b74.toUShort
  val GL_DEPTH_RANGE: UShort = 0x0b70.toUShort
  val GL_DEPTH_WRITEMASK: UShort = 0x0b72.toUShort
  val GL_DEPTH_COMPONENT: UShort = 0x1902.toUShort

  /* Lighting */
  val GL_LIGHTING: UShort = 0x0b50.toUShort
  val GL_LIGHT0: UShort = 0x4000.toUShort
  val GL_LIGHT1: UShort = 0x4001.toUShort
  val GL_LIGHT2: UShort = 0x4002.toUShort
  val GL_LIGHT3: UShort = 0x4003.toUShort
  val GL_LIGHT4: UShort = 0x4004.toUShort
  val GL_LIGHT5: UShort = 0x4005.toUShort
  val GL_LIGHT6: UShort = 0x4006.toUShort
  val GL_LIGHT7: UShort = 0x4007.toUShort
  val GL_SPOT_EXPONENT: UShort = 0x1205.toUShort
  val GL_SPOT_CUTOFF: UShort = 0x1206.toUShort
  val GL_CONSTANT_ATTENUATION: UShort = 0x1207.toUShort
  val GL_LINEAR_ATTENUATION: UShort = 0x1208.toUShort
  val GL_QUADRATIC_ATTENUATION: UShort = 0x1209.toUShort
  val GL_AMBIENT: UShort = 0x1200.toUShort
  val GL_DIFFUSE: UShort = 0x1201.toUShort
  val GL_SPECULAR: UShort = 0x1202.toUShort
  val GL_SHININESS: UShort = 0x1601.toUShort
  val GL_EMISSION: UShort = 0x1600.toUShort
  val GL_POSITION: UShort = 0x1203.toUShort
  val GL_SPOT_DIRECTION: UShort = 0x1204.toUShort
  val GL_AMBIENT_AND_DIFFUSE: UShort = 0x1602.toUShort
  val GL_COLOR_INDEXES: UShort = 0x1603.toUShort
  val GL_LIGHT_MODEL_TWO_SIDE: UShort = 0x0b52.toUShort
  val GL_LIGHT_MODEL_LOCAL_VIEWER: UShort = 0x0b51.toUShort
  val GL_LIGHT_MODEL_AMBIENT: UShort = 0x0b53.toUShort
  val GL_FRONT_AND_BACK: UShort = 0x0408.toUShort
  val GL_SHADE_MODEL: UShort = 0x0b54.toUShort
  val GL_FLAT: UShort = 0x1d00.toUShort
  val GL_SMOOTH: UShort = 0x1d01.toUShort
  val GL_COLOR_MATERIAL: UShort = 0x0b57.toUShort
  val GL_COLOR_MATERIAL_FACE: UShort = 0x0b55.toUShort
  val GL_COLOR_MATERIAL_PARAMETER: UShort = 0x0b56.toUShort
  val GL_NORMALIZE: UShort = 0x0ba1.toUShort

  /* User clipping planes */
  val GL_CLIP_PLANE0: UShort = 0x3000.toUShort
  val GL_CLIP_PLANE1: UShort = 0x3001.toUShort
  val GL_CLIP_PLANE2: UShort = 0x3002.toUShort
  val GL_CLIP_PLANE3: UShort = 0x3003.toUShort
  val GL_CLIP_PLANE4: UShort = 0x3004.toUShort
  val GL_CLIP_PLANE5: UShort = 0x3005.toUShort

  /* Accumulation buffer */
  val GL_ACCUM_RED_BITS: UShort = 0x0d58.toUShort
  val GL_ACCUM_GREEN_BITS: UShort = 0x0d59.toUShort
  val GL_ACCUM_BLUE_BITS: UShort = 0x0d5a.toUShort
  val GL_ACCUM_ALPHA_BITS: UShort = 0x0d5b.toUShort
  val GL_ACCUM_CLEAR_VALUE: UShort = 0x0b80.toUShort
  val GL_ACCUM: UShort = 0x0100.toUShort
  val GL_ADD: UShort = 0x0104.toUShort
  val GL_LOAD: UShort = 0x0101.toUShort
  val GL_MULT: UShort = 0x0103.toUShort
  val GL_RETURN: UShort = 0x0102.toUShort

  /* Alpha testing */
  val GL_ALPHA_TEST: UShort = 0x0bc0.toUShort
  val GL_ALPHA_TEST_REF: UShort = 0x0bc2.toUShort
  val GL_ALPHA_TEST_FUNC: UShort = 0x0bc1.toUShort

  /* Blending */
  val GL_BLEND: UShort = 0x0be2.toUShort
  val GL_BLEND_SRC: UShort = 0x0be1.toUShort
  val GL_BLEND_DST: UShort = 0x0be0.toUShort
  val GL_ZERO: UShort = 0.toUShort
  val GL_ONE: UShort = 1.toUShort
  val GL_SRC_COLOR: UShort = 0x0300.toUShort
  val GL_ONE_MINUS_SRC_COLOR: UShort = 0x0301.toUShort
  val GL_SRC_ALPHA: UShort = 0x0302.toUShort
  val GL_ONE_MINUS_SRC_ALPHA: UShort = 0x0303.toUShort
  val GL_DST_ALPHA: UShort = 0x0304.toUShort
  val GL_ONE_MINUS_DST_ALPHA: UShort = 0x0305.toUShort
  val GL_DST_COLOR: UShort = 0x0306.toUShort
  val GL_ONE_MINUS_DST_COLOR: UShort = 0x0307.toUShort
  val GL_SRC_ALPHA_SATURATE: UShort = 0x0308.toUShort

  /* Render Mode */
  val GL_FEEDBACK: UShort = 0x1c01.toUShort
  val GL_RENDER: UShort = 0x1c00.toUShort
  val GL_SELECT: UShort = 0x1c02.toUShort

  /* Feedback */
  val GL_2D: UShort = 0x0600.toUShort
  val GL_3D: UShort = 0x0601.toUShort
  val GL_3D_COLOR: UShort = 0x0602.toUShort
  val GL_3D_COLOR_TEXTURE: UShort = 0x0603.toUShort
  val GL_4D_COLOR_TEXTURE: UShort = 0x0604.toUShort
  val GL_POINT_TOKEN: UShort = 0x0701.toUShort
  val GL_LINE_TOKEN: UShort = 0x0702.toUShort
  val GL_LINE_RESET_TOKEN: UShort = 0x0707.toUShort
  val GL_POLYGON_TOKEN: UShort = 0x0703.toUShort
  val GL_BITMAP_TOKEN: UShort = 0x0704.toUShort
  val GL_DRAW_PIXEL_TOKEN: UShort = 0x0705.toUShort
  val GL_COPY_PIXEL_TOKEN: UShort = 0x0706.toUShort
  val GL_PASS_THROUGH_TOKEN: UShort = 0x0700.toUShort
  val GL_FEEDBACK_BUFFER_POINTER: UShort = 0x0df0.toUShort
  val GL_FEEDBACK_BUFFER_SIZE: UShort = 0x0df1.toUShort
  val GL_FEEDBACK_BUFFER_TYPE: UShort = 0x0df2.toUShort

  /* Selection */
  val GL_SELECTION_BUFFER_POINTER: UShort = 0x0df3.toUShort
  val GL_SELECTION_BUFFER_SIZE: UShort = 0x0df4.toUShort

  /* Fog */
  val GL_FOG: Short = 0x0b60.toShort
  val GL_FOG_MODE: Short = 0x0b65.toShort
  val GL_FOG_DENSITY: Short = 0x0b62.toShort
  val GL_FOG_COLOR: Short = 0x0b66.toShort
  val GL_FOG_INDEX: Short = 0x0b61.toShort
  val GL_FOG_START: Short = 0x0b63.toShort
  val GL_FOG_END: Short = 0x0b64.toShort
  val GL_LINEAR: Short = 0x2601.toShort
  val GL_EXP: Short = 0x0800.toShort
  val GL_EXP2: Short = 0x0801.toShort

  /* Logic Ops */
  val GL_LOGIC_OP: UShort = 0x0bf1.toUShort
  val GL_INDEX_LOGIC_OP: UShort = 0x0bf1.toUShort
  val GL_COLOR_LOGIC_OP: UShort = 0x0bf2.toUShort
  val GL_LOGIC_OP_MODE: UShort = 0x0bf0.toUShort
  val GL_CLEAR: UShort = 0x1500.toUShort
  val GL_SET: UShort = 0x150f.toUShort
  val GL_COPY: UShort = 0x1503.toUShort
  val GL_COPY_INVERTED: UShort = 0x150c.toUShort
  val GL_NOOP: UShort = 0x1505.toUShort
  val GL_INVERT: UShort = 0x150a.toUShort
  val GL_AND: UShort = 0x1501.toUShort
  val GL_NAND: UShort = 0x150e.toUShort
  val GL_OR: UShort = 0x1507.toUShort
  val GL_NOR: UShort = 0x1508.toUShort
  val GL_XOR: UShort = 0x1506.toUShort
  val GL_EQUIV: UShort = 0x1509.toUShort
  val GL_AND_REVERSE: UShort = 0x1502.toUShort
  val GL_AND_INVERTED: UShort = 0x1504.toUShort
  val GL_OR_REVERSE: UShort = 0x150b.toUShort
  val GL_OR_INVERTED: UShort = 0x150d.toUShort

  /* Stencil */
  val GL_STENCIL_BITS: UShort = 0x0d57.toUShort
  val GL_STENCIL_TEST: UShort = 0x0b90.toUShort
  val GL_STENCIL_CLEAR_VALUE: UShort = 0x0b91.toUShort
  val GL_STENCIL_FUNC: UShort = 0x0b92.toUShort
  val GL_STENCIL_VALUE_MASK: UShort = 0x0b93.toUShort
  val GL_STENCIL_FAIL: UShort = 0x0b94.toUShort
  val GL_STENCIL_PASS_DEPTH_FAIL: UShort = 0x0b95.toUShort
  val GL_STENCIL_PASS_DEPTH_PASS: UShort = 0x0b96.toUShort
  val GL_STENCIL_REF: UShort = 0x0b97.toUShort
  val GL_STENCIL_WRITEMASK: UShort = 0x0b98.toUShort
  val GL_STENCIL_INDEX: UShort = 0x1901.toUShort
  val GL_KEEP: UShort = 0x1e00.toUShort
  val GL_REPLACE: UShort = 0x1e01.toUShort
  val GL_INCR: UShort = 0x1e02.toUShort
  val GL_DECR: UShort = 0x1e03.toUShort

  /* Buffers, Pixel Drawing/Reading */
  val GL_NONE: UShort = 0.toUShort
  val GL_LEFT: UShort = 0x0406.toUShort
  val GL_RIGHT: UShort = 0x0407.toUShort
  /*val GL_FRONT: UShort = 0x0404.toUShort*/
  /*val GL_BACK: UShort = 0x0405.toUShort*/
  /*val GL_FRONT_AND_BACK: UShort = 0x0408.toUShort*/
  val GL_FRONT_LEFT: UShort = 0x0400.toUShort
  val GL_FRONT_RIGHT: UShort = 0x0401.toUShort
  val GL_BACK_LEFT: UShort = 0x0402.toUShort
  val GL_BACK_RIGHT: UShort = 0x0403.toUShort
  val GL_AUX0: UShort = 0x0409.toUShort
  val GL_AUX1: UShort = 0x040a.toUShort
  val GL_AUX2: UShort = 0x040b.toUShort
  val GL_AUX3: UShort = 0x040c.toUShort
  val GL_COLOR_INDEX: UShort = 0x1900.toUShort
  val GL_RED: UShort = 0x1903.toUShort
  val GL_GREEN: UShort = 0x1904.toUShort
  val GL_BLUE: UShort = 0x1905.toUShort
  val GL_ALPHA: UShort = 0x1906.toUShort
  val GL_LUMINANCE: UShort = 0x1909.toUShort
  val GL_LUMINANCE_ALPHA: UShort = 0x190a.toUShort
  val GL_ALPHA_BITS: UShort = 0x0d55.toUShort
  val GL_RED_BITS: UShort = 0x0d52.toUShort
  val GL_GREEN_BITS: UShort = 0x0d53.toUShort
  val GL_BLUE_BITS: UShort = 0x0d54.toUShort
  val GL_INDEX_BITS: UShort = 0x0d51.toUShort
  val GL_SUBPIXEL_BITS: UShort = 0x0d50.toUShort
  val GL_AUX_BUFFERS: UShort = 0x0c00.toUShort
  val GL_READ_BUFFER: UShort = 0x0c02.toUShort
  val GL_DRAW_BUFFER: UShort = 0x0c01.toUShort
  val GL_DOUBLEBUFFER: UShort = 0x0c32.toUShort
  val GL_STEREO: UShort = 0x0c33.toUShort
  val GL_BITMAP: UShort = 0x1a00.toUShort
  val GL_COLOR: UShort = 0x1800.toUShort
  val GL_DEPTH: UShort = 0x1801.toUShort
  val GL_STENCIL: UShort = 0x1802.toUShort
  val GL_DITHER: UShort = 0x0bd0.toUShort
  val GL_RGB: UShort = 0x1907.toUShort
  val GL_RGBA: UShort = 0x1908.toUShort

  /* Implementation limits */
  val GL_MAX_LIST_NESTING: UShort = 0x0b31.toUShort
  val GL_MAX_EVAL_ORDER: UShort = 0x0d30.toUShort
  val GL_MAX_LIGHTS: UShort = 0x0d31.toUShort
  val GL_MAX_CLIP_PLANES: UShort = 0x0d32.toUShort
  val GL_MAX_TEXTURE_SIZE: UShort = 0x0d33.toUShort
  val GL_MAX_PIXEL_MAP_TABLE: UShort = 0x0d34.toUShort
  val GL_MAX_ATTRIB_STACK_DEPTH: UShort = 0x0d35.toUShort
  val GL_MAX_MODELVIEW_STACK_DEPTH: UShort = 0x0d36.toUShort
  val GL_MAX_NAME_STACK_DEPTH: UShort = 0x0d37.toUShort
  val GL_MAX_PROJECTION_STACK_DEPTH: UShort = 0x0d38.toUShort
  val GL_MAX_TEXTURE_STACK_DEPTH: UShort = 0x0d39.toUShort
  val GL_MAX_VIEWPORT_DIMS: UShort = 0x0d3a.toUShort
  val GL_MAX_CLIENT_ATTRIB_STACK_DEPTH: UShort = 0x0d3b.toUShort

  /* Gets */
  val GL_ATTRIB_STACK_DEPTH: UShort = 0x0bb0.toUShort
  val GL_CLIENT_ATTRIB_STACK_DEPTH: UShort = 0x0bb1.toUShort
  val GL_COLOR_CLEAR_VALUE: UShort = 0x0c22.toUShort
  val GL_COLOR_WRITEMASK: UShort = 0x0c23.toUShort
  val GL_CURRENT_INDEX: UShort = 0x0b01.toUShort
  val GL_CURRENT_COLOR: UShort = 0x0b00.toUShort
  val GL_CURRENT_NORMAL: UShort = 0x0b02.toUShort
  val GL_CURRENT_RASTER_COLOR: UShort = 0x0b04.toUShort
  val GL_CURRENT_RASTER_DISTANCE: UShort = 0x0b09.toUShort
  val GL_CURRENT_RASTER_INDEX: UShort = 0x0b05.toUShort
  val GL_CURRENT_RASTER_POSITION: UShort = 0x0b07.toUShort
  val GL_CURRENT_RASTER_TEXTURE_COORDS: UShort = 0x0b06.toUShort
  val GL_CURRENT_RASTER_POSITION_VALID: UShort = 0x0b08.toUShort
  val GL_CURRENT_TEXTURE_COORDS: UShort = 0x0b03.toUShort
  val GL_INDEX_CLEAR_VALUE: UShort = 0x0c20.toUShort
  val GL_INDEX_MODE: UShort = 0x0c30.toUShort
  val GL_INDEX_WRITEMASK: UShort = 0x0c21.toUShort
  val GL_MODELVIEW_MATRIX: UShort = 0x0ba6.toUShort
  val GL_MODELVIEW_STACK_DEPTH: UShort = 0x0ba3.toUShort
  val GL_NAME_STACK_DEPTH: UShort = 0x0d70.toUShort
  val GL_PROJECTION_MATRIX: UShort = 0x0ba7.toUShort
  val GL_PROJECTION_STACK_DEPTH: UShort = 0x0ba4.toUShort
  val GL_RENDER_MODE: UShort = 0x0c40.toUShort
  val GL_RGBA_MODE: UShort = 0x0c31.toUShort
  val GL_TEXTURE_MATRIX: UShort = 0x0ba8.toUShort
  val GL_TEXTURE_STACK_DEPTH: UShort = 0x0ba5.toUShort
  val GL_VIEWPORT: UShort = 0x0ba2.toUShort

  /* Evaluators */
  val GL_AUTO_NORMAL: UShort = 0x0d80.toUShort
  val GL_MAP1_COLOR_4: UShort = 0x0d90.toUShort
  val GL_MAP1_INDEX: UShort = 0x0d91.toUShort
  val GL_MAP1_NORMAL: UShort = 0x0d92.toUShort
  val GL_MAP1_TEXTURE_COORD_1: UShort = 0x0d93.toUShort
  val GL_MAP1_TEXTURE_COORD_2: UShort = 0x0d94.toUShort
  val GL_MAP1_TEXTURE_COORD_3: UShort = 0x0d95.toUShort
  val GL_MAP1_TEXTURE_COORD_4: UShort = 0x0d96.toUShort
  val GL_MAP1_VERTEX_3: UShort = 0x0d97.toUShort
  val GL_MAP1_VERTEX_4: UShort = 0x0d98.toUShort
  val GL_MAP2_COLOR_4: UShort = 0x0db0.toUShort
  val GL_MAP2_INDEX: UShort = 0x0db1.toUShort
  val GL_MAP2_NORMAL: UShort = 0x0db2.toUShort
  val GL_MAP2_TEXTURE_COORD_1: UShort = 0x0db3.toUShort
  val GL_MAP2_TEXTURE_COORD_2: UShort = 0x0db4.toUShort
  val GL_MAP2_TEXTURE_COORD_3: UShort = 0x0db5.toUShort
  val GL_MAP2_TEXTURE_COORD_4: UShort = 0x0db6.toUShort
  val GL_MAP2_VERTEX_3: UShort = 0x0db7.toUShort
  val GL_MAP2_VERTEX_4: UShort = 0x0db8.toUShort
  val GL_MAP1_GRID_DOMAIN: UShort = 0x0dd0.toUShort
  val GL_MAP1_GRID_SEGMENTS: UShort = 0x0dd1.toUShort
  val GL_MAP2_GRID_DOMAIN: UShort = 0x0dd2.toUShort
  val GL_MAP2_GRID_SEGMENTS: UShort = 0x0dd3.toUShort
  val GL_COEFF: UShort = 0x0a00.toUShort
  val GL_ORDER: UShort = 0x0a01.toUShort
  val GL_DOMAIN: UShort = 0x0a02.toUShort

  /* Hints */
  val GL_PERSPECTIVE_CORRECTION_HINT: UShort = 0x0c50.toUShort
  val GL_POINT_SMOOTH_HINT: UShort = 0x0c51.toUShort
  val GL_LINE_SMOOTH_HINT: UShort = 0x0c52.toUShort
  val GL_POLYGON_SMOOTH_HINT: UShort = 0x0c53.toUShort
  val GL_FOG_HINT: UShort = 0x0c54.toUShort
  val GL_DONT_CARE: UShort = 0x1100.toUShort
  val GL_FASTEST: UShort = 0x1101.toUShort
  val GL_NICEST: UShort = 0x1102.toUShort

  /* Scissor box */
  val GL_SCISSOR_BOX: UShort = 0x0c10.toUShort
  val GL_SCISSOR_TEST: UShort = 0x0c11.toUShort

  /* Pixel Mode / Transfer */
  val GL_MAP_COLOR: UShort = 0x0d10.toUShort
  val GL_MAP_STENCIL: UShort = 0x0d11.toUShort
  val GL_INDEX_SHIFT: UShort = 0x0d12.toUShort
  val GL_INDEX_OFFSET: UShort = 0x0d13.toUShort
  val GL_RED_SCALE: UShort = 0x0d14.toUShort
  val GL_RED_BIAS: UShort = 0x0d15.toUShort
  val GL_GREEN_SCALE: UShort = 0x0d18.toUShort
  val GL_GREEN_BIAS: UShort = 0x0d19.toUShort
  val GL_BLUE_SCALE: UShort = 0x0d1a.toUShort
  val GL_BLUE_BIAS: UShort = 0x0d1b.toUShort
  val GL_ALPHA_SCALE: UShort = 0x0d1c.toUShort
  val GL_ALPHA_BIAS: UShort = 0x0d1d.toUShort
  val GL_DEPTH_SCALE: UShort = 0x0d1e.toUShort
  val GL_DEPTH_BIAS: UShort = 0x0d1f.toUShort
  val GL_PIXEL_MAP_S_TO_S_SIZE: UShort = 0x0cb1.toUShort
  val GL_PIXEL_MAP_I_TO_I_SIZE: UShort = 0x0cb0.toUShort
  val GL_PIXEL_MAP_I_TO_R_SIZE: UShort = 0x0cb2.toUShort
  val GL_PIXEL_MAP_I_TO_G_SIZE: UShort = 0x0cb3.toUShort
  val GL_PIXEL_MAP_I_TO_B_SIZE: UShort = 0x0cb4.toUShort
  val GL_PIXEL_MAP_I_TO_A_SIZE: UShort = 0x0cb5.toUShort
  val GL_PIXEL_MAP_R_TO_R_SIZE: UShort = 0x0cb6.toUShort
  val GL_PIXEL_MAP_G_TO_G_SIZE: UShort = 0x0cb7.toUShort
  val GL_PIXEL_MAP_B_TO_B_SIZE: UShort = 0x0cb8.toUShort
  val GL_PIXEL_MAP_A_TO_A_SIZE: UShort = 0x0cb9.toUShort
  val GL_PIXEL_MAP_S_TO_S: UShort = 0x0c71.toUShort
  val GL_PIXEL_MAP_I_TO_I: UShort = 0x0c70.toUShort
  val GL_PIXEL_MAP_I_TO_R: UShort = 0x0c72.toUShort
  val GL_PIXEL_MAP_I_TO_G: UShort = 0x0c73.toUShort
  val GL_PIXEL_MAP_I_TO_B: UShort = 0x0c74.toUShort
  val GL_PIXEL_MAP_I_TO_A: UShort = 0x0c75.toUShort
  val GL_PIXEL_MAP_R_TO_R: UShort = 0x0c76.toUShort
  val GL_PIXEL_MAP_G_TO_G: UShort = 0x0c77.toUShort
  val GL_PIXEL_MAP_B_TO_B: UShort = 0x0c78.toUShort
  val GL_PIXEL_MAP_A_TO_A: UShort = 0x0c79.toUShort
  val GL_PACK_ALIGNMENT: UShort = 0x0d05.toUShort
  val GL_PACK_LSB_FIRST: UShort = 0x0d01.toUShort
  val GL_PACK_ROW_LENGTH: UShort = 0x0d02.toUShort
  val GL_PACK_SKIP_PIXELS: UShort = 0x0d04.toUShort
  val GL_PACK_SKIP_ROWS: UShort = 0x0d03.toUShort
  val GL_PACK_SWAP_BYTES: UShort = 0x0d00.toUShort
  val GL_UNPACK_ALIGNMENT: UShort = 0x0cf5.toUShort
  val GL_UNPACK_LSB_FIRST: UShort = 0x0cf1.toUShort
  val GL_UNPACK_ROW_LENGTH: UShort = 0x0cf2.toUShort
  val GL_UNPACK_SKIP_PIXELS: UShort = 0x0cf4.toUShort
  val GL_UNPACK_SKIP_ROWS: UShort = 0x0cf3.toUShort
  val GL_UNPACK_SWAP_BYTES: UShort = 0x0cf0.toUShort
  val GL_ZOOM_X: UShort = 0x0d16.toUShort
  val GL_ZOOM_Y: UShort = 0x0d17.toUShort

  /* Texture mapping */
  val GL_TEXTURE_ENV: UShort = 0x2300.toUShort
  val GL_TEXTURE_ENV_MODE: UShort = 0x2200.toUShort
  val GL_TEXTURE_1D: UShort = 0x0de0.toUShort
  val GL_TEXTURE_2D: UShort = 0x0de1.toUShort
  val GL_TEXTURE_WRAP_S: UShort = 0x2802.toUShort
  val GL_TEXTURE_WRAP_T: UShort = 0x2803.toUShort
  val GL_TEXTURE_MAG_FILTER: UShort = 0x2800.toUShort
  val GL_TEXTURE_MIN_FILTER: UShort = 0x2801.toUShort
  val GL_TEXTURE_ENV_COLOR: UShort = 0x2201.toUShort
  val GL_TEXTURE_GEN_S: UShort = 0x0c60.toUShort
  val GL_TEXTURE_GEN_T: UShort = 0x0c61.toUShort
  val GL_TEXTURE_GEN_R: UShort = 0x0c62.toUShort
  val GL_TEXTURE_GEN_Q: UShort = 0x0c63.toUShort
  val GL_TEXTURE_GEN_MODE: UShort = 0x2500.toUShort
  val GL_TEXTURE_BORDER_COLOR: UShort = 0x1004.toUShort
  val GL_TEXTURE_WIDTH: UShort = 0x1000.toUShort
  val GL_TEXTURE_HEIGHT: UShort = 0x1001.toUShort
  val GL_TEXTURE_BORDER: UShort = 0x1005.toUShort
  val GL_TEXTURE_COMPONENTS: UShort = 0x1003.toUShort
  val GL_TEXTURE_RED_SIZE: UShort = 0x805c.toUShort
  val GL_TEXTURE_GREEN_SIZE: UShort = 0x805d.toUShort
  val GL_TEXTURE_BLUE_SIZE: UShort = 0x805e.toUShort
  val GL_TEXTURE_ALPHA_SIZE: UShort = 0x805f.toUShort
  val GL_TEXTURE_LUMINANCE_SIZE: UShort = 0x8060.toUShort
  val GL_TEXTURE_INTENSITY_SIZE: UShort = 0x8061.toUShort
  val GL_NEAREST_MIPMAP_NEAREST: UShort = 0x2700.toUShort
  val GL_NEAREST_MIPMAP_LINEAR: UShort = 0x2702.toUShort
  val GL_LINEAR_MIPMAP_NEAREST: UShort = 0x2701.toUShort
  val GL_LINEAR_MIPMAP_LINEAR: UShort = 0x2703.toUShort
  val GL_OBJECT_LINEAR: UShort = 0x2401.toUShort
  val GL_OBJECT_PLANE: UShort = 0x2501.toUShort
  val GL_EYE_LINEAR: UShort = 0x2400.toUShort
  val GL_EYE_PLANE: UShort = 0x2502.toUShort
  val GL_SPHERE_MAP: UShort = 0x2402.toUShort
  val GL_DECAL: UShort = 0x2101.toUShort
  val GL_MODULATE: UShort = 0x2100.toUShort
  val GL_NEAREST: UShort = 0x2600.toUShort
  val GL_REPEAT: UShort = 0x2901.toUShort
  val GL_CLAMP: UShort = 0x2900.toUShort
  val GL_S: UShort = 0x2000.toUShort
  val GL_T: UShort = 0x2001.toUShort
  val GL_R: UShort = 0x2002.toUShort
  val GL_Q: UShort = 0x2003.toUShort

  /* Utility */
  val GL_VENDOR: UShort = 0x1f00.toUShort
  val GL_RENDERER: UShort = 0x1f01.toUShort
  val GL_VERSION: UShort = 0x1f02.toUShort
  val GL_EXTENSIONS: UShort = 0x1f03.toUShort

  /* Errors */
  val GL_NO_ERROR: UShort = 0.toUShort
  val GL_INVALID_ENUM: UShort = 0x0500.toUShort
  val GL_INVALID_VALUE: UShort = 0x0501.toUShort
  val GL_INVALID_OPERATION: UShort = 0x0502.toUShort
  val GL_STACK_OVERFLOW: UShort = 0x0503.toUShort
  val GL_STACK_UNDERFLOW: UShort = 0x0504.toUShort
  val GL_OUT_OF_MEMORY: UShort = 0x0505.toUShort

  /* glPush/PopAttrib bits */
  val GL_CURRENT_BIT: GLbitfield = 0x00000001
  val GL_POINT_BIT: GLbitfield = 0x00000002
  val GL_LINE_BIT: GLbitfield = 0x00000004
  val GL_POLYGON_BIT: GLbitfield = 0x00000008
  val GL_POLYGON_STIPPLE_BIT: GLbitfield = 0x00000010
  val GL_PIXEL_MODE_BIT: GLbitfield = 0x00000020
  val GL_LIGHTING_BIT: GLbitfield = 0x00000040
  val GL_FOG_BIT: GLbitfield = 0x00000080
  val GL_DEPTH_BUFFER_BIT: GLbitfield = 0x00000100
  val GL_ACCUM_BUFFER_BIT: GLbitfield = 0x00000200
  val GL_STENCIL_BUFFER_BIT: GLbitfield = 0x00000400
  val GL_VIEWPORT_BIT: GLbitfield = 0x00000800
  val GL_TRANSFORM_BIT: GLbitfield = 0x00001000
  val GL_ENABLE_BIT: GLbitfield = 0x00002000
  val GL_COLOR_BUFFER_BIT: GLbitfield = 0x00004000
  val GL_HINT_BIT: GLbitfield = 0x00008000
  val GL_EVAL_BIT: GLbitfield = 0x00010000
  val GL_LIST_BIT: GLbitfield = 0x00020000
  val GL_TEXTURE_BIT: GLbitfield = 0x00040000
  val GL_SCISSOR_BIT: GLbitfield = 0x00080000
  val GL_ALL_ATTRIB_BITS: GLbitfield = 0x000fffff

  /* OpenGL 1.1 */
  val GL_PROXY_TEXTURE_1D: UShort = 0x8063.toUShort
  val GL_PROXY_TEXTURE_2D: UShort = 0x8064.toUShort
  val GL_TEXTURE_PRIORITY: UShort = 0x8066.toUShort
  val GL_TEXTURE_RESIDENT: UShort = 0x8067.toUShort
  val GL_TEXTURE_BINDING_1D: UShort = 0x8068.toUShort
  val GL_TEXTURE_BINDING_2D: UShort = 0x8069.toUShort
  val GL_TEXTURE_INTERNAL_FORMAT: UShort = 0x1003.toUShort
  val GL_ALPHA4: UShort = 0x803b.toUShort
  val GL_ALPHA8: UShort = 0x803c.toUShort
  val GL_ALPHA12: UShort = 0x803d.toUShort
  val GL_ALPHA16: UShort = 0x803e.toUShort
  val GL_LUMINANCE4: UShort = 0x803f.toUShort
  val GL_LUMINANCE8: UShort = 0x8040.toUShort
  val GL_LUMINANCE12: UShort = 0x8041.toUShort
  val GL_LUMINANCE16: UShort = 0x8042.toUShort
  val GL_LUMINANCE4_ALPHA4: UShort = 0x8043.toUShort
  val GL_LUMINANCE6_ALPHA2: UShort = 0x8044.toUShort
  val GL_LUMINANCE8_ALPHA8: UShort = 0x8045.toUShort
  val GL_LUMINANCE12_ALPHA4: UShort = 0x8046.toUShort
  val GL_LUMINANCE12_ALPHA12: UShort = 0x8047.toUShort
  val GL_LUMINANCE16_ALPHA16: UShort = 0x8048.toUShort
  val GL_INTENSITY: UShort = 0x8049.toUShort
  val GL_INTENSITY4: UShort = 0x804a.toUShort
  val GL_INTENSITY8: UShort = 0x804b.toUShort
  val GL_INTENSITY12: UShort = 0x804c.toUShort
  val GL_INTENSITY16: UShort = 0x804d.toUShort
  val GL_R3_G3_B2: UShort = 0x2a10.toUShort
  val GL_RGB4: UShort = 0x804f.toUShort
  val GL_RGB5: UShort = 0x8050.toUShort
  val GL_RGB8: UShort = 0x8051.toUShort
  val GL_RGB10: UShort = 0x8052.toUShort
  val GL_RGB12: UShort = 0x8053.toUShort
  val GL_RGB16: UShort = 0x8054.toUShort
  val GL_RGBA2: UShort = 0x8055.toUShort
  val GL_RGBA4: UShort = 0x8056.toUShort
  val GL_RGB5_A1: UShort = 0x8057.toUShort
  val GL_RGBA8: UShort = 0x8058.toUShort
  val GL_RGB10_A2: UShort = 0x8059.toUShort
  val GL_RGBA12: UShort = 0x805a.toUShort
  val GL_RGBA16: UShort = 0x805b.toUShort
  val GL_CLIENT_PIXEL_STORE_BIT: UInt = 0x00000001.toUInt
  val GL_CLIENT_VERTEX_ARRAY_BIT: UInt = 0x00000002.toUInt
  val GL_ALL_CLIENT_ATTRIB_BITS: UInt = 0xffffffff.toUInt
  val GL_CLIENT_ALL_ATTRIB_BITS: UInt = 0xffffffff.toUInt

  /*
   * OpenGL 1.2
   */
  val GL_RESCALE_NORMAL: UShort = 0x803a.toUShort
  val GL_CLAMP_TO_EDGE: UShort = 0x812f.toUShort
  val GL_MAX_ELEMENTS_VERTICES: UShort = 0x80e8.toUShort
  val GL_MAX_ELEMENTS_INDICES: UShort = 0x80e9.toUShort
  val GL_BGR: UShort = 0x80e0.toUShort
  val GL_BGRA: UShort = 0x80e1.toUShort
  val GL_UNSIGNED_BYTE_3_3_2: UShort = 0x8032.toUShort
  val GL_UNSIGNED_BYTE_2_3_3_REV: UShort = 0x8362.toUShort
  val GL_UNSIGNED_SHORT_5_6_5: UShort = 0x8363.toUShort
  val GL_UNSIGNED_SHORT_5_6_5_REV: UShort = 0x8364.toUShort
  val GL_UNSIGNED_SHORT_4_4_4_4: UShort = 0x8033.toUShort
  val GL_UNSIGNED_SHORT_4_4_4_4_REV: UShort = 0x8365.toUShort
  val GL_UNSIGNED_SHORT_5_5_5_1: UShort = 0x8034.toUShort
  val GL_UNSIGNED_SHORT_1_5_5_5_REV: UShort = 0x8366.toUShort
  val GL_UNSIGNED_INT_8_8_8_8: UShort = 0x8035.toUShort
  val GL_UNSIGNED_INT_8_8_8_8_REV: UShort = 0x8367.toUShort
  val GL_UNSIGNED_INT_10_10_10_2: UShort = 0x8036.toUShort
  val GL_UNSIGNED_INT_2_10_10_10_REV: UShort = 0x8368.toUShort
  val GL_LIGHT_MODEL_COLOR_CONTROL: UShort = 0x81f8.toUShort
  val GL_SINGLE_COLOR: UShort = 0x81f9.toUShort
  val GL_SEPARATE_SPECULAR_COLOR: UShort = 0x81fa.toUShort
  val GL_TEXTURE_MIN_LOD: UShort = 0x813a.toUShort
  val GL_TEXTURE_MAX_LOD: UShort = 0x813b.toUShort
  val GL_TEXTURE_BASE_LEVEL: UShort = 0x813c.toUShort
  val GL_TEXTURE_MAX_LEVEL: UShort = 0x813d.toUShort
  val GL_SMOOTH_POINT_SIZE_RANGE: UShort = 0x0b12.toUShort
  val GL_SMOOTH_POINT_SIZE_GRANULARITY: UShort = 0x0b13.toUShort
  val GL_SMOOTH_LINE_WIDTH_RANGE: UShort = 0x0b22.toUShort
  val GL_SMOOTH_LINE_WIDTH_GRANULARITY: UShort = 0x0b23.toUShort
  val GL_ALIASED_POINT_SIZE_RANGE: UShort = 0x846d.toUShort
  val GL_ALIASED_LINE_WIDTH_RANGE: UShort = 0x846e.toUShort
  val GL_PACK_SKIP_IMAGES: UShort = 0x806b.toUShort
  val GL_PACK_IMAGE_HEIGHT: UShort = 0x806c.toUShort
  val GL_UNPACK_SKIP_IMAGES: UShort = 0x806d.toUShort
  val GL_UNPACK_IMAGE_HEIGHT: UShort = 0x806e.toUShort
  val GL_TEXTURE_3D: UShort = 0x806f.toUShort
  val GL_PROXY_TEXTURE_3D: UShort = 0x8070.toUShort
  val GL_TEXTURE_DEPTH: UShort = 0x8071.toUShort
  val GL_TEXTURE_WRAP_R: UShort = 0x8072.toUShort
  val GL_MAX_3D_TEXTURE_SIZE: UShort = 0x8073.toUShort
  val GL_TEXTURE_BINDING_3D: UShort = 0x806a.toUShort

  /*
   * GL_ARB_imaging
   */
  val GL_CONSTANT_COLOR: UShort = 0x8001.toUShort
  val GL_ONE_MINUS_CONSTANT_COLOR: UShort = 0x8002.toUShort
  val GL_CONSTANT_ALPHA: UShort = 0x8003.toUShort
  val GL_ONE_MINUS_CONSTANT_ALPHA: UShort = 0x8004.toUShort
  val GL_COLOR_TABLE: UShort = 0x80d0.toUShort
  val GL_POST_CONVOLUTION_COLOR_TABLE: UShort = 0x80d1.toUShort
  val GL_POST_COLOR_MATRIX_COLOR_TABLE: UShort = 0x80d2.toUShort
  val GL_PROXY_COLOR_TABLE: UShort = 0x80d3.toUShort
  val GL_PROXY_POST_CONVOLUTION_COLOR_TABLE: UShort = 0x80d4.toUShort
  val GL_PROXY_POST_COLOR_MATRIX_COLOR_TABLE: UShort = 0x80d5.toUShort
  val GL_COLOR_TABLE_SCALE: UShort = 0x80d6.toUShort
  val GL_COLOR_TABLE_BIAS: UShort = 0x80d7.toUShort
  val GL_COLOR_TABLE_FORMAT: UShort = 0x80d8.toUShort
  val GL_COLOR_TABLE_WIDTH: UShort = 0x80d9.toUShort
  val GL_COLOR_TABLE_RED_SIZE: UShort = 0x80da.toUShort
  val GL_COLOR_TABLE_GREEN_SIZE: UShort = 0x80db.toUShort
  val GL_COLOR_TABLE_BLUE_SIZE: UShort = 0x80dc.toUShort
  val GL_COLOR_TABLE_ALPHA_SIZE: UShort = 0x80dd.toUShort
  val GL_COLOR_TABLE_LUMINANCE_SIZE: UShort = 0x80de.toUShort
  val GL_COLOR_TABLE_INTENSITY_SIZE: UShort = 0x80df.toUShort
  val GL_CONVOLUTION_1D: UShort = 0x8010.toUShort
  val GL_CONVOLUTION_2D: UShort = 0x8011.toUShort
  val GL_SEPARABLE_2D: UShort = 0x8012.toUShort
  val GL_CONVOLUTION_BORDER_MODE: UShort = 0x8013.toUShort
  val GL_CONVOLUTION_FILTER_SCALE: UShort = 0x8014.toUShort
  val GL_CONVOLUTION_FILTER_BIAS: UShort = 0x8015.toUShort
  val GL_REDUCE: UShort = 0x8016.toUShort
  val GL_CONVOLUTION_FORMAT: UShort = 0x8017.toUShort
  val GL_CONVOLUTION_WIDTH: UShort = 0x8018.toUShort
  val GL_CONVOLUTION_HEIGHT: UShort = 0x8019.toUShort
  val GL_MAX_CONVOLUTION_WIDTH: UShort = 0x801a.toUShort
  val GL_MAX_CONVOLUTION_HEIGHT: UShort = 0x801b.toUShort
  val GL_POST_CONVOLUTION_RED_SCALE: UShort = 0x801c.toUShort
  val GL_POST_CONVOLUTION_GREEN_SCALE: UShort = 0x801d.toUShort
  val GL_POST_CONVOLUTION_BLUE_SCALE: UShort = 0x801e.toUShort
  val GL_POST_CONVOLUTION_ALPHA_SCALE: UShort = 0x801f.toUShort
  val GL_POST_CONVOLUTION_RED_BIAS: UShort = 0x8020.toUShort
  val GL_POST_CONVOLUTION_GREEN_BIAS: UShort = 0x8021.toUShort
  val GL_POST_CONVOLUTION_BLUE_BIAS: UShort = 0x8022.toUShort
  val GL_POST_CONVOLUTION_ALPHA_BIAS: UShort = 0x8023.toUShort
  val GL_CONSTANT_BORDER: UShort = 0x8151.toUShort
  val GL_REPLICATE_BORDER: UShort = 0x8153.toUShort
  val GL_CONVOLUTION_BORDER_COLOR: UShort = 0x8154.toUShort
  val GL_COLOR_MATRIX: UShort = 0x80b1.toUShort
  val GL_COLOR_MATRIX_STACK_DEPTH: UShort = 0x80b2.toUShort
  val GL_MAX_COLOR_MATRIX_STACK_DEPTH: UShort = 0x80b3.toUShort
  val GL_POST_COLOR_MATRIX_RED_SCALE: UShort = 0x80b4.toUShort
  val GL_POST_COLOR_MATRIX_GREEN_SCALE: UShort = 0x80b5.toUShort
  val GL_POST_COLOR_MATRIX_BLUE_SCALE: UShort = 0x80b6.toUShort
  val GL_POST_COLOR_MATRIX_ALPHA_SCALE: UShort = 0x80b7.toUShort
  val GL_POST_COLOR_MATRIX_RED_BIAS: UShort = 0x80b8.toUShort
  val GL_POST_COLOR_MATRIX_GREEN_BIAS: UShort = 0x80b9.toUShort
  val GL_POST_COLOR_MATRIX_BLUE_BIAS: UShort = 0x80ba.toUShort
  val GL_POST_COLOR_MATRIX_ALPHA_BIAS: UShort = 0x80bb.toUShort
  val GL_HISTOGRAM: UShort = 0x8024.toUShort
  val GL_PROXY_HISTOGRAM: UShort = 0x8025.toUShort
  val GL_HISTOGRAM_WIDTH: UShort = 0x8026.toUShort
  val GL_HISTOGRAM_FORMAT: UShort = 0x8027.toUShort
  val GL_HISTOGRAM_RED_SIZE: UShort = 0x8028.toUShort
  val GL_HISTOGRAM_GREEN_SIZE: UShort = 0x8029.toUShort
  val GL_HISTOGRAM_BLUE_SIZE: UShort = 0x802a.toUShort
  val GL_HISTOGRAM_ALPHA_SIZE: UShort = 0x802b.toUShort
  val GL_HISTOGRAM_LUMINANCE_SIZE: UShort = 0x802c.toUShort
  val GL_HISTOGRAM_SINK: UShort = 0x802d.toUShort
  val GL_MINMAX: UShort = 0x802e.toUShort
  val GL_MINMAX_FORMAT: UShort = 0x802f.toUShort
  val GL_MINMAX_SINK: UShort = 0x8030.toUShort
  val GL_TABLE_TOO_LARGE: UShort = 0x8031.toUShort
  val GL_BLEND_EQUATION: UShort = 0x8009.toUShort
  val GL_MIN: UShort = 0x8007.toUShort
  val GL_MAX: UShort = 0x8008.toUShort
  val GL_FUNC_ADD: UShort = 0x8006.toUShort
  val GL_FUNC_SUBTRACT: UShort = 0x800a.toUShort
  val GL_FUNC_REVERSE_SUBTRACT: UShort = 0x800b.toUShort
  val GL_BLEND_COLOR: UShort = 0x8005.toUShort

  /*
   * OpenGL 1.3
   */

  /* multitexture */
  val GL_TEXTURE0: UShort = 0x84c0.toUShort
  val GL_TEXTURE1: UShort = 0x84c1.toUShort
  val GL_TEXTURE2: UShort = 0x84c2.toUShort
  val GL_TEXTURE3: UShort = 0x84c3.toUShort
  val GL_TEXTURE4: UShort = 0x84c4.toUShort
  val GL_TEXTURE5: UShort = 0x84c5.toUShort
  val GL_TEXTURE6: UShort = 0x84c6.toUShort
  val GL_TEXTURE7: UShort = 0x84c7.toUShort
  val GL_TEXTURE8: UShort = 0x84c8.toUShort
  val GL_TEXTURE9: UShort = 0x84c9.toUShort
  val GL_TEXTURE10: UShort = 0x84ca.toUShort
  val GL_TEXTURE11: UShort = 0x84cb.toUShort
  val GL_TEXTURE12: UShort = 0x84cc.toUShort
  val GL_TEXTURE13: UShort = 0x84cd.toUShort
  val GL_TEXTURE14: UShort = 0x84ce.toUShort
  val GL_TEXTURE15: UShort = 0x84cf.toUShort
  val GL_TEXTURE16: UShort = 0x84d0.toUShort
  val GL_TEXTURE17: UShort = 0x84d1.toUShort
  val GL_TEXTURE18: UShort = 0x84d2.toUShort
  val GL_TEXTURE19: UShort = 0x84d3.toUShort
  val GL_TEXTURE20: UShort = 0x84d4.toUShort
  val GL_TEXTURE21: UShort = 0x84d5.toUShort
  val GL_TEXTURE22: UShort = 0x84d6.toUShort
  val GL_TEXTURE23: UShort = 0x84d7.toUShort
  val GL_TEXTURE24: UShort = 0x84d8.toUShort
  val GL_TEXTURE25: UShort = 0x84d9.toUShort
  val GL_TEXTURE26: UShort = 0x84da.toUShort
  val GL_TEXTURE27: UShort = 0x84db.toUShort
  val GL_TEXTURE28: UShort = 0x84dc.toUShort
  val GL_TEXTURE29: UShort = 0x84dd.toUShort
  val GL_TEXTURE30: UShort = 0x84de.toUShort
  val GL_TEXTURE31: UShort = 0x84df.toUShort
  val GL_ACTIVE_TEXTURE: UShort = 0x84e0.toUShort
  val GL_CLIENT_ACTIVE_TEXTURE: UShort = 0x84e1.toUShort
  val GL_MAX_TEXTURE_UNITS: UShort = 0x84e2.toUShort
  /* texture_cube_map */
  val GL_NORMAL_MAP: UShort = 0x8511.toUShort
  val GL_REFLECTION_MAP: UShort = 0x8512.toUShort
  val GL_TEXTURE_CUBE_MAP: UShort = 0x8513.toUShort
  val GL_TEXTURE_BINDING_CUBE_MAP: UShort = 0x8514.toUShort
  val GL_TEXTURE_CUBE_MAP_POSITIVE_X: UShort = 0x8515.toUShort
  val GL_TEXTURE_CUBE_MAP_NEGATIVE_X: UShort = 0x8516.toUShort
  val GL_TEXTURE_CUBE_MAP_POSITIVE_Y: UShort = 0x8517.toUShort
  val GL_TEXTURE_CUBE_MAP_NEGATIVE_Y: UShort = 0x8518.toUShort
  val GL_TEXTURE_CUBE_MAP_POSITIVE_Z: UShort = 0x8519.toUShort
  val GL_TEXTURE_CUBE_MAP_NEGATIVE_Z: UShort = 0x851a.toUShort
  val GL_PROXY_TEXTURE_CUBE_MAP: UShort = 0x851b.toUShort
  val GL_MAX_CUBE_MAP_TEXTURE_SIZE: UShort = 0x851c.toUShort
  /* texture_compression */
  val GL_COMPRESSED_ALPHA: UShort = 0x84e9.toUShort
  val GL_COMPRESSED_LUMINANCE: UShort = 0x84ea.toUShort
  val GL_COMPRESSED_LUMINANCE_ALPHA: UShort = 0x84eb.toUShort
  val GL_COMPRESSED_INTENSITY: UShort = 0x84ec.toUShort
  val GL_COMPRESSED_RGB: UShort = 0x84ed.toUShort
  val GL_COMPRESSED_RGBA: UShort = 0x84ee.toUShort
  val GL_TEXTURE_COMPRESSION_HINT: UShort = 0x84ef.toUShort
  val GL_TEXTURE_COMPRESSED_IMAGE_SIZE: UShort = 0x86a0.toUShort
  val GL_TEXTURE_COMPRESSED: UShort = 0x86a1.toUShort
  val GL_NUM_COMPRESSED_TEXTURE_FORMATS: UShort = 0x86a2.toUShort
  val GL_COMPRESSED_TEXTURE_FORMATS: UShort = 0x86a3.toUShort
  /* multisample */
  val GL_MULTISAMPLE: UShort = 0x809d.toUShort
  val GL_SAMPLE_ALPHA_TO_COVERAGE: UShort = 0x809e.toUShort
  val GL_SAMPLE_ALPHA_TO_ONE: UShort = 0x809f.toUShort
  val GL_SAMPLE_COVERAGE: UShort = 0x80a0.toUShort
  val GL_SAMPLE_BUFFERS: UShort = 0x80a8.toUShort
  val GL_SAMPLES: UShort = 0x80a9.toUShort
  val GL_SAMPLE_COVERAGE_VALUE: UShort = 0x80aa.toUShort
  val GL_SAMPLE_COVERAGE_INVERT: UShort = 0x80ab.toUShort
  val GL_MULTISAMPLE_BIT: UInt = 0x20000000.toUInt
  /* transpose_matrix */
  val GL_TRANSPOSE_MODELVIEW_MATRIX: UShort = 0x84e3.toUShort
  val GL_TRANSPOSE_PROJECTION_MATRIX: UShort = 0x84e4.toUShort
  val GL_TRANSPOSE_TEXTURE_MATRIX: UShort = 0x84e5.toUShort
  val GL_TRANSPOSE_COLOR_MATRIX: UShort = 0x84e6.toUShort
  /* texture_env_combine */
  val GL_COMBINE: UShort = 0x8570.toUShort
  val GL_COMBINE_RGB: UShort = 0x8571.toUShort
  val GL_COMBINE_ALPHA: UShort = 0x8572.toUShort
  val GL_SOURCE0_RGB: UShort = 0x8580.toUShort
  val GL_SOURCE1_RGB: UShort = 0x8581.toUShort
  val GL_SOURCE2_RGB: UShort = 0x8582.toUShort
  val GL_SOURCE0_ALPHA: UShort = 0x8588.toUShort
  val GL_SOURCE1_ALPHA: UShort = 0x8589.toUShort
  val GL_SOURCE2_ALPHA: UShort = 0x858a.toUShort
  val GL_OPERAND0_RGB: UShort = 0x8590.toUShort
  val GL_OPERAND1_RGB: UShort = 0x8591.toUShort
  val GL_OPERAND2_RGB: UShort = 0x8592.toUShort
  val GL_OPERAND0_ALPHA: UShort = 0x8598.toUShort
  val GL_OPERAND1_ALPHA: UShort = 0x8599.toUShort
  val GL_OPERAND2_ALPHA: UShort = 0x859a.toUShort
  val GL_RGB_SCALE: UShort = 0x8573.toUShort
  val GL_ADD_SIGNED: UShort = 0x8574.toUShort
  val GL_INTERPOLATE: UShort = 0x8575.toUShort
  val GL_SUBTRACT: UShort = 0x84e7.toUShort
  val GL_CONSTANT: UShort = 0x8576.toUShort
  val GL_PRIMARY_COLOR: UShort = 0x8577.toUShort
  val GL_PREVIOUS: UShort = 0x8578.toUShort
  /* texture_env_dot3 */
  val GL_DOT3_RGB: UShort = 0x86ae.toUShort
  val GL_DOT3_RGBA: UShort = 0x86af.toUShort
  /* texture_border_clamp */
  val GL_CLAMP_TO_BORDER: UShort = 0x812d.toUShort

  /*
   * OpenGL 1.2.1 ARB extension
   */
  val GL_TEXTURE0_ARB: UShort = 0x84c0.toUShort
  val GL_TEXTURE1_ARB: UShort = 0x84c1.toUShort
  val GL_TEXTURE2_ARB: UShort = 0x84c2.toUShort
  val GL_TEXTURE3_ARB: UShort = 0x84c3.toUShort
  val GL_TEXTURE4_ARB: UShort = 0x84c4.toUShort
  val GL_TEXTURE5_ARB: UShort = 0x84c5.toUShort
  val GL_TEXTURE6_ARB: UShort = 0x84c6.toUShort
  val GL_TEXTURE7_ARB: UShort = 0x84c7.toUShort
  val GL_TEXTURE8_ARB: UShort = 0x84c8.toUShort
  val GL_TEXTURE9_ARB: UShort = 0x84c9.toUShort
  val GL_TEXTURE10_ARB: UShort = 0x84ca.toUShort
  val GL_TEXTURE11_ARB: UShort = 0x84cb.toUShort
  val GL_TEXTURE12_ARB: UShort = 0x84cc.toUShort
  val GL_TEXTURE13_ARB: UShort = 0x84cd.toUShort
  val GL_TEXTURE14_ARB: UShort = 0x84ce.toUShort
  val GL_TEXTURE15_ARB: UShort = 0x84cf.toUShort
  val GL_TEXTURE16_ARB: UShort = 0x84d0.toUShort
  val GL_TEXTURE17_ARB: UShort = 0x84d1.toUShort
  val GL_TEXTURE18_ARB: UShort = 0x84d2.toUShort
  val GL_TEXTURE19_ARB: UShort = 0x84d3.toUShort
  val GL_TEXTURE20_ARB: UShort = 0x84d4.toUShort
  val GL_TEXTURE21_ARB: UShort = 0x84d5.toUShort
  val GL_TEXTURE22_ARB: UShort = 0x84d6.toUShort
  val GL_TEXTURE23_ARB: UShort = 0x84d7.toUShort
  val GL_TEXTURE24_ARB: UShort = 0x84d8.toUShort
  val GL_TEXTURE25_ARB: UShort = 0x84d9.toUShort
  val GL_TEXTURE26_ARB: UShort = 0x84da.toUShort
  val GL_TEXTURE27_ARB: UShort = 0x84db.toUShort
  val GL_TEXTURE28_ARB: UShort = 0x84dc.toUShort
  val GL_TEXTURE29_ARB: UShort = 0x84dd.toUShort
  val GL_TEXTURE30_ARB: UShort = 0x84de.toUShort
  val GL_TEXTURE31_ARB: UShort = 0x84df.toUShort
  val GL_ACTIVE_TEXTURE_ARB: UShort = 0x84e0.toUShort
  val GL_CLIENT_ACTIVE_TEXTURE_ARB: UShort = 0x84e1.toUShort
  val GL_MAX_TEXTURE_UNITS_ARB: UShort = 0x84e2.toUShort

  /*
   * OpenGL 1.4
   */
  val GL_BLEND_DST_RGB: UShort = 0x80c8.toUShort
  val GL_BLEND_SRC_RGB: UShort = 0x80c9.toUShort
  val GL_BLEND_DST_ALPHA: UShort = 0x80ca.toUShort
  val GL_BLEND_SRC_ALPHA: UShort = 0x80cb.toUShort
  val GL_POINT_FADE_THRESHOLD_SIZE: UShort = 0x8128.toUShort
  val GL_DEPTH_COMPONENT16: UShort = 0x81a5.toUShort
  val GL_DEPTH_COMPONENT24: UShort = 0x81a6.toUShort
  val GL_DEPTH_COMPONENT32: UShort = 0x81a7.toUShort
  val GL_MIRRORED_REPEAT: UShort = 0x8370.toUShort
  val GL_MAX_TEXTURE_LOD_BIAS: UShort = 0x84fd.toUShort
  val GL_TEXTURE_LOD_BIAS: UShort = 0x8501.toUShort
  val GL_INCR_WRAP: UShort = 0x8507.toUShort
  val GL_DECR_WRAP: UShort = 0x8508.toUShort
  val GL_TEXTURE_DEPTH_SIZE: UShort = 0x884a.toUShort
  val GL_TEXTURE_COMPARE_MODE: UShort = 0x884c.toUShort
  val GL_TEXTURE_COMPARE_FUNC: UShort = 0x884d.toUShort
  val GL_POINT_SIZE_MIN: UShort = 0x8126.toUShort
  val GL_POINT_SIZE_MAX: UShort = 0x8127.toUShort
  val GL_POINT_DISTANCE_ATTENUATION: UShort = 0x8129.toUShort
  val GL_GENERATE_MIPMAP: UShort = 0x8191.toUShort
  val GL_GENERATE_MIPMAP_HINT: UShort = 0x8192.toUShort
  val GL_FOG_COORDINATE_SOURCE: UShort = 0x8450.toUShort
  val GL_FOG_COORDINATE: UShort = 0x8451.toUShort
  val GL_FRAGMENT_DEPTH: UShort = 0x8452.toUShort
  val GL_CURRENT_FOG_COORDINATE: UShort = 0x8453.toUShort
  val GL_FOG_COORDINATE_ARRAY_TYPE: UShort = 0x8454.toUShort
  val GL_FOG_COORDINATE_ARRAY_STRIDE: UShort = 0x8455.toUShort
  val GL_FOG_COORDINATE_ARRAY_POINTER: UShort = 0x8456.toUShort
  val GL_FOG_COORDINATE_ARRAY: UShort = 0x8457.toUShort
  val GL_COLOR_SUM: UShort = 0x8458.toUShort
  val GL_CURRENT_SECONDARY_COLOR: UShort = 0x8459.toUShort
  val GL_SECONDARY_COLOR_ARRAY_SIZE: UShort = 0x845a.toUShort
  val GL_SECONDARY_COLOR_ARRAY_TYPE: UShort = 0x845b.toUShort
  val GL_SECONDARY_COLOR_ARRAY_STRIDE: UShort = 0x845c.toUShort
  val GL_SECONDARY_COLOR_ARRAY_POINTER: UShort = 0x845d.toUShort
  val GL_SECONDARY_COLOR_ARRAY: UShort = 0x845e.toUShort
  val GL_TEXTURE_FILTER_CONTROL: UShort = 0x8500.toUShort
  val GL_DEPTH_TEXTURE_MODE: UShort = 0x884b.toUShort
  val GL_COMPARE_R_TO_TEXTURE: UShort = 0x884e.toUShort

  /*
   * OpenGL 1.5
   */
  val GL_BUFFER_SIZE: UShort = 0x8764.toUShort
  val GL_BUFFER_USAGE: UShort = 0x8765.toUShort
  val GL_QUERY_COUNTER_BITS: UShort = 0x8864.toUShort
  val GL_CURRENT_QUERY: UShort = 0x8865.toUShort
  val GL_QUERY_RESULT: UShort = 0x8866.toUShort
  val GL_QUERY_RESULT_AVAILABLE: UShort = 0x8867.toUShort
  val GL_ARRAY_BUFFER: UShort = 0x8892.toUShort
  val GL_ELEMENT_ARRAY_BUFFER: UShort = 0x8893.toUShort
  val GL_ARRAY_BUFFER_BINDING: UShort = 0x8894.toUShort
  val GL_ELEMENT_ARRAY_BUFFER_BINDING: UShort = 0x8895.toUShort
  val GL_VERTEX_ATTRIB_ARRAY_BUFFER_BINDING: UShort = 0x889f.toUShort
  val GL_READ_ONLY: UShort = 0x88b8.toUShort
  val GL_WRITE_ONLY: UShort = 0x88b9.toUShort
  val GL_READ_WRITE: UShort = 0x88ba.toUShort
  val GL_BUFFER_ACCESS: UShort = 0x88bb.toUShort
  val GL_BUFFER_MAPPED: UShort = 0x88bc.toUShort
  val GL_BUFFER_MAP_POINTER: UShort = 0x88bd.toUShort
  val GL_STREAM_DRAW: UShort = 0x88e0.toUShort
  val GL_STREAM_READ: UShort = 0x88e1.toUShort
  val GL_STREAM_COPY: UShort = 0x88e2.toUShort
  val GL_STATIC_DRAW: UShort = 0x88e4.toUShort
  val GL_STATIC_READ: UShort = 0x88e5.toUShort
  val GL_STATIC_COPY: UShort = 0x88e6.toUShort
  val GL_DYNAMIC_DRAW: UShort = 0x88e8.toUShort
  val GL_DYNAMIC_READ: UShort = 0x88e9.toUShort
  val GL_DYNAMIC_COPY: UShort = 0x88ea.toUShort
  val GL_SAMPLES_PASSED: UShort = 0x8914.toUShort
  val GL_SRC1_ALPHA: UShort = 0x8589.toUShort
  val GL_VERTEX_ARRAY_BUFFER_BINDING: UShort = 0x8896.toUShort
  val GL_NORMAL_ARRAY_BUFFER_BINDING: UShort = 0x8897.toUShort
  val GL_COLOR_ARRAY_BUFFER_BINDING: UShort = 0x8898.toUShort
  val GL_INDEX_ARRAY_BUFFER_BINDING: UShort = 0x8899.toUShort
  val GL_TEXTURE_COORD_ARRAY_BUFFER_BINDING: UShort = 0x889a.toUShort
  val GL_EDGE_FLAG_ARRAY_BUFFER_BINDING: UShort = 0x889b.toUShort
  val GL_SECONDARY_COLOR_ARRAY_BUFFER_BINDING: UShort = 0x889c.toUShort
  val GL_FOG_COORDINATE_ARRAY_BUFFER_BINDING: UShort = 0x889d.toUShort
  val GL_WEIGHT_ARRAY_BUFFER_BINDING: UShort = 0x889e.toUShort
  val GL_FOG_COORD_SRC: UShort = 0x8450.toUShort
  val GL_FOG_COORD: UShort = 0x8451.toUShort
  val GL_CURRENT_FOG_COORD: UShort = 0x8453.toUShort
  val GL_FOG_COORD_ARRAY_TYPE: UShort = 0x8454.toUShort
  val GL_FOG_COORD_ARRAY_STRIDE: UShort = 0x8455.toUShort
  val GL_FOG_COORD_ARRAY_POINTER: UShort = 0x8456.toUShort
  val GL_FOG_COORD_ARRAY: UShort = 0x8457.toUShort
  val GL_FOG_COORD_ARRAY_BUFFER_BINDING: UShort = 0x889d.toUShort
  val GL_SRC0_RGB: UShort = 0x8580.toUShort
  val GL_SRC1_RGB: UShort = 0x8581.toUShort
  val GL_SRC2_RGB: UShort = 0x8582.toUShort
  val GL_SRC0_ALPHA: UShort = 0x8588.toUShort
  val GL_SRC2_ALPHA: UShort = 0x858a.toUShort
  /*
   * End OpenGL 1.5
   */

  /*
   * OpenGL 2.0
   */
  val GL_BLEND_EQUATION_RGB: UShort = 0x8009.toUShort
  val GL_VERTEX_ATTRIB_ARRAY_ENABLED: UShort = 0x8622.toUShort
  val GL_VERTEX_ATTRIB_ARRAY_SIZE: UShort = 0x8623.toUShort
  val GL_VERTEX_ATTRIB_ARRAY_STRIDE: UShort = 0x8624.toUShort
  val GL_VERTEX_ATTRIB_ARRAY_TYPE: UShort = 0x8625.toUShort
  val GL_CURRENT_VERTEX_ATTRIB: UShort = 0x8626.toUShort
  val GL_VERTEX_PROGRAM_POINT_SIZE: UShort = 0x8642.toUShort
  val GL_VERTEX_ATTRIB_ARRAY_POINTER: UShort = 0x8645.toUShort
  val GL_STENCIL_BACK_FUNC: UShort = 0x8800.toUShort
  val GL_STENCIL_BACK_FAIL: UShort = 0x8801.toUShort
  val GL_STENCIL_BACK_PASS_DEPTH_FAIL: UShort = 0x8802.toUShort
  val GL_STENCIL_BACK_PASS_DEPTH_PASS: UShort = 0x8803.toUShort
  val GL_MAX_DRAW_BUFFERS: UShort = 0x8824.toUShort
  val GL_DRAW_BUFFER0: UShort = 0x8825.toUShort
  val GL_DRAW_BUFFER1: UShort = 0x8826.toUShort
  val GL_DRAW_BUFFER2: UShort = 0x8827.toUShort
  val GL_DRAW_BUFFER3: UShort = 0x8828.toUShort
  val GL_DRAW_BUFFER4: UShort = 0x8829.toUShort
  val GL_DRAW_BUFFER5: UShort = 0x882a.toUShort
  val GL_DRAW_BUFFER6: UShort = 0x882b.toUShort
  val GL_DRAW_BUFFER7: UShort = 0x882c.toUShort
  val GL_DRAW_BUFFER8: UShort = 0x882d.toUShort
  val GL_DRAW_BUFFER9: UShort = 0x882e.toUShort
  val GL_DRAW_BUFFER10: UShort = 0x882f.toUShort
  val GL_DRAW_BUFFER11: UShort = 0x8830.toUShort
  val GL_DRAW_BUFFER12: UShort = 0x8831.toUShort
  val GL_DRAW_BUFFER13: UShort = 0x8832.toUShort
  val GL_DRAW_BUFFER14: UShort = 0x8833.toUShort
  val GL_DRAW_BUFFER15: UShort = 0x8834.toUShort
  val GL_BLEND_EQUATION_ALPHA: UShort = 0x883d.toUShort
  val GL_MAX_VERTEX_ATTRIBS: UShort = 0x8869.toUShort
  val GL_VERTEX_ATTRIB_ARRAY_NORMALIZED: UShort = 0x886a.toUShort
  val GL_MAX_TEXTURE_IMAGE_UNITS: UShort = 0x8872.toUShort
  val GL_FRAGMENT_SHADER: UShort = 0x8b30.toUShort
  val GL_VERTEX_SHADER: UShort = 0x8b31.toUShort
  val GL_MAX_FRAGMENT_UNIFORM_COMPONENTS: UShort = 0x8b49.toUShort
  val GL_MAX_VERTEX_UNIFORM_COMPONENTS: UShort = 0x8b4a.toUShort
  val GL_MAX_VARYING_FLOATS: UShort = 0x8b4b.toUShort
  val GL_MAX_VERTEX_TEXTURE_IMAGE_UNITS: UShort = 0x8b4c.toUShort
  val GL_MAX_COMBINED_TEXTURE_IMAGE_UNITS: UShort = 0x8b4d.toUShort
  val GL_SHADER_TYPE: UShort = 0x8b4f.toUShort
  val GL_FLOAT_VEC2: UShort = 0x8b50.toUShort
  val GL_FLOAT_VEC3: UShort = 0x8b51.toUShort
  val GL_FLOAT_VEC4: UShort = 0x8b52.toUShort
  val GL_INT_VEC2: UShort = 0x8b53.toUShort
  val GL_INT_VEC3: UShort = 0x8b54.toUShort
  val GL_INT_VEC4: UShort = 0x8b55.toUShort
  val GL_BOOL: UShort = 0x8b56.toUShort
  val GL_BOOL_VEC2: UShort = 0x8b57.toUShort
  val GL_BOOL_VEC3: UShort = 0x8b58.toUShort
  val GL_BOOL_VEC4: UShort = 0x8b59.toUShort
  val GL_FLOAT_MAT2: UShort = 0x8b5a.toUShort
  val GL_FLOAT_MAT3: UShort = 0x8b5b.toUShort
  val GL_FLOAT_MAT4: UShort = 0x8b5c.toUShort
  val GL_SAMPLER_1D: UShort = 0x8b5d.toUShort
  val GL_SAMPLER_2D: UShort = 0x8b5e.toUShort
  val GL_SAMPLER_3D: UShort = 0x8b5f.toUShort
  val GL_SAMPLER_CUBE: UShort = 0x8b60.toUShort
  val GL_SAMPLER_1D_SHADOW: UShort = 0x8b61.toUShort
  val GL_SAMPLER_2D_SHADOW: UShort = 0x8b62.toUShort
  val GL_DELETE_STATUS: UShort = 0x8b80.toUShort
  val GL_COMPILE_STATUS: UShort = 0x8b81.toUShort
  val GL_LINK_STATUS: UShort = 0x8b82.toUShort
  val GL_VALIDATE_STATUS: UShort = 0x8b83.toUShort
  val GL_INFO_LOG_LENGTH: UShort = 0x8b84.toUShort
  val GL_ATTACHED_SHADERS: UShort = 0x8b85.toUShort
  val GL_ACTIVE_UNIFORMS: UShort = 0x8b86.toUShort
  val GL_ACTIVE_UNIFORM_MAX_LENGTH: UShort = 0x8b87.toUShort
  val GL_SHADER_SOURCE_LENGTH: UShort = 0x8b88.toUShort
  val GL_ACTIVE_ATTRIBUTES: UShort = 0x8b89.toUShort
  val GL_ACTIVE_ATTRIBUTE_MAX_LENGTH: UShort = 0x8b8a.toUShort
  val GL_FRAGMENT_SHADER_DERIVATIVE_HINT: UShort = 0x8b8b.toUShort
  val GL_SHADING_LANGUAGE_VERSION: UShort = 0x8b8c.toUShort
  val GL_CURRENT_PROGRAM: UShort = 0x8b8d.toUShort
  val GL_POINT_SPRITE_COORD_ORIGIN: UShort = 0x8ca0.toUShort
  val GL_LOWER_LEFT: UShort = 0x8ca1.toUShort
  val GL_UPPER_LEFT: UShort = 0x8ca2.toUShort
  val GL_STENCIL_BACK_REF: UShort = 0x8ca3.toUShort
  val GL_STENCIL_BACK_VALUE_MASK: UShort = 0x8ca4.toUShort
  val GL_STENCIL_BACK_WRITEMASK: UShort = 0x8ca5.toUShort
  val GL_VERTEX_PROGRAM_TWO_SIDE: UShort = 0x8643.toUShort
  val GL_POINT_SPRITE: UShort = 0x8861.toUShort
  val GL_COORD_REPLACE: UShort = 0x8862.toUShort
  val GL_MAX_TEXTURE_COORDS: UShort = 0x8871.toUShort
  /*
   * End OpenGL 2.0
   */

  /*
   * OpenGL 2.1
   */
  val GL_PIXEL_PACK_BUFFER: UShort = 0x88eb.toUShort
  val GL_PIXEL_UNPACK_BUFFER: UShort = 0x88ec.toUShort
  val GL_PIXEL_PACK_BUFFER_BINDING: UShort = 0x88ed.toUShort
  val GL_PIXEL_UNPACK_BUFFER_BINDING: UShort = 0x88ef.toUShort
  val GL_FLOAT_MAT2x3: UShort = 0x8b65.toUShort
  val GL_FLOAT_MAT2x4: UShort = 0x8b66.toUShort
  val GL_FLOAT_MAT3x2: UShort = 0x8b67.toUShort
  val GL_FLOAT_MAT3x4: UShort = 0x8b68.toUShort
  val GL_FLOAT_MAT4x2: UShort = 0x8b69.toUShort
  val GL_FLOAT_MAT4x3: UShort = 0x8b6a.toUShort
  val GL_SRGB: UShort = 0x8c40.toUShort
  val GL_SRGB8: UShort = 0x8c41.toUShort
  val GL_SRGB_ALPHA: UShort = 0x8c42.toUShort
  val GL_SRGB8_ALPHA8: UShort = 0x8c43.toUShort
  val GL_COMPRESSED_SRGB: UShort = 0x8c48.toUShort
  val GL_COMPRESSED_SRGB_ALPHA: UShort = 0x8c49.toUShort
  val GL_CURRENT_RASTER_SECONDARY_COLOR: UShort = 0x845f.toUShort
  val GL_SLUMINANCE_ALPHA: UShort = 0x8c44.toUShort
  val GL_SLUMINANCE8_ALPHA8: UShort = 0x8c45.toUShort
  val GL_SLUMINANCE: UShort = 0x8c46.toUShort
  val GL_SLUMINANCE8: UShort = 0x8c47.toUShort
  val GL_COMPRESSED_SLUMINANCE: UShort = 0x8c4a.toUShort
  val GL_COMPRESSED_SLUMINANCE_ALPHA: UShort = 0x8c4b.toUShort
  /*
   * End OpenGL 2.1
   */

  /*
   * OpenGL 3.0
   */
  val GL_COMPARE_REF_TO_TEXTURE: UShort = 0x884e.toUShort
  val GL_CLIP_DISTANCE0: UShort = 0x3000.toUShort
  val GL_CLIP_DISTANCE1: UShort = 0x3001.toUShort
  val GL_CLIP_DISTANCE2: UShort = 0x3002.toUShort
  val GL_CLIP_DISTANCE3: UShort = 0x3003.toUShort
  val GL_CLIP_DISTANCE4: UShort = 0x3004.toUShort
  val GL_CLIP_DISTANCE5: UShort = 0x3005.toUShort
  val GL_CLIP_DISTANCE6: UShort = 0x3006.toUShort
  val GL_CLIP_DISTANCE7: UShort = 0x3007.toUShort
  val GL_MAX_CLIP_DISTANCES: UShort = 0x0d32.toUShort
  val GL_MAJOR_VERSION: UShort = 0x821b.toUShort
  val GL_MINOR_VERSION: UShort = 0x821c.toUShort
  val GL_NUM_EXTENSIONS: UShort = 0x821d.toUShort
  val GL_CONTEXT_FLAGS: UShort = 0x821e.toUShort
  val GL_COMPRESSED_RED: UShort = 0x8225.toUShort
  val GL_COMPRESSED_RG: UShort = 0x8226.toUShort
  val GL_CONTEXT_FLAG_FORWARD_COMPATIBLE_BIT: UInt = 0x00000001.toUInt
  val GL_RGBA32F: UShort = 0x8814.toUShort
  val GL_RGB32F: UShort = 0x8815.toUShort
  val GL_RGBA16F: UShort = 0x881a.toUShort
  val GL_RGB16F: UShort = 0x881b.toUShort
  val GL_VERTEX_ATTRIB_ARRAY_INTEGER: UShort = 0x88fd.toUShort
  val GL_MAX_ARRAY_TEXTURE_LAYERS: UShort = 0x88ff.toUShort
  val GL_MIN_PROGRAM_TEXEL_OFFSET: UShort = 0x8904.toUShort
  val GL_MAX_PROGRAM_TEXEL_OFFSET: UShort = 0x8905.toUShort
  val GL_CLAMP_READ_COLOR: UShort = 0x891c.toUShort
  val GL_FIXED_ONLY: UShort = 0x891d.toUShort
  val GL_MAX_VARYING_COMPONENTS: UShort = 0x8b4b.toUShort
  val GL_TEXTURE_1D_ARRAY: UShort = 0x8c18.toUShort
  val GL_PROXY_TEXTURE_1D_ARRAY: UShort = 0x8c19.toUShort
  val GL_TEXTURE_2D_ARRAY: UShort = 0x8c1a.toUShort
  val GL_PROXY_TEXTURE_2D_ARRAY: UShort = 0x8c1b.toUShort
  val GL_TEXTURE_BINDING_1D_ARRAY: UShort = 0x8c1c.toUShort
  val GL_TEXTURE_BINDING_2D_ARRAY: UShort = 0x8c1d.toUShort
  val GL_R11F_G11F_B10F: UShort = 0x8c3a.toUShort
  val GL_UNSIGNED_INT_10F_11F_11F_REV: UShort = 0x8c3b.toUShort
  val GL_RGB9_E5: UShort = 0x8c3d.toUShort
  val GL_UNSIGNED_INT_5_9_9_9_REV: UShort = 0x8c3e.toUShort
  val GL_TEXTURE_SHARED_SIZE: UShort = 0x8c3f.toUShort
  val GL_TRANSFORM_FEEDBACK_VARYING_MAX_LENGTH: UShort = 0x8c76.toUShort
  val GL_TRANSFORM_FEEDBACK_BUFFER_MODE: UShort = 0x8c7f.toUShort
  val GL_MAX_TRANSFORM_FEEDBACK_SEPARATE_COMPONENTS: UShort = 0x8c80.toUShort
  val GL_TRANSFORM_FEEDBACK_VARYINGS: UShort = 0x8c83.toUShort
  val GL_TRANSFORM_FEEDBACK_BUFFER_START: UShort = 0x8c84.toUShort
  val GL_TRANSFORM_FEEDBACK_BUFFER_SIZE: UShort = 0x8c85.toUShort
  val GL_PRIMITIVES_GENERATED: UShort = 0x8c87.toUShort
  val GL_TRANSFORM_FEEDBACK_PRIMITIVES_WRITTEN: UShort = 0x8c88.toUShort
  val GL_RASTERIZER_DISCARD: UShort = 0x8c89.toUShort
  val GL_MAX_TRANSFORM_FEEDBACK_INTERLEAVED_COMPONENTS: UShort = 0x8c8a.toUShort
  val GL_MAX_TRANSFORM_FEEDBACK_SEPARATE_ATTRIBS: UShort = 0x8c8b.toUShort
  val GL_INTERLEAVED_ATTRIBS: UShort = 0x8c8c.toUShort
  val GL_SEPARATE_ATTRIBS: UShort = 0x8c8d.toUShort
  val GL_TRANSFORM_FEEDBACK_BUFFER: UShort = 0x8c8e.toUShort
  val GL_TRANSFORM_FEEDBACK_BUFFER_BINDING: UShort = 0x8c8f.toUShort
  val GL_RGBA32UI: UShort = 0x8d70.toUShort
  val GL_RGB32UI: UShort = 0x8d71.toUShort
  val GL_RGBA16UI: UShort = 0x8d76.toUShort
  val GL_RGB16UI: UShort = 0x8d77.toUShort
  val GL_RGBA8UI: UShort = 0x8d7c.toUShort
  val GL_RGB8UI: UShort = 0x8d7d.toUShort
  val GL_RGBA32I: UShort = 0x8d82.toUShort
  val GL_RGB32I: UShort = 0x8d83.toUShort
  val GL_RGBA16I: UShort = 0x8d88.toUShort
  val GL_RGB16I: UShort = 0x8d89.toUShort
  val GL_RGBA8I: UShort = 0x8d8e.toUShort
  val GL_RGB8I: UShort = 0x8d8f.toUShort
  val GL_RED_INTEGER: UShort = 0x8d94.toUShort
  val GL_GREEN_INTEGER: UShort = 0x8d95.toUShort
  val GL_BLUE_INTEGER: UShort = 0x8d96.toUShort
  val GL_RGB_INTEGER: UShort = 0x8d98.toUShort
  val GL_RGBA_INTEGER: UShort = 0x8d99.toUShort
  val GL_BGR_INTEGER: UShort = 0x8d9a.toUShort
  val GL_BGRA_INTEGER: UShort = 0x8d9b.toUShort
  val GL_SAMPLER_1D_ARRAY: UShort = 0x8dc0.toUShort
  val GL_SAMPLER_2D_ARRAY: UShort = 0x8dc1.toUShort
  val GL_SAMPLER_1D_ARRAY_SHADOW: UShort = 0x8dc3.toUShort
  val GL_SAMPLER_2D_ARRAY_SHADOW: UShort = 0x8dc4.toUShort
  val GL_SAMPLER_CUBE_SHADOW: UShort = 0x8dc5.toUShort
  val GL_UNSIGNED_INT_VEC2: UShort = 0x8dc6.toUShort
  val GL_UNSIGNED_INT_VEC3: UShort = 0x8dc7.toUShort
  val GL_UNSIGNED_INT_VEC4: UShort = 0x8dc8.toUShort
  val GL_INT_SAMPLER_1D: UShort = 0x8dc9.toUShort
  val GL_INT_SAMPLER_2D: UShort = 0x8dca.toUShort
  val GL_INT_SAMPLER_3D: UShort = 0x8dcb.toUShort
  val GL_INT_SAMPLER_CUBE: UShort = 0x8dcc.toUShort
  val GL_INT_SAMPLER_1D_ARRAY: UShort = 0x8dce.toUShort
  val GL_INT_SAMPLER_2D_ARRAY: UShort = 0x8dcf.toUShort
  val GL_UNSIGNED_INT_SAMPLER_1D: UShort = 0x8dd1.toUShort
  val GL_UNSIGNED_INT_SAMPLER_2D: UShort = 0x8dd2.toUShort
  val GL_UNSIGNED_INT_SAMPLER_3D: UShort = 0x8dd3.toUShort
  val GL_UNSIGNED_INT_SAMPLER_CUBE: UShort = 0x8dd4.toUShort
  val GL_UNSIGNED_INT_SAMPLER_1D_ARRAY: UShort = 0x8dd6.toUShort
  val GL_UNSIGNED_INT_SAMPLER_2D_ARRAY: UShort = 0x8dd7.toUShort
  val GL_QUERY_WAIT: UShort = 0x8e13.toUShort
  val GL_QUERY_NO_WAIT: UShort = 0x8e14.toUShort
  val GL_QUERY_BY_REGION_WAIT: UShort = 0x8e15.toUShort
  val GL_QUERY_BY_REGION_NO_WAIT: UShort = 0x8e16.toUShort
  val GL_BUFFER_ACCESS_FLAGS: UShort = 0x911f.toUShort
  val GL_BUFFER_MAP_LENGTH: UShort = 0x9120.toUShort
  val GL_BUFFER_MAP_OFFSET: UShort = 0x9121.toUShort
  val GL_DEPTH_COMPONENT32F: UShort = 0x8cac.toUShort
  val GL_DEPTH32F_STENCIL8: UShort = 0x8cad.toUShort
  val GL_FLOAT_32_UNSIGNED_INT_24_8_REV: UShort = 0x8dad.toUShort
  val GL_INVALID_FRAMEBUFFER_OPERATION: UShort = 0x0506.toUShort
  val GL_FRAMEBUFFER_ATTACHMENT_COLOR_ENCODING: UShort = 0x8210.toUShort
  val GL_FRAMEBUFFER_ATTACHMENT_COMPONENT_TYPE: UShort = 0x8211.toUShort
  val GL_FRAMEBUFFER_ATTACHMENT_RED_SIZE: UShort = 0x8212.toUShort
  val GL_FRAMEBUFFER_ATTACHMENT_GREEN_SIZE: UShort = 0x8213.toUShort
  val GL_FRAMEBUFFER_ATTACHMENT_BLUE_SIZE: UShort = 0x8214.toUShort
  val GL_FRAMEBUFFER_ATTACHMENT_ALPHA_SIZE: UShort = 0x8215.toUShort
  val GL_FRAMEBUFFER_ATTACHMENT_DEPTH_SIZE: UShort = 0x8216.toUShort
  val GL_FRAMEBUFFER_ATTACHMENT_STENCIL_SIZE: UShort = 0x8217.toUShort
  val GL_FRAMEBUFFER_DEFAULT: UShort = 0x8218.toUShort
  val GL_FRAMEBUFFER_UNDEFINED: UShort = 0x8219.toUShort
  val GL_DEPTH_STENCIL_ATTACHMENT: UShort = 0x821a.toUShort
  val GL_MAX_RENDERBUFFER_SIZE: UShort = 0x84e8.toUShort
  val GL_DEPTH_STENCIL: UShort = 0x84f9.toUShort
  val GL_UNSIGNED_INT_24_8: UShort = 0x84fa.toUShort
  val GL_DEPTH24_STENCIL8: UShort = 0x88f0.toUShort
  val GL_TEXTURE_STENCIL_SIZE: UShort = 0x88f1.toUShort
  val GL_TEXTURE_RED_TYPE: UShort = 0x8c10.toUShort
  val GL_TEXTURE_GREEN_TYPE: UShort = 0x8c11.toUShort
  val GL_TEXTURE_BLUE_TYPE: UShort = 0x8c12.toUShort
  val GL_TEXTURE_ALPHA_TYPE: UShort = 0x8c13.toUShort
  val GL_TEXTURE_DEPTH_TYPE: UShort = 0x8c16.toUShort
  val GL_UNSIGNED_NORMALIZED: UShort = 0x8c17.toUShort
  val GL_FRAMEBUFFER_BINDING: UShort = 0x8ca6.toUShort
  val GL_DRAW_FRAMEBUFFER_BINDING: UShort = 0x8ca6.toUShort
  val GL_RENDERBUFFER_BINDING: UShort = 0x8ca7.toUShort
  val GL_READ_FRAMEBUFFER: UShort = 0x8ca8.toUShort
  val GL_DRAW_FRAMEBUFFER: UShort = 0x8ca9.toUShort
  val GL_READ_FRAMEBUFFER_BINDING: UShort = 0x8caa.toUShort
  val GL_RENDERBUFFER_SAMPLES: UShort = 0x8cab.toUShort
  val GL_FRAMEBUFFER_ATTACHMENT_OBJECT_TYPE: UShort = 0x8cd0.toUShort
  val GL_FRAMEBUFFER_ATTACHMENT_OBJECT_NAME: UShort = 0x8cd1.toUShort
  val GL_FRAMEBUFFER_ATTACHMENT_TEXTURE_LEVEL: UShort = 0x8cd2.toUShort
  val GL_FRAMEBUFFER_ATTACHMENT_TEXTURE_CUBE_MAP_FACE: UShort = 0x8cd3.toUShort
  val GL_FRAMEBUFFER_ATTACHMENT_TEXTURE_LAYER: UShort = 0x8cd4.toUShort
  val GL_FRAMEBUFFER_COMPLETE: UShort = 0x8cd5.toUShort
  val GL_FRAMEBUFFER_INCOMPLETE_ATTACHMENT: UShort = 0x8cd6.toUShort
  val GL_FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT: UShort = 0x8cd7.toUShort
  val GL_FRAMEBUFFER_INCOMPLETE_DRAW_BUFFER: UShort = 0x8cdb.toUShort
  val GL_FRAMEBUFFER_INCOMPLETE_READ_BUFFER: UShort = 0x8cdc.toUShort
  val GL_FRAMEBUFFER_UNSUPPORTED: UShort = 0x8cdd.toUShort
  val GL_MAX_COLOR_ATTACHMENTS: UShort = 0x8cdf.toUShort
  val GL_COLOR_ATTACHMENT0: UShort = 0x8ce0.toUShort
  val GL_COLOR_ATTACHMENT1: UShort = 0x8ce1.toUShort
  val GL_COLOR_ATTACHMENT2: UShort = 0x8ce2.toUShort
  val GL_COLOR_ATTACHMENT3: UShort = 0x8ce3.toUShort
  val GL_COLOR_ATTACHMENT4: UShort = 0x8ce4.toUShort
  val GL_COLOR_ATTACHMENT5: UShort = 0x8ce5.toUShort
  val GL_COLOR_ATTACHMENT6: UShort = 0x8ce6.toUShort
  val GL_COLOR_ATTACHMENT7: UShort = 0x8ce7.toUShort
  val GL_COLOR_ATTACHMENT8: UShort = 0x8ce8.toUShort
  val GL_COLOR_ATTACHMENT9: UShort = 0x8ce9.toUShort
  val GL_COLOR_ATTACHMENT10: UShort = 0x8cea.toUShort
  val GL_COLOR_ATTACHMENT11: UShort = 0x8ceb.toUShort
  val GL_COLOR_ATTACHMENT12: UShort = 0x8cec.toUShort
  val GL_COLOR_ATTACHMENT13: UShort = 0x8ced.toUShort
  val GL_COLOR_ATTACHMENT14: UShort = 0x8cee.toUShort
  val GL_COLOR_ATTACHMENT15: UShort = 0x8cef.toUShort
  val GL_DEPTH_ATTACHMENT: UShort = 0x8d00.toUShort
  val GL_STENCIL_ATTACHMENT: UShort = 0x8d20.toUShort
  val GL_FRAMEBUFFER: UShort = 0x8d40.toUShort
  val GL_RENDERBUFFER: UShort = 0x8d41.toUShort
  val GL_RENDERBUFFER_WIDTH: UShort = 0x8d42.toUShort
  val GL_RENDERBUFFER_HEIGHT: UShort = 0x8d43.toUShort
  val GL_RENDERBUFFER_INTERNAL_FORMAT: UShort = 0x8d44.toUShort
  val GL_STENCIL_INDEX1: UShort = 0x8d46.toUShort
  val GL_STENCIL_INDEX4: UShort = 0x8d47.toUShort
  val GL_STENCIL_INDEX8: UShort = 0x8d48.toUShort
  val GL_STENCIL_INDEX16: UShort = 0x8d49.toUShort
  val GL_RENDERBUFFER_RED_SIZE: UShort = 0x8d50.toUShort
  val GL_RENDERBUFFER_GREEN_SIZE: UShort = 0x8d51.toUShort
  val GL_RENDERBUFFER_BLUE_SIZE: UShort = 0x8d52.toUShort
  val GL_RENDERBUFFER_ALPHA_SIZE: UShort = 0x8d53.toUShort
  val GL_RENDERBUFFER_DEPTH_SIZE: UShort = 0x8d54.toUShort
  val GL_RENDERBUFFER_STENCIL_SIZE: UShort = 0x8d55.toUShort
  val GL_FRAMEBUFFER_INCOMPLETE_MULTISAMPLE: UShort = 0x8d56.toUShort
  val GL_MAX_SAMPLES: UShort = 0x8d57.toUShort
  val GL_INDEX: UShort = 0x8222.toUShort
  val GL_TEXTURE_LUMINANCE_TYPE: UShort = 0x8c14.toUShort
  val GL_TEXTURE_INTENSITY_TYPE: UShort = 0x8c15.toUShort
  val GL_FRAMEBUFFER_SRGB: UShort = 0x8db9.toUShort
  val GL_HALF_FLOAT: UShort = 0x140b.toUShort
  val GL_MAP_READ_BIT: UShort = 0x0001.toUShort
  val GL_MAP_WRITE_BIT: UShort = 0x0002.toUShort
  val GL_MAP_INVALIDATE_RANGE_BIT: UShort = 0x0004.toUShort
  val GL_MAP_INVALIDATE_BUFFER_BIT: UShort = 0x0008.toUShort
  val GL_MAP_FLUSH_EXPLICIT_BIT: UShort = 0x0010.toUShort
  val GL_MAP_UNSYNCHRONIZED_BIT: UShort = 0x0020.toUShort
  val GL_COMPRESSED_RED_RGTC1: UShort = 0x8dbb.toUShort
  val GL_COMPRESSED_SIGNED_RED_RGTC1: UShort = 0x8dbc.toUShort
  val GL_COMPRESSED_RG_RGTC2: UShort = 0x8dbd.toUShort
  val GL_COMPRESSED_SIGNED_RG_RGTC2: UShort = 0x8dbe.toUShort
  val GL_RG: UShort = 0x8227.toUShort
  val GL_RG_INTEGER: UShort = 0x8228.toUShort
  val GL_R8: UShort = 0x8229.toUShort
  val GL_R16: UShort = 0x822a.toUShort
  val GL_RG8: UShort = 0x822b.toUShort
  val GL_RG16: UShort = 0x822c.toUShort
  val GL_R16F: UShort = 0x822d.toUShort
  val GL_R32F: UShort = 0x822e.toUShort
  val GL_RG16F: UShort = 0x822f.toUShort
  val GL_RG32F: UShort = 0x8230.toUShort
  val GL_R8I: UShort = 0x8231.toUShort
  val GL_R8UI: UShort = 0x8232.toUShort
  val GL_R16I: UShort = 0x8233.toUShort
  val GL_R16UI: UShort = 0x8234.toUShort
  val GL_R32I: UShort = 0x8235.toUShort
  val GL_R32UI: UShort = 0x8236.toUShort
  val GL_RG8I: UShort = 0x8237.toUShort
  val GL_RG8UI: UShort = 0x8238.toUShort
  val GL_RG16I: UShort = 0x8239.toUShort
  val GL_RG16UI: UShort = 0x823a.toUShort
  val GL_RG32I: UShort = 0x823b.toUShort
  val GL_RG32UI: UShort = 0x823c.toUShort
  val GL_VERTEX_ARRAY_BINDING: UShort = 0x85b5.toUShort
  val GL_CLAMP_VERTEX_COLOR: UShort = 0x891a.toUShort
  val GL_CLAMP_FRAGMENT_COLOR: UShort = 0x891b.toUShort
  val GL_ALPHA_INTEGER: UShort = 0x8d97.toUShort
  /*
   * End OpenGL 3.0
   */

  /*
   * OpenGL 3.1
   */
  val GL_SAMPLER_2D_RECT: UShort = 0x8b63.toUShort
  val GL_SAMPLER_2D_RECT_SHADOW: UShort = 0x8b64.toUShort
  val GL_SAMPLER_BUFFER: UShort = 0x8dc2.toUShort
  val GL_INT_SAMPLER_2D_RECT: UShort = 0x8dcd.toUShort
  val GL_INT_SAMPLER_BUFFER: UShort = 0x8dd0.toUShort
  val GL_UNSIGNED_INT_SAMPLER_2D_RECT: UShort = 0x8dd5.toUShort
  val GL_UNSIGNED_INT_SAMPLER_BUFFER: UShort = 0x8dd8.toUShort
  val GL_TEXTURE_BUFFER: UShort = 0x8c2a.toUShort
  val GL_MAX_TEXTURE_BUFFER_SIZE: UShort = 0x8c2b.toUShort
  val GL_TEXTURE_BINDING_BUFFER: UShort = 0x8c2c.toUShort
  val GL_TEXTURE_BUFFER_DATA_STORE_BINDING: UShort = 0x8c2d.toUShort
  val GL_TEXTURE_RECTANGLE: UShort = 0x84f5.toUShort
  val GL_TEXTURE_BINDING_RECTANGLE: UShort = 0x84f6.toUShort
  val GL_PROXY_TEXTURE_RECTANGLE: UShort = 0x84f7.toUShort
  val GL_MAX_RECTANGLE_TEXTURE_SIZE: UShort = 0x84f8.toUShort
  val GL_R8_SNORM: UShort = 0x8f94.toUShort
  val GL_RG8_SNORM: UShort = 0x8f95.toUShort
  val GL_RGB8_SNORM: UShort = 0x8f96.toUShort
  val GL_RGBA8_SNORM: UShort = 0x8f97.toUShort
  val GL_R16_SNORM: UShort = 0x8f98.toUShort
  val GL_RG16_SNORM: UShort = 0x8f99.toUShort
  val GL_RGB16_SNORM: UShort = 0x8f9a.toUShort
  val GL_RGBA16_SNORM: UShort = 0x8f9b.toUShort
  val GL_SIGNED_NORMALIZED: UShort = 0x8f9c.toUShort
  val GL_PRIMITIVE_RESTART: UShort = 0x8f9d.toUShort
  val GL_PRIMITIVE_RESTART_INDEX: UShort = 0x8f9e.toUShort
  val GL_COPY_READ_BUFFER: UShort = 0x8f36.toUShort
  val GL_COPY_WRITE_BUFFER: UShort = 0x8f37.toUShort
  val GL_UNIFORM_BUFFER: UShort = 0x8a11.toUShort
  val GL_UNIFORM_BUFFER_BINDING: UShort = 0x8a28.toUShort
  val GL_UNIFORM_BUFFER_START: UShort = 0x8a29.toUShort
  val GL_UNIFORM_BUFFER_SIZE: UShort = 0x8a2a.toUShort
  val GL_MAX_VERTEX_UNIFORM_BLOCKS: UShort = 0x8a2b.toUShort
  val GL_MAX_FRAGMENT_UNIFORM_BLOCKS: UShort = 0x8a2d.toUShort
  val GL_MAX_COMBINED_UNIFORM_BLOCKS: UShort = 0x8a2e.toUShort
  val GL_MAX_UNIFORM_BUFFER_BINDINGS: UShort = 0x8a2f.toUShort
  val GL_MAX_UNIFORM_BLOCK_SIZE: UShort = 0x8a30.toUShort
  val GL_MAX_COMBINED_VERTEX_UNIFORM_COMPONENTS: UShort = 0x8a31.toUShort
  val GL_MAX_COMBINED_FRAGMENT_UNIFORM_COMPONENTS: UShort = 0x8a33.toUShort
  val GL_UNIFORM_BUFFER_OFFSET_ALIGNMENT: UShort = 0x8a34.toUShort
  val GL_ACTIVE_UNIFORM_BLOCK_MAX_NAME_LENGTH: UShort = 0x8a35.toUShort
  val GL_ACTIVE_UNIFORM_BLOCKS: UShort = 0x8a36.toUShort
  val GL_UNIFORM_TYPE: UShort = 0x8a37.toUShort
  val GL_UNIFORM_SIZE: UShort = 0x8a38.toUShort
  val GL_UNIFORM_NAME_LENGTH: UShort = 0x8a39.toUShort
  val GL_UNIFORM_BLOCK_INDEX: UShort = 0x8a3a.toUShort
  val GL_UNIFORM_OFFSET: UShort = 0x8a3b.toUShort
  val GL_UNIFORM_ARRAY_STRIDE: UShort = 0x8a3c.toUShort
  val GL_UNIFORM_MATRIX_STRIDE: UShort = 0x8a3d.toUShort
  val GL_UNIFORM_IS_ROW_MAJOR: UShort = 0x8a3e.toUShort
  val GL_UNIFORM_BLOCK_BINDING: UShort = 0x8a3f.toUShort
  val GL_UNIFORM_BLOCK_DATA_SIZE: UShort = 0x8a40.toUShort
  val GL_UNIFORM_BLOCK_NAME_LENGTH: UShort = 0x8a41.toUShort
  val GL_UNIFORM_BLOCK_ACTIVE_UNIFORMS: UShort = 0x8a42.toUShort
  val GL_UNIFORM_BLOCK_ACTIVE_UNIFORM_INDICES: UShort = 0x8a43.toUShort
  val GL_UNIFORM_BLOCK_REFERENCED_BY_VERTEX_SHADER: UShort = 0x8a44.toUShort
  val GL_UNIFORM_BLOCK_REFERENCED_BY_FRAGMENT_SHADER: UShort = 0x8a46.toUShort
  val GL_INVALID_INDEX: UInt = 0xffffffff.toUInt
  /*
   * End OpenGL 3.1
   */

  /*
   * OpenGL 3.2
   */
  val GL_CONTEXT_CORE_PROFILE_BIT: UInt = 0x00000001.toUInt
  val GL_CONTEXT_COMPATIBILITY_PROFILE_BIT: UInt = 0x00000002.toUInt
  val GL_LINES_ADJACENCY: UShort = 0x000a.toUShort
  val GL_LINE_STRIP_ADJACENCY: UShort = 0x000b.toUShort
  val GL_TRIANGLES_ADJACENCY: UShort = 0x000c.toUShort
  val GL_TRIANGLE_STRIP_ADJACENCY: UShort = 0x000d.toUShort
  val GL_PROGRAM_POINT_SIZE: UShort = 0x8642.toUShort
  val GL_MAX_GEOMETRY_TEXTURE_IMAGE_UNITS: UShort = 0x8c29.toUShort
  val GL_FRAMEBUFFER_ATTACHMENT_LAYERED: UShort = 0x8da7.toUShort
  val GL_FRAMEBUFFER_INCOMPLETE_LAYER_TARGETS: UShort = 0x8da8.toUShort
  val GL_GEOMETRY_SHADER: UShort = 0x8dd9.toUShort
  val GL_GEOMETRY_VERTICES_OUT: UShort = 0x8916.toUShort
  val GL_GEOMETRY_INPUT_TYPE: UShort = 0x8917.toUShort
  val GL_GEOMETRY_OUTPUT_TYPE: UShort = 0x8918.toUShort
  val GL_MAX_GEOMETRY_UNIFORM_COMPONENTS: UShort = 0x8ddf.toUShort
  val GL_MAX_GEOMETRY_OUTPUT_VERTICES: UShort = 0x8de0.toUShort
  val GL_MAX_GEOMETRY_TOTAL_OUTPUT_COMPONENTS: UShort = 0x8de1.toUShort
  val GL_MAX_VERTEX_OUTPUT_COMPONENTS: UShort = 0x9122.toUShort
  val GL_MAX_GEOMETRY_INPUT_COMPONENTS: UShort = 0x9123.toUShort
  val GL_MAX_GEOMETRY_OUTPUT_COMPONENTS: UShort = 0x9124.toUShort
  val GL_MAX_FRAGMENT_INPUT_COMPONENTS: UShort = 0x9125.toUShort
  val GL_CONTEXT_PROFILE_MASK: UShort = 0x9126.toUShort
  val GL_DEPTH_CLAMP: UShort = 0x864f.toUShort
  val GL_QUADS_FOLLOW_PROVOKING_VERTEX_CONVENTION: UShort = 0x8e4c.toUShort
  val GL_FIRST_VERTEX_CONVENTION: UShort = 0x8e4d.toUShort
  val GL_LAST_VERTEX_CONVENTION: UShort = 0x8e4e.toUShort
  val GL_PROVOKING_VERTEX: UShort = 0x8e4f.toUShort
  val GL_TEXTURE_CUBE_MAP_SEAMLESS: UShort = 0x884f.toUShort
  val GL_MAX_SERVER_WAIT_TIMEOUT: UShort = 0x9111.toUShort
  val GL_OBJECT_TYPE: UShort = 0x9112.toUShort
  val GL_SYNC_CONDITION: UShort = 0x9113.toUShort
  val GL_SYNC_STATUS: UShort = 0x9114.toUShort
  val GL_SYNC_FLAGS: UShort = 0x9115.toUShort
  val GL_SYNC_FENCE: UShort = 0x9116.toUShort
  val GL_SYNC_GPU_COMMANDS_COMPLETE: UShort = 0x9117.toUShort
  val GL_UNSIGNALED: UShort = 0x9118.toUShort
  val GL_SIGNALED: UShort = 0x9119.toUShort
  val GL_ALREADY_SIGNALED: UShort = 0x911a.toUShort
  val GL_TIMEOUT_EXPIRED: UShort = 0x911b.toUShort
  val GL_CONDITION_SATISFIED: UShort = 0x911c.toUShort
  val GL_WAIT_FAILED: UShort = 0x911d.toUShort
  val GL_TIMEOUT_IGNORED: ULong = 0xffffffffffffffffL.toULong
  val GL_SYNC_FLUSH_COMMANDS_BIT: UInt = 0x00000001.toUInt
  val GL_SAMPLE_POSITION: UShort = 0x8e50.toUShort
  val GL_SAMPLE_MASK: UShort = 0x8e51.toUShort
  val GL_SAMPLE_MASK_VALUE: UShort = 0x8e52.toUShort
  val GL_MAX_SAMPLE_MASK_WORDS: UShort = 0x8e59.toUShort
  val GL_TEXTURE_2D_MULTISAMPLE: UShort = 0x9100.toUShort
  val GL_PROXY_TEXTURE_2D_MULTISAMPLE: UShort = 0x9101.toUShort
  val GL_TEXTURE_2D_MULTISAMPLE_ARRAY: UShort = 0x9102.toUShort
  val GL_PROXY_TEXTURE_2D_MULTISAMPLE_ARRAY: UShort = 0x9103.toUShort
  val GL_TEXTURE_BINDING_2D_MULTISAMPLE: UShort = 0x9104.toUShort
  val GL_TEXTURE_BINDING_2D_MULTISAMPLE_ARRAY: UShort = 0x9105.toUShort
  val GL_TEXTURE_SAMPLES: UShort = 0x9106.toUShort
  val GL_TEXTURE_FIXED_SAMPLE_LOCATIONS: UShort = 0x9107.toUShort
  val GL_SAMPLER_2D_MULTISAMPLE: UShort = 0x9108.toUShort
  val GL_INT_SAMPLER_2D_MULTISAMPLE: UShort = 0x9109.toUShort
  val GL_UNSIGNED_INT_SAMPLER_2D_MULTISAMPLE: UShort = 0x910a.toUShort
  val GL_SAMPLER_2D_MULTISAMPLE_ARRAY: UShort = 0x910b.toUShort
  val GL_INT_SAMPLER_2D_MULTISAMPLE_ARRAY: UShort = 0x910c.toUShort
  val GL_UNSIGNED_INT_SAMPLER_2D_MULTISAMPLE_ARRAY: UShort = 0x910d.toUShort
  val GL_MAX_COLOR_TEXTURE_SAMPLES: UShort = 0x910e.toUShort
  val GL_MAX_DEPTH_TEXTURE_SAMPLES: UShort = 0x910f.toUShort
  val GL_MAX_INTEGER_SAMPLES: UShort = 0x9110.toUShort
  /*
   * End OpenGL 3.2
   */

  /*
   * OpenGL 3.3
   */
  val GL_VERTEX_ATTRIB_ARRAY_DIVISOR: UShort = 0x88fe.toUShort
  val GL_SRC1_COLOR: UShort = 0x88f9.toUShort
  val GL_ONE_MINUS_SRC1_COLOR: UShort = 0x88fa.toUShort
  val GL_ONE_MINUS_SRC1_ALPHA: UShort = 0x88fb.toUShort
  val GL_MAX_DUAL_SOURCE_DRAW_BUFFERS: UShort = 0x88fc.toUShort
  val GL_ANY_SAMPLES_PASSED: UShort = 0x8c2f.toUShort
  val GL_SAMPLER_BINDING: UShort = 0x8919.toUShort
  val GL_RGB10_A2UI: UShort = 0x906f.toUShort
  val GL_TEXTURE_SWIZZLE_R: UShort = 0x8e42.toUShort
  val GL_TEXTURE_SWIZZLE_G: UShort = 0x8e43.toUShort
  val GL_TEXTURE_SWIZZLE_B: UShort = 0x8e44.toUShort
  val GL_TEXTURE_SWIZZLE_A: UShort = 0x8e45.toUShort
  val GL_TEXTURE_SWIZZLE_RGBA: UShort = 0x8e46.toUShort
  val GL_TIME_ELAPSED: UShort = 0x88bf.toUShort
  val GL_TIMESTAMP: UShort = 0x8e28.toUShort
  val GL_INT_2_10_10_10_REV: UShort = 0x8d9f.toUShort
  /*
   * End OpenGL 3.3
   */

  /*
   * OpenGL 4.0
   */
  val GL_SAMPLE_SHADING: UShort = 0x8c36.toUShort
  val GL_MIN_SAMPLE_SHADING_VALUE: UShort = 0x8c37.toUShort
  val GL_MIN_PROGRAM_TEXTURE_GATHER_OFFSET: UShort = 0x8e5e.toUShort
  val GL_MAX_PROGRAM_TEXTURE_GATHER_OFFSET: UShort = 0x8e5f.toUShort
  val GL_TEXTURE_CUBE_MAP_ARRAY: UShort = 0x9009.toUShort
  val GL_TEXTURE_BINDING_CUBE_MAP_ARRAY: UShort = 0x900a.toUShort
  val GL_PROXY_TEXTURE_CUBE_MAP_ARRAY: UShort = 0x900b.toUShort
  val GL_SAMPLER_CUBE_MAP_ARRAY: UShort = 0x900c.toUShort
  val GL_SAMPLER_CUBE_MAP_ARRAY_SHADOW: UShort = 0x900d.toUShort
  val GL_INT_SAMPLER_CUBE_MAP_ARRAY: UShort = 0x900e.toUShort
  val GL_UNSIGNED_INT_SAMPLER_CUBE_MAP_ARRAY: UShort = 0x900f.toUShort
  val GL_DRAW_INDIRECT_BUFFER: UShort = 0x8f3f.toUShort
  val GL_DRAW_INDIRECT_BUFFER_BINDING: UShort = 0x8f43.toUShort
  val GL_GEOMETRY_SHADER_INVOCATIONS: UShort = 0x887f.toUShort
  val GL_MAX_GEOMETRY_SHADER_INVOCATIONS: UShort = 0x8e5a.toUShort
  val GL_MIN_FRAGMENT_INTERPOLATION_OFFSET: UShort = 0x8e5b.toUShort
  val GL_MAX_FRAGMENT_INTERPOLATION_OFFSET: UShort = 0x8e5c.toUShort
  val GL_FRAGMENT_INTERPOLATION_OFFSET_BITS: UShort = 0x8e5d.toUShort
  val GL_MAX_VERTEX_STREAMS: UShort = 0x8e71.toUShort
  val GL_DOUBLE_VEC2: UShort = 0x8ffc.toUShort
  val GL_DOUBLE_VEC3: UShort = 0x8ffd.toUShort
  val GL_DOUBLE_VEC4: UShort = 0x8ffe.toUShort
  val GL_DOUBLE_MAT2: UShort = 0x8f46.toUShort
  val GL_DOUBLE_MAT3: UShort = 0x8f47.toUShort
  val GL_DOUBLE_MAT4: UShort = 0x8f48.toUShort
  val GL_DOUBLE_MAT2x3: UShort = 0x8f49.toUShort
  val GL_DOUBLE_MAT2x4: UShort = 0x8f4a.toUShort
  val GL_DOUBLE_MAT3x2: UShort = 0x8f4b.toUShort
  val GL_DOUBLE_MAT3x4: UShort = 0x8f4c.toUShort
  val GL_DOUBLE_MAT4x2: UShort = 0x8f4d.toUShort
  val GL_DOUBLE_MAT4x3: UShort = 0x8f4e.toUShort
  val GL_ACTIVE_SUBROUTINES: UShort = 0x8de5.toUShort
  val GL_ACTIVE_SUBROUTINE_UNIFORMS: UShort = 0x8de6.toUShort
  val GL_ACTIVE_SUBROUTINE_UNIFORM_LOCATIONS: UShort = 0x8e47.toUShort
  val GL_ACTIVE_SUBROUTINE_MAX_LENGTH: UShort = 0x8e48.toUShort
  val GL_ACTIVE_SUBROUTINE_UNIFORM_MAX_LENGTH: UShort = 0x8e49.toUShort
  val GL_MAX_SUBROUTINES: UShort = 0x8de7.toUShort
  val GL_MAX_SUBROUTINE_UNIFORM_LOCATIONS: UShort = 0x8de8.toUShort
  val GL_NUM_COMPATIBLE_SUBROUTINES: UShort = 0x8e4a.toUShort
  val GL_COMPATIBLE_SUBROUTINES: UShort = 0x8e4b.toUShort
  val GL_PATCHES: UShort = 0x000e.toUShort
  val GL_PATCH_VERTICES: UShort = 0x8e72.toUShort
  val GL_PATCH_DEFAULT_INNER_LEVEL: UShort = 0x8e73.toUShort
  val GL_PATCH_DEFAULT_OUTER_LEVEL: UShort = 0x8e74.toUShort
  val GL_TESS_CONTROL_OUTPUT_VERTICES: UShort = 0x8e75.toUShort
  val GL_TESS_GEN_MODE: UShort = 0x8e76.toUShort
  val GL_TESS_GEN_SPACING: UShort = 0x8e77.toUShort
  val GL_TESS_GEN_VERTEX_ORDER: UShort = 0x8e78.toUShort
  val GL_TESS_GEN_POINT_MODE: UShort = 0x8e79.toUShort
  val GL_ISOLINES: UShort = 0x8e7a.toUShort
  val GL_FRACTIONAL_ODD: UShort = 0x8e7b.toUShort
  val GL_FRACTIONAL_EVEN: UShort = 0x8e7c.toUShort
  val GL_MAX_PATCH_VERTICES: UShort = 0x8e7d.toUShort
  val GL_MAX_TESS_GEN_LEVEL: UShort = 0x8e7e.toUShort
  val GL_MAX_TESS_CONTROL_UNIFORM_COMPONENTS: UShort = 0x8e7f.toUShort
  val GL_MAX_TESS_EVALUATION_UNIFORM_COMPONENTS: UShort = 0x8e80.toUShort
  val GL_MAX_TESS_CONTROL_TEXTURE_IMAGE_UNITS: UShort = 0x8e81.toUShort
  val GL_MAX_TESS_EVALUATION_TEXTURE_IMAGE_UNITS: UShort = 0x8e82.toUShort
  val GL_MAX_TESS_CONTROL_OUTPUT_COMPONENTS: UShort = 0x8e83.toUShort
  val GL_MAX_TESS_PATCH_COMPONENTS: UShort = 0x8e84.toUShort
  val GL_MAX_TESS_CONTROL_TOTAL_OUTPUT_COMPONENTS: UShort = 0x8e85.toUShort
  val GL_MAX_TESS_EVALUATION_OUTPUT_COMPONENTS: UShort = 0x8e86.toUShort
  val GL_MAX_TESS_CONTROL_UNIFORM_BLOCKS: UShort = 0x8e89.toUShort
  val GL_MAX_TESS_EVALUATION_UNIFORM_BLOCKS: UShort = 0x8e8a.toUShort
  val GL_MAX_TESS_CONTROL_INPUT_COMPONENTS: UShort = 0x886c.toUShort
  val GL_MAX_TESS_EVALUATION_INPUT_COMPONENTS: UShort = 0x886d.toUShort
  val GL_MAX_COMBINED_TESS_CONTROL_UNIFORM_COMPONENTS: UShort = 0x8e1e.toUShort
  val GL_MAX_COMBINED_TESS_EVALUATION_UNIFORM_COMPONENTS: UShort =
    0x8e1f.toUShort
  val GL_UNIFORM_BLOCK_REFERENCED_BY_TESS_CONTROL_SHADER: UShort =
    0x84f0.toUShort
  val GL_UNIFORM_BLOCK_REFERENCED_BY_TESS_EVALUATION_SHADER: UShort =
    0x84f1.toUShort
  val GL_TESS_EVALUATION_SHADER: UShort = 0x8e87.toUShort
  val GL_TESS_CONTROL_SHADER: UShort = 0x8e88.toUShort
  val GL_TRANSFORM_FEEDBACK: UShort = 0x8e22.toUShort
  val GL_TRANSFORM_FEEDBACK_BUFFER_PAUSED: UShort = 0x8e23.toUShort
  val GL_TRANSFORM_FEEDBACK_BUFFER_ACTIVE: UShort = 0x8e24.toUShort
  val GL_TRANSFORM_FEEDBACK_BINDING: UShort = 0x8e25.toUShort
  val GL_MAX_TRANSFORM_FEEDBACK_BUFFERS: UShort = 0x8e70.toUShort
  /*
   * End OpenGL 4.0
   */

  /*
   * OpenGL 4.1
   */
  val GL_FIXED: UInt = 0x140c.toUInt
  val GL_IMPLEMENTATION_COLOR_READ_TYPE: UInt = 0x8b9a.toUInt
  val GL_IMPLEMENTATION_COLOR_READ_FORMAT: UInt = 0x8b9b.toUInt
  val GL_LOW_FLOAT: UInt = 0x8df0.toUInt
  val GL_MEDIUM_FLOAT: UInt = 0x8df1.toUInt
  val GL_HIGH_FLOAT: UInt = 0x8df2.toUInt
  val GL_LOW_INT: UInt = 0x8df3.toUInt
  val GL_MEDIUM_INT: UInt = 0x8df4.toUInt
  val GL_HIGH_INT: UInt = 0x8df5.toUInt
  val GL_SHADER_COMPILER: UInt = 0x8dfa.toUInt
  val GL_SHADER_BINARY_FORMATS: UInt = 0x8df8.toUInt
  val GL_NUM_SHADER_BINARY_FORMATS: UInt = 0x8df9.toUInt
  val GL_MAX_VERTEX_UNIFORM_VECTORS: UInt = 0x8dfb.toUInt
  val GL_MAX_VARYING_VECTORS: UInt = 0x8dfc.toUInt
  val GL_MAX_FRAGMENT_UNIFORM_VECTORS: UInt = 0x8dfd.toUInt
  val GL_RGB565: UInt = 0x8d62.toUInt
  val GL_PROGRAM_BINARY_RETRIEVABLE_HINT: UInt = 0x8257.toUInt
  val GL_PROGRAM_BINARY_LENGTH: UInt = 0x8741.toUInt
  val GL_NUM_PROGRAM_BINARY_FORMATS: UInt = 0x87fe.toUInt
  val GL_PROGRAM_BINARY_FORMATS: UInt = 0x87ff.toUInt
  val GL_VERTEX_SHADER_BIT: UInt = 0x00000001.toUInt
  val GL_FRAGMENT_SHADER_BIT: UInt = 0x00000002.toUInt
  val GL_GEOMETRY_SHADER_BIT: UInt = 0x00000004.toUInt
  val GL_TESS_CONTROL_SHADER_BIT: UInt = 0x00000008.toUInt
  val GL_TESS_EVALUATION_SHADER_BIT: UInt = 0x00000010.toUInt
  val GL_ALL_SHADER_BITS: UInt = 0xffffffff.toUInt
  val GL_PROGRAM_SEPARABLE: UInt = 0x8258.toUInt
  val GL_ACTIVE_PROGRAM: UInt = 0x8259.toUInt
  val GL_PROGRAM_PIPELINE_BINDING: UInt = 0x825a.toUInt
  val GL_MAX_VIEWPORTS: UInt = 0x825b.toUInt
  val GL_VIEWPORT_SUBPIXEL_BITS: UInt = 0x825c.toUInt
  val GL_VIEWPORT_BOUNDS_RANGE: UInt = 0x825d.toUInt
  val GL_LAYER_PROVOKING_VERTEX: UInt = 0x825e.toUInt
  val GL_VIEWPORT_INDEX_PROVOKING_VERTEX: UInt = 0x825f.toUInt
  val GL_UNDEFINED_VERTEX: UInt = 0x8260.toUInt
  /*
   * End OpenGL 4.1
   */

  /*
   * OpenGL 4.2
   */
  val GL_COPY_READ_BUFFER_BINDING: UInt = 0x8f36.toUInt
  val GL_COPY_WRITE_BUFFER_BINDING: UInt = 0x8f37.toUInt
  val GL_TRANSFORM_FEEDBACK_ACTIVE: UInt = 0x8e24.toUInt
  val GL_TRANSFORM_FEEDBACK_PAUSED: UInt = 0x8e23.toUInt
  val GL_UNPACK_COMPRESSED_BLOCK_WIDTH: UInt = 0x9127.toUInt
  val GL_UNPACK_COMPRESSED_BLOCK_HEIGHT: UInt = 0x9128.toUInt
  val GL_UNPACK_COMPRESSED_BLOCK_DEPTH: UInt = 0x9129.toUInt
  val GL_UNPACK_COMPRESSED_BLOCK_SIZE: UInt = 0x912a.toUInt
  val GL_PACK_COMPRESSED_BLOCK_WIDTH: UInt = 0x912b.toUInt
  val GL_PACK_COMPRESSED_BLOCK_HEIGHT: UInt = 0x912c.toUInt
  val GL_PACK_COMPRESSED_BLOCK_DEPTH: UInt = 0x912d.toUInt
  val GL_PACK_COMPRESSED_BLOCK_SIZE: UInt = 0x912e.toUInt
  val GL_NUM_SAMPLE_COUNTS: UInt = 0x9380.toUInt
  val GL_MIN_MAP_BUFFER_ALIGNMENT: UInt = 0x90bc.toUInt
  val GL_ATOMIC_COUNTER_BUFFER: UInt = 0x92c0.toUInt
  val GL_ATOMIC_COUNTER_BUFFER_BINDING: UInt = 0x92c1.toUInt
  val GL_ATOMIC_COUNTER_BUFFER_START: UInt = 0x92c2.toUInt
  val GL_ATOMIC_COUNTER_BUFFER_SIZE: UInt = 0x92c3.toUInt
  val GL_ATOMIC_COUNTER_BUFFER_DATA_SIZE: UInt = 0x92c4.toUInt
  val GL_ATOMIC_COUNTER_BUFFER_ACTIVE_ATOMIC_COUNTERS: UInt = 0x92c5.toUInt
  val GL_ATOMIC_COUNTER_BUFFER_ACTIVE_ATOMIC_COUNTER_INDICES: UInt =
    0x92c6.toUInt
  val GL_ATOMIC_COUNTER_BUFFER_REFERENCED_BY_VERTEX_SHADER: UInt = 0x92c7.toUInt
  val GL_ATOMIC_COUNTER_BUFFER_REFERENCED_BY_TESS_CONTROL_SHADER: UInt =
    0x92c8.toUInt
  val GL_ATOMIC_COUNTER_BUFFER_REFERENCED_BY_TESS_EVALUATION_SHADER: UInt =
    0x92c9.toUInt
  val GL_ATOMIC_COUNTER_BUFFER_REFERENCED_BY_GEOMETRY_SHADER: UInt =
    0x92ca.toUInt
  val GL_ATOMIC_COUNTER_BUFFER_REFERENCED_BY_FRAGMENT_SHADER: UInt =
    0x92cb.toUInt
  val GL_MAX_VERTEX_ATOMIC_COUNTER_BUFFERS: UInt = 0x92cc.toUInt
  val GL_MAX_TESS_CONTROL_ATOMIC_COUNTER_BUFFERS: UInt = 0x92cd.toUInt
  val GL_MAX_TESS_EVALUATION_ATOMIC_COUNTER_BUFFERS: UInt = 0x92ce.toUInt
  val GL_MAX_GEOMETRY_ATOMIC_COUNTER_BUFFERS: UInt = 0x92cf.toUInt
  val GL_MAX_FRAGMENT_ATOMIC_COUNTER_BUFFERS: UInt = 0x92d0.toUInt
  val GL_MAX_COMBINED_ATOMIC_COUNTER_BUFFERS: UInt = 0x92d1.toUInt
  val GL_MAX_VERTEX_ATOMIC_COUNTERS: UInt = 0x92d2.toUInt
  val GL_MAX_TESS_CONTROL_ATOMIC_COUNTERS: UInt = 0x92d3.toUInt
  val GL_MAX_TESS_EVALUATION_ATOMIC_COUNTERS: UInt = 0x92d4.toUInt
  val GL_MAX_GEOMETRY_ATOMIC_COUNTERS: UInt = 0x92d5.toUInt
  val GL_MAX_FRAGMENT_ATOMIC_COUNTERS: UInt = 0x92d6.toUInt
  val GL_MAX_COMBINED_ATOMIC_COUNTERS: UInt = 0x92d7.toUInt
  val GL_MAX_ATOMIC_COUNTER_BUFFER_SIZE: UInt = 0x92d8.toUInt
  val GL_MAX_ATOMIC_COUNTER_BUFFER_BINDINGS: UInt = 0x92dc.toUInt
  val GL_ACTIVE_ATOMIC_COUNTER_BUFFERS: UInt = 0x92d9.toUInt
  val GL_UNIFORM_ATOMIC_COUNTER_BUFFER_INDEX: UInt = 0x92da.toUInt
  val GL_UNSIGNED_INT_ATOMIC_COUNTER: UInt = 0x92db.toUInt
  val GL_VERTEX_ATTRIB_ARRAY_BARRIER_BIT: ULong = 0x00000001.toULong
  val GL_ELEMENT_ARRAY_BARRIER_BIT: ULong = 0x00000002.toULong
  val GL_UNIFORM_BARRIER_BIT: ULong = 0x00000004.toULong
  val GL_TEXTURE_FETCH_BARRIER_BIT: ULong = 0x00000008.toULong
  val GL_SHADER_IMAGE_ACCESS_BARRIER_BIT: ULong = 0x00000020.toULong
  val GL_COMMAND_BARRIER_BIT: ULong = 0x00000040.toULong
  val GL_PIXEL_BUFFER_BARRIER_BIT: ULong = 0x00000080.toULong
  val GL_TEXTURE_UPDATE_BARRIER_BIT: ULong = 0x00000100.toULong
  val GL_BUFFER_UPDATE_BARRIER_BIT: ULong = 0x00000200.toULong
  val GL_FRAMEBUFFER_BARRIER_BIT: ULong = 0x00000400.toULong
  val GL_TRANSFORM_FEEDBACK_BARRIER_BIT: ULong = 0x00000800.toULong
  val GL_ATOMIC_COUNTER_BARRIER_BIT: ULong = 0x00001000.toULong
  val GL_ALL_BARRIER_BITS: ULong = 0xffffffff.toULong
  val GL_MAX_IMAGE_UNITS: UInt = 0x8f38.toUInt
  val GL_MAX_COMBINED_IMAGE_UNITS_AND_FRAGMENT_OUTPUTS: UInt = 0x8f39.toUInt
  val GL_IMAGE_BINDING_NAME: UInt = 0x8f3a.toUInt
  val GL_IMAGE_BINDING_LEVEL: UInt = 0x8f3b.toUInt
  val GL_IMAGE_BINDING_LAYERED: UInt = 0x8f3c.toUInt
  val GL_IMAGE_BINDING_LAYER: UInt = 0x8f3d.toUInt
  val GL_IMAGE_BINDING_ACCESS: UInt = 0x8f3e.toUInt
  val GL_IMAGE_1D: UInt = 0x904c.toUInt
  val GL_IMAGE_2D: UInt = 0x904d.toUInt
  val GL_IMAGE_3D: UInt = 0x904e.toUInt
  val GL_IMAGE_2D_RECT: UInt = 0x904f.toUInt
  val GL_IMAGE_CUBE: UInt = 0x9050.toUInt
  val GL_IMAGE_BUFFER: UInt = 0x9051.toUInt
  val GL_IMAGE_1D_ARRAY: UInt = 0x9052.toUInt
  val GL_IMAGE_2D_ARRAY: UInt = 0x9053.toUInt
  val GL_IMAGE_CUBE_MAP_ARRAY: UInt = 0x9054.toUInt
  val GL_IMAGE_2D_MULTISAMPLE: UInt = 0x9055.toUInt
  val GL_IMAGE_2D_MULTISAMPLE_ARRAY: UInt = 0x9056.toUInt
  val GL_INT_IMAGE_1D: UInt = 0x9057.toUInt
  val GL_INT_IMAGE_2D: UInt = 0x9058.toUInt
  val GL_INT_IMAGE_3D: UInt = 0x9059.toUInt
  val GL_INT_IMAGE_2D_RECT: UInt = 0x905a.toUInt
  val GL_INT_IMAGE_CUBE: UInt = 0x905b.toUInt
  val GL_INT_IMAGE_BUFFER: UInt = 0x905c.toUInt
  val GL_INT_IMAGE_1D_ARRAY: UInt = 0x905d.toUInt
  val GL_INT_IMAGE_2D_ARRAY: UInt = 0x905e.toUInt
  val GL_INT_IMAGE_CUBE_MAP_ARRAY: UInt = 0x905f.toUInt
  val GL_INT_IMAGE_2D_MULTISAMPLE: UInt = 0x9060.toUInt
  val GL_INT_IMAGE_2D_MULTISAMPLE_ARRAY: UInt = 0x9061.toUInt
  val GL_UNSIGNED_INT_IMAGE_1D: UInt = 0x9062.toUInt
  val GL_UNSIGNED_INT_IMAGE_2D: UInt = 0x9063.toUInt
  val GL_UNSIGNED_INT_IMAGE_3D: UInt = 0x9064.toUInt
  val GL_UNSIGNED_INT_IMAGE_2D_RECT: UInt = 0x9065.toUInt
  val GL_UNSIGNED_INT_IMAGE_CUBE: UInt = 0x9066.toUInt
  val GL_UNSIGNED_INT_IMAGE_BUFFER: UInt = 0x9067.toUInt
  val GL_UNSIGNED_INT_IMAGE_1D_ARRAY: UInt = 0x9068.toUInt
  val GL_UNSIGNED_INT_IMAGE_2D_ARRAY: UInt = 0x9069.toUInt
  val GL_UNSIGNED_INT_IMAGE_CUBE_MAP_ARRAY: UInt = 0x906a.toUInt
  val GL_UNSIGNED_INT_IMAGE_2D_MULTISAMPLE: UInt = 0x906b.toUInt
  val GL_UNSIGNED_INT_IMAGE_2D_MULTISAMPLE_ARRAY: UInt = 0x906c.toUInt
  val GL_MAX_IMAGE_SAMPLES: UInt = 0x906d.toUInt
  val GL_IMAGE_BINDING_FORMAT: UInt = 0x906e.toUInt
  val GL_IMAGE_FORMAT_COMPATIBILITY_TYPE: UInt = 0x90c7.toUInt
  val GL_IMAGE_FORMAT_COMPATIBILITY_BY_SIZE: UInt = 0x90c8.toUInt
  val GL_IMAGE_FORMAT_COMPATIBILITY_BY_CLASS: UInt = 0x90c9.toUInt
  val GL_MAX_VERTEX_IMAGE_UNIFORMS: UInt = 0x90ca.toUInt
  val GL_MAX_TESS_CONTROL_IMAGE_UNIFORMS: UInt = 0x90cb.toUInt
  val GL_MAX_TESS_EVALUATION_IMAGE_UNIFORMS: UInt = 0x90cc.toUInt
  val GL_MAX_GEOMETRY_IMAGE_UNIFORMS: UInt = 0x90cd.toUInt
  val GL_MAX_FRAGMENT_IMAGE_UNIFORMS: UInt = 0x90ce.toUInt
  val GL_MAX_COMBINED_IMAGE_UNIFORMS: UInt = 0x90cf.toUInt
  val GL_COMPRESSED_RGBA_BPTC_UNORM: UInt = 0x8e8c.toUInt
  val GL_COMPRESSED_SRGB_ALPHA_BPTC_UNORM: UInt = 0x8e8d.toUInt
  val GL_COMPRESSED_RGB_BPTC_SIGNED_FLOAT: UInt = 0x8e8e.toUInt
  val GL_COMPRESSED_RGB_BPTC_UNSIGNED_FLOAT: UInt = 0x8e8f.toUInt
  val GL_TEXTURE_IMMUTABLE_FORMAT: UInt = 0x912f.toUInt
  /*
   * End OpenGL 4.2
   */

  /*
   * OpenGL 4.3
   */
  val GL_NUM_SHADING_LANGUAGE_VERSIONS: UInt = 0x82e9.toUInt
  val GL_VERTEX_ATTRIB_ARRAY_LONG: UInt = 0x874e.toUInt
  val GL_COMPRESSED_RGB8_ETC2: UInt = 0x9274.toUInt
  val GL_COMPRESSED_SRGB8_ETC2: UInt = 0x9275.toUInt
  val GL_COMPRESSED_RGB8_PUNCHTHROUGH_ALPHA1_ETC2: UInt = 0x9276.toUInt
  val GL_COMPRESSED_SRGB8_PUNCHTHROUGH_ALPHA1_ETC2: UInt = 0x9277.toUInt
  val GL_COMPRESSED_RGBA8_ETC2_EAC: UInt = 0x9278.toUInt
  val GL_COMPRESSED_SRGB8_ALPHA8_ETC2_EAC: UInt = 0x9279.toUInt
  val GL_COMPRESSED_R11_EAC: UInt = 0x9270.toUInt
  val GL_COMPRESSED_SIGNED_R11_EAC: UInt = 0x9271.toUInt
  val GL_COMPRESSED_RG11_EAC: UInt = 0x9272.toUInt
  val GL_COMPRESSED_SIGNED_RG11_EAC: UInt = 0x9273.toUInt
  val GL_PRIMITIVE_RESTART_FIXED_INDEX: UInt = 0x8d69.toUInt
  val GL_ANY_SAMPLES_PASSED_CONSERVATIVE: UInt = 0x8d6a.toUInt
  val GL_MAX_ELEMENT_INDEX: UInt = 0x8d6b.toUInt
  val GL_COMPUTE_SHADER: UInt = 0x91b9.toUInt
  val GL_MAX_COMPUTE_UNIFORM_BLOCKS: UInt = 0x91bb.toUInt
  val GL_MAX_COMPUTE_TEXTURE_IMAGE_UNITS: UInt = 0x91bc.toUInt
  val GL_MAX_COMPUTE_IMAGE_UNIFORMS: UInt = 0x91bd.toUInt
  val GL_MAX_COMPUTE_SHARED_MEMORY_SIZE: UInt = 0x8262.toUInt
  val GL_MAX_COMPUTE_UNIFORM_COMPONENTS: UInt = 0x8263.toUInt
  val GL_MAX_COMPUTE_ATOMIC_COUNTER_BUFFERS: UInt = 0x8264.toUInt
  val GL_MAX_COMPUTE_ATOMIC_COUNTERS: UInt = 0x8265.toUInt
  val GL_MAX_COMBINED_COMPUTE_UNIFORM_COMPONENTS: UInt = 0x8266.toUInt
  val GL_MAX_COMPUTE_WORK_GROUP_INVOCATIONS: UInt = 0x90eb.toUInt
  val GL_MAX_COMPUTE_WORK_GROUP_COUNT: UInt = 0x91be.toUInt
  val GL_MAX_COMPUTE_WORK_GROUP_SIZE: UInt = 0x91bf.toUInt
  val GL_COMPUTE_WORK_GROUP_SIZE: UInt = 0x8267.toUInt
  val GL_UNIFORM_BLOCK_REFERENCED_BY_COMPUTE_SHADER: UInt = 0x90ec.toUInt
  val GL_ATOMIC_COUNTER_BUFFER_REFERENCED_BY_COMPUTE_SHADER: UInt =
    0x90ed.toUInt
  val GL_DISPATCH_INDIRECT_BUFFER: UInt = 0x90ee.toUInt
  val GL_DISPATCH_INDIRECT_BUFFER_BINDING: UInt = 0x90ef.toUInt
  val GL_COMPUTE_SHADER_BIT: ULong = 0x00000020.toULong
  val GL_DEBUG_OUTPUT_SYNCHRONOUS: UInt = 0x8242.toUInt
  val GL_DEBUG_NEXT_LOGGED_MESSAGE_LENGTH: UInt = 0x8243.toUInt
  val GL_DEBUG_CALLBACK_FUNCTION: UInt = 0x8244.toUInt
  val GL_DEBUG_CALLBACK_USER_PARAM: UInt = 0x8245.toUInt
  val GL_DEBUG_SOURCE_API: UInt = 0x8246.toUInt
  val GL_DEBUG_SOURCE_WINDOW_SYSTEM: UInt = 0x8247.toUInt
  val GL_DEBUG_SOURCE_SHADER_COMPILER: UInt = 0x8248.toUInt
  val GL_DEBUG_SOURCE_THIRD_PARTY: UInt = 0x8249.toUInt
  val GL_DEBUG_SOURCE_APPLICATION: UInt = 0x824a.toUInt
  val GL_DEBUG_SOURCE_OTHER: UInt = 0x824b.toUInt
  val GL_DEBUG_TYPE_ERROR: UInt = 0x824c.toUInt
  val GL_DEBUG_TYPE_DEPRECATED_BEHAVIOR: UInt = 0x824d.toUInt
  val GL_DEBUG_TYPE_UNDEFINED_BEHAVIOR: UInt = 0x824e.toUInt
  val GL_DEBUG_TYPE_PORTABILITY: UInt = 0x824f.toUInt
  val GL_DEBUG_TYPE_PERFORMANCE: UInt = 0x8250.toUInt
  val GL_DEBUG_TYPE_OTHER: UInt = 0x8251.toUInt
  val GL_MAX_DEBUG_MESSAGE_LENGTH: UInt = 0x9143.toUInt
  val GL_MAX_DEBUG_LOGGED_MESSAGES: UInt = 0x9144.toUInt
  val GL_DEBUG_LOGGED_MESSAGES: UInt = 0x9145.toUInt
  val GL_DEBUG_SEVERITY_HIGH: UInt = 0x9146.toUInt
  val GL_DEBUG_SEVERITY_MEDIUM: UInt = 0x9147.toUInt
  val GL_DEBUG_SEVERITY_LOW: UInt = 0x9148.toUInt
  val GL_DEBUG_TYPE_MARKER: UInt = 0x8268.toUInt
  val GL_DEBUG_TYPE_PUSH_GROUP: UInt = 0x8269.toUInt
  val GL_DEBUG_TYPE_POP_GROUP: UInt = 0x826a.toUInt
  val GL_DEBUG_SEVERITY_NOTIFICATION: UInt = 0x826b.toUInt
  val GL_MAX_DEBUG_GROUP_STACK_DEPTH: UInt = 0x826c.toUInt
  val GL_DEBUG_GROUP_STACK_DEPTH: UInt = 0x826d.toUInt
  val GL_BUFFER: UInt = 0x82e0.toUInt
  val GL_SHADER: UInt = 0x82e1.toUInt
  val GL_PROGRAM: UInt = 0x82e2.toUInt
  val GL_QUERY: UInt = 0x82e3.toUInt
  val GL_PROGRAM_PIPELINE: UInt = 0x82e4.toUInt
  val GL_SAMPLER: UInt = 0x82e6.toUInt
  val GL_MAX_LABEL_LENGTH: UInt = 0x82e8.toUInt
  val GL_DEBUG_OUTPUT: UInt = 0x92e0.toUInt
  val GL_CONTEXT_FLAG_DEBUG_BIT: ULong = 0x00000002.toULong
  val GL_MAX_UNIFORM_LOCATIONS: UInt = 0x826e.toUInt
  val GL_FRAMEBUFFER_DEFAULT_WIDTH: UInt = 0x9310.toUInt
  val GL_FRAMEBUFFER_DEFAULT_HEIGHT: UInt = 0x9311.toUInt
  val GL_FRAMEBUFFER_DEFAULT_LAYERS: UInt = 0x9312.toUInt
  val GL_FRAMEBUFFER_DEFAULT_SAMPLES: UInt = 0x9313.toUInt
  val GL_FRAMEBUFFER_DEFAULT_FIXED_SAMPLE_LOCATIONS: UInt = 0x9314.toUInt
  val GL_MAX_FRAMEBUFFER_WIDTH: UInt = 0x9315.toUInt
  val GL_MAX_FRAMEBUFFER_HEIGHT: UInt = 0x9316.toUInt
  val GL_MAX_FRAMEBUFFER_LAYERS: UInt = 0x9317.toUInt
  val GL_MAX_FRAMEBUFFER_SAMPLES: UInt = 0x9318.toUInt
  val GL_INTERNALFORMAT_SUPPORTED: UInt = 0x826f.toUInt
  val GL_INTERNALFORMAT_PREFERRED: UInt = 0x8270.toUInt
  val GL_INTERNALFORMAT_RED_SIZE: UInt = 0x8271.toUInt
  val GL_INTERNALFORMAT_GREEN_SIZE: UInt = 0x8272.toUInt
  val GL_INTERNALFORMAT_BLUE_SIZE: UInt = 0x8273.toUInt
  val GL_INTERNALFORMAT_ALPHA_SIZE: UInt = 0x8274.toUInt
  val GL_INTERNALFORMAT_DEPTH_SIZE: UInt = 0x8275.toUInt
  val GL_INTERNALFORMAT_STENCIL_SIZE: UInt = 0x8276.toUInt
  val GL_INTERNALFORMAT_SHARED_SIZE: UInt = 0x8277.toUInt
  val GL_INTERNALFORMAT_RED_TYPE: UInt = 0x8278.toUInt
  val GL_INTERNALFORMAT_GREEN_TYPE: UInt = 0x8279.toUInt
  val GL_INTERNALFORMAT_BLUE_TYPE: UInt = 0x827a.toUInt
  val GL_INTERNALFORMAT_ALPHA_TYPE: UInt = 0x827b.toUInt
  val GL_INTERNALFORMAT_DEPTH_TYPE: UInt = 0x827c.toUInt
  val GL_INTERNALFORMAT_STENCIL_TYPE: UInt = 0x827d.toUInt
  val GL_MAX_WIDTH: UInt = 0x827e.toUInt
  val GL_MAX_HEIGHT: UInt = 0x827f.toUInt
  val GL_MAX_DEPTH: UInt = 0x8280.toUInt
  val GL_MAX_LAYERS: UInt = 0x8281.toUInt
  val GL_MAX_COMBINED_DIMENSIONS: UInt = 0x8282.toUInt
  val GL_COLOR_COMPONENTS: UInt = 0x8283.toUInt
  val GL_DEPTH_COMPONENTS: UInt = 0x8284.toUInt
  val GL_STENCIL_COMPONENTS: UInt = 0x8285.toUInt
  val GL_COLOR_RENDERABLE: UInt = 0x8286.toUInt
  val GL_DEPTH_RENDERABLE: UInt = 0x8287.toUInt
  val GL_STENCIL_RENDERABLE: UInt = 0x8288.toUInt
  val GL_FRAMEBUFFER_RENDERABLE: UInt = 0x8289.toUInt
  val GL_FRAMEBUFFER_RENDERABLE_LAYERED: UInt = 0x828a.toUInt
  val GL_FRAMEBUFFER_BLEND: UInt = 0x828b.toUInt
  val GL_READ_PIXELS: UInt = 0x828c.toUInt
  val GL_READ_PIXELS_FORMAT: UInt = 0x828d.toUInt
  val GL_READ_PIXELS_TYPE: UInt = 0x828e.toUInt
  val GL_TEXTURE_IMAGE_FORMAT: UInt = 0x828f.toUInt
  val GL_TEXTURE_IMAGE_TYPE: UInt = 0x8290.toUInt
  val GL_GET_TEXTURE_IMAGE_FORMAT: UInt = 0x8291.toUInt
  val GL_GET_TEXTURE_IMAGE_TYPE: UInt = 0x8292.toUInt
  val GL_MIPMAP: UInt = 0x8293.toUInt
  val GL_MANUAL_GENERATE_MIPMAP: UInt = 0x8294.toUInt
  val GL_AUTO_GENERATE_MIPMAP: UInt = 0x8295.toUInt
  val GL_COLOR_ENCODING: UInt = 0x8296.toUInt
  val GL_SRGB_READ: UInt = 0x8297.toUInt
  val GL_SRGB_WRITE: UInt = 0x8298.toUInt
  val GL_FILTER: UInt = 0x829a.toUInt
  val GL_VERTEX_TEXTURE: UInt = 0x829b.toUInt
  val GL_TESS_CONTROL_TEXTURE: UInt = 0x829c.toUInt
  val GL_TESS_EVALUATION_TEXTURE: UInt = 0x829d.toUInt
  val GL_GEOMETRY_TEXTURE: UInt = 0x829e.toUInt
  val GL_FRAGMENT_TEXTURE: UInt = 0x829f.toUInt
  val GL_COMPUTE_TEXTURE: UInt = 0x82a0.toUInt
  val GL_TEXTURE_SHADOW: UInt = 0x82a1.toUInt
  val GL_TEXTURE_GATHER: UInt = 0x82a2.toUInt
  val GL_TEXTURE_GATHER_SHADOW: UInt = 0x82a3.toUInt
  val GL_SHADER_IMAGE_LOAD: UInt = 0x82a4.toUInt
  val GL_SHADER_IMAGE_STORE: UInt = 0x82a5.toUInt
  val GL_SHADER_IMAGE_ATOMIC: UInt = 0x82a6.toUInt
  val GL_IMAGE_TEXEL_SIZE: UInt = 0x82a7.toUInt
  val GL_IMAGE_COMPATIBILITY_CLASS: UInt = 0x82a8.toUInt
  val GL_IMAGE_PIXEL_FORMAT: UInt = 0x82a9.toUInt
  val GL_IMAGE_PIXEL_TYPE: UInt = 0x82aa.toUInt
  val GL_SIMULTANEOUS_TEXTURE_AND_DEPTH_TEST: UInt = 0x82ac.toUInt
  val GL_SIMULTANEOUS_TEXTURE_AND_STENCIL_TEST: UInt = 0x82ad.toUInt
  val GL_SIMULTANEOUS_TEXTURE_AND_DEPTH_WRITE: UInt = 0x82ae.toUInt
  val GL_SIMULTANEOUS_TEXTURE_AND_STENCIL_WRITE: UInt = 0x82af.toUInt
  val GL_TEXTURE_COMPRESSED_BLOCK_WIDTH: UInt = 0x82b1.toUInt
  val GL_TEXTURE_COMPRESSED_BLOCK_HEIGHT: UInt = 0x82b2.toUInt
  val GL_TEXTURE_COMPRESSED_BLOCK_SIZE: UInt = 0x82b3.toUInt
  val GL_CLEAR_BUFFER: UInt = 0x82b4.toUInt
  val GL_TEXTURE_VIEW: UInt = 0x82b5.toUInt
  val GL_VIEW_COMPATIBILITY_CLASS: UInt = 0x82b6.toUInt
  val GL_FULL_SUPPORT: UInt = 0x82b7.toUInt
  val GL_CAVEAT_SUPPORT: UInt = 0x82b8.toUInt
  val GL_IMAGE_CLASS_4_X_32: UInt = 0x82b9.toUInt
  val GL_IMAGE_CLASS_2_X_32: UInt = 0x82ba.toUInt
  val GL_IMAGE_CLASS_1_X_32: UInt = 0x82bb.toUInt
  val GL_IMAGE_CLASS_4_X_16: UInt = 0x82bc.toUInt
  val GL_IMAGE_CLASS_2_X_16: UInt = 0x82bd.toUInt
  val GL_IMAGE_CLASS_1_X_16: UInt = 0x82be.toUInt
  val GL_IMAGE_CLASS_4_X_8: UInt = 0x82bf.toUInt
  val GL_IMAGE_CLASS_2_X_8: UInt = 0x82c0.toUInt
  val GL_IMAGE_CLASS_1_X_8: UInt = 0x82c1.toUInt
  val GL_IMAGE_CLASS_11_11_10: UInt = 0x82c2.toUInt
  val GL_IMAGE_CLASS_10_10_10_2: UInt = 0x82c3.toUInt
  val GL_VIEW_CLASS_128_BITS: UInt = 0x82c4.toUInt
  val GL_VIEW_CLASS_96_BITS: UInt = 0x82c5.toUInt
  val GL_VIEW_CLASS_64_BITS: UInt = 0x82c6.toUInt
  val GL_VIEW_CLASS_48_BITS: UInt = 0x82c7.toUInt
  val GL_VIEW_CLASS_32_BITS: UInt = 0x82c8.toUInt
  val GL_VIEW_CLASS_24_BITS: UInt = 0x82c9.toUInt
  val GL_VIEW_CLASS_16_BITS: UInt = 0x82ca.toUInt
  val GL_VIEW_CLASS_8_BITS: UInt = 0x82cb.toUInt
  val GL_VIEW_CLASS_S3TC_DXT1_RGB: UInt = 0x82cc.toUInt
  val GL_VIEW_CLASS_S3TC_DXT1_RGBA: UInt = 0x82cd.toUInt
  val GL_VIEW_CLASS_S3TC_DXT3_RGBA: UInt = 0x82ce.toUInt
  val GL_VIEW_CLASS_S3TC_DXT5_RGBA: UInt = 0x82cf.toUInt
  val GL_VIEW_CLASS_RGTC1_RED: UInt = 0x82d0.toUInt
  val GL_VIEW_CLASS_RGTC2_RG: UInt = 0x82d1.toUInt
  val GL_VIEW_CLASS_BPTC_UNORM: UInt = 0x82d2.toUInt
  val GL_VIEW_CLASS_BPTC_FLOAT: UInt = 0x82d3.toUInt
  val GL_UNIFORM: UInt = 0x92e1.toUInt
  val GL_UNIFORM_BLOCK: UInt = 0x92e2.toUInt
  val GL_PROGRAM_INPUT: UInt = 0x92e3.toUInt
  val GL_PROGRAM_OUTPUT: UInt = 0x92e4.toUInt
  val GL_BUFFER_VARIABLE: UInt = 0x92e5.toUInt
  val GL_SHADER_STORAGE_BLOCK: UInt = 0x92e6.toUInt
  val GL_VERTEX_SUBROUTINE: UInt = 0x92e8.toUInt
  val GL_TESS_CONTROL_SUBROUTINE: UInt = 0x92e9.toUInt
  val GL_TESS_EVALUATION_SUBROUTINE: UInt = 0x92ea.toUInt
  val GL_GEOMETRY_SUBROUTINE: UInt = 0x92eb.toUInt
  val GL_FRAGMENT_SUBROUTINE: UInt = 0x92ec.toUInt
  val GL_COMPUTE_SUBROUTINE: UInt = 0x92ed.toUInt
  val GL_VERTEX_SUBROUTINE_UNIFORM: UInt = 0x92ee.toUInt
  val GL_TESS_CONTROL_SUBROUTINE_UNIFORM: UInt = 0x92ef.toUInt
  val GL_TESS_EVALUATION_SUBROUTINE_UNIFORM: UInt = 0x92f0.toUInt
  val GL_GEOMETRY_SUBROUTINE_UNIFORM: UInt = 0x92f1.toUInt
  val GL_FRAGMENT_SUBROUTINE_UNIFORM: UInt = 0x92f2.toUInt
  val GL_COMPUTE_SUBROUTINE_UNIFORM: UInt = 0x92f3.toUInt
  val GL_TRANSFORM_FEEDBACK_VARYING: UInt = 0x92f4.toUInt
  val GL_ACTIVE_RESOURCES: UInt = 0x92f5.toUInt
  val GL_MAX_NAME_LENGTH: UInt = 0x92f6.toUInt
  val GL_MAX_NUM_ACTIVE_VARIABLES: UInt = 0x92f7.toUInt
  val GL_MAX_NUM_COMPATIBLE_SUBROUTINES: UInt = 0x92f8.toUInt
  val GL_NAME_LENGTH: UInt = 0x92f9.toUInt
  val GL_TYPE: UInt = 0x92fa.toUInt
  val GL_ARRAY_SIZE: UInt = 0x92fb.toUInt
  val GL_OFFSET: UInt = 0x92fc.toUInt
  val GL_BLOCK_INDEX: UInt = 0x92fd.toUInt
  val GL_ARRAY_STRIDE: UInt = 0x92fe.toUInt
  val GL_MATRIX_STRIDE: UInt = 0x92ff.toUInt
  val GL_IS_ROW_MAJOR: UInt = 0x9300.toUInt
  val GL_ATOMIC_COUNTER_BUFFER_INDEX: UInt = 0x9301.toUInt
  val GL_BUFFER_BINDING: UInt = 0x9302.toUInt
  val GL_BUFFER_DATA_SIZE: UInt = 0x9303.toUInt
  val GL_NUM_ACTIVE_VARIABLES: UInt = 0x9304.toUInt
  val GL_ACTIVE_VARIABLES: UInt = 0x9305.toUInt
  val GL_REFERENCED_BY_VERTEX_SHADER: UInt = 0x9306.toUInt
  val GL_REFERENCED_BY_TESS_CONTROL_SHADER: UInt = 0x9307.toUInt
  val GL_REFERENCED_BY_TESS_EVALUATION_SHADER: UInt = 0x9308.toUInt
  val GL_REFERENCED_BY_GEOMETRY_SHADER: UInt = 0x9309.toUInt
  val GL_REFERENCED_BY_FRAGMENT_SHADER: UInt = 0x930a.toUInt
  val GL_REFERENCED_BY_COMPUTE_SHADER: UInt = 0x930b.toUInt
  val GL_TOP_LEVEL_ARRAY_SIZE: UInt = 0x930c.toUInt
  val GL_TOP_LEVEL_ARRAY_STRIDE: UInt = 0x930d.toUInt
  val GL_LOCATION: UInt = 0x930e.toUInt
  val GL_LOCATION_INDEX: UInt = 0x930f.toUInt
  val GL_IS_PER_PATCH: UInt = 0x92e7.toUInt
  val GL_SHADER_STORAGE_BUFFER: UInt = 0x90d2.toUInt
  val GL_SHADER_STORAGE_BUFFER_BINDING: UInt = 0x90d3.toUInt
  val GL_SHADER_STORAGE_BUFFER_START: UInt = 0x90d4.toUInt
  val GL_SHADER_STORAGE_BUFFER_SIZE: UInt = 0x90d5.toUInt
  val GL_MAX_VERTEX_SHADER_STORAGE_BLOCKS: UInt = 0x90d6.toUInt
  val GL_MAX_GEOMETRY_SHADER_STORAGE_BLOCKS: UInt = 0x90d7.toUInt
  val GL_MAX_TESS_CONTROL_SHADER_STORAGE_BLOCKS: UInt = 0x90d8.toUInt
  val GL_MAX_TESS_EVALUATION_SHADER_STORAGE_BLOCKS: UInt = 0x90d9.toUInt
  val GL_MAX_FRAGMENT_SHADER_STORAGE_BLOCKS: UInt = 0x90da.toUInt
  val GL_MAX_COMPUTE_SHADER_STORAGE_BLOCKS: UInt = 0x90db.toUInt
  val GL_MAX_COMBINED_SHADER_STORAGE_BLOCKS: UInt = 0x90dc.toUInt
  val GL_MAX_SHADER_STORAGE_BUFFER_BINDINGS: UInt = 0x90dd.toUInt
  val GL_MAX_SHADER_STORAGE_BLOCK_SIZE: UInt = 0x90de.toUInt
  val GL_SHADER_STORAGE_BUFFER_OFFSET_ALIGNMENT: UInt = 0x90df.toUInt
  val GL_SHADER_STORAGE_BARRIER_BIT: UInt = 0x00002000.toUInt
  val GL_MAX_COMBINED_SHADER_OUTPUT_RESOURCES: UInt = 0x8f39.toUInt
  val GL_DEPTH_STENCIL_TEXTURE_MODE: UInt = 0x90ea.toUInt
  val GL_TEXTURE_BUFFER_OFFSET: UInt = 0x919d.toUInt
  val GL_TEXTURE_BUFFER_SIZE: UInt = 0x919e.toUInt
  val GL_TEXTURE_BUFFER_OFFSET_ALIGNMENT: UInt = 0x919f.toUInt
  val GL_TEXTURE_VIEW_MIN_LEVEL: UInt = 0x82db.toUInt
  val GL_TEXTURE_VIEW_NUM_LEVELS: UInt = 0x82dc.toUInt
  val GL_TEXTURE_VIEW_MIN_LAYER: UInt = 0x82dd.toUInt
  val GL_TEXTURE_VIEW_NUM_LAYERS: UInt = 0x82de.toUInt
  val GL_TEXTURE_IMMUTABLE_LEVELS: UInt = 0x82df.toUInt
  val GL_VERTEX_ATTRIB_BINDING: UInt = 0x82d4.toUInt
  val GL_VERTEX_ATTRIB_RELATIVE_OFFSET: UInt = 0x82d5.toUInt
  val GL_VERTEX_BINDING_DIVISOR: UInt = 0x82d6.toUInt
  val GL_VERTEX_BINDING_OFFSET: UInt = 0x82d7.toUInt
  val GL_VERTEX_BINDING_STRIDE: UInt = 0x82d8.toUInt
  val GL_MAX_VERTEX_ATTRIB_RELATIVE_OFFSET: UInt = 0x82d9.toUInt
  val GL_MAX_VERTEX_ATTRIB_BINDINGS: UInt = 0x82da.toUInt
  val GL_VERTEX_BINDING_BUFFER: UInt = 0x8f4f.toUInt
  val GL_DISPLAY_LIST: UInt = 0x82e7.toUInt
  /*
   * End OpenGL 4.3
   */

  /*
   * OpenGL 4.4
   */
  val GL_MAX_VERTEX_ATTRIB_STRIDE: UInt = 0x82e5.toUInt
  val GL_PRIMITIVE_RESTART_FOR_PATCHES_SUPPORTED: UInt = 0x8221.toUInt
  val GL_TEXTURE_BUFFER_BINDING: UInt = 0x8c2a.toUInt
  val GL_MAP_PERSISTENT_BIT: UInt = 0x0040.toUInt
  val GL_MAP_COHERENT_BIT: UInt = 0x0080.toUInt
  val GL_DYNAMIC_STORAGE_BIT: UInt = 0x0100.toUInt
  val GL_CLIENT_STORAGE_BIT: UInt = 0x0200.toUInt
  val GL_CLIENT_MAPPED_BUFFER_BARRIER_BIT: ULong = 0x00004000.toULong
  val GL_BUFFER_IMMUTABLE_STORAGE: UInt = 0x821f.toUInt
  val GL_BUFFER_STORAGE_FLAGS: UInt = 0x8220.toUInt
  val GL_CLEAR_TEXTURE: UInt = 0x9365.toUInt
  val GL_LOCATION_COMPONENT: UInt = 0x934a.toUInt
  val GL_TRANSFORM_FEEDBACK_BUFFER_INDEX: UInt = 0x934b.toUInt
  val GL_TRANSFORM_FEEDBACK_BUFFER_STRIDE: UInt = 0x934c.toUInt
  val GL_QUERY_BUFFER: UInt = 0x9192.toUInt
  val GL_QUERY_BUFFER_BARRIER_BIT: ULong = 0x00008000.toULong
  val GL_QUERY_BUFFER_BINDING: UInt = 0x9193.toUInt
  val GL_QUERY_RESULT_NO_WAIT: UInt = 0x9194.toUInt
  val GL_MIRROR_CLAMP_TO_EDGE: UInt = 0x8743.toUInt
  /*
   * End OpenGL 4.4
   */

  /*
   * OpenGL 4.5
   */
  val GL_CONTEXT_LOST: UInt = 0x0507.toUInt
  val GL_NEGATIVE_ONE_TO_ONE: UInt = 0x935e.toUInt
  val GL_ZERO_TO_ONE: UInt = 0x935f.toUInt
  val GL_CLIP_ORIGIN: UInt = 0x935c.toUInt
  val GL_CLIP_DEPTH_MODE: UInt = 0x935d.toUInt
  val GL_QUERY_WAIT_INVERTED: UInt = 0x8e17.toUInt
  val GL_QUERY_NO_WAIT_INVERTED: UInt = 0x8e18.toUInt
  val GL_QUERY_BY_REGION_WAIT_INVERTED: UInt = 0x8e19.toUInt
  val GL_QUERY_BY_REGION_NO_WAIT_INVERTED: UInt = 0x8e1a.toUInt
  val GL_MAX_CULL_DISTANCES: UInt = 0x82f9.toUInt
  val GL_MAX_COMBINED_CLIP_AND_CULL_DISTANCES: UInt = 0x82fa.toUInt
  val GL_TEXTURE_TARGET: UInt = 0x1006.toUInt
  val GL_QUERY_TARGET: UInt = 0x82ea.toUInt
  val GL_GUILTY_CONTEXT_RESET: UInt = 0x8253.toUInt
  val GL_INNOCENT_CONTEXT_RESET: UInt = 0x8254.toUInt
  val GL_UNKNOWN_CONTEXT_RESET: UInt = 0x8255.toUInt
  val GL_RESET_NOTIFICATION_STRATEGY: UInt = 0x8256.toUInt
  val GL_LOSE_CONTEXT_ON_RESET: UInt = 0x8252.toUInt
  val GL_NO_RESET_NOTIFICATION: UInt = 0x8261.toUInt
  val GL_CONTEXT_FLAG_ROBUST_ACCESS_BIT: ULong = 0x00000004.toULong
  val GL_CONTEXT_RELEASE_BEHAVIOR: UInt = 0x82fb.toUInt
  val GL_CONTEXT_RELEASE_BEHAVIOR_FLUSH: UInt = 0x82fc.toUInt
  /*
   * End OpenGL 4.5
   */

  /*
   * OpenGL 4.6
   */
  val GL_SHADER_BINARY_FORMAT_SPIR_V: UInt = 0x9551.toUInt
  val GL_SPIR_V_BINARY: UInt = 0x9552.toUInt
  val GL_PARAMETER_BUFFER: UInt = 0x80ee.toUInt
  val GL_PARAMETER_BUFFER_BINDING: UInt = 0x80ef.toUInt
  val GL_CONTEXT_FLAG_NO_ERROR_BIT: ULong = 0x00000008.toULong
  val GL_VERTICES_SUBMITTED: UInt = 0x82ee.toUInt
  val GL_PRIMITIVES_SUBMITTED: UInt = 0x82ef.toUInt
  val GL_VERTEX_SHADER_INVOCATIONS: UInt = 0x82f0.toUInt
  val GL_TESS_CONTROL_SHADER_PATCHES: UInt = 0x82f1.toUInt
  val GL_TESS_EVALUATION_SHADER_INVOCATIONS: UInt = 0x82f2.toUInt
  val GL_GEOMETRY_SHADER_PRIMITIVES_EMITTED: UInt = 0x82f3.toUInt
  val GL_FRAGMENT_SHADER_INVOCATIONS: UInt = 0x82f4.toUInt
  val GL_COMPUTE_SHADER_INVOCATIONS: UInt = 0x82f5.toUInt
  val GL_CLIPPING_INPUT_PRIMITIVES: UInt = 0x82f6.toUInt
  val GL_CLIPPING_OUTPUT_PRIMITIVES: UInt = 0x82f7.toUInt
  val GL_POLYGON_OFFSET_CLAMP: UInt = 0x8e1b.toUInt
  val GL_SPIR_V_EXTENSIONS: UInt = 0x9553.toUInt
  val GL_NUM_SPIR_V_EXTENSIONS: UInt = 0x9554.toUInt
  val GL_TEXTURE_MAX_ANISOTROPY: UInt = 0x84fe.toUInt
  val GL_MAX_TEXTURE_MAX_ANISOTROPY: UInt = 0x84ff.toUInt
  val GL_TRANSFORM_FEEDBACK_OVERFLOW: UInt = 0x82ec.toUInt
  val GL_TRANSFORM_FEEDBACK_STREAM_OVERFLOW: UInt = 0x82ed.toUInt
  /*
   * End OpenGL 4.6
   */
end GLExtras
