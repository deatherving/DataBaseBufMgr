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
	
	public boolean remove(int key) {
		int hashedkey = hash(key);
		if(directory[hashedkey] != null) {
			return setKeyValue(hashedkey, key, -1);
		}
		return false;
	}
	
	private int hash(int key) {
		return (10 * key + 5) % directory.length;
	}
	
	public static void main(String[] args) {
		HashTable ht=new HashTable(512);
		ht.setKey(50, 10);
		ht.setKey(51, 20);
		ht.setKey(50, 50);
	
		ht.setKey(53, 10);
		ht.setKey(512, 20);
		ht.setKey(5123123, 50);
		
		ht.setKey(21, 20);
		ht.setKey(20, 50);
		ht.setKey(23, 10);
		ht.setKey(5212, 20);
		ht.setKey(2123123, 50);
		ht.setKey(121, 20);
		ht.setKey(30, 50);
		ht.setKey(13, 10);
		ht.setKey(1212, 20);
		ht.setKey(323123, 50);
		
		ht.setKey(1211, 20);
		ht.setKey(302, 50);
		ht.setKey(133, 10);
		ht.setKey(12132, 20);
		ht.setKey(3231523, 50);
		ht.setKey(323152, 50);
		System.out.println(ht.getKey(133));
		ht.remove(133);
		System.out.println(ht.getKey(133));
		ht.setKey(32315, 50);
		ht.setKey(32315, 30);
		System.out.println(ht);
		System.out.println(ht.getKey(32315));
		//System.out.println(ht);
	
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

