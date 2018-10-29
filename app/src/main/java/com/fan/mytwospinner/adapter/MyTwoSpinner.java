package com.fan.mytwospinner.adapter;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.example.fan.myapplication2.R;
/**
 * 自定义二级级联菜单<br/>
 * fan 2018/8/31 12:03
 */
public class MyTwoSpinner extends LinearLayout implements View.OnClickListener {

    private ClassifyMainAdapter classifyMainAdapter;
    private ClassifyMoreAdapter classifyMoreAdapter;
    private List<Map<String, Object>> mainList;
    private ListView mainlist;
    private ListView morelist;

    private int mainSelectPostion = 0;
    private TextView tv_select_action;
    private LinearLayout ll_spinner_container;

    private boolean isContainerShow;
    private ImageView iv_select_action_arrow;

    public MyTwoSpinner(Context context) {
        this(context, null);
    }

    public MyTwoSpinner(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyTwoSpinner(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initModelData();
        initView();
    }

    public void hide(){
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(ll_spinner_container, "translationY",
                0, -ll_spinner_container.getHeight());
        //按钮方位改变
        ObjectAnimator rotationAnimator = ObjectAnimator.ofFloat(iv_select_action_arrow, "rotation", 90f, 0f);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(objectAnimator).with(rotationAnimator);
        animatorSet.setDuration(600);
        animatorSet.start();
        isContainerShow = false;
    }

    public void show(){
        //面板显示
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(ll_spinner_container, "translationY",
                -ll_spinner_container.getHeight(), 0);
        //按钮方位改变
        ObjectAnimator rotationAnimator = ObjectAnimator.ofFloat(iv_select_action_arrow, "rotation", 0, 90f);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(objectAnimator).with(rotationAnimator);
        animatorSet.setDuration(600);
        animatorSet.start();


        isContainerShow = true;
    }

    // 显示视图
    private void initView() {
        View inflate = LayoutInflater.from(getContext()).inflate(R.layout.my_two_spinner, this);
        iv_select_action_arrow = ((ImageView) inflate.findViewById(R.id.iv_select_action_arrow));
        // TODO Auto-generated method stub
        mainlist = (ListView) inflate.findViewById(R.id.main_view);
        morelist = (ListView) inflate.findViewById(R.id.more_view);
        tv_select_action = ((TextView) inflate.findViewById(R.id.tv_select_action));
        tv_select_action.setOnClickListener(this);
        ll_spinner_container = ((LinearLayout) inflate.findViewById(R.id.ll_spinner_container));
        //一开始将面板隐藏
//        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.spinner_container_start);
//        ll_spinner_container.startAnimation(animation);

        classifyMainAdapter = new ClassifyMainAdapter(
                getContext(), mainList);
        // 默认选中第一个选项
        mainlist.setSelection(0);
        // 建立数据适配
        mainlist.setAdapter(classifyMainAdapter);

        // 设置listView当中的每个单项点击的事件变化逻辑处理
        mainlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            // 主目录的点击事件发生后，就要为侧目进行数据的交互
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                mainSelectPostion = position;
                // 主目录一位数组的大小和侧目录二维数组的行的数目是一致的
                // 点击传入二维数组的一行的数据
                inintAdapter(DataModel.MORELISTVIEWTXT[position]);
                // 设置选中的选的id
                classifyMainAdapter.setSelectItem(position);
                // 更新数据的变更
                classifyMainAdapter.notifyDataSetChanged();

            }

        });

        /**
         * CHOICE_MODE_NONE是普通模式， CHOICE_MODE_SINGLE是单选模式，不常用，
         * CHOICE_MODE_MULTIPLE和CHOICE_MODE_MULTIPLE_MODAL都是多选模式
         *
         * 设置选着的模式
         * */
        mainlist.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        // 设置还没有点击时默认的显示页面的内容
        inintAdapter(DataModel.MORELISTVIEWTXT[0]);

        // 设置详细列表的点击事件处理逻辑
        morelist.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                classifyMoreAdapter.setSelectItem(position);
                classifyMoreAdapter.notifyDataSetChanged();
                String content = DataModel.MORELISTVIEWTXT[mainSelectPostion][position];
                Toast.makeText(getContext(),
                        "你点击的是" + content,
                        Toast.LENGTH_SHORT).show();
                tv_select_action.setText(content);
                hide();
            }
        });
    }

    // 为侧目录(详细目录)进行数据的匹配处理
    private void inintAdapter(String[] array) {
        // TODO Auto-generated method stub
        classifyMoreAdapter = new ClassifyMoreAdapter(getContext(), array);
        morelist.setAdapter(classifyMoreAdapter);
        classifyMoreAdapter.notifyDataSetChanged();
    }

    // 初始化化数据的设定（String在java中为对象存储的,不是基本的常量）
    private void initModelData() {
        // TODO Auto-generated method stub
        mainList = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < DataModel.LISTVIEWTXT.length; i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            // 根据键值对存储到HashMap中去
//            map.put("img", DataModel.LISTVIEWIMG[i]);
            map.put("txt", DataModel.LISTVIEWTXT[i]);
            mainList.add(map);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_select_action:
                if (isContainerShow) {
                    hide();
                }else{
                    show();
                }
            break;
        }
    }
}