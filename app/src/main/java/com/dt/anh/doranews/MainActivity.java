package com.dt.anh.doranews;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dt.anh.doranews.adapter.viewpager.CategoryViewPagerAdapter;
import com.dt.anh.doranews.fragment.CategoryFragment;
import com.dt.anh.doranews.model.Category;
import com.dt.anh.doranews.model.result.eventdetailresult.Article;
import com.dt.anh.doranews.notification.NotificationAction;
import com.dt.anh.doranews.service.ArticlePlayerService;
import com.dt.anh.doranews.service.StateLevel;
import com.dt.anh.doranews.service.servicefirebase.FireBaseInstanceIDService;
import com.dt.anh.doranews.util.ConstParamStorageLocal;
import com.dt.anh.doranews.util.ConstParamTransfer;
import com.dt.anh.doranews.util.UtilTools;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener/*, ArticlePlayerService.OnListenerActivity*/, View.OnClickListener {

    private ArrayList<Category> mCategoryArrayList;

    private ImageView mImagePlaySmall;
    private ImageView mImageNextSmall;
    private ImageView mImagePreviousSmall;
    private TextView mTextTitleArticle;
    private LinearLayout mSmallControlView;

    private int mProgress;
    public ArticlePlayerService mPlayerService;
    private ServiceConnection mConnection;
    private boolean mIsConnect;
    private boolean mIsOnline;
    private boolean mIsForegroundRunning;

    public static final String TAG = "XXXXX";

    protected void registerListeners() {
        mImagePlaySmall.setOnClickListener(this);
        mImageNextSmall.setOnClickListener(this);
        mImagePreviousSmall.setOnClickListener(this);
        mSmallControlView.setOnClickListener(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        initViewController();
        registerListeners();
//        addSoundCloudFragment();
//        boundService();
        getIntentFromNotification();
//        fireBaseSetUp();
//        tokenFireBaseServices();

        //====GetIntent từ EmptyActivity để load sang màn hình DetailEventAcitivty (nếu có)=====
        getIntentFromEmptyAct();
    }

    private void getIntentFromEmptyAct() {
        Intent result = getIntent();
        if (result == null) {
            return;
        }
        String idEvent = result.getStringExtra(ConstParamTransfer.TRANSFER_EVENT_ID_FR_EMPTY_TO_MAIN);
        String idLongEvent = result.getStringExtra(ConstParamTransfer.TRANSFER_LONG_EVENT_ID_FR_EMPTY_TO_MAIN);
        if (idEvent == null || idLongEvent == null) {
            return;
        }
        if (idEvent.equals("") || idLongEvent.equals("")) {
            return;
        }
        Intent intentToDetailEventAct = new Intent(MainActivity.this, DetailEventActivity.class);
        intentToDetailEventAct.putExtra(ConstParamTransfer.PARAM_ID_EVENT, idEvent);
        intentToDetailEventAct.putExtra(ConstParamTransfer.PARAM_ID_LONG_EVENT, idLongEvent);
        startActivity(intentToDetailEventAct);
    }

    //    private void addSoundCloudFragment() {
//
//    }
    private void tokenFireBaseServices() {
        FireBaseInstanceIDService fireBaseInstanceIDService = new FireBaseInstanceIDService();
        String mToken = fireBaseInstanceIDService.getToken();
        Log.d(TAG, "Token FireBase: " + mToken);
    }

    private void fireBaseSetUp() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            // Create channel to show notifications.
//            String channelId = getString(R.string.default_notification_channel_id);
//            String channelName = getString(R.string.default_notification_channel_name);
//            NotificationManager notificationManager =
//                    getSystemService(NotificationManager.class);
//            notificationManager.createNotificationChannel(new NotificationChannel(channelId,
//                    channelName, NotificationManager.IMPORTANCE_LOW));
//        }


        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.w(TAG, "getInstanceId failed", task.getException());
                        return;
                    }

                    // Get new Instance ID token
                    String token = task.getResult().getToken();

                    // Log and toast
//                        String msg = getString(R.string.msg_token_fmt, token);
                    String msg = token;
                    Log.d("Mess-XXX", msg);
//                    Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show(); //mainn
                });
    }

