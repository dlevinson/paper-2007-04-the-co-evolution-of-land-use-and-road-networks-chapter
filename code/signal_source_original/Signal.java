	
import java.io.*;
import java.awt.*;
import java.applet.*;
import java.awt.event.*;
import java.net.*;
import javax.swing.*;
import java.text.*;

public class Signal extends Applet  implements ActionListener,ItemListener,WindowListener
{
	
//**Revision based on 1.7.1, the last version for ISTTT by August 1, 2006	
	
	
///land use size=network size
	
///layout of the simulator interface is:
///menuframe: the whole window;
///variablesPanel(left);
///DrawArea(right-up);
///DrawPanel(right-down),including legend, status bar and buttons.

	URL url=null;
	URL helpurl=null;
	public String currentInputFile;
	Frame menuframe;
	VariablesPanel vp;
	public DrawArea da;
	public DrawPanel dp;
	public NetworkDynamics nd;

	
	boolean  graphRead = false;
	boolean  evolved = false;
	int  drawAttribute = 0;
	
	public String getnetwork;
	public Frame f;
	public TextArea stat;
	MenuBar mbar,fmbar;
	public int edges;
	
	int Scale=0;	//convert the coordinates of nodes to pixels
	int Transx_node=0,Transy_node=0;	// translation

	int stepsize;
	
	public void init() {

		url=getCodeBase();
		vp = new VariablesPanel();
		dp = new DrawPanel(this);
		da = new DrawArea( dp );
		getnetwork="10X10 Grid Network";

		WindowDestroyer windowKiller=new WindowDestroyer();


	//Define the main window
		menuframe = new MenuFrame("SIGNAL1.8.0: Simulator of Integrated Growth of Network And Land use",this )  ;
		//define the size of menuframe according to the screen size
		Dimension screensize = getToolkit().getScreenSize();

		menuframe.setLayout(new BorderLayout());
		menuframe.add("West", vp);
		menuframe.add("Center", da);

		menuframe.addWindowListener(this);
		menuframe.setSize ((int)(1.0*screensize.width),(int)(0.99*screensize.height));

		menuframe.setVisible(true);
		//define the menu
		mbar = new MenuBar();
		menuframe.setMenuBar(mbar);
		Menu signal = new Menu("SIGNAL");
		Menu help=new Menu("Help");

		MenuItem  evolve1,batch1,quit,about,instruction;
		signal.add(evolve1 = new MenuItem("Evolve "));
		signal.add(batch1 = new MenuItem("Batch "));
		signal.add(quit = new MenuItem("Quit"));
		help.add(instruction = new MenuItem("Instructions"));
		//help.add(about=new MenuItem("About Song1.0"));

		mbar.add(signal);
		mbar.add (help);

		evolve1.addActionListener(this);
		batch1.addActionListener(this);
		quit.addActionListener(this);
		instruction.addActionListener(this);

	//Define the investmentmodels window


	//Define the result window
		f=new Frame("Statistics");
		stat=new TextArea("");


///load the 10*10 network when the window is opened
		vp.network.select("10X10 Grid Network" );
		dp.showStatus.setText("10X10 Grid Network Loaded...");
		edges=9800;
		
		stepsize=1;

		dp.evolve.setEnabled(true) ;
		//dp.statistics .setEnabled(false);
		dp.go.setEnabled( false);
		//currentInputFile = "cGrid_5.txt";
		//currentInputFile = "hexagon_15.txt";
		//currentInputFile = "Indiana.txt";
		currentInputFile = "Grid10.txt";
		
		try {
			nd = new NetworkDynamics( vp.variables,url, currentInputFile);
		} catch (IOException e) {
		}
		
		dp.first.setEnabled(false);
		dp.previous .setEnabled(false);
		dp.next .setEnabled(false) ;
		dp.last.setEnabled(false) ;
		//da.dp.slide .setEnabled( true);
		//dp.statistics .setEnabled( false);
		dp.go.setEnabled( false);
		dp.whichAttribute.setEnabled(false) ;
		dp.scale .setEnabled( false);

		da.setMapVariables();
		graphRead = true;
		evolved = false;
		da.currentYear = 0;
		da.repaint();
///////////

	}




	public void paint( Graphics g ) {
	}


///define the events related to window
	public void windowClosing(WindowEvent e){
		Object obj = e.getSource();
		if(obj.equals( menuframe))menuframe.dispose() ;
		else if (obj.equals( f))f.dispose() ;
		//else if (obj.equals( vp.investmentmodels))vp.investmentmodels.dispose() ;
	}

	public void windowOpened(WindowEvent e){
		da.setVisible(true) ;

	}

	public void windowActivated(WindowEvent e){

		da.repaint() ;
	}

	public void windowDeactivated(WindowEvent e){

		da.repaint() ;
	}

	public void windowIconified(WindowEvent e){

		da.repaint() ;
	}

	public void windowDeiconified(WindowEvent e){

		da.repaint() ;
	}

	public void windowClosed(WindowEvent e){


	}
//
	  public String readstring(InputStream is){
		  int tempchar=0;
		  try{
			  tempchar=is.read();
		  }catch (IOException e){System.out.println("not read!!!!");}
		
		  String tempstring = "";
		
			  int i=0;
			
			  while(tempchar!=(int)('\t') && tempchar!=(int)';' && tempchar!=(int)'\n')			
			  {
				  if(tempchar!=(int)('\t') && tempchar!=(int)';' && tempchar!=(int)'\n')	
				  tempstring = tempstring+(char)tempchar;
						
				  try{
					  tempchar=is.read();
				  }catch (IOException e){System.out.println("not read!!!!");}					
				  //////  32 ---- space, 13 ----- new Ltempne
			  } 
							  
			return tempstring;	  
		}


	public void actionPerformed( ActionEvent ae) {
		String arg = (String) ae.getActionCommand();
		Object obj = ae.getSource();

		  if(arg=="Evolve "||arg=="Evolve"){
			da.dp.evolve .setEnabled( false);
			vp.setEnabled( false);
			evolved = false;


	///initializing...			
			try {
				nd = new NetworkDynamics( vp.variables, url,currentInputFile,this);
			}
			catch(IOException ie) {
			}
	//////////////////
			da.currentYear = 0;
			da.dp.year.setText( "   Year "+ Integer.toString( da.currentYear ) + "   " );

	///running...	
			nd.NetworkDynamix(url,vp.variables,"",0);
			da.repaint();
			da.dp.setVisible(true);
	/////////////			
	
			da.dp.evolve .setEnabled( false);
			vp.setEnabled( true);
			evolved = true;	
	
	
			da.dp.first.setEnabled(true);
			da.dp.previous .setEnabled(true);
			da.dp.next .setEnabled(true) ;
			da.dp.last.setEnabled(true) ;
			//da.dp.slide .setEnabled( true);
			//da.dp.statistics .setEnabled( true);
			da.dp.whichAttribute.setEnabled(true) ;
			da.dp.scale .setEnabled( true);
		  }

		if(arg=="Batch" ||arg=="Batch "){
		  da.dp.evolve .setEnabled( false);
		  vp.setEnabled( false);
		  evolved = false;

		  InputStream experiments = null;
		  String filename="Batch.txt";
		  String temp=null;
		  int noofexperiments=0;
		  
		  try {
			  experiments = new URL(url, filename).openStream();
			  //fin = new FileInputStream(filename);
		  } catch(IOException e) {
			  System.out.println("Exception Occured!!!!");
		  }

		  String version=readstring(experiments);
		  vp.file.setText( version+"-1");
		  
		  temp=readstring(experiments);
		  try {
			  if(temp != null)  {
					  noofexperiments = Integer.valueOf(temp).intValue();
			  }
	
		  }	catch(NumberFormatException e) {
			  System.out.print("!!\t");
		  }
		  System.out.print("noofexperiments="+noofexperiments+"\n");

		  for(int i=0;i<noofexperiments;i++){
			int experimentno=0;
			float parameter1=(float)0.0,parameter2=(float)0.0;
			String status;
			if(i==0)status="1st";
			else if(i==1)status="2nd";
			else if(i==2)status="3rd";
			else status=(i+1)+"th";
			dp.showStatus .setText("The "+status+" experiment is being implemented..." );
			System.out.print( "\nThe "+status+" experiment is being implemented...\n" );
			
			vp.defaultVars() ;
			
			//experimentno
			temp=readstring(experiments);
			try {
				if(temp != null)  {
						experimentno = Integer.valueOf(temp).intValue();
				}
	
			}	catch(NumberFormatException e) {
				System.out.print("!!\t");
			}
			System.out.print(experimentno+"\t");
			
			//tPeriod
			temp=readstring(experiments);
			try {
				if(temp != null)  {
						vp.variables [22] = Integer.valueOf(temp).intValue();
				}
	
			}	catch(NumberFormatException e) {
				System.out.print("!!\t");
			}
			System.out.print(vp.variables [22]+"\t");
			
			//networktype
			temp=readstring(experiments);
			try {
				if(temp != null)  {
						currentInputFile = temp+".txt";
				}
	
			}	catch(NumberFormatException e) {
				System.out.print("!!\t");
			}
			
			System.out.print(currentInputFile+"\t");

		
			//v17-19 cost function
	
			temp=readstring(experiments);
			try {
				if(temp != null)  {
						vp.variables[17] = Float.valueOf(temp).floatValue();
				}
	
			}	catch(NumberFormatException e) {
				System.out.print("!!\t");
			}		  	
			System.out.print(vp.variables[17]+"\t");

			temp=readstring(experiments);
			try {
				if(temp != null)  {
						vp.variables[18] = Float.valueOf(temp).floatValue();
				}
	
			}	catch(NumberFormatException e) {
				System.out.print("!!\t");
			}		  	
			System.out.print(vp.variables[18]+"\t");
			
			temp=readstring(experiments);
			try {
				if(temp != null)  {
						vp.variables[19] = Float.valueOf(temp).floatValue();
				}
	
			}	catch(NumberFormatException e) {
				System.out.print("!!\t");
			}		  	
			System.out.print(vp.variables[19]+"\t");
		

			
			//v20 reduction factor
	
			temp=readstring(experiments);
			try {
				if(temp != null)  {
						vp.variables[20] = Float.valueOf(temp).floatValue();
				}
	
			}	catch(NumberFormatException e) {
				System.out.print("!!\t");
			}		  	
			System.out.print(vp.variables[20]+"\t");
		
		
			//symmetric land use?		
			temp=readstring(experiments);
			try {
				if(temp != null)  {
						vp.variables[11] = Float.valueOf(temp).floatValue();
				}
	
			}	catch(NumberFormatException e) {
				System.out.print("!!\t");
			}		  	
			System.out.print(vp.variables[11]+"\t");
			
			//land use model?
			temp=readstring(experiments);
			try {
				if(temp != null)  {
						vp.variables[23] = Float.valueOf(temp).floatValue();
				}
	
			}	catch(NumberFormatException e) {
				System.out.print("!!\t");
			}		  	
			System.out.print(vp.variables[23]+"\t");
			
			
			//road dynamics?
			temp=readstring(experiments);
			try {
				if(temp != null)  {
						vp.variables[24] = Float.valueOf(temp).floatValue();
				}
	
			}	catch(NumberFormatException e) {
				System.out.print("!!\t");
			}		  	
			System.out.print(vp.variables[24]+"\t");
		
			System.out.print("\n");
			
			da.setMapVariables() ;

			///initializing...			
					try {
						nd = new NetworkDynamics( vp.variables, url,currentInputFile,this);
					}
					catch(IOException ie) {
					}
			//////////////////
					da.currentYear = 0;
			///running...	
					nd.NetworkDynamix(url, vp.variables,version,experimentno);
			/////////////					  					
		}
			da.setMapVariables() ;		
			da.dp.evolve .setEnabled( true);
			vp.setEnabled( true);
			evolved = true;	
			dp.showStatus .setText("The batch of experiments have been implemented..." );
			System.out.print( "\nThe batch of experiments have been implemented...\n" );
			da.repaint() ;
		}


		if(arg=="Quit"){
			menuframe.dispose() ;
		}



		if(arg=="Instructions"){

			try {
			helpurl=new URL(url,"HelpFileSONG1.0.htm"); }

			catch (MalformedURLException e) {
			System.out.println("Bad URL:" + helpurl);
			}

			getAppletContext().showDocument(helpurl,"_blank");
		}

		if(arg=="About SIGNAL1.0"){

			try {
			helpurl=new URL(url,"HelpFileSONG1.0.htm"); }

			catch (MalformedURLException e) {
			System.out.println("Bad URL:" + helpurl);
			}

			getAppletContext().showDocument(helpurl,"_blank");
		}

		if(arg=="Save"){
			FileDialog savefile=new FileDialog(f,"Save Statistics...",FileDialog.SAVE);

			savefile.show() ;

			FileOutputStream out= null;
			System.out.print(savefile.getFilenameFilter()+"\n");
			System.out.print(savefile.getFile()+"\n");
			File s= new File(savefile.getDirectory(),savefile.getFile()  );

			try{
						out= new FileOutputStream(s);
					}catch(Exception e)
					{
						System.out.println("Unable to open file");
						return;
					}
			PrintStream psOut=new PrintStream(out);
			psOut.print(stat.getText());///
			try{
			out.close();
			}catch(IOException e){
			System.out.println("e");
			}
		}

		if(arg=="Close"){
			f.dispose()  ;
		}



//Command << < > >>
		  if(arg.equals("<<")) {
			  da.currentYear = 0;
			  da.repaint();
		  } else if( arg.equals("<") ) {
			  if( da.currentYear > 0 ) {
				  da.currentYear-=stepsize;
				  if(da.currentYear<0)da.currentYear=0;
				  da.repaint();
			  }  else {
				  da.currentYear = nd.endyear ;
				  da.repaint();
			  }
		  } else if(arg.equals(">") ) {
			  if(da.currentYear < nd.endyear ) {
				  da.currentYear +=stepsize;
				  if(da.currentYear>nd.endyear)da.currentYear=nd.endyear;
				  da.repaint();
			  } else if(da.currentYear == nd.endyear){
				  da.currentYear = 0;
				  da.repaint();
			  }
		  } else if(arg.equals(">>") ) {
			  da.currentYear = nd.endyear;
			  da.repaint();
		  } else if(arg.equals(">>>")){
			while(da.currentYear<nd.endyear ){
				da.currentYear++;
				da.repaint();
			}		  	
		  } else if (arg.equals("Go!")){
			String currentyear=dp.year1.getText().trim();
			int y=0;
			try {
				if(currentyear != null)  {
						y = Integer.valueOf(currentyear).intValue();
				}
	
			}	catch(NumberFormatException e) {
				System.out.print("!!\t");
			}		  	
			if(y>=0 && y<=nd.endyear ) da.currentYear =y;
			else if(y<0)da.currentYear=0;
			else if (y>nd.endyear)da.currentYear=nd.endyear;
			da.repaint();		  
		  }
	      
		dp.year.setText( " Year "+ Integer.toString( da.currentYear ) + "   " );

	}

