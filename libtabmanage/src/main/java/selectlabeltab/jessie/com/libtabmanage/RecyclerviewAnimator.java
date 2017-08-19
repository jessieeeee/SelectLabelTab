package selectlabeltab.jessie.com.libtabmanage;

import android.graphics.Bitmap;
import android.os.Handler;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;

import java.util.List;


/**
 * @author JessieKate
 * @date 13/08/2017
 * @email lyj1246505807@gmail.com
 * @describe recyclerview item动画工具类
 */

public class RecyclerviewAnimator {

    public static Handler delayHandler = new Handler();//延时handler
    private static final long ANIM_TIME = 360L;//动画时间
    //我的分页移动到其他分页
    public static void myToOther(int position, RecyclerView parent, RecyclerView.Adapter adapter,RecyclerView.ViewHolder myHolder, List myItems,List otherItems){
        if(position == 1){//过滤第一个
            return;
        }
        RecyclerView recyclerView = parent;
        //目标view是其他分页的第一个
        View targetView = recyclerView.getLayoutManager().findViewByPosition(myItems.size() + BaseTabListAdapter.COUNT_PRE_OTHER_HEADER);
        //当前view
        View currentView = recyclerView.getLayoutManager().findViewByPosition(position);
        // 如果targetView不在屏幕内,则indexOfChild为-1，此时不需要添加动画,因为此时notifyItemMoved自带一个向目标移动的动画
        // 如果在屏幕内,则添加一个位移动画
        if (recyclerView.indexOfChild(targetView) >= 0) {
            int targetX, targetY;

            RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
            int spanCount = ((GridLayoutManager) manager).getSpanCount();
            // 计算移动后的坐标
            // 移动后 高度变化 (我的分页Grid最后一个item在其他分页新的一行第一个)
            if ((myItems.size() - BaseTabListAdapter.COUNT_PRE_MY_HEADER) % spanCount == 0) {
                //获取我的分页最后一个
                View preTargetView = recyclerView.getLayoutManager().findViewByPosition(myItems.size() + BaseTabListAdapter.COUNT_PRE_OTHER_HEADER - 1);
                targetX = preTargetView.getLeft();
                targetY = preTargetView.getTop();
            } else {
                targetX = targetView.getLeft();
                targetY = targetView.getTop();
            }
            //开始执行动画
            startAnimation(recyclerView, currentView, targetX, targetY);
            moveMyToOther(adapter,myHolder,myItems,otherItems);
        } else {
            moveMyToOther(adapter,myHolder,myItems,otherItems);
        }
    }

    //其他分页移动到我的分页
    public static void otherToMy(RecyclerView parent,RecyclerView.Adapter adapter,RecyclerView.ViewHolder otherHolder, List myItems ,List otherItems){
        RecyclerView recyclerView =  parent;
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        //获取当前位置
        int currentPiosition = otherHolder.getAdapterPosition();
        // 获取当前位置的view
        View currentView = manager.findViewByPosition(currentPiosition);
        // 获取目标位置的view，我的分页最后一个
        View preTargetView = manager.findViewByPosition(myItems.size() - 1 + BaseTabListAdapter.COUNT_PRE_MY_HEADER);

        // 如果targetView不在屏幕内,则为-1，此时不需要添加动画,因为此时notifyItemMoved自带一个向目标移动的动画
        // 如果在屏幕内,则添加一个位移动画
        if (recyclerView.indexOfChild(preTargetView) >= 0) {
            int targetX = preTargetView.getLeft();
            int targetY = preTargetView.getTop();
            // target 我的分页最后一个
            int targetPosition = myItems.size() - 1 + BaseTabListAdapter.COUNT_PRE_OTHER_HEADER;

            GridLayoutManager gridLayoutManager = ((GridLayoutManager) manager);
            int spanCount = gridLayoutManager.getSpanCount();

            //目标位置在grid的第一个位置
            if ((targetPosition - BaseTabListAdapter.COUNT_PRE_MY_HEADER) % spanCount == 0) {
                View targetView = manager.findViewByPosition(targetPosition);
                targetX = targetView.getLeft();
                targetY = targetView.getTop();
            } else {
                targetX += preTargetView.getWidth();

                // 最后一个item可见时
                if (gridLayoutManager.findLastVisibleItemPosition() == adapter.getItemCount() - 1) {
                    // 最后一个item在最后一行第一个位置
                    if ((adapter.getItemCount() - 1 - myItems.size() - BaseTabListAdapter.COUNT_PRE_OTHER_HEADER) % spanCount == 0) {
                        int firstVisiblePostion = gridLayoutManager.findFirstVisibleItemPosition();
                        // 第一个item可见时
                        if (firstVisiblePostion == 0) {
                            // FirstCompletelyVisibleItemPosition == 0 内容不满一屏幕 , targetY值不需要变化
                            // // FirstCompletelyVisibleItemPosition != 0 内容满一屏幕 并且 可滑动 , targetY值 + firstItem.getTop
                            if (gridLayoutManager.findFirstCompletelyVisibleItemPosition() != 0) {
                                int offset = (-recyclerView.getChildAt(0).getTop()) - recyclerView.getPaddingTop();
                                targetY += offset;
                            }
                        } else {
                            // 移动后, targetY值+一个item的高度
                            targetY += preTargetView.getHeight();
                        }
                    }
                } else {
                    System.out.println("current--No");
                }
            }

            // 如果当前位置是其他分页的最后一个
            // 并且 当前位置不在grid的第一个位置
            // 并且 目标位置不在grid的第一个位置

            // 则需要延迟250秒notifyItemMove,这种情况,不触发ItemAnimator,会直接刷新界面
            if (currentPiosition == gridLayoutManager.findLastVisibleItemPosition()
                    && (currentPiosition - myItems.size() - BaseTabListAdapter.COUNT_PRE_OTHER_HEADER) % spanCount != 0
                    && (targetPosition - BaseTabListAdapter.COUNT_PRE_MY_HEADER) % spanCount != 0) {
                moveOtherToMyWithDelay(otherHolder,adapter,myItems,otherItems);
            } else {
                moveOtherToMy(otherHolder,adapter,myItems,otherItems);
            }
            startAnimation(recyclerView, currentView, targetX, targetY);

        } else {
            moveOtherToMy(otherHolder,adapter,myItems,otherItems);
        }
    }

