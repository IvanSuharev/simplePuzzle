package com.suharev.mydraw;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.Timer;

public class MyDraw extends View {
    boolean gameField[][], isGenerate = false;
    int n;
    int m;
    int r;
    int ro = 12;
    int[] arrayN = new int[]{3, 4, 5, 6, 7};
    int[] arrayM = new int[]{3, 4, 5, 6, 7};
    int[] arrayR = new int[]{100, 80, 65, 50, 45};
    int[] arrayColor = new int[]{Color.BLACK, Color.rgb(128, 128, 0), Color.GRAY, Color.MAGENTA, Color.RED};
    int indexSettings = 1;
    int cxButton, cyButton;
    int width;
    int height;
    Paint paint;

    public MyDraw(Context context) {
        super(context);
        paint = new Paint();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        width = canvas.getWidth();
        height = canvas.getHeight();
        paint.setColor(arrayColor[indexSettings]);
        n = arrayN[indexSettings];
        m = arrayM[indexSettings];
        r = arrayR[indexSettings];
        cxButton = width - r - 25;
        cyButton = height - r - 25;
        if (!isGenerate) {
            generateGameField(n, m);
            isGenerate = !isGenerate;
        }

        renderGameField(canvas);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            double touchX = event.getX();
            double touchY = event.getY();
            changeGameField(touchX, touchY);
            if (Math.pow(touchX - cxButton, 2) + Math.pow(touchY - cyButton, 2) <= r * r) {
                indexSettings++;
                if (indexSettings == arrayM.length) {
                    indexSettings = 0;
                }
                generateGameField(arrayN[indexSettings], arrayM[indexSettings]);
            }
        }
        return true;
    }
    public void renderGameField(Canvas canvas) {
        int cx = (width - (r * 2) * m - ro * (m - 1)) / 2 + r;
        int cy = (height - (r * 2) * n - ro * (n - 1)) / 2 + r;
        for (int i = 0; i < gameField.length; i++) {
            for (int j = 0; j < gameField[i].length; j++) {
                paint.setStyle((gameField[i][j]) ? Paint.Style.FILL_AND_STROKE : Paint.Style.STROKE);
                canvas.drawCircle(cx, cy, r, paint);
                cx += r * 2 + ro;
            }
            cx = (width - (r * 2) * m - ro * (m - 1)) / 2 + r;
            cy += r * 2 + ro;
        }
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        canvas.drawCircle(cxButton, cyButton, r, paint);
    }
    public void generateGameField(int height, int width){
        gameField = new boolean[height][width];
        Random random = new Random();
        for (int i = 0; i < gameField.length; i++) {
            for (int j = 0; j < gameField[i].length; j++) {
                gameField[i][j] = random.nextBoolean();
            }
        }
    }
    public void changeGameField(double tx, double ty) {
        int i = (int) (ty - (height - (r * 2) * m - ro * (m - 1)) / 2) / (2 * r + ro);
        int j = (int) (tx - (width - (r * 2) * n - ro * (n - 1)) / 2) / (2 * r + ro);
        int cx = (width - (r * 2) * n - ro * (n - 1)) / 2 + ((2 * r + ro) * j) + (r + ro);
        int cy = (height - (r * 2) * n - ro * (n - 1)) / 2 + ((2 * r + ro) * i) + (r + ro);
        if (Math.pow(tx - cx, 2) + Math.pow(ty - cy, 2) <= r * r) {
            for (int k = i - 1; k <= i + 1; k++) {
                for (int l = j - 1; l <= j + 1; l++) {
                    try {
                        gameField[k][l] = !gameField[k][l];
                    }catch (IndexOutOfBoundsException e) {}
                }
            }
        }
        if (checkWin()) {
            generateGameField(n, m);
        }
        invalidate();
    }
    public boolean checkWin(){
        Set<Boolean> isWin = new HashSet<>();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                isWin.add(gameField[i][j]);
            }
        }
        return (isWin.size() == 1);
    }
}
