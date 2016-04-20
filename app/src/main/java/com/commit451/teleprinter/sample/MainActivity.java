package com.commit451.teleprinter.sample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mTeleprinter = new Teleprinter(this);
    }
}
