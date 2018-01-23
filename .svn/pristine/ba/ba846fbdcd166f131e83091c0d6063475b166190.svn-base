package com.liyang.util;

import java.util.Calendar;

/**
 * 生成唯一流水号
 * 订单号组成15位
 * 大写字母+日期+（二进制）小时、分、序列数
 *                 小时          分             序列数
 * E + 20171110 + 11110 100001 000111111
 * 一分钟之内，最多允许创建511个，多于上限，等待下一分钟再生成
 */
public enum SerialNumberUtil {
	INSTANCE;
	
	/**
	 * 流水号的类型，即开头的字母
	 */
	public enum SerialType{
		/**
		 * 客户向门店申请订单号
		 */
		ORDERCDD("A"),
		/**
		 * 平台向资方申请单号
		 */
		ORDERCDDLOAN("B"),
		/**
		 * 平台向客户放款
		 */
		LOAN("E"),
		/**
		 * 平台向门店放款，分红
		 */
		STOREBONUS("F"),
		/**
		 * 客户还款计划
		 */
		LOANCREDITREPAYPLAN("K"),
		/**
		 * 平台还款计划
		 */
		STOREBONUSCREDITREPAYPLAN("L"),
		/**
		 * 客户还款记录
		 */
		LOANCREDITREPAYMENT("M"),
		/**
		 * 平台还款记录
		 */
		STOREBONUSCREDITREPAYMENT("N");
		
		private String value;
		
		private SerialType(String value) {
			this.value = value;
		}
		
		@Override
		public String toString() {
			return value;
		}
	}
	
	private String firstLetter;//流水号第一个字母
	
	private int currentTime;//当前时间，由小时、分钟组成的四位数字
	private int sequence = 0;//序列号
	/** 上次生成ID的时间截 */
    private long lastTimestamp = -1L;
	/*****************固定值，除非要修改算法，否则不能修改******************/
	private final int hourLeftRange = 15;//小时左移位数
	private final int MinuteLeftRange = 9;//分钟左移位数
	private final int maxSequence = 511;//序列数最大值，要求必须二进制数都是1
	private final int tailNumberLength = 6;//最后数值的位数
	
	public synchronized String create(SerialNumberUtil.SerialType type) {
		long timestamp = System.currentTimeMillis();
		//如果当前时间小于上一次ID生成的时间戳，说明系统时钟回退过这个时候应当抛出异常
        if (timestamp < lastTimestamp) {
            throw new RuntimeException(
                    String.format("Clock moved backwards.  Refusing to generate id for %d milliseconds", lastTimestamp - timestamp));
        }
        this.lastTimestamp = timestamp;
		this.firstLetter = type.value;
		int currentHour = currentHour();
		int currentMinute = currentMinute();
		int serialTime = currentHour*100+currentMinute;
		
		//如果是同一分钟生成，就需要序列号
		if (serialTime==this.currentTime) {
			//如果不超过maxSequence最大值，就会得到较小的值，即序列号加一，但要求maxSequence的二进制数都是1
			sequence = (sequence + 1) & maxSequence;
			if (sequence == 0) {//如果超过最大值，等待
				int[] result = nextMinute();
				currentHour = result[0];
				currentMinute = result[1];
				serialTime = result[2];
			}
		}else {//时间已经改变，重置序列号
			sequence = 0;
		}
		this.currentTime = serialTime;
		
		//流水号最后几位，确保位数固定
		int tailNumber = (currentHour << this.hourLeftRange)|(currentMinute << this.MinuteLeftRange)|sequence;
		String tail = String.valueOf(tailNumber);
		while (tail.length() < this.tailNumberLength) {
			tail = "0"+tail;
		}
		return this.firstLetter + todayDate() + tail;
	}
	
	/**
	 * 获取当天的日期字符串
	 */
	private String todayDate() {
		Calendar calendar = Calendar.getInstance();
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH) + 1;
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		StringBuffer buffer = new StringBuffer(String.valueOf(year));
		if (month < 10) {//个位数字
			buffer.append("0").append(String.valueOf(month));
		}else {
			buffer.append(String.valueOf(month));
		}
		if (day < 10) {//个位数字
			buffer.append("0").append(String.valueOf(day));
		}else {
			buffer.append(String.valueOf(day));
		}
		return buffer.toString();
	}
	
	/**
	 * 返回当前的小时
	 */
	private int currentHour() {
		Calendar calendar = Calendar.getInstance();
		return calendar.get(Calendar.HOUR_OF_DAY);
	}
	
	/**
	 * 获取当前的分钟
	 */
	private int currentMinute() {
		Calendar calendar = Calendar.getInstance();
		return calendar.get(Calendar.MINUTE);
	}
	
	/**等待下一分钟
	 * 如果正好在23:59的时候等待，需要判断是不是新的一天的开始
	 * @param currentTime
	 * @return
	 */
	private int[] nextMinute() {
		int[] result = new int[3];
		int currentHour = currentHour();
		int currentMinute = currentMinute();
		int serialTime = currentHour*100+currentMinute;
		//特殊时间，如果在晚上23点59分等待，刚好等到00点，将直接返回
		if (this.currentTime>2300 && this.currentTime<2360 && currentHour==0) {
			result[0] = currentHour;
			result[1] = currentMinute;
			result[2] = serialTime;
			return result;
		}
		while (serialTime <= this.currentTime) {
			currentHour = currentHour();
			currentMinute = currentMinute();
			serialTime = currentHour*100+currentMinute;
		}
		result[0] = currentHour;
		result[1] = currentMinute;
		result[2] = serialTime;
		return result;
	}
}
