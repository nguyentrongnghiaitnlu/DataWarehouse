package Test;


import constants.Status;
import model.Log;
import model.Staging;

public class Test {

	public static void main(String[] args) throws Exception {
		Log log = new Log();
		log.getLog(Status.ER);
		Staging.loadToStaging(log);
	}
}
