package com.york.minesweeper.View;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.york.minesweeper.R;
import com.york.minesweeper.model.HighScoreTable;

import java.util.List;

public class HighScoresDialog extends DialogFragment {

    public int TOP_SCORE_COUNT = 5;
    private View.OnClickListener clickListener;
    private HighScoreTable model;
    private Context context;
    private TableRow editNameRow;
    private EditText editText;
    private TextWatcher curTextWatcher;

    public HighScoresDialog(Context context, View.OnClickListener clickListener, HighScoreTable model) {
        super();
        this.context = context;
        this.clickListener = clickListener;
        this.model = model;
        curTextWatcher = null;
        buildNameRow();
    }

    private void resetTextWatcher(TextView textView) {
        editText.removeTextChangedListener(curTextWatcher);
        curTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String inputText = editText.getText().toString();
                textView.setText(inputText);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        };
        editText.addTextChangedListener(curTextWatcher);
    }


    public static TextView applyFormats(TextView textView, String text) {
        textView.setText(text);
        TableRow.LayoutParams params = new TableRow.LayoutParams(
                0,
                TableRow.LayoutParams.WRAP_CONTENT,
                0.33f
        );
        textView.setLayoutParams(params);
        textView.setGravity(Gravity.CENTER);
        textView.setTextSize(R.dimen.HSD_text_size);
        return textView;
    }

    private void buildNameRow() {
        editNameRow = new TableRow(context);

        TextView nameText = applyFormats(new TextView(context), "Enter Name:");
        nameText.setTextColor(Color.RED);
        nameText.setTextSize(10);
        editNameRow.addView(nameText);

        editText = (EditText) applyFormats(new EditText(context), "");
        editText.setTextColor(Color.RED);
        int maxLength = 3;
        editText.setFilters(new InputFilter[] {new InputFilter.LengthFilter(maxLength)});
        editNameRow.addView(editText);

        TextView spacingView = applyFormats(new TextView(context), "");

        editNameRow.addView(spacingView);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View highScoreLayout = inflater.inflate(R.layout.high_score_layout, null);

        Button okHighScoreBtn = highScoreLayout.findViewById(R.id.okHighScoreBtn);
        TableLayout table = highScoreLayout.findViewById(R.id.highScoresD);

        List<ScoreEntryView> tableRows = model.getFormattedRows(context, TOP_SCORE_COUNT, true);
        tableRows.forEach(table::addView);

        if (model.isPendingName()) {
            TextView nameView = tableRows.get(model.getPending()).getNameView();
            nameView.setTextColor(Color.RED);
            editText.setText("");
            resetTextWatcher(nameView);
            table.addView(editNameRow);
        }

        okHighScoreBtn.setOnClickListener(v -> {
            String newName = String.valueOf(editText.getText());
            model.setName(newName);   // extract name chars from editText view.
            clickListener.onClick(v);
            dismiss();
        });

        return builder.setTitle("High Scores")
                .setView(highScoreLayout)
                .create();
    }
}
