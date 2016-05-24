/*!
*
* AMOS-SS16-PROJ9
*
* Released under the AGPL license
* Date: 2016-05-25
*/
package AMOSAlchemy;

public abstract class IAlchemyFactory {
	public abstract IAlchemy createAlchemy(String apiKey);
	public abstract IAlchemyLanguage createAlchemyLanguage(String apiKey);
	public abstract IAlchemyToneAnalyzer createAlchemyToneAnalyzer(String username, String password);
	
	public static IAlchemyFactory newInstance(){
		return (IAlchemyFactory)new AlchemyFactoryImpl();
	}
}
