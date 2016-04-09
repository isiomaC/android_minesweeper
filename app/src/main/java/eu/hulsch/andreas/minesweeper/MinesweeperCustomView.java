package eu.hulsch.andreas.minesweeper;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by murphy on 08.04.2016.
 */
public class MinesweeperCustomView extends View
{
    private static final float CELL_PADDING = 3.0f;
    private ArrayList<IMinesMarkedCountListener> markedCountListeners;

    private float width_height;
    private float cell_width_height;



    private Paint paint_black;
    private Paint paint_white;
    private Paint paint_gray;
    private Paint paint_red;
    private Paint paint_yellow;
    private Paint paint_black_text;
    private Paint paint_blue_text;
    private Paint paint_green_text;
    private Paint paint_yellow_text;
    private Paint paint_red_text;
    private float cellTextSize;

    private float[] pointsVertical;
    private float[] pointsHorizontal;

    private Point touchedDownCell;
    private MinesweepterCell[][] minesweepterCells;

    private boolean uncoverMode = true;
    private boolean failed = false;
    private int markedCount;

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
        invalidate();
    }

    private void init()
    {
        markedCountListeners = new ArrayList<>();
        this.uncoverMode = true;
        this.failed = false;
        this.markedCount = 0;


        this.cellTextSize = getResources().getDimension(R.dimen.cell_text_size);

        paint_black = new Paint();
        paint_black.setColor(Color.BLACK);

        paint_yellow = new Paint();
        paint_yellow.setColor(Color.YELLOW);

        paint_black_text = new Paint();
        paint_black_text.setColor(Color.BLACK);
        paint_black_text.setTextSize(cellTextSize);

        paint_blue_text = new Paint();
        paint_blue_text.setColor(Color.BLUE);
        paint_blue_text.setTextSize(cellTextSize);

        paint_green_text = new Paint();
        paint_green_text.setColor(Color.GREEN);
        paint_green_text.setTextSize(cellTextSize);

        paint_yellow_text = new Paint();
        paint_yellow_text.setColor(Color.YELLOW);
        paint_yellow_text.setTextSize(cellTextSize);

        paint_red_text = new Paint();
        paint_red_text.setColor(Color.RED);
        paint_red_text.setTextSize(cellTextSize);



        paint_white = new Paint();
        paint_white.setColor(Color.WHITE);
        paint_white.setStyle(Paint.Style.STROKE);
        paint_white.setStrokeWidth(1.0f);

        paint_gray = new Paint();
        paint_gray.setColor(Color.GRAY);

        paint_red = new Paint();
        paint_red.setColor(Color.RED);


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
                    switch (this.minesweepterCells[i][j].getMineCount())
                    {
                        case -1:
                            drawCell(canvas,i,j,this.paint_red, "M", this.paint_black_text);
                            break;
                        case 0:
                            drawCell(canvas,i,j,this.paint_gray);
                            break;
                        case 1:
                            drawCell(canvas,i,j,this.paint_gray, "1", this.paint_green_text);
                            break;
                        case 2:
                            drawCell(canvas,i,j,this.paint_gray, "2", this.paint_blue_text);
                            break;
                        case 3:
                            drawCell(canvas,i,j,this.paint_gray, "3", this.paint_yellow_text);
                            break;
                        case 4:
                            drawCell(canvas,i,j,this.paint_gray, "4", this.paint_red_text);
                            break;
                        case 5:
                            drawCell(canvas,i,j,this.paint_gray, "5", this.paint_red_text);
                            break;
                        case 6:
                            drawCell(canvas,i,j,this.paint_gray, "6", this.paint_red_text);
                            break;
                        case 7:
                            drawCell(canvas,i,j,this.paint_gray, "7", this.paint_red_text);
                            break;
                        case 8:
                            drawCell(canvas,i,j,this.paint_gray, "8", this.paint_red_text);
                            break;
                        default:
                            break;
                    }
                }
                else
                {

                    if(this.minesweepterCells[i][j].isMarked())
                    {
                        drawCell(canvas,i,j,this.paint_yellow);
                    }
                    else
                    {
                        drawCell(canvas,i,j,this.paint_black);
                    }
                }


            }
        }


    }

    public static float getCellPadding() {
        return CELL_PADDING;
    }

    public boolean onTouchEvent(MotionEvent event)
    {
        if(!this.failed)
        {
            // return super.onTouchEvent(event);
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                double x = (double) event.getX();
                double y = (double) event.getY();
                int column = (int) (x / cell_width_height);
                int row = (int) (y / cell_width_height);
                this.touchedDownCell = new Point(row, column);

            }
            if (event.getAction() == MotionEvent.ACTION_UP) {
                double x = (double) event.getX();
                double y = (double) event.getY();
                int row = (int) (y / cell_width_height);
                int column = (int) (x / cell_width_height);

                // same cell pressed and released
                if (this.touchedDownCell.equals(row, column))
                {
                    // uncover cells
                    if(this.uncoverMode)
                    {
                        // not marked
                        if(!this.minesweepterCells[row][column].isMarked())
                        {
                            this.minesweepterCells[row][column].setCovered(false);
                            // uncovered a mine
                            if (this.minesweepterCells[row][column].getMineCount() == -1)
                            {
                                this.failed = true;
                            }
                            else if(this.minesweepterCells[row][column].getMineCount() == 0)
                            {
                                // uncover nearby cells
                            }
                        }
                    }
                    // marking mode
                    else
                    {
                        if(this.minesweepterCells[row][column].isCovered())
                        {
                            if(!this.minesweepterCells[row][column].isMarked())
                            {
                              if(this.checkMarkedCount())
                              {
                                  this.minesweepterCells[row][column].setMarked(true);
                                  this.setMarkedCount(this.getMarkedCount() + 1);
                              }
                            }
                            else if(this.minesweepterCells[row][column].isMarked())
                            {
                                this.minesweepterCells[row][column].setMarked(false);
                                this.setMarkedCount(this.getMarkedCount() -1);
                            }


                        }
                    }
                }
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

    private boolean checkMarkedCount()
    {
        if(this.markedCount < 20)
        {
            return true;
        }
        return false;
    }

    public void drawCell(Canvas canvas, int row, int column, Paint cellBackgroundColor)
    {
        // left, top, right, bot, color
        float left = (float) ((column * this.cell_width_height) + CELL_PADDING);
        float top = (float) ((row *this.cell_width_height) + CELL_PADDING);
        float right = (float) (((column+1) * this.cell_width_height) - CELL_PADDING);
        float bottom = (float) (((row+1) *this.cell_width_height) - CELL_PADDING);

        canvas.drawRect(left, top, right, bottom, cellBackgroundColor);
        // Log.d("dqwdq", "height: " + (bottom-top) + " width: " + (right-left) + " row " + row + " column: " + column);


    }
    public void drawCell(Canvas canvas, int row, int column, Paint cellBackgroundColor, String cellText, Paint cellTextColor)
    {
        drawCell(canvas, row, column, cellBackgroundColor);
        drawText(canvas, cellTextColor, cellText, row, column);
    }

    public void drawText(Canvas canvas, Paint paint, String cellText, int row, int column)
    {

        int left = (int) ((column * this.cell_width_height) + CELL_PADDING);
        int top = (int) ((row *this.cell_width_height) + CELL_PADDING);
        int right = (int) (((column+1) * this.cell_width_height) - CELL_PADDING);
        int bottom =  (int) (((row+1) *this.cell_width_height) - CELL_PADDING);

        // the display area.
        Rect areaRect = new Rect(left, top, right, bottom);
        RectF bounds = new RectF(areaRect);
        // measure text width
        bounds.right = paint.measureText(cellText, 0, cellText.length());
        // measure text height
        bounds.bottom = paint.descent() - paint.ascent();
        bounds.left += (areaRect.width() - bounds.right) / 2.0f;
        bounds.top += (areaRect.height() - bounds.bottom) / 2.0f;
        canvas.drawText(cellText, bounds.left, bounds.top - paint.ascent(), paint);
    }

    public boolean isFailed() {
        return failed;
    }

    public void setFailed(boolean failed) {
        this.failed = failed;
    }

    public boolean isUncoverMode() {
        return uncoverMode;
    }

    public void setUncoverMode(boolean uncoverMode) {
        this.uncoverMode = uncoverMode;
    }
    public int getMarkedCount() {
        return markedCount;
    }

    public void setMarkedCount(int markedCount)
    {
        for(IMinesMarkedCountListener listener : this.markedCountListeners)
        {
            listener.onMarkedCountChanged(markedCount);
        }
        this.markedCount = markedCount;
    }
    public void addMinesMarkedCountListener(IMinesMarkedCountListener listener)
    {
        this.markedCountListeners.add(listener);
    }
    public void removeMinesMarkedCountListener(IMinesMarkedCountListener listener)
    {
        this.markedCountListeners.remove(listener);
    }


    public interface IMinesMarkedCountListener
    {
        void onMarkedCountChanged(int count);
    }


}
