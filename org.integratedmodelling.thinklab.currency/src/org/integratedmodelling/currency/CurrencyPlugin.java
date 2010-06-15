package org.integratedmodelling.currency;

import java.net.URL;

import org.integratedmodelling.currency.cpi.CpiConversionFactory;
import org.integratedmodelling.sql.SQLPlugin;
import org.integratedmodelling.sql.SQLServer;
import org.integratedmodelling.thinklab.ThinklabActivator;

public class CurrencyPlugin extends ThinklabActivator {


	CpiConversionFactory convFactory = null;
	URL cpiDataURL = null;
	URL exchangeDataURL = null;
	
	static final public String ID = "org.integratedmodelling.thinklab.currency";
	public static final String MONETARY_VALUE_OBSERVATION = "currency:MonetaryValuation";
	public static final String MONETARY_VALUE_OBSERVABLE = "economics:EconomicValue";
	
	public CpiConversionFactory getConverter() {
		return convFactory;
	}
	
	public static CurrencyPlugin get() {
		return (CurrencyPlugin) getPlugin(ID);
	}

	@Override
	protected void doStart() throws Exception {
	
		cpiDataURL = this.getResourceURL("data/cpidata.txt");
		exchangeDataURL = this.getResourceURL("data/exchrates.txt");

		String db = getProperties().getProperty("currency.database", "hsqlmem://sa@localhost/currency");
		
		info("currency database is " + db);

		/**
		 * Create a database to hold the currency data using the given
		 * database from property, defaulting to HSQLDB in-memory. Any db-relevant properties
		 * can be put in currency.properties.
		 */
		SQLServer sqls = SQLPlugin.get().createSQLServer(db, getProperties());

		convFactory = new CpiConversionFactory();
		convFactory.initialize(
				sqls,
				exchangeDataURL, 
				cpiDataURL);
	}

	@Override
	protected void doStop() throws Exception {
		// TODO Auto-generated method stub
		
	}


}
