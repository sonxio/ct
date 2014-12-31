package com.ibm.issac.toolkit.security;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyFactory;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Collection;

import com.ibm.issac.toolkit.file.FileUtility;
import com.ibm.issac.toolkit.logging.ColorLog;
import com.ibm.issac.toolkit.security.dto.ImportKeyDTO;

/**
 * ImportKey.java
 * 
 * <p>
 * This class imports a key and a certificate into a keystore (
 * <code>$home/keystore.ImportKey</code>). If the keystore is already present,
 * it is simply deleted. Both the key and the certificate file must be in
 * <code>DER</code>-format. The key must be encoded with <code>PKCS#8</code>
 * -format. The certificate must be encoded in <code>X.509</code>-format.
 * </p>
 * 
 * <p>
 * Key format:
 * </p>
 * <p>
 * <code>openssl pkcs8 -topk8 -nocrypt -in YOUR.KEY -out YOUR.KEY.der
 * -outform der</code>
 * </p>
 * <p>
 * Format of the certificate:
 * </p>
 * <p>
 * <code>openssl x509 -in YOUR.CERT -out YOUR.CERT.der -outform
 * der</code>
 * </p>
 * <p>
 * Import key and certificate:
 * </p>
 * <p>
 * <code>java comu.ImportKey YOUR.KEY.der YOUR.CERT.der</code>
 * </p>
 * <br />
 * 
 * <p>
 * <em>Caution:</em> the old <code>keystore.ImportKey</code>-file is deleted and
 * replaced with a keystore only containing <code>YOUR.KEY</code> and
 * <code>YOUR.CERT</code>. The keystore and the key has no password; they can be
 * set by the <code>keytool -keypasswd</code>-command for setting the key
 * password, and the <code>keytool -storepasswd</code>-command to set the
 * keystore password.
 * <p>
 * The key and the certificate is stored under the alias <code>importkey</code>;
 * to change this, use <code>keytool -keyclone</code>.
 * 
 * Created: Fri Apr 13 18:15:07 2001 Updated: Fri Apr 19 11:03:00 2002
 * 
 * @author Joachim Karrer, Jens Carlberg
 * @version 1.1
 **/
public class KeyManipulation {

	/**
	 * <p>
	 * Takes two file names for a key and the certificate for the key, and
	 * imports those into a keystore. Optionally it takes an alias for the key.
	 * <p>
	 * The first argument is the filename for the key. The key should be in
	 * PKCS8-format.
	 * <p>
	 * The second argument is the filename for the certificate for the key.
	 * <p>
	 * If a third argument is given it is used as the alias. If missing, the key
	 * is imported with the alias importkey
	 * <p>
	 * The name of the keystore file can be controlled by setting the keystore
	 * property (java -Dkeystore=mykeystore). If no name is given, the file is
	 * named <code>keystore.ImportKey</code> and placed in your home directory.
	 * 
	 **/
	public void importKey(ImportKeyDTO ikDto) {
		final String keypass = ikDto.getKeypass();
		final String defaultalias = ikDto.getDefaultalias();
		final String keystorename = ikDto.getKeystorename();
		final String keyfile = ikDto.getKeyFileName();
		final String certfile = ikDto.getCertFileName();

		// initializing and clearing keystore
		KeyStore ks;
		try {
			ks = KeyStore.getInstance("JKS", "SUN");
			ks.load(null, keypass.toCharArray());
			ColorLog.debug("Operating on keystore : " + keystorename);
			ks.store(new FileOutputStream(keystorename), keypass.toCharArray());
			ks.load(new FileInputStream(keystorename), keypass.toCharArray());

			// loading Key
			InputStream fl = FileUtility.fullStream(keyfile);
			byte[] key = new byte[fl.available()];
			KeyFactory kf = KeyFactory.getInstance("RSA");
			fl.read(key, 0, fl.available());
			fl.close();
			PKCS8EncodedKeySpec keysp = new PKCS8EncodedKeySpec(key);
			PrivateKey ff = kf.generatePrivate(keysp);

			// loading CertificateChain
			CertificateFactory cf = CertificateFactory.getInstance("X.509");
			InputStream certstream = FileUtility.fullStream(certfile);

			Collection c = cf.generateCertificates(certstream);
			Certificate[] certs = new Certificate[c.toArray().length];

			if (c.size() == 1) {
				certstream = FileUtility.fullStream(certfile);
				System.out.println("One certificate, no chain.");
				Certificate cert = cf.generateCertificate(certstream);
				certs[0] = cert;
			} else {
				System.out.println("Certificate chain length: " + c.size());
				certs = (Certificate[]) c.toArray();
			}

			// storing keystore
			ks.setKeyEntry(defaultalias, ff, keypass.toCharArray(), certs);
			ColorLog.info("Key and certificate stored.");
			ColorLog.debug("Alias:" + defaultalias + "  Password:" + keypass);
			ks.store(new FileOutputStream(keystorename), keypass.toCharArray());
		} catch (KeyStoreException e) {
			e.printStackTrace();
		} catch (NoSuchProviderException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (CertificateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
		}
	}

}
