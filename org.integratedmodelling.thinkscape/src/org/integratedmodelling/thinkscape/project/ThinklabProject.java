package org.integratedmodelling.thinkscape.project;

import java.io.ByteArrayInputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.runtime.CoreException;
import org.integratedmodelling.modelling.ModellingPlugin;
import org.integratedmodelling.thinklab.annotation.SemanticAnnotationContainer;
import org.integratedmodelling.thinklab.annotation.SemanticAnnotationFactory;
import org.integratedmodelling.thinklab.exception.ThinklabException;
import org.integratedmodelling.thinklab.exception.ThinklabPluginException;
import org.integratedmodelling.thinklab.exception.ThinklabRuntimeException;
import org.integratedmodelling.thinklab.interfaces.knowledge.IConcept;
import org.integratedmodelling.thinkscape.ThinkScape;
import org.integratedmodelling.thinkscape.ThinkscapeEvent;
import org.integratedmodelling.thinkscape.builder.ThinkscapeNature;
import org.integratedmodelling.utils.MiscUtilities;

public class ThinklabProject {

	public IProject project = null;
	
	final String initialManifestContent = "Manifest-Version: 1.0\n" + 
			"Bundle-ManifestVersion: 2\n" + 
			"Bundle-Name: _NAME_\n" + 
			"Bundle-SymbolicName: _NAME_\n" + 
			"Bundle-Version: 1.0.0.qualifier\n" + 
			"Bundle-Vendor: integratedmodelling.org\n"; 
	
	// TODO add the rest
	final String initialPropertiesContent = "bin.includes = META-INF/,\\\n" + 
			"               .,\\\n" + 
			"               models/,\\\n" + 
			"               ontologies/,\\\n" + 
			"               annotations/,\\\n" + 
			"               THINKLAB-INF/\n";
	
	public IFolder modelPath;
	public IFolder configPath;
	public IFolder annotPath;
	public IFolder ontoPath;
	private IFolder metaPath;
	
	private ArrayList<SemanticAnnotationContainer> sources = 
		new ArrayList<SemanticAnnotationContainer>();
	
	public static void requireNature(IProject project, String nature) {

		boolean hasNature;
		try {
			hasNature = project.hasNature(nature);
			if (!hasNature) {
				IProjectDescription desc = project.getDescription();
				String[] natures = new String[desc.getNatureIds().length + 1];
				int i = 0;
				for (String s : desc.getNatureIds()) {
					natures[i++] = s;
				}
				natures[i] = nature;			
				desc.setNatureIds(natures);
				project.setDescription(desc, null);
			}
		} catch (CoreException e) {
			throw new ThinklabRuntimeException(e);
		}

	}
	
	public void initialize() throws ThinklabException  {
		
		boolean isNew = false;
		try {
			if  (!project.exists()) {
				project.create(null);
				isNew = true;
			}
			if (!project.isOpen()) {
				project.open(null);
			}

			this.modelPath = project.getFolder("models");
			this.configPath = project.getFolder("config");
			this.annotPath = project.getFolder("annotations");
			this.ontoPath = project.getFolder("ontologies");
			
			if (!this.modelPath.exists())	
				this.modelPath.create(true, true, null);
			if (!this.configPath.exists())	
				this.configPath.create(true, true, null);
			if (!this.annotPath.exists())	
				this.annotPath.create(true, true, null);
			if (!this.ontoPath.exists())	
				this.ontoPath.create(true, true, null);
			
			/*
			 * create Thinklab metadata
			 */
			this.metaPath = project.getFolder("THINKLAB-INF");
			if (!this.metaPath.exists()) {
				this.metaPath.create(true, true, null);
				/*
				 * create property file
				 */
				IFile prop = project.getFile("THINKLAB-INF/thinklab.properties");
				prop.create(new ByteArrayInputStream(new byte[0]), true, null);
			}

			/*
			 * create OSGI metadata, using dependency info (must be passed)
			 */
			this.metaPath = project.getFolder("META-INF");
			if (!this.metaPath.exists()) {
				this.metaPath.create(true, true, null);
				/*
				 * create OSGI property files
				 */
				String imf = initialManifestContent.replaceAll("_NAME_",project.getName());
				IFile prop = project.getFile("META-INF/MANIFEST.MF");
				prop.create(new ByteArrayInputStream(imf.getBytes()), true, null);
				prop = project.getFile("build.properties");
				prop.create(new ByteArrayInputStream(initialPropertiesContent.getBytes()), true, null);
			}

			// add necessary natures
			requireNature(project, "org.eclipse.pde.PluginNature");
			requireNature(project, ThinkscapeNature.NATURE_ID);

		} catch (CoreException e) {
			throw new ThinklabPluginException(e);
		}
	}

	public ThinklabProject(IProject project) throws ThinklabException {
		this.project = project;
		initialize();
	}

	public String getLabel() {
		// TODO beautify this?
		return project.getName();
	}

	public IFile getNewAnnotationFile(String src) {

		String fn = "annotations" + "/" + MiscUtilities.getFileBaseName(src) + ".ann";
		IFile ret = project.getFile(fn);
		if (!ret.exists()) {
			try {
				ret.create(new ByteArrayInputStream(new byte[0]), true, null);
			} catch (CoreException e) {
				throw new ThinklabRuntimeException(e);
			}
		}
		return ret;
	}
	
	public IFile getNewModelFile(String src) {

		String fn = "models" + "/" + MiscUtilities.getFileBaseName(src) + ".model";
		IFile ret = project.getFile(fn);
		if (!ret.exists()) {
			try {
				ret.create(new ByteArrayInputStream(new byte[0]), true, null);
			} catch (CoreException e) {
				throw new ThinklabRuntimeException(e);
			}
		}
		return ret;
	}
	
	public IFile getNewConceptFile(IConcept src) {

		String fn = 
			"ontologies" + "/" + 
			src.toString().toLowerCase().replace(':','_') + 
			".concept";

		IFile ret = project.getFile(fn);
		if (!ret.exists()) {
			try {
				ret.create(new ByteArrayInputStream(new byte[0]), true, null);
			} catch (CoreException e) {
				throw new ThinklabRuntimeException(e);
			}
		}
		return ret;
	}

	public void importNewSemanticSource(String typ, String src) {

		try {
			/*
			 * find the source importer and use it
			 */
			SemanticAnnotationContainer sc =
				SemanticAnnotationFactory.get().createAnnotationContainer(typ, new URL(src));

			/*
			 * add the source url and type to metadata
			 */
			addToMetadata(ModellingPlugin.SEMANTIC_ANNOTATION_SOURCES_PROPERTY, typ + "|" + src);
			
			sources.add(sc);
			
			/*
			 * notify added source so that we can update the views.
			 */
			ThinkScape.getDefault().notifyPropertyChange(
					ThinkscapeEvent.ANNOTATION_SOURCE_CONNECTED, this);
		
		} catch (Exception e) {
			throw new ThinklabRuntimeException(e);
		}
		
		
	}

	public void addToMetadata(String property, String string) {

		// TODO add string to content of given property, creating if not there, and save the
		// property file.
	}

	public Collection<SemanticAnnotationContainer> getSemanticSources() {
		return sources;
	}

	
}
