/**
 * Shell.java
 * ----------------------------------------------------------------------------------
 * 
 * Copyright (C) 2008 www.integratedmodelling.org
 * Created: Jan 17, 2008
 *
 * ----------------------------------------------------------------------------------
 * This file is part of Thinklab.
 * 
 * Thinklab is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 * 
 * Thinklab is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with the software; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 * 
 * ----------------------------------------------------------------------------------
 * 
 * @copyright 2008 www.integratedmodelling.org
 * @author    Ferdinando Villa (fvilla@uvm.edu)
 * @author    Ioannis N. Athanasiadis (ioannis@athanasiadis.info)
 * @date      Jan 17, 2008
 * @license   http://www.gnu.org/licenses/gpl.txt GNU General Public License v3
 * @link      http://www.integratedmodelling.org
 **/
package org.integratedmodelling.thinklab.shell;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.apache.commons.io.FileUtils;
import org.integratedmodelling.thinklab.KnowledgeManager;
import org.integratedmodelling.thinklab.Thinklab;
import org.integratedmodelling.thinklab.command.Command;
import org.integratedmodelling.thinklab.command.CommandManager;
import org.integratedmodelling.thinklab.command.CommandParser;
import org.integratedmodelling.thinklab.configuration.LocalConfiguration;
import org.integratedmodelling.thinklab.exception.ThinklabException;
import org.integratedmodelling.thinklab.exception.ThinklabIOException;
import org.integratedmodelling.thinklab.exception.ThinklabRuntimeException;
import org.integratedmodelling.thinklab.interfaces.applications.ISession;
import org.integratedmodelling.thinklab.interfaces.applications.IUserModel;
import org.integratedmodelling.thinklab.interfaces.literals.IValue;
import org.integratedmodelling.thinklab.owlapi.Session;

import bsh.util.JConsole;

/**
 * A simple command-line driven interface, using the graphical BeanShell console.
 * 
 * @author Ferdinando Villa
 */
public class GraphicalShell {
	
	
	File historyFile = null;
	
	Font inputFont = new Font("Courier", Font.BOLD, 12);
	Font outputFont = new Font("Courier", Font.PLAIN, 12);

	public static String readLine(InputStream stream) throws ThinklabIOException {
		String ret = null;
		BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
		try {
			ret = reader.readLine();
		} catch (IOException e) {
			throw new ThinklabIOException(e);
		}
		return ret;
	}
	
	class REPLThread extends Thread {

		JConsole console = null;
		boolean error = false;
		ISession session;
		
		public class ConsoleUserModel implements IUserModel {

			@Override
			public InputStream getInputStream() {
				return console.getInputStream();
			}

			@Override
			public PrintStream getOutputStream() {
				return console.getOut();
			}

			@Override
			public void initialize(ISession session) {
				// TODO Auto-generated method stub
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
		
		public  void printStatusMessage() {

			console.println("ThinkLab shell v" + Thinklab.get().getVersion());
			console.println("System path: " + LocalConfiguration.getSystemPath());
			console.println("Data path: " + LocalConfiguration.getDataPath());					
			console.println();
			
			console.println("Enter \'help\' for a list of commands; \'exit\' quits");
			console.println();
		}
		
		public void run() {
					
			ConsolePanel jpanels = new ConsolePanel();
			this.console = jpanels.getConsole();
			try {
				this.session = new ConsoleSession();
			} catch (ThinklabException e1) {
				throw new ThinklabRuntimeException(e1);
			}

			/*
			 * read history if any
			 */
			List<?> lines = null;
			synchronized (historyFile) {
				try {
					lines = FileUtils.readLines(historyFile, null);
				} catch (IOException e) {
					// no problem
				}
			}
			
			if (lines != null) {
				for (Object line : lines) {
					console.addToHistory(line.toString());
				}
			}
			
			/*
			 * TODO
			 * privileged shell by default. We may want to condition this
			 * to authentication. For now all a privileged shell can do
			 * is to auto-annotate concepts. 
			 */
			KnowledgeManager.get().setAdminPrivileges(true);
			
			/* greet user */
			printStatusMessage();

			String input = "";
			boolean finished = false;
			
			/* define commands from user input */
			while(!finished) {
								
	            console.print("> ");
				console.setStyle(inputFont);
				
				// TODO change to console input stream
				try {
					input = readLine(session.getInputStream()).trim();
				} catch (ThinklabIOException e) {
					throw new ThinklabRuntimeException(e);
				}
				
				console.setStyle(outputFont);
				
				if ("exit".equals(input)) {
					console.println("shell terminated");
					finished = true;
					
				} else if (input.startsWith("!")) {
					
					String ss = input.substring(1);
					for (int i = console.getHistory().size(); i > 0; i--) {
						String s = console.getHistory().get(i-1);
						if (s.startsWith(ss)) {
							console.println(s);
							execute(s);
							break;
						}
					}
					
				} else if (!("".equals(input)) && /* WTF? */!input.equals(";")) {
					
					execute(input);
					
					// TODO see if we want to exclude commands that created errors.
					if (/*!error*/true) {
				          BufferedWriter bw = null;
					      try {
					        	 bw = new BufferedWriter(
					        			  new FileWriter(historyFile, true));
					          bw.write(input.trim());
					          bw.newLine();
					          bw.flush();
					       } catch (IOException ioe) {
					       } finally {
					 	 if (bw != null) 
					 		 try {
					 			 bw.close();
					 			 break;
					 	 	} catch (IOException ioe2) {
					 	 		break;
					 	 	}
					    }
					}
				}
				
			}
			
			this.session = null;
			this.console.setVisible(false);
			this.console = null;
			
		}

		private void execute(String input) {

			try {
				this.error = false;
				
				Command cmd = CommandParser.parse(input);
				
				if (cmd == null)
					return;
				
				IValue result = CommandManager.get().submitCommand(cmd, session);
	            if (result != null)
	                console.println(result.toString());
	            
	            console.getOut().flush();
	            
	            
			} catch (Exception e) {
				e.printStackTrace();
				this.error = true;
				console.println(">>> error: " + e.getMessage() + " <<<");
			}

		}

	}
	
	public class ConsolePanel extends JFrame {

		private static final long serialVersionUID = -1303258585100820402L;
		JConsole cons = null;

		public ConsolePanel() {

			super("Thinklab console");
			Container content = getContentPane();
			content.setBackground(Color.lightGray);
			JPanel controlArea = new JPanel(new GridLayout(2, 1));
			content.add(controlArea, BorderLayout.EAST);
			this.cons = new JConsole();
			this.cons.setFont(outputFont);
			// Preferred height is irrelevant, since using WEST region
			this.cons.setPreferredSize(new Dimension(600, 400));
			this.cons.setBorder(BorderFactory.createLineBorder(Color.blue, 2));
			this.cons.setBackground(Color.white);
			content.add(cons, BorderLayout.WEST);
			pack();
			setVisible(true);
		}

		public JConsole getConsole() {
			return cons;
		}
	}
	
	
	public GraphicalShell() throws ThinklabException {
		
		
		historyFile = 
			new File(
				Activator.get().getScratchPath() + 
				File.separator + 
				".history");
	}
	
	public void startConsole() {
		new REPLThread().run();
	}
}
