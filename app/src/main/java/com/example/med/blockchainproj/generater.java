package com.example.med.blockchainproj;

import android.content.Context;
import android.util.Base64;

import com.example.med.blockchainproj.DB.DBOBJ.Keys_DB;

import org.apache.commons.codec.digest.DigestUtils;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

import static java.nio.charset.StandardCharsets.UTF_8;

public class generater {

	public  static KeyPair gpubprivlickey() throws InvalidKeySpecException {
		KeyPairGenerator kprgen;
		try {
			kprgen = KeyPairGenerator.getInstance("RSA");
			kprgen.initialize(2048);

			KeyPair kypair = kprgen.generateKeyPair();

			return kypair;


		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		return null;

	}

	public static byte[] encrypt(PublicKey pubk, String msg) throws Exception {
		Cipher ciph = Cipher.getInstance("RSA");
		ciph.init(Cipher.ENCRYPT_MODE, pubk);
		return ciph.doFinal(msg.getBytes());
	}

	public static byte[] decrypt(PrivateKey pubk, byte[] msg) throws Exception {
		Cipher ciph = Cipher.getInstance("RSA");
		ciph.init(Cipher.DECRYPT_MODE, pubk);
		return ciph.doFinal(msg);
	}

	public static void operations_generate(Context context, Keys_DB keys_db){
		try {
			generater g = new generater();
			KeyPair kypair = g.gpubprivlickey();
			PublicKey pubkey = kypair.getPublic();
			PrivateKey privateKey = kypair.getPrivate();
			String pubk = Base64.encodeToString(pubkey.getEncoded(),Base64.DEFAULT);
			String prik = Base64.encodeToString(privateKey.getEncoded(),Base64.DEFAULT);

			keys_db.save_keys(keys_db,prik,pubk,"user_keys");
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
		}
	}

	public static boolean verify(String plainText, String signature, String publicKey) throws Exception {


		byte [] pkcs8EncodedBytes = Base64.decode(publicKey, Base64.DEFAULT);
		X509EncodedKeySpec  keySpec = new X509EncodedKeySpec (pkcs8EncodedBytes);
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		PublicKey pubKey = keyFactory.generatePublic(keySpec);


		Signature publicSignature = Signature.getInstance("SHA256withRSA");
		publicSignature.initVerify(pubKey);
		publicSignature.update(plainText.getBytes(UTF_8));

		byte[] signatureBytes = Base64.decode(signature,Base64.DEFAULT);

		return publicSignature.verify(signatureBytes);


	}

	public static String getSig(String plainText ,String priv) throws InvalidKeyException, NoSuchAlgorithmException, SignatureException, InvalidKeySpecException, UnsupportedEncodingException {

		byte [] pkcs8EncodedBytes = Base64.decode(priv, Base64.DEFAULT);
		PKCS8EncodedKeySpec   keySpec = new PKCS8EncodedKeySpec(pkcs8EncodedBytes);
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		PrivateKey privateKey = keyFactory.generatePrivate(keySpec);

		Signature privateSignature = Signature.getInstance("SHA256withRSA");
		privateSignature.initSign(privateKey);
		privateSignature.update(plainText.getBytes(UTF_8));

		byte[] signature = privateSignature.sign();

		return Base64.encodeToString(signature,Base64.DEFAULT);

	}

	public static void main(String[] args) throws Exception {
		generater g = new generater();
		try {
			KeyPair kypair = g.gpubprivlickey();
			PublicKey pubkey = kypair.getPublic();
			PrivateKey privateKey = kypair.getPrivate();



			String msg = "hello my name is med";
			System.out.println("------------------ public key ----------------------");
			System.out.println(Base64.encodeToString(pubkey.getEncoded(),Base64.DEFAULT));


			System.out.println("\n------------------ private key ----------------------");
			System.out.println(Base64.encodeToString(privateKey.getEncoded(),Base64.DEFAULT));



			System.out.println("------------------ encrypy ----------------------");
			byte[] msg_enc = generater.encrypt(pubkey, msg);
			System.out.println(new String(msg_enc));

			System.out.println("------------------ decrypt ----------------------");
			System.out.println(new String(generater.decrypt(privateKey, msg_enc)));


			System.out.println(Base64.encodeToString(DigestUtils.sha256(Base64.encodeToString(privateKey.getEncoded(),Base64.DEFAULT)),Base64.DEFAULT));


			Signature sig = Signature.getInstance("NONEwithRSA");
			SecureRandom srec = new SecureRandom();
			sig.initSign(privateKey);

			byte[] data = "hello world ?".getBytes();
			sig.update(data);
			byte[] digitalSig = sig.sign();


			Signature sig2 = Signature.getInstance("NONEwithRSA");
			sig2.initVerify(pubkey);
			sig2.update(data);

			System.out.println("------------------ digital sig ----------------------");
			System.out.println(new String(digitalSig));

			System.out.println("------------------ Verification ----------------------");
			System.out.println(sig2.verify(digitalSig));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
