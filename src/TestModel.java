import java.util.ArrayList;

import org.apache.log4j.Logger;

import df.bean.obj.util.JDate;
import df.bean.process.ProcessDischargeSummary;


public class TestModel {
	ModelTest t;
	ModelUser u;
	/*
	final static Logger logger = Logger.getLogger(TestModel.class);

	private boolean modelTest(){
		String st = "Start : "+JDate.getTime();
		t = new ModelTest();

		for(int i = 0; i<50000000; i++){
			//System.out.println(i);
			t = new ModelTest();
			t = new ModelTest();
			t.setCount(i+"");
		}
		System.out.println(st+" Finish : "+JDate.getTime()+" <> "+t.getCount());
		return true;
	}
	*/
	
	private void test(ArrayList<MasterModel> m){
		for(int i = 0; i<m.size(); i++){
			System.out.println(m.get(i));
		}
	}
	
	
	public static void main(String[] test){
		TestModel tm = new TestModel();
		System.out.println("Test");
		ArrayList<MasterModel> l = new ArrayList<MasterModel>();
		ModelTest t = new ModelTest();
		ModelUser u = new ModelUser();
				
		t.setCount("Thailand");
		u.setUserName("English");
		l.add(t);
		l.add(u);
		tm.test(l);
	}
}
