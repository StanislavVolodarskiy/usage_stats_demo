package com.kutirmobility.usage_stats_demo;

import android.app.usage.UsageStats;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UsageStatsListAdapter extends RecyclerView.Adapter<UsageStatsListAdapter.ViewHolder> {

    private final PackageManager packageManager;
    private List<UsageStats> usageStatsList = new ArrayList<>();
    private DateFormat dateFormat = new SimpleDateFormat();

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final ImageView appIcon;
        public final TextView packageName;
        public final TextView appName;
        public final TextView statsRange;
        public final TextView lastTimeUsed;
        public final TextView foregroundDuration;

        public ViewHolder(View v) {
            super(v);
            appIcon = v.findViewById(R.id.app_icon);
            packageName = v.findViewById(R.id.package_name);
            appName = v.findViewById(R.id.app_name);
            lastTimeUsed = v.findViewById(R.id.last_time_used);
            statsRange = v.findViewById(R.id.stats_range);
            foregroundDuration = v.findViewById(R.id.foreground_duration);
        }
    }

    public UsageStatsListAdapter(PackageManager packageManager) {
        this.packageManager = packageManager;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater
                .from(viewGroup.getContext())
                .inflate(R.layout.usage_stats_row, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        UsageStats usageStats = usageStatsList.get(position);
        String packageName = usageStats.getPackageName();
        viewHolder.appIcon.setImageDrawable(getAppIcon(packageName));
        viewHolder.packageName.setText(packageName);
        viewHolder.appName.setText(getAppName(packageName));
        viewHolder.statsRange.setText(String.format(
                "%s - %s",
                formatTime(usageStats.getFirstTimeStamp()),
                formatTime(usageStats.getLastTimeStamp()))
        );
        viewHolder.lastTimeUsed.setText(formatTime(usageStats.getLastTimeUsed()));
        viewHolder.foregroundDuration.setText(
                formatDuration(usageStats.getTotalTimeInForeground()));
    }

    @Override
    public int getItemCount() {
        return usageStatsList.size();
    }

    public void setUsageStatsList(List<UsageStats> usageStatsList) {
        this.usageStatsList = usageStatsList;
    }

    private Drawable getAppIcon(String packageName) {
        try {
            return packageManager.getApplicationIcon(packageName);
        } catch (PackageManager.NameNotFoundException e) {
        }
        return null;
    }

    private CharSequence getAppName(String packageName) {
        try {
            return packageManager.getApplicationLabel(packageManager.getApplicationInfo(packageName, 0));
        } catch (PackageManager.NameNotFoundException e) {
        }
        return null;
    }

    private String formatTime(long time) {
        return dateFormat.format(new Date(time));
    }
    private String formatDuration(long duration) {
        long s = duration / 1000;
        return String.format("%d:%02d:%02d", s / 3600, (s % 3600) / 60, s % 60);
    }
}