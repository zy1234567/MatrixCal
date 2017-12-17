package com.bn.juzhen;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bn.juzhen.adapter.GridViewAdapter;
import com.bn.juzhen.util.MatrixCal;
import com.github.florent37.viewtooltip.ViewTooltip;

import java.lang.reflect.Method;

//主界面Activity
public class MainActivity extends Activity  implements RadioGroup.OnCheckedChangeListener{

    GridViewAdapter gridViewAdapter;    //光标操作按钮适配器
    GridViewAdapter gridViewAdapte2;    //底部按钮适配器
    GridView gridView;                  //光标操作按钮布局
    GridView gridView2;                 //底部按钮布局
    public static Activity activity;    //当前Activity

    ImageView help;                     //帮助图标
    EditText editText;                  //文本框
    TextView tiptext;                   //提示文本框
    //按钮字符串
    String[] ss = new String[]{ "7", "8", "9","换行","4", "5", "6", "空格", "1", "2", "3", "清空", "-","0", ".", "计算"};

    //单选按钮组
    RadioGroup radioGroup;
    RadioButton HLS;
    RadioButton QZ;
    RadioButton XC;
    RadioButton FJ;

    //矩阵计算类
    MatrixCal matrixCal = new MatrixCal();
    public static boolean isFirst=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        activity=this;
        initView();     //初始化控件
        addListener();  //添加监听
        isFirst=false;
        //Log.d("","初始光标位置："+getEditSelection());
    }

    //初始化控件
    public void initView() {
        //适配器类型
        gridViewAdapter=new GridViewAdapter(this,"0");
        gridViewAdapte2=new GridViewAdapter(this,"2");
        //布局初始化
        gridView=(GridView) this.findViewById(R.id.mGridView);
        gridView2=(GridView)this.findViewById(R.id.mGridView2);
        help=(ImageView) this.findViewById(R.id.help) ;

        //文本框初始化
        editText=(EditText)this.findViewById(R.id.edittext);
        tiptext=(TextView)this.findViewById(R.id.tiptext);

        //按钮组的初始化
        radioGroup =(RadioGroup)this.findViewById(R.id.main_bottom_tabs) ;
        HLS=(RadioButton)this.findViewById(R.id.radio1);
        QZ=(RadioButton)this.findViewById(R.id.radio2);
        XC=(RadioButton)this.findViewById(R.id.radio3);
        FJ=(RadioButton)this.findViewById(R.id.radio4);

        //按钮组添加监听
        radioGroup.setOnCheckedChangeListener(this);
        //默认选中行列式
        HLS.setChecked(true);
        matrixCal.type = MatrixCal.CAL_TYPE.DETERMINANT;

        //布局添加适配器
        gridView.setAdapter(gridViewAdapter);
        gridView2.setAdapter(gridViewAdapte2);

        //设置不弹出手机的软键盘
        if (android.os.Build.VERSION.SDK_INT <= 10) {//版本低于10
            editText.setInputType(InputType.TYPE_NULL);   //设置软键盘不弹出
        } else {
            MainActivity.this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            try {
                Class<EditText> cls = EditText.class;
                Method setSoftInputShownOnFocus;
                setSoftInputShownOnFocus = cls.getMethod("setShowSoftInputOnFocus", boolean.class);
                setSoftInputShownOnFocus.setAccessible(true);
                setSoftInputShownOnFocus.invoke(editText, false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //Girdview监听部分  数字部分和 和 图片部分
    public void addListener() {
            editText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ViewTooltip
                            .on(editText)
                            .color(Color.BLACK)
                            .position(ViewTooltip.Position.BOTTOM).align(ViewTooltip.ALIGN.START)
                            .text("数据显示在这里！")
                            .clickToHide(true)
                            .autoHide(false, 0)
                            .animation(new ViewTooltip.FadeTooltipAnimation(500))
                            .onDisplay(new ViewTooltip.ListenerDisplay() {
                                @Override
                                public void onDisplay(View view) {
                                }
                            })
                            .onHide(new ViewTooltip.ListenerHide() {
                                @Override
                                public void onHide(View view) {
                                }
                            })
                            .show();
                }
            });

        //底部GridView添加监听

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           @Override
           public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               //根据点击情况更改文本框的内容
               switch(position) {
                   case 3://换行按键
                       addString("\n");
                       break;
                   case 7://空格按键
                       addString(" ");
                       break;
                   case 11://清空按键
                       clearText();
                       break;
                   case 15://计算按键
                       String s = editText.getEditableText().toString().substring(0,editText.getEditableText().toString().length())+"";//文本框中的内容
                       Log.d("","文本框内容："+s);
                       calMatrix(s);
                       editText.setSelection(editText.getEditableText().length());
                       break;
                   default://其他数字按键
                       addString(ss[position]);
                       break;
               }
           }
       });
        //光标按钮部分设置监听
       gridView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
          @Override
          public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
              ViewTooltip
                      .on(gridView2)
                      .color(Color.BLACK)
                      .position(ViewTooltip.Position.BOTTOM).align(ViewTooltip.ALIGN.START)
                      .text("前进、后退、删除在这里")
                      .clickToHide(true)
                      .autoHide(false, 0)
                      .animation(new ViewTooltip.FadeTooltipAnimation(500))
                      .onDisplay(new ViewTooltip.ListenerDisplay() {
                          @Override
                          public void onDisplay(View view) {
                          }
                      })
                      .onHide(new ViewTooltip.ListenerHide() {
                          @Override
                          public void onHide(View view) {
                          }
                      })
                      .show();
             if(position==1) {      //光标回退
                if(getEditSelection()>=1) {
                   editText.setSelection(getEditSelection() - 1);
                }
             }if(position==2) {     //光标前进
                  if(getEditSelection()!=editText.getText().length()){
                      editText.setSelection(getEditSelection()+1);
                  }
              }if(position==3) {    //删除上一字符
                  if(getEditSelection()>=1) {
                      deleteEditValue(getEditSelection());
                  }
              }
          }
       });

    }

    //显示顶部提示文字
    public void setTip(String s) {
        tiptext.setText(s);
    }

    //向文本框中增添字符
    public void addString(String sequence) {
        //System.out.println("IN");
        int index = getEditSelection();     //获取光标的位置
        //System.out.println("光标的位置："+index);

        //根据光标位置添加字符
        //contents.insert(index,sequence);
        editText.getText().insert(index, sequence);// 光标所在位置插入文字

    }

    //计算矩阵
    public void calMatrix(String s){
        //String s = editText.getText().toString();//文本框中的内容

        //Log.d("","Cal文本框的内容："+s);
        if(s.equals("")) return;
        String ans = "";//计算结果
        try{
            ans = matrixCal.calMatrix(s);
            if(ans.equals("##")){
                clearText();
                setTip("请输入矩阵B，计算A*B");
            }else{
                if(matrixCal.type == MatrixCal.CAL_TYPE.MULTIPLICATION){
                    editText.setText("矩阵A为：\n"+matrixCal.ArrayToString(matrixCal.matrixA.getArray())+
                                    "\n矩阵B为：\n"+matrixCal.ArrayToString(matrixCal.matrixB.getArray())+
                                    "\nA*B结果为：\n"+ans);
                    matrixCal.matrixA=null;
                    matrixCal.matrixB=null;
                    setTip("请输入矩阵A，计算A*B");
                }else{
                    editText.append("\n\n 计算结果为:\n"+ans);
                }

            }

        }catch (Exception e){
            showToast("请输入正确格式的矩阵");
        }
    }


    // 获取光标当前位置
    public int getEditSelection() {
        return editText.getSelectionStart();
    }

    // 获取文本框的内容
    public String getEditTextViewString() {
        return editText.getText().toString();
    }

    // 清除文本框中的内容
    public void clearText() {
        editText.getText().clear();
    }

    // 删除指定位置的字符
    public void deleteEditValue(int index) {
        editText.getText().delete(index - 1, index);
    }

    //计算类型监听方法
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        ViewTooltip
                .on(radioGroup)
                .color(Color.BLACK)
                .position(ViewTooltip.Position.BOTTOM).align(ViewTooltip.ALIGN.START)
                .text("计算方法这里！")
                .clickToHide(true)
                .autoHide(false, 0)
                .animation(new ViewTooltip.FadeTooltipAnimation(500))
                .onDisplay(new ViewTooltip.ListenerDisplay() {
                    @Override
                    public void onDisplay(View view) {
                    }
                })
                .onHide(new ViewTooltip.ListenerHide() {
                    @Override
                    public void onHide(View view) {
                    }
                })
                .show();
        matrixCal.matrixB = null;
        matrixCal.matrixA = null;

        if(checkedId==R.id.radio1)//行列式
        {
            setTip("请输入矩阵计算行列式");
            matrixCal.type = MatrixCal.CAL_TYPE.DETERMINANT;
        }if(checkedId==R.id.radio2)//求秩
        {
            setTip("请输入矩阵求秩");
            matrixCal.type = MatrixCal.CAL_TYPE.RANK;
        }if(checkedId==R.id.radio3)//相乘
        {
            setTip("请输入矩阵A，计算A*B");
            matrixCal.type = MatrixCal.CAL_TYPE.MULTIPLICATION;
        }if(checkedId==R.id.radio4)//LU分解
        {
            setTip("请输入矩阵进行LU分解");
            matrixCal.type = MatrixCal.CAL_TYPE.LUDECOMPOSITION;
        }
    }

    //显示Toast提示信息
    public void showToast(String s){
        Toast toast = Toast.makeText(MainActivity.this,s , Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, -90);
        toast.show();
    }

}
