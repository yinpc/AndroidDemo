package com.example.yinp.gank;

import android.databinding.DataBindingUtil;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.example.yinp.gank.databinding.ActivityMainBinding;
import com.example.yinp.gank.databinding.NavHeaderMainBinding;
import com.example.yinp.gank.ui.book.BookFragment;
import com.example.yinp.gank.ui.gank.GankFragment;
import com.example.yinp.gank.ui.one.OneFragment;
import com.example.yinp.gank.view.statusbar.MyFragmentPagerAdapter;
import com.example.yinp.gank.view.statusbar.StatusBarUtil;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, ViewPager.OnPageChangeListener {

    private ActivityMainBinding mBinding;
    private DrawerLayout drawerLayout;
    private FrameLayout llTitleMenu;
    private NavigationView navView;
    private NavHeaderMainBinding navHeaderBind;
    private ViewPager mViewPager;
    private ImageView ivTitleOne;
    private ImageView ivTitleGank;
    private ImageView ivTitleDou;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        initId();

        StatusBarUtil.setColorNoTranslucentForDrawerLayout(MainActivity.this, drawerLayout,
                getResources().getColor(R.color.colorTheme));

        initDrawerLayout();
        initListener();
        initContentFragment();
    }

    private void initContentFragment() {
        ArrayList<Fragment> mFragmentList = new ArrayList<>();
        mFragmentList.add(new OneFragment());
        mFragmentList.add(new GankFragment());
        mFragmentList.add(new BookFragment());
        MyFragmentPagerAdapter adapter = new MyFragmentPagerAdapter(getSupportFragmentManager(), mFragmentList);
        mViewPager.setAdapter(adapter);
//        mViewPager.setOffscreenPageLimit(2);
        mViewPager.addOnPageChangeListener(this);
//        ivTitleGank.setSelected(true);
//        mViewPager.setCurrentItem(0);
//        setSupportActionBar(toolbar);
        ivTitleGank.setSelected(true);
        mViewPager.setCurrentItem(1);
    }

    private void initId() {
        drawerLayout = mBinding.drawerLayout;
        llTitleMenu = mBinding.include.llTitleMenu;
        navView = mBinding.navView;
        mViewPager = mBinding.include.vpContent;
        ivTitleOne = mBinding.include.ivTitleOne;
        ivTitleGank = mBinding.include.ivTitleGank;
        ivTitleDou = mBinding.include.ivTitleDou;
        toolbar = mBinding.include.toolbar;
    }

    private void initDrawerLayout() {
        View headerView = navView.getHeaderView(0);
        navHeaderBind = DataBindingUtil.bind(headerView);
        navHeaderBind.llNavExit.setOnClickListener(this);
    }

    private void initListener() {
        llTitleMenu.setOnClickListener(this);
        ivTitleOne.setOnClickListener(this);
        ivTitleGank.setOnClickListener(this);
        ivTitleDou.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_title_menu:
                drawerLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.iv_title_one:
                if (mViewPager.getCurrentItem() != 0) {//不然cpu会有损耗
                    ivTitleOne.setSelected(true);
                    ivTitleGank.setSelected(false);
                    ivTitleDou.setSelected(false);
                    mViewPager.setCurrentItem(0);
                }
                break;
            case R.id.iv_title_gank:
                if (mViewPager.getCurrentItem() != 1) {//不然cpu会有损耗
                    ivTitleOne.setSelected(false);
                    ivTitleGank.setSelected(true);
                    ivTitleDou.setSelected(false);
                    mViewPager.setCurrentItem(1);
                }
                break;
            case R.id.iv_title_dou:
                if (mViewPager.getCurrentItem() != 2) {//不然cpu会有损耗
                    ivTitleOne.setSelected(false);
                    ivTitleGank.setSelected(false);
                    ivTitleDou.setSelected(true);
                    mViewPager.setCurrentItem(2);
                }
                break;
            case R.id.ll_nav_exit:
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        switch (position) {
            case 0:
                ivTitleOne.setSelected(true);
                ivTitleGank.setSelected(false);
                ivTitleDou.setSelected(false);
                break;
            case 1:
                ivTitleOne.setSelected(false);
                ivTitleGank.setSelected(true);
                ivTitleDou.setSelected(false);
                break;
            case 2:
                ivTitleOne.setSelected(false);
                ivTitleGank.setSelected(false);
                ivTitleDou.setSelected(true);
                break;
            default:
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
