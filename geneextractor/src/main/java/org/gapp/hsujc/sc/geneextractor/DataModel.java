package org.gapp.hsujc.sc.geneextractor;

import java.util.ArrayList;

public class DataModel {
	//The only thread in the Model
	Thread curThread=null;
	
	//prepare listener for call back
	public interface DataModelListener{
		void showMessage(String _msg);
		
		/*
		 	@Override
			public void showMessage(String _msg) {
		
			Platform.runLater(new Runnable(){
			@Override
				public void run() {
					runtimeMsg.set(_msg);
				}
			});
			}
		 */
		
	}	
	private ArrayList<DataModelListener> listeners = new ArrayList<DataModelListener>();
	public void addListener(DataModelListener _lstn) {
		listeners.add(_lstn);
	}
	public void removeListener(DataModelListener _lstn) {
		listeners.remove(_lstn);
	}	
	public void removeAllListener() {
		listeners.clear();
	}
	
	//Call back those who listner the object
	void callListener(String _msg) {
		System.out.println(_msg);
		for(DataModelListener dml:listeners) {
			dml.showMessage(_msg);
		}
	}
	
	
	//internal class for run thread
	class LoadTask implements Runnable{
		
		//mission of the thread
		@Override
		public void run() {
			try {
				callListener("Start");
				Thread.sleep(3000);
				callListener("Step 1");
				Thread.sleep(3000);
				callListener("Step 2");
				Thread.sleep(3000);
				callListener("Step 3");
				Thread.sleep(3000);
				callListener("done");
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
	}
	
	//Run a thread task
	public void RunThread() {
		if(curThread != null && curThread.isAlive()) {
			System.out.println("Thread is occupied");
			return;
		}
		LoadTask task = new LoadTask();
		curThread=new Thread(task,"MyThread");
		curThread.start();
		
	}

}
