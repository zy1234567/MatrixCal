package com.bn.juzhen.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.bn.juzhen.MainActivity;
import com.bn.juzhen.R;
import com.bn.juzhen.util.BaseTools;

//GridViewAdapter适配器
public class GridViewAdapter extends BaseAdapter {

    Context context;
    //按钮字符数组
    String[] ss = new String[]{ "7", "8", "9","换行","4", "5", "6", "空格", "1", "2", "3", "清空", "—","0", ".", "计算"};
    int d[] = new int[]{R.mipmap.ab_back1,R.mipmap.ab_go1,R.mipmap.delete};//图片数组
    LayoutInflater layoutInflater;
    String type;

    //初始化
    public GridViewAdapter(Context context, String type) {
        this.context=context;
        this.type=type;
        layoutInflater=LayoutInflater.from(context);
    }

    //获取Item的数量
    @Override
    public int getCount() {
        if(type.equals("0")){
            return ss.length;
        }else if(type.equals("2")) {
            return 4;
        }else {
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        View view;
        //设置Item的高度和宽度
        int width= BaseTools.getWindowWidth(MainActivity.activity)/4;
        int height=BaseTools.getWindowHeigh(MainActivity.activity)*7/100;
        //背景颜色
        Resources resources = context.getResources();
        int color = resources.getColor(R.color.yellow);

        //初始化布局
        if (convertView == null) {
            viewHolder = new ViewHolder();
            if(type.equals("0")) {
                view = layoutInflater.inflate(R.layout.gridview_item, parent, false);
                viewHolder.textView = (TextView) view.findViewById(R.id.shuju);
            }
            else{
                view = layoutInflater.inflate(R.layout.girdview_item2, parent, false);
                viewHolder.imageView= (ImageView) view.findViewById(R.id.shuju2);
            }
            view.setTag(viewHolder);
        } else {
            view=convertView;
            viewHolder=(ViewHolder) view.getTag();
        }
        if(type.equals("0")) {
            //设置计算按钮的颜色
            if (position == 15) {
                viewHolder.textView.setBackgroundColor(color);
            }
            //设置按钮的属性
            viewHolder.textView.setWidth(width);
            viewHolder.textView.setHeight(height);
            viewHolder.textView.setGravity(Gravity.CENTER);
            viewHolder.textView.setText(ss[position]);
        }if(type.equals("2")) {
            //设置光标操作按钮图标属性
            if(position>=1) {
                Drawable drawable=context.getResources().getDrawable(d[position-1]);
                viewHolder.imageView.setImageDrawable(drawable);
            }else {
                viewHolder.imageView.setBackgroundResource(R.color.heiye);
            }
        }
       return view;
    }

    //优化部分
    class ViewHolder {
        TextView textView;
        ImageView imageView;
    }
}
