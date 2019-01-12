/*
 * Matt Packwood, Orchard Ridge, Monday Evening Class, Fall Semester 2003
 * PA09:  model figure MVC w/array cocepts 
 * screen size 576x396
 *
 * The Applet from the previous lab is modified to construct and manipulate an
 * array of model figure objects and associated array of random values. The
 * model objects are displayed vertically on the left side of the screen; the
 * object selected by user mouse-click is displayed in larger size in the
 * middle of the screen along with a text string showing the ID and associated
 * value.  The scrollbar controls appearance variable per previous exercises.
 * copyright2002 by Henry Austin and Oakland CC
 */
import java.awt.*;
import java.applet.Applet;
import java.awt.event.*;
import java.util.*; // for Random class
public class CmptrL9 extends Applet 
    implements AdjustmentListener, MouseListener { // multiple events

	Scrollbar ballB; // screen ball control
	Label ballL; // ball count feedback
	boolean runF= false; // large display control flag
	int f, smlSlot; // fig index, sml fig slot size
	int scrnW, scrnH; // screen bounds
	final int FIGS= 8, WDGTh= 60, BALLmax= 6;  // constants
	int smlX, smlY, smlSz, bigX, bigY, bigSz, ballCt= 0; // state vars
	Color bgClr= Color.white, fgClr= Color.black;
	Color FIGclr [ ]= {null, Color.yellow, Color.green, Color.cyan, // assoc clrs
		 Color.gray, Color.pink, Color.orange, Color.magenta, Color.white};     
	Graphics g; // global screen ref
	CompSys [ ] figs; // ref to array of model obj refs
	int [ ] unitSales; // ref to associated count array
	Random r; // random obj ref
	
public void init ( ) {
	setBackground (bgClr); // BG color
	r= new Random (-32767); // construct Random obj; seed for sameness
	ballL= new Label ("Balls= "+ ballCt); // screen ball feedback
	add (ballL);
	ballB= new Scrollbar (Scrollbar.HORIZONTAL, 0, 1, 0, BALLmax+ 1); 
	add (ballB);
	ballB.addAdjustmentListener (this); 
    this.addMouseListener (this); // activate mouse events
	Dimension scrnSz= getSize ( ); // get size of applet screen from HTML tag
	scrnW= scrnSz.width;  scrnH= scrnSz.height; // set screen dim vars
		// calc base vals
	smlSlot= scrnH /8 ; // slot height for sml fig
	smlSz= smlSlot /6; // 20% of 7/8 slot size
	smlX= 9; // qtr inch from left edge 
	smlY= 40; // init to top slot
	bigSz= scrnH / 7; // max sz
	bigX=  150; // rt of tiny
	bigY=  scrnH -100; // screen bot- mrgn
		// construct associative arrays
	figs= new CompSys [FIGS+ 1];  // init fig array to FIGS+ 1 cells
	unitSales= new int [FIGS+ 1]; // init assoc array to FIGS+ 1 cells 
		// set fig colors & associated sale vals
	for (int i= 1; i <= FIGS; i++) { // ref by traverse index
 		figs [i]= new CompSys (smlX, smlY, smlSz); // construct model obj for tiny, side dsply
		figs [i].setClr (FIGclr [i] ); // set figs[i] color to assoc color in FIGclr array
		figs [i].setID (i* i+ i); // hash ID from index (crude....)
			// gen unit sales from bell-curve distribution w/avg=0, std dev=1;
		double c= r.nextGaussian ( ); // 95% of vals are in -2 thru +2 range
		if (c < -4) // compress outliers
			c= -4; 
		else if (c > 4)
			c= 4;
		unitSales [i]= (int) ((c+4)* 250); // scale to avg 1000, std dev 250
		 
		smlY+= smlSlot; // incrs y for nxt fig
		} // end traverse
	}
public void paint (Graphics gg) {
	g= gg; // global screen
	g.setFont (new Font ("SansSerif", Font.BOLD, 14)); // dressUp applet text
	if (runF)  // bigFig dsply request
		dsplyBigFig ( );
	else
		upDteBallCt (0);
		// upDateBallCt with zero
	for (int i= 1; i <= FIGS; i++) { // ref by traverse index
		figs [i].dsplyFig (g); // dsply small fig on screen left side
		g.setColor(Color.black);
		g.drawString (""+figs [i].getID ( ), smlSz*7, figs [i].getY( ) ); // ID nxt to fig
			// x/y here...
		} // end traverse
	} 
private void dsplyBigFig ( ) { // ref by hash index; set in event method
	int y= figs [f].getY( ); // save model obj y for tiny, side dsply
	// set figs[f] x/y/sz vals for big dsply 
	figs [f].setX (bigX);
	figs [f].setY (bigY);
	figs [f].setSz (bigSz);
	figs [f].dsplyFig (g); // dsply big fig
	g.setColor (Color.black);
	g.drawString ("ID="+figs [f].getID ( )+ ", Unit Sales="+unitSales [f], bigX+bigSz, bigY-(4*bigSz)-9 ); // dsply id & unit sales above big dsply
	// restore figs[f] state to tiny, side dsply
	figs [f].setX (smlX);
	figs [f].setY (y);
	figs [f].setSz (smlSz);
	
	}
private void upDteBallCt (int b) {
	ballCt= b; // set scroll bar ctr and label to ballCt
	ballL.setText("Balls= "+ ballCt); 
	} 
public void adjustmentValueChanged (AdjustmentEvent e) { // SB event
	if (runF) { // run flag on
		upDteBallCt (ballB.getValue ( ) ); // upDteBallCt w/scrollbar
		figs [f].setBallCt ( ballCt ); // set figs [f] ballCt
		showStatus (figs [f].getID ( )+ "'s screen balls updated"); // status msg w/figs[f] ID
		repaint ( );
		}
	else
		showStatus ("select figure before using scrollbar"); // status msg
	}
public void mouseClicked (MouseEvent mE) {
	int y= mE.getY ( ); 
	int x= mE.getX ( );
	if (x <=bigX) { // mouse x at left edge of screen?        	
		f= (y / smlSlot)+1; // hash mouse Y to corr fig index
		// upDteBallCt with figs [f] ballCt
		upDteBallCt (figs [f].getBallCt ( ) );
		ballB.setValue (figs [f].getBallCt ( ) );
		runF= true; // turn on big dsply cntrl flag
    	repaint ();
		}  
	}    
public void mouseReleased (MouseEvent e) { }
public void mousePressed (MouseEvent e) { }
public void mouseEntered (MouseEvent e) { }
public void mouseExited (MouseEvent e) { }
} // ** END APPLET CLASS **

