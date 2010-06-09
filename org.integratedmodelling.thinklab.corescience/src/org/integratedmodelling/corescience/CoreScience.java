package org.integratedmodelling.corescience;

import org.integratedmodelling.thinklab.ThinklabActivator;
import org.integratedmodelling.thinklab.interfaces.knowledge.IConcept;

public class CoreScience extends ThinklabActivator {

	private IConcept NumericRankingSpace;
	private IConcept DiscreteNumericRankingSpace;
	private IConcept MeasurementSpace;
	private IConcept MeasurementType;
	private IConcept RankingType;
	private IConcept CategorizationType;
	private IConcept RandomValueType;
	private IConcept ContinuousDistributionType;
	private IConcept DiscreteDistributionType;
	private IConcept ObservationType;
	
	// properties
	public static final String DEPENDS_ON = "observation:dependsOn";
	public static final String HAS_VALUE = "observation:value";
	public static final String HAS_DATASOURCE = "observation:hasDataSource";
	public static final String HAS_CONTINGENCY = "observation:isContingentTo";
	public static final String HAS_CONTEXT ="observation:hasObservationContext";
	public static final String HAS_EXTENT ="observation:hasObservationExtent";
	public static final String MEDIATES_OBSERVATION ="observation:mediates";
	public static final String HAS_OBSERVABLE = "observation:hasObservable";
	public static final String HAS_CONCEPTUAL_MODEL = "observation:hasConceptualModel";
	public static final String HAS_SOURCE_URI = "source:hasSourceURI";
	public static final String HAS_FILTER = "source:hasFilter";
	public static final String HAS_UNIT = "measurement:unit";
	public static final String HAS_CLASS_MAPPING = "observation:hasClassMapping";
	public static final String HAS_SOURCE_VALUE_TYPE = "observation:sourceValueType";
	public static final String HAS_CONCEPTUAL_SPACE = "observation:hasConceptSpace";
	public static final String HAS_SOURCE_VALUE = "observation:sourceValue";
	public static final String HAS_TARGET_CONCEPT = "observation:targetConcept";
	public static final String HAS_TARGET_INSTANCE = "observation:targetInstance";
	public static final String HAS_TARGET_CLASS = "observation:targetClass";
	public static final String HAS_INTERVAL = "observation:hasInterval";
	public static final String HAS_FORMAL_NAME = "observation:hasFormalName";
	public static final String HAS_SAME_CONTEXT_ANTECEDENT = "observation:hasSameContextAntecedent";
	public static final String DERIVED_FROM = "observation:derivedFrom";


	// concepts
	public static final String OBSERVATION = "observation:Observation";
	public static final String RANKING = "measurement:Ranking";
	public static final String CATEGORIZATION = "observation:Categorization";
	public static final String IDENTIFICATION = "observation:Identification";
	public static final String CLASSIFICATION = "observation:Classification";
	public static final String INDIRECT_OBSERVATION = "observation:IndirectObservation";
	public static final String EXCEL_CSV_DATASOURCE = "source:ExcelCSVDataSource";
	public static final String MEASUREMENT = "measurement:Measurement";
	public static final String COUNT = "measurement:Count";
	public static final String CSV_DATASOURCE = "source:CSVDataSource";
	public static final String RANDOM_DATASOURCE = "source:Randomizer";
	public static final String RANDOM_VALUE = "observation:RandomValue";
	public static final String RANDOM_OBSERVATION = "observation:RandomObservation";
	public static final String CONTINUOUS_DISTRIBUTION = "observation:ContinuousDistribution";
	public static final String DISCRETE_DISTRIBUTION = "observation:DiscreteDistribution";
	public static final String UNIT = "measurement:Unit";
	public static final String PARSED_STRING = "source:ParsedString";
	public static final String COLUMN_EXTRACTOR = "source:ColumnExtractor";
	public static final String PHYSICAL_PROPERTY = "representation:PhysicalProperty";
	public static final String EXTENSIVE_PHYSICAL_PROPERTY = "representation:ExtensivePhysicalProperty";
	public static final String INTENSIVE_PHYSICAL_PROPERTY = "representation:IntensivePhysicalProperty";
	public static final String EXTENT_OBSERVATION = "observation:ExtentObservation";
	public static final String DATASOURCE_FUNCTION_LITERAL = "source:hasFunctionLiteral";
	public static final String CLASSIFICATION_MODEL = "observation:ClassificationSpace";
	public static final String DISCRETE_RANKING_MODEL = "observation:DiscreteNumericRankingSpace";
	public static final String RANKING_MODEL = "observation:DiscreteNumericRankingSpace";
	public static final String CLASS_MAPPING = "observation:ClassMapping";
	public static final String CONTEXTUALIZED_DATASOURCE = "observation:ContextualizedDataSource";
	public static final String RANKING_SET_REMAPPER = "measurement:RankingSetRemapper";
	public static final String RANKING_INTERVAL_REMAPPER = "measurement:RankingIntervalRemapper";
	public static final String PROBABILISTIC_CLASSIFICATION = "observation:ProbabilisticClassification";
	public static final String STATELESS_MERGER_OBSERVATION = "observation:ContingencyMerger";
	public static final String BINARY_CODING = "measurement:BinaryCoding";
	public static final String NUMERIC_CODING = "measurement:NumericCoding";

	static final public String GENERIC_OBSERVABLE = "representation:GenericObservable";
	static final public String GENERIC_QUANTIFIABLE = "representation:GenericQuantifiable";

	public static final String CONTINGENT_TO = "observation:isContingentTo";

	public static CoreScience get() {
		return (CoreScience)(ThinklabActivator.get());
	}
	
	@Override
	protected void doStart() throws Exception {

		NumericRankingSpace = _km.requireConcept(RANKING_MODEL);
		DiscreteNumericRankingSpace = _km.requireConcept(DISCRETE_RANKING_MODEL);
		MeasurementSpace = _km.requireConcept(UNIT);
		RandomValueType = _km.requireConcept(RANDOM_VALUE);
		ContinuousDistributionType = _km.requireConcept(CONTINUOUS_DISTRIBUTION);
		DiscreteDistributionType = _km.requireConcept(DISCRETE_DISTRIBUTION);
		ObservationType = _km.requireConcept(OBSERVATION);
		MeasurementType = _km.requireConcept(MEASUREMENT);
		RankingType = _km.requireConcept(RANKING);
		CategorizationType = _km.requireConcept(CATEGORIZATION);
		
	}

	@Override
	protected void doStop() throws Exception {
		// TODO Auto-generated method stub
		
	}

	public static IConcept RankingModel() {
		return get().NumericRankingSpace;
	}
	
	public static IConcept DiscreteRankingModel() {
		return get().DiscreteNumericRankingSpace;
	}

	public static IConcept MeasurementModel() {
		return get().MeasurementSpace;
	}

	public static IConcept RandomValue() {
		return get().RandomValueType;
	}

	public static IConcept ContinuousDistribution() {
		return get().ContinuousDistributionType;
	}

	public static IConcept DiscreteDistribution() {
		return get().DiscreteDistributionType;
	}

	public static IConcept Observation() {
		return get().ObservationType;
	}

	public static IConcept Measurement() {
		return get().MeasurementType;
	}

	public static IConcept Ranking() {
		return get().RankingType;
	}

	public static IConcept Categorization() {
		return get().CategorizationType;
	}

}