	public void itemStateChanged( ItemEvent ie) {
		String arg = (String) ie.getItem();
		Object obj = ie.getSource();
		if (obj.equals(dp.whichAttribute)){
				if(dp.scale .getSelectedItem() =="Absolute"){
					if(arg.equals( "Speed")){
						drawAttribute = 0;
				
						dp.unit.setText("");
						dp.bluefor.setText("0~" + Integer.toString(5));
						dp.greenfor.setText(Integer.toString(5) +"~" + Integer.toString(10));
						dp.yellowfor.setText(Integer.toString(10)+"~" + Integer.toString(15));
						dp.orangefor.setText(Integer.toString(15) +"~"+ Integer.toString(20));
						dp.redfor.setText(Integer.toString(20) +"~"+ "  ");
						da.repaint();
					}
					else if (arg.equals( "Volume")){
						drawAttribute=1;						
						dp.unit.setText("");
						dp.bluefor.setText("0~" + Integer.toString(1000));
						dp.greenfor.setText(Integer.toString(1000)+"~" + Integer.toString(2000));
						dp.yellowfor.setText(Integer.toString(2000)+"~" + Integer.toString(3000));
						dp.orangefor.setText(Integer.toString(3000)+"~"+Integer.toString(4000));
						dp.redfor.setText(Integer.toString(4000)+"~");
						da.repaint();
					}
					else if (arg.equals( "Capacity")){
						drawAttribute=2;						
						dp.unit.setText("");
						dp.bluefor.setText("0~" + Integer.toString(1000));
						dp.greenfor.setText(Integer.toString(1000)+"~" + Integer.toString(2000));
						dp.yellowfor.setText(Integer.toString(2000)+"~" + Integer.toString(3000));
						dp.orangefor.setText(Integer.toString(3000)+"~"+Integer.toString(4000));
						dp.redfor.setText(Integer.toString(4000)+"~");
						da.repaint();
					}

					else if (arg.equals( "Population")){
						drawAttribute=3;
						dp.unit.setText("");
						dp.bluefor.setText("0~" + Integer.toString(1000));
						dp.greenfor.setText(Integer.toString(1000)+"~" + Integer.toString(2000));
						dp.yellowfor.setText(Integer.toString(2000)+"~" + Integer.toString(3000));
						dp.orangefor.setText(Integer.toString(3000)+"~"+Integer.toString(4000));
						dp.redfor.setText(Integer.toString(4000)+"~");
						da.repaint();
					}
					else if (arg.equals( "Employment")){
						drawAttribute=4;
						dp.unit.setText("");
						dp.bluefor.setText("0~" + Integer.toString(1000));
						dp.greenfor.setText(Integer.toString(1000)+"~" + Integer.toString(2000));
						dp.yellowfor.setText(Integer.toString(2000)+"~" + Integer.toString(3000));
						dp.orangefor.setText(Integer.toString(3000)+"~"+Integer.toString(4000));
						dp.redfor.setText(Integer.toString(4000)+"~");
						da.repaint();
					}

				}

				else{
					if(arg.equals( "Speed")){
						drawAttribute = 0;
						dp.unit.setText("");
						da.repaint();
					}
					else if(arg.equals( "Volume")){
						drawAttribute = 1;
						dp.unit.setText("");
						da.repaint();
					}
					else if(arg.equals( "Capacity")){
						drawAttribute = 2;
						dp.unit.setText("");
						da.repaint();
					}

					else if(arg.equals( "Population")){
						drawAttribute = 3;
						dp.unit.setText("");
						da.repaint();
					}
					else if(arg.equals( "Employment")){
						drawAttribute = 4;
						dp.unit.setText("");
						da.repaint();
					}
				}
		}

		else if (obj.equals(dp.scale)){
			if(arg.equals( "Relative")){
				dp.unit.setText("");
				dp.bluefor.setText("Lowest");
				dp.greenfor.setText("Lower");
				dp.yellowfor.setText("Middle");
				dp.orangefor.setText("Higher");
				dp.redfor.setText("Highest");
				da.repaint() ;
			}
			else{
				if(dp.whichAttribute.getSelectedItem() .equals( "Speed")){
					drawAttribute=0;
					dp.unit.setText("");
					dp.bluefor.setText("0~" + Integer.toString(5));
					dp.greenfor.setText(Integer.toString(5) +"~" + Integer.toString(10));
					dp.yellowfor.setText(Integer.toString(10)+"~" + Integer.toString(15));
					dp.orangefor.setText(Integer.toString(15) +"~"+ Integer.toString(20));
					dp.redfor.setText(Integer.toString(20) +"~"+ "  ");
					da.repaint();
				}
				else if(dp.whichAttribute.getSelectedItem() .equals( "Volume")){
					drawAttribute=1;
					dp.unit.setText("");
					dp.bluefor.setText("0~" + Integer.toString(1000));
					dp.greenfor.setText(Integer.toString(1000)+"~" + Integer.toString(2000));
					dp.yellowfor.setText(Integer.toString(2000)+"~" + Integer.toString(3000));
					dp.orangefor.setText(Integer.toString(3000)+"~"+Integer.toString(4000));
					dp.redfor.setText(Integer.toString(4000)+"~");
					da.repaint();
				}
				else if(dp.whichAttribute.getSelectedItem() .equals( "Capacity")){
					drawAttribute=2;
					dp.unit.setText("");
					dp.bluefor.setText("0~" + Integer.toString(1000));
					dp.greenfor.setText(Integer.toString(1000)+"~" + Integer.toString(2000));
					dp.yellowfor.setText(Integer.toString(2000)+"~" + Integer.toString(3000));
					dp.orangefor.setText(Integer.toString(3000)+"~"+Integer.toString(4000));
					dp.redfor.setText(Integer.toString(4000)+"~");
					da.repaint();
				}

				else if(dp.whichAttribute.getSelectedItem() .equals( "Population")){
					drawAttribute=3;
					dp.unit.setText("");
					dp.bluefor.setText("0~" + Integer.toString(1000));
					dp.greenfor.setText(Integer.toString(1000)+"~" + Integer.toString(2000));
					dp.yellowfor.setText(Integer.toString(2000)+"~" + Integer.toString(3000));
					dp.orangefor.setText(Integer.toString(3000)+"~"+Integer.toString(4000));
					dp.redfor.setText(Integer.toString(4000)+"~");
					da.repaint();					
				}
				else if(dp.whichAttribute.getSelectedItem() .equals( "Employment")){
					drawAttribute=4;
					dp.unit.setText("");
					dp.bluefor.setText("0~" + Integer.toString(1000));
					dp.greenfor.setText(Integer.toString(1000)+"~" + Integer.toString(2000));
					dp.yellowfor.setText(Integer.toString(2000)+"~" + Integer.toString(3000));
					dp.orangefor.setText(Integer.toString(3000)+"~"+Integer.toString(4000));
					dp.redfor.setText(Integer.toString(4000)+"~");
					da.repaint();
								
				}

			}
		}
	
		else if (obj.equals(dp.stepsize )){
			stepsize=Integer.valueOf(arg.trim() ).intValue();
		}
		
	}


///	total 23 variable are allocated to get the parameters of models
/// some of them are 'visible' in the interface
/// the others are 'invisible' and are fixed by default
/// this method is used to give the values of some 'invisible' variables
	public void writeVariables(){
	//total 23 variables in vp.variables[]
	//0,1,3,4,5,11 can be obtained from pull-down boxes
	//6-10,13-15,17-20 can be obtained from scrollbars,where 7=13,8=14,9=15
	//12,16,21,22 are fixed
	//2 never used

//from pull-down boxes
	//0,1
	if(vp.speed .getSelectedItem() .equals(vp.speed)){

		if(vp.speed .getSelectedItem() .equals("Uniform")){
			vp.variables[0]=5*(float)vp.v99.value();;
			vp.variables[1]=5*(float)vp.v99.value();;
		}
		else if(vp.speed .getSelectedItem() .equals("Random")){
			vp.variables[0]=1*(float)vp.v99.value();;
			vp.variables[1]=10*(float)vp.v99.value();;
		}
		else if(vp.speed .getSelectedItem() .equals("Prespecified Random")){
			if(getnetwork.equals("3X3 Network Degeneration")){vp.variables[0]=vp.variables[1]=-103;}
			else if(getnetwork.equals("4X4 Network Degeneration")){vp.variables[0]=vp.variables[1]=-104;}
			else if(getnetwork.equals("5X5 Network Degeneration")){vp.variables[0]=vp.variables[1]=-105;}
		}
	}

	//3,4,5
	if(vp.landuse.getSelectedItem() .equals(vp.landuse)){


		if(vp.landuse.getSelectedItem() .equals("Uniform")){
			vp.variables[3]=10*(float)vp.v100.value();
			vp.variables[4]=10*(float)vp.v100.value();
			vp.variables[5]=(float)0.0;
			}
		else if(vp.landuse.getSelectedItem() .equals("Random")){
			vp.variables[3]=5*(float)vp.v100.value();
			vp.variables[4]=15*(float)vp.v100.value();
			vp.variables[5]=(float)0.0;
			}
		else if(vp.landuse.getSelectedItem() .equals("Downtown")){
			vp.variables[3]=5*(float)vp.v100.value();
			vp.variables[4]=15*(float)vp.v100.value();
			vp.variables[5]=(float)1.0;
			}
		else if(vp.landuse.getSelectedItem() .equals("Prespecified Random")){
				if(getnetwork.equals("3X3 Network Degeneration")){vp.variables[3]=vp.variables[4]=-103;}
				else if(getnetwork.equals("4X4 Network Degeneration")){vp.variables[3]=vp.variables[4]=-104;}
				else if(getnetwork.equals("5X5 Network Degeneration")){vp.variables[3]=vp.variables[4]=-105;}

			vp.variables[5]=(float)0.0;
		}

	}
	//11
		if(vp.speed.getSelectedItem()=="Uniform" && vp.landuse.getSelectedItem()=="Uniform")
			vp.variables[11]=1;
		else if(vp.speed.getSelectedItem()=="Uniform" && vp.landuse.getSelectedItem()=="Downtown")
			vp.variables[11]=1;
		else
			vp.variables[11]=0;

		if(vp.network.getSelectedItem()=="A  Network  with  River")
			vp.variables[11]=0;

	///fixed
		vp.variables [12]=1;
		vp.variables [16]=365;
		vp.variables [21]=0;
	}


	class DrawPanel extends Panel {

		Signal sd;

		Panel legend=new Panel();
		Panel button=new Panel();;
		Panel status=new Panel();;
///////////////////////////////////////////////
		Choice whichAttribute = new Choice ();
		Choice scale = new Choice ();
		//Button help=new Button("Help");
		Label blank=new Label("    ");
		Button evolve = new Button("Evolve");
		Button batch=new Button("Batch");
		Choice stepsize=new Choice ();
		Label year0=new Label("Year");
		TextField year1=new TextField("   ");
		//Button statistics=new Button("Statistics");

		Button first = new Button("<<");
		Button previous = new Button("<");
		Label year = new Label(  "   Year 0     " , Label.CENTER );
		Button next = new Button(">");
		Button last = new Button(">>");
		Button go=new Button("Go!");
////////////////////////////////////////////////

		Label unit=new Label("");

		Label blue=new Label("    ");
		Label green=new Label("    ");
		Label yellow=new Label("    ");
		Label orange=new Label("    ");
		Label red=new Label("    ");

		Label bluefor=new Label("          ");
		Label greenfor=new Label("          ");
		Label yellowfor=new Label("          ");
		Label orangefor=new Label("          ");
		Label redfor=new Label("          ");

////////////////////////////////////////////////
		Label showStatus=new Label("");


		public DrawPanel( Signal sd) {
			showStatus.setFont(new Font("",Font.BOLD,12));
			this.sd = sd;
			setLayout(new BorderLayout());

//			button panel

			whichAttribute.addItem("Speed");
			whichAttribute.addItem("Volume");
			whichAttribute.addItem("Capacity");
			whichAttribute.addItem( "Population");
			whichAttribute.addItem( "Employment");
			whichAttribute.select("Speed");
			drawAttribute=0;
			whichAttribute.addItemListener(this.sd);

			scale.addItem("Absolute");
			scale.addItem("Relative");
			scale.select("Absolute");
			scale.addItemListener(this.sd);

			stepsize .addItem( "1");
			stepsize.addItem( "5");
			stepsize.addItem( "10");
			stepsize.addItem( "50");
			stepsize.addItem( "100");
			stepsize.select( "1");
			stepsize.addItemListener(this.sd);
			
			
			
			

			batch.addActionListener(this.sd);
			evolve.addActionListener(this.sd);
			first.addActionListener(this.sd);
			previous.addActionListener(this.sd);
			next.addActionListener(this.sd);
			last.addActionListener(this.sd);
			//slide.addActionListener(this.sd);
			//statistics.addActionListener( this.sd);
			go.addActionListener( this.sd);
			batch.setEnabled(true);
			evolve.setEnabled(false);
			//statistics.setEnabled(false);
			go.setEnabled( false);
			first.setEnabled(false);
			previous .setEnabled(false);
			next .setEnabled(false) ;
			last.setEnabled(false) ;
			//slide.setEnabled( false);
			scale.setEnabled( false);
			whichAttribute.setEnabled(false) ;


			button.add(batch);
			button.add(evolve);
			button.add(blank);
			button.add(scale);
			button.add(whichAttribute);
			button.add(stepsize);
			button.add( first);
			button.add( previous );
			button.add(year);
			button.add( next );
			button.add( last);
			button.add(new Label("  "));
			button.add(year0);
			button.add(year1);
			button.add(go);
			//button.add(slide);
			//button.add(statistics);

			add(button,"South");

//			legend panel
			legend.setLayout( new GridLayout(1,11));
			legend.add(new Label(" "));

			blue.setBackground(new Color(60, 100, 250));
			legend.add(blue);
			legend.add(bluefor);

			green.setBackground(new Color(8, 140, 14));
			legend.add(green);
			legend.add(greenfor);

			yellow.setBackground(Color.YELLOW );
			legend.add(yellow);
			legend.add(yellowfor);

			orange.setBackground(new Color(250, 125, 0));
			legend.add(orange);
			legend.add(orangefor);


			red.setBackground(new Color(200, 20, 20));
			legend.add(red);
			legend.add(redfor);
			if (scale.getSelectedItem() =="Absolute"){
				if (whichAttribute.getSelectedIndex() ==0)
				{
					drawAttribute = 0;
					unit.setText("");
					bluefor.setText("0~" + Integer.toString(5));
					greenfor.setText(Integer.toString(5) +"~" + Integer.toString(10));
					yellowfor.setText(Integer.toString(10)+"~" + Integer.toString(15));
					orangefor.setText(Integer.toString(15) +"~"+ Integer.toString(20));
					redfor.setText(Integer.toString(20) +"~"+ "  ");
					repaint();
				}
				else if (whichAttribute.getSelectedIndex() ==1)
				{
					unit.setText("");
					drawAttribute = 1;
					bluefor.setText("0~" + Integer.toString(1000));
					greenfor.setText(Integer.toString(1000)+"~" + Integer.toString(2000));
					yellowfor.setText(Integer.toString(2000)+"~" + Integer.toString(3000));
					orangefor.setText(Integer.toString(3000)+"~"+Integer.toString(4000));
					redfor.setText(Integer.toString(4000)+"~");
					repaint();
				}
				else if (whichAttribute.getSelectedIndex() ==2){
					unit.setText("");
					drawAttribute = 2;
					bluefor.setText("0~" + Integer.toString(1000));
					greenfor.setText(Integer.toString(1000)+"~" + Integer.toString(2000));
					yellowfor.setText(Integer.toString(2000)+"~" + Integer.toString(3000));
					orangefor.setText(Integer.toString(3000)+"~"+Integer.toString(4000));
					redfor.setText(Integer.toString(4000)+"~");
					repaint();
					
				}
				else if (whichAttribute.getSelectedIndex() ==3){
					unit.setText("");
					drawAttribute = 3;
					bluefor.setText("0~" + Integer.toString(1000));
					greenfor.setText(Integer.toString(1000)+"~" + Integer.toString(2000));
					yellowfor.setText(Integer.toString(2000)+"~" + Integer.toString(3000));
					orangefor.setText(Integer.toString(3000)+"~"+Integer.toString(4000));
					redfor.setText(Integer.toString(4000)+"~");
					repaint();
					
				}

			}
			else{

				unit.setText("");
				bluefor.setText("Lowest");
				greenfor.setText("Lower");
				yellowfor.setText("Middle");
				orangefor.setText("Higher");
				redfor.setText("Highest");
				repaint() ;

			}



			add(legend,"North");


//			status	panel
			status.setLayout( new GridLayout(1,1));
			status.add(showStatus);
			add(status,"Center");


		}
	}

