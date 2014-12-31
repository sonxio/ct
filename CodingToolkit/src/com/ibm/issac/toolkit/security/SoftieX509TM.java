package com.ibm.issac.toolkit.security;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.X509TrustManager;

/**
 * 一个会信任任何证书的X509TrustManager
 * @author issac
 *
 */
public class SoftieX509TM implements X509TrustManager {
	public X509Certificate[] getAcceptedIssuers() {
		return null;
	}

	public void checkclientTrusted(X509Certificate[] certs, String authType) {
		// Leave blank to trust every client
	}

	public void checkServerTrusted(X509Certificate[] certs, String authType) {
		// Leave blank to trust every server
	}

	public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
	}
}
