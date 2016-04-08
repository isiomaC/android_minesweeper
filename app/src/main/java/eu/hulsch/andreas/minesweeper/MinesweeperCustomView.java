package eu.hulsch.andreas.minesweeper;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by murphy on 08.04.2016.
 */
public class MinesweeperCustomView extends View
{
    private static final float CELL_PADDING = 3.0f;

    private float width_height;
    private float cell_width_height;


    private Paint paint_black;
    private Paint paint_white;
    private Paint paint_gray;


    private float[] pointsVertical;
    private float[] pointsHorizontal;

    private Point touchedDownCell;
    private MinesweepterCell[][] minesweepterCells;

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

    public void setArray(MinesweepterCell[][] minesweepterCells)
    {
        this.minesweepterCells = minesweepterCells;
    }

    private void init()
    {
        paint_black = new Paint();
        paint_black.setColor(Color.BLACK);

        paint_white = new Paint();
        paint_white.setColor(Color.WHITE);
        paint_white.setStyle(Paint.Style.STROKE);
        paint_white.setStrokeWidth(1.0f);


        paint_gray = new Paint();
        paint_gray.setColor(Color.GRAY);

    }


    // gets called very often
    public void onDraw(Canvas canvas)
    {
        //super.onDraw(canvas);
        canvas.drawRect(0, 0, this.width_height, this.width_height, this.paint_black);
        // canvas.drawRect(0,0, this.width_height, this.width_height, this.paint_white);

        canvas.drawLines(this.pointsHorizontal, this.paint_white);
        canvas.drawLines(this.pointsVertical, this.paint_white);


        for(int i = 0; i < this.minesweepterCells.length; i++)
        {
            for(int j = 0; j < this.minesweepterCells[i].length; j++)
            {
                if(!this.minesweepterCells[i][j].isCovered())
                {
                    drawCell(canvas,i,j,this.paint_gray);

                }
            }
        }


    }

    public boolean onTouchEvent(MotionEvent event)
    {
        // return super.onTouchEvent(event);
        if(event.getAction() == MotionEvent.ACTION_DOWN)
        {
            double x = (double) event.getX();
            double y = (double) event.getY();
            int column = (int) (x / cell_width_height);
            int row = (int) (y / cell_width_height);
            this.touchedDownCell = new Point(row, column);

        }
        if(event.getAction() == MotionEvent.ACTION_UP)
        {
            double x = (double) event.getX();
            double y = (double) event.getY();
            int row = (int) (y / cell_width_height);
            int column = (int) (x / cell_width_height);

            // same cell pressed and released
            if(this.touchedDownCell.equals(row, column))
            {
                this.minesweepterCells[row][column].setCovered(false);

            }
        }
        invalidate();
        return true;
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

        this.cell_width_height = (float) (this.width_height/10);

        this.pointsVertical = new float[44];
        for(int i = 0; i < this.pointsVertical.length; i=i+4)
        {
            if(i == this.pointsVertical.length-4)
            {
                // first point x, y
                this.pointsVertical[i] = (float) (this.cell_width_height * (i/4)-1);
                this.pointsVertical[i+1] = 0f;
                // second point x, y
                this.pointsVertical[i+2] = (float)((this.cell_width_height * (i / 4)) - 1);
                this.pointsVertical[i+3] = (float) (this.width_height-1);
                continue;
            }
            // first point x, y
            this.pointsVertical[i] = (float)(this.cell_width_height * (i/4));
            this.pointsVertical[i+1] = 0f;
            // second point x, y
            this.pointsVertical[i+2] = (float) (this.cell_width_height * (i/4));
            this.pointsVertical[i+3] = (float) this.width_height;
        }

        this.pointsHorizontal = new float[44];
        // each iteration is for one line
        for(int i = 0; i < this.pointsHorizontal.length; i=i+4)
        {
            if(i == this.pointsHorizontal.length-4)
            {
                // first point x, y
                this.pointsHorizontal[i] = 0f;
                this.pointsHorizontal[i+1] = (float) (this.cell_width_height * (i/4))-1;
                // second point x, y
                this.pointsHorizontal[i+2] = (float) (this.width_height-1);
                this.pointsHorizontal[i+3] = (float) ((this.cell_width_height*(i/4))-1);
                continue;
            }
            // first point x, y
            this.pointsHorizontal[i] = 0f;
            this.pointsHorizontal[i+1] = (float) (this.cell_width_height * (i/4));
            // second point x, y
            this.pointsHorizontal[i+2] = (float) this.width_height ;
            this.pointsHorizontal[i+3] = (float) (this.cell_width_height*(i/4));

        }

        setMeasuredDimension((int) this.width_height, (int) this.width_height);
    }

    public void drawCell(Canvas canvas, int row, int column, Paint cellBackgroundColor)
    {
        // left, top, right, bot, color

        float left = (float) ((column * this.cell_width_height) + CELL_PADDING);
        float top = (float) ((row *this.cell_width_height) + CELL_PADDING);
        float right = (float) (((column+1) * this.cell_width_height) - CELL_PADDING);
        float bottom = (float) (((row+1) *this.cell_width_height) - CELL_PADDING);
        canvas.drawRect(left,top,right,bottom, this.paint_gray);
        Log.d("dqwdq", "height: " + (bottom-top) + " width: " + (right-left) + " row " + row + " column: " + column);


    }
    public void drawCell(Canvas canvas, int row, int column, Paint cellBackgroundColor, String cellText, Paint cellTextColor)
    {
        drawCell(canvas, row, column,cellBackgroundColor);

    }






}
