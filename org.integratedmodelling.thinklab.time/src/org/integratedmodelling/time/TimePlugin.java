package org.integratedmodelling.time;

import org.integratedmodelling.clojure.ClojureActivator;
import org.integratedmodelling.corescience.CoreScience;
import org.integratedmodelling.geospace.Geospace;
import org.integratedmodelling.thinklab.KnowledgeManager;
import org.integratedmodelling.thinklab.ThinklabActivator;
import org.integratedmodelling.thinklab.exception.ThinklabException;
import org.integratedmodelling.thinklab.exception.ThinklabPluginException;
import org.integratedmodelling.thinklab.interfaces.knowledge.IConcept;
import org.integratedmodelling.thinklab.interfaces.knowledge.IInstance;

public class TimePlugin extends ThinklabActivator {

	public static final String PLUGIN_ID = "org.integratedmodelling.thinklab.time";
	
	public static final String CONTINUOUS_TIME_OBSERVABLE_INSTANCE = "time:ContinuousTimeObservableInstance";
	public static final String ABSOLUTE_TIME_OBSERVABLE_INSTANCE = "time:AbsoluteTimeObservableInstance";

	public static final String TIME_OBSERVABLE_ID = "time:TemporalObservable";
	
	static public String DATETIME_TYPE_ID;
    static public String TIMERECORD_TYPE_ID;
    static public String TEMPORALGRID_TYPE_ID;
    static public String PERIOD_TYPE_ID;
    static public String DURATION_TYPE_ID;

    public static String STARTS_AT_PROPERTY_ID;
    public static String ENDS_AT_PROPERTY_ID;
    public static String STEP_SIZE_PROPERTY_ID;
    
    static private IConcept timeRecordConcept;
    static private IConcept dateTimeConcept;
    static private IConcept durationConcept;
    static private IConcept timeObservable;
    
    private static IInstance absoluteTimeInstance; 
    private static IInstance continuousTimeInstance; 
    
	public static TimePlugin get() {
		return (TimePlugin) getPlugin(PLUGIN_ID);
	}
	
	/* (non-Javadoc)
	 * @see org.integratedmodelling.thinklab.ThinklabActivator#preStart()
	 */
	@Override
	protected void preStart() throws Exception {
		// force core plugins to load
		ClojureActivator cj = ClojureActivator.get();
		IConcept c = CoreScience.Ranking();
		IConcept o = Geospace.RasterObservationSpace();
	}

	@Override
	protected void doStart() throws Exception {
		
		KnowledgeManager km = KnowledgeManager.get();
		
    	DATETIME_TYPE_ID = 
    		getProperties().getProperty("DateTimeTypeID", "time:DateTimeValue");
    	TIMERECORD_TYPE_ID = 
    		getProperties().getProperty("TemporalLocationRecordTypeID", "time:TemporalLocationRecord");
    	TEMPORALGRID_TYPE_ID = 
    		getProperties().getProperty("TemporalGridTypeID", "time:RegularTemporalGrid");
    	PERIOD_TYPE_ID = 
    		getProperties().getProperty("PeriodTypeID", "time:PeriodValue");
    	DURATION_TYPE_ID = 
    		getProperties().getProperty("DurationTypeID", "time:DurationValue");

    	STARTS_AT_PROPERTY_ID = 
    		getProperties().getProperty("StartsAtPropertyID", "time:startsAt");
    	ENDS_AT_PROPERTY_ID = 
    		getProperties().getProperty("EndsAtPropertyID", "time:endsAt");
    	STEP_SIZE_PROPERTY_ID = 
    		getProperties().getProperty("StepSizePropertyID", "time:inStepsOf");
    	
    	try {
    		
			timeRecordConcept = km.requireConcept(TIMERECORD_TYPE_ID);
			dateTimeConcept = km.requireConcept(DATETIME_TYPE_ID);
			durationConcept = km.requireConcept(DURATION_TYPE_ID);
			timeObservable = km.requireConcept(TIME_OBSERVABLE_ID);
			
			absoluteTimeInstance = km.requireInstance(ABSOLUTE_TIME_OBSERVABLE_INSTANCE);
			continuousTimeInstance = km.requireInstance(CONTINUOUS_TIME_OBSERVABLE_INSTANCE);
			
		} catch (ThinklabException e) {
			throw new ThinklabPluginException(e);
		}

	}

	@Override
	protected void doStop() throws Exception {
		// TODO Auto-generated method stub
		
	}

	public static IConcept TimeRecord() {
		return timeRecordConcept;
	}

	public static IConcept DateTime() {
		return dateTimeConcept;
	}
	
	public static IConcept Duration() {
		return durationConcept;
	}
	
	public static IInstance continuousTimeInstance() {
		return continuousTimeInstance;
	}

	public static IInstance absoluteTimeInstance() {
		return absoluteTimeInstance;
	}

	public IConcept TimeObservable() {
		return timeObservable;
	}


}
