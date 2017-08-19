package selectlabeltab.jessie.com.libtabmanage;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * @author JessieKate
 * @date 19/08/2017
 * @email lyj1246505807@gmail.com
 * @describe
 */

public class BaseTabListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements OnItemMoveListener {
    // 类型定义
    public static final int TYPE_MY_HEADER = 0; // 我的分页标题
    public static final int TYPE_MY = 1; // 我的分页
    public static final int TYPE_OTHER_HEADER = 2;  // 其他分页标题
    public static final int TYPE_OTHER = 3; // 其他分页

    // 数量定义
    protected static final int COUNT_PRE_MY_HEADER = 1; // 我的分页之前header数量
    protected static final int COUNT_PRE_OTHER_HEADER = COUNT_PRE_MY_HEADER + 1; // 其他分页之前header数量

    protected long startTime; // touch点击开始时间
    protected static final long SPACE_TIME = 100; // touch间隔时间 与"点击"区分

    protected boolean isEditMode;  //编辑模式标志
    protected ItemTouchHelper mItemTouchHelper;
    protected OnMyItemClickListener onMyItemClickListener;// 我的分页点击事件

    private List myItems;
    private List otherItems;

    public BaseTabListAdapter(List myItems, List otherItems) {
        this.myItems = myItems;
        this.otherItems = otherItems;
    }

    public interface OnMyItemClickListener {
        void onItemClick(View v, int position);
    }

    public void setOnMyItemClickListener(OnMyItemClickListener listener) {
        this.onMyItemClickListener = listener;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        // 我的分页标题 + 我的分页.size + 其他分页标题 + 其他分页.size
        return myItems.size() + otherItems.size() + BaseTabListAdapter.COUNT_PRE_OTHER_HEADER;
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        //过滤第一个，并且也不能移动到第一个去
        if(fromPosition == 1 || toPosition == 1){
            return;
        }
        //得到当前移动的item
        Object item = myItems.get(fromPosition - BaseTabListAdapter.COUNT_PRE_MY_HEADER);
        //删除数据集中的对应数据
        myItems.remove(fromPosition - BaseTabListAdapter.COUNT_PRE_MY_HEADER);
        //添加数据到移动后的位置
        myItems.add(toPosition - BaseTabListAdapter.COUNT_PRE_MY_HEADER, item);
        //开始刷新
        notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {    // 我分页标题
            return BaseTabListAdapter.TYPE_MY_HEADER;
        } else if (position == myItems.size() + 1) { // 其他分页标题
            return BaseTabListAdapter.TYPE_OTHER_HEADER;
        } else if (position > 0 && position < myItems.size() + 1) { //我的分页
            return BaseTabListAdapter.TYPE_MY;
        } else {              //其他分页
            return BaseTabListAdapter.TYPE_OTHER;
        }
    }


}
