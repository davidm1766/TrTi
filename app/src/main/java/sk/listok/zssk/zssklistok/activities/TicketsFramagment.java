package sk.listok.zssk.zssklistok.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import sk.listok.zssk.zssklistok.R;
import sk.listok.zssk.zssklistok.helpers.FileHelper;
import sk.listok.zssk.zssklistok.objects.Ticket;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class TicketsFramagment extends Fragment {

    private View rootView;
    private ArrayList<Ticket> tickets;

    private ListView lv;
    ArrayAdapter<Ticket> adapter;



    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        populatestationsList();
        rootView = inflater.inflate(R.layout.fragment_ticket_list, container, false);
        lv = (ListView) rootView.findViewById(R.id.listViewTicket);
        adapter = new ArrayAdapter<Ticket>(getActivity(),android.R.layout.simple_expandable_list_item_1,tickets);
        lv.setAdapter(adapter);
        return rootView;
    }


    /**
     * Naplní arraylist lístkami, ktoré sú
     * uložene v mobile.
     */
    private void populatestationsList() {
        File dir = FileHelper.getTicketFolder();

        String[] names = dir.list(
                new FilenameFilter()
                {
                    public boolean accept(File dir, String name)
                    {
                        return name.endsWith(".png");
                    }
                });
        tickets= new ArrayList<Ticket>();
        if(names == null){
            return;
        }
        for(String filename : names) {
            try {
                tickets.add(new Ticket(filename));
            } catch (IllegalArgumentException e){
                //zly nazov suboru
            }
        }
        /**
         * zotriedm listky tak aby boli
         * od najnovsie kupenych po nastarsie
         */
        Collections.sort(tickets, new Comparator<Ticket>() {
            @Override
            public int compare(Ticket tic1, Ticket tic2)
            {
                return  tic2.getFilename().compareTo(tic1.getFilename());
            }
        });

    }


}
