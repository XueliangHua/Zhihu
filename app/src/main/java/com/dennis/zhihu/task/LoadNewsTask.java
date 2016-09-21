package com.dennis.zhihu.task;

import android.os.AsyncTask;

import com.dennis.zhihu.adapter.NewsAdapter;
import com.dennis.zhihu.entity.News;
import com.dennis.zhihu.http.Http;
import com.dennis.zhihu.http.JsonHelper;

import org.json.JSONException;

import java.io.IOException;
import java.util.List;

/**
 * Created by Xueliang Hua on 2016/9/16.
 */
public class LoadNewsTask extends AsyncTask<Void, Void, List<News>> {
    private NewsAdapter adapter;
    private OnFinishListener listener;


    public LoadNewsTask(NewsAdapter adapter) {
        super();
        this.adapter = adapter;
    }

    public LoadNewsTask(NewsAdapter adapter, OnFinishListener listener) {
        super();
        this.adapter = adapter;
        this.listener = listener;
    }

    @Override
    protected List<News> doInBackground(Void... params) {
        List<News> newsList = null;
        try {
            newsList = JsonHelper.parseJsonToList(Http.getLastNewsList());
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            return newsList;
        }
    }

    @Override
    protected void onPostExecute(List<News> newslist) {
        adapter.refreshNewsList(newslist);
        if (listener != null) {
            listener.afterTaskFinish();
        }
    }

    public interface OnFinishListener {
        public void afterTaskFinish();
    }
}