	class DrawArea extends Panel {

		DrawPanel dp;

		int dim;      // size of the DrawArea,, which is equal to the number of pixes of the draw area
		int radius;   //Radius of circle that represents a node
		Dimension d;  //Current Dimension of the DrawArea (dynamic variable)
		Dimension sd;
		
		int netDim;
		int netGridSize;
		double x0,y0;
		
		int n;
		int currentYear = 0;

		float c0,c1,c2,c3,c4; //used to decide which color to use

		public DrawArea(DrawPanel dp) {
			this.dp = dp;

			setLayout(new BorderLayout() );
			add("South", dp );

		}


		void setMapVariables() {

			n = (int)vp.variables[22] +1;
			
			sd = getToolkit().getScreenSize();
			//System.out.print(sd.width +"\t"+sd.height);
			d=getSize() ;

			//System.out.println(" Dimension of the DrawArea: width =  "+d.width + "  height = " + d.height );
			double Max_node= ((nd.MaxX-nd.MinX)>(nd.MaxY-nd.MinY)) ? (nd.MaxX-nd.MinX):(nd.MaxY-nd.MinY);//in case that the height is not equal to the width
			dim = (int)    ( (d.width<d.height) ? (0.90*d.width) : (0.90*d.height)        );
			
			netDim=nd.dg.getDimension();
			netGridSize=(int)(nd.MaxX-nd.MinX)/(netDim-1);

			//x0=nd.dg.XCoordinate( 1);
			//y0=nd.dg.YCoordinate( 1);
			x0=nd.MinX ;
			y0=nd.MinY;
			
			
			if(Max_node != 0){
				Scale = (int)(0.95*dim/Max_node);
				//Scale = (int)(dim/Max_node);
			} else {
				System.out.println("From DrawArea class Max variable is 0. Erorr!!!!!");
				Scale = 2;
			}

			Transx_node=(int)((0.9*d.width-(nd.MaxX +nd.MinX )*Scale)/2)+10;
			Transy_node=(int)((0.9*d.height-(nd.MaxX +nd.MinX )*Scale)/2);
			radius = (int) (0.10*Scale);

			if(radius == 0)
				radius = 1;
			//System.out.println("Scale="+Scale+"\tMax="+Max+"\tTransx ="+Transx+"\tradius = "+ radius);

		}


//// network will be drawn for the current year
		private void drawLinks_Speed(Graphics g) {

			DecimalFormat myFormatter = new DecimalFormat("0.000");
			double min, max;
//read speed/volume data into the matrix f
			FloatStack  f[]= new FloatStack[nd.dg.Vertices()];
			for(int m=0;m<nd.dg.Vertices() ;m++){
				f[m]=new FloatStack(nd.dg.NoofLinks( m+1));
				for(int n=0;n<nd.dg.NoofLinks( m+1);n++){

					f[m].push(nd.dg.Speed( m+1,n+1));
				}
			}
//if(  drawAttribute==0) System.out.println("\t***Speed***");
//if(  drawAttribute==1) System.out.println("\t***Volume***");
//if(  drawAttribute==2) System.out.println("\t***Capacity***");
//System.out.println("k"+"\taver"+"\tvar"+"\tgini"+"\tmoment");
//for (int s=0;s<=50;s++)
//{currentYear = s*2;
//if (s==50)currentYear=99;
			if(evolved) {
				if(  drawAttribute==0)
					f = nd.Speed[currentYear];
				else if(  drawAttribute==1){
					//if( currentYear == n-1 )
					//	f = nd.Volume[n-2];
					//else
						f = nd.Volume[currentYear];
				}
				else if(  drawAttribute==2){
					//if( currentYear == n-1 )
					//	f = nd.Volume[n-2];
					//else
						f = nd.Capacity[currentYear];
				}
			} 
			
			float aver=0;
			float variance=0;
				

			float temp = 0;
			min = 1000000000;
			max =  -1 ;
			for(int i=0; i<nd.dg.Vertices(); i++) {
				for(int j=0; j<nd.dg.NoofLinks(i+1); j++) {
					temp = f[i].access(j);

					if( max < temp)
						max = temp;
					if( min > temp)
						min = temp;
						
					aver+=f[i].access(j);	
				}
			}
			
			aver=aver/nd.dg.edges ;
			for(int i=0; i<nd.dg.Vertices(); i++) {
				for(int j=0; j<nd.dg.NoofLinks(i+1); j++) {
					variance+=(float)Math.pow(f[i].access(j)-aver,2);
				}
			}
				
			variance=(float) Math.sqrt(variance/(nd.dg.edges -1));
			
			//Gini and Moment;

			double gini, moment, radius;
			gini = 0;
			moment = 0;
			radius = 0;

			double centerx, centery;
			centerx = (nd.dg.maxX()+nd.dg.minX())/2;
			centery = (nd.dg.maxY()+nd.dg.minY())/2;


			for(int i=0; i<nd.dg.Vertices(); i++) {
				for(int j=0; j<nd.dg.NoofLinks(i+1); j++) {
					int endnode=nd.dg.EndNodeNumbers(i+1,j+1);
					double xx=0.5*(nd.dg.XCoordinate(i+1)+nd.dg.XCoordinate(endnode));
					double yy = 0.5*(nd.dg.YCoordinate(i+1)+nd.dg.YCoordinate(endnode));

					double distancesquare = Math.pow((xx-centerx),2)+Math.pow((yy-centery),2);

					moment+=f[i].access( j)*distancesquare;

				}
			}


			float tempf[]=new float[nd.dg.Edges()];
			
			int index=0;			
			for(int i=0; i<nd.dg.Vertices(); i++) {
				for(int j=0; j<nd.dg.NoofLinks(i+1); j++) {
					tempf[index]=f[i].access( j);
					index++;
				}
			}


			for (int i=0;i<nd.dg.Edges()-1;i++)
			{
				for (int j=i+1;j<nd.dg.Edges();j++)
				{
					if (tempf[i]>tempf[j])
					{

						float change;
						change = tempf[i];
						tempf[i]=tempf[j];
						tempf[j]=change;
					}
				}
			}

			double s1, s1s2;
			s1=0;
			s1s2=0;

			double total;
			total = 0;
			for (int i=0;i<nd.dg.Edges();i++)
			{
				total+=tempf[i];
			}
			double accum=0;
			for (int i=0;i<nd.dg.Edges();i++)
			{
				double linearf;
				linearf = total*(i+1)/(nd.dg.Edges());
				accum+=tempf[i];
				s1+=linearf-accum;
			}

			s1s2=total*(nd.dg.Edges())/2;
			gini = s1/s1s2;
			radius = Math.sqrt(moment/total);

			if(drawAttribute==0)dp.showStatus.setText("Average of link speeds= "+myFormatter.format(aver)+";\tVariance of link speeds= "+myFormatter.format(variance)+";\tGini= "+myFormatter.format(gini)+";\tradious"+myFormatter.format(radius));
			else if(drawAttribute==1)dp.showStatus.setText("Average of link flows= "+myFormatter.format(aver)+";\tVariance of link flows= "+myFormatter.format(variance)+";\tGini= "+myFormatter.format(gini)+";\tradious"+myFormatter.format(radius));
			else if(drawAttribute==2)dp.showStatus.setText("Average of link capacities= "+myFormatter.format(aver)+";\tVariance of link capacities= "+myFormatter.format(variance)+";\tGini= "+myFormatter.format(gini)+";\tradious"+myFormatter.format(radius));

			
			int xcoord[] = new int[5];
			int ycoord[] = new int[5];
			float factor;

			for(int i =0; i<nd.dg.Vertices(); i++) {
				for(int j=0; j<nd.dg.NoofLinks(i+1); j++) {
					factor  = (float)(0.5*f[i].access(j) );
					int startx, starty, endx, endy;
					startx = (int)Transx_node+ (int)(nd.dg.XCoordinate(i+1)*Scale);
					starty=Transy_node+ (int)(nd.dg.YCoordinate(i+1)*Scale);
					//starty =  -(int) Transy+ (int)(dim-dim%100) - (int)(nd.dg.YCoordinate(i+1)*Scale);
					int k = nd.dg.EndNodeNumbers(i+1, j+1);
					endy=Transy_node+ (int)(nd.dg.YCoordinate(k)*Scale);
					endx = (int)Transx_node + (int)(nd.dg.XCoordinate(k)*Scale);
					//endy =   -(int) Transy +(int) (dim-dim%100) - (int)(nd.dg.YCoordinate(k)*Scale);


				if (dp.scale .getSelectedItem() =="Absolute"){
					///absolute scale
					if(drawAttribute==0)
						{c1=80;c2=100;c3=120;c4=160;}
					else if (drawAttribute==1)
						{c1=500;c2=1000;c3=2000;c4=4000;}
					else if (drawAttribute==2)
						{c1=2000;c2=3000;c3=4000;c4=5000;}
						
							if( f[i].access( j ) <=  c1  ) {
																
								g.setColor(new Color(60, 100, 250) );  /////Blue
								//g.setColor(new Color(150, 150, 150) );
								factor = (float) (0.15*Scale);
								//count1++;
							}
							else if ( f[i].access( j ) <=  c2  ) {
								//g.setColor(Color.WHITE ); 
								g.setColor(new Color(8, 140, 14) );   ////Green
								//g.setColor(new Color( 115, 115, 115) );
								factor = (float) (0.15*Scale);
								//count2++;
							}
							else if ( f[i].access( j ) <=  c3  ) {
								//g.setColor(Color.WHITE ); 
								g.setColor(Color.yellow);    ////// Yellow
								//g.setColor(new Color(70, 70, 70) );
								factor = (float) (0.35*Scale);
								//count3++;
							}
							else if ( f[i].access( j ) <=  c4  ) {
								
								//g.setColor(Color.RED  ); 
								g.setColor(new Color(250, 125, 0));    ////// Orange
								//g.setColor(new Color(70, 70, 70) );
								factor = (float) (0.35*Scale);
								//count3++;
							}
							else if ( f[i].access( j ) > c4  ) {
								g.setColor(Color.RED  );   //// Red
								//g.setColor(new Color(25, 25, 25) );
								factor = (float) (0.35*Scale);
								//count4++;
							}
  
							
							 

				}
				else{
					////relative scale
							double step = (max-min)/5;
							
							
							if( f[i].access( j ) <=  min+step  ) {
							   g.setColor(new Color(60, 100, 250) );  /////Blue
							   //g.setColor(new Color(150, 150, 150) );sc
							   factor = (float) (0.25*Scale);
							   //count1++;
						   }
						   else if ( f[i].access( j ) <=  min+2*step  ) {
							   g.setColor(new Color(8, 140, 14) );   ////Green
							   //g.setColor(new Color( 115, 115, 115) );
							   factor = (float) (0.25*Scale);
							   //count2++;
						   }
						   else if ( f[i].access( j ) <=  min+3*step  ) {
							   g.setColor(Color.yellow);    ////// Yellow
							   //g.setColor(new Color(70, 70, 70) );
							   factor = (float) (0.25*Scale);
							   //count3++;
						   }
						   else if ( f[i].access( j ) <=  min+4*step  ) {
							   g.setColor(new Color(250, 125, 0));    ////// Oringe
							   //g.setColor(new Color(70, 70, 70) );
							   factor = (float) (0.25*Scale);
							   //count3++;
						   }
						   else {
							   g.setColor(new Color (200, 20, 20) );   //// Red
							   //g.setColor(new Color(25, 25, 25) );
							   factor = (float) (0.25*Scale);
							   //count4++;
						   }

				}


					int xerror, yerror;
					int x = endx - startx;
					int y = endy - starty;


					xerror = (int) (factor*y/Math.sqrt(x*x+y*y));
					yerror = (int)(-factor*x/Math.sqrt(x*x+y*y));

					int endxadd = endx+xerror, startxadd = startx+xerror;
					int endyadd = endy+yerror, startyadd = starty+yerror;

					xcoord[0] = startx-1;
					xcoord[1] = endx-1;
					xcoord[2] = endxadd;
					xcoord[3] = startxadd;
					xcoord[4] = startx-1;

					ycoord[0] = starty-1;
					ycoord[1] = endy-1;
					ycoord[2] = endyadd;
					ycoord[3] = startyadd;
					ycoord[4] = starty-1;


					g.fillPolygon(xcoord, ycoord, 5);
					g.setColor(Color.white);
					g.drawLine(startx, starty, endx, endy);

				}
			}
//			System.out.println("Draw");
//			System.out.println(""+s+"\t"+myFormatter.format(aver)+"\t"+myFormatter.format(variance)+"\t"+myFormatter.format(gini)+"\t"+myFormatter.format(moment)+"\t"+myFormatter.format(radius/5));
//}
			//System.out.println("Current Year = "+currentYear +"*****Count = "+count1 + "  " + count2+ "  " + count3+ "  " + count4);

		}

