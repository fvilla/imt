package org.integratedmodelling.thinkscape.project;

import java.io.ByteArrayInputStream;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.runtime.CoreException;
import org.integratedmodelling.thinklab.exception.ThinklabException;
import org.integratedmodelling.thinklab.exception.ThinklabPluginException;
import org.integratedmodelling.thinklab.exception.ThinklabRuntimeException;
import org.integratedmodelling.thinklab.interfaces.knowledge.IConcept;
import org.integratedmodelling.thinkscape.builder.ThinkscapeNature;
import org.integratedmodelling.utils.MiscUtilities;

public class ThinklabProject {

	public IProject project = null;
	
	public IFolder modelPath;
	public IFolder configPath;
	public IFolder annotPath;
	public IFolder ontoPath;
	
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
			
			// add thinklab nature if necessary
			boolean hasNature = project.hasNature(ThinkscapeNature.NATURE_ID);
			if (!hasNature) {
				IProjectDescription desc = project.getDescription();
				String[] natures = new String[desc.getNatureIds().length + 1];
				int i = 0;
				for (String s : desc.getNatureIds()) {
					natures[i++] = s;
				}
				natures[i] = ThinkscapeNature.NATURE_ID;			
				desc.setNatureIds(natures);
				project.setDescription(desc, null);
			}

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

	
}
