package com.tona.myapplication.openglesdemo

import android.content.Context
import android.graphics.BitmapFactory
import android.opengl.*
import com.tona.myapplication.R
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class TextureCompareRenderer(
    private val context: Context
) : GLSurfaceView.Renderer {

    var mode = DisplayMode.ORIGINAL

    var drawCount = 250   // có thể set từ Activity (1, 50, 100, 200...)

    private var program = 0
    private var rgbaTex = 0
    private var etc1Tex = 0

    private lateinit var vertexBuffer: FloatBuffer

    // Fullscreen quad (pos.xy, tex.xy)
    private val vertices = floatArrayOf(
        -1f, -1f, 0f, 1f,
        1f, -1f, 1f, 1f,
        -1f,  1f, 0f, 0f,

        1f, -1f, 1f, 1f,
        1f,  1f, 1f, 0f,
        -1f,  1f, 0f, 0f
    )

    private val vertexShader = """
        attribute vec4 aPosition;
        attribute vec2 aTexCoord;
        varying vec2 vTexCoord;

        void main() {
            gl_Position = aPosition;
            vTexCoord = aTexCoord;
        }
    """

    private val fragmentShader = """
        precision mediump float;
        varying vec2 vTexCoord;

        uniform sampler2D uTexRGBA;
        uniform sampler2D uTexETC1;
        uniform int uMode;

        void main() {
            vec4 rgba = texture2D(uTexRGBA, vTexCoord);

            // ETC1 chỉ có RGB
            vec3 etcRgb = texture2D(uTexETC1, vTexCoord).rgb;
            vec4 etc1 = vec4(etcRgb, 1.0);

            if (uMode == 0) {
                gl_FragColor = rgba;
            } else if (uMode == 1) {
                gl_FragColor = etc1;
            } else {
                gl_FragColor = abs(rgba - etc1) * 5.0;
            }
        }
    """

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        GLES20.glClearColor(0f, 0f, 0f, 1f)

        // Kiểm tra ETC1 support
        if (!ETC1Util.isETC1Supported()) {
            throw RuntimeException("ETC1 not supported on this device")
        }

        program = ShaderUtils.createProgram(vertexShader, fragmentShader)

        vertexBuffer = ByteBuffer
            .allocateDirect(vertices.size * 4)
            .order(ByteOrder.nativeOrder())
            .asFloatBuffer()
        vertexBuffer.put(vertices).position(0)

        rgbaTex = loadRGBA()
        etc1Tex = loadETC1()
    }

    override fun onSurfaceChanged(gl: GL10?, w: Int, h: Int) {
        GLES20.glViewport(0, 0, w, h)
    }

    override fun onDrawFrame(gl: GL10?) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)
        GLES20.glUseProgram(program)

        val posLoc = GLES20.glGetAttribLocation(program, "aPosition")
        val texLoc = GLES20.glGetAttribLocation(program, "aTexCoord")
        val modeLoc = GLES20.glGetUniformLocation(program, "uMode")

        vertexBuffer.position(0)
        GLES20.glVertexAttribPointer(posLoc, 2, GLES20.GL_FLOAT, false, 16, vertexBuffer)
        GLES20.glEnableVertexAttribArray(posLoc)

        vertexBuffer.position(2)
        GLES20.glVertexAttribPointer(texLoc, 2, GLES20.GL_FLOAT, false, 16, vertexBuffer)
        GLES20.glEnableVertexAttribArray(texLoc)

        GLES20.glUniform1i(modeLoc, mode.ordinal)

        // RGBA
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0)
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, rgbaTex)
        GLES20.glUniform1i(
            GLES20.glGetUniformLocation(program, "uTexRGBA"),
            0
        )

        // ETC1
        GLES20.glActiveTexture(GLES20.GL_TEXTURE1)
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, etc1Tex)
        GLES20.glUniform1i(
            GLES20.glGetUniformLocation(program, "uTexETC1"),
            1
        )

        // ================== DRAW N TIMES ==================
        for (i in 0 until drawCount) {
            GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 6)
        }
    }

    // ================= TEXTURE LOAD =================

    private fun loadRGBA(): Int {
        val tex = IntArray(1)
        GLES20.glGenTextures(1, tex, 0)
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, tex[0])

        val bmp = BitmapFactory.decodeResource(
            context.resources,
            R.drawable.texture_rgba
        )

        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bmp, 0)
        bmp.recycle()

        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR)
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR)
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE)
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE)

        return tex[0]
    }

    private fun loadETC1(): Int {
        val tex = IntArray(1)
        GLES20.glGenTextures(1, tex, 0)
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, tex[0])

        val input = context.resources.openRawResource(R.raw.texture_etc1)
        val etc1 = ETC1Util.createTexture(input)

        ETC1Util.loadTexture(
            GLES20.GL_TEXTURE_2D,
            0,
            0,
            GLES20.GL_RGB,
            GLES20.GL_UNSIGNED_BYTE,
            etc1
        )
        input.close()

        // ⚠️ BẮT BUỘC cho ETC1
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR)
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR)
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE)
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE)

        return tex[0]
    }
}