    /**
     * 其他分页移动到我的分页
     *
     * @param otherHolder
     */
    public static void moveOtherToMy(RecyclerView.ViewHolder otherHolder,RecyclerView.Adapter adapter,List myItems,List otherItems) {
        int position = processItemRemoveAdd(otherHolder,myItems,otherItems);
        if (position == -1) {
            return;
        }
        //刷新该位置item
        adapter.notifyItemMoved(position, myItems.size() - 1 + BaseTabListAdapter.COUNT_PRE_MY_HEADER);
    }


    //其他分页移动到我的分页 伴随延迟
    public static void moveOtherToMyWithDelay(RecyclerView.ViewHolder otherHolder, final RecyclerView.Adapter adapter, final List myItems,List otherItems) {
        final int position = processItemRemoveAdd(otherHolder,myItems,otherItems);
        if (position == -1) {
            return;
        }
        delayHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //刷新该位置item
                adapter.notifyItemMoved(position, myItems.size() - 1 + BaseTabListAdapter.COUNT_PRE_MY_HEADER);
            }
        }, ANIM_TIME);
    }

    //将其他分页移动到我的分页
    public static int processItemRemoveAdd(RecyclerView.ViewHolder otherHolder, List myItems ,List otherItems) {
        //当前操作的位置
        int position = otherHolder.getAdapterPosition();
        //对应数据集中的位置
        int startPosition = position - myItems.size() - BaseTabListAdapter.COUNT_PRE_OTHER_HEADER;

        if (startPosition > otherItems.size() - 1) {
            return -1;
        }
        //获得移动的item
        Object item = otherItems.get(startPosition);
        //移除数据集中对应的数据
        otherItems.remove(startPosition);
        //添加到末尾
        myItems.add(item);
        return position;
    }


    //我的分页移动到其他分页
    public static void moveMyToOther(RecyclerView.Adapter adapter, RecyclerView.ViewHolder myHolder, List myItems ,List otherItems) {
        //当前操作的位置
        int position = myHolder.getAdapterPosition();
        //对应数据集中的位置
        int startPosition = position - BaseTabListAdapter.COUNT_PRE_MY_HEADER;
        if(startPosition == 0){ //过滤第一个
            return;
        }
        if (startPosition > myItems.size() - 1) {
            return;
        }
        //获得移动的item
        Object item = myItems.get(startPosition);
        //移除数据集中对应的数据
        myItems.remove(startPosition);
        //添加到第一个的位置
        otherItems.add(0, item);
        //刷新该位置item
        adapter.notifyItemMoved(position, myItems.size() + BaseTabListAdapter.COUNT_PRE_OTHER_HEADER);
    }


    /**
     * 启动动画
     */
    public static void startAnimation(RecyclerView recyclerView, final View currentView, float targetX, float targetY) {
        //获取recyclerView的外层view
        final ViewGroup viewGroup = (ViewGroup) recyclerView.getParent();
        final ImageView mirrorView = addMirrorView(viewGroup, recyclerView, currentView);

        Animation animation = getTranslateAnimator(
                targetX - currentView.getLeft(), targetY - currentView.getTop());
        //隐藏当前view
        currentView.setVisibility(View.INVISIBLE);
        //镜像view开始动画
        mirrorView.startAnimation(animation);
        // 动画监听
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                //动画结束，移除镜像view
                viewGroup.removeView(mirrorView);
                //当前view不可见时让其可见
                if (currentView.getVisibility() == View.INVISIBLE) {
                    currentView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

   //位移动画
    public static TranslateAnimation getTranslateAnimator(float targetX, float targetY) {
        TranslateAnimation translateAnimation = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0f,
                Animation.ABSOLUTE, targetX,
                Animation.RELATIVE_TO_SELF, 0f,
                Animation.ABSOLUTE, targetY);
        // RecyclerView默认移动动画250ms 这里设置360ms 是为了防止在位移动画结束后 remove(view)过早 导致闪烁
        translateAnimation.setDuration(ANIM_TIME);
        translateAnimation.setFillAfter(true);
        return translateAnimation;
    }

    //添加需要移动的 镜像View
    public static ImageView addMirrorView(ViewGroup parent, RecyclerView recyclerView, View view) {
        //把旧的cache销毁
        view.destroyDrawingCache();
        //通过setDrawingCacheEnable方法开启cache
        view.setDrawingCacheEnabled(true);
        //创建镜像view
        ImageView mirrorView = new ImageView(recyclerView.getContext());
        //调用getDrawingCache方法就可以获得view的cache图片
        Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache());
        mirrorView.setImageBitmap(bitmap);
        view.setDrawingCacheEnabled(false);
        int[] locations = new int[2];
        //获取view的位置到locations
        view.getLocationOnScreen(locations);
        int[] parenLocations = new int[2];
        //获取recyclerView的位置到parenLocations
        recyclerView.getLocationOnScreen(parenLocations);
        //设置布局的margin值
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(bitmap.getWidth(), bitmap.getHeight());
        params.setMargins(locations[0], locations[1] - parenLocations[1], 0, 0);
        //在动画起始处添加镜像view
        parent.addView(mirrorView, params);

        return mirrorView;
    }

}
