package com.york.minesweeper.View;


import android.content.Context;
import android.view.Gravity;
import android.widget.TableRow;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;

public class GridLabel extends AppCompatTextView {


    public GridLabel(@NonNull Context context, int textSize, int gridSize, String text) {
        super(context);

        setTextSize(textSize);
        setText(text);

        setGravity(Gravity.CENTER);
        TableRow.LayoutParams params = new TableRow.LayoutParams(
                0,
                TableRow.LayoutParams.WRAP_CONTENT,
                (float) (1.0 / (gridSize + 1))
        );
        setLayoutParams(params);
    }

    @Override
    protected void onMeasure(int widthMS, int heightMS) {
        int width = MeasureSpec.getSize(widthMS);
        setMeasuredDimension(width, width);
    }
}
