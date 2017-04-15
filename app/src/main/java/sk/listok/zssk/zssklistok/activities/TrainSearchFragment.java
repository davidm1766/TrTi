package sk.listok.zssk.zssklistok.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import java.text.Normalizer;
import java.util.ArrayList;

import sk.listok.zssk.zssklistok.R;
import sk.listok.zssk.zssklistok.dataLayer.DatabaseProvider;
import sk.listok.zssk.zssklistok.dataLayer.objects.IQueryTown;
import sk.listok.zssk.zssklistok.dataLayer.objects.QueryTown;
import sk.listok.zssk.zssklistok.dataLayer.objects.Town;
import sk.listok.zssk.zssklistok.objects.TrainStation;


public class TrainSearchFragment extends Fragment {

    private View rootView;
    private ArrayList<Town> stations;

    private ListView lv;
    private SearchView sv;
    ArrayAdapter<Town> adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        stations = this.getTowns();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_train_search, container, false);


        lv = (ListView) rootView.findViewById(R.id.listViewTrain);
        sv = (SearchView) rootView.findViewById(R.id.searchViewTrain);
        adapter = new ArrayAdapter<Town>(getActivity(),android.R.layout.simple_list_item_1,stations);

        lv.setAdapter(adapter);
        sv.clearFocus();// hodim focus na vyhladavanie

        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
               // String clearText = Normalizer.normalize(newText, Normalizer.Form.NFD); //zrusim diakritiku
                adapter.getFilter().filter(newText);
                return true;
            }
        });

        return rootView;
    }


    public void clearFilter(){
        sv.setQuery("",false);
        adapter.getFilter().filter("");
    }


    private ArrayList<Town> getTowns() {

        if(stations == null || stations.size() == 0) {
            stations = DatabaseProvider.Instance(getActivity()).worker().towns().getCachedTowns();
        }

        return stations;
    }

}
