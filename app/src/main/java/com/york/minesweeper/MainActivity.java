package com.york.minesweeper;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.york.minesweeper.View.GameView;
import com.york.minesweeper.View.HighScoresDialog;
import com.york.minesweeper.View.ScoreEntryDialog;
import com.york.minesweeper.View.ScoreEntryView;
import com.york.minesweeper.model.Cell;
import com.york.minesweeper.model.HighScoreTable;
import com.york.minesweeper.model.MineField;
import com.york.minesweeper.model.ScoreEntry;

import org.json.JSONException;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity
implements View.OnClickListener, View.OnLongClickListener {

    private int gridSize = 10;
    private static final int MAX_GRID_SIZE = 15;
    private static final int MIN_GRID_SIZE = 5;
    private static final String JSON_SAVE = "scores.json";
    private int mines = 20;
    private static final double MAX_MINES_RATIO = .3;
    private static final double MIN_MINES_RATIO = .1;
    private Button upGrid;
    private Button downGrid;
    private TextView gridSizeTxt;
    private int textSize;
    private MineField mineField;
    private GameView gameView;
    private HighScoreTable highScores;
    private int clicks;
    private Button downMines;
    private TextView minesTxt;
    private TableLayout highScoresMM;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);
        gridSize = 10;
        mines = 20;
        this.textSize = 17;
        this.clicks = 0;
        this.highScores = null;
        try {
            highScores = new HighScoreTable(JSON_SAVE, getApplicationContext());
        } catch (IOException | JSONException e) {
            Log.i("i", "Error loading highScores.");
            highScores = new HighScoreTable();
        }
        initMenu();
    }

    @Override
    public void onClick(View v) {
        // I'm not entirely sure why view.isClickable() being false
        // doesn't disable onClick() be default, but whatever.
        // I'm just explicitly telling it view has to be clickable
        // in the bottom condition.
        if (v.isClickable() && v instanceof Cell && !((Cell) v).isFlagged()) {
            mineField.revealCell((Cell) v);
            clicks++;
        }
    }

    @Override
    public boolean onLongClick(View v) {
        if (v.isClickable() && v instanceof Cell && !((Cell) v).isRevealed()) {
            mineField.toggleFlag((Cell) v);
            clicks++;
        }
        return true;
    }

    private void refreshHighScoresMM() {
        highScoresMM.removeAllViews();

        List<ScoreEntryView> scores = highScores.getFormattedRows(this, 10, false);

        for (ScoreEntryView row : scores) {
            row.setOnClickListener(v -> {
                ScoreEntryDialog sed = new ScoreEntryDialog(row);
                sed
                    .setOKListener(v1 -> {
                        sed.dismiss();
                    })
                    .setDeleteListener(v1 -> {
                        ScoreEntry entry = row.getRelatedScoreEntry();
                        highScores.remove(entry);
                        refreshHighScoresMM();
                        sed.dismiss();
                    })
                    .show(getSupportFragmentManager(), "");
            });

            highScoresMM.addView(row);
            try {
                highScores.saveToJson(JSON_SAVE, getApplicationContext());
            } catch (IOException | JSONException e) {
                Log.i("i", "Error saving minesweeper scores.");
            }
        }
    }

    private void initMenu() {
        upGrid = findViewById(R.id.upGrid);
        downGrid = findViewById(R.id.downGrid);
        downMines = findViewById(R.id.downMines);
        gridSizeTxt = findViewById(R.id.gridSizeTxt);
        minesTxt = findViewById(R.id.minesTxt);
        highScoresMM = findViewById(R.id.highScoresMM);
        refreshHighScoresMM();

        if (gridSize >= MAX_GRID_SIZE) upGrid.setClickable(false);
        if (gridSize <= MIN_GRID_SIZE) downGrid.setClickable(false);
        if (mines <= 1) downMines.setClickable(false);

        gridSizeTxt.setText(String.format("Grid Size: %dx%d", gridSize, gridSize));
        minesTxt.setText("Mines: " + mines);
    }

    private static double calcMineRatio(int gridSize, int mines) {
        double gridCount = gridSize * gridSize;
        return ((double) mines) /  gridCount;
    }

    public void onClickGridUp(View v) {
        gridSize++;
        gridSizeTxt.setText(String.format("Grid Size: %dx%d", gridSize, gridSize));

        if (gridSize >= MAX_GRID_SIZE) upGrid.setClickable(false);
        else if (gridSize == MIN_GRID_SIZE+1) downGrid.setClickable(true);
    }

    public void onClickGridDown(View v) {
        gridSize--;
        gridSizeTxt.setText(String.format("Grid Size: %dx%d", gridSize, gridSize));

        if (gridSize <= MIN_GRID_SIZE) downGrid.setClickable(false);
        else if (gridSize == MAX_GRID_SIZE-1) upGrid.setClickable(true);
    }

    public void onClickMinesUp(View v) {
        mines++;
        minesTxt.setText("Mines: " + mines);
        if (mines == 2) {
            downMines.setClickable(true);
        }
    }

    public void onClickMinesDown(View v) {
        mines--;
        minesTxt.setText("Mines: " + mines);
        if (mines <= 1) {
            downMines.setClickable(false);
        }
    }

    public void onClickPlay(View v) {
        double ratio = calcMineRatio(gridSize, mines);

        if (ratio < MIN_MINES_RATIO) {
            int minMines = (int) Math.ceil(MIN_MINES_RATIO * (gridSize * gridSize));
            String displayText = "Too few mines. Please increase to at least " + minMines;
            Toast.makeText(this, displayText, Toast.LENGTH_SHORT).show();
        } else if (ratio > MAX_MINES_RATIO) {
            int maxMines = (int) (MAX_MINES_RATIO * (gridSize * gridSize));
            String displayText = "Too many mines. Please decrease to at least " + maxMines;
            Toast.makeText(this, displayText, Toast.LENGTH_SHORT).show();
        } else {
            clicks = 0;
            mineField = new MineField(this, textSize, gridSize, mines);
            gameView = new GameView(this, textSize, gridSize).useField(mineField);
            mineField.getTimer().start();

            setContentView(gameView);
            Log.i("i", "minesweeper game started.");
        }
    }

    public void gotoMenu() {
        mineField.getTimer().stop();
        mineField = null;
        gameView = null;

        setContentView(R.layout.main_menu);
        initMenu();

        Log.i("i", "minesweeper game stopped.");
    }

    public void onClickNewGame() {
        if (mineField.isGameOver()) {
            int userScore = mineField.getCurScore();

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            String sweepDate = dateFormat.format(Calendar.getInstance().getTime());

            highScores.add(new ScoreEntry("___", userScore, gridSize, mines,
                    mineField.getTimer().getFormattedTime(), clicks, sweepDate));

            new HighScoresDialog(this, v -> gotoMenu(), highScores)
                    .show(getSupportFragmentManager(), "");
        } else {
            new AlertDialog.Builder(this)
                    .setTitle("New Game?")
                    .setPositiveButton(R.string.ok_btn_text, (dialog, button) -> gotoMenu())
                    .setNegativeButton(R.string.cancel_btn_text, (dialog, button) ->
                            dialog.cancel())
                    .create()
                    .show();
        }
    }
}