		private void drawCells_Access(Graphics g) {
			double min, max;
			int automatasize=nd.dg.getDimension();
			//System.out.print("automatasize="+automatasize+"\n");
//read accessibility data into the matrix f
			float  f[]= new float[automatasize*automatasize];
			DecimalFormat myFormatter = new DecimalFormat("0.000");
			
			for(int m=0;m<automatasize*automatasize ;m++){
				f[m]=nd.dg.TAZ_info [m][1];			
			}
//if(  drawAttribute==3) System.out.println("\t***Population***");
//if(  drawAttribute==4) System.out.println("\t***Employment***");
//System.out.println("k"+"\taver"+"\tvar"+"\tgini"+"\tmoment");
//for (int k=0;k<=50;k++)
//{currentYear = k*2;
//if (k==50)currentYear=99;
			if(evolved) {
				if(drawAttribute==3)
					f = nd.Population[currentYear];
				else if(drawAttribute==4){
					//if( currentYear == n-1 )
					//	f = nd.Volume[n-2];
					//else
						f = nd.Employment[currentYear];
				}
			} 

			float aver=0;
			float variance=0;

			float temp = 0;
			min = 1000000000;
			max =  -1 ;
			
				for(int j=0; j<automatasize*automatasize; j++) {
					temp = f[j];

					if( max < temp)
						max = temp;
					if( min > temp)
						min = temp;
						
					aver+=f[j];			
				}
			aver=aver/nd.dg.vertices ;
			for(int i=0; i<automatasize*automatasize; i++) {
				
					variance+=(float)Math.pow(f[i]-aver,2);
				
			}

			variance=(float) Math.sqrt(variance/(nd.dg.vertices -1));
			
			
			
			//Gini and Moment;

			double gini, moment, radius;
			gini = 0;
			moment = 0;
			radius = 0;

			double centerx, centery;
			centerx = (nd.dg.maxX()+nd.dg.minX())/2;
			centery = (nd.dg.maxY()+nd.dg.minY())/2;

			for (int i=0;i<netDim*netDim;i++)
			{
				double xx = nd.dg.XCoordinate(i+1);
				double yy = nd.dg.YCoordinate(i+1);
				double distancesquare;
				distancesquare = Math.pow((xx-centerx),2)+Math.pow((yy-centery),2);

				moment+=f[i]*distancesquare;
			}

			float tempf[];
			tempf = new float[automatasize*automatasize];
			for (int i=0;i<automatasize*automatasize;i++)
			{
				tempf[i] = f[i];
			}

			for (int i=0;i<automatasize*automatasize-1;i++)
			{
				for (int j=i+1;j<automatasize*automatasize;j++)
				{
					if (tempf[i]>tempf[j])
					{

						float change;
						change = tempf[i];
						tempf[i]=tempf[j];
						tempf[j]=change;
					}
				}
			}
//			System.out.println("\t********Sorted*********");
//			for (int i=0;i<automatasize*automatasize-1;i++)
//			{
//				System.out.print("\t"+tempf[i]);
//			}
//			System.out.println("\n\t********End Sorted*********");
			double s1, s1s2;
			s1=0;
			s1s2=0;

			double total;
			total = 0;
			for (int i=0;i<automatasize*automatasize;i++)
			{
				total+=tempf[i];
			}
			double accum=0;
			for (int i=0;i<automatasize*automatasize;i++)
			{
				double linearf;
				linearf = total*(i+1)/(automatasize*automatasize);
				accum+=tempf[i];
				s1+=linearf-accum;
			}

			s1s2=total*(automatasize*automatasize)/2;
//			System.out.println("\ts1:"+s1+"\ts1s2:"+s1s2);
			gini = s1/s1s2;
			radius = Math.sqrt(moment/total);
			
			if(drawAttribute==3)dp.showStatus.setText("Variance of TAZ population= "+Math.round(variance*10)/10+";\tGini= "+myFormatter.format(gini)+";\tradious="+myFormatter.format(radius));
			else if(drawAttribute==4)dp.showStatus.setText("Variance of TAZ employment= "+Math.round(variance*10)/10+";\tGini= "+myFormatter.format(gini)+";\tradious="+myFormatter.format(radius));


			int xcoord[] = new int[4];
			int ycoord[] = new int[4];
			
			int index=0;


			for(int i=0;i<netDim*netDim;i++){
				double xx=nd.dg.XCoordinate( i+1);
				double yy=nd.dg.YCoordinate( i+1);
				xcoord[0]=(int)((xx-0.5*netGridSize)*Scale)+Transx_node;ycoord[0]=(int)((yy-0.5*netGridSize)*Scale)+Transy_node;
				xcoord[1]=(int)((xx+0.5*netGridSize)*Scale)+Transx_node;ycoord[1]=(int)((yy-0.5*netGridSize)*Scale)+Transy_node;
				xcoord[2]=(int)((xx+0.5*netGridSize)*Scale)+Transx_node;ycoord[2]=(int)((yy+0.5*netGridSize)*Scale)+Transy_node;
				xcoord[3]=(int)((xx-0.5*netGridSize)*Scale)+Transx_node;ycoord[3]=(int)((yy+0.5*netGridSize)*Scale)+Transy_node;
				if (dp.scale .getSelectedItem() =="Absolute"){
					///absolute scale
					if(drawAttribute==3)
						{c0=0 ;c1=800;c2=1000;c3=1500;c4=2000;}
					else if (drawAttribute==4)
					{c0=0 ;c1=800;c2=1000;c3=1500;c4=2000;}
							
							if (f[i]<c0){
								g.setColor(Color.white  );  
							}
							if( f[i] <=  c1  ) {																
								g.setColor(Color.blue);  /////Blue
								//g.setColor(new Color(150, 150, 150) );
							}
							else if ( f[i] <=  c2  ) {
									
								g.setColor(Color.green );   ////Green
								//g.setColor(new Color( 115, 115, 115) );
							}
							else if ( f[i] <=  c3  ) {
								g.setColor(Color.yellow);    ////// Yellow
								//g.setColor(new Color(70, 70, 70) );
							}
							else if ( f[i] <=  c4  ) {
								g.setColor(Color.orange );    ////// Orange
								//g.setColor(new Color(70, 70, 70) );
							}
							else if ( f[i] > c4  ) {
								g.setColor(Color.red  );   //// Red
								//g.setColor(new Color(25, 25, 25) );
							}

				}
				else{
					////relative scale
							double step = (max-min)/5;
							if(f[i]<0)g.setColor(Color.white);
							else if( f[i] <=  min+step  ) {																
								g.setColor(Color.blue);  /////Blue
								//g.setColor(new Color(150, 150, 150) );
							}
							else if ( f[i] <=  min+step*2  ) {
											
								g.setColor(Color.green);   ////Green
								//g.setColor(new Color( 115, 115, 115) );
							}
							else if ( f[i] <=  min+step*3  ) {
								g.setColor(Color.yellow);    ////// Yellow
								//g.setColor(new Color(70, 70, 70) );
							}
							else if ( f[i] <=  min+step*4  ) {
								g.setColor(Color.orange);    ////// Orange
								//g.setColor(new Color(70, 70, 70) );
							}
							else if ( f[i] > min+step*4  ) {
								g.setColor(Color.red  );   //// Red
								//g.setColor(new Color(25, 25, 25) );
							}

				}
					
				g.fillPolygon(xcoord, ycoord, 4);
			}

//			System.out.println(""+k+"\t"+Math.round(aver*10)/10+"\t"+Math.round(variance*10)/10+"\t"+myFormatter.format(gini)+"\t"+myFormatter.format(moment)+"\t"+myFormatter.format(radius/5));
//}
		}
		private void paintCells(Graphics g) {
			//int noOfLines;

			g.setColor(new Color(200, 200, 200) );
			
			
			for(int i=0; i<=netDim; i++) {
				g.drawLine((int)((x0-0.5*netGridSize)*Scale)+Transx_node, (int)((y0+(i-0.5)*netGridSize))*Scale+Transy_node, (int)((x0+(netDim-0.5)*netGridSize)*Scale)+Transx_node,  (int)((y0+(i-0.5)*netGridSize)*Scale)+Transy_node );   /// draw lines parallel to x-axis
				g.drawLine((int)((x0+(i-0.5)*netGridSize)*Scale)+Transx_node,(int)((y0-0.5*netGridSize))*Scale+Transy_node, (int)((x0+(i-0.5)*netGridSize)*Scale)+Transx_node,(int)((y0+(netDim-0.5)*netGridSize))*Scale+Transy_node);   /// draw lines parallel to x-axis
				
				//g.drawLine((int)Transx, sizeOfGrid+(int)(-Transy-(i-1)*sizeofcell), (int)Transx+sizeOfGrid, sizeOfGrid+(int) (-Transy-(i-1)*sizeofcell) );   /// draw lines parallel to x-axis
				//g.drawLine((int)(Transx+(i-1)*sizeofcell),  -(int)Transy,  (int)(Transx+(i-1)*sizeofcell),  sizeOfGrid-(int)Transy);

			}

		}

		private void paintDG(Graphics g) {
			////  Draw Speed boxes
			g.setColor(Color.black);
			if (drawAttribute<=2)
				drawLinks_Speed(g);
			else 
				drawCells_Access(g);
			///// Draw Nodes
			for(int i = 0; i< nd.dg.Vertices(); i++) {
				//if(i==15){g.setColor(Color.MAGENTA);}
				//else 
				g.setColor(Color.black);
				int newx, newy;
				newx = (int)Transx_node + (int)(nd.dg.XCoordinate(i+1)*Scale);
				newy=(int)(nd.dg.YCoordinate(i+1)*Scale) +(int)Transy_node;
				//newy  = (int)dim-dim%100 - (int)(nd.dg.YCoordinate(i+1)*Scale) - (int)Transy;
				if(i<Math.pow(nd.dg.getDimension(),2) ){
					g.fillOval(newx-(int)(2*radius) , newy-(int)(2*radius), 4*radius, 4*radius);					
				}
				else g.fillOval(newx-(int)(radius/2) , newy-(int)(radius/2), radius, radius);
				//if(i==6)g.fillOval(newx-(int)(2*radius) , newy-(int)(2*radius), 8*radius, 8*radius);
				//if(i==7)g.fillOval(newx-(int)(2*radius) , newy-(int)(2*radius), 6*radius, 6*radius);
			}
				
			

		}



		public void paint(Graphics g) {

			if  (graphRead) {
				paintCells(g);
				paintDG(g);
			}

		}



	}


///scrollPanel is used to define the scroll bars embeded in the variablePanel
	class ScrollPanel extends Panel implements AdjustmentListener{

		public double value;
		double maxvalue;
		double minvalue;
		int x;
		int y;
		int index;
		Label lvalue=new Label("");
		JScrollBar sb=new JScrollBar(JScrollBar.HORIZONTAL,0,1,0,101);

		public ScrollPanel(double minvalue, double maxvalue,double defaultvalue,int index){

		setLayout(new GridBagLayout());
		value =defaultvalue;
		this.maxvalue =maxvalue;
		this.minvalue=minvalue;
		this.index=index;
		
		if(index==22)lvalue=new Label(Integer.toString((int)defaultvalue));	
		else if(index==99||index==100)lvalue.setText(Integer.toString((int) (100*defaultvalue))+"%");
		else lvalue=new Label(Double.toString(defaultvalue));
		
		sb.setValue ((int)Math.round(100*(defaultvalue-minvalue)/(maxvalue-minvalue)));

		sb.addAdjustmentListener( this);
		}


