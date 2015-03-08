package bufmgr;

import java.math.BigInteger;
import java.util.LinkedList;


public class HashTable {
    private LinkedList<content> directory[];
	
	//Construct Hash Table
	public HashTable(int tablesize) {
		
		//I got a question, why don't we use dynamic hash table?????
		int i = 0;		
		
		BigInteger primenum = new BigInteger("" + tablesize);
		primenum = primenum.nextProbablePrime();
		
		System.out.printf("The Prime number is %d\n", primenum.intValue());
		
		directory = (LinkedList<content>[]) new LinkedList[primenum.intValue()];
		
		for(i = 0; i < directory.length; i++) {
			directory[i] = new LinkedList<content>();
		}
		
		size = primenum.intValue();
	}
	
	public boolean hasKey(int key) {
		return getKey(key) != -1?true:false;	
	}
	
	public void setKey(int key, int value) {
		int hashedkey = hash(key);
		
		if(hasKey(key)) {
			setKeyValue(hashedkey, key, value);
		}
		else {
			directory[hashedkey].add(new content(key, value));
		}
	}
	
	public int getKey(int key) {
		int hashedkey = hash(key);
		if(directory[hashedkey] != null) {
			return findKeyValue(hashedkey, key);
		}
		return -1;
	}
	
	private int findKeyValue(int hashedkey, int key) {
		for(content ct : directory[hashedkey]) {
			if(ct.key == key)
				return ct.value;
		}
		return -1;
	}
	
	private boolean setKeyValue(int hashedkey, int key, int value) {
		for(content ct: directory[hashedkey]) {
			if(ct.key == key) {
				ct.value = value;
				return true;
			}
		}
		return false;
	}
	
	private int hash(int key) {
		return (10 * key + 5) % directory.length;
	}
	
}

class content {
	int key;
	int value;
	
	public content(int k, int v) {
		key = k;
		value = v;	
	}
}
