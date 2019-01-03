package com.lrs.common.utils.permission;

import com.lrs.common.utils.Tools;

import java.math.BigInteger;

/**
 * @author Administrator
 * 权限计算帮助类
 */
public class RightsHelper {
	/**
	 * 利用BigInteger对权限进行2的权的和计算
	 * @param rights int型权限编码数组
	 * @return 2的权的和
	 */
	public static BigInteger sumRights(int[] rights){
		BigInteger num = new BigInteger("0");
		for(int i=0; i<rights.length; i++){
			num = num.setBit(rights[i]);
		}
		return num;
	}
	/**
	 * 利用BigInteger对权限进行2的权的和计算
	 * @param rights String型权限编码数组
	 * @return 2的权的和
	 */
	public static BigInteger sumRights(String[] rights){
		BigInteger num = new BigInteger("0");
		for(int i=0; i<rights.length; i++){
			num = num.setBit(Integer.parseInt(rights[i]));
		}
		return num;
	}
	
	/**
	 * 测试是否具有指定编码的权限
	 * @param sum
	 * @param targetRights
	 * @return
	 */
	public static boolean testRights(BigInteger sum,int targetRights){
		return sum.testBit(targetRights);
	}
	
	public static boolean testRights(BigInteger sum,long targetRights){
		return sum.testBit((int)targetRights);
	}
	
	/**
	 * 测试是否具有指定编码的权限
	 * @param sum
	 * @param targetRights
	 * @return
	 */
	public static boolean testRights(String sum,int targetRights){
		if(Tools.isEmpty(sum))
			return false;
		return testRights(new BigInteger(sum),targetRights);
	}
	
	public static boolean testRights(String sum,long targetRights){
		if(Tools.isEmpty(sum))
			return false;
		return testRights(new BigInteger(sum),(int)targetRights);
	}
	
	/**
	 * 测试是否具有指定编码的权限
	 * @param sum
	 * @param targetRights
	 * @return
	 */
	public static boolean testRights(String sum,String targetRights){
		if(Tools.isEmpty(sum))
			return false;
		return testRights(new BigInteger(sum),targetRights);
	}
	
	/**
	 * 测试是否具有指定编码的权限
	 * @param sum
	 * @param targetRights
	 * @return
	 */
	public static boolean testRights(BigInteger sum,String targetRights){
		return testRights(sum,Integer.parseInt(targetRights));
	}
	
	public static void main(String[] args) {
		BigInteger num = new BigInteger("24");    
//		num = num.setBit(1);
//	    num = num.setBit(2);    
//	    num = num.setBit(3);
//	    num = num.setBit(4);
//	    num = num.setBit(5);
//	    num = num.setBit(6);
	    System.out.println(num);    //62  94
		System.out.println(num.testBit(2));
		System.out.println(num.testBit(1));
		System.out.println(num.testBit(3));
		int sum = (int) (Math.pow(2, 1)+Math.pow(2, 2)+Math.pow(2, 3)+Math.pow(2, 4)+Math.pow(2, 5));
		System.out.println("sum="+sum);
	}
}
