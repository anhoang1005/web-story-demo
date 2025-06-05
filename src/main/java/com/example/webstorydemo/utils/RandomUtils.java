package com.example.webstorydemo.utils;

import java.security.SecureRandom;

public class RandomUtils {
	public static String temporaryPassword(int length) {
		String character = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
		StringBuilder password = new StringBuilder();
		SecureRandom random = new SecureRandom();
		
		for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(character.length());
            password.append(character.charAt(randomIndex));
        }
		return password.toString();
	}
	
	public static String verifyCode() {
		String character = "0123456789";
		StringBuilder password = new StringBuilder();
		SecureRandom random = new SecureRandom();
		
		for (int i = 0; i < 6; i++) {
            int randomIndex = random.nextInt(character.length());
            password.append(character.charAt(randomIndex));
        }
		return password.toString();
	}
}
