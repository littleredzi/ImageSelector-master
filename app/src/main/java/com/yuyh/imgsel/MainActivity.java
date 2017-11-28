package com.yuyh.imgsel;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.facebook.drawee.view.SimpleDraweeView;
import com.yuyh.library.imgsel.ISNav;
import com.yuyh.library.imgsel.common.ImageLoader;
import com.yuyh.library.imgsel.config.ISListConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * https://github.com/smuyyh/ImageSelector
 *
 * @author yuyh.
 * @date 2016/8/5.
 */
public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_LIST_CODE = 0;
    private static final int REQUEST_CAMERA_CODE = 1;

    private TextView tvResult;
    private SimpleDraweeView draweeView;
    private GridView gridView;
    List<String>listUriData=new ArrayList<>();
    private MyBase myBase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        Fresco.initialize(this);
        setContentView(R.layout.activity_main);
        initView();

         myBase = new MyBase();
        gridView.setAdapter(myBase);
        listUriData.add(0,"");
        myBase.notifyDataSetChanged();


//        tvResult = (TextView) findViewById(R.id.tvResult);
        draweeView = (SimpleDraweeView) findViewById(R.id.my_image_view);

        ISNav.getInstance().init(new ImageLoader() {
            @Override
            public void displayImage(Context context, String path, ImageView imageView) {
                Glide.with(context).load(path).into(imageView);
            }
        });
    }

    public void Multiselect(View view) {
//        tvResult.setText("");
        ISListConfig config = new ISListConfig.Builder()
                .multiSelect(true)
                // 是否记住上次选中记录
                .rememberSelected(false)
                // 使用沉浸式状态栏
                .statusBarColor(Color.parseColor("#3F51B5")).build();

        ISNav.getInstance().toListActivity(this, config, REQUEST_LIST_CODE);
    }

    public void Single(View view) {
        tvResult.setText("");
        ISListConfig config = new ISListConfig.Builder()
                // 是否多选
                .multiSelect(false)
                .btnText("Confirm")
                // 确定按钮背景色
                //.btnBgColor(Color.parseColor(""))
                // 确定按钮文字颜色
                .btnTextColor(Color.WHITE)
                // 使用沉浸式状态栏
                .statusBarColor(Color.parseColor("#3F51B5"))
                // 返回图标ResId
                .backResId(android.support.v7.appcompat.R.drawable.abc_ic_ab_back_mtrl_am_alpha)
                .title("Images")
                .titleColor(Color.WHITE)
                .titleBgColor(Color.parseColor("#3F51B5"))
                .allImagesText("All Images")
                .needCrop(true)
                .cropSize(1, 1, 200, 200)
                // 第一个是否显示相机
                .needCamera(true)
                // 最大选择图片数量
                .maxNum(1)
                .build();

        ISNav.getInstance().toListActivity(this, config, REQUEST_LIST_CODE);
    }
//
//    public void Camera(View view) {
//        ISCameraConfig config = new ISCameraConfig.Builder()
//                .needCrop(true)
//                .cropSize(1, 1, 200, 200)
//                .build();
//
//        ISNav.getInstance().toCameraActivity(this, config, REQUEST_CAMERA_CODE);
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_LIST_CODE && resultCode == RESULT_OK && data != null) {
            List<String> pathList = data.getStringArrayListExtra("result");

            // 测试Fresco
//            draweeView.setImageURI(Uri.parse("file://" + pathList.get(0)));
            listUriData.addAll(pathList);
            myBase.notifyDataSetChanged();

//            for (String path : pathList) {
//                tvResult.append(path + "\n");
//            }
//        } else if (requestCode == REQUEST_CAMERA_CODE && resultCode == RESULT_OK && data != null) {
//            String path = data.getStringExtra("result");
//            tvResult.append(path + "\n");
//
        }

    }

    private void initView() {
        gridView = (GridView) findViewById(R.id.gridView);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                listUriData.remove(i);
//                myBase.notifyDataSetChanged();


            }
        });
    }

    class MyBase extends BaseAdapter{

        @Override
        public int getCount() {
            return listUriData.size();
        }

        @Override
        public Object getItem(int i) {
            return listUriData.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View inflate=null;

            if(i==getCount()-1){
                inflate = LayoutInflater.from(MainActivity.this).inflate(R.layout.grid_item2, null);
            }else {
                inflate = LayoutInflater.from(MainActivity.this).inflate(R.layout.grid_item, null);
                ImageView image = (ImageView) inflate.findViewById(R.id.my_image_view);
                image.setImageURI(Uri.parse("file://" + listUriData.get(i)));

            }
            return inflate;
        }
    }
}
