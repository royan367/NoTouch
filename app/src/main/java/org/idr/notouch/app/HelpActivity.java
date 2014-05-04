package org.idr.notouch.app;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;


public class HelpActivity extends MyActionBarActivity {
    private ExpandableListView list;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        setupActionBar();

        final ExpandableAdapter mAdapter = new ExpandableAdapter();
        list = (ExpandableListView) findViewById(R.id.expandableListView1);
        list.setAdapter(mAdapter);

        //ExpandableListView ile alakalı her event'i onCreate metoduna yazıyoruz
        list.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            public void onGroupExpand(int groupPosition) {
                int len = mAdapter.getGroupCount();
                for (int i = 0; i < len; i++) {
                    if (i != groupPosition) {
                        list.collapseGroup(i);
                    }
                }
            }
        });
    }

    /**
     * Set up the {@link android.app.ActionBar}, if the API is available.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void setupActionBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            // Show the Up button in the action bar.
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }


    private class ExpandableAdapter extends BaseExpandableListAdapter {
        private String[] groups = {getString(R.string.i_can_send_message), getString(R.string.i_can_call), getString(R.string.i_can_play_music), getString(R.string.i_can_set_alarm)};
        private String[][] children = {{getString(R.string.description_for_send_message)},
                {getString(R.string.description_for_call)},
                {getString(R.string.description_for_play_music)},
                {getString(R.string.description_for_set_alarm)}};

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return children[groupPosition][childPosition];
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return groupPosition;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded,
                                 View convertView, ViewGroup parent) {
            View rowView = convertView;
            // reuse views
            if (rowView == null) {
                rowView = getLayoutInflater().inflate(R.layout.help_list_view_group, null);
                // configure view holder
                ViewHolder viewHolder = new ViewHolder();
                assert rowView != null;
                viewHolder.text = (TextView) rowView.findViewById(R.id.text);
                rowView.setTag(viewHolder);
            }

            // fill data
            ViewHolder holder = (ViewHolder) rowView.getTag();
            holder.text.setText(groups[groupPosition]);

            return rowView;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition,
                                 boolean isLastChild, View convertView, ViewGroup parent) {
            View rowView = convertView;
            // reuse views
            if (rowView == null) {
                rowView = getLayoutInflater().inflate(R.layout.help_list_view_child, null);
                // configure view holder
                ViewHolder viewHolder = new ViewHolder();
                assert rowView != null;
                viewHolder.text = (TextView) rowView.findViewById(R.id.text);
                rowView.setTag(viewHolder);
            }

            // fill data
            ViewHolder holder = (ViewHolder) rowView.getTag();
            holder.text.setText(children[groupPosition][childPosition]);

            return rowView;
        }


        private class ViewHolder {
            TextView text;
        }


        @Override
        public int getChildrenCount(int groupPosition) {
            return children[groupPosition].length;
        }

        @Override
        public Object getGroup(int groupPosition) {
            return groups[groupPosition];
        }

        @Override
        public int getGroupCount() {
            return groups.length;
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return false;
        }
    }
}
