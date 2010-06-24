package org.integratedmodelling.thinkscape;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

import org.eclipse.core.internal.resources.WorkspaceRoot;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.integratedmodelling.clojure.ClojureActivator;
import org.integratedmodelling.corescience.CoreScience;
import org.integratedmodelling.geospace.Geospace;
import org.integratedmodelling.modelling.ModelFactory;
import org.integratedmodelling.modelling.ModellingPlugin;
import org.integratedmodelling.thinklab.exception.ThinklabException;
import org.integratedmodelling.thinklab.exception.ThinklabRuntimeException;
import org.integratedmodelling.thinklab.exception.ThinklabValidationException;
import org.integratedmodelling.thinklab.interfaces.knowledge.IConcept;
import org.integratedmodelling.thinkscape.builder.ThinkscapeNature;
import org.integratedmodelling.thinkscape.project.ThinklabProject;
import org.integratedmodelling.time.TimePlugin;
import org.integratedmodelling.utils.Pair;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class ThinkScape extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "org.integratedmodelling.thinkscape";

	// The shared instance
	private static ThinkScape plugin;
	
	private Queue<Pair<Integer, Object>> messages = new LinkedList<Pair<Integer,Object>>();

	/**
	 * Dispatch relevant workspace events to views or any other listening objects.
	 */
	class DispatchingListener implements IResourceChangeListener {

		@Override
		public void resourceChanged(IResourceChangeEvent event) {
			
			IResourceDelta delta = event.getDelta();
			IResource resource = delta == null ? null : delta.getResource();
			System.out.println("EVENT: " + event.getType() + " CHANGE: " + delta + " ON " + resource);

			/*
			 * TODO decide what to notify
			 */
			if (resource instanceof WorkspaceRoot) {
				notifyPropertyChange(ThinkscapeEvent.WORKSPACE_CHANGE, resource);
			}
		}
	}

	/*
	 * we have an active project
	 */
	static ThinklabProject activeProject = null;
	
	private ArrayList<ThinklabProject> projects = new ArrayList<ThinklabProject>();

	private ArrayList<IPropertyChangeListener> listeners = new ArrayList<IPropertyChangeListener>();
	
	/**
	 * The constructor
	 */
	public ThinkScape() {
	}
	
	public static boolean isThinklabProject(IProject pfile) {
		try {
			return pfile.hasNature(ThinkscapeNature.NATURE_ID);
		} catch (CoreException e) {
		}
		return false;
	}

	
	
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
		setup();

	}

	private void setup() throws ThinklabException {
		
		// force core plugins to load
		ClojureActivator cj = ClojureActivator.get();
		IConcept c = CoreScience.Ranking();
		Geospace.get().ArealLocation();
		TimePlugin.DateTime();
		ModelFactory manager = ModellingPlugin.get().getModelManager();
		
		ResourcesPlugin.getWorkspace().addResourceChangeListener(
				new DispatchingListener(),
				IResourceChangeEvent.POST_BUILD);
		
		scanProjects();
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static ThinkScape getDefault() {
		return plugin;
	}

	public static void scanProjects() throws ThinklabException {
		
		ArrayList<IProject> tprojects = new ArrayList<IProject>();
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		
		try {
			root.refreshLocal(IWorkspaceRoot.DEPTH_INFINITE, new NullProgressMonitor());
		} catch (CoreException e) {
			throw new ThinklabValidationException(e);
		}

		for (IProject proj : root.getProjects()) {
			if (isThinklabProject(proj))
				tprojects.add(proj);
		}
		
		ArrayList<IProject> toadd = new ArrayList<IProject>();
		ArrayList<ThinklabProject> torem = new ArrayList<ThinklabProject>();
		
		for (IProject p : tprojects) {
			if (getProject(p.getName(), false) == null) {
				toadd.add(p);
			}
		}

		for (ThinklabProject pp : getDefault().projects) {
			for (IProject pro : tprojects) {
				if (getProject(pro.getName(), false) != null) {
					torem.add(pp);
				}
			}
		}
		
		for (ThinklabProject p : torem) {
			getDefault().projects.remove(p);
		}
		
		for (IProject p : toadd) {
			getDefault().projects.add(new ThinklabProject(p));
		}
	}

	public void addPropertyChangeListener(IPropertyChangeListener listener) {
		listeners.add(listener);
	}
	
	public void removePropertyChangeListener(IPropertyChangeListener listener) {
		listeners.remove(listener);
	}

	public void notifyPropertyChange(String type, Object data) {
		for (Iterator<IPropertyChangeListener> iter = listeners.iterator(); iter.hasNext();) {
			IPropertyChangeListener element = iter.next();
			element.propertyChange(new PropertyChangeEvent(this, type, null , data));		
		}
	}
	
	/**
	 * Returns an image descriptor for the image file at the given
	 * plug-in relative path
	 *
	 * @param path the path
	 * @return the image descriptor
	 */
	public static ImageDescriptor getImageDescriptor(String path) {
		return imageDescriptorFromPlugin(PLUGIN_ID, path);
	}

	public static ThinklabProject getActiveProject() {

		if (activeProject == null) {
			Collection<ThinklabProject> prjs = getProjects();

			if (prjs.size() > 0)
				// FIXME offer to select it if more than one.
				activeProject = prjs.iterator().next();
		}
		
		if (activeProject /* still */ == null) {	
			MessageDialog.openWarning(
				PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
				"Thinkscape Notification","No current project selected. Please create a project.");
		}
		
		return activeProject;
	}

	public static Collection<ThinklabProject> getProjects() {
		return getDefault().projects;
	}
	
	public static void addProject(String src) {
		
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		IProject proj = root.getProject(src);
		try {
			proj.create(null);
			activeProject = new ThinklabProject(proj);
			getDefault().notifyPropertyChange(ThinkscapeEvent.WORKSPACE_CHANGE, activeProject);
		} catch (Exception e) {
			throw new ThinklabRuntimeException(e);
		}
	}

	public static void enqueue(int type, Object data) {
		getDefault().messages.add(new Pair<Integer,Object>(type, data));
	}

	public static ThinklabProject getProject(String project, boolean activate) {

		for (ThinklabProject p : getProjects())
			if (p.getName().equals(project)) {
				if (activate
						&& (activeProject == null || !activeProject.getName()
								.equals(project))) {
					activeProject = p;
					getDefault().notifyPropertyChange(
							ThinkscapeEvent.PROJECT_ACTIVATED, p);
				}
				return p;
			}
		return null;
	}

	public static void setActiveProject(ThinklabProject project) {

		for (ThinklabProject p : getProjects())
			if (p.getName().equals(project)) {
				if (activeProject == null || !activeProject.getName().equals(project.getName())) {
					activeProject = p;
					getDefault().notifyPropertyChange(ThinkscapeEvent.PROJECT_ACTIVATED, p);
				}
			}
	}

}
