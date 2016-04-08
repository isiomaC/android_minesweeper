package eu.hulsch.andreas.minesweeper;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by murphy on 08.04.2016.
 */
public class MinesweeperCustomView extends View
{
    private int width_height;
    private int cell_width_height;

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

    }

    // gets called often
    public void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);

    }


    public boolean onTouchEvent(MotionEvent event)
    {
        return super.onTouchEvent(event);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        int height = getMeasuredHeight();
        int width = getMeasuredWidth();

        if (height >= width)
        {
            this.width_height = width;
        }
        else
        {
            this.width_height = height;
        }
        setMeasuredDimension(this.width_height, this.width_height);


    }
}
