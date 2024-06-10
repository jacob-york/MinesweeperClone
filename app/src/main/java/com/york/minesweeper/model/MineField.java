package com.york.minesweeper.model;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.ColorRes;
import androidx.core.content.ContextCompat;

import com.york.minesweeper.R;
import com.york.minesweeper.View.GameView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Somewhat like the backend logic/model (like in MVC)
 * I conformed to MVC very loosely
 */
public class MineField {
    private final Cell[][] grid;
    private Context context;
    public final int mines;
    private final int minelessCells;
    private int minesLeft;
    private int minelessLeft;
    public final int gridSize;
    private int textSize;
    private MinesweeperTimer timer;
    private Button newGameBtn;
    private TextView mineCounter;
    private boolean gameOver;

    public MineField(Context context, int textSize, int gridSize, int mines) {
        grid = new Cell[gridSize][gridSize];
        this.context = context;
        this.mines = mines;
        this.minelessCells = (gridSize * gridSize) - mines;

        this.minesLeft = mines;
        this.minelessLeft = minelessCells;

        this.textSize = textSize;
        this.gridSize = gridSize;

        this.mineCounter = new TextView(context);
        mineCounter.setText(String.valueOf(mines));
        this.timer = new MinesweeperTimer(context, (gridSize * gridSize) + mines);
        this.newGameBtn = new Button(context);

        initGrid();
        addMines();
    }

    public void toggleFlag(Cell cell) {
        if (cell.isFlagged()) {
            cell.setBackgroundColor(Color.LTGRAY);
            minesLeft++;
            cell.setFlagged(false);
        } else if (!cell.isRevealed() && minesLeft > 0) {
            cell.setBackgroundColor(Color.YELLOW);
            minesLeft--;
            cell.setFlagged(true);

            if (minelessLeft == 0 && minesLeft == 0) {
                winGame();
            }
        }

        mineCounter.setText(String.valueOf(minesLeft));
    }

    private void initGrid() {
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                grid[i][j] = new Cell(context, gridSize, textSize, i, j);
            }
        }
    }

    public int getCurScore() {
        return timer.getCurScore();
    }

    private void addMines() {
        for (int i = 0; i < mines; i++) {
            int mineX;
            int mineY;
            do {
                mineX = (int) Math.floor(Math.random() * gridSize);
                mineY = (int) Math.floor(Math.random() * gridSize);
            } while (grid[mineY][mineX].mine == 1);
            grid[mineY][mineX].mine = 1;
        }
    }

    private List<Cell> getSurrounding(Cell cell) {
        List<Cell> surrounding = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                surrounding.add(cellAt(cell.row + (i-1), cell.column + (j-1)));
            }
        }

        return surrounding.stream().filter(Objects::nonNull).collect(Collectors.toList());
    }

    public Cell cellAt(int row, int col) {
        if (row < 0 || row >= grid.length) return null;
        if (col < 0 || col >= grid[0].length) return null;
        return grid[row][col];
    }

    public void loseGame() {
        timer.wipeScore();
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                Cell cell = grid[i][j];
                if (cell.hasMine() && !cell.isRevealed() && !cell.isFlagged()) {
                    cell.setBackgroundColor(Color.BLACK);
                } else if (!cell.hasMine() && cell.isFlagged()) {
                    cell.setText("X");
                }
            }
        }
        newGameBtn.setBackgroundColor(colorFromId(R.color.light_blue));
        stopGame();
        Log.i("i", "user lost the game");
    }

    private int colorFromId(@ColorRes int id) {
        return ContextCompat.getColor(context, id);
    }

    private void stopGame() {
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                grid[i][j].setClickable(false);
            }
        }
        timer.stop();
        gameOver = true;
    }

    public void winGame() {
        newGameBtn.setBackgroundColor(Color.GREEN);
        stopGame();
        Log.i("i", "user won the game.");
    }

    public void revealCell(Cell rCell) {
        if (rCell.isRevealed()) return;
        rCell.setRevealed(true);
        rCell.setClickable(false);

        if (rCell.hasMine()) {
            rCell.setBackgroundColor(Color.RED);
            loseGame();
        } else {
            rCell.setBackgroundColor(colorFromId(R.color.dark_gray));
            minelessLeft--;
            Log.i("i", String.format(
                    "cell (r=%d, c=%d) cleared (mineless cells remaining: %d).",
                    rCell.row,
                    rCell.column,
                    minelessLeft)
            );
            if (minelessLeft == 0 && minesLeft == 0) {
                winGame();
                return;
            }
            int surroundingMineCnt = countSurroundingMines(rCell);
            if (surroundingMineCnt == 0) {
                List<Cell> surrounding = getSurrounding(rCell);
                for (Cell curCell : surrounding) {
                    if (!curCell.isFlagged() && !curCell.hasMine()) {
                        revealCell(curCell);
                    }
                }
            } else {
                rCell.setText(String.valueOf(surroundingMineCnt));
                rCell.setTextColor(GameView.getColorForCellNum(surroundingMineCnt));
            }
        }
    }

    public int countSurroundingMines(Cell cell) {
        return (int) getSurrounding(cell).stream()
                .filter(Cell::hasMine)
                .count();
    }

    public MinesweeperTimer getTimer() {
        return timer;
    }

    public Button getNewGameBtn() {
        return newGameBtn;
    }

    public TextView getMineCounter() {
        return mineCounter;
    }

    public TextView getScoreView() {
        return timer.getScoreView();
    }

    public boolean isGameOver() {
        return gameOver;
    }
}
