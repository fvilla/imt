
package org.integratedmodelling.thinkscape.splash;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.splash.AbstractSplashHandler;
import com.swtdesigner.ResourceManager;

/**
 * @since 3.3
 * 
 */
public class InteractiveSplashHandler extends AbstractSplashHandler {
	
	private final static int F_LABEL_HORIZONTAL_INDENT = 175;

	private final static int F_BUTTON_WIDTH_HINT = 80;

	private final static int F_TEXT_WIDTH_HINT = 175;
	
	private final static int F_COLUMN_COUNT = 3;
	
	private Composite fCompositeLogin;
	private Composite fCompositeLogin_1;
	
	private Text fTextUsername;
	
	private Text fTextPassword;
	
	private Button fButtonOK;
	
	private Button fButtonCancel;
	
	private boolean fAuthenticated;
	
	/**
	 * 
	 */
	public InteractiveSplashHandler() {
		fCompositeLogin = null;
		fTextUsername = null;
		fTextPassword = null;
		fButtonOK = null;
		fButtonCancel = null;
		fAuthenticated = false;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.splash.AbstractSplashHandler#init(org.eclipse.swt.widgets.Shell)
	 */
	public void init(final Shell splash) {
		// Store the shell
		super.init(splash);
		// Configure the shell layout
		configureUISplash();
		// Create UI
		createUI();		
		// Create UI listeners
		createUIListeners();
		// Force the splash screen to layout
		splash.layout(true);
		// Keep the splash screen visible and prevent the RCP application from 
		// loading until the close button is clicked.
		doEventLoop();
	}
	
	/**
	 * 
	 */
	private void doEventLoop() {
		Shell splash = getSplash();
		while (fAuthenticated == false) {
			if (splash.getDisplay().readAndDispatch() == false) {
				splash.getDisplay().sleep();
			}
		}
	}

	/**
	 * 
	 */
	private void createUIListeners() {
		// Create the OK button listeners
		createUIListenersButtonOK();
		// Create the cancel button listeners
		createUIListenersButtonCancel();
	}

	/**
	 * 
	 */
	private void createUIListenersButtonCancel() {
		fButtonCancel.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				handleButtonCancelWidgetSelected();
			}
		});		
	}

	/**
	 * 
	 */
	private void handleButtonCancelWidgetSelected() {
		// Abort the loading of the RCP application
		getSplash().getDisplay().close();
		System.exit(0);		
	}
	
	/**
	 * 
	 */
	private void createUIListenersButtonOK() {
		fButtonOK.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				handleButtonOKWidgetSelected();
			}
		});				
	}

	/**
	 * 
	 */
	private void handleButtonOKWidgetSelected() {
		String username = fTextUsername.getText();
		String password = fTextPassword.getText();
		// Aunthentication is successful if a user provides any username and
		// any password
		if ((username.length() > 0) &&
				(password.length() > 0)) {
			fAuthenticated = true;
		} else {
			MessageDialog.openError(
					getSplash(),
					"Authentication Failed",  //$NON-NLS-1$
					"A username and password must be specified to login.");  //$NON-NLS-1$
		}
	}
	
	/**
	 * 
	 */
	private void createUI() {
		// Create the login panel
		createUICompositeLogin();
		// Create the blank spanner
		createUICompositeBlank();
		// Create the user name label
		createUILabelUserName();
		// Create the user name text widget
		createUITextUserName();
		// Create the password label
		createUILabelPassword();
		// Create the password text widget
		createUITextPassword();
		// Create the blank label
		createUILabelBlank();
		// Create the OK button
		createUIButtonOK();
		// Create the cancel button
		createUIButtonCancel();
	}		
	
	/**
	 * 
	 */
	private void createUIButtonCancel() {
		// Create the button
		fButtonCancel = new Button(fCompositeLogin_1, SWT.PUSH);
		fButtonCancel.setText("Cancel"); //$NON-NLS-1$
		// Configure layout data
		GridData data = new GridData(SWT.NONE, SWT.NONE, false, false);
		data.widthHint = F_BUTTON_WIDTH_HINT;	
		data.verticalIndent = 10;
		fButtonCancel.setLayoutData(data);
		new Label(fCompositeLogin_1, SWT.NONE);
	}

	/**
	 * 
	 */
	private void createUIButtonOK() {
		// Create the button
		fButtonOK = new Button(fCompositeLogin_1, SWT.PUSH);
		fButtonOK.setText("OK"); //$NON-NLS-1$
		// Configure layout data
		GridData data = new GridData(SWT.NONE, SWT.NONE, false, false);
		data.widthHint = F_BUTTON_WIDTH_HINT;
		data.verticalIndent = 10;
		fButtonOK.setLayoutData(data);
	}

	/**
	 * 
	 */
	private void createUILabelBlank() {
		new Label(fCompositeLogin_1, SWT.NONE);
		new Label(fCompositeLogin_1, SWT.NONE);
		new Label(fCompositeLogin_1, SWT.NONE);
		new Label(fCompositeLogin_1, SWT.NONE);
		Label label = new Label(fCompositeLogin_1, SWT.NONE);
		label.setVisible(false);
	}

	/**
	 * 
	 */
	private void createUITextPassword() {
		// Create the text widget
		int style = SWT.PASSWORD | SWT.BORDER;
	}

	/**
	 * 
	 */
	private void createUILabelPassword() {
		new Label(fCompositeLogin_1, SWT.NONE);
		// Create the label
		Label label_1 = new Label(fCompositeLogin_1, SWT.NONE);
		label_1.setText("&User Name:"); //$NON-NLS-1$
		// Configure layout data
		GridData gd_label_1 = new GridData();
		gd_label_1.horizontalIndent = F_LABEL_HORIZONTAL_INDENT;
		label_1.setLayoutData(gd_label_1);
		// Create the text widget
		fTextUsername = new Text(fCompositeLogin_1, SWT.BORDER);
		// Configure layout data
		GridData data_1 = new GridData(SWT.FILL, SWT.NONE, true, false);
		data_1.widthHint = F_TEXT_WIDTH_HINT;
		fTextUsername.setLayoutData(data_1);
		new Label(fCompositeLogin_1, SWT.NONE);
		new Label(fCompositeLogin_1, SWT.NONE);
		// Create the label
		Label label = new Label(fCompositeLogin_1, SWT.NONE);
		label.setText("&Password:"); //$NON-NLS-1$
		// Configure layout data
		GridData data = new GridData();
		data.horizontalIndent = F_LABEL_HORIZONTAL_INDENT;
		label.setLayoutData(data);
		fTextPassword = new Text(fCompositeLogin_1,  SWT.PASSWORD | SWT.BORDER);
		// Configure layout data
		GridData data_2 = new GridData(SWT.FILL, SWT.NONE, true, false);
		data_2.widthHint = F_TEXT_WIDTH_HINT;
		fTextPassword.setLayoutData(data_2);
	}

	/**
	 * 
	 */
	private void createUITextUserName() {
		new Label(fCompositeLogin_1, SWT.NONE);
	}

	/**
	 * 
	 */
	private void createUILabelUserName() {
	}

	/**
	 * 
	 */
	private void createUICompositeBlank() {
		Composite spanner = new Composite(fCompositeLogin_1, SWT.NONE);
		spanner.setLayout(new FillLayout(SWT.HORIZONTAL));
		GridData data = new GridData(SWT.FILL, SWT.FILL, true, true);
		data.horizontalSpan = 4;
		spanner.setLayoutData(data);
		
		Label label = new Label(spanner, SWT.NONE);
		label.setImage(ResourceManager.getPluginImage("org.integratedmodelling.thinkscape", "thinklab.png"));
	}

	/**
	 * 
	 */
	private void createUICompositeLogin() {
		// Create the composite
		fCompositeLogin_1 = new Composite(getSplash(), SWT.BORDER);
		GridLayout layout = new GridLayout(4, false);
		fCompositeLogin_1.setLayout(layout);		
	}

	/**
	 * 
	 */
	private void configureUISplash() {
		// Configure layout
		FillLayout layout = new FillLayout(); 
		getSplash().setLayout(layout);
		// Force shell to inherit the splash background
		getSplash().setBackgroundMode(SWT.INHERIT_DEFAULT);
	}
}
