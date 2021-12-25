package com.calc;

class StringCalculator {
	public static int add(String numbers) {
		if(numbers.equals("")){
			return 0;
		}
		return Integer.parseInt(numbers);
	}
}