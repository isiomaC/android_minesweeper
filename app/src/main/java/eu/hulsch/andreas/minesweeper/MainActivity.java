package eu.hulsch.andreas.minesweeper;

import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
{
    private static final int N_MINES = 20;
    private static final int ROWS_COLUMNS = 10;

    private MinesweeperCustomView minesweeperCustomView;
    private TextView numberOfMines;
    private TextView numberOfMinesMarked;
    private Button btnReset;
    private Button btnClickMode;

    private MinesweeperCell[][] minesweeperCells;
    private ArrayList<Point> mines;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        reset();
    }

    private void initViews()
    {
        this.minesweeperCustomView = (MinesweeperCustomView) findViewById(R.id.minesweepter_custom_view);
        this.numberOfMines = (TextView) findViewById(R.id.number_mines);
        this.numberOfMinesMarked = (TextView) findViewById(R.id.number_mines_marked);
        this.btnReset = (Button) findViewById(R.id.btn_reset);
        this.btnClickMode = (Button) findViewById(R.id.btn_click_mode);

        this.minesweeperCustomView.addMinesMarkedCountListener(new MinesweeperCustomView.IMinesMarkedCountListener() {
            @Override
            public void onMarkedCountChanged(int count) {
                setTextViewMinesMarkedCount(count);
            }
        });

        this.btnClickMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeMode();
            }
        });

        this.btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reset();
            }
        });


    }

    private void setTextViewMinesMarkedCount(int count)
    {
        String text = getString(R.string.number_mines_marked, count);
        this.numberOfMinesMarked.setText(text);
    }

    private void changeMode()
    {
        this.minesweeperCustomView.setUncoverMode(!this.minesweeperCustomView.isUncoverMode());
        if(this.minesweeperCustomView.isUncoverMode())
        {
            this.btnClickMode.setText(R.string.marking_mode);
        }
        else
        {
            this.btnClickMode.setText(R.string.uncover_mode);
        }

    }

    private void reset()
    {
        this.initCells();
        this.minesweeperCustomView.setFailed(false);
        this.minesweeperCustomView.setUncoverMode(true);
        this.btnClickMode.setText(R.string.marking_mode);
       // String text = getString(R.string.number_mines_marked, 0);
       // this.numberOfMinesMarked.setText(text);
        this.minesweeperCustomView.setMarkedCount(0);
        String totalMines = getString(R.string.total_number_of_mines, this.getTotalNumberOfMines());
        this.numberOfMines.setText(totalMines);
    }
    // init the two dimensional array of minesweepercells
    private void initCells()
    {
        this.minesweeperCells = new MinesweeperCell[ROWS_COLUMNS][ROWS_COLUMNS];
        int k = 0;
        mines = new ArrayList<>();
        while(k < N_MINES)
        {
            int randomRow, randomColumn;
            randomRow = (int) (Math.random() *this.minesweeperCells.length);
            randomColumn = (int) (Math.random() *this.minesweeperCells[0].length);
            Point mine = new Point(randomRow, randomColumn);
            if(!mines.contains(mine))
            {
                mines.add(mine);
                k++;
            }
        }
        // init the cells
        for(int i = 0; i < this.minesweeperCells.length; i++)
        {
            for(int j = 0; j < this.minesweeperCells[i].length; j++)
            {
                this.minesweeperCells[i][j] = new MinesweeperCell(false, true, getMineCount(i,j), i, j);
            }
        }
        this.minesweeperCustomView.setArray(this.minesweeperCells);
    }
    // the mine count for a cell (measured by nearby cells)
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
                    if(row+i < 0 || row+i > this.minesweeperCells.length ||
                            column+j < 0 || column+j > this.minesweeperCells.length)
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

    private int getTotalNumberOfMines()
    {
        int number = 0;
       for(int i = 0; i< this.minesweeperCells.length; i++)
       {
           for(int j = 0; j < this.minesweeperCells[i].length;j++)
           {
               if(this.minesweeperCells[i][j].getMineCount() == -1)
               {
                   number++;
               }
           }
       }
        return number;
    }



}
