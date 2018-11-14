package com.softwaretraining.cddgame.rank;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;

import com.softwaretraining.cddgame.R;

import java.util.List;

public class RankActivity extends Activity implements RankContract.View {

    private RankContract.Presenter mRankPresenter;

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
        setContentView(R.layout.activity_rank);

        initRankView();
    }

    @Override
    public void setPresenter(RankContract.Presenter presenter) {
        this.mRankPresenter = presenter;
    }

    public void onClickButtonBack(View view) {
        this.finish();
    }

    @Override
    public void showRank(List<RankList> rankList) {
        this.runOnUiThread(() -> {
            ListView listView = findViewById(R.id.listView);
            RankListAdapter rankListAdapter = new RankListAdapter(this, R.layout.layout_ranklist, rankList);
            listView.setAdapter(rankListAdapter);
        });
    }

    private void initRankView() {
        new RankPresenter(new RankModel(), this);

        initRankList();
    }

    @SuppressWarnings("ConstantConditions")
    private void initRankList() {
        String username;
        try {
            username = getIntent().getExtras().getString("Username");
        } catch (NullPointerException e) {
            username = "";
            e.printStackTrace();
        }
        mRankPresenter.getRank(username);
    }

}
