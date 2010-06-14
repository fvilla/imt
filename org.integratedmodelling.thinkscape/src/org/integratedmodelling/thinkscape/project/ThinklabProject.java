package org.integratedmodelling.thinkscape.project;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.runtime.CoreException;
import org.integratedmodelling.thinklab.exception.ThinklabException;
import org.integratedmodelling.thinklab.exception.ThinklabPluginException;
import org.integratedmodelling.thinkscape.builder.ThinkscapeNature;

public class ThinklabProject {

	IProject project = null;
	
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
			
			// add thinklab nature
			boolean hasNature = false;
			IProjectDescription desc = project.getDescription();
			String[] natures = new String[desc.getNatureIds().length + 1];
			int i = 0;
			for (String s : desc.getNatureIds()) {
				natures[i++] = s;
				if (s.equals(ThinkscapeNature.NATURE_ID))
					hasNature = true;
			}
			if (!hasNature) {
				natures[i] = ThinkscapeNature.NATURE_ID;			
				desc.setNatureIds(natures);
				project.setDescription(desc, null);
			}

		} catch (CoreException e) {
			throw new ThinklabPluginException(e);
		}
		
	}

	public ThinklabProject(IProject project) {
		this.project = project;
	}

	public String getLabel() {
		// TODO beautify this?
		return project.getName();
	}
	
}
