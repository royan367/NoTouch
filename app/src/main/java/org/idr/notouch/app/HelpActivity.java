package org.idr.notouch.app;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;


public class HelpActivity extends ActionBarActivity {
    private ExpandableListView list;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        final ExpandableAdapter mAdapter = new ExpandableAdapter();
        list = (ExpandableListView) findViewById(R.id.exp_list);
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


        list.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                // TODO Auto-generated method stub
                if(groupPosition==3)
                {
                    if(childPosition==0)
                    {
                        PokeAndroid();
                    }
                }
                return false;
            }

            private void PokeAndroid() {
                // TODO Auto-generated method stub
                Toast msg = Toast.makeText(HelpActivity.this, "Poke", Toast.LENGTH_LONG);
                msg.setGravity(Gravity.CENTER, msg.getXOffset() / 2, msg.getYOffset() / 2);
                msg.show();
            }
        });
    }

    private class ExpandableAdapter extends BaseExpandableListAdapter {
        private String[] groups = { "Senin için alarm Kurabiliyorum", "Senin için Müzik Açabilirim", "Senin için Mesaj yazabilirim", "Senin için Arama yapabilirim" };
        private String[][] children = { { "Mesela, saat 8 dk 40 söylemeyi dene","Unutma eğer alarm kurmak istediğin saat akşam saatleriyse saat 20 dk 40 diyebilirsin" }, { "Mesela, /“Müzik aç/” dedikten sonra istediğin şarkının ismini söyle " },
                { "“Mesaj yaz” dedikten sonra “Kime ?” diye sorduğumda  bana rehberinde kayıtlı olan isimlerden birini söyle.", "“Ne yazayım” dediğim de istediğin mesajı söyle. ",
                        "Son olarak, “mesaj gönderilsin mi?” dediğimde “evet” ya da “hayır” diyebilirsin." },
                { "Mesela “Arama yap” dedikten sonra “Kimi arayayım?” dediğim zaman sende bana rehberinde kayıtlı olan isimlerden birini söyle ve bende senin için arayayım." } };

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            // TODO Auto-generated method stub
            return children[groupPosition][childPosition];
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            // TODO Auto-generated method stub
            return childPosition;
        }

        public TextView getGenericView() {
            // Layout parameters for the ExpandableListView
            AbsListView.LayoutParams lp = new AbsListView.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, 64);

            TextView textView = new TextView(HelpActivity.this);
            textView.setLayoutParams(lp);
            // Center the text vertically
            textView.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
            // Set the text starting position
            textView.setPadding(10, 0, 0, 0);
            return textView;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition,
                                 boolean isLastChild, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            TextView textView = getGenericView();
            textView.setText(getChild(groupPosition, childPosition).toString());
            return textView;
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            // TODO Auto-generated method stub
            return children[groupPosition].length;
        }

        @Override
        public Object getGroup(int groupPosition) {
            // TODO Auto-generated method stub
            return groups[groupPosition];
        }

        @Override
        public int getGroupCount() {
            // TODO Auto-generated method stub
            return groups.length;
        }

        @Override
        public long getGroupId(int groupPosition) {
            // TODO Auto-generated method stub
            return groupPosition;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded,
                                 View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            TextView textView = getGenericView();
            textView.setText(getGroup(groupPosition).toString());
            return textView;
        }

        @Override
        public boolean hasStableIds() {
            // TODO Auto-generated method stub
            return true;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            // TODO Auto-generated method stub
            return true;
        }

    }
}