package org.integratedmodelling.thinkscape.project;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Properties;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.integratedmodelling.modelling.Model;
import org.integratedmodelling.modelling.ModelFactory;
import org.integratedmodelling.modelling.ModelImportListener;
import org.integratedmodelling.modelling.ModellingPlugin;
import org.integratedmodelling.thinklab.annotation.DefaultAnnotationContainer;
import org.integratedmodelling.thinklab.annotation.SemanticAnnotationContainer;
import org.integratedmodelling.thinklab.annotation.SemanticAnnotationFactory;
import org.integratedmodelling.thinklab.exception.ThinklabException;
import org.integratedmodelling.thinklab.exception.ThinklabPluginException;
import org.integratedmodelling.thinklab.exception.ThinklabRuntimeException;
import org.integratedmodelling.thinklab.exception.ThinklabValidationException;
import org.integratedmodelling.thinklab.interfaces.knowledge.IConcept;
import org.integratedmodelling.thinkscape.ThinkScape;
import org.integratedmodelling.thinkscape.ThinkscapeEvent;
import org.integratedmodelling.thinkscape.annotation.ThinkscapeSemanticAnnotationContainer;
import org.integratedmodelling.thinkscape.builder.ThinkscapeNature;
import org.integratedmodelling.thinkscape.modeleditor.model.ModelNamespace;
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
	
	private Properties projectProperties = new Properties();
	
	private ArrayList<SemanticAnnotationContainer> sources = 
		new ArrayList<SemanticAnnotationContainer>();

	private ArrayList<SemanticAnnotationContainer> annotationNamespaces = 
		new ArrayList<SemanticAnnotationContainer>();

	private ArrayList<ModelNamespace> modelNamespaces = 
		new ArrayList<ModelNamespace>();
	
	private IFile propertiesFile;
	
	private String name;
	
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
			}
				/*
				 * create property file
				 */
			this.propertiesFile = project.getFile("THINKLAB-INF/thinklab.properties");
			if (!this.propertiesFile.exists()) {
				this.propertiesFile.create(new ByteArrayInputStream(new byte[0]), true, null);
			}

			/*
			 * read properties
			 */
			try {
				InputStream is = this.propertiesFile.getContents();
				projectProperties.load(is);
				is.close();
			} catch (IOException e1) {
				throw new ThinklabRuntimeException(e1);
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

			/*
			 * read up contents
			 */
			for (IResource r : this.annotPath.members()) {
				
				if (r instanceof IFile && r.toString().endsWith(".ann")) {
					try {
						ThinkscapeSemanticAnnotationContainer container = 
							new ThinkscapeSemanticAnnotationContainer(
								MiscUtilities.getURLBaseName(r.getLocationURI().toURL().toString()),
								(IFile)r);
						container.initialize();
						annotationNamespaces.add(container);
					} catch (MalformedURLException e) {
						throw new ThinklabValidationException(e);
					}
				}
			}

			/*
			 * TODO
			 * read up models
			 */
			for (IResource r : this.modelPath.members()) {
				
				if (r instanceof IFile && r.toString().endsWith(".model")) {
				}
			}

			/*
			 * TODO
			 * read up observables
			 */
			for (IResource r : this.modelPath.members()) {
				
				if (r instanceof IFile && r.toString().endsWith(".model")) {
				}
			}

			// TODO contexts and kboxes

			
		} catch (CoreException e) {
			throw new ThinklabPluginException(e);
		}
		
		this.name = project.getName();
		
		/*
		 * TODO ensure all dependent plugins are loaded
		 */
	}

	public ThinklabProject(IProject project) throws ThinklabException {
		this.project = project;
		initialize();
	}

	public String getLabel() {
		// TODO beautify this?
		return project.getName();
	}

	
	public boolean isActive() {
		boolean ret = false;
		
		if (ThinkScape.getActiveProject() != null)
			ret = ThinkScape.getActiveProject().getName().equals(getName());
		return ret;
	}

	public ModelNamespace getModelNamespace(String src) {

		for (ModelNamespace mn : modelNamespaces) {
			if (mn.getNamespace().equals(src))
				return mn;
		}
		
		String fn = "models" + "/" + MiscUtilities.getFileBaseName(src) + ".model";
		IFile file = project.getFile(fn);
		if (!file.exists()) {
			try {
				file.create(new ByteArrayInputStream(new byte[0]), true, new NullProgressMonitor());
			} catch (CoreException e) {
				throw new ThinklabRuntimeException(e);
			}
		}
		
		ModelNamespace ret = new ModelNamespace(src, file);
		modelNamespaces.add(ret);
		
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
				ret.create(
					new ByteArrayInputStream(("concept=" + src + "\n").getBytes()), 
					true, new NullProgressMonitor());
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

		projectProperties.setProperty(property, string);
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(propertiesFile.getName());
			projectProperties.store(out, null);
			out.close();
		} catch (Exception e) {
			throw new ThinklabRuntimeException(e);
		}
	}

	public Collection<SemanticAnnotationContainer> getSemanticSources() {
		return sources;
	}

	public String getName() {
		return name;
	}

	public SemanticAnnotationContainer getSemanticSource(String string) {
		
		SemanticAnnotationContainer ret = null;
		for (SemanticAnnotationContainer s : getSemanticSources())
			if (s.getSourceUrl().equals(string)) {
				ret = s;
				break;
			}
		return ret;
	}

	public Collection<SemanticAnnotationContainer> getAnnotationNamespaces() {
		return annotationNamespaces;
	}
	
	public SemanticAnnotationContainer getAnnotationNamespace(String namespace, boolean create) {
		SemanticAnnotationContainer ret = null;
		for (SemanticAnnotationContainer c : annotationNamespaces) {
			if (c.getNamespace().equals(namespace)) {
				ret = c;
				break;
			}
		}
		
		if (ret == null && create) {
			String fn = "annotations" + "/" + namespace + ".ann";
			IFile file = project.getFile(fn);
			if (!file.exists()) {
				try {
					file.create(new ByteArrayInputStream(new byte[0]), true, new NullProgressMonitor());
				} catch (CoreException e) {
					throw new ThinklabRuntimeException(e);
				}
			}
			ret = new ThinkscapeSemanticAnnotationContainer(namespace, file);
			InputStream inp;
			try {
				inp = file.getContents();
				ret.load(inp);
				inp.close();
			} catch (Exception e) {
				throw new ThinklabRuntimeException(e);
			}
			ThinkScape.getDefault().notifyPropertyChange(ThinkscapeEvent.ANNOTATION_NAMESPACE_CREATED, ret);
		}
		return ret;
	}

	public void importModel(File file) {

		final ArrayList<Model> models = new ArrayList<Model>();
		final ArrayList<String> znn = new ArrayList<String>();
		
		try {
	
			ModelFactory.get().importModel(file.toURI().toURL(),
					new ModelImportListener() {

						@Override
						public void modelImported(String namespace, Model model) {
							models.add(model);
							znn.add(namespace);
						}
					});

			if (models.size() > 0) {
				ModelNamespace mn = getModelNamespace(znn.get(0));
				for (Model m : models) {
					mn.addModel(m);
				}
				
				ThinkScape.getDefault().notifyPropertyChange(ThinkscapeEvent.MODEL_FILE_IMPORTED, mn);
			}
			
		} catch (Exception e) {
			throw new ThinklabRuntimeException(e);
		}
		
		
	}

	public void importAnnotations(String namespace, File file) {
		// TODO import annotations from xml file
		
	}

	public Collection<ModelNamespace> getModelNamespaces() {
		return modelNamespaces;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		return name.equals(obj);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return name.hashCode();
	}

	public void makeActive() {
		ThinkScape.setActiveProject(this);
	}
	
	
	
}
