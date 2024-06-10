package com.york.minesweeper.View;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.york.minesweeper.R;
import com.york.minesweeper.model.ScoreEntry;

public class ScoreEntryDialog extends DialogFragment {

    private ScoreEntryView sev;
    private View.OnClickListener okListener;
    private View.OnClickListener deleteListener;

    public ScoreEntryDialog(ScoreEntryView sev) {
        super();
        this.sev = sev;
        this.okListener = null;
        this.deleteListener = null;
        setCancelable(true);
    }

    public ScoreEntryDialog setOKListener(View.OnClickListener okListener) {
        this.okListener = okListener;
        return this;
    }

    public ScoreEntryDialog setDeleteListener(View.OnClickListener deleteListener) {
        this.deleteListener = deleteListener;
        return this;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View scoreRowDialog = inflater.inflate(R.layout.score_row_dialog, null);

        TextView rankValSD = scoreRowDialog.findViewById(R.id.rankValSD);
        TextView nameValSD = scoreRowDialog.findViewById(R.id.nameValSD);
        TextView scoreValSD = scoreRowDialog.findViewById(R.id.scoreValSD);
        TextView fieldValSD = scoreRowDialog.findViewById(R.id.fieldValSD);
        TextView minesValSD = scoreRowDialog.findViewById(R.id.minesValSD);
        TextView timeValSD = scoreRowDialog.findViewById(R.id.timeValSD);
        TextView clicksValSD = scoreRowDialog.findViewById(R.id.clicksValSD);
        TextView sweepDateValSD = scoreRowDialog.findViewById(R.id.sweepDateValSD);

        Button okBtnSD = scoreRowDialog.findViewById(R.id.okBtnSD);
        if (okListener != null) okBtnSD.setOnClickListener(okListener);
        Button deleteBtnSD = scoreRowDialog.findViewById(R.id.deleteBtnSD);
        if (deleteListener != null) deleteBtnSD.setOnClickListener(deleteListener);

        ScoreEntry entry = sev.getRelatedScoreEntry();

        if (entry != null) {
            rankValSD.setText(sev.getRankView().getText());
            nameValSD.setText(sev.getNameView().getText());
            scoreValSD.setText(sev.getScoreView().getText());
            fieldValSD.setText(entry.getField() + "x" + entry.getField());
            minesValSD.setText(String.valueOf(entry.getMines()));
            timeValSD.setText(entry.getTime());
            clicksValSD.setText(String.valueOf(entry.getClicks()));
            sweepDateValSD.setText(entry.getSweepDate());
        }

        return builder
                .setTitle("High Score Details")
                .setView(scoreRowDialog)
                .create();
    }
}