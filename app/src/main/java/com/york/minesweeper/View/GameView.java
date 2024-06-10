package com.york.minesweeper.View;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.york.minesweeper.R;
import com.york.minesweeper.MainActivity;
import com.york.minesweeper.model.MineField;
import com.york.minesweeper.model.MinesweeperTimer;

public class GameView extends TableLayout {

    private Context context;
    private final int textSize;
    private final int UITextSize;
    private int gridSize;

    public GameView(Context context, int textSize, int gridSize) {
        super(context);
        this.context = context;
        this.textSize = textSize;
        this.UITextSize = textSize + 4;
        this.gridSize = gridSize;

        setOrientation(LinearLayout.VERTICAL);
    }

    public static int getColorForCellNum(int cellNum) {
        switch (cellNum) {
            case 1:
                return Color.BLUE;
            case 2:
                return Color.GREEN;
            case 3:
                return Color.RED;
            case 4:
                return R.color.dark_blue;
            case 5:
                return R.color.brown;
            case 6:
                return Color.CYAN;
            case 7:
                return Color.BLACK;
            case 8:
                return Color.GRAY;
        }
        return -1;
    }

    private TableRow configureTableRow(TableRow row) {
        row.setLayoutParams(new TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT
        ));

        return row;
    }

    private Button configureNewGameBtn(Button newGameBtn) {
        newGameBtn.setText("NEW GAME");
        newGameBtn.setTextSize(UITextSize);
        newGameBtn.setGravity(Gravity.CENTER);
        newGameBtn.setPadding(10, 3, 10, 3);
        newGameBtn.setBackgroundColor(Color.LTGRAY);
        TableRow.LayoutParams params = new TableRow.LayoutParams(
                0,
                TableRow.LayoutParams.WRAP_CONTENT,
                (float) (1.0 / 4.0)
        );
        params.setMargins(4, 4, 4, 4);
        newGameBtn.setLayoutParams(params);
        newGameBtn.setOnClickListener(v -> ((MainActivity) context).onClickNewGame());

        return newGameBtn;
    }

    private TextView configureUITextView(TextView textView) {
        textView.setTextSize(UITextSize);
        textView.setGravity(Gravity.CENTER);
        TableRow.LayoutParams params = new TableRow.LayoutParams(
                0,
                TableRow.LayoutParams.WRAP_CONTENT,
                (float) (1.0 / 4.0)
        );
        params.setMargins(4, 4, 4, 4);
        textView.setLayoutParams(params);

        return textView;
    }

    public GameView useField(MineField mineField) {

        for (int i = 0; i <= gridSize; i++) {
            TableRow curRow = configureTableRow(new TableRow(context));
            for (int j = 0; j <= gridSize; j++) {
                if (i == 0) {
                    curRow.addView(new GridLabel(context, textSize, gridSize, String.valueOf(j)));
                } else if (j == 0) {
                    curRow.addView(new GridLabel(
                            context, textSize, gridSize, String.valueOf((char) ('A' + (i-1)))));
                } else {
                    curRow.addView(mineField.cellAt(i-1, j-1));
                }
            }

            addView(curRow);
        }

        TableRow UIRow = configureTableRow(new TableRow(context));
        UIRow.addView(configureUITextView(mineField.getMineCounter()));
        UIRow.addView(configureNewGameBtn(mineField.getNewGameBtn()));
        MinesweeperTimer mineTimer = mineField.getTimer();
        mineTimer.setTextSize(UITextSize);
        UIRow.addView(mineTimer);
        UIRow.addView(configureUITextView(mineField.getScoreView()));
        addView(UIRow);

        return this;
    }
}
