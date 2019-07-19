package com.example.med.blockchainproj;

import com.example.med.blockchainproj.DAPP.utils;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.json.JSONObject;

import java.io.Serializable;
import java.security.spec.InvalidKeySpecException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("serial")
public class Block  implements Serializable {

	private  long trans_id;
	private static long next_id = 0;
	private String previous_Hash;
	private String nonce="";
	private String hash;
	private String data;
	private Timestamp timestamp ;
	private float diff = 1;
	//private BigInteger target = new BigInteger("28269553036454149273332760011886696253239742350009903329945699220681916415");
	private long max_nonce = 30000;



	public Block(String data,float diff) {
		this.data = data;
		this.diff = diff;
		this.trans_id = Block.next_id;
		Block.next_id +=1;
		this.timestamp = utils.getTimeStamp();
	}


	public String getPrevious_Hash() {
		return previous_Hash;
	}
	public void setPrevious_Hash(String previous_Hash) {
		this.previous_Hash = previous_Hash;
	}
	public String getNonce() {
		return nonce;
	}
	public void setNonce(String nonce) {
		this.nonce = nonce;
	}
	public String getHash() {
		if (this.hash==null)
			this.hash = hash_this_data(getProper());
		return hash;
	}
	public void setHash(String hash) {
		this.hash = hash;
	}
	public Date getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Date timestamp) {
		this.timestamp = utils.getTimeStamp();
	}

	public void setdiff(float diff) {
		this.diff = diff;
	}


	public float getdiff() {
		return this.diff;
	}


	@Override
	public String toString() {
		return getProper();
	}


	public  long getTrans_id() {
		return trans_id;
	}

	public void setTrans_id(long trans_id) {
		this.trans_id = trans_id;
	}



	public static long getNext_id() {
		return next_id;
	}



	public static void setNext_id(long next_id) {
		Block.next_id = next_id;
	}




	public String hash_this_data(String data) {
		return new String(Hex.encodeHex(DigestUtils.sha256(data.getBytes())));
	}
	public boolean diff_test(String data) {
		if ( data.substring(0, (int)this.diff).equals(n_zero((int)this.diff)) )
			return true;
		return false;
	}

	private String n_zero(int diff) {
		String tmp = "";
		for (int i=0;i<diff;i++)
			tmp += "0";
		return tmp;
	}

	public void mine() {
		int ne = 0 ;
		String ha ="";
		//System.out.println("mining started");
		while (true){
			this.nonce = String.valueOf(ne);
			ha = hash_this_data(getProper());
			if (diff_test(ha)) {
				//System.out.println(ha +" : " + ne + " : " + new BigInteger(ha,16));
				this.nonce =  String.valueOf(ne);
				break;
			}

			if (this.max_nonce < ne)
				this.nonce = "0";
			ne += 1;
		}
	}



	public String getProper() {
		Map<String,String> map = new HashMap<>();
		map.put("id", String.valueOf(trans_id));
		map.put("data", data.toString());
		map.put("nonce", nonce);
		map.put("diff", String.valueOf(diff));
		map.put("timestamp",String.valueOf(timestamp));
		if (previous_Hash !=null)
			try {
				map.put("previous_hash", previous_Hash);
			} catch (Exception e) {
				System.out.println("[?] you must set the previous hash first ");
			}

		return new JSONObject(map).toString();
	}


	public static void main(String[] args) throws InvalidKeySpecException {

	}


}
