package com.example.android.rhythwalk;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.View;

public class WaveView extends View{
	
	byte[] waveform;
	
	public WaveView(Context context){
		super(context);
		
		waveform = null;
    }
	
	public void updateWaveform(byte[] waveform){
		this.waveform = waveform;
		postInvalidate();
	}

    // 描画処理
    public void onDraw(Canvas canvas){
		Paint paint = new Paint();

		drawArray(canvas, waveform, (int)(getHeight() * 0.5));

    	// 連続して描画する
//		invalidate();
//        try {
//            Thread.sleep(30);
//        } catch (InterruptedException e) {
//        }
    }
    
    // 波形が描けます
    private void drawArray(Canvas canvas, byte[] array, int zero_y){
    	Paint paint = new Paint();
		paint.setARGB(255, 36, 170, 0);
        int width = getWidth();
        int height = getHeight();
        if(array != null){
            for (int i = 0; i < array.length - 1; i++) {
            	int x1 = width * i / (array.length);
            	int y1 = zero_y + (int)(array[i] * (height / 128.0));
            	int x2 = width * (i + 1) / (array.length);
            	int y2 = zero_y - (int)(array[i + 1] * (height / 128.0));
    	        canvas.drawLine(x1, y1, x2, y2, paint);				
            }
        }
        canvas.drawLine(0, zero_y, width, zero_y, paint);    	        	
    }

}
