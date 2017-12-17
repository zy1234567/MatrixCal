package com.bn.juzhen;

import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import Jama.*;

//���������
public class MatrixCal {

    public CAL_TYPE type;//��ǰ��������
	public enum CAL_TYPE {DETERMINANT,RANK,MULTIPLICATION,TRANSPOSE,LUDECOMPOSITION;} //��������
	public Matrix matrixA=null;//����A�����ھ���ĳ˷�
	public Matrix matrixB=null;//����B
	
	//�ַ���תΪ��ά����
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
	
	
	//��ά����������תΪ�ַ���
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

	//��ά�ַ�������תΪ�ַ���
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


	//�������������Լ��
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

	//С��ת��Ϊ����
	public String changeToFraction(double num){

		//Log.d("","���룺"+num);
		String[] array = new String[2];
		array = (num+"").split("\\.");
		int a = Integer.parseInt(array[0]);//��ȡ��������
		int b = Integer.parseInt(array[1]);//��ȡС������
		//Log.d("a��b",a+":"+b);
		int length = array[1].length();
		int FenZi = (int) (a * Math.pow(10, length) + b);
		int FenMu = (int) Math.pow(10, length);
		int MaxYueShu = getGongYueShu(FenZi, FenMu);

		return FenZi / MaxYueShu + "/" + FenMu / MaxYueShu;
	}

	//�������
	public String calMatrix(String s) throws Exception{

		double [][] matrixArray = null; //��������
		matrixArray = StringToArray(s); //������תΪ�ַ���

		if(matrixA == null){
			matrixA = new Matrix(matrixArray);//��¼����A
		}else{
			matrixB = new Matrix(matrixArray);//��¼����B
		}

		Matrix matrix = new Matrix(matrixArray);//�������������
		//����ѡȡ���ͽ��м���
		switch(type){
		case DETERMINANT://����ʽ
            double detNum = matrix.det();
            return detNum+"";
		case RANK:      //�������
			int n = matrix.rank();
            return n+"";
		case TRANSPOSE://ת�þ���
			double[][] tranMatrix = matrix.transpose().getArray();
            return ArrayToString(tranMatrix)+"";
		case MULTIPLICATION://����ĳ˷�
			if(matrixB != null){
				double[][] mulMatrix=matrixA.times(matrixB).getArray();
                return ArrayToString(mulMatrix);
			}
            return "##";
		case LUDECOMPOSITION://LU�ֽ�
			LUDecomposition lu = matrix.lu();

			//��LU�ֽ�����ȡ��С�����ת��Ϊ����
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
