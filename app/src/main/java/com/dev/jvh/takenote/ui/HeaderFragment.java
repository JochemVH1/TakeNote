package com.dev.jvh.takenote.ui;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dev.jvh.takenote.R;

/**
 * Created by JochemVanHespen on 10/29/2017.
 * Fragment which displays information about the app
 */

public class HeaderFragment extends Fragment {
    private TextView headerTextView;
    private UpdateTitleView updateTitleView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View headerView = inflater.inflate(R.layout.header_fragment, container, false);
        headerTextView = headerView.findViewById(R.id.titleViewMainActivity);
        updateTitleView = new UpdateTitleView();
        updateTitleView.execute();
        return headerView;

    }

    @Override
    public void onResume() {
        super.onResume();
        updateTitleView = new UpdateTitleView();
        updateTitleView.execute();
    }

    private class UpdateTitleView extends AsyncTask<Void,String,Void>
    {
        private String[] titles;
        private int counter;
        UpdateTitleView()
        {
            titles = getResources().getStringArray(R.array.app_info_titles);
            counter = 0;
        }
        @Override
        protected Void doInBackground(Void... params) {
            try
            {
                do {
                    publishProgress(titles[counter++]);
                    if(counter == titles.length)
                        counter = 0;
                    Thread.sleep(20000);
                }while(true);
            }catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }
        // called from doInBackground with publishProgress, can modify UI
        @Override
        protected void onProgressUpdate(String... parameters) {
            headerTextView.setText(parameters[0]);
        }
    }
}
