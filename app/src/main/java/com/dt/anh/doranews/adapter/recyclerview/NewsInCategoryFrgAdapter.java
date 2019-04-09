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
import android.widget.ImageView;
import android.widget.TextView;

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

public class NewsInCategoryFrgAdapter extends RecyclerView.Adapter<NewsInCategoryFrgAdapter.ViewHolder>{
    private List<Article> mListArticles;
    private List<Article> mListArticlesMain;
    private LayoutInflater mLayoutInflater;
    private Context mContext;
    private String articleName;
//    public static final int VISIBLETHRESHOLD = 4;
    public static final String POSITION_CLICK = "POSITION_CLICK";

    public NewsInCategoryFrgAdapter(List<Article> mListArticles, Context mContext, String articleName) {
        this.mListArticles = mListArticles;
        this.mContext = mContext;
        this.articleName = articleName;
        mListArticlesMain = new ArrayList<>();
    }

    public void setArticleName(String articleName) {
        this.articleName = articleName;
    }

    @NonNull
    @Override
    public NewsInCategoryFrgAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (mLayoutInflater == null) {
            mLayoutInflater = LayoutInflater.from(parent.getContext());
        }
        View view = mLayoutInflater.inflate(R.layout.item_news_in_frg_event, parent, false);
        return new NewsInCategoryFrgAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsInCategoryFrgAdapter.ViewHolder holder, int position) {
        holder.bindData(mListArticlesMain.get(position));
    }

    @Override
    public int getItemCount() {
        if (mListArticlesMain == null) {
            return 0;
        }
        return mListArticlesMain.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView mImageCoverNews;
        private TextView mTextTitle;
        private TextView mTextSource;
        private TextView mTime;

        public ViewHolder(View itemView) {
            super(itemView);
            mImageCoverNews = itemView.findViewById(R.id.iv_cover_news_item_news);
            mTextTitle = itemView.findViewById(R.id.tv_title_news_item_news);
            mTextSource = itemView.findViewById(R.id.tv_source_news_item_news);
            mTime = itemView.findViewById(R.id.tv_time_news_item_news);

            itemView.setOnClickListener(this);
        }

        @SuppressLint("SetTextI18n")
        public void bindData(Article article) {
            if (article == null) {
                return;
            }
            Log.d("Article-category frg: ", article.getImage());
            Glide.with(itemView.getContext()).load(article.getImage()).
                    apply(new RequestOptions().override(200, 0).
                            placeholder(R.drawable.image_default).error(R.drawable.image_default))
                    .into(mImageCoverNews);

            mTextTitle.setText(article.getTitle());
            mTextSource.setText(article.getSource().getName());
            mTime.setText(article.getReadableTime());
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();

            Gson gson = new Gson();
            String json = gson.toJson(mListArticles);
            Intent intent = new Intent(mContext, DetailNewsActivity.class);
            intent.putExtra(ConstParamTransfer.TITLE_EVENT, articleName);
            intent.putExtra(ConstParamTransfer.DETAIL_NEWS, json);
            intent.putExtra(NewsInCategoryFrgAdapter.POSITION_CLICK, position);

            mContext.startActivity(intent);
        }
    }

    /**
     * @return List of songs
     */
    public List<Article> getListArticles() {
        return mListArticles;
    }

    /**
     * Update adapter when list events is changed
     *
     * @param listArticles List songs after being changed
     */
    public void updateListArticles(List<Article> listArticles) {
        mListArticles = listArticles;
        //====
        //bỏ bớt articles
        mListArticlesMain.clear();
        if (mListArticles.size() <= ConstParamAPI.ARTICLE_THRESHOLD_CATEGORY_FRG) {
            mListArticlesMain.addAll(mListArticles);
        } else {
            for (int i = 0; i < ConstParamAPI.ARTICLE_THRESHOLD_CATEGORY_FRG; i++) {
                mListArticlesMain.add(mListArticles.get(i));
            }
        }
        notifyDataSetChanged();
    }
}
