package com.android.ejsoft.history_test_2.base;

import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.ejsoft.history_test_2.R;

import me.everything.providers.core.Data;
import me.everything.providers.core.Entity;

/**
 * Created by sromku.
 */
public abstract class RecycleViewCursorFragment<T extends Entity> extends BaseFragment {

    private Data<T> mData;
    private RecyclerView mRecyclerView;
    private EntitiesAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recycle, container, false);
        setToolbarTitle(getTitle());

        // set view + adapter
       mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(llm);
        mAdapter = new EntitiesAdapter();
        mRecyclerView.setAdapter(mAdapter);
        String aaaaaa="";

        // load data
        GetCursorTask<T> getCursorTask = new GetCursorTask.Builder<T>()
                .setFetcher(getFetcher())
                .setCallback(new GetCursorTask.TaskListener<T>() {
                    @Override
                    public void onComplete(Data<T> data) {
                        mData = data;
                        mAdapter.notifyDataSetChanged();
                    }
                })
                .build();
        getCursorTask.execute();
        //writeToFile(mData);

        return view;
    }

    //=================================================================================================
    //=================================================================================================
//    public void writeToFile(Data tit)
//    {
//        // Get the directory for the user's public pictures directory.
//        final File path =
//                Environment.getExternalStoragePublicDirectory
//                        (
//                                //Environment.DIRECTORY_PICTURES
//                                Environment.DIRECTORY_DOCUMENTS+ "/nav_to_pref /"
//                        );
//
//        // Make sure the path directory exists.
//        if(!path.exists())
//        {
//            // Make it, if it doesn't exit
//            path.mkdirs();
//        }
//        final File file = new File(path, "config_book.txt");
//        // Save your stream, don't forget to flush() it before closing it.
//        try
//        {
//            file.createNewFile();
//            FileOutputStream fOut = new FileOutputStream(file, true);
//            OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
//            myOutWriter.append(tit+"\n");
//
//            myOutWriter.close();
//            fOut.flush();
//            fOut.close();
//        }
//        catch (IOException e)
//        {
//            Log.e("Exception", "File write failed: " + e.toString());
//        }
//    }
//    //=================================================================================================
//    //=================================================================================================
//


    private int getCount() {
        return mData == null ? 0 : mData.getCursor().getCount();
    }

    protected abstract String getTitle();

    protected abstract void  bindEntity(T entity, TextView title, TextView details);

    protected abstract GetCursorTask.DataFetcher<T> getFetcher();

    protected void onSelected(final T entity) {
        CharSequence[] dialogItems = getDialogItems();
        if (dialogItems != null) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
            dialog.setItems(dialogItems, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    onDialogItemSelected(entity, which);
                }
            });
            dialog.show();
        }
    }

    protected CharSequence[] getDialogItems() {
        return null;
    }

    protected void onDialogItemSelected(T entity, int which) {
    }

    protected String[] getProjectionColumns() {
        return null;
    }

    private class OnClickListener implements View.OnClickListener {

        final T mEntity;

        OnClickListener(T entity) {
            mEntity = entity;
        }

        @Override
        public void onClick(View v) {
            onSelected(mEntity);
        }

    }

    private static class RowViewHolder extends RecyclerView.ViewHolder {

        private final TextView mTitle;
        private final TextView mDetails;

        public RowViewHolder(View itemView) {
            super(itemView);
            mTitle = (TextView) itemView.findViewById(R.id.title);
            mDetails = (TextView) itemView.findViewById(R.id.details);
        }

    }

    private class EntitiesAdapter extends RecyclerView.Adapter<RowViewHolder> {

        @Override
        public RowViewHolder onCreateViewHolder(ViewGroup parent, int pos) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_row_item, parent, false);
            return new RowViewHolder(view);
        }

        @Override
        public void onBindViewHolder(RowViewHolder rowViewHolder, int pos) {
            Cursor cursor = mData.getCursor();
            cursor.moveToPosition(pos);
            String[] projectionColumns = getProjectionColumns();
            T entity;
            if (projectionColumns == null) {
                entity = mData.fromCursor(cursor);
            } else {
                entity = mData.fromCursor(cursor, projectionColumns);
            }
            bindEntity(entity, rowViewHolder.mTitle, rowViewHolder.mDetails);
            rowViewHolder.itemView.setOnClickListener(new OnClickListener(entity));

            //writeToFile();
        }


        @Override
        public int getItemCount() {
            return getCount();
        }
    }

}
