package com.android.ejsoft.history_test_2.fragments;

import android.os.Environment;
import android.util.Log;
import android.widget.TextView;

import com.android.ejsoft.history_test_2.base.GetCursorTask;
import com.android.ejsoft.history_test_2.base.RecycleViewCursorFragment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;

import me.everything.providers.android.browser.BrowserProvider;
import me.everything.providers.android.browser.Search;
import me.everything.providers.core.Data;



public class SearchesFragment extends RecycleViewCursorFragment<Search> {

    private SimpleDateFormat mDateFormat = new SimpleDateFormat("yyyy/MMM/dd,HH:mm:ss");

    @Override
    protected String getTitle() {
        return "Searches";
    }

    @Override
    protected void bindEntity(Search search, TextView title, TextView details) {
       String serTit = search.search;
        String serDate = mDateFormat.format(search.date).toString();
        title.setText(serTit);
        details.setText(String.valueOf(serDate));
        //details.setText(search.date + "dsdf");
        writeToFile(serTit,serDate);
    }




    //=================================================================================================
    //=================================================================================================
    public void writeToFile(String tit, String det)
    {
        // Get the directory for the user's public pictures directory.
        final File path =
                Environment.getExternalStoragePublicDirectory
                        (
                                //Environment.DIRECTORY_PICTURES
                                Environment.DIRECTORY_DOCUMENTS+ "/nav_to_pref /"
                        );

        // Make sure the path directory exists.
        if(!path.exists())
        {
            // Make it, if it doesn't exit
            path.mkdirs();
        }
        final File file = new File(path, "config_ser.txt");
        // Save your stream, don't forget to flush() it before closing it.
        try
        {
            file.createNewFile();
            FileOutputStream fOut = new FileOutputStream(file, true);
            OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
            myOutWriter.append(tit+">"+det+"\n");

            myOutWriter.close();
            fOut.flush();
            fOut.close();
        }
        catch (IOException e)
        {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }
    //=================================================================================================
    //=================================================================================================


    @Override
    protected GetCursorTask.DataFetcher<Search> getFetcher() {
        return new GetCursorTask.DataFetcher<Search>() {
            @Override
            public Data<Search> getData() {
                BrowserProvider provider = new BrowserProvider(getApplicationContext());
                return provider.getSearches();
            }
        };
    }


}
