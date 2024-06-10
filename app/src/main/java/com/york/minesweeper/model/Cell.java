package com.york.minesweeper.model;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.TableRow;

import androidx.appcompat.widget.AppCompatButton;

public class Cell extends AppCompatButton {

    public final int row;
    public final int column;
    public int mine;
    private boolean flagged;
    private boolean revealed;

    public Cell(Context context, int gridSize, int textSize, int row, int column) {
        super(context);
        this.row = row;
        this.column = column;
        mine = 0;
        TableRow.LayoutParams params = new TableRow.LayoutParams(
                0,
                TableRow.LayoutParams.WRAP_CONTENT,
                (float) (1.0 / (gridSize + 1))
        );
        params.setMargins(4, 4, 4, 4);
        setLayoutParams(params);
        setLongClickable(true);
        setOnClickListener((View.OnClickListener) context);
        setOnLongClickListener((View.OnLongClickListener) context);
        setTextSize(textSize);
        setBackgroundColor(Color.LTGRAY);
        setPadding(0, 0, 0, 0);
    }

    @Override
    protected void onMeasure(int widthMS, int heightMS) {
        int width = MeasureSpec.getSize(widthMS);
        setMeasuredDimension(width, width);
    }

    public boolean hasMine() {
        return mine == 1;
    }

    public boolean isFlagged() {
        return flagged;
    }

    public void setFlagged(boolean newValue) {
        flagged = newValue;
    }

    public String toString() {
        return String.format("Cell{row: %d col: %d}", row, column);
    }

    public boolean isRevealed() {
        return revealed;
    }

    public void setRevealed(boolean newValue) {
        revealed = newValue;
    }
}
