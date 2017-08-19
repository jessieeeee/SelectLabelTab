package selectlabeltab.jessie.com.selectlabeltab;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import selectlabeltab.jessie.com.libtabmanage.BaseTabListAdapter;
import selectlabeltab.jessie.com.libtabmanage.ItemDragHelperCallback;

//tab管理界面
public class TabManageActivity extends Activity {
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;

    private ArrayList<MainTitleDTO> otherTabs = new ArrayList<>(); //未选分页
    private ArrayList<MainTitleDTO> myTabs = new ArrayList<>();//已选择分页
    private ArrayList<MainTitleDTO> lastTabs =new ArrayList<>(); //从主页读取到的分页
    public static final String MY_CHANNEL = "MY_TABS";  //传递当前分页
    public static final String OTHER_CHANNEL = "OTHER_TABS"; //其余的分页
    public static final int RESULT_CODE = 1;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_manage);
        if (getIntent().hasExtra(MY_CHANNEL)) {
            ArrayList curTabs = (ArrayList) getIntent().getSerializableExtra(MY_CHANNEL);

            this.lastTabs.addAll(curTabs);
            this.myTabs.addAll(curTabs);
        }
        if (getIntent().hasExtra(OTHER_CHANNEL)) {
            ArrayList otherChannels = (ArrayList) getIntent().getSerializableExtra(OTHER_CHANNEL);

            this.otherTabs.addAll(otherChannels);
        }
        init();

    }


    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        ButterKnife.bind(this);//ButterKnife初始化
    }

    private void init() {
        GridLayoutManager manager = new GridLayoutManager(this, 4);
        mRecyclerView.setLayoutManager(manager);

        ItemDragHelperCallback callback = new ItemDragHelperCallback();
        final ItemTouchHelper helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(mRecyclerView);

        final TabListAdapter adapter = new TabListAdapter(this, helper, myTabs, otherTabs);
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                int viewType = adapter.getItemViewType(position);
                return viewType == BaseTabListAdapter.TYPE_MY || viewType == BaseTabListAdapter.TYPE_OTHER ? 1 : 4;
            }
        });
        mRecyclerView.setAdapter(adapter);

        adapter.setOnMyItemClickListener(new BaseTabListAdapter.OnMyItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                Intent intent=new Intent();
                if(isUpdate()){
                    intent.putExtra(MY_CHANNEL, myTabs);
                    intent.putExtra(OTHER_CHANNEL, otherTabs);
                }

                intent.putExtra(MainActivity.GOTO_FRAGMENT,position);
                setResult(RESULT_CODE,intent);
                finish();
            }
        });
    }

    //数据是否改变，有无操作
    private boolean isUpdate(){
        if(lastTabs.size()!= myTabs.size()){
            return true;
        }else{
            for (int i = 0; i < lastTabs.size(); i++) {
                if(lastTabs.get(i).getId()!= myTabs.get(i).getId()){
                    return true;
                }
            }

        }
        return false;
    }

    @OnClick({R.id.icon_collapse})
    public void onEventClick(View view) {
        switch (view.getId()) {
            case R.id.icon_collapse://退出监听

                Intent intent=new Intent();
                intent.putExtra(MY_CHANNEL, myTabs);
                intent.putExtra(OTHER_CHANNEL, otherTabs);
                setResult(RESULT_CODE,intent);
                finish();
                break;
        }
    }

}
