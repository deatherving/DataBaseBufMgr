package bufmgr;

import global.*;

public class discriptor {
	private PageId pageNumber;
	private int pinCount;
	private boolean dirtyPage;
	
	public discriptor(int pId){
		pinCount = 0;
		dirtyPage = false;
		pageNumber = new PageId();
		pageNumber.pid = pId;
	}
	
	public PageId getPageId(){
		return pageNumber;
	}
	
	public int getPinCount(){
		return pinCount;
	}
	
	public boolean getDirtyPage(){
		return dirtyPage;
	}
	
	public void setPage(int pId){
		pageNumber.pid = pId;
	}
	
	public void incPin(){
		++pinCount;
	}
	
	public void decPin(){
		if(pinCount == 0){
			System.out.println("Decrementing pincount fail, because the pinCount already equalls to 0");
			return;
		}
		--pinCount;
	}
	
	public void setDirty(boolean dirty){
		dirtyPage = dirty;
	}
	
	public void setPin(int num){
		pinCount = num;
	}
	
	
}
