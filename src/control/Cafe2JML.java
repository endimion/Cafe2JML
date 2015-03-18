package control;

import java.awt.Dimension;
import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.UIManager;



public class Cafe2JML extends JFrame{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5104363095182829607L;
	private static Cafe2JML the_frame;


	public Cafe2JML(){
		super("Translator of CafeOBJ to JML Specifications -- NTUA");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	
	
	
	public static void main(String[] args){
		 try{ 
		        //UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel"); 
		 	UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		    }catch(Exception e){e.printStackTrace();}
		
		
		 
		 EventQueue.invokeLater(new Runnable() {
				@Override
				public void run() {
					the_frame = new Cafe2JML();
					Dimension d = new Dimension(800,600);
					the_frame.setMinimumSize(d);
					the_frame.setExtendedState(JFrame.MAXIMIZED_BOTH); 
					the_frame.setVisible(true); 
					
					MainViewController mvc = new MainViewController(the_frame);
					mvc.makeView();
				}//end of run
		});//end of invokeLater call
		 
	}//end of main
	
	
	
	
	
	
	

}//end of Cafe2Athena
