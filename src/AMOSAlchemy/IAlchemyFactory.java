package AMOSAlchemy;

public abstract class IAlchemyFactory {
	public abstract IAlchemy createAlchemy(String apiKey);
	public abstract IAlchemyLanguage createAlchemyLanguage(String apiKey);
	
	public static IAlchemyFactory newInstance(){
		return (IAlchemyFactory)new AlchemyFactoryImpl();
	}
}
