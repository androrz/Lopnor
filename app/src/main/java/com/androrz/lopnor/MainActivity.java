package com.androrz.lopnor;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.androrz.lopnor.model.Element;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    protected RecyclerView.LayoutManager mLayoutManager;
    protected MainAdapter mAdapter;
    protected List<Element> mDateList = new ArrayList<>();
    private final int COLUMN_COUNT = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initDataset();
        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.r_fun_list);

        mAdapter = new MainAdapter(this, mDateList);
        mLayoutManager = new GridLayoutManager(this, COLUMN_COUNT);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(new MainAdapter.GridSpacingItemDecoration(COLUMN_COUNT, 10, true));
    }

    private void initDataset() {
        InputStream is = null;
        BufferedReader in = null;
        String line;
        boolean isFirst = true;
        try {
            is = getAssets().open("table-data.txt");
            in = new BufferedReader(new InputStreamReader(is));
            while ((line = in.readLine()) != null) {
                if (isFirst) {
                    isFirst = false;
                } else {
                    String[] a = line.split("\t");
                    Element e = new Element();
                    e.number = a[0];
                    e.shortName = a[1];
                    e.wholeName = a[2];
                    e.config = a[4];
                    mDateList.add(e);
                }
            }
            Collections.sort(mDateList, new Comparator<Element>() {
                @Override
                public int compare(Element o1, Element o2) {
                    int s1 = Integer.parseInt(o1.number);
                    int s2 = Integer.parseInt(o2.number);
                    return s1 - s2;
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
