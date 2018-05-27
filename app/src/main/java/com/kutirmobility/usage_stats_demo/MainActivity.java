package com.kutirmobility.usage_stats_demo;

import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private UsageStatsListAdapter usageStatsListAdapter = null;
    private RecyclerView recyclerView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SpinnerAdapter spinnerAdapter = ArrayAdapter.createFromResource(this, R.array.action_list,
                android.R.layout.simple_spinner_dropdown_item);
        Spinner spinner = findViewById(R.id.time_span_spinner);
        spinner.setAdapter(spinnerAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String[] strings = getResources().getStringArray(R.array.action_list);
                int mins = Integer.valueOf(strings[position]);
                long now = System.currentTimeMillis();
                long since = now - mins * 60 * 1000;
                updateAppsList(getUsageStatistics(since, now));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        usageStatsListAdapter = new UsageStatsListAdapter(getPackageManager());

        recyclerView = findViewById(R.id.usage_stats);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(usageStatsListAdapter);
    }

    private List<UsageStats> getUsageStatistics(long start, long stop) {
        UsageStatsManager usageStatsManager =
                (UsageStatsManager) getSystemService(Context.USAGE_STATS_SERVICE);
        return new ArrayList<>(usageStatsManager.queryAndAggregateUsageStats(start, stop).values());
    }

    private void updateAppsList(List<UsageStats> usageStatsList) {
        usageStatsListAdapter.setUsageStatsList(usageStatsList);
        usageStatsListAdapter.notifyDataSetChanged();
    }
}
