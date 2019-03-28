package com.dt.anh.doranews.adapter.recyclerview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dt.anh.doranews.DetailEventActivity;
import com.dt.anh.doranews.R;
import com.dt.anh.doranews.model.result.longevent.Datum;
import com.dt.anh.doranews.util.ConstParamAPI;
import com.dt.anh.doranews.util.ConstParamTransfer;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class LongEventAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Datum> mListLongEvents;
    private LayoutInflater mLayoutInflater;
    private Context mContext;
    private String idCurrentEvent;
    private LinearLayoutManager linearLayoutManager;
    boolean flagFinishLoadData = false; //true, false ám chỉ lần load này đã phải cuối chưa? true tức là cuối rồi
    int lastVisibleItem, totalItemCount;
    boolean isLoading;
    private final int VIEW_TYPE_ITEM = 0, VIEW_TYPE_LOADING = 1;
//    public static int VISIBLE_THRESHOLD = 3;
    ILoadMore loadMore;
    private String idLongEvent;

    public void addItemLoadMore() {
        mListLongEvents.add(null);
        notifyItemInserted(mListLongEvents.size() - 1);

    }

    public void removeItemLoadMore() {
        mListLongEvents.remove(mListLongEvents.size() - 1);
        notifyItemRemoved(mListLongEvents.size());
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public LongEventAdapter(List<Datum> listLongEvents, Context mContext, String idEvent, String idLongEvent, RecyclerView recyclerView, NestedScrollView nestedScrollView) {
        this.mListLongEvents = listLongEvents;
        this.mContext = mContext;
        this.idCurrentEvent = idEvent;
        this.idLongEvent = idLongEvent;

        linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        if (!flagFinishLoadData) {
            //Nếu flagFinishLoadData == false, tức là vẫn còn, load tiếp đê
            //Nếu flagFinishLoadData == true, tức là hết rồi, đừng load nữa
            nestedScrollView.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
                int lastVisibleItemX = linearLayoutManager.findLastVisibleItemPosition();
//                Log.e("piPI-in", lastVisibleItemX + "");
                if (isFlagFinishLoadData()) {
                    return;
                }
                if (v.getChildAt(v.getChildCount() - 1) != null) {
                    if ((scrollY >= (v.getChildAt(v.getChildCount() - 1).getMeasuredHeight() - v.getMeasuredHeight())) &&
                            scrollY > oldScrollY) {
                        //code to fetch more data for endless scrolling
                        if (scrollY > 0) {
                            totalItemCount = linearLayoutManager.getItemCount();
                            lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                            notifyItemChanged(lastVisibleItem);
                            if (!isLoading && totalItemCount <= (lastVisibleItem + ConstParamAPI.NUMBER_LONG_EVENT_PER_PAGE)) {
                                if (loadMore != null) {
                                    loadMore.onLoadMore();
                                }
                                isLoading = true;
                            }
                        }
                    }
                }
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        return mListLongEvents.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    public void setLoadMore(ILoadMore loadMore) {
        this.loadMore = loadMore;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (mLayoutInflater == null) {
            mLayoutInflater = LayoutInflater.from(parent.getContext());
        }

        if (viewType == VIEW_TYPE_ITEM) {
            View view = mLayoutInflater
                    .inflate(R.layout.item_long_events, parent, false);
            return new ViewHolder(view);
        } else if (viewType == VIEW_TYPE_LOADING) {
            View view = mLayoutInflater
                    .inflate(R.layout.item_loading, parent, false);
            return new LoadingViewHolder(view);
        }
        return null;
    }

    public boolean isFlagFinishLoadData() {
        return flagFinishLoadData;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
//        holder.bindData(mListLongEvents.get(position));
        if (holder instanceof LongEventAdapter.ViewHolder) {
            int posi = position;
            Datum datum = mListLongEvents.get(position);
            ViewHolder viewHolder = (ViewHolder) holder;
            viewHolder.bindData(datum);
        } else if (holder instanceof LoadingViewHolder) {
            if (flagFinishLoadData) {
                //false - tức là vẫn còn, hãy load tiếp?
                return;
            }
            LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
            loadingViewHolder.progressBar.setIndeterminate(true);
        }
    }

    @Override
    public int getItemCount() {
        if (mListLongEvents == null) {
            return 0;
        }
        return mListLongEvents.size();
    }

    public void setLoaded() {
        isLoading = false;
    }

    public class LoadingViewHolder extends RecyclerView.ViewHolder {

        public ProgressBar progressBar;

        public LoadingViewHolder(View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progress_bar_news);
        }
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
            if (event.getId().equals(idCurrentEvent)) {
//                    Log.e("Disappear idEvent-pro: ", event.getTitle());
//                    Log.e("Disappear idCurrent", idCurrentEvent);
                    lnlLayout.setEnabled(false);
                    viewLongEvent.setVisibility(View.VISIBLE);
            } else {
                lnlLayout.setEnabled(true);
                viewLongEvent.setVisibility(View.GONE);
            }
            //Cần split time ra trước
            String time = event.getTime();
            String[] arrayLongEvent = time.split("T");

            String timeFormat = arrayLongEvent[0];

            @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            try {
                Date date = simpleDateFormat.parse(timeFormat);
                @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateNewFormat = new SimpleDateFormat("dd-MM-yyyy");
                String dateAfterFormatVN = simpleDateNewFormat.format(date);
                mTextTime.setText(dateAfterFormatVN);
            } catch (ParseException e) {
                e.printStackTrace();
                mTextTime.setText(arrayLongEvent[0]);
            }

            mTextTitle.setText(event.getTitle());
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            String id = mListLongEvents.get(position).getId();
            Intent intent = new Intent(mContext, DetailEventActivity.class);
            intent.putExtra(ConstParamTransfer.PARAM_ID_EVENT, id);
            intent.putExtra(ConstParamTransfer.PARAM_ID_LONG_EVENT, idLongEvent);
            mContext.startActivity(intent);
        }
    }

    /**
     * Update adapter when list events is changed
     *
     * @param listEvents List songs after being changed
     */
    public void updateListLongEvents(List<Datum> listEvents) {
        boolean flag = true;

        for (Datum datum : mListLongEvents) {
            for (Datum datumCmp : listEvents) {
                if (datum.getId().equals(datumCmp.getId())) {
                    flag = false;
                    break;
                }
            }
            if (!flag) {
                break;
            }
        }
        if (flag) {
            mListLongEvents.addAll(listEvents);
            notifyDataSetChanged();
            this.setLoaded();
        }
    }

    public void setFlagFinishLoadData(boolean flagFinishLoadData) {
        this.flagFinishLoadData = flagFinishLoadData;
    }
}
