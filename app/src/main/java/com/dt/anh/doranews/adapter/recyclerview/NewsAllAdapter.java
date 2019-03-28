//package com.dt.anh.doranews.adapter.recyclerview;
//
//import android.annotation.SuppressLint;
//import android.app.Activity;
//import android.content.Context;
//import android.content.Intent;
//import android.support.annotation.NonNull;
//import android.support.v4.widget.NestedScrollView;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.ProgressBar;
//import android.widget.TextView;
//
//import com.bumptech.glide.Glide;
//import com.bumptech.glide.request.RequestOptions;
//import com.dt.anh.doranews.DetailEventActivity;
//import com.dt.anh.doranews.DetailNewsActivity;
//import com.dt.anh.doranews.R;
//import com.dt.anh.doranews.model.result.eventdetailresult.Article;
//import com.dt.anh.doranews.model.result.eventresult.Datum;
//import com.dt.anh.doranews.util.ConstParamTransfer;
//import com.google.gson.Gson;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class NewsAllAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
//    private List<Article> mListArticles;
//    private List<Article> mListCurrentArticles;
//    private LayoutInflater mLayoutInflater;
//    private Context mContext;
//    private String titleEvent;
//    int currentItemCount = 0;
//
//    private static final int VISIBLE_THRESHOLD = 5;
//
//    private final int VIEW_TYPE_ITEM = 0, VIEW_TYPE_LOADING = 1;
//    ILoadMore loadMore;
//    boolean isLoading;
//    Activity mActivity;
//    int visibleThreshold = 5;
//    int lastVisibleItem, totalItemCount;
//    boolean flagFinishLoadData = false;
//
//    public NewsAllAdapter(List<Article> listArticles, final Context mContext, RecyclerView recyclerView, NestedScrollView nestedScrollView) {
//        this.mListArticles = listArticles;
//        this.mContext = mContext;
//        this.mListCurrentArticles = new ArrayList<>();
//
//        final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
//
//        if (!flagFinishLoadData) {
//            nestedScrollView.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
//                if (v.getChildAt(v.getChildCount() - 1) != null) {
//                    if ((scrollY >= (v.getChildAt(v.getChildCount() - 1).getMeasuredHeight() - v.getMeasuredHeight())) &&
//                            scrollY > oldScrollY) {
//                        //code to fetch more data for endless scrolling
//                        if (scrollY > 0) {
//                            totalItemCount = linearLayoutManager.getItemCount();
//                            lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
//                            if (!isLoading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
//                                if (loadMore != null)
//                                    loadMore.onLoadMore();
//                                isLoading = true;
//                            }
//                        }
//                    }
//                }
//            });
//        }
//    }
//
//    @Override
//    public int getItemViewType(int position) {
//        return mListCurrentArticles.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
//    }
//
//    public void setTitleEvent(String titleEvent) {
//        this.titleEvent = titleEvent;
//    }
//
//    public void setLoadMore(ILoadMore loadMore) {
//        this.loadMore = loadMore;
//    }
//
//    @NonNull
//    @Override
//    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        if (mLayoutInflater == null) {
//            mLayoutInflater = LayoutInflater.from(parent.getContext());
//        }
////        View view = mLayoutInflater.inflate(R.layout.item_event, parent, false);
////        return new ViewHolder(view);
//
//
//        if (viewType == VIEW_TYPE_ITEM) {
//            View view = mLayoutInflater
//                    .inflate(R.layout.item_news_all, parent, false);
//            return new NewsAllAdapter.ViewHolder(view);
//        } else if (viewType == VIEW_TYPE_LOADING) {
//            View view = mLayoutInflater
//                    .inflate(R.layout.item_loading, parent, false);
//            return new NewsAllAdapter.LoadingViewHolder(view);
//        }
//        return null;
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
////        holder.bindData(mListEvents.get(position));
//        if (holder instanceof NewsAllAdapter.ViewHolder) {
//            Article article = mListCurrentArticles.get(position);
//            NewsAllAdapter.ViewHolder viewHolder = (NewsAllAdapter.ViewHolder) holder;
//            viewHolder.bindData(article);
//        } else if (holder instanceof EventAdapter.LoadingViewHolder) {
//            if (!flagFinishLoadData) {
//                return;
//            }
//            NewsAllAdapter.LoadingViewHolder loadingViewHolder = (NewsAllAdapter.LoadingViewHolder) holder;
//            loadingViewHolder.progressBar.setIndeterminate(true);
//        }
//    }
//
//    @Override
//    public int getItemCount() {
//        if (mListCurrentArticles == null) {
//            return 0;
//        }
//        return mListCurrentArticles.size();
//    }
//
//    public void setLoaded() {
//        isLoading = false;
//    }
//
//    public class LoadingViewHolder extends RecyclerView.ViewHolder {
//
//        public ProgressBar progressBar;
//
//        public LoadingViewHolder(View itemView) {
//            super(itemView);
//            progressBar = itemView.findViewById(R.id.progress_bar_news);
//        }
//    }
//
//    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
//        private ImageView mImageCoverArticle;
////        private TextView mTextSource;
//        private TextView mTextTitle;
//        private TextView mTextTimeReadable;
//
//        public ViewHolder(View itemView) {
//            super(itemView);
//            mImageCoverArticle = itemView.findViewById(R.id.image_cover_news_all);
////            mTextSource = itemView.findViewById(R.id.text_source_news_all);
//            mTextTitle = itemView.findViewById(R.id.text_title_news_all);
//            mTextTimeReadable = itemView.findViewById(R.id.text_time_news_all);
//
//            itemView.setOnClickListener(this);
//        }
//
//        @SuppressLint("SetTextI18n")
//        public void bindData(Article article) {
//            if (article == null) {
//                return;
//            }
//            Log.d("article: ", article.getImage());
//            Glide.with(itemView.getContext()).load(article.getImage()).
//                    apply(new RequestOptions().override(400, 0).
//                            placeholder(R.drawable.ic_launcher_background).error(R.drawable.ic_launcher_background))
//                    .into(mImageCoverArticle);
//
//            String source = "(Nguồn: " + article.getSource().getName() + ")";
////            mTextSource.setText(source);
//            mTextTitle.setText(article.getTitle());
//            mTextTimeReadable.setText(article.getReadableTime());
//        }
//
//        @Override
//        public void onClick(View view) {
//            int position = getAdapterPosition();
//            Gson gson = new Gson();
//            String json = gson.toJson(mListArticles);
//            Intent intent = new Intent(mContext, DetailNewsActivity.class);
//            intent.putExtra(ConstParamTransfer.TITLE_EVENT, titleEvent);
//            intent.putExtra(ConstParamTransfer.DETAIL_NEWS, json);
//            intent.putExtra(NewsInCategoryFrgAdapter.POSITION_CLICK, position);
//
//            mContext.startActivity(intent);
//        }
//    }
//
//    /**
//     * @return List of songs
//     */
//    public List<Article> getListArticles() {
//        return mListArticles;
//    }
//
//    /**
//     * Update adapter when list events is changed
//     *
//     * @param mListArticless List songs after being changed
//     */
//    public void updateListArticles(List<Article> mListArticless) {
//////        mListEvents = listEvents;
////        if (mListArticles.size() - mListCurrentArticles.size() >= VISIBLE_THRESHOLD) {
////            this.flagFinishLoadData = true;
////        }
////        if (mListArticles.size() <= VISIBLE_THRESHOLD) {
////            mListCurrentArticles.addAll(mListArticles);
////            currentItemCount = listNews.size();
////        } else {
////            for (int i = 0; i < VISIBLE_THRESHOLD; i++) {
////                mCurrentListNews.add(listNews.get(i));
////                currentItemCount = VISIBLETHRESHOLD;
////            }
////        }
////        notifyDataSetChanged();
////        this.setLoaded();
//
//        this.mListArticles = mListArticless;
//        if (mListArticless.size() <= VISIBLE_THRESHOLD) {
//            mListCurrentArticles.addAll(mListArticles);
//            currentItemCount = mListArticles.size();
//        } else {
//            for (int i = 0; i < VISIBLE_THRESHOLD; i++) {
//                mListCurrentArticles.add(mListArticles.get(i));
//                currentItemCount = VISIBLE_THRESHOLD;
//            }
//        }
//        notifyDataSetChanged();
//    }
//
//    public void loadMoreNews() {
//        if (mListArticles.size() <= VISIBLE_THRESHOLD) {
//            this.flagFinishLoadData = true;
//            return;
//        }
//        if (mListArticles.size() <= currentItemCount + VISIBLE_THRESHOLD) {
//            //Ví dụ: mListNews.size() = 5, currentItemCount = 3, VISIBLETHRESHOLD = 3
//            for (int i = currentItemCount; i < mListArticles.size(); i++) {
//                mListCurrentArticles.add(mListArticles.get(i));
//            }
//            currentItemCount = mListArticles.size();
//            this.flagFinishLoadData = true;
//        } else {
//            Log.e("currentItemCount-XXX", currentItemCount + "");
//            Log.e("mListArticles.size()", mListArticles.size() + "");
//            for (int i = currentItemCount; i < currentItemCount + VISIBLE_THRESHOLD; i++) {
//                Log.e("iiii-xxxx", i + "");
//                mListCurrentArticles.add(mListArticles.get(i));
//            }
//            currentItemCount = currentItemCount + VISIBLE_THRESHOLD;
//            this.flagFinishLoadData = false;
//        }
//        notifyDataSetChanged();
//        this.setLoaded();
//    }
//
//    public void setListArticles(List<Article> listArticles) {
//        mListArticles = listArticles;
//    }
//
//    public void setFlagFinishLoadData(boolean flagFinishLoadData) {
//        this.flagFinishLoadData = flagFinishLoadData;
//    }
//}
