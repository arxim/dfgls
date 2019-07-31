import org.apache.log4j.Logger;
import df.bean.obj.util.JDate;
import df.bean.process.ProcessDischargeSummary;


public class TestModel {
	ModelTest t;
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
	
	public static void main(String[] test){
		System.out.println("Test");
		logger.debug("LDAPLOGIN");
		//TestModel t = new TestModel();
		//t.modelTest();
	}

}
