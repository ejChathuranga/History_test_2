package com.android.ejsoft.history_test_2.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.android.ejsoft.history_test_2.R;
import com.android.ejsoft.history_test_2.base.BaseFragment;

public class HomeFragment extends BaseFragment {

    private static final Option[] options = new Option[] {


            new Option("Browser", null),
            new Option("Bookmarks", BookmarksFragment.class),
            new Option("Searches", SearchesFragment.class),
          
    };

    public final static class Option {
        final String name;
        final Class<? extends Fragment> fragment;
        int param;

        Option(String name, Class<? extends Fragment> fragment) {
            this.name = name;
            this.fragment = fragment;
        }

        Option(String name, Class<? extends Fragment> fragment, int param) {
            this(name, fragment);
            this.param = param;
        }
    }

    private class OnMenuItemSelected implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Option option = options[position];
            Class<? extends Fragment> fragment = option.fragment;
            setFragment(fragment, option.param);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        setToolbarTitle("Home");
        OnMenuItemSelected onMenuItemSelected = new OnMenuItemSelected();
        onMenuItemSelected.onItemClick(null,null,1,12312312);
        //CollectDate.execute();
//        ListView listView = (ListView) view.findViewById(R.id.list);
//        listView.setAdapter(new MenuAdapter());
//        listView.setOnItemClickListener(new OnMenuItemSelected());
        return view;
    }



}
