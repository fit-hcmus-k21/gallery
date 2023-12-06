package com.example.gallery.ui.main.adapter;

import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class Square{
    private float vertices[] = {
            -1f,-1f,
            1f,-1f,
            -1f,1f,
            1f,1f
    };
    private float textureVertices[]={
            0f,1f,
            1f,1f,
            0f,0f,
            1f,0f
    };
    private FloatBuffer verticesBuffer;
    private FloatBuffer textureBuffer;
    private void initializeBuffers(){
        ByteBuffer buff = ByteBuffer.allocateDirect(vertices.length * 4);   // hàm khởi tạo buffer (nhân 4 vì mỗi float sẽ chiếm 4 byte)
        buff.order(ByteOrder.nativeOrder());                // xác định thứ tự các byte nằm bên dưới native flatform và set thứ tự của buffer theo value
        verticesBuffer = buff.asFloatBuffer();              // dùng để chuyển ByteBuffer về FloatBuffer
        verticesBuffer.put(vertices);                       // load mảng float vào Buffer
        verticesBuffer.position(0);                 // đảm bảo nằng buffer sẽ được đọc từ đầu
        buff = ByteBuffer.allocateDirect(textureVertices.length * 4);
        buff.order(ByteOrder.nativeOrder());
        textureBuffer = buff.asFloatBuffer();
        textureBuffer.put(textureVertices);
        textureBuffer.position(0);
    }
    private final String vertexShaderCode = "attribute highp vec4 aPosition;" +
            "attribute vec2 aTexPosition;"+
            "varying vec2 vTexPosition;"+
            "void main() {"+
            "gl_Position = aPosition;"+
            "vTexPosition = aTexPosition;"+
            "}";

    private final String fragmentShaderCode= "precision mediump float;"+
            "uniform sampler2D uTexture;"+
            "varying vec2 vTexPosition;"+
            "void main() {" +
            " gl_FragColor = texture2D(uTexture, vTexPosition);"+
            "}";

    private int vertexShader;
    private int fragmentShader;
    private int program;
    private void initializeProgram(){

        vertexShader = GLES20.glCreateShader(GLES20.GL_VERTEX_SHADER);
        GLES20.glShaderSource(vertexShader,vertexShaderCode);
        GLES20.glCompileShader(vertexShader);



        fragmentShader = GLES20.glCreateShader(GLES20.GL_FRAGMENT_SHADER);
        GLES20.glShaderSource(fragmentShader,fragmentShaderCode);
        GLES20.glCompileShader(fragmentShader);

        program = GLES20.glCreateProgram();
        GLES20.glAttachShader(program,vertexShader);
        GLES20.glAttachShader(program,fragmentShader);
        GLES20.glLinkProgram(program);




    }

    public void draw(int texture){
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);
        GLES20.glUseProgram(program);
        GLES20.glDisable(GLES20.GL_BLEND);
        int positionHandle = GLES20.glGetAttribLocation(program, "aPosition");
        int textureHandle = GLES20.glGetUniformLocation(program, "uTexture");
        int texturePositionHandle = GLES20.glGetAttribLocation(program, "aTexPosition");
        GLES20.glVertexAttribPointer(texturePositionHandle, 2, GLES20.GL_FLOAT, false, 0, textureBuffer);
        GLES20.glEnableVertexAttribArray(texturePositionHandle);
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texture);
        GLES20.glUniform1i(textureHandle, 0);
        GLES20.glVertexAttribPointer(positionHandle, 2, GLES20.GL_FLOAT, false, 0, verticesBuffer);
        GLES20.glEnableVertexAttribArray(positionHandle);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);
    }
    public Square(){
        initializeBuffers();
        initializeProgram();
    }

}