class CompSys { // ** BASE MODEL CLASS **
// PA08 base model class here; update with var for int id,
// setter/getter methods for x, y, sz, id and ballCt.
//
	int bX, bY, bS; // base vars
	int qS, hS, dS; // work ratio vars; yours may vary... 
	int ballCt, figId; // ball ctr and ID
	boolean zipF; // ZIP option on/off
	Graphics g; // class-scope screen ref
	Color sysClr = (Color.yellow); // change Color.yellow refs to sysClr
// ** PUBLIC, INTERACTION METHODS **
public CompSys (int x, int y, int s) { // ** CONSTRUCTOR **
	// init global base, ballCt and zipF vals here
	bX = x;
	bY = y;
	bS = s;
	}
public void setX (int x) { // ** SETTER METHODS **
	// update bX here
	bX=x;
	}
public void setY (int y) { 
	bY=y;
	}	
public void setBallCt (int ct) {
	// update ballCt here
	ballCt = ct;
	}
public void setID (int id) {
	// update ballCt here
	figId = id;
	}
public void setClr (Color c) {
	// update ballCt here
	sysClr = c;
	}
public void setSz (int s) {
	// update ballCt here
	bS = s;
	}	
public void toglOptStat ( ) { // change toglZIP refs to toglOpt 
	zipF= ! zipF;
	}
public boolean getOptStat ( ) {
	return zipF;
	}
public int getBallCt ( ) {
	return ballCt;
	}
public int getX ( ) {
	return bX;
	}
public int getY ( ) {
	return bY;
	}
public int getID ( ) {
	return figId;
	}	
public int getSz ( ) {
	return bS;
	}	
public void dsplyFig (Graphics gg) {
	g= gg;
	calcRatios ( );
	dsplyKeyBord ( );
	dsplyPtrDev ( ); 
	dsplyCPU ( );
	dsplyDsply ( );
	}
void calcRatios ( ) { 
	// same old same old......
	qS= Math.round (bS/4.0f); // calc ratio vals
	hS= Math.round (bS/2.0f); // calc ratio vals
	dS= Math.round (bS*2.0f); // calc ratio vals
	}
void dsplyCPU ( ) { 
	g.setColor (sysClr); //draw stuff
	// CPU, CD, opt ZIP
	g.fillRect (bX, bY-bS, dS+dS+bS, bS); // CPU
	g.setColor (Color.black);
	g.drawRect (bX, bY-bS, dS+dS+bS, bS); // CPU outline
	g.fillRect (bX+dS+hS, bY-hS-qS, dS, qS); // DVD slot
	// conditional ZIP slot display; see hat dsply technique in SF demo
	if (zipF) {
		g.fillRect (bX+hS, bY-bS+qS, bS, qS);
		}
	}
void dsplyKeyBord ( ) {
	// KB & keys
	g.setColor (sysClr);
	g.fillRect (bX, bY, dS+dS, bS); // Keyboard
	g.setColor (Color.black);
	g.drawRect (bX, bY, dS+dS, bS); // Keyboard Outline
	g.fillRect (bX+qS, bY+qS, dS+bS, hS); // Keys
	}
void dsplyPtrDev ( ) {
	// mouse, buttons & cord
	g.setColor (sysClr);
	g.fillOval (bX+dS+dS+qS, bY+qS, hS, hS); // Mouse
	g.setColor (Color.black);
	g.drawOval (bX+dS+dS+qS, bY+qS, hS, hS); // Mouse outline
	g.drawLine (bX+dS+dS, bY+hS, bX+dS+dS+qS, bY+hS); // Mouse cord
	g.fillArc (bX+dS+dS+qS, bY+qS, hS, hS, 45, 90); // Mouse 1
	g.fillArc (bX+dS+dS+qS, bY+qS, hS, hS, 225, 90); // Mouse 2
	}
void dsplyDsply ( ) {
	// monitor, screen & opt balls
	g.setColor (sysClr);
	g.fillRect (bX+hS, bY-dS-dS, dS+dS, dS+bS); // Monitor
	g.setColor (Color.black);
	g.drawRect (bX+hS, bY-dS-dS, dS+dS, dS+bS); // Monitor Outline
	g.setColor (Color.lightGray);
	g.fillRect (bX+hS+qS, bY-dS-bS-hS-qS, dS+bS+hS, dS+hS); // Screen
	// conditional screen ball display; see hair display technique in SF demo
	switch (ballCt) { // no breaks == fall-thru
		case 6: 	g.setColor (Color.black);
				g.fillOval (bX+bS+dS-(((ballCt-3)%3)*bS), bY-dS-hS, bS, bS);
		case 5: 	g.setColor (Color.red);
				g.fillOval (bX+bS+dS-(((ballCt-2)%3)*bS), bY-dS-hS, bS, bS);   
		case 4: 	g.setColor (Color.yellow);
				g.fillOval (bX+bS+dS-(((ballCt-1)%3)*bS), bY-dS-hS, bS, bS);
		case 3: 	g.setColor (Color.green);
				g.fillOval (bX+bS+dS-(((ballCt-3)%3)*bS), bY-dS-hS-((ballCt <=5 ? 0 : 1)*bS), bS, bS);
		case 2: 	g.setColor (Color.blue);
				g.fillOval (bX+bS+dS-(((ballCt-2)%3)*bS), bY-dS-hS-((ballCt <=4 ? 0 : 1)*bS), bS, bS);
		case 1: 	g.setColor (Color.white);
				g.fillOval (bX+bS+dS-(((ballCt-1)%3)*bS), bY-dS-hS-((ballCt <=3 ? 0 : 1)*bS), bS, bS);
		} // end switch; minus values ignored
	}
}