//    private void boundService() {
//        mConnection = new ServiceConnection() {
//            @Override
//            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
//                if (iBinder instanceof ArticlePlayerService.ArticleBinder) {
//                    mIsConnect = true;
//                    mPlayerService = ((ArticlePlayerService.ArticleBinder) iBinder).getService();
//                    mPlayerService.setListenerActivity(MainActivity.this);
//                } else {
//                    mIsConnect = false;
//                }
//            }
//
//            @Override
//            public void onServiceDisconnected(ComponentName componentName) {
//                mIsConnect = false;
//            }
//        };
//
//        Intent intent = new Intent(this, ArticlePlayerService.class);
//        bindService(intent, mConnection, Service.BIND_AUTO_CREATE);
//    }

    private void stopForegroundServiceArticle() {
        Intent intent = new Intent(this, ArticlePlayerService.class);
        intent.setAction(NotificationAction.STOP_FOREGROUND_ACTION);
        startService(intent);
    }

    private void initViewController() {
        mSmallControlView = findViewById(R.id.constraint_control_music);
        mImagePlaySmall = findViewById(R.id.image_play_control_music);
        mImageNextSmall = findViewById(R.id.image_next_control_music);
        mImagePreviousSmall = findViewById(R.id.image_previous_control_music);
        mTextTitleArticle = findViewById(R.id.text_title_control_music);
    }

    private ArrayList<Category> getListCategoriesChosen() {
        ArrayList<Category> categories = new ArrayList<>();
        SharedPreferences pre = getSharedPreferences
                (ConstParamStorageLocal.FILE_NAME_PREF_LIST_CATEGORY, MODE_PRIVATE);
        String json = pre.getString(ConstParamStorageLocal.KEY_PREF_LIST_CATEGORY,
                ConstParamStorageLocal.DEFAULT_VALUE_PREF_LIST_CATEGORY_DEFAULT);
        if (json.equals(ConstParamStorageLocal.DEFAULT_VALUE_PREF_LIST_CATEGORY_DEFAULT)) {
            Toast.makeText(this, "Nothing in pref", Toast.LENGTH_SHORT).show();
            return categories;
        }
        Gson gson = new Gson();
        categories = gson.fromJson(json, new TypeToken<List<Category>>() {
        }.getType());
        return categories;
    }

    private void initViews() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setElevation(0);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ViewPager viewPager = findViewById(R.id.view_pager_genre);
        TabLayout tabLayout = findViewById(R.id.tab_layout_genre);
        setUpViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);

        toolbar.setOnClickListener(view -> {
            startActivity(new Intent(this, SearchActivity.class));
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            UtilTools.clearCacheCategory(MainActivity.this);
            UtilTools.clearCacheUId(MainActivity.this);
            Toast.makeText(this, "Clear cache!", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_my_category) {
            startActivity(new Intent(MainActivity.this, PickCategoryActivity.class));
            finish();
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void setUpViewPager(ViewPager viewPager) {
//        Gson gson = new Gson();
        CategoryViewPagerAdapter adapter = new CategoryViewPagerAdapter(getSupportFragmentManager());


//        mCategoryArrayList = CategoriesFake.getListCategory();
        mCategoryArrayList = getListCategoriesChosen();
        ArrayList<Fragment> fragments = new ArrayList<>();


        //Add trước tiên một category hot
        Fragment fragment = CategoryFragment.newInstance("", "", true);
        fragments.add(fragment);

        //add list category bình thường

        for (int i = 0; i < mCategoryArrayList.size(); i++) {
            Category category = mCategoryArrayList.get(i);
//            String jsoncategory = gson.toJson(category);
            Fragment fragmentt = CategoryFragment.newInstance(category.getSlug(), category.getNameCategory(), false);
            fragments.add(fragmentt);
        }
        adapter.set_fragments(fragments);
        mCategoryArrayList.add(0, new Category("", "Hot-event", "Hot event", ""));

        adapter.set_categories(mCategoryArrayList);

        viewPager.setAdapter(adapter);
    }

//    @Override
//    public void updateArticle(Article article) {
//
//    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
//            case R.id.image_back:
//                setVisibleController(true);
//            case R.id.image_download:
//                break;
            case R.id.image_play_control_music:
//                changeStateFromController();
                break;
            case R.id.image_next_control_music:
//                nextFromController();
//                updateUI();
                break;
            case R.id.image_previous_control_music:
//                previousFromController();
//                updateUI();
                break;
            case R.id.constraint_control_music:
//                showDetailArticle();
                break;
            default:
                break;
        }
    }

    //====
//    private void previousFromController() {
//        if (!mIsConnect) {
//            return;
//        }
//        mPlayerService.previousArticle();
//    }
//
//    private void nextFromController() {
//        if (!mIsConnect) {
//            return;
//        }
//        mPlayerService.nextArticle();
//    }

//    private void changeStateFromController() {
//        if (!mIsConnect) {
//            return;
//        }
//        mPlayerService.playArticle();
//        if (mPlayerService.isOnlyPlaying()) {
//            mImagePlaySmall.setImageLevel(StateLevel.PAUSE);
//            return;
//        }
//        mImagePlaySmall.setImageLevel(StateLevel.PLAY);
//    }
//
//    private int getLevelImagePlay() {
//        if (mPlayerService.isOnlyPlaying()) {
//            return StateLevel.PAUSE;
//        } else {
//            return StateLevel.PLAY;
//        }
//    }

//
//    public void updateUI() {
//        this.runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                mImagePlaySmall.setImageLevel(getLevelImagePlay());
//                mTextTitleArticle
//                        .setText(getCurrentArticle().getTitle());
//            }
//        });
//    }
//
//    public Article getCurrentArticle() {
//        return mPlayerService.getCurrentArticle();
//    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
//        unbindService(mConnection);
//        mIsConnect = false;
        super.onDestroy();
    }

