package com.tona.myapplication.openglesdemo

import android.opengl.GLES20
import android.util.Log

object ShaderUtils {

    fun loadShader(type: Int, code: String): Int {
        val shader = GLES20.glCreateShader(type)
        GLES20.glShaderSource(shader, code)
        GLES20.glCompileShader(shader)

        val compiled = IntArray(1)
        GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, compiled, 0)

        if (compiled[0] == 0) {
            Log.e("Shader", GLES20.glGetShaderInfoLog(shader))
            GLES20.glDeleteShader(shader)
        }
        return shader
    }

    fun createProgram(vs: String, fs: String): Int {
        val vertex = loadShader(GLES20.GL_VERTEX_SHADER, vs)
        val fragment = loadShader(GLES20.GL_FRAGMENT_SHADER, fs)

        val program = GLES20.glCreateProgram()
        GLES20.glAttachShader(program, vertex)
        GLES20.glAttachShader(program, fragment)
        GLES20.glLinkProgram(program)
        return program
    }
}
