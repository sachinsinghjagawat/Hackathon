package com.example.hackathon;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class TextDialog extends DialogFragment {

    EditText editText;
    Button button;

    public interface OnInputSelected{
        void sendInputInActivity(String text);
    }
    public OnInputSelected mOnInputSelected;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.text_dialog, container, false);
        editText = rootView.findViewById(R.id.editText);
        button = rootView.findViewById(R.id.textEnter);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( editText.getText().toString().isEmpty()){
                    Toast.makeText(getActivity(), "Please Enter The Texxt", Toast.LENGTH_SHORT).show();
                    return;
                }
                mOnInputSelected.sendInputInActivity(editText.getText().toString());
                getDialog().dismiss();
            }
        });

        return rootView;
    }

}
