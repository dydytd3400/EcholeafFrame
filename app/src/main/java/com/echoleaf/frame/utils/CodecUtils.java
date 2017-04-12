package com.echoleaf.frame.utils;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;


/**
 * 加密、解密编解码综合工具类
 * 
 * @author 何常平
 * @version 1.0
 */
public class CodecUtils {

	public static class Hex {

		/**
		 * 用于建立十六进制字符的输出的小写字符数组
		 */
		private static final char[] DIGITS_LOWER = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
		/**
		 * 用于建立十六进制字符的输出的大写字符数组
		 */
		private static final char[] DIGITS_UPPER = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

		/**
		 * 将字节数组转换为十六进制字符串
		 *
		 * @param data byte[]
		 * @return 十六进制String
		 */
		public static String encodeHex(byte[] data ) {
			return encodeHex ( data, true );
		}

		/**
		 * 将字节数组转换为十六进制字符串
		 *
		 * @param data byte[]
		 * @param toLowerCase <code>true</code> 传换成小写格式 ， <code>false</code>
		 *        传换成大写格式
		 * @return 十六进制String
		 */
		public static String encodeHex(byte[] data, boolean toLowerCase ) {
			return encodeHex ( data, toLowerCase ? DIGITS_LOWER : DIGITS_UPPER );
		}

		/**
		 * 将字节数组转换为十六进制字符串
		 *
		 * @param data byte[]
		 * @param toDigits 用于控制输出的char[]
		 * @return 十六进制String
		 */
		protected static String encodeHex(byte[] data, char[] toDigits ) {
			int l = data.length;
			char[] out = new char[l << 1];
			for ( int i = 0, j = 0; i < l; i++ ) {
				out[j++] = toDigits[( 0xF0 & data[i] ) >>> 4];
				out[j++] = toDigits[0x0F & data[i]];
			}
			return new String( out );
		}

		/**
		 * 将十六进制字符数组转换为字节数组
		 *
		 * @param str String
		 * @return byte[]
		 * @throws RuntimeException 如果源十六进制字符数组是一个奇怪的长度，将抛出运行时异常
		 */
		public static byte[] decodeHex( String str ) {
			char[] data = str.toCharArray ();
			int len = data.length;
			if ( ( len & 0x01 ) != 0 ) {
				throw new RuntimeException( "Odd number of characters." );
			}
			byte[] out = new byte[len >> 1];
			for ( int i = 0, j = 0; j < len; i++ ) {
				int f = toDigit ( data[j], j ) << 4;
				j++;
				f = f | toDigit ( data[j], j );
				j++;
				out[i] = (byte) ( f & 0xFF );
			}
			return out;
		}

		/**
		 * 将十六进制字符转换成一个整数
		 *
		 * @param ch 十六进制char
		 * @param index 十六进制字符在字符数组中的位置
		 * @return 一个整数
		 * @throws RuntimeException 当ch不是一个合法的十六进制字符时，抛出运行时异常
		 */
		protected static int toDigit( char ch, int index ) {
			int digit = Character.digit ( ch, 16 );
			if ( digit == -1 ) {
				throw new RuntimeException( "Illegal hexadecimal character " + ch + " at index " + index );
			}
			return digit;
		}
	}

	/**
	 * DES工具
	 * 
	 * @author 何常平
	 * @version 1.0
	 */
	public static class DES {

		/**
		 * 加密字节数组
		 * 
		 * @param arrB 需加密的字节数组
		 * @return 加密后的字节数组
		 * @throws Exception
		 */
		public static byte[] encrypt( byte[] arrB, String password ) throws Exception {
			Key key = getKey ( password.getBytes () );
			Cipher encryptCipher = Cipher.getInstance ( "DES" );
			SecureRandom random = new SecureRandom();
			encryptCipher.init ( Cipher.ENCRYPT_MODE, key, random );
			return encryptCipher.doFinal ( arrB );
		}

		/**
		 * 加密字符串
		 * 
		 * @param strIn 需加密的字符串
		 * @return 加密后的字符串
		 * @throws Exception
		 */
		public static String encrypt(String strIn, String password ) {
			try {
				if ( StringUtils.isEmpty ( strIn ) ) {
					return strIn;
				}
				return Hex.encodeHex ( encrypt ( strIn.getBytes (), password ) );
			} catch ( Exception e ) {
				e.printStackTrace ();
			}
			return strIn;

		}

