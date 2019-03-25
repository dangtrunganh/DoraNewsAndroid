package com.dt.anh.doranews.adapter.recyclerview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.dt.anh.doranews.DetailEventActivity;
import com.dt.anh.doranews.R;
import com.dt.anh.doranews.model.Event;
import com.dt.anh.doranews.model.LongEvent;
import com.dt.anh.doranews.model.result.longevent.Datum;
import com.dt.anh.doranews.util.ConstParamTransfer;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class LongEventAdapter extends RecyclerView.Adapter<LongEventAdapter.ViewHolder> {
    private List<Datum> mListLongEvents;
    private LayoutInflater mLayoutInflater;
    private Context mContext;
    private String idCurrentEvent;

    public LongEventAdapter(List<Datum> listLongEvents, Context mContext, String id) {
        this.mListLongEvents = listLongEvents;
        this.mContext = mContext;
        idCurrentEvent = id;
    }

    public void setIdCurrentEvent(String idCurrentEvent) {
        this.idCurrentEvent = idCurrentEvent;
    }

    @NonNull
    @Override
    public LongEventAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (mLayoutInflater == null) {
            mLayoutInflater = LayoutInflater.from(parent.getContext());
        }
        View view = mLayoutInflater.inflate(R.layout.item_long_events, parent, false);
        return new LongEventAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LongEventAdapter.ViewHolder holder, int position) {
        holder.bindData(mListLongEvents.get(position));
    }

    @Override
    public int getItemCount() {
        if (mListLongEvents == null) {
            return 0;
        }
        return mListLongEvents.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mTextTime;
        private TextView mTextTitle;
        private FrameLayout lnlLayout;
        private View viewLongEvent;

        public ViewHolder(View itemView) {
            super(itemView);
            mTextTime = itemView.findViewById(R.id.txt_time_long_events);
            mTextTitle = itemView.findViewById(R.id.txt_title_long_events);
            lnlLayout = itemView.findViewById(R.id.lnl_long_event);
            viewLongEvent = itemView.findViewById(R.id.view_long_event);
            itemView.setOnClickListener(this);
        }

        @SuppressLint("SetTextI18n")
        public void bindData(Datum event) {
            if (event == null) {
                return;
            }
//            Log.d("Long Event: ", event.getTitle());
//            Log.e("LKLK1:", "event.getId=" + event.getId());
//            Log.e("LKLK2:", "idCurrentEvent=" + idCurrentEvent);
            if (event.getId().equals(idCurrentEvent)) {
                lnlLayout.setEnabled(false);
                viewLongEvent.setVisibility(View.VISIBLE);
            }
            //Cần split time ra trước
            String time = event.getTime();
            String[] arrayLongEvent = time.split("T");

            String timeFormat = arrayLongEvent[0];
            //====
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            try {
                Date date = simpleDateFormat.parse(timeFormat);
                SimpleDateFormat simpleDateNewFormat = new SimpleDateFormat("dd-MM-yyyy");
                String dateAfterFormatVN = simpleDateNewFormat.format(date);
                mTextTime.setText(dateAfterFormatVN);
            } catch (ParseException e) {
                e.printStackTrace();
                mTextTime.setText(arrayLongEvent[0]);
            }
            //====
            mTextTitle.setText(event.getTitle());
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
//            mContext.startActivity(new Intent(mContext, DetailEventActivity.class));
//            Toast.makeText(mContext, "Click long events!", Toast.LENGTH_SHORT).show();
            String id = mListLongEvents.get(position).getId();
            Intent intent = new Intent(mContext, DetailEventActivity.class);
            intent.putExtra(ConstParamTransfer.PARAM_ID_EVENT, id);
            mContext.startActivity(intent);
        }
    }

    /**
     * @return List of songs
     */
    public List<Datum> getListEvents() {
        return mListLongEvents;
    }

    /**
     * Update adapter when list events is changed
     *
     * @param listEvents List songs after being changed
     */
    public void updateListLongEvents(List<Datum> listEvents) {
        mListLongEvents = listEvents;
        notifyDataSetChanged();
    }
}
