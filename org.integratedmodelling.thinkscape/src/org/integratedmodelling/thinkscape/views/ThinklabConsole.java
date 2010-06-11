package org.integratedmodelling.thinkscape.views;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;

import javax.swing.SwingUtilities;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import com.swtdesigner.SWTResourceManager;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.graphics.Color;
import org.integratedmodelling.thinklab.Thinklab;
import org.integratedmodelling.thinklab.command.Command;
import org.integratedmodelling.thinklab.command.CommandManager;
import org.integratedmodelling.thinklab.command.CommandParser;
import org.integratedmodelling.thinklab.exception.ThinklabException;
import org.integratedmodelling.thinklab.exception.ThinklabRuntimeException;
import org.integratedmodelling.thinklab.interfaces.applications.ISession;
import org.integratedmodelling.thinklab.interfaces.applications.IUserModel;
import org.integratedmodelling.thinklab.interfaces.literals.IValue;
import org.integratedmodelling.thinklab.owlapi.Session;
import org.eclipse.swt.widgets.Button;

public class ThinklabConsole extends ViewPart {

	public static final String ID = "org.integratedmodelling.thinkscape.views.ThinklabConsole"; //$NON-NLS-1$
	private Text text;
	private StyledText styledText;
	
	private StyleRange normalStyle;
	private StyleRange errorCommandStyle;
	private StyleRange successCommandStyle;	
	private StyleRange errorMessageStyle;
	
	private ConsoleSession session;

	public class CaptureOutputStream extends OutputStream {

		public StringBuffer text = new StringBuffer(512);
		
		@Override
		public void write(int b) throws IOException {
			text.append((char)b);
		}
		
		public void reset() {
			text = new StringBuffer(512);
		}
		
	}
	
	public class ConsoleUserModel implements IUserModel {

		PrintStream out = null;
		public CaptureOutputStream bytestream; 

		public ConsoleUserModel() {
			bytestream = new CaptureOutputStream();
			out = new PrintStream(bytestream);
		}
		
		@Override
		public InputStream getInputStream() {
			return null;
		}

		@Override
		public PrintStream getOutputStream() {
			return out;
		}

		@Override
		public void initialize(ISession session) {
		}
	}
	
	public class ConsoleSession extends Session {

		public ConsoleSession() throws ThinklabException {
			super();
		}

		@Override
		protected IUserModel createUserModel() {
			return new ConsoleUserModel();
		}
	}
	
	public ThinklabConsole() {
		
		
		try {
			this.session = new ConsoleSession();
			
			/*
			 * TODO populate history
			 */
			
		} catch (ThinklabException e) {
			throw new ThinklabRuntimeException(e);
		}
	}

	/**
	 * Create contents of the view part.
	 * @param parent
	 */
	@Override
	public void createPartControl(Composite parent) {
		GridLayout gl_parent = new GridLayout(1, false);
		gl_parent.marginWidth = 3;
		gl_parent.verticalSpacing = 3;
		parent.setLayout(gl_parent);
		
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		GridLayout gl_composite = new GridLayout(1, false);
		gl_composite.marginWidth = 0;
		gl_composite.marginHeight = 0;
		composite.setLayout(gl_composite);

		text = new Text(composite, SWT.BORDER);
		text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		text.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) 
			{
				// boh
				if (e.keyCode == java.awt.event.KeyEvent.VK_ENTER || 
					e.keyCode == 13) {
					execute(text.getText());
				} else if (e.keyCode == java.awt.event.KeyEvent.VK_UP) {
					historyUp();
				} else if (e.keyCode == java.awt.event.KeyEvent.VK_DOWN) {
					historyDown();
				}
				
			}
		});
		text.setBackground(SWTResourceManager.getColor(SWT.COLOR_INFO_BACKGROUND));
		text.setText("> ");
		this.text.setSelection(2);

		styledText = new StyledText(parent, SWT.BORDER);
		styledText.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.NORMAL));
		styledText.setLeftMargin(2);
		styledText.setDoubleClickEnabled(false);
		styledText.setEditable(true);
		styledText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		styledText.setTextLimit(10240);
		
		createActions();
		initializeToolBar();
		initializeMenu();
	}

	protected void historyDown() {
		// TODO Auto-generated method stub
		Thinklab.get().info("DOWN!");
	}

	protected void historyUp() {
		// TODO Auto-generated method stub
		Thinklab.get().info("UP!");
	}	

	protected void execute(String cmds) {
		
		cmds = cmds.trim();
		if (cmds.startsWith(">")) 
			cmds = cmds.substring(1).trim();

		try {			
			
			Command cmd = null;
			
			if (!cmds.equals(""))
				cmd = CommandParser.parse(cmds);
			
			if (cmd != null) {
			
				IValue result = CommandManager.get().submitCommand(cmd, session);			

				/* capture text */
				session.getUserModel().getOutputStream().flush();			
				String output = 
					((ConsoleUserModel)(session.getUserModel())).bytestream.text.toString();
				((ConsoleUserModel)(session.getUserModel())).bytestream.reset();
			
				appendText(cmds + "\n", java.awt.Color.GREEN, SWT.BOLD);
				appendText(output+"\n", java.awt.Color.BLACK, SWT.NORMAL);
			}
			
		} catch (Exception e) {			
			appendText(cmds + "\n", java.awt.Color.RED, SWT.BOLD);
			appendText(e+"\n", java.awt.Color.GRAY, SWT.ITALIC);
		} finally {
			this.text.setText("> ");
			this.text.setSelection(2);
		}
	}

	private void appendText(String text, java.awt.Color c, int bold) {
		
		Thinklab.get().info("setting: " + text);
		
		Color col = new Color(styledText.getDisplay(), c.getRed(), c.getGreen(), c.getBlue());
		int start = styledText.getText().length();
		styledText.append(text);
		styledText.setStyleRange(new StyleRange(start, text.length(), col, null, bold));
		styledText.setSelection(start+text.length());
		styledText.showSelection();
	}

	/**
	 * Create the actions.
	 */
	private void createActions() {
		// Create the actions
	}

	/**
	 * Initialize the toolbar.
	 */
	private void initializeToolBar() {
		IToolBarManager toolbarManager = getViewSite().getActionBars()
				.getToolBarManager();
	}

	/**
	 * Initialize the menu.
	 */
	private void initializeMenu() {
		IMenuManager menuManager = getViewSite().getActionBars()
				.getMenuManager();
	}

	@Override
	public void setFocus() {
		// Set the focus
	}
}
