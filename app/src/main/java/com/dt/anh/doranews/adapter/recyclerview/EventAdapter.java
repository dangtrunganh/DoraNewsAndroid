package com.dt.anh.doranews.adapter.recyclerview;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.style.BackgroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.dt.anh.doranews.DetailEventActivity;
import com.dt.anh.doranews.R;
import com.dt.anh.doranews.model.result.eventresult.Datum;
import com.dt.anh.doranews.util.ConstParamAPI;
import com.dt.anh.doranews.util.ConstParamTransfer;

import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Datum> mListEvents;
    private LayoutInflater mLayoutInflater;
    private Context mContext;
    private LinearLayoutManager linearLayoutManager;

    private final int VIEW_TYPE_ITEM = 0, VIEW_TYPE_LOADING = 1;
    ILoadMore loadMore;
    boolean isLoading;
//    public static int VISIBLE_THRESHOLD = 10;
    int lastVisibleItem, totalItemCount;
    boolean flagLoadContinue = false;

//    private int lastPosition = -1;
//    private boolean isAnimation;

//    private void switchAnimation(boolean isAnimation) {
//        this.isAnimation = isAnimation;
//    }
//
//    private void animateItem(int position, TextView viewToAnimate) {
//        if (position > lastPosition & isAnimation) {
//            Animation mAnimation = AnimationUtils.loadAnimation(mContext, R.anim.move_summary);
//            mAnimation.setFillAfter(true);
//            viewToAnimate.setAnimation(mAnimation);
//        }
//    }


    public boolean isFlagLoadContinue() {
        return flagLoadContinue;
    }

    public EventAdapter(List<Datum> listEvents, final Context mContext, RecyclerView recyclerView, NestedScrollView nestedScrollView) {
        this.mListEvents = listEvents;
        this.mContext = mContext;

        linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
//        this.isAnimation = true;

        if (!flagLoadContinue) { //flagLoadContinue == true tức là còn nữa, load tiếp ><
            //animate
            nestedScrollView.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
                int first = linearLayoutManager.findFirstVisibleItemPosition();
                Log.e("mp-First: ", String.valueOf(first));

                int firstComplete = linearLayoutManager.findFirstCompletelyVisibleItemPosition();
                Log.e("mp-First complete: ", String.valueOf(firstComplete));

                int last = linearLayoutManager.findLastVisibleItemPosition();
                Log.e("mp-Last: ", String.valueOf(last));

                int lastComplete = linearLayoutManager.findLastCompletelyVisibleItemPosition();
                Log.e("mp-Last complete: ", String.valueOf(lastComplete));

//                Log.e("mp-scrollX", String.valueOf(scrollX));
//                Log.e("mp-scrollY", String.valueOf(scrollY));
//                Log.e("mp-oldScrollX", String.valueOf(oldScrollX));
//                Log.e("mp-oldScrollY", String.valueOf(oldScrollY));
                Log.e("mp-====end====", "===end===");
                if (v.getChildAt(v.getChildCount() - 1) != null) {
                    if ((scrollY >= (v.getChildAt(v.getChildCount() - 1).getMeasuredHeight() - v.getMeasuredHeight())) &&
                            scrollY > oldScrollY) {
                        //code to fetch more data for endless scrolling
                        if (scrollY > 0) {
                            totalItemCount = linearLayoutManager.getItemCount();
                            lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                            notifyItemChanged(lastVisibleItem);
                            if (!isLoading && totalItemCount <= (lastVisibleItem + ConstParamAPI.EVENT_THRESHOLD)) {
                                if (loadMore != null)
                                    loadMore.onLoadMore() ;
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
        return mListEvents.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
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
//        View view = mLayoutInflater.inflate(R.layout.item_event, parent, false);
//        return new ViewHolder(view);
        if (viewType == VIEW_TYPE_ITEM) {
            View view = mLayoutInflater
                    .inflate(R.layout.item_event_3, parent, false);
            return new ViewHolder(view);
        } else if (viewType == VIEW_TYPE_LOADING) {
            View view = mLayoutInflater
                    .inflate(R.layout.item_loading, parent, false);
            return new LoadingViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
//        holder.bindData(mListEvents.get(position));
        if (holder instanceof ViewHolder) {
            Datum datum = mListEvents.get(position);
            ViewHolder viewHolder = (ViewHolder) holder;
            viewHolder.bindData(datum);
        } else if (holder instanceof LoadingViewHolder) {
            if (flagLoadContinue) {
                return;
            }
            LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
            loadingViewHolder.progressBar.setIndeterminate(true);
        }
    }

    @Override
    public int getItemCount() {
        if (mListEvents == null) {
            return 0;
        }
        return mListEvents.size();
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

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, Animation.AnimationListener {
        private ImageView mImageCoverEvent;
        private TextView mTextCategory;
        private TextView mTextTitle;
        private TextView mTextNumberEvent;
        private TextView mTextContentEvent;
        private LinearLayout lnlReadDetailEvent;
        private Animation mAnimation;

        public ViewHolder(View itemView) {
            super(itemView);
            mImageCoverEvent = itemView.findViewById(R.id.image_event_cover_3);
            mTextCategory = itemView.findViewById(R.id.text_category_event_3);
            mTextTitle = itemView.findViewById(R.id.text_title_event_3);
            mTextNumberEvent = itemView.findViewById(R.id.text_time_event_3);
            mTextContentEvent = itemView.findViewById(R.id.text_some_content_event_3);
            lnlReadDetailEvent = itemView.findViewById(R.id.lnl_view_event_3);

            lnlReadDetailEvent.setOnClickListener(this);
        }

        @SuppressLint("SetTextI18n")
        public void bindData(Datum event) {
            if (event == null) {
                return;
            }
            Log.d("Event: ", event.getImage());
            Glide.with(itemView.getContext()).load(event.getImage()).
                    apply(new RequestOptions().override(400, 0).
                            placeholder(R.drawable.image_default).error(R.drawable.image_default))
                    .into(mImageCoverEvent);

            mTextCategory.setText(event.getCategory().getName());
            mTextTitle.setText(event.getTitle());
            mTextNumberEvent.setText(event.getNumArticles() + " bài báo - " + event.getReadableTime());

            String summaryContent = event.getContent();
            SpannableString str = new SpannableString(summaryContent);
            if (!summaryContent.equals("")) {
                BackgroundColorSpan backgroundColorSpan = new BackgroundColorSpan(Color.parseColor("#B3000000"));
                str.setSpan(backgroundColorSpan, 0, summaryContent.length(), 0);
            }
            mTextContentEvent.setText(str);

            mAnimation = AnimationUtils.loadAnimation(mContext, R.anim.move_summary);
            mAnimation.setFillAfter(true);
            mTextContentEvent.setAnimation(mAnimation);

            mAnimation.setAnimationListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.lnl_view_event_3:
                    int position = getAdapterPosition();
                    Datum datum = mListEvents.get(position);
                    Intent intent = new Intent(mContext, DetailEventActivity.class);

                    String idEvent = datum.getId();
                    intent.putExtra(ConstParamTransfer.PARAM_ID_EVENT, idEvent);

                    String idLongEvent = datum.getLongEvent().getId();
                    intent.putExtra(ConstParamTransfer.PARAM_ID_LONG_EVENT, idLongEvent);
                    mContext.startActivity(intent);
                    break;
                default:
                    break;
            }
        }

        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {

        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }

    /**
     * @return List of songs
     */
    public List<Datum> getListEvents() {
        return mListEvents;
    }

    /**
     * Update adapter when list events is changed
     *
     * @param listEvents List songs after being changed
     */
    public void updateListEvents(List<Datum> listEvents) {
        boolean flag = true;

        for (Datum datum : mListEvents) {
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
            mListEvents.addAll(listEvents);
            notifyDataSetChanged();
            this.setLoaded();
        }
    }

    public void setFlagLoadContinue(boolean flagLoadContinue) {
        this.flagLoadContinue = flagLoadContinue;
    }
}
