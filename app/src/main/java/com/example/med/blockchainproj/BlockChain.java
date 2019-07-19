package com.example.med.blockchainproj;

import com.example.med.blockchainproj.DAPP.utils;
import com.google.gson.Gson;

import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
public class BlockChain  implements Serializable{


	private List<Block> chain = new LinkedList<>() ;


	private Block previous_block =null;
	private float diff = 1;
	private long next_block_check = 2016;
	public BlockChain() {

	}




	public List<Block> getChain() {
		return chain;
	}




	public void setChain(List<Block> chain) {
		this.chain = chain;
	}




	public Block getPrevious_block() {
		return previous_block;
	}




	public void setPrevious_block(Block previous_block) {
		this.previous_block = previous_block;
	}




	public float getDiff() {
		return diff;
	}




	public void setDiff(float diff) {
		this.diff = diff;
	}




	public long getNext_block_check() {
		return next_block_check;
	}




	public void setNext_block_check(long next_block_check) {
		this.next_block_check = next_block_check;
	}




	public boolean check_block(Block b) {
		b.setdiff(this.diff);
		String v = b.hash_this_data(b.getProper());
		return b.diff_test(v);
	}

	public boolean add(Block b) {
		if (check_block(b)) {
			chain.add(b);
			previous_block =b;
			return true;
		}

		return false;
	}

	public void add_genesis(Block b) {
		chain.add(b);
		previous_block = b;

	}


	public void show() {
		for (int i =0;i< chain.size();i++)
			System.out.println(chain.get(i));
	}





	public float Update_difficulty() {
		//update difficulty

		//	System.out.println(previous_block.getdiff() +" : "+this.getLast2016TimeInSec());
		if (previous_block.getTrans_id() >= 2016 ) { // check if we passed 2016 block
			System.out.println("\ncalculating : "+ this.getLast2016TimeInSec() +  " :=> total time "+ (previous_block.getdiff() * 20160 ) /this.getLast2016TimeInSec() + " : previous :  " +findById(previous_block.getTrans_id()) + " : minux2  " + findById(previous_block.getTrans_id()-2016)  + "\n\n"  );
			//sc.next();
			int sign,perc;
			float value =  ((previous_block.getdiff() * 20160 ) /this.getLast2016TimeInSec());

			if (value > diff) {
				if (value > 4 ) {
					return previous_block.getdiff()+ 2; //can't go up more than a factor of 4
				}
				else if ( (int)value ==2) {
					return previous_block.getdiff()+ 2;
				}
				else if ( (int)value ==1) {
					return previous_block.getdiff()+ 1;
				}

				return value;
			}else if (value < diff) {
				if (value > utils.get_percent_of_value(previous_block.getdiff(), 25))
					return previous_block.getdiff() +utils.get_percent_of_value(previous_block.getdiff(), 25);
				else
					return previous_block.getdiff() + value;


			}
		}
		return  diff; //no update were made
	}
	private Iterator<Block> getIter() {
		return chain.iterator();
	}

	public long findById(long id) {
		Iterator<Block> e = getIter();
		Block tmp = null;
		while(e.hasNext()) {
			tmp = (Block) e.next();
			if (tmp.getTrans_id()==id)
				break;

		}
		return tmp.getTimestamp().getTime();
	}

	private int getLast2016TimeInSec(){
		return ((int) (findById(previous_block.getTrans_id()) - findById(previous_block.getTrans_id() - 2016)));

	}

	public static void main(String[] args) {
		Gson g = new Gson();

		BlockChain chain = new BlockChain();
		Block genesis = new Block(g.toJson("{" +
				"creater : mohamed amine dahmen," +
				"name : Genesis," +
				"email : m_amine22@outlook.com"
				+ "}"),1);
		genesis.setPrevious_Hash("000000000000000000000000");
		genesis.mine();

		chain.add_genesis(genesis);
		for (int i = 0; i<3000;i++) {
			Block c = new Block("{from :c ,to : s, amount :180}",chain.diff);
			c.setPrevious_Hash(chain.previous_block.getHash());
			c.mine();
			chain.add(c);


			Block e = new Block("{from :f ,to : e, amount :30}",chain.diff) ;
			e.setPrevious_Hash(chain.previous_block.getHash());
			e.mine();
			chain.add(e);


			if (chain.previous_block.getTrans_id() >= chain.next_block_check) {
				//Re-targeting
				chain.diff = chain.Update_difficulty();
				chain.setNext_block_check(chain.next_block_check+2016);
			}

			System.out.println(chain.diff);

		}
		//chain.show();




	}


}
