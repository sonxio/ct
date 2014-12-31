package com.ibm.issac.toolkit.license;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * 使用简单的方法来加强应用安全性的应用。
 * 
 * @author issac
 * 
 */
public abstract class AbstractPriliminaryLicenseEnforcedApplication {

	protected String getProductName(){
		return "IRE Family, (c)Copyright Song Jie";
	}

	/**
	 * 根据命令参数开始处理业务逻辑
	 * 
	 * @param args
	 */
	public abstract void proceed(String[] args);

	/**
	 * 所有IRE ACCESSER系列的应用都只需要一个参数，就是配置文件的名称
	 * 
	 * @param args
	 * @return
	 */
	protected String getPropertyFileNameFromCommandLineArguments(String[] args) {
		try {
			return args[0];
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Error occurred while trying to acquire configuration file name.");
		}
	}

	protected final void checkLicense() {
		// 加载和验证LICENSE文件
		final String requestCode = loadRequestCodeFromLicenseFile();
		boolean licenseValid = new LicenseValidation().isLicenseGranted("FlooredSqrtPattern", requestCode);
		if (!licenseValid) {
			throw new RuntimeException("License data is not valid. Operation aborted.");
		}
	}

	protected String loadRequestCodeFromLicenseFile() {
		final String licenseFileName = "ire_FR.lic";
		try {
			final FileReader reader = new FileReader(licenseFileName);
			final BufferedReader br = new BufferedReader(reader);
			String requestCode = br.readLine(); // 只读取第一行
			br.close();
			reader.close();
			return requestCode;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		throw new RuntimeException(licenseFileName + " can not be found in the current directory. The license file is required to run this program.");
	}
}
