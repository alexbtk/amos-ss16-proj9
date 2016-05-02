package AMOSAlchemy;

import com.ibm.watson.developer_cloud.service.BadRequestException;

public class Demo {

	public static void main(String[] args) {
		IAlchemyFactory fac = IAlchemyFactory.newInstance();
		IAlchemy service = fac.createAlchemy("07e9cd2bcb99c18aa05cf79bf93a0e0f3a6bafaf");
		try{
			System.out.println("Main industry: " + service.getCompanyMainIndustry("Apple"));
			System.out.println("Main product: " + service.getCompanyMainProduct("Apple"));
		} catch(BadRequestException e){
			System.out.println("Could not find company...");
		}
	}

}
