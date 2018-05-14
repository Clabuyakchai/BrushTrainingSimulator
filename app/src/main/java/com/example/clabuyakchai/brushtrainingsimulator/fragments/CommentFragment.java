package com.example.clabuyakchai.brushtrainingsimulator.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.example.clabuyakchai.brushtrainingsimulator.R;

/**
 * Created by Clabuyakchai on 14.05.2018.
 */

public class CommentFragment extends DialogFragment {

    private EditText mComment;
    public static final String EXTRA_COMMENT = "com.example.clabuyakchai.brushtrainingsimulator.comment";

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_comment, null, false);

        mComment = view.findViewById(R.id.comment);

        return new AlertDialog.Builder(getActivity())
                .setView(view)
                .setTitle(R.string.addcomment)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(mComment != null || mComment.length() > 0) {
                            sendResult(Activity.RESULT_OK, mComment.getText().toString());
                        } else {
                            sendResult(Activity.RESULT_OK, getResources().getString(R.string.no_comment));
                        }
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        sendResult(Activity.RESULT_OK, getResources().getString(R.string.no_comment));
                    }
                })
                .create();
    }

    private void sendResult(int resultCode, String comment){
        if(getTargetFragment() == null){
            return;
        }

        Intent intent = new Intent();
        intent.putExtra(EXTRA_COMMENT, comment);

        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);
    }

    public static CommentFragment newInstance(){
        return new CommentFragment();
    }
}
