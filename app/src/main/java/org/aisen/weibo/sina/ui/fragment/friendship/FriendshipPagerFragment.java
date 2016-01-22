package org.aisen.weibo.sina.ui.fragment.friendship;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.design.widget.TabLayout;

import org.aisen.android.common.utils.Utils;
import org.aisen.android.support.bean.TabItem;
import org.aisen.android.ui.activity.basic.BaseActivity;
import org.aisen.android.ui.activity.container.FragmentArgs;
import org.aisen.android.ui.fragment.ATabsTabLayoutFragment;
import org.aisen.weibo.sina.R;
import org.aisen.weibo.sina.base.AppContext;
import org.aisen.weibo.sina.sinasdk.bean.WeiBoUser;
import org.aisen.weibo.sina.support.utils.AisenUtils;
import org.aisen.weibo.sina.ui.activity.base.SinaCommonActivity;

import java.util.ArrayList;

/**
 * Created by wangdan on 16/1/22.
 */
public class FriendshipPagerFragment extends ATabsTabLayoutFragment<TabItem> {

    /**
     * 用户关系
     *
     * @param from
     * @param user
     * @param type 1:粉丝 0:关注 2:互粉
     */
    public static void launch(Activity from, WeiBoUser user, int type) {
        FragmentArgs args = new FragmentArgs();
        args.add("user", user);
        args.add(SET_INDEX, type);

        SinaCommonActivity.launch(from, FriendshipPagerFragment.class, args);
    }

    private WeiBoUser mUser;
    private int type;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mUser = savedInstanceState == null ? (WeiBoUser) getArguments().getSerializable("user")
                                           : (WeiBoUser) savedInstanceState.getSerializable("user");
        type = savedInstanceState == null ? getArguments().getInt("index", type)
                                          : savedInstanceState.getInt("index");

        BaseActivity activity = (BaseActivity) getActivity();
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        activity.getSupportActionBar().setTitle(AisenUtils.getUserScreenName(mUser));
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putSerializable("user", mUser);
        outState.putInt("index", type);
    }

    @Override
    protected void setupTabLayout(Bundle savedInstanceSate, TabLayout tabLayout) {
        super.setupTabLayout(savedInstanceSate, tabLayout);

        tabLayout.setPadding(Utils.dip2px(16), tabLayout.getPaddingTop(), tabLayout.getPaddingRight(), tabLayout.getPaddingBottom());
    }

    @Override
    protected ArrayList<TabItem> generateTabs() {
        ArrayList<TabItem> beans = new ArrayList<TabItem>();

        beans.add(new TabItem("300", String.format(getString(R.string.friends_format), mUser.getFriends_count())));
        beans.add(new TabItem("301", String.format(getString(R.string.followers_format), mUser.getFollowers_count())));
        // 是当前授权用户时，显示互粉
        if (mUser.getIdstr().equals(AppContext.getAccount().getUser().getIdstr()))
            beans.add(new TabItem("302", String.format(getString(R.string.bilateral_format), mUser.getBi_followers_count())));

        return beans;
    }

    @Override
    protected Fragment newFragment(TabItem bean) {
        switch (Integer.parseInt(bean.getType())) {
            case 300:
                return FriendsFragment.newInstance(mUser);
            case 301:
                return FollowersFragment.newInstance(mUser);
            case 302:
                return BilateralFragment.newInstance(mUser);
        }

        return null;
    }

}