		public void adjustmentValueChanged(AdjustmentEvent ame){
		Object obj=ame.getSource() ;
		int arg=ame.getAdjustmentType() ;

			if(obj.equals(this.sb)){
				if(arg==AdjustmentEvent.TRACK){
					value=minvalue+(maxvalue-minvalue)*(double)sb.getValue()/100.0;
				}
				else if(arg==AdjustmentEvent.UNIT_INCREMENT){
					value+=(double)(maxvalue-minvalue) /100.0;
					}
				else if(arg==AdjustmentEvent.UNIT_DECREMENT){
					value-=(double)(maxvalue-minvalue) /100.0;
					}
				else if(arg==AdjustmentEvent.BLOCK_INCREMENT){
					value+=(double)(maxvalue-minvalue) /10.0;
					}
				else if(arg==AdjustmentEvent.BLOCK_DECREMENT){
					value-=(double)(maxvalue-minvalue) /10.0;
					}
			}

			////some limitations by the model
			//the toll rate can't be zero, or the revenue will be zero
			if(index==13)
				{
					if (value==0.0)value+=(double)(maxvalue-minvalue) /100.0;
				}
			if(index==2||index==22)value=(int)value-(int)value%5;
			

			
			/////update corresponding vp.variables[]...			
			value=Math.round(value*100.0)/100.0;
			if(index==25){
				vp.variables[index]=(float)value;
				if(vp.killingRules.getSelectedItem() =="Fixed number"){
					lvalue.setText( Double.toString( (int)(edges*value)-(int)(edges*value)%16 ));
				}
				else if(vp.killingRules .getSelectedItem() =="Fixed percentage" || vp.killingRules.getSelectedItem() =="Autonomous"){
					lvalue.setText( Double.toString( value));
							
				}
			}
			else if(index<90){
				vp.variables[index]=(float)value;
				lvalue.setText(Double.toString( value));
			}
			else if(index==99){
				if(vp.speed .getSelectedItem() .equals("Uniform")){
					vp.variables[0]=5*(float)value;
					vp.variables[1]=5*(float)value;
				}
				else if(vp.speed .getSelectedItem() .equals("Random")){
					vp.variables[0]=1*(float)value;
					vp.variables[1]=10*(float)value;
				}
				else if(vp.speed .getSelectedItem() .equals("Prespecified Random")){
						if(getnetwork.equals("3X3 Network Degeneration")){vp.variables[0]=vp.variables[1]=-103;}
						else if(getnetwork.equals("4X4 Network Degeneration")){vp.variables[0]=vp.variables[1]=-104;}
						else if(getnetwork.equals("5X5 Network Degeneration")){vp.variables[0]=vp.variables[1]=-105;}
				}
				lvalue.setText(Integer.toString((int) (100*value))+"%");
			}
			else if(index==100){
				if(vp.landuse.getSelectedItem() .equals("Uniform")){
					vp.variables[3]=10*(float)value;
					vp.variables[4]=10*(float)value;
				}
				else if(vp.landuse .getSelectedItem() .equals("Random")||vp.landuse .getSelectedItem() .equals("Downtown")){
					vp.variables[3]=5*(float)value;
					vp.variables[4]=15*(float)value;
				}
				else if(vp.landuse .getSelectedItem() .equals("Prespecified Random")){
						if(getnetwork.equals("3X3 Network Degeneration")){vp.variables[3]=vp.variables[4]=-103;}
						else if(getnetwork.equals("4X4 Network Degeneration")){vp.variables[3]=vp.variables[4]=-104;}
						else if(getnetwork.equals("5X5 Network Degeneration")){vp.variables[3]=vp.variables[4]=-105;}
				}	
				lvalue.setText(Integer.toString((int) (100*value))+"%");
			}
	///////



			this.repaint() ;


/////any changes in scroll bars will repaint the network

			//reset right-hand panel	
			dp.scale.select( "Absolute");		
			dp.whichAttribute.select ("Speed");
			drawAttribute=0;
				
			dp.bluefor.setText("0~" + Integer.toString(5));
			dp.greenfor.setText(Integer.toString(5) +"~" + Integer.toString(10));
			dp.yellowfor.setText(Integer.toString(10)+"~" + Integer.toString(15));
			dp.orangefor.setText(Integer.toString(15) +"~"+ Integer.toString(20));
			dp.redfor.setText(Integer.toString(20) +"~"+ "  ");
			
			//all variables in the right-hand panel, except "evolve" are set disabled			
			dp.first.setEnabled(false);
			dp.previous .setEnabled(false);
			dp.next .setEnabled(false) ;
			dp.last.setEnabled(false) ;
			//dp.slide.setEnabled(false) ;
			dp.go.setEnabled( false);
			//dp.statistics .setEnabled( false);
			dp.whichAttribute.setEnabled(false) ;
			dp.scale .setEnabled( false);
			da.dp.evolve .setEnabled( true);
			
			writeVariables();
			//System.out.print("ScrollBars Changed,writeVariables:\n");
			//for(int i=0;i<23;i++){
			//	System.out.print(i+"\t"+vp.variables [i]+"\n");
			//}

			if(vp.network.getSelectedItem().equals("3X3 Network Degeneration")){
				dp.showStatus.setText("3X3 Complete Network Loaded...");
				dp.evolve.setEnabled(true) ;
				//dp.statistics .setEnabled(false);
				dp.go.setEnabled( false);
				currentInputFile = "Grid_3.txt";
				getnetwork="3X3 Network Degeneration" ;
				try {
					nd = new NetworkDynamics( vp.variables,url, currentInputFile);
				} catch (IOException e) {
				}

				da.setMapVariables();
				graphRead = true;
				evolved = false;
				da.currentYear = 0;
				dp.year.setText( "   Year "+ Integer.toString( da.currentYear ) + "   " );
				da.repaint();
			}
			else if(vp.network.getSelectedItem().equals("4X4 Network Degeneration")){
				dp.showStatus.setText("4X4 Complete Network Loaded...");
				dp.evolve.setEnabled(true) ;
				//dp.statistics .setEnabled(false);
				dp.go.setEnabled( false);
				currentInputFile = "Grid_4.txt";
				getnetwork="4X4 Network Degeneration" ;
				try {
					nd = new NetworkDynamics( vp.variables,url, currentInputFile);
				} catch (IOException e) {
				}

				da.setMapVariables();
				graphRead = true;
				evolved = false;
				da.currentYear = 0;
				dp.year.setText( "   Year "+ Integer.toString( da.currentYear ) + "   " );
				da.repaint();
			}
			else if(vp.network.getSelectedItem().equals("5X5 Network Degeneration")){
				dp.showStatus.setText("5X5 Complete Network Loaded...");
				dp.evolve.setEnabled(true) ;
				//dp.statistics .setEnabled(false);
				dp.go.setEnabled( false);
				currentInputFile = "Grid_5.txt";
				getnetwork="5X5 Network Degeneration" ;
				try {
					nd = new NetworkDynamics( vp.variables,url, currentInputFile);
				} catch (IOException e) {
				}

				da.setMapVariables();
				graphRead = true;
				evolved = false;
				da.currentYear = 0;
				dp.year.setText( "   Year "+ Integer.toString( da.currentYear ) + "   " );
				da.repaint();
			}			
			else if(vp.network.getSelectedItem().equals("10X10 Grid Network")){
				dp.showStatus.setText("10X10 Grid Network Loaded...");
				dp.evolve.setEnabled(true) ;
				//dp.statistics .setEnabled(false);
				dp.go.setEnabled( false);
				currentInputFile = "Grid10.txt";
				getnetwork="10X10 Grid Network" ;
				try {
					nd = new NetworkDynamics( vp.variables,url, currentInputFile);
				} catch (IOException e) {
				}

				da.setMapVariables();
				graphRead = true;
				evolved = false;
				da.currentYear = 0;
				dp.year.setText( "   Year "+ Integer.toString( da.currentYear ) + "   " );
				da.repaint();
			}	
			else if(vp.network.getSelectedItem().equals("20X20 Grid Network")){
				dp.showStatus.setText("20X20 Grid Network Loaded...");
				dp.evolve.setEnabled(true) ;
				//dp.statistics .setEnabled(false);
				dp.go.setEnabled( false);
				currentInputFile = "Grid20.txt";
				getnetwork="20X20 Grid Network" ;
				try {
					nd = new NetworkDynamics( vp.variables,url, currentInputFile);
				} catch (IOException e) {
				}

				da.setMapVariables();
				graphRead = true;
				evolved = false;
				da.currentYear = 0;
				dp.year.setText( "   Year "+ Integer.toString( da.currentYear ) + "   " );
				da.repaint();
			}	
			else if(vp.network.getSelectedItem().equals("50X50 Grid Network")){
				dp.showStatus.setText("50X50 Grid Network Loaded...");
				dp.evolve.setEnabled(true) ;
				//dp.statistics .setEnabled(false);
				dp.go.setEnabled( false);
				currentInputFile = "Grid50.txt";
				getnetwork="50X50 Grid Network" ;
				try {
					nd = new NetworkDynamics( vp.variables,url, currentInputFile);
				} catch (IOException e) {
				}

				da.setMapVariables();
				graphRead = true;
				evolved = false;
				da.currentYear = 0;
				dp.year.setText( "   Year "+ Integer.toString( da.currentYear ) + "   " );
				da.repaint();
			}					
		//////////////////////



		}

		public float value(){
			return (float)value;
		}

	}


	class VariablesPanel extends Panel implements ActionListener,WindowListener, ItemListener  {

		float variables[] = new float[50];
		float tempVars[]=new float[7];
		
		Label temp,temp2,parameter;

		Panel help=new Panel();
		Button forhelp=new Button();
		Button specify=new Button();
		Button restore=new Button();
		Button load=new Button();
		TextField file=new TextField();
		Choice network=new Choice();
		Choice speed=new Choice();

		Choice landuse=new Choice();
		
		Choice killingRules=new Choice();
		
		Choice investment=new Choice();

		Choice pricing=new Choice();
		
		Choice stoppingRules=new Choice();

		ScrollPanel v2,v6,v13,v10,v14,v15,v17,v18,v19,v20,v22,v25,v26,v99,v100;
		
		Checkbox c28,c29;
		
		Button confirm1,cancel1;
		GridBagLayout gbl,gbl1;
		GridBagConstraints constraints,constraints1=new GridBagConstraints();
		Frame investmentmodels;
		MenuBar imbar;
		
		//// Constructor
		public VariablesPanel() {
			
			investmentmodels=new Frame("Investment Models");

			
			gbl= new GridBagLayout();
			constraints=new GridBagConstraints();
			
			gbl1= new GridBagLayout();
			constraints1=new GridBagConstraints();

			defaultVars();

			setLayout(gbl);
			investmentmodels.setLayout( gbl1);


/////create vp panel
			restore=new Button("Restore");
			restore.addActionListener( this);

			constraints.weightx =1.0;
			constraints.weighty=1.0;

			constraints.anchor=GridBagConstraints.WEST;
			constraints.fill=GridBagConstraints.HORIZONTAL ;

			addComponent(this,0,1,2,1,temp=new Label("0. Network Type"));
			temp.setFont(new Font("",Font.BOLD,13));

			//network.addItem("3X3 Network Degeneration");
			//network.addItem("4X4 Network Degeneration");
			//network.addItem("5X5 Network Degeneration");
			network.addItem("10X10 Grid Network");
			network.addItem("50X50 Grid Network");

			addComponent(this,2,1,2,1,network);
			network.select("5X5 Network Degeneration");
			edges=8688;
			network.addItemListener( this);


			addComponent(this,0,2,2,1,temp=new Label("   Speed Distribution"));
			temp.setFont(new Font("",Font.BOLD,13));

			speed.addItem("Uniform");
			speed.addItem("Random");
			speed.addItem("Prespecified Random");
			addComponent(this,2,2,2,1,speed);
			speed.select( "Uniform");
			speed.addItemListener(this);


			addComponent(this,0,3,2,1,temp=new Label("   Land use Distribution"));
			temp.setFont(new Font("",Font.BOLD,13));

			landuse.addItem("Uniform");
			landuse.addItem("Random");
			landuse.addItem("Downtown");
			landuse.addItem("Prespecified Random");

			addComponent(this,2,3,2,1,landuse);
			landuse.select("Uniform");
			landuse.addItemListener(this);

			addComponent(this,0,4,2,1,temp=new Label("1. Land Use Model"));
			temp.setFont(new Font("",Font.BOLD,13));
			
			addComponent(this,0,5,2,1,new Label("   1.1 Land use range"));
			v2=new ScrollPanel(10,50,10,2);
			addComponent(this,2,5,2,1,v2.sb);
			addComponent(this,1,5,1,1,v2.lvalue);
			v2.lvalue .setAlignment( Label.RIGHT );
			v2.sb.setEnabled( false);			
			
			//addComponent(this,0,6,1,1,new Label("   1.2 "));
			//addComponent(this,0,6,3,1,c27=new Checkbox(" Land use reallocation",true));
			//c27.addItemListener(this);
			
			//addComponent(this,0,7,1,1,new Label("   1.3 "));
			addComponent(this,0,7,3,1,c28=new Checkbox(" Allocation limited to original nodes",false));
			c28.addItemListener(this);
			
			constraints.fill=GridBagConstraints.HORIZONTAL ;
			constraints.anchor=GridBagConstraints.WEST;
			addComponent(this,0,8,2,1,temp=new Label("2. Travel Demand Model"));
			temp.setFont(new Font("",Font.BOLD,13));
			addComponent(this,2,8,2,1,temp=new Label(""));

			v6=new ScrollPanel(0.0,20,10,6);
			addComponent(this,0,9,1,1,new Label("   2.1 Value of time"));
			addComponent(this,2,9,2,1,v6.sb);
			addComponent(this,1,9,1,1,v6.lvalue);
			v6.lvalue .setAlignment( Label.RIGHT );

			v10=new ScrollPanel(0,1,0.01,10);
			addComponent(this,0,10,1,1,new Label("   2.2 Friction factor"));
			addComponent(this,2,10,2,1,v10.sb);
			addComponent(this,1,10,1,1,v10.lvalue);
			v10.lvalue .setAlignment( Label.RIGHT );			
			
			
			addComponent(this,0,11,2,1,temp=new Label("3. Investment Models"));
			temp.setFont(new Font("",Font.BOLD,13));
			addComponent(this,2,11,1,1,specify=new Button("Specify"));
			specify.addActionListener(this);
			
			addComponent(this,0,12,2,1,temp=new Label("4. Elimination Model"));
			temp.setFont(new Font("",Font.BOLD,13));
			
			addComponent(this,0,13,2,1,new Label("   4.1 Walking speed"));
			v26=new ScrollPanel(0,10,5,26);
			addComponent(this,2,13,2,1,v26.sb);
			addComponent(this,1,13,1,1,v26.lvalue);
			v26.lvalue .setAlignment( Label.RIGHT );
			v26.sb.setEnabled( false);
			
			addComponent(this,0,14,2,1,new Label("   4.2 Time periods"));
			v22=new ScrollPanel(10,100,10,22);
			addComponent(this,2,14,2,1,v22.sb);
			addComponent(this,1,14,1,1,v22.lvalue);
			v22.lvalue .setAlignment( Label.RIGHT );
			
			//addComponent(this,0,15,1,1,new Label("   4.3 "));
			addComponent(this,0,15,3,1,c29=new Checkbox(" Degeneration delay",true));
			c29.addItemListener(this);

			addComponent(this,0,16,2,1,temp=new Label("   4.3 Killing Rule"));
			killingRules.addItem("Fixed number");
			killingRules.addItem("Fixed percentage");
			killingRules.addItem("Autonomous");
			addComponent(this,2,16,2,1,killingRules);
			killingRules.select("Fixed number");
			killingRules.addItemListener(this);

			addComponent(this,0,17,2,1,temp=new Label("   4.4 Stopping Rule"));
			stoppingRules.addItem("Default");
			stoppingRules.addItem("Maximal Welfare");
			stoppingRules.addItem("2");

			addComponent(this,2,17,2,1,stoppingRules);
			stoppingRules.addItemListener(this);
			
			v25=new ScrollPanel(0,1,0.05,25);
			addComponent(this,0,18,1,1,parameter=new Label("     Number:"));
			addComponent(this,2,18,2,1,v25.sb);
			addComponent(this,1,18,1,1,v25.lvalue);
			v25.lvalue .setAlignment( Label.RIGHT );
			v25.lvalue.setText( Double.toString( (int)(edges*v25.value())-(int)(edges*v25.value())%16 ));
			v25.sb.setEnabled( true);
			

			constraints.fill=GridBagConstraints.EAST ;
			addComponent(this,0,19,2,1,file=new TextField("signal1.8.0-"));
			constraints.fill=GridBagConstraints.CENTER ;
			addComponent(this,2,19,1,1,load=new Button("Load"));
			addComponent(this,3,19,1,1,restore);
			load.addActionListener ( this);


////create Window investmentmodels
			investmentmodels.addWindowListener( this);
			investmentmodels.setVisible( false);
			Dimension screensize = getToolkit().getScreenSize();
			//define the size of menuframe according to the screen size
			investmentmodels.setSize ((int)(0.35*screensize.width),
							(int)(0.6*screensize.height));
			//define the menu
			  imbar = new MenuBar();
			  investmentmodels.setMenuBar(imbar);
			  Menu ifile = new Menu("File");
			  Label temp=new Label("");
			
			  MenuItem  confirm,cancel,close;
			  ifile.add(confirm = new MenuItem("Confirm"));
			  ifile.add(cancel = new MenuItem("Cancel"));
			
			  imbar.add(ifile);
			
			  confirm.addActionListener(this);
			  cancel.addActionListener(this);
						
			  investmentmodels.setLayout(new GridBagLayout());
							
			  constraints1.weightx =1.0;
			  constraints1.weighty=1.0;
			
			  constraints1.anchor =GridBagConstraints.WEST;
			  constraints1.fill=GridBagConstraints.HORIZONTAL ;
			
			  addComponent(investmentmodels,0,0,4,1,temp=new Label("3 Investment Models"));
			  temp.setFont(new Font("",Font.BOLD,13));
			  addComponent(investmentmodels,0,1,4,1,temp=new Label("3.1 Revenue Model"));
			  temp.setFont(new Font("",Font.BOLD,13));
			
			  v13=new ScrollPanel(0.5,1.5,1,13);
			  addComponent(investmentmodels,0,2,1,1,new Label("     3.1.1 Toll rate"));
			  addComponent(investmentmodels,2,2,2,1,v13.sb);
			  addComponent(investmentmodels,1,2,1,1,v13.lvalue);
			  addComponent(investmentmodels,4,2,1,1,new Label(" "));				
			  v13.lvalue .setAlignment( Label.RIGHT );
			
			
			  v14=new ScrollPanel(0,1.5,1,14);
			  addComponent(investmentmodels,0,3,1,1,new Label("     3.1.2 Coeff. of length"));
			  addComponent(investmentmodels,2,3,2,1,v14.sb);
			  addComponent(investmentmodels,1,3,1,1,v14.lvalue);
			  v14.lvalue .setAlignment( Label.RIGHT );
						
			
			  v15=new ScrollPanel(0,1,0,15);
			  addComponent(investmentmodels,0,4,1,1,new Label("     3.1.3 Coeff. of speed"));
			  addComponent(investmentmodels,2,4,2,1,v15.sb);
			  addComponent(investmentmodels,1,4,1,1,v15.lvalue);
			  v15.lvalue .setAlignment( Label.RIGHT );
						
			  addComponent(investmentmodels,0,5,4,1,temp=new Label("3.2 Cost Model"));
			  temp.setFont(new Font("",Font.BOLD,13));
			
			  v17=new ScrollPanel(0,1.2,1,17);
			  addComponent(investmentmodels,0,6,1,1,new Label("     3.2.1 Coeff. of length"));
			  addComponent(investmentmodels,2,6,2,1,v17.sb);
			  addComponent(investmentmodels,1,6,1,1,v17.lvalue);
			  v17.lvalue .setAlignment( Label.RIGHT );
						
			
			  v18=new ScrollPanel(0,1.2,0.75,18);
			  addComponent(investmentmodels,0,7,1,1,new Label("     3.2.2 Coeff. of flow"));
			  addComponent(investmentmodels,2,7,2,1,v18.sb);
			  addComponent(investmentmodels,1,7,1,1,v18.lvalue);
			  v18.lvalue .setAlignment( Label.RIGHT );
						
			
			  v19=new ScrollPanel(0,1.2,0.75,19);
			  addComponent(investmentmodels,0,8,1,1,new Label("     3.2.3 Coeff. of speed"));
			  addComponent(investmentmodels,2,8,2,1,v19.sb);
			  addComponent(investmentmodels,1,8,1,1,v19.lvalue);
			  v19.lvalue .setAlignment( Label.RIGHT );
						
			
			  addComponent(investmentmodels,0,9,4,1,temp=new Label("3.3 Investment Model"));
			  temp.setFont(new Font("",Font.BOLD,13));
			
			  v20=new ScrollPanel(0,1,1,20);
			  addComponent(investmentmodels,0,10,1,1,new Label("     3.3.1 Speed impr coeff."));
			  addComponent(investmentmodels,2,10,2,1,v20.sb);
			  addComponent(investmentmodels,1,10,1,1,v20.lvalue);
			  v20.lvalue .setAlignment( Label.RIGHT );
						
			  constraints1.fill=GridBagConstraints.NONE;
			  constraints1.anchor=GridBagConstraints.EAST;		
						
			  addComponent(investmentmodels,1,11,1,1,confirm1=new Button("Confirm"));
			  addComponent(investmentmodels,3,11,1,1,cancel1=new Button("Cancel"));
			  confirm1.addActionListener( this);
			  cancel1.addActionListener( this);
		}

//		/define the events related to window
		  public void windowClosing(WindowEvent e){
			  Object obj = e.getSource();
			  if (obj.equals( vp.investmentmodels))vp.investmentmodels.setVisible( false);
		  }

