package com.dt.anh.doranews.adapter.recyclerview;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.dt.anh.doranews.DetailNewsActivity;
import com.dt.anh.doranews.R;
import com.dt.anh.doranews.model.result.eventdetailresult.Article;
import com.dt.anh.doranews.util.ConstParamAPI;
import com.dt.anh.doranews.util.ConstParamTransfer;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class NewsAllAdapter2 extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Article> mListArticles;
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private String titleCategory;
    private String slugCategory;

    private final int VIEW_TYPE_ITEM = 0, VIEW_TYPE_LOADING = 1;
    ILoadMore loadMore;
    boolean isLoading;
    Activity mActivity;
//    public static int VISIBLE_THRESHOLD = 10;
    int lastVisibleItem, totalItemCount;
    boolean flagLoadContinue = false;

    public void setTitleCategory(String titleCategory) {
        this.titleCategory = titleCategory;
    }

    public void resetListArticle() {
        this.mListArticles = new ArrayList<>();
        notifyDataSetChanged();
    }

    public void setSlugCategory(String slugCategory) {
        this.slugCategory = slugCategory;
    }

    public NewsAllAdapter2(List<Article> listArticles, Context context, String category, String mSlugCategory, RecyclerView recyclerView) {
        mListArticles = listArticles;
        mContext = context;
        titleCategory = category;
        slugCategory = mSlugCategory;

        final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

        if (!flagLoadContinue) {
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                }

                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//                    Toast.makeText(mContext, "dxdx: " + dx + ", dydy=" + dy + "", Toast.LENGTH_SHORT).show();
//                    Log.e("dxdx: ", dx + ", dydy=" + dy + "");
                    if (dy > 0) {
                        totalItemCount = linearLayoutManager.getItemCount();
                        lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                        if (!isLoading && totalItemCount <= (lastVisibleItem + ConstParamAPI.ARTICLE_THRESHOLD)) {
                            if (loadMore != null)
                                loadMore.onLoadMore();
                            isLoading = true;
                        }
                    }
                }
            });
//            nestedScrollView.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
//                if (v.getChildAt(v.getChildCount() - 1) != null) {
//                    if ((scrollY >= (v.getChildAt(v.getChildCount() - 1).getMeasuredHeight() - v.getMeasuredHeight())) &&
//                            scrollY > oldScrollY) {
//                        //code to fetch more data for endless scrolling
//                        if (scrollY > 0) {
//                            totalItemCount = linearLayoutManager.getItemCount();
//                            lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
//                            if (!isLoading && totalItemCount <= (lastVisibleItem + VISIBLE_THRESHOLD)) {
//                                if (loadMore != null)
//                                    loadMore.onLoadMore();
//                                isLoading = true;
//                            }
//                        }
//                    }
//                }
//            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        return mListArticles.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
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
//        View view = mLayoutInflater.inflate(R.layout.item_news_all_main_2, parent, false);
//        return new NewsAllAdapter2.ViewHolder(view);


        if (viewType == VIEW_TYPE_ITEM) {
            View view = mLayoutInflater.inflate(R.layout.item_news_all_main_2, parent, false);
            return new NewsAllAdapter2.ViewHolder(view);
        } else if (viewType == VIEW_TYPE_LOADING) {
            View view = mLayoutInflater
                    .inflate(R.layout.item_loading, parent, false);
            return new NewsAllAdapter2.LoadingViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof NewsAllAdapter2.ViewHolder) {
            Article article = mListArticles.get(position);
            NewsAllAdapter2.ViewHolder viewHolder = (NewsAllAdapter2.ViewHolder) holder;
            viewHolder.bindData(article);
        } else if (holder instanceof NewsAllAdapter2.LoadingViewHolder) {
            if (!flagLoadContinue) {
                return;
            }
            NewsAllAdapter2.LoadingViewHolder loadingViewHolder = (NewsAllAdapter2.LoadingViewHolder) holder;
            loadingViewHolder.progressBar.setIndeterminate(true);
        }
//        holder.bindData(mListCategories.get(position));
    }

    @Override
    public int getItemCount() {
        if (mListArticles == null) {
            return 0;
        }
        return mListArticles.size();
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
        private ImageView mImageCoverArticle;
        private TextView mTextSource;
        private TextView mTextTitle;
        private TextView mTextTimeReadable;
        private TextView mTextSummary;

        public ViewHolder(View itemView) {
            super(itemView);
            mImageCoverArticle = itemView.findViewById(R.id.image_cover_news_all);
            mTextSource = itemView.findViewById(R.id.text_source_news_all);
            mTextTitle = itemView.findViewById(R.id.text_title_news_all);
            mTextTimeReadable = itemView.findViewById(R.id.text_time_news_all);
            mTextSummary = itemView.findViewById(R.id.text_summary_news_all);

            itemView.setOnClickListener(this);
        }

        @SuppressLint("SetTextI18n")
        public void bindData(Article article) {
            if (article == null) {
                return;
            }
            Log.d("article: ", article.getImage());
            Glide.with(itemView.getContext()).load(article.getImage()).
                    apply(new RequestOptions().override(400, 0).
                            placeholder(R.drawable.image_default).error(R.drawable.image_default))
                    .into(mImageCoverArticle);

            String source = /*"(Nguồn: " +*/ article.getSource().getName() /*+ ")"*/;
            mTextSource.setText(source);
            mTextTitle.setText(article.getTitle());
            mTextTimeReadable.setText(article.getReadableTime());
            String summarization = "";
            boolean flag = true;
            for (int i = 0; i < article.getMedias().size(); i++) {
                if (article.getMedias().get(i).getType().equals(ConstParamTransfer.MEDIUM)) {
                    summarization = article.getMedias().get(i).getBody().get(0).getContent();
                    mTextSummary.setText(summarization);
                    flag = false;
                    break;
                }
            }

            if (flag) {
                mTextSummary.setText("\n\n\n\n");
            }
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            Gson gson = new Gson();
            String json = gson.toJson(mListArticles);
            Intent intent = new Intent(mContext, DetailNewsActivity.class);
            intent.putExtra(ConstParamTransfer.TITLE_EVENT, titleCategory);
            intent.putExtra(ConstParamTransfer.DETAIL_NEWS, json);
            intent.putExtra(NewsInCategoryFrgAdapter.POSITION_CLICK, position);

            mContext.startActivity(intent);
        }
    }

    public void updateListArticles(List<Article> listArticle) {
//        mListArticles = listArticle;
//        //====
//        notifyDataSetChanged();
        //========
        if (mListArticles.size() < ConstParamAPI.ARTICLE_THRESHOLD) {
            this.flagLoadContinue = true;
        }
        boolean flag = true;

        for (Article datum : mListArticles) {
            for (Article datumCmp : listArticle) {
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
            mListArticles.addAll(listArticle);
            notifyDataSetChanged();
            this.setLoaded();
        }
    }

    public void setFlagLoadContinue(boolean flagLoadContinue) {
        this.flagLoadContinue = flagLoadContinue;
    }
}
