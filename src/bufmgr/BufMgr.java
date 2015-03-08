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
	private HashTable directory;
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
		directory = new HashTable(numbufs);
		Lirs = new Hashtable<Integer,Vector<Integer>>();
		for(int i = 0;i < numbufs;i++){
			emptyFrame.add(i);
		}
	}

	
	public void createLirs(PageId pageno){
		Vector<Integer> temp = new Vector<Integer>();
		temp.add(Integer.MAX_VALUE);
		temp.add(0);
		Lirs.put(pageno.pid, temp);
	}
	
	public void updateLirs(PageId pageno){
		Set<Integer> keys = Lirs.keySet();
		for(Integer key:keys){
			if(key != pageno.pid){
				Vector<Integer> temp = new Vector<Integer>();
				temp = Lirs.get(key);
				if(temp.get(0) == Integer.MAX_VALUE){
					temp.set(1, temp.get(1) + 1);
				}
				else{
					temp.set(0, temp.get(0) + 1);
					temp.set(1, temp.get(1) + 1);
				}
				Lirs.put(pageno.pid, temp);
			}
			else{
				Vector<Integer> temp = new Vector<Integer>();
				temp = Lirs.get(key);
				temp.set(0, temp.get(1) + 1);
				temp.set(1, 0);
				Lirs.put(pageno.pid, temp);
			}
		}
	}
	
	public void removeLirs(PageId pageno){
		Lirs.remove(pageno.pid);
	}
	
	public void pinPage(PageId pageno, Page page, boolean emptyPage) throws BufferPoolExceededException{
		//if page is already in buffer pool, we just increase the pin count. In addition
		//we will remove this page from replacement candidates
		if(directory.hasKey(pageno.pid)){
			int loc = directory.getKey(pageno.pid);
			if(discriptors[loc].getPinCount() == 0){
				flushFrame.remove(loc);
				discriptors[loc].incPin();
			}
			else{
				discriptors[loc].incPin();
			}
			page.setpage(buffPool[loc].getpage());
			updateLirs(pageno);
			
			
		}
		//if page is not in buffer pool. Firstly we'd like to check if empty frame has available
		//space. If not we will get replacement policy
		else{
			Page weGet = new Page();
			int loc = -1;
            SystemDefs.JavabaseDB.read_page(new PageId(pageno.pid),page);
			if(!emptyFrame.isEmpty()){
				loc = emptyFrame.poll();
				page.setpage(buffPool[loc].getpage());
				flushFrame.add(loc);
				createLirs(pageno);
			}
			
			else{
				int maxWeight = 0;
				for (Integer x : flushFrame) { 
					int pid = discriptors[x].getPageId().pid;
					Vector<Integer> temp = new Vector<Integer>();
					temp = Lirs.get(pid);
					int maxTemp;
					if(temp.get(0) >= temp.get(1)){
						maxTemp = temp.get(0); 
					}
					else
						maxTemp = temp.get(1);
					
					if(maxWeight <= maxTemp){
						loc = x;
						maxWeight = maxTemp;
					}
				}
				if ((discriptors[loc] != null) && discriptors[loc].getDirtyPage()) {
                    flushPage(discriptors[loc].getPageId());
                    removeLirs(discriptors[loc].getPageId());
                    directory.remove(discriptors[loc].getPageId().pid);
                    createLirs(pageno);
               
				}
				
				
				
			}
			discriptors[loc].setDirty(false);
			discriptors[loc].setPage(pageno.pid);
			discriptors[loc].setPin(0);
			buffPool[loc] = weGet;
			page.setpage(buffPool[loc].getpage());
			directory.setKey(pageno.pid,loc);
			
		}
	}
	
	public void unpinPage(PageId pageno, boolean dirty) throws HashEntryNotFoundException {
		if(directory.hasKey(pageno.pid)) {
			int index = directory.getKey(pageno.pid);
			
			if(discriptors[index].getPinCount() == 0) {
				throw new PageUnpinnedException(null, "Page Unpin failed");
			}
			
			discriptors[index].setDirty(dirty);
			discriptors[index].decPin();
			
			if(discriptors[index].getPinCount() == 0) {
				flushFrame.add(index);
			}	
		}
	}

	public PageId newPage(Page firstpage, int howmany) {
		PageId pid = new PageId();		

		return pid;
	}


	public void freePage(PageId globalPageId) throws PagePinnedException {

	}


	public void flushPage(PageId pageid) {
		Page fpage = null;
		int index = getFrameIndex(pageid);
		if(buffPool[index] != null) {
			fpage = new Page(buffPool[index].getpage().clone());
		}
		try {
			if(fpage != null) {
				Minibase.DiskManager.write_page(pageid, fpage);
				discriptors[index].setDirty(false);
			} else {
				throw new HashEntryNotFoundException(null,"BUF_MNGR:HASH_ENTRY_NOT_FOUND_EXCEPTION");
			}
		}
		catch (Exception e){
			throw new DiskMgrException(null,"DB.java: flushPage() failed");
		}

	}


	public void flushAllPages() {
		int i = 0;
		
		for(i = 0; i < numBuff; i++) {
			if(discriptors[i] != null)
				flushPage(discriptors[i].getPageId());
		}
	}


	public int getNumBuffers() {
		return numBuff;
	}

	public int getNumUnpinned() {
		return emptyFrame.size() + flushFrame.size();
	}
	
	private int getFrameIndex(PageId pId) throws HashEntryNotFoundException {
		if(directory.hasKey(pId.pid))
			return directory.getKey(pId.pid);
		else{
			throw new HashEntryNotFoundException(null,"BUF_MNGR:HASH_ENTRY_NOT_FOUND_EXCEPTION");
		}
	}

}
