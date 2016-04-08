package eu.hulsch.andreas.minesweeper;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by murphy on 08.04.2016.
 */
public class MinesweeperCustomView extends View
{
    private int width_height;
    private double cell_width_height;

    private Paint paint_black;
    private Paint paint_white;

    private float[] pointsVertical;
    private float[] pointsHorizontal;

    // ctor
    public MinesweeperCustomView(Context context)
    {
        super(context);
        init();
    }
    public MinesweeperCustomView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    public MinesweeperCustomView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init()
    {
        paint_black = new Paint();
        paint_black.setColor(Color.BLACK);

        paint_white = new Paint();
        paint_white.setColor(Color.WHITE);
        paint_white.setStyle(Paint.Style.STROKE);

    }

    // gets called very often
    public void onDraw(Canvas canvas)
    {
        //super.onDraw(canvas);
        canvas.drawRect(0, 0, this.width_height, this.width_height, this.paint_black);
        // canvas.drawRect(0,0, this.width_height, this.width_height, this.paint_white);

        canvas.drawLines(this.pointsHorizontal, this.paint_white);
        canvas.drawLines(this.pointsVertical, this.paint_white);


    }


    public boolean onTouchEvent(MotionEvent event)
    {
        return super.onTouchEvent(event);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        setPadding(0,0,0,0);

        int height = getMeasuredHeight();
        int width = getMeasuredWidth();

        if (height > width)
        {
            this.width_height = width;
        }
        else
        {
            this.width_height = height;
        }
        this.cell_width_height = (double) this.width_height/10;

        this.pointsVertical = new float[44];
        for(int i = 0; i < this.pointsVertical.length; i=i+4)
        {
            // first point x, y
            this.pointsVertical[i] = (float) (this.cell_width_height * (i/4));
            this.pointsVertical[i+1] = 0f;
            // second point x, y
            this.pointsVertical[i+2] = (float) (this.cell_width_height*(i/4));
            this.pointsVertical[i+3] = (float) this.width_height;
        }

        this.pointsHorizontal = new float[44];
        for(int i = 0; i < this.pointsHorizontal.length; i=i+4)
        {
            // first point x, y
            this.pointsHorizontal[i] = 0f;
            this.pointsHorizontal[i+1] = (float) (this.cell_width_height * (i/4));
            // second point x, y
            this.pointsHorizontal[i+2] = (float) this.width_height;
            this.pointsHorizontal[i+3] = (float) (this.cell_width_height*(i/4));
        }

        setMeasuredDimension(this.width_height, this.width_height);
    }

}
