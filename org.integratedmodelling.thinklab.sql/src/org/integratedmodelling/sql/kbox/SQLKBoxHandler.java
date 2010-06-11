package org.integratedmodelling.sql.kbox;

import java.net.URI;
import java.util.Properties;

import org.integratedmodelling.sql.SQLKBox;
import org.integratedmodelling.thinklab.exception.ThinklabException;
import org.integratedmodelling.thinklab.exception.ThinklabStorageException;
import org.integratedmodelling.thinklab.extensions.IKBoxHandler;
import org.integratedmodelling.thinklab.interfaces.annotations.KBoxHandler;
import org.integratedmodelling.thinklab.interfaces.storage.IKBox;

@KBoxHandler(protocol="pg,mysql,hsql")
public class SQLKBoxHandler implements IKBoxHandler {


	public IKBox createKBox(String originalURI, String protocol, String dataUri, Properties properties) throws ThinklabException {
		
		if (protocol.equals("pg") || protocol.equals("hsqldb") || protocol.equals("mysql"))
			return new SQLKBox(originalURI, protocol, dataUri, properties);

		return null;
	}

	public IKBox createKBoxFromURL(URI url) throws ThinklabStorageException {
		throw new ThinklabStorageException("sql kboxes must be created with the kbox protocol");
	}
	

}
