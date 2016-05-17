package com.commit451.teleprinter.sample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import com.commit451.teleprinter.Teleprinter;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @OnClick(R.id.button_show)
    public void onShowClicked() {
        mTeleprinter.showKeyboard(mEditText);
    }

    @OnClick(R.id.button_hide)
    public void onHideClicked() {
        mTeleprinter.hideKeyboard();
    }

    @Bind(R.id.edit_text)
    EditText mEditText;

    private Teleprinter mTeleprinter;

    private final Teleprinter.OnKeyboardToggleListener mKeyboardToggleListener = new Teleprinter.OnKeyboardToggleListener() {
        @Override
        public void onKeyboardShown(int keyboardSize) {
            Toast.makeText(MainActivity.this, "keyboard shown with size " + keyboardSize, Toast.LENGTH_SHORT)
                    .show();
        }

        @Override
        public void onKeyboardClosed() {
            Toast.makeText(MainActivity.this, "Keyboard closed", Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mTeleprinter = new Teleprinter(this);
        mTeleprinter.addKeyboardToggleListener(mKeyboardToggleListener);
    }

    @Override
    protected void onDestroy() {
        mTeleprinter.removeKeyboardWatcher(mKeyboardToggleListener);
        super.onDestroy();
    }
}
