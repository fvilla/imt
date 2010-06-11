package org.integratedmodelling.sql;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.Properties;

import org.integratedmodelling.sql.hsql.HSQLServerConstructor;
import org.integratedmodelling.sql.mysql.MySQLServerConstructor;
import org.integratedmodelling.sql.postgres.PostgresSQLServerConstructor;
import org.integratedmodelling.thinklab.ThinklabActivator;
import org.integratedmodelling.thinklab.exception.ThinklabException;
import org.integratedmodelling.thinklab.exception.ThinklabIOException;
import org.integratedmodelling.thinklab.exception.ThinklabStorageException;

public class SQLPlugin extends ThinklabActivator {

	public File coreSchema = null;
	private HashMap<String, SQLServerConstructor> serverConstructors =
		new HashMap<String, SQLServerConstructor>();

	public static final String PLUGIN_ID = "org.integratedmodelling.thinklab.sql";
	
	public static SQLPlugin get() {
		return (SQLPlugin)getPlugin(PLUGIN_ID);
	}
	

	public URL getSchema(String schemaID) throws ThinklabException {
		
		URL r = getResourceURL(schemaID + ".sqx");
		if (r == null) {
			throw new ThinklabIOException("schema " + schemaID + " referenced in kbox is not installed");
		}	
		
		return r;
	}

	public void registerServerConstructor(String string, SQLServerConstructor serverConstructor) {
		serverConstructors.put(string, serverConstructor);	
	}

	@Override
	protected void doStart() throws Exception {
		
		/* register server types to be returned by createSQLServer() 
		 * FIXME this works but it's very old school, should be changed at some
		 * point.
		 */
		registerServerConstructor("hsql", new HSQLServerConstructor());
		registerServerConstructor("postgres", new PostgresSQLServerConstructor());
		registerServerConstructor("mysql", new MySQLServerConstructor());		
	}

	@Override
	protected void doStop() throws Exception {
		// TODO Auto-generated method stub
		
	}

	public SQLServer createSQLServer(String uri, Properties properties) throws ThinklabStorageException {

		SQLServer ret = null;
		URI u;
		try {
			u = new URI(uri);
		} catch (URISyntaxException e) {
			throw new ThinklabStorageException(e);
		}
		
		for (String s : serverConstructors.keySet()) {
			if (uri.startsWith(s)) {
				ret = serverConstructors.get(s).createServer(u, properties);
				break;
			}
		}
		
		if (ret == null)
			throw new ThinklabStorageException("SQL plugin: cannot create SQL server for URI " +
					uri);

		return ret;
	}



}
