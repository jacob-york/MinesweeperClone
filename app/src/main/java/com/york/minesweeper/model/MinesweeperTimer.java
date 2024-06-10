package com.york.minesweeper.model;

import android.content.Context;
import android.view.Gravity;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;

import java.util.Timer;
import java.util.TimerTask;

public class MinesweeperTimer extends AppCompatTextView {
    private int[] timeData;
    private TimerTask timerTask;
    private Timer timer;
    private Context context;
    private int timerTracker;
    private final int baseScore;
    private int score;
    private TextView scoreView;

    private static final int HOUR = 0;
    private static final int MINUTE = 1;
    private static final int SECOND = 2;


    public MinesweeperTimer(Context context, int initScore) {
        super(context);
        this.timer = new Timer();
        this.timeData = new int[3];
        this.context = context;
        this.timerTracker = 0;
        this.baseScore = initScore;
        this.score = baseScore;
        this.scoreView = new TextView(context);
        scoreView.setText(String.valueOf(score));

        setGravity(Gravity.CENTER);
        TableRow.LayoutParams params = new TableRow.LayoutParams(
                0,
                TableRow.LayoutParams.WRAP_CONTENT,
                (float) (1.0 / 3.0)
        );
        params.setMargins(4, 4, 4, 4);
        setLayoutParams(params);

        setText("0:00:00");
    }

    public int getCurScore() {
        return score;
    }

    public TextView getScoreView() {
        return scoreView;
    }

    public void wipeScore() {
        score = 0;
        ((AppCompatActivity) context).runOnUiThread(() ->
                scoreView.setText(String.valueOf(score)));
    }

    public void start() {
        timerTask = new TimerTask() {
            @Override
            public void run() {
                timeData[SECOND]++;
                if (timeData[SECOND] >= 60) {
                    timeData[SECOND] = 0;
                    timeData[MINUTE]++;
                } else if (timeData[MINUTE] >= 60) {
                    timeData[MINUTE] = 0;
                    timeData[HOUR]++;
                }
                ((AppCompatActivity) context).runOnUiThread(() ->
                        MinesweeperTimer.this.setText(getFormattedTime()));
                timerTracker++;
                if (timerTracker % 6 == 0 && score > 0) {
                    timerTracker = 0;
                    score--;
                    ((AppCompatActivity) context).runOnUiThread(() ->
                            scoreView.setText(String.valueOf(score)));
                }
            }
        };
        timer.scheduleAtFixedRate(timerTask, 0, 1000);
    }

    public void stop() {
        timerTask.cancel();
        timer.cancel();
    }

    public String getFormattedTime() {
        return timeData[HOUR] +
                ":" + String.format("%02d", timeData[MINUTE])  +
                ":" + String.format("%02d", timeData[SECOND]);
    }
}
