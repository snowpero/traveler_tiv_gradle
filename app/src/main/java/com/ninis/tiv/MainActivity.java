package com.ninis.tiv;

import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.ninis.tiv.data.GeoDataManager;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private final int MODE_WEBVIEW = 0;
    private final int MODE_MAPVIEW = 1;

    private int mCurrentMode = MODE_WEBVIEW;

    private long mLastBackPressTime = 0l;

    private Toolbar mToolbar;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private LinearLayout mBottomNaviMenu;

    private ViewPagerAdapter mViewPagerAdapter;

    private WebviewFragment mWebviewFragment;
    private MapviewFragment mMapviewFragment;

    private Animation mAniSlideUp;
    private Animation mAniSlideDown;

    private WebviewFragment.OnPageFinishedCallback mOnPageFinishedCallback;
    private GeoDataManager.OnGeoDataLoadComplete mOnGeoDataLoadComplete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GeoDataManager.getInstance().getGeoData();

        mToolbar = (Toolbar) findViewById(R.id.toolBar);
        setSupportActionBar(mToolbar);

        mViewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.viewPager);
        if( mViewPager != null ) {
            initViewPager();
        }

        mTabLayout = (TabLayout) findViewById(R.id.tabLayout);
        if( mTabLayout != null ) {
            initTabLayout();
        }

        mBottomNaviMenu = (LinearLayout) findViewById(R.id.ll_bottom_navi_menu);
        initBottomNaviMenu();

        mAniSlideDown = AnimationUtils.loadAnimation(this, R.anim.slide_out_bottom);
        mAniSlideUp = AnimationUtils.loadAnimation(this, R.anim.slide_in_bottom);

        showBottomNaviMenu();

        mOnGeoDataLoadComplete = new GeoDataManager.OnGeoDataLoadComplete() {
            @Override
            public void onLoadComplete() {
                if( mMapviewFragment != null ) {
                    mMapviewFragment.setUpMap();
                }
            }
        };
        GeoDataManager.getInstance().setOnGeoDataLoadComplete(mOnGeoDataLoadComplete);
    }

    private void initViewPager() {
        mWebviewFragment = new WebviewFragment();
        mViewPagerAdapter.addFragment(mWebviewFragment, "");
        mMapviewFragment = new MapviewFragment();
        mViewPagerAdapter.addFragment(mMapviewFragment, "");

        mViewPager.setAdapter(mViewPagerAdapter);

        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    showBottomNaviMenu();
                    mCurrentMode = MODE_WEBVIEW;
                } else {
                    hideBottomNaviMenu();
                    mCurrentMode = MODE_MAPVIEW;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        initCallback();
    }

    private void initTabLayout() {
        mTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.setTabTextColors(Color.GRAY, Color.DKGRAY);

        for( int i = 0; i < mTabLayout.getTabCount(); ++i ) {
            TabLayout.Tab tabItem = mTabLayout.getTabAt(i);
            if( i == 0 ) {
                tabItem.setIcon(R.mipmap.calendar_text);
            } else {
                tabItem.setIcon(R.mipmap.google_maps);
            }
        }
    }

    private void initBottomNaviMenu() {
        if( mBottomNaviMenu != null ) {
            findViewById(R.id.iv_bottom_btn_home).setOnClickListener(this);
            findViewById(R.id.iv_bottom_btn_left).setOnClickListener(this);
            findViewById(R.id.iv_bottom_btn_reload).setOnClickListener(this);
            findViewById(R.id.iv_bottom_btn_right).setOnClickListener(this);
        }
    }

    private void showBottomNaviMenu() {
        if( mBottomNaviMenu != null ) {
            mBottomNaviMenu.setVisibility(View.VISIBLE);
            mBottomNaviMenu.startAnimation(mAniSlideUp);
        }
    }

    private void hideBottomNaviMenu() {
        if( mBottomNaviMenu != null ) {
            mBottomNaviMenu.startAnimation(mAniSlideDown);
            mBottomNaviMenu.setVisibility(View.GONE);
        }
    }

    private void initCallback() {
        mOnPageFinishedCallback = new WebviewFragment.OnPageFinishedCallback() {
            @Override
            public void onPageFinished(WebView view, String url) {

            }
        };

        if( mWebviewFragment != null )
            mWebviewFragment.setOnPageFinishedCallback(mOnPageFinishedCallback);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.iv_bottom_btn_home:
                mWebviewFragment.loadHomeUrl();
                break;
            case R.id.iv_bottom_btn_left:
                mWebviewFragment.goBackPage();
                break;
            case R.id.iv_bottom_btn_right:
                mWebviewFragment.goForwardPage();
                break;
            case R.id.iv_bottom_btn_reload:
                mWebviewFragment.refreshPage();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (!mWebviewFragment.canGoBackPage()) {
            finishActivity();
        } else {
            mWebviewFragment.goBackPage();
        }
    }

    protected void finishActivity() {
        long lNow = System.currentTimeMillis();

        if (lNow - mLastBackPressTime < 1500) {
            finish();
            return;
        }
        mLastBackPressTime = lNow;
        Toast.makeText(MainActivity.this,
                getString(R.string.message_exit_check), Toast.LENGTH_SHORT)
                .show();
    }
}
