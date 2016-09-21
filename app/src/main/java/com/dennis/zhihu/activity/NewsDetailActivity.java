package com.dennis.zhihu.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;

import com.dennis.zhihu.R;
import com.dennis.zhihu.db.DailyNewsDB;
import com.dennis.zhihu.entity.News;
import com.dennis.zhihu.task.LoadNewsDetailTask;
import com.dennis.zhihu.utility.Utility;

/**
 * Created by Xueliang Hua on 2016/9/16.
 */
public class NewsDetailActivity extends Activity {
    private WebView mWebView;
    private boolean isFavourite = false;
    private News news;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_detail);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        mWebView = (WebView) findViewById(R.id.webview);
        setWebView(mWebView);

        news = (News) getIntent().getSerializableExtra("news");
        new LoadNewsDetailTask(mWebView).execute(news.getId());
        isFavourite = DailyNewsDB.getInstance(this).isFavourite(news);
    }

    private void setWebView(WebView webView) {
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setVerticalScrollBarEnabled(false);
        webView.setHorizontalScrollBarEnabled(false);
    }

    public static void startActivity(Context context, News news) {
        if (Utility.checkNetworkConnection(context)) {
            Intent i = new Intent(context, NewsDetailActivity.class);
            i.putExtra("news", news);
            context.startActivity(i);
        } else {
            Utility.notNetworkAlert(context);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        if (isFavourite) {
            menu.findItem(R.id.action_favourite).setIcon(R.mipmap.fav_active);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
            case R.id.action_favourite:
                if (!isFavourite) {
                    DailyNewsDB.getInstance(this).saveFavourite(news);
                    item.setIcon(R.mipmap.fav_active);
                    isFavourite = true;
                } else {
                    DailyNewsDB.getInstance(this).deleteFavourite(news);
                    item.setIcon(R.mipmap.fav_normal);
                    isFavourite = false;
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
