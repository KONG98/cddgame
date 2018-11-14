package com.softwaretraining.cddgame.register;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.softwaretraining.cddgame.R;

public class RegisterActivity extends Activity implements RegisterContract.View {

    private RegisterContract.Presenter mRegisterPresenter;

    protected void onCreate(Bundle savedInstanceState) {

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        if (Build.VERSION.SDK_INT < 19) {
            this.getWindow().getDecorView().setSystemUiVisibility(View.GONE);
        } else if (Build.VERSION.SDK_INT >= 19) {
            this.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initRegisterView();
    }

    public void onClickRegisterButton(View view) {
        String name = getUserInputName();
        String password = getUserInputPassword();
        String repw = getRepeatPassword();

        if (mRegisterPresenter.checkName(name)
                && mRegisterPresenter.checkPassword(password)
                && mRegisterPresenter.checkRepeatPassword(password, repw)) {
            mRegisterPresenter.register(name, password);
        }
    }

    public void onClickBackButton(View view) {
        this.toLoginView();
    }

    @Override
    public void setPresenter(RegisterContract.Presenter presenter) {
        mRegisterPresenter = presenter;
    }

    @Override
    public void showServerResponse(String responseMessage) {
        this.runOnUiThread(() -> {
            TextView responseTv = findViewById(R.id.textViewResponseMessage);
            responseTv.setText(responseMessage);
        });
    }

    @Override
    public void showNameLimit(String limit) {
        TextView nameLimitTv = findViewById(R.id.textViewResponseMessage);
        nameLimitTv.setText(limit);
    }

    @Override
    public void showPasswordLimit(String limit) {
        TextView pwLimitTv = findViewById(R.id.textViewResponseMessage);
        pwLimitTv.setText(limit);
    }

    @Override
    public void showRepeatPasswordLimit(String limit) {
        TextView pwLimitTv = findViewById(R.id.textViewResponseMessage);
        pwLimitTv.setText(limit);
    }

    @Override
    public void registerSuccess(String username) {
        toWaitView(username);
    }

    private String getUserInputName() {
        TextView nameTv = findViewById(R.id.editTextName);
        return nameTv.getText().toString();
    }

    private String getUserInputPassword() {
        TextView pwTv = findViewById(R.id.editTextPassword);
        return pwTv.getText().toString();
    }

    private String getRepeatPassword() {
        TextView pwTv = findViewById(R.id.editTextRePw);
        return pwTv.getText().toString();
    }

    private void toWaitView(String username) {
        Intent intent = new Intent();
        intent.setClass(com.softwaretraining.cddgame.register.RegisterActivity.this, com.softwaretraining.cddgame.wait.WaitActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("Username", username);
        intent.putExtras(bundle);
        startActivity(intent);
        this.finish();
    }

    private void toLoginView() {
        Intent intent = new Intent();
        intent.setClass(com.softwaretraining.cddgame.register.RegisterActivity.this, com.softwaretraining.cddgame.login.LoginActivity.class);
        startActivity(intent);
        this.finish();
    }

    private void initRegisterView() {
        new RegisterPresenter(new RegisterModel(), this).setContext(this);
        mRegisterPresenter.start();
    }

}
