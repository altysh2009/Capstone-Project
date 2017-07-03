package com.example.home_.news;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.home_.news.data.NewsContract;
import com.example.home_.news.sync.NewsJopInitialize;
import com.example.home_.news.sync.RecyceleAdpterMain;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>, RecyceleAdpterMain.ReciveClick {

    String TAG = "MainActivity";

    RecyceleAdpterMain mAdapter;
    RecyclerView mNewsList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        NewsJopInitialize.initialize(getApplicationContext());
        mNewsList = (RecyclerView) findViewById(R.id.recycle_view_main);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mNewsList.setLayoutManager(layoutManager);
        mNewsList.setHasFixedSize(true);
        mAdapter = new RecyceleAdpterMain(this);
        mNewsList.setAdapter(mAdapter);

        getSupportLoaderManager().initLoader(1, null, this);
        Log.d(TAG, "onCreate: ");
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String s = NewSpi.getSourcesNames("", "", "").toString();


                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(getApplicationContext(), NewsPrefranceActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getApplicationContext(), NewsContract.articles, null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapter.setdata(data);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.setdata(null);
        loader.reset();
    }

    @Override
    public void theClickedItem(String thumb) {

        Intent intent = new Intent(getApplicationContext(), WebViewActivity.class);
        intent.putExtra("url", thumb);
        startActivity(intent);
    }

    @Override
    public void theClickedItem(String thumb, int id) {
        if (thumb.contains("http")) {
            Intent intent = new Intent(getApplicationContext(), WebViewActivity.class);
            intent.putExtra("url", thumb);
            startActivity(intent);
        }
    }
}
