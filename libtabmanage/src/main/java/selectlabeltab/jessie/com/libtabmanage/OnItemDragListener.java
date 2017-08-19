package selectlabeltab.jessie.com.libtabmanage;

/**
 * @date 创建时间:13/08/2017
 * @author 编写人:JessieKate
 * @description 描述: 选中与拖拽后监听
 */

public interface OnItemDragListener {
    //Item选中监听
    void onItemSelected();


    //Item拖拽结束/滑动结束后监听
    void onItemFinish();
}
