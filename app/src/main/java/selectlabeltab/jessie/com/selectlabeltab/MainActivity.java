package selectlabeltab.jessie.com.selectlabeltab;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static selectlabeltab.jessie.com.selectlabeltab.TabManageActivity.MY_CHANNEL;
import static selectlabeltab.jessie.com.selectlabeltab.TabManageActivity.OTHER_CHANNEL;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.view_pager)
    ViewPager viewPager;
    @BindView(R.id.tablayout)
    TabLayout tabLayout;

    private List<String> mDataList = new ArrayList<>();
    private ExamplePagerAdapter mExamplePagerAdapter = new ExamplePagerAdapter(mDataList);
    @BindView(R.id.icon_category)
    ImageView icon_category;


    private int myChannelNum = 10;//初始化当前选择的数量
    private int otherChannelNum = 10;//初始化未选择的数量
    private ArrayList<MainTitleDTO> items;
    private ArrayList<MainTitleDTO> otherItems;
    public static final int REQUEST_CODE = 0;
    public static final String GOTO_FRAGMENT = "GOTO_FRAGMENT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);//ButterKnife初始化


        items = new ArrayList<>();
        for (int i = 0; i < myChannelNum; i++) {
            MainTitleDTO entity = new MainTitleDTO();
            entity.setId(i);
            entity.setTitle("分页标题" + (i + 1));
            items.add(entity);
        }
        otherItems = new ArrayList<>();
        for (int i = myChannelNum; i < myChannelNum + otherChannelNum; i++) {
            MainTitleDTO entity = new MainTitleDTO();
            entity.setId(i);
            entity.setTitle("分页标题" + (i + 1));
            otherItems.add(entity);
        }
        tabLayout.setupWithViewPager(viewPager);
        setTabTitle();
        viewPager.setAdapter(mExamplePagerAdapter);
        icon_category.setVisibility(View.VISIBLE);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }


    private void setTabTitle() {
        mDataList.clear();
        Iterator<MainTitleDTO> it = items.iterator();
        while (it.hasNext()) {
            MainTitleDTO mainTitleDTO = it.next();
            mDataList.add(mainTitleDTO.getTitle());
            tabLayout.addTab(tabLayout.newTab().setText(mainTitleDTO.getTitle()));
        }
    }

    public void gotoFragment(int index) {
        viewPager.setCurrentItem(index);
    }

    @OnClick({R.id.icon_category})
    public void onEventClick(View view) {
        switch (view.getId()) {
            case R.id.icon_category:
                Intent intent = new Intent(this, TabManageActivity.class);
                intent.putExtra(MY_CHANNEL, items);
                intent.putExtra(OTHER_CHANNEL, otherItems);
                startActivityForResult(intent, REQUEST_CODE);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            if (resultCode == TabManageActivity.RESULT_CODE) {
                if (data.hasExtra(MY_CHANNEL) && data.hasExtra(OTHER_CHANNEL)) {

                    if (data.hasExtra(MY_CHANNEL)) {
                        items.clear();
                        ArrayList myChannels = (ArrayList) data.getSerializableExtra(MY_CHANNEL);
                        items.addAll(myChannels);
                    }
                    if (data.hasExtra(OTHER_CHANNEL)) {
                        otherItems.clear();
                        ArrayList otherChannels = (ArrayList) data.getSerializableExtra(OTHER_CHANNEL);
                        otherItems.addAll(otherChannels);
                    }
                    setTabTitle();
                    mExamplePagerAdapter.notifyDataSetChanged();
                }
                if (data.hasExtra(GOTO_FRAGMENT)) {
                    int index = data.getIntExtra(GOTO_FRAGMENT, 0);
                    gotoFragment(index);
                }

            }
        }
    }
}
