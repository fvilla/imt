package org.integratedmodelling.thinkscape;

public interface ThinkscapeEvent {

	static public final String MODEL_FILE_IMPORTED = "import.file.model";
	static public final String ANNOTATION_FILE_IMPORTED = "import.file.annotation";
	static public final String ONTOLOGY_FILE_IMPORTED = "import.file.ontology";
	static public final String ANNOTATION_SOURCE_CONNECTED = "connect.annotation-source";
	static public final String PROJECT_CREATED = "create.project";
	static public final String PROJECT_ACTIVATED = "activate.project";
	public static final String WORKSPACE_CHANGE = "changed.workspace";
	public static final String ANNOTATION_CREATED = "create.annotation";
	public static final String ANNOTATION_NAMESPACE_CREATED = "create.annotation-namespace";
	
	public void notify(int event, Object data);
	
}
