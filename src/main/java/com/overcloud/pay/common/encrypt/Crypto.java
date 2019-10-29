package com.overcloud.pay.common.encrypt;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;

/**
 * 1) 提供了 TripleDES、AES、SHA1、MD5、BASE64等加解密、编解码工具;
 * 2) 可以在静态导入（import static）这个类，这样用起来更方便;
 */
public final class Crypto {
	
	public static final class TripleDES {
		public static final String encrypt(String data, String secretKey) {
			byte[] encrpyted = tripleDES(Cipher.ENCRYPT_MODE, data.getBytes(),
					secretKey.getBytes()); //3DES加密
			byte[] encoded = Base64.encodeBase64(encrpyted); // Base64编码
			return new String(encoded);
		}

		public static final String decrypt(String data, String secretKey) {
			byte[] decoded = Base64.decodeBase64(data); //Base64解码
			byte[] decrypted = tripleDES(Cipher.DECRYPT_MODE, decoded, secretKey.getBytes());//3DES解密
			return new String(decrypted);
		}	
		
		private static byte[] tripleDES(int opmode, byte[] data,
				byte[] secretKey) {
			return cipher("DESede", "DESede/CBC/PKCS5Padding", opmode, data, "01234567".getBytes(), secretKey);
		}
	}	
	

	public static final String SHA256(String data) {
		return DigestUtils.sha256Hex(data.getBytes());
	}
	
	public static final String SHA1(String data) {
		return DigestUtils.shaHex(data.getBytes());
	}

	public static final String MD5(String data) {
		return DigestUtils.md5Hex(data.getBytes());
	}

	public static final class BASE64 {
		public static final String encode(String data) {
			return Base64.encodeBase64String(data.getBytes());
		}

		public static final String decode(String data) {
			return new String(Base64.decodeBase64(data));
		}
	}	
	
	/**
	 * 通用的对称加密算法
	 * @param algorithm, 算法名称
	 * @param transformation, 算法名称/工作模式/填充模式
	 * @param opmode：Cipher.ENCRYPT_MODE和Cipher.DECRYPT_MODE
	 * @param data, 明文或密文数据
	 * @param iv, 初始化向量
	 * @param secretKey，密钥
	 * @return 加密或解密的结果
	 */
	private static final byte[] cipher(String algorithm, String transformation, int opmode, byte[] data, byte[] iv,
			byte[] secretKey) {
		try {
			// 转换密钥
			Key key = SecretKeyFactory.getInstance(algorithm)
					.generateSecret(new DESedeKeySpec(secretKey));
			// 转换初始化向量
			IvParameterSpec spec = new IvParameterSpec(iv);

			// 加密解密器
			Cipher cipher = Cipher.getInstance(transformation);
			cipher.init(opmode, key, spec);

			// 加密解密操作
			return cipher.doFinal(data);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
