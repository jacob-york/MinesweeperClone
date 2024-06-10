package com.york.minesweeper.View;

import android.content.Context;
import android.view.Gravity;
import android.widget.TableRow;
import android.widget.TextView;

import com.york.minesweeper.model.ScoreEntry;

public class ScoreEntryView extends TableRow {

    private final TextView rank;
    private final TextView name;
    private final TextView score;
    private final ScoreEntry entry;

    public ScoreEntryView(Context context, int rank, String name, int score, ScoreEntry entry) {
        super(context);
        TableRow.LayoutParams rowParams = new TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT
        );
        setLayoutParams(rowParams);
        this.rank = applyFormats(new TextView(context), String.valueOf(rank));
        this.name = applyFormats(new TextView(context), name);
        this.score = applyFormats(new TextView(context), String.format("%03d", score));
        addView(this.rank);
        addView(this.name);
        addView(this.score);

        this.entry = entry;
    }

    private static TextView applyFormats(TextView textView, String text) {
        textView.setText(text);
        TableRow.LayoutParams params = new TableRow.LayoutParams(
                0,
                TableRow.LayoutParams.WRAP_CONTENT,
                0.33f
        );
        textView.setLayoutParams(params);
        textView.setGravity(Gravity.CENTER);
        textView.setTextSize(20);
        return textView;
    }

    public TextView getRankView() {
        return rank;
    }
    public TextView getNameView() {
        return name;
    }
    public TextView getScoreView() {
        return score;
    }
    public ScoreEntry getRelatedScoreEntry() {
        return entry;
    }
}
