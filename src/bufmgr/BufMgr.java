package bufmgr;

import java.util.Queue;
import java.util.Vector;
import java.util.Hashtable;
import java.util.Set;
import java.util.LinkedList;

import global.*;
import diskmgr.*;

import java.io.IOException;

public class BufMgr {
	private Page[] buffPool;
	private discriptor[] discriptors;
	private int numBuff;
	private int lookAheadSize;
	private String replacementPolicy;
	private Queue<Integer> emptyFrame;
	private Queue<Integer> flushFrame;
	private HashTable<Integer,Integer> directory;
	//Lirs contains a vector value, value[0] = RD, value[1] = R
	private Hashtable<Integer,Vector<Integer>> Lirs;

	public BufMgr(int numbufs, int lookAheadSize, String replacementPolicy){
		this.lookAheadSize = lookAheadSize;
		numBuff = numbufs;
		buffPool = new Page[numbufs];
		this.replacementPolicy = replacementPolicy;
		discriptors = new discriptor[numbufs];
		emptyFrame = new LinkedList<Integer>();
		flushFrame = new LinkedList<Integer>();
		directory = new HashTable<Integer,Integer>();
		Lirs = new Hashtable<Integer,Vector<Integer>>();
		for(int i = 0;i < numbufs;i++){
			emptyFrame.add(i);
		}
	}

	public void pinPage(PageId pageno, Page page, boolean emptyPage) throws BufferPoolExceededException{

	}

	public void unpinPage(PageId pageno, boolean dirty) throws HashEntryNotFoundException {

	}

	public PageId newPage(Page firstpage, int howmany) {
		PageId pid = new PageId();		

		return pid;
	}


	public void freePage(PageId globalPageId) throws PagePinnedException {

	}


	public void flushPage(PageId pageid) {


	}


	public void flushAllPages() {

	}


	public int getNumBuffers() {

		return 0;
	}

	public int getNumUnpinned() {


		return 0;
	}

}
