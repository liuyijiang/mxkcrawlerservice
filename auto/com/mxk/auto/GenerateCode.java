package com.mxk.auto;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.internal.DefaultShellCallback;

/**
 * 数据库操作代码生成
 * 2011-12-28 
 * version 0.01
 */
public class GenerateCode {

	public static void main(String[] args) throws Exception {
		new GenerateCode().generateMain();
	}

    public static String SCHEMA = null;

	/**
	 * 生成主数据代码
	 * @throws Exception
	 */
	public void generateMain() throws Exception {
//		SCHEMA = "HLWQKTEST";
//		generate(GenerateCode.class.getResource("generate_config_test.xml").getPath());
		//SCHEMA = "MXK";
		SCHEMA = "root";
		
		generate(GenerateCode.class.getResource("generate_config.xml").getPath());
//		SCHEMA = "HLWQKTYQK";
//		generate(GenerateCode.class.getResource("generate_config_tyst_tyqk.xml").getPath());
	}
	
	/**
	 * @param f
	 * @throws Exception
	 */
	public static void generate(String f) throws Exception {
		List<String> warnings = new ArrayList<String>();
		boolean overwrite = true;
		File configFile = new File(f);
		ConfigurationParser cp = new ConfigurationParser(warnings);
		Configuration config = cp.parseConfiguration(configFile);
		
		DefaultShellCallback callback = new DefaultShellCallback(overwrite);
		MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config, callback, warnings);
		myBatisGenerator.generate(null);
		for(String s : warnings) {
			System.err.println(s);
		}
	}

}
