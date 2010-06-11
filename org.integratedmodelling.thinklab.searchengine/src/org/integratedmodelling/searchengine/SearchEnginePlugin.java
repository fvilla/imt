package org.integratedmodelling.searchengine;

import java.util.ArrayList;
import java.util.Properties;

import org.integratedmodelling.thinklab.ThinklabActivator;
import org.integratedmodelling.thinklab.exception.ThinklabException;

public class SearchEnginePlugin extends ThinklabActivator {

	static final String PLUGIN_ID = "org.integratedmodelling.thinklab.searchengine";
	
	ArrayList<SearchEngine> engines = new ArrayList<SearchEngine>();
	SearchEngine defaultSearchEngine = null;
	
	/**
	 * Set to a true value to enable indexing of individuals contained in
	 * ontologies. Kbox indexing is not affected by this property.
	 */
	public static final String SEARCHENGINE_INDEX_INDIVIDUALS_PROPERTY = 
		"searchengine.index.individuals";
	
	/**
	 * Set to the path where you want the Lucene index to be created. Default
	 * is scratch path + /index.
	 */
	public static final String SEARCHENGINE_INDEX_PATH_PROPERTY = 
		"searchengine.index.path";
	
	/**
	 * If set to a true value, concepts without comments or labels are indexed using
	 * their id. Otherwise they're ignored. Default is false.
	 */
	public static final String SEARCHENGINE_INDEX_UNCOMMENTED_PROPERTY = 
		"searchengine.index.uncommented";
	
	/**
	 * Class to use for the analyzer; if not supplied, the standard
	 * Lucene analyzer (English) is used.
	 */
	public static final String SEARCHENGINE_ANALYZER_CLASS_PROPERTY = 
		"searchengine.analyzer.class";
	
	
	public static final String SEARCHENGINE_INDEX_TYPES_PROPERTY =
		"searchengine.index.types";
	
	/**
	 * Comma-separated list of kbox URLs that should be indexed
	 */
	public static final String SEARCHENGINE_KBOX_LIST_PROPERTY = 
		"searchengine.kbox";
	
	/**
	 * Ontologies listed here will be included unless "all" is one of the ontologies, then
	 * all will be included unless listed here with a ! in front of them.
	 */
	public static final String SEARCHENGINE_INDEX_ONTOLOGIES_PROPERTY = 
		"searchengine.index.ontologies";
	
	public static SearchEnginePlugin get() {
		return (SearchEnginePlugin) getPlugin(PLUGIN_ID );
	}
	
	/**
	 * Get your engine here, passing the necessary configuration properties. 
	 * 
	 * @param id
	 * @param properties
	 * @return
	 * @throws ThinklabException
	 */
	public SearchEngine createSearchEngine(String id, Properties properties) throws ThinklabException {
		
		SearchEngine engine = new SearchEngine(id, properties);
		engines.add(engine);
		return engine;
	}

	
	public SearchEngine getDefaultSearchEngine() throws ThinklabException {
		
		if (defaultSearchEngine == null) {
			
			Properties p = new Properties();
			p.setProperty(SEARCHENGINE_INDEX_UNCOMMENTED_PROPERTY, "true");
			defaultSearchEngine = new SearchEngine("thinkab-knowledge-index", p);
			defaultSearchEngine.addIndexField("rdfs:label", "text", 4.0);
			defaultSearchEngine.addIndexField("rdfs:comment", "text", 1.0);
		}
		
		return defaultSearchEngine;
	}
	

	public SearchEngine getSearchEngine(String string) {
		
		for (SearchEngine s : engines) {
			if (s.getID().equals(string))
				return s;
		}
		
		return null;
	}

	@Override
	protected void doStart() throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void doStop() throws Exception {
		engines.clear();
	}

}
