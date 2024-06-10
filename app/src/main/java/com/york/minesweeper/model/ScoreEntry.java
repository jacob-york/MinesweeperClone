package com.york.minesweeper.model;

import org.json.JSONException;
import org.json.JSONObject;

public class ScoreEntry {

    private String mName;
    private static final String NAME = "name";

    private final int mUserScore;
    private static final String USER_SCORE = "userScore";

    private final int mField;
    private static final String FIELD = "field";

    private final int mMines;
    private static final String MINES = "mines";

    private final String mTime;
    private static final String TIME = "time";

    private final int mClicks;
    private static final String CLICKS = "clicks";

    private final String mSweepDate;
    private static final String SWEEP_DATE = "sweepDate";

    public ScoreEntry(String name, int userScore, int field, int mines, String time, int clicks, String sweepDate) {
        mName = name;
        mUserScore = userScore;
        mField = field;
        mMines = mines;
        mTime = time;
        mClicks = clicks;
        mSweepDate = sweepDate;
    }
    public ScoreEntry(JSONObject jo) throws JSONException {
        this.mName = jo.getString(NAME);
        this.mUserScore = jo.getInt(USER_SCORE);
        this.mField = jo.getInt(FIELD);
        this.mMines = jo.getInt(MINES);
        this.mTime = jo.getString(TIME);
        this.mClicks = jo.getInt(CLICKS);
        this.mSweepDate = jo.getString(NAME);
    }

    public JSONObject convertToJSON() throws JSONException {
        JSONObject jo = new JSONObject();
        jo.put(NAME, mName);
        jo.put(USER_SCORE, mUserScore);
        jo.put(FIELD, mField);
        jo.put(MINES, mMines);
        jo.put(TIME, mTime);
        jo.put(CLICKS, mClicks);
        jo.put(SWEEP_DATE, mSweepDate);

        return jo;
    }

    public String getName() {
        return mName;
    }
    public void setName(String newName) {
        mName = newName;
    }
    public int getScore() {
        return mUserScore;
    }
    public int getField() {
        return mField;
    }
    public int getMines() {
        return mMines;
    }
    public String getTime() {
        return mTime;
    }
    public int getClicks() {
        return mClicks;
    }
    public String getSweepDate() {
        return mSweepDate;
    }
}
