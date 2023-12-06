package com.example.gallery.ui.main.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.text.TextPaint;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.gallery.ui.main.doing.EditActivity;

public class MyCustomView extends View {
    private boolean drawLine = false, drawText = false;
    String input = null;
    Bitmap bitmap = null;
    Context ctx;
    Paint paint,line;
    Path touch ;
    private  Bitmap b;
    private Canvas c;
    private TextPaint Dtext = new TextPaint();
    float textX,textY;
    Rect textBound;
    boolean check = true;
    String type = null;
    float OvalLeft = -1,OvalTop = -1,OvalRight = -1,OvalBottom = -1;

    public MyCustomView(Context context, Bitmap input) {
        super(context);
        bitmap = input;
        this.ctx = context;
        paint = new Paint();
        paint.setColor(Color.RED);
        line = new Paint();
        line.setColor(Color.WHITE);
        line.setAntiAlias(true);
        line.setStyle(Paint.Style.STROKE);
        line.setStrokeWidth(15);
        touch = new Path();
//        Dtext = new TextPaint();
//        Dtext.setColor(Color.WHITE);
//        Dtext.setTextSize(30);
//        Dtext.setAntiAlias(true);
        textX = 660/2;
        textY = 1466/2;
        textBound = new Rect();
    }
    @SuppressLint("ResourceAsColor")
    @Override
    public void onDraw(@NonNull Canvas canvas){
        super.onDraw(canvas);
        // Kiểm tra nếu bitmap không null
        if (bitmap != null) {
                       //Vẽ ảnh với kích thước mới
            Rect srcRect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight()   );
            Rect destRect = new Rect(0, 0, getWidth(), getHeight());
            canvas.drawBitmap(bitmap, srcRect, destRect, null);
        }
        if(drawLine == true){
            //draw line
            if(type.equals("Curve")){
                canvas.drawBitmap(b, 0, 0, paint);
                canvas.drawPath(touch, line);
            }
            if(type.equals("Circle")){
                canvas.drawBitmap(b,0,0,paint);
                canvas.drawOval(OvalLeft,OvalTop,OvalRight,OvalBottom,line);
            }
            if(type.equals("Square")){
                canvas.drawBitmap(b,0,0,paint);
                canvas.drawRect(OvalLeft,OvalTop,OvalRight,OvalBottom,line);
            }
            if(type.equals("Line")){
                canvas.drawBitmap(b,0,0,paint);
                canvas.drawLine(OvalLeft,OvalTop,OvalRight,OvalBottom,line);
            }


        }
        if(drawText == true){
            //draw text
            Dtext.getTextBounds(input,0,input.length(),textBound);
            canvas.drawText(input,textX,textY,Dtext);
            textBound.offset((int)textX,(int)textY);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        // Kiểm tra kích thước ảnh và điều chỉnh kích thước của MyCustomView
        if (bitmap != null) {
            int imageWidth = bitmap.getWidth();
            int imageHeight = bitmap.getHeight();

            if (imageWidth > 0 && imageHeight > 0) {
                float ratio = (float) imageWidth / imageHeight;

                //test
                if((float)width/height != ratio){
                    if(ratio > 1){
                        height = (int)(width/ratio);
                    }
                    if(ratio < 1){
                        width = (int)(height*ratio);
                    }

                }

//                if (width > height * ratio) {
//                    width = (int) (height * ratio);
//                } else {
//                    height = (int) (width / ratio);
//                }
            }
        }

        setMeasuredDimension(width, height);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(drawLine == true && type.equals("Curve")) {
            float touchX = event.getX();
            float touchY = event.getY();
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    touch.moveTo(touchX, touchY);
                    break;
                case MotionEvent.ACTION_MOVE:
                    touch.lineTo(touchX, touchY);
                    break;
                case MotionEvent.ACTION_UP:
                    touch.lineTo(touchX, touchY);
                    c.drawPath(touch, line);
                    touch = new Path();
                    break;
                default:
                    return false;
            }
            invalidate();
        }
        if(drawLine == true && (type.equals("Circle") || type == "Square" || type.equals("Line"))){
            float eventX = event.getX(), eventY = event.getY();
            switch (event.getAction()){
                case MotionEvent.ACTION_DOWN:
                    OvalLeft = eventX;
                    OvalTop = eventY;
                    break;
                case MotionEvent.ACTION_MOVE:
                    OvalBottom = eventY;
                    OvalRight = eventX;
                    break;

                case MotionEvent.ACTION_UP:
                    OvalBottom = eventY;
                    OvalRight = eventX;
                    if(type.equals("Circle"))
                        c.drawOval(OvalLeft,OvalTop,OvalRight,OvalBottom,line);
                    else if(type.equals("Square"))
                        c.drawRect(OvalLeft,OvalTop,OvalRight,OvalBottom,line);
                    else
                        c.drawLine(OvalLeft,OvalTop,OvalRight,OvalBottom,line);
                    break;
            }
            invalidate();
        }
        if(drawText == true){

            float eventX = event.getX(), eventY = event.getY();
            switch(event.getAction()){
                case MotionEvent.ACTION_DOWN:
                    if (textBound.contains((int)event.getX(),(int)event.getY())){
                        check = true;
                        Toast.makeText(ctx,String.valueOf(check),Toast.LENGTH_SHORT).show();
                    }
                    else{
                        check = false;
                        Toast.makeText(ctx,"OUT OF BOUND",Toast.LENGTH_SHORT).show();
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    //to do nothing
                    if(check == true){
                        textX = eventX;
                        textY = eventY;
                        invalidate();
                    }
                    check = false;
                    break;
                case MotionEvent.ACTION_MOVE:
                    if(check == true){
                        textX = eventX;
                        textY = eventY;
                        invalidate();
                    }
                    break;
            }
            invalidate();


        }
        return true;
    }
    public Bitmap saveDrawingToBitmap() {
        setDrawingCacheEnabled(true);
        Bitmap drawingCache = getDrawingCache().copy(Bitmap.Config.ARGB_8888, false);
        setDrawingCacheEnabled(false);
        return drawingCache;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        b = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        c = new Canvas(b);
    }

    public void onUpdateDrawFunction(boolean action, String text,int size, int color){
        if(action == true){     // draw line
            type = text;
            line.setColor(color);
            line.setStrokeWidth(size);
            drawLine = true;
            drawText = false;
            Toast.makeText(ctx,text,Toast.LENGTH_SHORT).show();

        }
        if(action == false){      //draw text
            Dtext.setTextSize(size);
            if(size == 0)
                Dtext.setTextSize(30);
            Dtext.setColor(color);
            drawText = true;
            input = text;
            drawLine = false;
        }
        invalidate();
    }

//    public float Spacing(MotionEvent event){
//        float x = event.getX() - Circlex;
//        float y = event.getY() - Circley;
//        return (float) Math.sqrt(x*x+y*y);
//    }
}
