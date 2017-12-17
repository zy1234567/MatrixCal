package com.bn.juzhen;

import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import Jama.*;

//矩阵计算类
public class MatrixCal {

    public CAL_TYPE type;//当前计算类型
	public enum CAL_TYPE {DETERMINANT,RANK,MULTIPLICATION,TRANSPOSE,LUDECOMPOSITION;} //计算类型
	public Matrix matrixA=null;//矩阵A，用于矩阵的乘法
	public Matrix matrixB=null;//矩阵B
	
	//字符串转为二维数组
	public double[][] StringToArray(String s) throws Exception{
		int rowCount=0;
		int columnCount=0;
		String[] sourceStrArray = s.split("\n");
		double[][] ansArray = null;

		ArrayList<String> arrayList = new ArrayList<String>(Arrays.asList(sourceStrArray));

		for(int i=0;i<arrayList.size();i++){
			if(arrayList.get(i).trim().equals("")){
				arrayList.remove(i--);
			}
		}
		rowCount = arrayList.size();
		

		for(int i=0;i<rowCount;i++){
			
			sourceStrArray[i] = sourceStrArray[i].trim();
			String[] itemArray = sourceStrArray[i].split("( )+");

			if(i==0){
				for(String itemStr : itemArray){
					if(!itemStr.trim().equals("")){
						columnCount++;
					}
				}
				ansArray = new double[rowCount][columnCount];
			}
				for(int j=0;j<itemArray.length;j++){
					ansArray[i][j] = Double.parseDouble(itemArray[j]);
				}

		}
		return ansArray;
	}
	
	
	//二维浮点型数组转为字符串
	public String ArrayToString(double[][] matrixArray){
		String s = "";
		for(int i = 0;i<matrixArray.length;i++){
			for(int j = 0;j<matrixArray[0].length;j++){
				if(matrixArray[i][j]%1 == 0){
					s+=((int)matrixArray[i][j]+" ");
				}else{

					s+=(String.format("%.3f", matrixArray[i][j])+"\t");
				}
			}
			s+="\n";
		}
		return s;
	}

	//二维字符串数组转为字符串
	public String ArrayToString(String[][] matrixArray){
		String s="";
		for(int i = 0;i<matrixArray.length;i++){
			for(int j = 0;j<matrixArray[0].length;j++){
					s+=(matrixArray[i][j]+" ");
			}
			s+="\n";
		}
		return s;
	}


	//求两个数的最大公约数
	public int getGongYueShu(int a, int b) {
		int t = 0;
		if(a < b){
			t = a;
			a = b;
			b = t;
		}
		int c = a % b;
		if(c == 0){
			return b;
		}else{
			return getGongYueShu(b, c);
		}
	}

	//小数转化为分数
	public String changeToFraction(double num){

		//Log.d("","输入："+num);
		String[] array = new String[2];
		array = (num+"").split("\\.");
		int a = Integer.parseInt(array[0]);//获取整数部分
		int b = Integer.parseInt(array[1]);//获取小数部分
		//Log.d("a和b",a+":"+b);
		int length = array[1].length();
		int FenZi = (int) (a * Math.pow(10, length) + b);
		int FenMu = (int) Math.pow(10, length);
		int MaxYueShu = getGongYueShu(FenZi, FenMu);

		return FenZi / MaxYueShu + "/" + FenMu / MaxYueShu;
	}

	//矩阵计算
	public String calMatrix(String s) throws Exception{

		double [][] matrixArray = null; //矩阵数组
		matrixArray = StringToArray(s); //将矩阵转为字符串

		if(matrixA == null){
			matrixA = new Matrix(matrixArray);//记录矩阵A
		}else{
			matrixB = new Matrix(matrixArray);//记录矩阵B
		}

		Matrix matrix = new Matrix(matrixArray);//创建矩阵计算类
		//根据选取类型进行计算
		switch(type){
		case DETERMINANT://行列式
            double detNum = matrix.det();
            return detNum+"";
		case RANK:      //矩阵的秩
			int n = matrix.rank();
            return n+"";
		case TRANSPOSE://转置矩阵
			double[][] tranMatrix = matrix.transpose().getArray();
            return ArrayToString(tranMatrix)+"";
		case MULTIPLICATION://矩阵的乘法
			if(matrixB != null){
				double[][] mulMatrix=matrixA.times(matrixB).getArray();
                return ArrayToString(mulMatrix);
			}
            return "##";
		case LUDECOMPOSITION://LU分解
			LUDecomposition lu = matrix.lu();

			//将LU分解所获取的小数结果转化为分数
			double[][] L = lu.getL().getArray();
			double[][] U = lu.getU().getArray();
			String sL = "";
			String sU = "";
			for(int i=0;i<L.length;i++){
				for(int j=0;j<U.length;j++){
					sL+=L[i][j]+" ";
					sU+=U[i][j]+" ";
				}
				sL+="\n";
				sU+="\n";
			}
			Log.d("SL",sL+"");
			Log.d("SU", sU+"");
			String[][] LS = new String[L.length][L[0].length];
			String[][] US = new String[U.length][U[0].length];
			for(int i=0; i<L.length;i++){
				for(int j=0; j<U.length;j++){
					Log.d(L[i][j]+":",""+U[i][j]);
					LS[i][j] = changeToFraction(L[i][j]);
					US[i][j] = changeToFraction(U[i][j]);
				}
			}

            return "L\n"+ArrayToString(LS)+"\nU\n"+ArrayToString(US);
		}
        return null;
	}
}