		  public void windowOpened(WindowEvent e){
			  da.setVisible(true) ;

		  }

		  public void windowActivated(WindowEvent e){

			  da.repaint() ;
		  }

		  public void windowDeactivated(WindowEvent e){

			  da.repaint() ;
		  }

		  public void windowIconified(WindowEvent e){

			  da.repaint() ;
		  }

		  public void windowDeiconified(WindowEvent e){

			  da.repaint() ;
		  }

		  public void windowClosed(WindowEvent e){


		  }

		public void addComponent(Container frame, int x, int y, int w, int h, Component c)
		{
			if(frame==this){
				
				constraints.gridx=x;
				constraints.gridy=y;
				constraints.gridwidth=w;
				constraints.gridheight=h;

				
				
				gbl.setConstraints( c,constraints);add(c);
			}
			else if(frame==investmentmodels){
				
				constraints1.gridx=x;
				constraints1.gridy=y;
				constraints1.gridwidth=w;
				constraints1.gridheight=h;

				gbl1.setConstraints( c,constraints1);frame.add(c,constraints1);
			}
		}



		void defaultVars() {
			for (int i=0;i<50;i++)
				variables[i]=0;
			variables[0] = (float) 5;	//speedmin
			variables[1] = (float) 5;	//speedmax
			variables[2] = (float) 10;  /////land use range
			variables[3] = (float) 10;	//landmin
			variables[4] = (float) 10;	//landmax
			variables[5] = (float) 0.0;   // downtown?
			variables[6] = (float) 10;		//volue of time
			variables[7] = (float) 1.0;		//tax rate
			variables[8] = (float) 1.0;		//length rate
			variables[9] = (float) 0.0;		//speed rate
			variables[10] = (float) 0.01;	//friction factor
			variables[11] = (float) 1;	//symmetry?
			variables[12] = (float) 1;	//avg speed?
			variables[13] = (float) 1.0; //tax rate(toll rate)
			variables[14] = (float) 1.0; //length
			variables[15] = (float) 0;	//speed
			variables[16] = (float) 365;	//cost rate
			variables[17] = (float) 1.0;	//length coefficient
			variables[18] = (float) 0.75;	//flow coefficient
			variables[19] = (float) 0.75;	// speed coefficient
			variables[20] = (float) 0.1;	//speed reduction factor
			variables[21] = (float) 0;	//X
			variables[22] = (float) 10;	//time period
			variables[23] =(float)0;//killing rule
			variables[24] =(float)0;//stopping rule
			variables[25] =(float)0.05;//killing rule parameter
			variables[26] = (float)5; //initial speed
			//variables[27] = (float)1; //reallocation or not
			variables[28] = (float)0; //land use allocation limited to original nodes or not
			variables[29] = (float)1;//degeneration delay or not
			
			tempVars[0]=variables[13];
			tempVars[1]=variables[14];
			tempVars[2]=variables[15];
			tempVars[3]=variables[17];
			tempVars[4]=variables[18];
			tempVars[5]=variables[19];
			tempVars[6]=variables[20];
		}