		/**
		 * 解密字节数组
		 * 
		 * @param arrB 需解密的字节数组
		 * @return 解密后的字节数组
		 * @throws Exception
		 */
		public static byte[] decrypt( byte[] arrB, String password ) throws Exception {
			Key key = getKey ( password.getBytes () );
			Cipher decryptCipher = Cipher.getInstance ( "DES" );
			SecureRandom random = new SecureRandom();
			decryptCipher.init ( Cipher.DECRYPT_MODE, key, random );
			return decryptCipher.doFinal ( arrB );
		}

		/**
		 * 解密字符串
		 * 
		 * @param strIn 需解密的字符串
		 * @return 解密后的字符串
		 * @throws Exception
		 */
		public static String decrypt(String strIn, String password ) throws Exception {
			if ( StringUtils.isEmpty ( strIn ) ) {
				return strIn;
			}
			return new String( decrypt ( Hex.decodeHex ( strIn ), password ) );
		}

		/**
		 * 从指定字符串生成密钥，密钥所需的字节数组长度为8位 不足8位时后面补0，超出8位只取前8位
		 * 
		 * @param arrBTmp 构成该字符串的字节数组
		 * @return 生成的密钥
		 * @throws Exception
		 */
		private static Key getKey(byte[] arrBTmp ) throws Exception {
			DESKeySpec desKey = new DESKeySpec( arrBTmp );
			// 创建一个密匙工厂，然后用它把DESKeySpec转换成
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance ( "DES" );
			SecretKey securekey = keyFactory.generateSecret ( desKey );
			return securekey;
		}
	}

	/**
	 * MD5工具
	 * 
	 * @author 何常平
	 * @version 1.0
	 */
	public static final class MD5 {

		/**
		 * Computes the MD5 hash of the data in the given input stream and
		 * returns it as an array of bytes.
		 */
		public static byte[] computeMD5Hash( InputStream is ) throws NoSuchAlgorithmException, IOException {
			BufferedInputStream bis = new BufferedInputStream( is );
			try {
				MessageDigest messageDigest = MessageDigest.getInstance ( "MD5" );
				byte[] buffer = new byte[16384];
				int bytesRead = -1;
				while (( bytesRead = bis.read ( buffer, 0, buffer.length ) ) != -1) {
					messageDigest.update ( buffer, 0, bytesRead );
				}
				return messageDigest.digest ();
			} finally {
				try {
					bis.close ();
				} catch ( Exception e ) {
					System.err.println ( "Unable to close input stream of hash candidate: " + e );
				}
			}
		}

		/**
		 * Computes the MD5 hash of the given data and returns it as an array of
		 * bytes.
		 */
		public static byte[] computeMD5Hash( byte[] data ) throws NoSuchAlgorithmException, IOException {
			return computeMD5Hash ( new ByteArrayInputStream( data ) );
		}

		public static String computeMD5HashToHex(byte[] data ) throws NoSuchAlgorithmException, IOException {
			return Hex.encodeHex ( computeMD5Hash ( data ) );
		}

		/**
		 * MD5
		 * 
		 * @param inStr
		 * @return
		 */
		public static String encode(String inStr ) {
			MessageDigest md5 = null;
			try {
				md5 = MessageDigest.getInstance ( "MD5" );
			} catch ( Exception e ) {
				e.printStackTrace ();
			}
			char[] charArray = inStr.toCharArray ();
			byte[] byteArray = new byte[charArray.length];

			for ( int i = 0; i < charArray.length; i++ )
				byteArray[i] = (byte) charArray[i];

			byte[] md5Bytes = md5.digest ( byteArray );

			StringBuffer hexValue = new StringBuffer();

			for ( int i = 0; i < md5Bytes.length; i++ ) {
				int val = ( (int) md5Bytes[i] ) & 0xff;
				if ( val < 16 )
					hexValue.append ( "0" );
				hexValue.append ( Integer.toHexString ( val ) );
			}

			return hexValue.toString ();
		}
	}

}
