package AMOSAlchemy;

public class AlchemyFactoryImpl extends IAlchemyFactory{

	public AlchemyFactoryImpl(){
		
	}

	@Override
	public IAlchemy createAlchemy(String apiKey) {
		return new AlchemyImpl(apiKey);
	}

	@Override
	public IAlchemyLanguage createAlchemyLanguage(String apiKey) {
		return new AlchemyLanguageImpl(apiKey);
	}
	
}
