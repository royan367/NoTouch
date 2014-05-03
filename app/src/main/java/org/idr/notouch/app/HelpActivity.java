package org.idr.notouch.app;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;


public class HelpActivity extends ActionBarActivity {
    private ExpandableListView list;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

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

    private class ExpandableAdapter extends BaseExpandableListAdapter {
        private String[] groups = { "Senin için alarm kurabilirim.", "Senin için müzik çalabilirim.", "Senin için mesaj yazabilirim.", "Senin için çağrı yapabilirim."};
        private String[][] children = {{"Mesela, \"Alarm kur, saat 20 dakika 40\" de."},
                { "Mesela, \"Müzik çal müzik Gülpembe\" de."},
                { "Mesela, \"Mesaj yaz.\" dedikten sonra \"Kime?\" diye sorduğumda  bana rehberinde kayıtlı olan isimlerden birini söyle, \"Mesaj?\" diye sorduğumda ise göndermek istediğin mesajı söyle. Son olarak, \"Mesaj gönderilsin mi?\" dediğimde \"Evet\" ya da \"Hayır\" de, sana mesajın gönderilip gönderilmediğini söyleyeceğim."},
                { "Mesela, \"Çağrı yap.\" dedikten sonra \"Kime?\" diye sorduğumda bana rehberinde kayıtlı olan isimlerden birini söyle ve ben de senin için arayayım."}};

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
