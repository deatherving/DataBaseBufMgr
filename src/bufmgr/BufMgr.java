package bufmgr;


import global.Page;
import global.PageId;

public class BufMgr {


	public BufMgr(int numbufs, int lookAheadSize, String replacementPolicy) {

	}

	public void pinPage(PageId pageno, Page page, boolean emptyPage) throws BufferPoolExceededException{

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