//    public void playArticleWhenClickItem(int position, List<Article> songs) {
//        setIndexCurrentArticle(position);
//        setCurrentArticles(songs);
//        playArticle(position);
//        setVisibleController(true);
//        updateUI();
//        startMusicForegroundService();
//    }

    //====

    public void getIntentFromNotification() {
        Intent intent = getIntent();
        if (intent == null) {
            return;
        }
        if (intent.getAction() == null) {
            return;
        }
        if (intent.getAction().equals(NotificationAction.MAIN_ACTION)) {
            showDetailArticle();
            setVisibleController(true);
        }
    }

    private void showDetailArticle() {
        Intent intent = new Intent(this, DetailNewsActivity.class);
        startActivity(intent);
    }

//    public void setIndexCurrentArticle(int position) {
//        if (!mIsConnect) {
//            mPlayerService.setIndexArticleCurrent(0);
//        }
//        mPlayerService.setIndexArticleCurrent(position);
//    }
//
//    public void setCurrentArticles(List<Article> articles) {
//        mPlayerService.setArticlesList(articles);
//    }

//    public void playArticle(int position) {
//        mPlayerService.playArticle(position);
//    }

    public void setVisibleController(boolean isVisible) {
        if (isVisible) {
            mSmallControlView.setVisibility(View.VISIBLE);
            return;
        }
        mSmallControlView.setVisibility(View.GONE);
    }

    public void startMusicForegroundService() {
        if (mIsForegroundRunning) {
            return;
        }
        Intent startIntent = new Intent(this, ArticlePlayerService.class);
        startIntent.setAction(NotificationAction.START_FOREGROUND_ACTION);
        startService(startIntent);
        mIsForegroundRunning = true;
    }
}
