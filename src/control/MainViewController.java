package control;

import java.io.File;
import java.io.PrintWriter;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;

import model.FileHelper;
import model.JmlGenerator;
import model.JmlModule;
import model.Module;
import view.MainView;


public class MainViewController {

	JFrame frame;
	JButton translate;
	JTextArea cafeText;
	JTextArea jmlText;
	FileHelper fh  ;
	MainView v ;
	
	
	public MainViewController(JFrame the_f){
		frame = the_f;
		translate = new JButton("Translate to JML");
		cafeText = new JTextArea();
		jmlText = new JTextArea();
		fh = null;
	}//end of consturctor
	
	
	
	public void makeView(){
		
		v = new MainView(frame, translate,new JTextArea("CafeOBJ Specification file"), setUpMenu());
		v.buildView();
		
		translate.addActionListener(event ->{
			
			if(fh!= null){
				
				fh.parseCafeFile();
				Vector<Module> mods = fh.getModules();
				
				JmlGenerator gen = new JmlGenerator(mods);
				String translation="";
				
				for(Module mod : mods){
					JmlModule jmod = new JmlModule(mod.getClassSort());
					translation += gen.translateSimpleModule(mod, jmod);
				
				}
				//File oldFile = fh.getFile();
				//if(!fh.readFileAsTxt().equals(cafeText.getText())){
				//	try {
				//		PrintWriter out = new PrintWriter(oldFile);
				//		out.print(cafeText.getText());
				//		out.close();
				//	} catch (Exception e) {e.printStackTrace();}
			//	}//end if the contents of the CafeOBJ pane changed
				jmlText = new JTextArea(translation);
				v.updateView(jmlText,true);
			}else JOptionPane.showMessageDialog(frame,
				    "Please use File > Open and select a CafeOBJ Specification File to translate");
		});//end of actionListener
		
		
		
		
	}//end of makeView
	
	
	public JMenuBar setUpMenu(){
		JMenuBar menuBar = new JMenuBar();;
		JMenu filemenu, aboutMenu;
		JMenuItem openItem, saveTransItem, aboutItem, exitItem;
		
		JFileChooser fc = new JFileChooser(); 
		
		
		openItem = new JMenuItem("Open File");
		saveTransItem = new JMenuItem("Save JML Translation");
		aboutItem = new JMenuItem("About Cafe2JML");
		exitItem = new JMenuItem("Exit");
		
		
		filemenu = new JMenu("File");
		aboutMenu = new JMenu("About");
		
		filemenu.add(openItem);
		filemenu.add(saveTransItem);
		filemenu.add(exitItem);
		aboutMenu.add(aboutItem);
		
		openItem.addActionListener(event ->{
			int choice = fc.showOpenDialog(frame);
			if(choice == JFileChooser.APPROVE_OPTION){
				File selectFile = fc.getSelectedFile();
				fh = new FileHelper(selectFile);
				fh.parseCafeFile();
				String cafeTxt = fh.readFileAsTxt();
				cafeText = new JTextArea(cafeTxt);
				v.updateView(cafeText,false);
			}//end if choice is APPROVE
		});//end of addActionListener
		
		
		exitItem.addActionListener(event->{
			frame.dispose();
		});
		
		
		

		saveTransItem.addActionListener(event ->{
			JTextArea athText = v.getTextArea(true);
			//System.out.println(athText.getText());
			int choice = fc.showSaveDialog(frame);
			if(choice == JFileChooser.APPROVE_OPTION){
				File selectFile = fc.getSelectedFile();
				//System.out.println(selectFile.getPath());
				try {
					PrintWriter out = new PrintWriter(selectFile);
					out.print(athText.getText());
					out.close();
				} catch (Exception e) {e.printStackTrace();}
			}//end if choice is APPROVE
			
		});//end of addActionListener
		
		
		
		
		aboutItem.addActionListener(event ->{
			 JOptionPane.showMessageDialog(frame,
					    "This program translates a CafeOBJ specification to an JML specification" + '\n' 
					    		+"It was developed by Nikos Triantafyllou, under partial funding" 
					    		+ '\n' +"from the Thalis program... and is distributed with the license GNU...");
		});
		
		
		menuBar.add(filemenu);
		menuBar.add(aboutMenu);
		return menuBar;
	}//end of setUpMenu
	
	
	
	
}//end of MainViewController
