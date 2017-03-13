package sk.listok.zssk.zssklistok;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;



public class TrainSearchFragment extends Fragment {

    private View rootView;
    private ArrayList<TrainStation> stations;

    private ListView lv;
    private SearchView sv;

    String statios1[] = {"Ba","KE","ZV","ZA"};
    ArrayAdapter<String> adapter;
    ListAdapter ad;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_train_search, container, false);
        //populatestationsList();

        lv = (ListView) rootView.findViewById(R.id.listViewTrain);
        sv = (SearchView) rootView.findViewById(R.id.searchViewTrain);
        adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,getItems());

        lv.setAdapter(adapter);

        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return true;
            }



            @Override
            public boolean onQueryTextChange(String newText) {

                //adapter.clear()

                adapter.getFilter().filter(newText);
              //  ArrayList<String> filtered = new ArrayList<String>();
              //  filtered.add(adapter.getItem(0));
              //  adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,filtered);
              //  lv.setAdapter(adapter);
                return true;
            }
        });

        return rootView;
    }



    private String[] getItems(){
        populatestationsList();
        String ret[] = new String[stations.size()];
        int i =0;
        for(TrainStation tr : stations){
            ret[i++] = tr.getName();
        }

        return ret;
    }

    private void populatestationsList() {
        stations = new ArrayList<TrainStation>();
        stations.add(new TrainStation("Zvolen"));
        stations.add(new TrainStation("Košice"));
        stations.add(new TrainStation("Bratislava"));
        stations.add(new TrainStation("Trenčín"));
        stations.add(new TrainStation("Turčianske Teplice"));
        stations.add(new TrainStation("Banská Bystrica"));
    }


}
