package AMOSAlchemy;

import com.ibm.watson.developer_cloud.service.BadRequestException;

public class Demo {

	public static void main(String[] args) {
		IAlchemyFactory fac = IAlchemyFactory.newInstance();
		IAlchemy service = fac.createAlchemy("<api-key>");
		try{
			System.out.println(service.getCompanyMainIndustry("Apple"));
		} catch(BadRequestException e){
			System.out.println("Could not find company main industry...");
		}
	}

}
