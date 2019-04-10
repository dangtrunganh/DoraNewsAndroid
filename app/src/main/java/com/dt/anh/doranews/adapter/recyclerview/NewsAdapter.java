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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.dt.anh.doranews.DetailEventActivity;
import com.dt.anh.doranews.DetailNewsActivity;
import com.dt.anh.doranews.R;
import com.dt.anh.doranews.adapter.fragmentpager.DetailNewsAdapter;
import com.dt.anh.doranews.model.Event;
import com.dt.anh.doranews.model.result.eventdetailresult.Article;
import com.dt.anh.doranews.util.ConstParamTransfer;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {
    private List<Article> mListNews;
    private LayoutInflater mLayoutInflater;
    private Context mContext;
    private String titleEvent;
    private List<Article> mCurrentListNews;
    private TextView textLoadMore;
    //    boolean isLoading;
//    ILoadMore loadMore;
    private static final int VISIBLETHRESHOLD = 2;
    int lastVisibleItem, totalItemCount;
    int currentItemCount = 0;

    public NewsAdapter(List<Article> mListNews, Context mContext, TextView textViewLoadMore) {
        this.mListNews = mListNews;
        this.mContext = mContext;
        mCurrentListNews = new ArrayList<>();
        textLoadMore = textViewLoadMore;
    }

    @NonNull
    @Override
    public NewsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (mLayoutInflater == null) {
            mLayoutInflater = LayoutInflater.from(parent.getContext());
        }
        View view = mLayoutInflater.inflate(R.layout.item_news_all_main_2, parent, false);
        return new NewsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsAdapter.ViewHolder holder, int position) {
        holder.bindData(mCurrentListNews.get(position));
    }

    @Override
    public int getItemCount() {
        if (mCurrentListNews == null) {
            return 0;
        }
        return mCurrentListNews.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView mImageCoverNews;
        private TextView mTextTitleNews;
        private TextView mTextTime;
        private TextView mtextSource;
        private TextView mTextSummary;

        public ViewHolder(View itemView) {
            super(itemView);
            mImageCoverNews = itemView.findViewById(R.id.image_cover_news_all);
            mTextTitleNews = itemView.findViewById(R.id.text_title_news_all);
            mTextTime = itemView.findViewById(R.id.text_time_news_all);
            mtextSource = itemView.findViewById(R.id.text_source_news_all);
            mTextSummary = itemView.findViewById(R.id.text_summary_news_all);


            itemView.setOnClickListener(this);
        }

        @SuppressLint("SetTextI18n")
        public void bindData(Article news) {
            if (news == null) {
                return;
            }
            if (news.getImage() != null) {
                Log.d("News: ", news.getImage());
                Glide.with(itemView.getContext()).load(news.getImage()).
                        apply(new RequestOptions().override(400, 0).
                                placeholder(R.drawable.image_default).error(R.drawable.image_default))
                        .into(mImageCoverNews);
            }
            mTextTitleNews.setText(news.getTitle());
            mtextSource.setText(news.getSource().getName());
            mTextTime.setText(news.getReadableTime());

            String summarization = "";
            boolean flag = true;
            for (int i = 0; i < news.getMedias().size(); i++) {
                if (news.getMedias().get(i).getType().equals(ConstParamTransfer.MEDIUM)) {
                    summarization = news.getMedias().get(i).getBody().get(0).getContent();
                    mTextSummary.setText(summarization);
                    flag = false;
                    break;
                }
            }

            if (flag) {
                mTextSummary.setText("\n\n\n\n");
            }
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
//            Article article = mListNews.get(position);
            Gson gson = new Gson();
            String json = gson.toJson(mListNews);
            Intent intent = new Intent(mContext, DetailNewsActivity.class);
            intent.putExtra(ConstParamTransfer.TITLE_EVENT, titleEvent);
            intent.putExtra(ConstParamTransfer.DETAIL_NEWS, json);
            intent.putExtra(NewsInCategoryFrgAdapter.POSITION_CLICK, position);

            mContext.startActivity(intent);
        }
    }

    /**
     * @return List of songs
     */
    public List<Article> getListNews() {
        return mCurrentListNews;
    }

    /**
     * Update adapter when list events is changed
     *
     * @param listNews List songs after being changed
     */
    public void updateListEvents(List<Article> listNews) {
        //Thực tế hàm này chỉ được gọi đúng 1 lần
        //Biến currentItemCount chỉ số lượng Item hiện có trong current list
        mListNews = listNews;
        if (listNews == null) {
            Toast.makeText(mContext, "Error - listNews NULL?", Toast.LENGTH_SHORT).show();
            return;
        }
        if (listNews.size() <= VISIBLETHRESHOLD) {
            mCurrentListNews.addAll(listNews);
            currentItemCount = listNews.size();
//            textLoadMore.setVisibility(View.GONE);
            textLoadMore.setVisibility(View.INVISIBLE);
            textLoadMore.setText("");
        } else {
            for (int i = 0; i < VISIBLETHRESHOLD; i++) {
                mCurrentListNews.add(listNews.get(i));
                currentItemCount = VISIBLETHRESHOLD;
            }
            textLoadMore.setVisibility(View.VISIBLE);
        }
        notifyDataSetChanged();
    }

    public void setTitleEvent(String titleEvent) {
        this.titleEvent = titleEvent;
    }

    public void loadMoreNews() {
        if (mListNews.size() <= VISIBLETHRESHOLD) {
            return;
        }
        if (mListNews.size() <= currentItemCount + VISIBLETHRESHOLD) {
            //Ví dụ: mListNews.size() = 5, currentItemCount = 3, VISIBLETHRESHOLD = 3
            for (int i = currentItemCount; i < mListNews.size(); i++) {
                mCurrentListNews.add(mListNews.get(i));
            }
            currentItemCount = mListNews.size();
//            textLoadMore.setVisibility(View.GONE);
            textLoadMore.setVisibility(View.INVISIBLE);
            textLoadMore.setText("");
        } else {
            Log.e("currentItemCount-XXX", currentItemCount + "");
            Log.e("mListNews.size()-XXX", mListNews.size() + "");
            for (int i = currentItemCount; i < currentItemCount + VISIBLETHRESHOLD; i++) {
                Log.e("iiii-xxxx", i + "");
                mCurrentListNews.add(mListNews.get(i));
            }
            currentItemCount = currentItemCount + VISIBLETHRESHOLD;
        }
        notifyDataSetChanged();
    }
}
