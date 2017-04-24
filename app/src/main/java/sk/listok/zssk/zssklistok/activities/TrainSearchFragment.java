package sk.listok.zssk.zssklistok.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SearchView;

import java.util.ArrayList;

import sk.listok.zssk.zssklistok.R;
import sk.listok.zssk.zssklistok.datalayer.DatabaseProvider;
import sk.listok.zssk.zssklistok.datalayer.objects.Town;


public class TrainSearchFragment extends Fragment {

    private View rootView;
    private ArrayList<Town> stations;

    private ListView lv;
    private SearchView sv;
    HRArrayAdapter<Town> adap;

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
        adap = new HRArrayAdapter<Town>(getActivity(), android.R.layout.simple_list_item_1, stations);

        lv.setAdapter(adap);
        sv.clearFocus();// hodim focus na vyhladavanie

        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adap.getFilter().filter(newText);
                return true;
            }
        });

        return rootView;
    }

    /**
     * Vyčistenie filtra, kde používateľ píše.
     */
    public void clearFilter() {
        sv.setQuery("", false);
        adap.getFilter().filter("");
    }

    /**
     * Načítanie miest z databázy do arraylistu.
     *
     * @return
     */
    private ArrayList<Town> getTowns() {
        if (stations == null || stations.size() == 0) {
            stations = DatabaseProvider.Instance(getActivity()).worker().towns().getCachedTowns();
        }
        return stations;
    }

}
