package com.york.minesweeper.model;

import android.content.Context;

import com.york.minesweeper.View.ScoreEntryView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

public class HighScoreTable {


    private int pendingInd;
    private List<ScoreEntry> entries;

    public HighScoreTable() {
        pendingInd = -1;
        entries = new ArrayList<>();
    }

    public HighScoreTable(String fileName, Context context) throws IOException, JSONException {
        this();

        BufferedReader reader = null;
        try {
            InputStream in = context.openFileInput(fileName);
            reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder jsonString = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                jsonString.append(line);
            }
            JSONArray jsonArray = (JSONArray) new JSONTokener(jsonString.toString()).nextValue();
            for (int i = 0; i < jsonArray.length(); i++) {
                entries.add(new ScoreEntry(jsonArray.getJSONObject(i)));
            }
        } catch (FileNotFoundException ignored) {

        } finally {
            if (reader != null) {
                reader.close();
            }
        }
    }

    /**
     *      > reorganizes the list,
     *      > adds the newScore (name absent),
     *      > saves a reference to the
     *        new entry as "pending" so
     *        the name can be
     *        re-assigned later.
     */
    public void add(ScoreEntry scoreEntry) {
        for (pendingInd = 0; pendingInd < entries.size() &&
        entries.get(pendingInd).getScore() > scoreEntry.getScore(); pendingInd++);

        entries.add(pendingInd, scoreEntry);
    }
    public void remove(ScoreEntry scoreEntry) {
        entries.remove(scoreEntry);
    }
    public boolean contains(ScoreEntry scoreEntry) {
        return entries.contains(scoreEntry);
    }

    public void saveToJson(String fileName, Context context) throws IOException, JSONException {
        JSONArray jsonArray = new JSONArray();
        for (ScoreEntry entry : entries) {
            jsonArray.put(entry.convertToJSON());
        }

        Writer writer = null;

        try {
            OutputStream out = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            writer = new OutputStreamWriter(out);
            writer.write(jsonArray.toString());
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }

    public List<ScoreEntryView> getFormattedRows(Context context, int length, boolean hasFillers) {
        List<ScoreEntryView> returnVal = new ArrayList<>();

        int n = Math.min(length, entries.size());

        int entryRank = 1;
        int i;
        for (i = 0; i < n; i++) {
            if (i != 0 && entries.get(i-1).getScore() != entries.get(i).getScore()) {
                entryRank = i + 1;
            }
            ScoreEntryView row = new ScoreEntryView(context, entryRank, entries.get(i).getName(), entries.get(i).getScore(),
                    entries.get(i));

            returnVal.add(row);
        }

        if (hasFillers) {
            for (int j = 0; j < length - entries.size(); j++) {
                returnVal.add(new ScoreEntryView(context, entryRank, "???", 0, null));
            }
        }

        return returnVal;
    }

    public boolean isPendingName() {
        return pendingInd != -1;
    }

    public int getPending() {
        return pendingInd;
    }

    /**
     * sets the name for pending (3 chars) ONLY if it has a pending.
     */
    public void setName(String newName) {
        if (isPendingName()) {
            entries.get(pendingInd).setName(newName);
            pendingInd = -1;
        }
    }
}
