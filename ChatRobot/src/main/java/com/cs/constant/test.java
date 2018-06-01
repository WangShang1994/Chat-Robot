package com.cs.constant;

import java.io.File;
import java.io.IOException;

public class test {

	public static void main(String[] args) throws IOException {
		File f = new File("D:\\weChatTest\\Login.jpg");
		System.out.println(f.delete());
		System.out.println(f.createNewFile());

	}

}