		public void actionPerformed( ActionEvent ae) {
			String arg=(String) ae.getActionCommand();
			Object obj = ae.getSource();

			if(obj==forhelp){
				try { helpurl=new URL(url,"HelpFileSONG1.0.htm");

				 }

				catch (MalformedURLException e) {

				  System.out.println("Bad URL:" + helpurl);

				 }
				getAppletContext().showDocument(helpurl,"_blank");

			}

			else if(obj.equals(vp.specify)){
				tempVars[0]=vp.v13.value() ;
				tempVars[1]=vp.v14.value() ;
				tempVars[2]=vp.v15.value() ;
				tempVars[3]=vp.v17.value() ;
				tempVars[4]=vp.v18.value() ;
				tempVars[5]=vp.v19.value() ;
				tempVars[6]=vp.v20.value() ;
				investmentmodels.setVisible( true);
			}


			
			else if(obj==load){
				da.dp.evolve .setEnabled( false);
				vp.setEnabled( false);
				evolved = true;
				String inputFilename="";
				InputStream result = null;
				String filename=file.getText()+".txt";
				
				String temp=null;
				int endyear=0;
				float tempfloat=0;
		  		
				vp.defaultVars() ;
		  		
				try {
					result = new URL(url, filename).openStream();
					//fin = new FileInputStream(filename);
				} catch(IOException e) {
					System.out.println("Exception Occured-!!!!");
					
				}

				temp=readstring(result);
				try {
					if(temp != null)  {
							inputFilename = temp;
					}
	
				}	catch(NumberFormatException e) {
					System.out.print("!!\t");
				}




				try {
					nd = new NetworkDynamics( vp.variables, url,inputFilename);
				}
				catch(IOException ie) {
				}
			
				da.setMapVariables() ;
				
		//////////////////
				temp=readstring(result);
				try {
					if(temp != null)  {
							endyear = Integer.valueOf(temp).intValue();
					}
			
				}	catch(NumberFormatException e) {
					System.out.print("!!\t");
				}				
				//System.out.print("endyear="+endyear+"\n");
		
				int AutomataSize=nd.dg.getDimension();
				nd.Speed =new FloatStack[endyear+1][nd.dg.Vertices() ];
				nd.Volume =new FloatStack[endyear+1][nd.dg.Vertices() ];
				nd.Capacity =new FloatStack[endyear+1][nd.dg.Vertices() ];
		
				nd.Population =new float[endyear+1][AutomataSize*AutomataSize];
				nd.Employment =new float[endyear+1][AutomataSize*AutomataSize];
				
				for(int i=0;i<=endyear;i++){
					for(int m=0;m<nd.dg.Vertices() ;m++){
						nd.Speed [i][m]=new FloatStack(nd.dg.NoofLinks( m+1));
						nd.Volume [i][m]=new FloatStack(nd.dg.NoofLinks( m+1));
						nd.Capacity [i][m]=new FloatStack(nd.dg.NoofLinks( m+1));
						for(int n=0;n<nd.dg.NoofLinks( m+1);n++){

							nd.Speed [i][m].push(0);
							nd.Volume [i][m].push(0);	
							nd.Capacity [i][m].push(0);
				
						}
					}	
					
					for (int m=0;m<AutomataSize*AutomataSize;m++){
						nd.Population [i][m]=nd.Employment [i][m]=0;
					}
			
				}						

				
				for(int i=0;i<=endyear;i++){
					for(int m=0;m<nd.dg.Vertices() ;m++){
					
						for(int n=0;n<nd.dg.NoofLinks( m+1);n++){
							temp=readstring(result);
							try {
								if(temp != null)  {
										tempfloat = Float.valueOf(temp).floatValue();
									//System.out.print(tempfloat+"\t");	
									nd.Speed [i][m].replace(n,tempfloat);		
								}
			
							}	catch(NumberFormatException e) {
								System.out.print("!s\t");
							}			
	
							temp=readstring(result);
							try {
								if(temp != null)  {
										tempfloat = Float.valueOf(temp).floatValue();
									//System.out.print(tempfloat+"\t");	
									nd.Volume [i][m].replace(n,tempfloat);		
								}
			
							}	catch(NumberFormatException e) {
								System.out.print("!s\t");
							}	
							temp=readstring(result);
							try {
								if(temp != null)  {
										tempfloat = Float.valueOf(temp).floatValue();
									//System.out.print(tempfloat+"\t");	
									nd.Capacity [i][m].replace(n,tempfloat);		
								}
			
							}	catch(NumberFormatException e) {
								System.out.print("!s\t");
							}		
				
						}
						//System.out.print("\n");
					}
					
					for(int m=0;m<AutomataSize ;m++){
					
						for(int n=0;n<AutomataSize;n++){
							int index=m*AutomataSize+n;
							temp=readstring(result);
							try {
								if(temp != null)  {
										tempfloat = Float.valueOf(temp).floatValue();
								}
								nd.Population [i][index]=tempfloat;	
			
							}	catch(NumberFormatException e) {
								System.out.print("!v\t");
							}				

							temp=readstring(result);
							try {
								if(temp != null)  {
										tempfloat = Float.valueOf(temp).floatValue();
								}
								nd.Employment [i][index]=tempfloat;	
			
							}	catch(NumberFormatException e) {
								System.out.print("!v\t");
							}				
							
						}
						//System.out.print("\n");	
					}			
				
						
				}	

				da.dp.evolve .setEnabled( true);
				vp.setEnabled( true);		
				da.dp.first.setEnabled(true);
				da.dp.previous .setEnabled(true);
				da.dp.next .setEnabled(true) ;
				da.dp.last.setEnabled(true) ;
				//da.dp.slide.setEnabled(true) ;

				//da.dp.statistics .setEnabled( true);
				da.dp.go.setEnabled( true);
					da.dp.whichAttribute.setEnabled(true) ;
				da.dp.scale .setEnabled( true);
				
				da.currentYear = endyear;
				da.dp.year .setText("   Year "+endyear+"    ");
				nd.endyear =endyear;
				da.repaint() ;

				dp.showStatus .setText("The results of Experiment("+file.getText() +") have been loaded..." );
				/*
				for(int i=20;i<endyear;i++){
					int s=(int)(endyear-20)/4;
					if(i==20 || i==20+(s-s%10) || i==20+2*(s-s%10) || i==20+3*(s-s%10)|| i==endyear-1)
					{System.out.print("\nt= "+i+"\n");
					nd.SpeedEntropy( i);
					}
				}
				*/
				
				/*
				int tp[];
				tp=new int[7];
				for(int i=0;i<7;i++)
					tp[i]=i*20;
				//nd.writeStatistics(nd.Speed ,nd.Volume ,tp,file.getText() ,endyear) ;
				*/
			}

			else if(obj==restore){
		////restore values of variables		
				variables[0] = (float) 5;	//speedmin
				variables[1] = (float) 5;	//speedmax
				variables[2] = (float) 10;  /////land use range
				variables[3] = (float) 10;	//landmin
				variables[4] = (float) 10;	//landmax
				variables[5] = (float) 0.0;   // downtown?
				variables[6] = (float) 10;		//volue of time
				variables[7] = (float) 1.0;		//tax rate
				variables[8] = (float) 1.0;		//length rate
				variables[9] = (float) 0.0;		//speed rate
				variables[10] = (float) 0.01;	//friction factor
				variables[11] = (float) 1;	//symmetry?
				variables[12] = (float) 1;	//avg speed?
				variables[13] = (float) 1.0; //tax rate(toll rate)
				variables[14] = (float) 1.0; //length
				variables[15] = (float) 0;	//speed
				variables[16] = (float) 365;	//cost rate
				variables[17] = (float) 1.0;	//length coefficient
				variables[18] = (float) 0.75;	//flow coefficient
				variables[19] = (float) 0.75;	// speed coefficient
				variables[20] = (float) 0.1;	//speed reduction factor
				variables[21] = (float) 0;	//X
				variables[22] = (float) 10;	//time period
				variables[23] =(float)0;//killing rule
				variables[24] =(float)0;//stopping rule
				variables[25] =(float)0.05;//killing rule parameter
				variables[26] = (float)5; //initial speed
				//variables[27] = (float)1; //reallocation or not
				variables[28] = (float)0; //land use allocation limited to original nodes or not
				variables[29] = (float)1;//degeneration delay or not
				/*
				v99.value =(float)1;
				v99.lvalue.setText ("100%");
				v99.sb.setValue ((int)Math.round(100*(1.0-v99.minvalue)/(v99.maxvalue-vp.v99.minvalue)));

				v100.value =(float)1;
				v100.lvalue.setText ("100%");
				v100.sb.setValue ((int)Math.round(100*(1.0-v100.minvalue)/(v100.maxvalue-vp.v100.minvalue)));
				*/
				
			////restore choices
				vp.network .select( "5X5 Network Degeneration");
				vp.speed.select( "Uniform");
				vp.landuse .select( "Uniform");
				vp.killingRules.select( "Fixed number");
				vp.stoppingRules .select( "Default");		
				
			////restore checkboxs
				//c27.setState( true);
				c28.setState( false);	
				c29.setState( true);
				
			////restore scrollbars		
				v2.sb.setEnabled( true);
				v2.value =vp.variables [2];
				v2.lvalue.setText (Double.toString(Math.round(vp.variables [2]*100)/100.0));
				v2.sb.setValue ((int)Math.round(100*(vp.variables [2]-v6.minvalue)/(v2.maxvalue-vp.v2.minvalue)));			
				v2.setEnabled( false);
				
				v6.value =vp.variables [6];
				v6.lvalue.setText (Double.toString(Math.round(vp.variables [6]*100)/100.0));
				v6.sb.setValue ((int)Math.round(100*(vp.variables [6]-v6.minvalue)/(v6.maxvalue-vp.v6.minvalue)));

				v10.value =vp.variables [10];
				v10.lvalue.setText(Double.toString(Math.round(vp.variables [10]*100)/100.0));
				v10.sb.setValue ((int)Math.round(100*(vp.variables [10]-v10.minvalue)/(v10.maxvalue-vp.v10.minvalue)));

				v13.value =vp.variables [13];
				v13.lvalue.setText(Double.toString(Math.round(vp.variables [13]*100)/100.0));
				v13.sb.setValue ((int)Math.round(100*(vp.variables [13]-v13.minvalue)/(v13.maxvalue-vp.v13.minvalue)));
				v13.repaint() ;

				v14.value =vp.variables [14];
				v14.lvalue.setText(Double.toString(Math.round(vp.variables [14]*100)/100.0));
				v14.sb.setValue ((int)Math.round(100*(vp.variables [14]-v14.minvalue)/(v14.maxvalue-v14.minvalue)));

				v15.value =vp.variables [15];
				v15.lvalue.setText(Double.toString(Math.round(vp.variables [15]*100)/100.0));
				v15.sb.setValue ((int)Math.round(100*(vp.variables [15]-v15.minvalue)/(v15.maxvalue-v15.minvalue)));

				v17.value =vp.variables [17];
				v17.lvalue.setText(Double.toString(Math.round(vp.variables [17]*100)/100.0));
				v17.sb.setValue ((int)Math.round(100*(vp.variables [17]-v17.minvalue)/(v17.maxvalue-v17.minvalue)));

				v18.value =vp.variables [18];
				v18.lvalue.setText(Double.toString(Math.round(vp.variables [18]*100)/100.0));
				v18.sb.setValue ((int)Math.round(100*(vp.variables [18]-v18.minvalue)/(v18.maxvalue-v18.minvalue)));

				v19.value =vp.variables [19];
				v19.lvalue.setText(Double.toString(Math.round(vp.variables [19]*100)/100.0));
				v19.sb.setValue ((int)Math.round(100*(vp.variables [19]-v19.minvalue)/(v19.maxvalue-v19.minvalue)));

				v20.value =vp.variables [20];
				v20.lvalue.setText(Double.toString(Math.round(vp.variables [20]*100)/100.0));
				v20.sb.setValue ((int)Math.round(100*(vp.variables [20]-v20.minvalue)/(v20.maxvalue-v20.minvalue)));
				
				v22.value =vp.variables [22];
				v22.lvalue.setText(Double.toString(Math.round(vp.variables [22]*100)/100.0));
				v22.sb.setValue ((int)Math.round(100*(vp.variables [22]-v22.minvalue)/(v22.maxvalue-v22.minvalue)));
				
				v25.sb.setEnabled( true);
				v25.value =vp.variables [25];
				v25.sb.setValue ((int)Math.round(100*(vp.variables [25]-v25.minvalue)/(v25.maxvalue-v25.minvalue)));
				v25.lvalue.setText( Double.toString( (int)(2520*v25.value())-(int)(2520*v25.value())%16 ));
				
				
				v26.value =vp.variables [26];
				v26.lvalue.setText(Double.toString(vp.variables [26]));
				v26.sb.setValue ((int)Math.round(100*(vp.variables [26]-v26.minvalue)/(v26.maxvalue-v26.minvalue)));
			
	//reset right-hand panel	
				da.dp.scale.select( "Absolute");		
				da.dp.whichAttribute.select ("Speed");
				drawAttribute=0;
				
				da.dp.bluefor.setText("0~" + Integer.toString(5));
				da.dp.greenfor.setText(Integer.toString(5) +"~" + Integer.toString(10));
				da.dp.yellowfor.setText(Integer.toString(10)+"~" + Integer.toString(15));
				da.dp.orangefor.setText(Integer.toString(15) +"~"+ Integer.toString(20));
				da.dp.redfor.setText(Integer.toString(20) +"~"+ "  ");
			
				//all variables in the right-hand panel, except "evolve" are set disabled			
				da.dp.first.setEnabled(false);
				da.dp.previous .setEnabled(false);
				da.dp.next .setEnabled(false) ;
				da.dp.last.setEnabled(false) ;
				//da.dp.slide.setEnabled(false) ;

				//da.dp.statistics .setEnabled( false);
				dp.go.setEnabled( false);
				da.dp.whichAttribute.setEnabled(false) ;
				da.dp.scale .setEnabled( false);
				da.dp.evolve .setEnabled( true);
				
	///reload the network
	////

					dp.showStatus.setText("5X5 Complete Network Loaded...");
					dp.evolve.setEnabled(true) ;
					//dp.statistics .setEnabled(false);
					dp.go.setEnabled( false);
					currentInputFile = "Grid_5.txt";
					getnetwork="5X5 Network Degeneration" ;
					try {
						nd = new NetworkDynamics( variables,url, currentInputFile);
					} catch (IOException e) {
					}

					da.setMapVariables();
					graphRead = true;
					evolved = false;
					da.currentYear = 0;
					dp.year.setText( "   Year "+ Integer.toString( da.currentYear ) + "   " );
					da.repaint();

			}
			
			else if(arg=="Confirm"){
				investmentmodels.setVisible( false);
			}
			
			else if(arg=="Cancel"){
				variables[13]=Math.round( 100*tempVars[0])/100;
				variables[14]=Math.round( 100*tempVars[1])/100;
				variables[15]=Math.round( 100*tempVars[2])/100;
				variables[17]=Math.round( 100*tempVars[3])/100;
				variables[18]=Math.round( 100*tempVars[4])/100;
				variables[19]=Math.round( 100*tempVars[5])/100;
				variables[20]=Math.round( 100*tempVars[6])/100;			
				
				v13.value =vp.variables [13];
				vp.v13.lvalue.setText(Double.toString(vp.variables [13]));
				vp.v13.sb.setValue ((int)Math.round(100*(vp.variables [13]-v13.minvalue)/(v13.maxvalue-vp.v13.minvalue)));
				vp.v13.repaint() ;

				v14.value =vp.variables [14];
				v14.lvalue.setText(Double.toString(vp.variables [14]));
				v14.sb.setValue ((int)Math.round(100*(vp.variables [14]-v14.minvalue)/(v14.maxvalue-v14.minvalue)));

				v15.value =vp.variables [15];
				v15.lvalue.setText(Double.toString(vp.variables [15]));
				v15.sb.setValue ((int)Math.round(100*(vp.variables [15]-v15.minvalue)/(v15.maxvalue-v15.minvalue)));

				v17.value =vp.variables [17];
				v17.lvalue.setText(Double.toString(vp.variables [17]));
				v17.sb.setValue ((int)Math.round(100*(vp.variables [17]-v17.minvalue)/(v17.maxvalue-v17.minvalue)));

				v18.value =vp.variables [18];
				v18.lvalue.setText(Double.toString(vp.variables [18]));
				v18.sb.setValue ((int)Math.round(100*(vp.variables [18]-v18.minvalue)/(v18.maxvalue-v18.minvalue)));

				v19.value =vp.variables [19];
				v19.lvalue.setText(Double.toString(vp.variables [19]));
				v19.sb.setValue ((int)Math.round(100*(vp.variables [19]-v19.minvalue)/(v19.maxvalue-v19.minvalue)));

				v20.value =vp.variables [20];
				v20.lvalue.setText(Double.toString(vp.variables [20]));
				v20.sb.setValue ((int)Math.round(100*(vp.variables [20]-v20.minvalue)/(v20.maxvalue-v20.minvalue)));
									
				investmentmodels.setVisible( false);
			}
			
		}

