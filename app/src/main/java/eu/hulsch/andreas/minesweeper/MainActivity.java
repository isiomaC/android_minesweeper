package eu.hulsch.andreas.minesweeper;

import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
{
    private static final int nMines = 20;
    private static final int rows_columns = 10;

    private MinesweeperCustomView minesweeperCustomView;
    private TextView numberOfMines;
    private TextView numberOfMinesMarked;
    private Button btnReset;
    private Button btnClickMode;

    private MinesweepterCell[][] minesweepterCells;
    private ArrayList<Point> mines;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        initCells();
    }

    private void initViews()
    {
        this.minesweeperCustomView = (MinesweeperCustomView) findViewById(R.id.minesweepter_custom_view);
        this.numberOfMines = (TextView) findViewById(R.id.number_mines);
        this.numberOfMinesMarked = (TextView) findViewById(R.id.number_mines_marked);
        this.btnReset = (Button) findViewById(R.id.btn_reset);
        this.btnClickMode = (Button) findViewById(R.id.btn_click_mode);
    }
    private void initCells()
    {
        this.minesweepterCells = new MinesweepterCell[rows_columns][rows_columns];
        int k = 0;
        mines = new ArrayList<>();
        while(k < this.nMines)
        {
            int randomRow, randomColumn;
            randomRow = (int) (Math.random() *this.minesweepterCells.length);
            randomColumn = (int) (Math.random() *this.minesweepterCells[0].length);
            Point mine = new Point(randomRow, randomColumn);
            if(!mines.contains(mine))
            {
                mines.add(mine);
                k++;
            }


        }

        // init the cells
        for(int i = 0; i < this.minesweepterCells.length; i++)
        {
            for(int j = 0; j < this.minesweepterCells[i].length; j++)
            {
                this.minesweepterCells[i][j] = new MinesweepterCell(false, true, getMineCount(i,j), i, j);
            }
        }
        this.minesweeperCustomView.setArray(this.minesweepterCells);
    }
    private int getMineCount(int row, int column)
    {
        if(mines.contains(new Point(row, column)))
        {
            return -1;
        }
        else
        {
            int mineCount = 0;
            for(int i = -1; i < 2; i++)
            {
                for(int j = -1; j < 2; j++)
                {
                    if(row+i < 0 || row+i > 10 || column+j < 0 || column+j > 10)
                    {
                        continue;
                    }
                    if(mines.contains(new Point(row+i,column+j)))
                    {
                        mineCount++;
                    }

                }
            }

            return mineCount;
        }
    }




}
