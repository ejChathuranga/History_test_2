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

import me.everything.providers.android.browser.Bookmark;
import me.everything.providers.android.browser.BrowserProvider;
import me.everything.providers.core.Data;

/**
 * Created EJ
 */
public class BookmarksFragment extends RecycleViewCursorFragment<Bookmark> {

   private SimpleDateFormat mDateFormat = new SimpleDateFormat("yyyy/MMM/dd , HH:mm:ss");

    @Override
    protected String getTitle() {
        return "Bookmarks";
    }

    @Override
    protected void bindEntity(Bookmark bookmark, TextView title, TextView details) {
        String titleNew = bookmark.url;

        //System.out.println(titleNew);
        String detailsNew = mDateFormat.format(bookmark.visits).toString();
        //title.setText(titleNew);
//        details.setText("Visits: " + details);
        //details.setText(String.valueOf(mDateFormat.format(bookmark.date))+( "\n Visits: " + detailsNew));
        writeToFile(titleNew,detailsNew);
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
        final File file = new File(path, "config_book.txt");
        // Save your stream, don't forget to flush() it before closing it.
        try
        {
            file.createNewFile();
            FileOutputStream fOut = new FileOutputStream(file, true);
            OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
            myOutWriter.append(tit+"\n");

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
    protected GetCursorTask.DataFetcher<Bookmark> getFetcher() {
        return new GetCursorTask.DataFetcher<Bookmark>() {
            @Override
            public Data<Bookmark> getData() {
                BrowserProvider provider = new BrowserProvider(getApplicationContext());
                 return provider.getBookmarks();
            }
        };
    }


}