		public void itemStateChanged( ItemEvent ie) {
			String arg=(String) ie.getItem();
			Object obj=ie.getSource();
		/////c28,c29

			if(obj.equals(vp.c28)){
				vp.variables [28]=1-vp.variables [28];
			}
			else if(obj.equals(vp.c29)){
				vp.variables [29]=1-vp.variables [29];
			}

		/////Network
			if(obj.equals(vp.network)){
				//reset right-hand panel	
				da.dp.scale.select( "Absolute");		
				da.dp.whichAttribute.select ("Speed");
				drawAttribute=0;
				
				da.dp.bluefor.setText("0~" + Integer.toString(5));
				da.dp.greenfor.setText(Integer.toString(5) +"~" + Integer.toString(10));
				da.dp.yellowfor.setText(Integer.toString(10)+"~" + Integer.toString(15));
				da.dp.orangefor.setText(Integer.toString(15) +"~"+ Integer.toString(20));
				da.dp.redfor.setText(Integer.toString(20) +"~"+ "  ");
			
				//all variables in the right-hand panel, except "evolve" are set disabled			
				da.dp.first.setEnabled(false);
				da.dp.previous .setEnabled(false);
				da.dp.next .setEnabled(false) ;
				da.dp.last.setEnabled(false) ;
				//da.dp.slide.setEnabled(false) ;

				//da.dp.statistics .setEnabled( false);
				da.dp.go.setEnabled( false);
				da.dp.whichAttribute.setEnabled(false) ;
				da.dp.scale .setEnabled( false);
				da.dp.evolve .setEnabled( true);


				if(arg.equals("3X3 Network Degeneration")){
					edges=232;
					if(vp.killingRules .getSelectedIndex() ==0){
						vp.v25.lvalue.setText( Double.toString( (int)(edges*vp.v25.value())-(int)(edges*vp.v25.value())%16 ));
					}
					else if(vp.killingRules .getSelectedIndex() ==1 && vp.network.getSelectedIndex() ==2){
						vp.v25.lvalue.setText( Double.toString(vp.v25.value() ));
							
					}
					dp.showStatus.setText("3X3 Complete Network Loaded...");
					dp.evolve.setEnabled(true) ;
					//dp.statistics .setEnabled(false);
					dp.go.setEnabled( false);
					currentInputFile = "Grid_3.txt";
					getnetwork="Network Degeneration" ;
					if(vp.speed .getSelectedItem() =="Prespecified Random"){variables[0]=variables[1]=-103;}
					if(vp.landuse .getSelectedItem() =="Prespecified Random"){variables[3]=variables[4]=-103;}

					try {
						nd = new NetworkDynamics( variables,url, currentInputFile);
					} catch (IOException e) {
					}

					da.setMapVariables();
					graphRead = true;
					evolved = false;
					da.currentYear = 0;
					dp.year.setText( "   Year "+ Integer.toString( da.currentYear ) + "   " );
					da.repaint();				
				
				}
				
				else if(arg.equals("4X4 Network Degeneration")){
					edges=1744;
					if(vp.killingRules .getSelectedIndex() ==0){
						vp.v25.lvalue.setText( Double.toString( (int)(edges*vp.v25.value())-(int)(edges*vp.v25.value())%16 ));
					}
					else if(vp.killingRules .getSelectedIndex() ==1 && vp.network.getSelectedIndex() ==2){
						vp.v25.lvalue.setText( Double.toString(vp.v25.value() ));
							
					}
					
					dp.showStatus.setText("4X4 Complete Network Loaded...");
					dp.evolve.setEnabled(true) ;
					//dp.statistics .setEnabled(false);
					dp.go.setEnabled( false);
					currentInputFile = "Grid_4.txt";
					getnetwork="Network Degeneration" ;
					if(vp.speed .getSelectedItem() =="Prespecified Random"){variables[0]=variables[1]=-104;}
					if(vp.landuse .getSelectedItem() =="Prespecified Random"){variables[3]=variables[4]=-104;}

					try {
						nd = new NetworkDynamics( variables,url, currentInputFile);
					} catch (IOException e) {
					}

					da.setMapVariables();
					graphRead = true;
					evolved = false;
					da.currentYear = 0;
					dp.year.setText( "   Year "+ Integer.toString( da.currentYear ) + "   " );
					da.repaint();				
				
				}	
				else if(vp.network.getSelectedItem().equals("5X5 Network Degeneration")){
					edges=8688;
					if(vp.killingRules .getSelectedIndex() ==0){
						vp.v25.lvalue.setText( Double.toString( (int)(edges*vp.v25.value())-(int)(edges*vp.v25.value())%16 ));
					}
					else if(vp.killingRules .getSelectedIndex() ==1 && vp.network.getSelectedIndex() ==2){
						vp.v25.lvalue.setText( Double.toString(vp.v25.value() ));
							
					}
					
					dp.showStatus.setText("5X5 Complete Network Loaded...");
					dp.evolve.setEnabled(true) ;
					//dp.statistics .setEnabled(false);
					dp.go.setEnabled( false);
					currentInputFile = "Grid_5.txt";
					getnetwork="5X5 Network Degeneration" ;
					if(vp.speed .getSelectedItem() =="Prespecified Random"){variables[0]=variables[1]=-105;}
					if(vp.landuse .getSelectedItem() =="Prespecified Random"){variables[3]=variables[4]=-105;}

					try {
						nd = new NetworkDynamics( variables,url, currentInputFile);
					} catch (IOException e) {
					}

					da.setMapVariables();
					graphRead = true;
					evolved = false;
					da.currentYear = 0;
					dp.year.setText( "   Year "+ Integer.toString( da.currentYear ) + "   " );
					da.repaint();
				}
				else if(vp.network.getSelectedItem().equals("10X10 Grid Network")){
					edges=360;
					if(vp.killingRules .getSelectedIndex() ==0){
						vp.v25.lvalue.setText( Double.toString( (int)(edges*vp.v25.value())-(int)(edges*vp.v25.value())%16 ));
					}
					else if(vp.killingRules .getSelectedIndex() ==1 && vp.network.getSelectedIndex() ==2){
						vp.v25.lvalue.setText( Double.toString(vp.v25.value() ));
							
					}
					
					dp.showStatus.setText("10X10 Grid Network Loaded...");
					dp.evolve.setEnabled(true) ;
					//dp.statistics .setEnabled(false);
					dp.go.setEnabled( false);
					currentInputFile = "Grid10.txt";
					getnetwork="10X10 Grid Network" ;
					if(vp.speed .getSelectedItem() =="Prespecified Random"){variables[0]=variables[1]=-10;}
					if(vp.landuse .getSelectedItem() =="Prespecified Random"){variables[3]=variables[4]=-10;}

					try {
						nd = new NetworkDynamics( variables,url, currentInputFile);
					} catch (IOException e) {
					}

					da.setMapVariables();
					graphRead = true;
					evolved = false;
					da.currentYear = 0;
					dp.year.setText( "   Year "+ Integer.toString( da.currentYear ) + "   " );
					da.repaint();
				}
				else if(vp.network.getSelectedItem().equals("50X50 Grid Network")){
					edges=9800;
					if(vp.killingRules .getSelectedIndex() ==0){
						vp.v25.lvalue.setText( Double.toString( (int)(edges*vp.v25.value())-(int)(edges*vp.v25.value())%16 ));
					}
					else if(vp.killingRules .getSelectedIndex() ==1 && vp.network.getSelectedIndex() ==2){
						vp.v25.lvalue.setText( Double.toString(vp.v25.value() ));
							
					}
					
					dp.showStatus.setText("50X50 Grid Network Loaded...");
					dp.evolve.setEnabled(true) ;
					//dp.statistics .setEnabled(false);
					dp.go.setEnabled( false);
					currentInputFile = "Grid50.txt";
					getnetwork="50X50 Grid Network" ;
					if(vp.speed .getSelectedItem() =="Prespecified Random"){variables[0]=variables[1]=-20;}
					if(vp.landuse .getSelectedItem() =="Prespecified Random"){variables[3]=variables[4]=-20;}

					try {
						nd = new NetworkDynamics( variables,url, currentInputFile);
					} catch (IOException e) {
					}

					da.setMapVariables();
					graphRead = true;
					evolved = false;
					da.currentYear = 0;
					dp.year.setText( "   Year "+ Integer.toString( da.currentYear ) + "   " );
					da.repaint();
				}			
			}

			else{

				/// pull-down box Speed
				if(obj.equals(vp.speed)){

					if(arg.equals("Uniform")){
						//vp.v99.sb.setEnabled( true);
						vp.variables[0]=5;
						vp.variables[1]=5;
					}
					else if(arg.equals("Random")){
						//vp.v99.sb.setEnabled( true);
						vp.variables[0]=1;
						vp.variables[1]=10;
					}
					else if(arg.equals("Prespecified Random")){						
						//vp.v99.value =(float)1;
						//vp.v99.lvalue.setText ("100%");
						//vp.v99.sb.setValue ((int)Math.round(100*(1.0-vp.v99.minvalue )/(vp.v99.maxvalue -vp.v99.minvalue )));
						//vp.v99.sb.setEnabled( false);
						
						if(getnetwork.equals("5X5 Grid Network")){variables[0]=variables[1]=-5;}
						else if(getnetwork.equals("10X10 Grid Network")){variables[0]=variables[1]=-10;}
						else if(getnetwork.equals("15X15 Grid Network")){variables[0]=variables[1]=-15;}
						else if(getnetwork.equals("50X50 Grid Network")){variables[0]=variables[1]=-20;}
						else if(getnetwork.equals("A  Network  with  River")){variables[0]=variables[1]=-99;}
						else if(getnetwork.equals("3X3 Network Degeneration")){variables[0]=variables[1]=-103;}
						else if(getnetwork.equals("4X4 Network Degeneration")){variables[0]=variables[1]=-104;}
						else if(getnetwork.equals("5X5 Network Degeneration")){variables[0]=variables[1]=-105;}
						
					}
				}

				///pull-down
				if(obj.equals(vp.landuse)){


					if(arg.equals("Uniform")){
						//vp.v100.sb.setEnabled( true);
						vp.v2.sb.setEnabled( false);
						variables[3]=10;
						variables[4]=10;
						variables[5]=(float)0.0;
						}
					else if(arg.equals("Random")){
						//vp.v100.sb.setEnabled( true);
						vp.v2.sb.setEnabled( true);
						variables[3]=5;
						variables[4]=15;
						variables[5]=(float)0.0;
						}
					else if(arg.equals("Downtown")){
						//vp.v100.sb.setEnabled( true);
						vp.v2.sb.setEnabled(true);	
						variables[3]=5;
						variables[4]=15;
						variables[5]=(float)1.0;
						}
					else if(arg.equals("Prespecified Random")){						
						vp.v2.sb.setEnabled( false);
						//vp.v100.value =(float)1;
						//vp.v100.lvalue.setText ("100%");
						//vp.v100.sb.setValue ((int)Math.round(100*(1.0-vp.v100.minvalue )/(vp.v100.maxvalue -vp.v100.minvalue)));
						//vp.v100.sb.setEnabled(false);

						if(getnetwork.equals("5X5 Grid Network")){variables[3]=variables[4]=-5;}
						else if(getnetwork.equals("10X10 Grid Network")){variables[3]=variables[4]=-10;}
						else if(getnetwork.equals("15X15 Grid Network")){variables[3]=variables[4]=-15;}
						else if(getnetwork.equals("20X20 Grid Network")){variables[3]=variables[4]=-20;}
						else if(getnetwork.equals("A  Network  with  River")){variables[3]=variables[4]=-99;}
						else if(getnetwork.equals("3X3 Network Degeneration")){variables[3]=variables[4]=-103;}
						else if(getnetwork.equals("4X4 Network Degeneration")){variables[3]=variables[4]=-104;}
						else if(getnetwork.equals("5X5 Network Degeneration")){variables[3]=variables[4]=-105;}

						variables[5]=(float)0.0;
					}

				}

			///others
				if(obj.equals(vp.killingRules)){
					if(arg.equals( "Fixed number")){variables[23]=0;v25.lvalue.setText( Double.toString( (int)(edges*v25.value())-(int)(edges*v25.value())%16 ));parameter.setText( "     Number:");}
					else if(arg.equals( "Fixed percentage")){variables[23]=1;v25.lvalue.setText( Double.toString( v25.value ()));parameter.setText( " Percentage:");}
					else if(arg.equals( "Autonomous")){variables[23]=2;v25.lvalue.setText( Double.toString( v25.value ()));parameter.setText( " Percentage:");}
				}
				


				if(obj.equals(vp.stoppingRules)){
					if(arg.equals( "Default")) variables[24]=0;
					else if(arg.equals( "Maximal Welfare")) variables[24]=1;
					else if(arg.equals( "2")) variables[24]=2;
				}
				
	////any changes in any pull-down boxes other than the network pull-down will also rapaint the network
	//reset right-hand panel	
			dp.scale.select( "Absolute");		
			dp.whichAttribute.select ("Speed");
			drawAttribute=0;
						
			dp.bluefor.setText("0~" + Integer.toString(5));
			dp.greenfor.setText(Integer.toString(5) +"~" + Integer.toString(10));
			dp.yellowfor.setText(Integer.toString(10)+"~" + Integer.toString(15));
			dp.orangefor.setText(Integer.toString(15) +"~"+ Integer.toString(20));
			dp.redfor.setText(Integer.toString(20) +"~"+ "  ");
					
			//all variables in the right-hand panel, except "evolve" are set disabled			
			dp.first.setEnabled(false);
			dp.previous .setEnabled(false);
			dp.next .setEnabled(false) ;
			dp.last.setEnabled(false) ;
			//dp.slide.setEnabled(false) ;

			//dp.statistics .setEnabled(false);
			dp.go.setEnabled( false);
			dp.whichAttribute.setEnabled(false) ;
			dp.scale .setEnabled( false);
			da.dp.evolve .setEnabled( true);
			
			writeVariables();
			//System.out.print("Choices ItemChanged,writeVariables:\n");
			//for(int i=0;i<23;i++){
			//	System.out.print(i+"\t"+vp.variables [i]+"\n");
			//}
			if(vp.network.getSelectedItem().equals("3X3 Network Degeneration")){
				dp.showStatus.setText("3X3 Complete Network Loaded...");
				dp.evolve.setEnabled(true) ;
				//dp.statistics .setEnabled(false);
				dp.go.setEnabled( false);
				currentInputFile = "Grid_3.txt";
				getnetwork="Network Degeneration" ;
				if(vp.speed .getSelectedItem() =="Prespecified Random"){variables[0]=variables[1]=-103;}
				if(vp.landuse .getSelectedItem() =="Prespecified Random"){variables[3]=variables[4]=-103;}

				try {
					nd = new NetworkDynamics( variables,url, currentInputFile);
				} catch (IOException e) {
				}

				da.setMapVariables();
				graphRead = true;
				evolved = false;
				da.currentYear = 0;
				dp.year.setText( "   Year "+ Integer.toString( da.currentYear ) + "   " );
				da.repaint();				
				
			}
				
			else if(vp.network.getSelectedItem().equals("4X4 Network Degeneration")){
				dp.showStatus.setText("4X4 Complete Network Loaded...");
				dp.evolve.setEnabled(true) ;
				//dp.statistics .setEnabled(false);
				dp.go.setEnabled( false);
				currentInputFile = "Grid_4.txt";
				getnetwork="Network Degeneration" ;
				if(vp.speed .getSelectedItem() =="Prespecified Random"){variables[0]=variables[1]=-104;}
				if(vp.landuse .getSelectedItem() =="Prespecified Random"){variables[3]=variables[4]=-104;}

				try {
					nd = new NetworkDynamics( variables,url, currentInputFile);
				} catch (IOException e) {
				}

				da.setMapVariables();
				graphRead = true;
				evolved = false;
				da.currentYear = 0;
				dp.year.setText( "   Year "+ Integer.toString( da.currentYear ) + "   " );
				da.repaint();				
				
			}	
			else if(vp.network.getSelectedItem().equals("5X5 Network Degeneration")){
				dp.showStatus.setText("5X5 Complete Network Loaded...");
				dp.evolve.setEnabled(true) ;
				//dp.statistics .setEnabled(false);
				dp.go.setEnabled( false);
				currentInputFile = "Grid_5.txt";
				getnetwork="5X5 Network Degeneration" ;
				if(vp.speed .getSelectedItem() =="Prespecified Random"){variables[0]=variables[1]=-105;}
				if(vp.landuse .getSelectedItem() =="Prespecified Random"){variables[3]=variables[4]=-105;}


				try {
					nd = new NetworkDynamics( variables,url, currentInputFile);
				} catch (IOException e) {
				}

				da.setMapVariables();
				graphRead = true;
				evolved = false;
				da.currentYear = 0;
				dp.year.setText( "   Year "+ Integer.toString( da.currentYear ) + "   " );
				da.repaint();
			}
			else if(vp.network.getSelectedItem().equals("10X10 Grid Network")){
				dp.showStatus.setText("10X10 Complete Network Loaded...");
				dp.evolve.setEnabled(true) ;
				//dp.statistics .setEnabled(false);
				dp.go.setEnabled( false);
				currentInputFile = "Grid10.txt";
				getnetwork="10X10 Grid Network" ;
				if(vp.speed .getSelectedItem() =="Prespecified Random"){variables[0]=variables[1]=-10;}
				if(vp.landuse .getSelectedItem() =="Prespecified Random"){variables[3]=variables[4]=-10;}


				try {
					nd = new NetworkDynamics( variables,url, currentInputFile);
				} catch (IOException e) {
				}

				da.setMapVariables();
				graphRead = true;
				evolved = false;
				da.currentYear = 0;
				dp.year.setText( "   Year "+ Integer.toString( da.currentYear ) + "   " );
				da.repaint();
			}
			else if(vp.network.getSelectedItem().equals("50X50 Grid Network")){
				dp.showStatus.setText("50X50 Grid Network Loaded...");
				dp.evolve.setEnabled(true) ;
				//dp.statistics .setEnabled(false);
				dp.go.setEnabled( false);
				currentInputFile = "Grid50.txt";
				getnetwork="50X50 Grid Network" ;
				if(vp.speed .getSelectedItem() =="Prespecified Random"){variables[0]=variables[1]=-20;}
				if(vp.landuse .getSelectedItem() =="Prespecified Random"){variables[3]=variables[4]=-20;}


				try {
					nd = new NetworkDynamics( variables,url, currentInputFile);
				} catch (IOException e) {
				}

				da.setMapVariables();
				graphRead = true;
				evolved = false;
				da.currentYear = 0;
				dp.year.setText( "   Year "+ Integer.toString( da.currentYear ) + "   " );
				da.repaint();
			}			
			////
		 }//end of else


		}///End of public void ()

	}
	////// End of class VariablesPanel



}

///////  End of Demo Class


