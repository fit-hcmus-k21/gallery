package com.example.gallery.ui.main.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.effect.Effect;
import android.media.effect.EffectContext;
import android.media.effect.EffectFactory;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;
import android.widget.Toast;

import com.example.gallery.ui.main.doing.EditActivity;
import com.example.gallery.ui.main.doing.FilterAction;
import com.example.gallery.ui.main.doing.SharpAction;
import com.example.gallery.ui.main.doing.StraightenAction;

import java.nio.ByteBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class EffectRenderer implements GLSurfaceView.Renderer{
    EditActivity mainContext;
    FilterAction filterContext;
    SharpAction sharpContext;
    StraightenAction straightenContext;
    private Bitmap photo;
    private int photoWidth, photoHeight;
    String Type;


    public EffectRenderer(Context context, String type, Bitmap bitmap){
        super();
        Type = type;
        switch(type){
            case "FILTER":
                filterContext = (FilterAction) context;
                Toast.makeText(filterContext, Type, Toast.LENGTH_SHORT).show();
                break;
            case "STRAIGHTEN":
                straightenContext = (StraightenAction) context;
                Toast.makeText(straightenContext, Type, Toast.LENGTH_SHORT).show();
                break;
            case "SHARPEN":
                sharpContext = (SharpAction) context;
                Toast.makeText(sharpContext, Type, Toast.LENGTH_SHORT).show();
                break;
        }
        photo = bitmap;
    }

    private EffectContext effectContext;
    private Effect effect;

    private int textures[] = new int[2];
    private Square square;
    private void generateSquare(){
        GLES20.glGenTextures(2, textures, 0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textures[0]);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, photo, 0);
        square = new Square();
    }

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        // to do nothing
        GLES20.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int width, int height) {
        float ratio = (float) photo.getWidth() / photo.getHeight();
        //test
        if(photo.getWidth() < photo.getHeight()){
            int x = (EditActivity.deviceWidth - (int)(ratio*height))/2;
            GLES20.glViewport(x,0,(int)(ratio*height),height);
            photoHeight = height;
            photoWidth = (int)(ratio*height);
        }
        else{
            int y = (500-(int) (width/ratio))/2;
            GLES20.glViewport(0,0,width,(int)(width/ratio));
            photoWidth = width;
            photoHeight = (int) (width/ratio);
        }
        generateSquare();
    }

    @Override
    public void onDrawFrame(GL10 gl10) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        square.draw(textures[0]);
        if(effectContext == null){
            effectContext = EffectContext.createWithCurrentGlContext();
        }
        switch(Type){
            case "FILTER":{
                int check = filterContext.curEffect;
                switch(check){
                    case 1:
                        brightnessEffect(filterContext.parameterEffect/100);
                        square.draw(textures[1]);
                        break;
                    case 2:
                        documentaryEffect();
                        square.draw(textures[1]);
                        break;
                    case 3:
                        duotoneEffect();
                        square.draw(textures[1]);
                        break;
                    case 4:
                        grainEffect();
                        square.draw(textures[1]);
                        break;
                    case 5:
                        grayScaleEffect();
                        square.draw(textures[1]);
                        break;
                    case 6:
                        lomoishEffect();
                        square.draw(textures[1]);
                        break;
                    case 7:
                        negativeEffect();
                        square.draw(textures[1]);
                        break;
                    case 8:
                        posterizeEffect();
                        square.draw(textures[1]);
                        break;
                    case 9:
                        sepiaEffect();
                        square.draw(textures[1]);
                        break;
                    case 10:
                        tintEffect();
                        square.draw(textures[1]);
                        break;
                    case 11:
                        saturateEffect((filterContext.parameterEffect-50)/50);
                        square.draw(textures[1]);
                        break;
                    case 12:
                        temperatureEffect(filterContext.parameterEffect/100);
                        square.draw(textures[1]);
                        break;
                    case 13:
                        filllightEffect(filterContext.parameterEffect/100);
                        square.draw(textures[1]);
                        break;
                    default:
                        break;
                }
                break;
            }
            case "SHARPEN":{
                float scale = sharpContext.scale;
                SharpenEffect(scale);
                square.draw(textures[1]);
                break;
            }
            case "STRAIGHTEN":{
                int scale = straightenContext.ScaleParameter;
                StraightenEffect(scale);
                square.draw(textures[1]);
                break;
            }
        }
        int width = photoWidth;
        int height = photoHeight;
        //save texture to bitmap
        ByteBuffer pixelBuffer = ByteBuffer.allocateDirect(width * height * 4); // RGBA: 4 bytes per pixel
        GLES20.glReadPixels(0, 0, width, height, GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, pixelBuffer);
        pixelBuffer.rewind();

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        bitmap.copyPixelsFromBuffer(pixelBuffer);
        // rotate image(hệ tọa độ trong OpenGL và hệ tọa độ trong ImageView là khác nhau)
        Matrix matrix = new Matrix();
        matrix.postScale(1, -1); // Đảo ngược dọc
        EditActivity.temp = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);

    }

    private void grayScaleEffect(){
        EffectFactory factory = effectContext.getFactory();
        effect = factory.createEffect(EffectFactory.EFFECT_GRAYSCALE);
        effect.apply(textures[0],photoWidth,photoHeight,textures[1]);
    }
    private void documentaryEffect(){
        EffectFactory factory = effectContext.getFactory();
        effect = factory.createEffect(EffectFactory.EFFECT_DOCUMENTARY);
        effect.apply(textures[0],photoWidth,photoHeight,textures[1]);
    }
    private void brightnessEffect(float brightness){
        EffectFactory factory = effectContext.getFactory();
        effect = factory.createEffect(EffectFactory.EFFECT_BRIGHTNESS);
        effect.setParameter("brightness",brightness);
        effect.apply(textures[0],photoWidth,photoHeight,textures[1] );
    }
    private void grainEffect(){
        EffectFactory factory = effectContext.getFactory();
        effect = factory.createEffect(EffectFactory.EFFECT_GRAIN);
        effect.apply(textures[0],photoWidth,photoHeight,textures[1] );
    }
    private void lomoishEffect(){
        EffectFactory factory = effectContext.getFactory();
        effect = factory.createEffect(EffectFactory.EFFECT_LOMOISH);
        effect.apply(textures[0],photoWidth,photoHeight,textures[1] );
    }
    private void posterizeEffect(){
        EffectFactory factory = effectContext.getFactory();
        effect = factory.createEffect(EffectFactory.EFFECT_POSTERIZE);
        effect.apply(textures[0],photoWidth,photoHeight,textures[1] );
    }
    private void sepiaEffect(){
        EffectFactory factory = effectContext.getFactory();
        effect = factory.createEffect(EffectFactory.EFFECT_SEPIA);
        effect.apply(textures[0],photoWidth,photoHeight,textures[1] );
    }
    private void duotoneEffect(){
        EffectFactory factory = effectContext.getFactory();
        effect = factory.createEffect(EffectFactory.EFFECT_DUOTONE);
        effect.apply(textures[0],photoWidth,photoHeight,textures[1] );
    }
    private void negativeEffect(){
        EffectFactory factory = effectContext.getFactory();
        effect = factory.createEffect(EffectFactory.EFFECT_NEGATIVE);
        effect.apply(textures[0],photoWidth,photoHeight,textures[1] );
    }
    private void tintEffect(){
        EffectFactory factory = effectContext.getFactory();
        effect = factory.createEffect(EffectFactory.EFFECT_TINT);
        effect.apply(textures[0],photoWidth,photoHeight,textures[1] );
    }
    private void saturateEffect(float scale){
        EffectFactory factory = effectContext.getFactory();
        effect = factory.createEffect(EffectFactory.EFFECT_SATURATE);
        effect.setParameter("scale",scale);
        effect.apply(textures[0],photoWidth,photoHeight,textures[1] );
    }

    private void temperatureEffect(float scale){
        EffectFactory factory = effectContext.getFactory();
        effect = factory.createEffect(EffectFactory.EFFECT_TEMPERATURE);
        effect.setParameter("scale",scale);
        effect.apply(textures[0],photoWidth,photoHeight,textures[1] );
    }

    private void filllightEffect(float scale){
        EffectFactory factory = effectContext.getFactory();
        effect = factory.createEffect(EffectFactory.EFFECT_VIGNETTE);
        effect.setParameter("scale",scale);
        effect.apply(textures[0],photoWidth,photoHeight,textures[1] );
    }


//    -------------------------------


    private void SharpenEffect(float scale){               // scale value from 0 to 1
        EffectFactory factory = effectContext.getFactory();
        effect = factory.createEffect(EffectFactory.EFFECT_SHARPEN);
        effect.setParameter("scale",scale);
        effect.apply(textures[0],photoWidth,photoHeight,textures[1] );
    }

    private void StraightenEffect(float angle){     // angle in range [-45.0,45.0]
        EffectFactory factory = effectContext.getFactory();
        effect = factory.createEffect(EffectFactory.EFFECT_STRAIGHTEN);
        effect.setParameter("angle",angle);
        effect.apply(textures[0],photoWidth,photoHeight,textures[1] );
    }

}
