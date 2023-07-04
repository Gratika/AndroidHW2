package com.gatikahome.androidhw2;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

public class CreateMessageDialogFragment extends DialogFragment implements View.OnClickListener {

    private EditText new_message;
    private ISaveMessage saveMessage;
    /*@NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        return builder.setView(R.layout.create_message_dialog).create();
    }*/
    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        saveMessage = (ISaveMessage) context;
    }
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.create_message_dialog,null, false);
        v.findViewById(R.id.btn_cancel).setOnClickListener(this);
        v.findViewById(R.id.btn_ok).setOnClickListener(this);
        new_message = v.findViewById(R.id.new_message);
        return v;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btn_cancel) {
            dismiss();
        } else if (id == R.id.btn_ok) {
            saveMessage.saveMessage(new_message.getText().toString());
            dismiss();
        }

    }
}
