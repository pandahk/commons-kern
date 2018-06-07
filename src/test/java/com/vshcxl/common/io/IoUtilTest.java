package com.vshcxl.common.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import org.junit.Test;

import cn.jszhan.commons.kern.apiext.io.IOUtil;

public class IoUtilTest {

	@Test
	public void slurp() {

		String ret = null;
		try {
			ret = IOUtil.slurp(new FileInputStream(new File("d:/a.txt")));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(ret);
	}

	@Test
	public void fileToProperties() {

		Properties prop = IOUtil.fileToProperties("/commons.properties", IoUtilTest.class);
		System.out.println(prop);

	}

	@Test
	public void fileToInputStream() {

		InputStream in = IOUtil.fileToInputStream("/commons.properties", IoUtilTest.class);
		try {
			String ret = IOUtil.slurp(in);
			System.out.println(ret);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void fileToPropertiesNull() {

		Properties prop = IOUtil.fileToProperties("commons.properties");
		System.out.println(prop);
	}

	@Test
	public void copyInToOut() {
		OutputStream os;
		InputStream is;
		try {
			is = new FileInputStream("d:/a.txt");
			os = new FileOutputStream("d:/bb.txt");
			long total;
			total = IOUtil.copyInToOut(is, os);

			System.out.println(total);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
