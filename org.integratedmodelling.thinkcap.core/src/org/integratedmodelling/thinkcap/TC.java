package org.integratedmodelling.thinkcap;

import org.integratedmodelling.thinkcap.core.Thinkcap;
import org.integratedmodelling.thinkcap.core.ThinkcapApplication;

/**
 * Utility class with simple static methods
 * @author Ferdinando Villa
 *
 */
public class TC {

	/**
	 * Thinkcap resource path: the relative URL path to a base thinkcap resource (e.g an icon) that
	 * will work across proxies. 
	 * 
	 * @param resource path to resource starting at thinkcap base dir. A leading slash is not necessary
	 *        but harmless.
	 */
	public static String url(ThinkcapApplication app, String resource) {
		return 
			resource == null ? 
				null :
				("/" + 
				 app.getId() +
				 "/tc" + 
				 (resource.startsWith("/") ? "" : "/") +
				 resource);
	}
}
