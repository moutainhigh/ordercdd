package com.liyang;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.liyang.util.SerialNumberUtil;

/**
 * 订单号自己的生成逻辑：
 * 1.用大写字母开头，标识类型
 * 2.日期，精确到天
 * 3.随机数，唯一标识
 */
public class SerialNumberUtilTest {
	
	public static void test1() {
		HashSet<String> serialSet = new HashSet<>();
		for (int i = 0; i < 10000; i++) {
			String serialNumber = SerialNumberUtil.INSTANCE.create(SerialNumberUtil.SerialType.ORDERCDD);
			if (serialSet.contains(serialNumber)) {
				System.out.println("重大错误，重复的流水号："+serialNumber);
				break;
			}else {
				serialSet.add(serialNumber);
			}
			System.out.println(serialNumber);
		}
	}
	
	public static void test2() {
		int max = 100000;
		Set<String> serialSet = new CopyOnWriteArraySet<>();
		List<String> errorList = new CopyOnWriteArrayList<>();
		
		ExecutorService pool=Executors.newFixedThreadPool(100);//创建固定大小的线程池
        CompletionService<Object> cs=new ExecutorCompletionService<Object>(pool);
        for (int i=0;i<max;i++) {
            cs.submit(new Callable<Object>() {
                @Override
                public Object call() throws Exception {
                	String serialNumber = SerialNumberUtil.INSTANCE.create(SerialNumberUtil.SerialType.ORDERCDD);
            		if (serialSet.contains(serialNumber)) {
        				System.out.println("重大错误，重复的流水号："+serialNumber);
        				errorList.add("重大错误，重复的流水号："+serialNumber);
        			}
    				serialSet.add(serialNumber);
                	System.out.println(serialNumber);
                    return null;
                }
            });
        }
        for (int i=0;i<max;i++) {//等待所有线程结束
            try {
                cs.take();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        pool.shutdown();
        System.out.println(serialSet.size());
        System.out.println(errorList.toString());
	}
	
	public static void test3() {
		System.out.println(SerialNumberUtil.INSTANCE.create(SerialNumberUtil.SerialType.ORDERCDD));
	}
	
	public static void main(String[] args) {
		test3();
		
//		System.out.println(Integer.toBinaryString(13));
//		System.out.println(Integer.toBinaryString(53));
//		System.out.println(Integer.toBinaryString((13<<15)|(53<<9)|0));
//		System.out.println(Integer.toBinaryString(453120));
//		System.out.println(Integer.parseInt("110101", 2));
	}
}
