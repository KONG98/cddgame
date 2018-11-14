package com.softwaretraining.cddgame.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.softwaretraining.cddgame.BuildConfig;
import com.softwaretraining.cddgame.R;
import com.softwaretraining.cddgame.wait.WaitActivity;

public class LoginActivity extends Activity implements LoginContract.View {

    private LoginContract.Presenter mLoginPresenter;

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
        setContentView(R.layout.activity_login);

        initLoginView();

    }

    public void onClickLoginButton(View view) {
        String name = getUserInputName();
        String password = getUserInputPassword();

        if (mLoginPresenter.checkName(name)
                && mLoginPresenter.checkPassword(password)) {
            mLoginPresenter.login(name, password);
        }
    }

    public void onClickRegisterNewAccountButton(View view) {
        this.toRegisterView();
    }

    @Override
    public void setPresenter(LoginContract.Presenter presenter) {
        mLoginPresenter = presenter;
    }

    @Override
    public void setRecentUsedName(String name) {
        EditText nameTv = findViewById(R.id.editTextName);
        nameTv.setText(name);
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
        TextView nameLimitTv = findViewById(R.id.textViewNameResponse);
        nameLimitTv.setText(limit);
    }

    @Override
    public void showPasswordLimit(String limit) {
        TextView pwLimitTv = findViewById(R.id.textViewPasswordResponse);
        pwLimitTv.setText(limit);
    }

    @Override
    public void loginSuccess(String username) {
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

    private void toWaitView(String username) {
        Intent intent = new Intent();
        intent.setClass(com.softwaretraining.cddgame.login.LoginActivity.this, WaitActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("Username", username);
        intent.putExtras(bundle);
        startActivity(intent);
        this.finish();
    }

    private void toRegisterView() {
        Intent intent = new Intent();
        intent.setClass(com.softwaretraining.cddgame.login.LoginActivity.this, com.softwaretraining.cddgame.register.RegisterActivity.class);
        startActivity(intent);
        this.finish();
    }

    private void initLoginView() {
        TextView textViewVersion = findViewById(R.id.textViewVersion);
        textViewVersion.setText("v" + BuildConfig.VERSION_NAME + " (" + BuildConfig.VERSION_CODE + ")");

        new LoginPresenter(new LoginModel(), this).setContext(this);
        mLoginPresenter.start();

    }

}
