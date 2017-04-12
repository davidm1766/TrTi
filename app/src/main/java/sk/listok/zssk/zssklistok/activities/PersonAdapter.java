//package sk.listok.zssk.zssklistok.activities;
//
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ArrayAdapter;
//import android.widget.TextView;
//
//import java.util.ArrayList;
//
//import sk.listok.zssk.zssklistok.R;
//import sk.listok.zssk.zssklistok.dataLayer.objects.Person;
//
///**
// * Created by Nexi on 11.04.2017.
// */
//
//public class PersonAdapter extends ArrayAdapter<Person> {
//
//        private static class ViewHolder {
//            private TextView itemView;
//        }
//
//        public PersonAdapter(Context context, int textViewResourceId, ArrayList<Person> items) {
//            super(context, textViewResourceId, items);
//        }
//
//        public View getView(int position, View convertView, ViewGroup parent) {
//
//                if (convertView == null) {
//                    convertView = LayoutInflater.from(this.getContext())
//                            .inflate(R.layout.listview_association, parent, false);
//
//                    viewHolder = new ViewHolder();
//                    viewHolder.itemView = (TextView) convertView.findViewById(R.id.ItemView);
//
//                    convertView.setTag(viewHolder);
//                } else {
//                    viewHolder = (ViewHolder) convertView.getTag();
//                }
//
//                Person item = getItem(position);
//                if (item!= null) {
//                    // My layout has only one TextView
//                    // do whatever you want with your string and long
//                    viewHolder.itemView.setText(String.format("%s %d", item.reason, item.long_val));
//                }
//
//            return convertView;
//        }
//
//}
