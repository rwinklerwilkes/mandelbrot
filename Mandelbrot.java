import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Stack;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.event.*;

/**
 *
 * @author Rich
 */
public class Mandelbrot extends JPanel {

    private BufferedImage canvas;
    private Color[] colorTable;
    private int[][] pixelVals;
	private double beginX;
	private double endX;
	private double beginY;
	private double endY;
	private Stack<double[]> oldVal;
	private static final int WIDTH = 800;
	private static final int HEIGHT = 400;
    public Mandelbrot()
    {
		oldVal = new Stack<double[]>();
		beginX = -2;
		endX = 1;
		beginY = -1;
		endY = 1;
        canvas = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
        colorTable = new Color[255];
        for(int i=0; i<colorTable.length-1; i++)
        {
            colorTable[i] = new Color(i,i,0);
        }
		colorTable[colorTable.length-1] = new Color(colorTable.length-1,colorTable.length-1,colorTable.length-1);
        pixelVals = new int[WIDTH][HEIGHT];
        fillPixels(pixelVals);
        fillCanvas(pixelVals);
        JFrame frame = new JFrame("Mandelbrot Set");
        frame.add(this);
		mandListener mListen = new mandListener();
		this.addMouseListener(mListen);
        frame.setSize(WIDTH,HEIGHT);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setVisible(true);
    }
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        Graphics2D g2=(Graphics2D) g;
        g2.drawImage(canvas,null,null);
    }
    public void fillCanvas(int[][] m)
    {
        for(int x = 0; x<m.length; x++)
        {
            for(int y = 0; y<m[x].length; y++)
            {
                int lookup = m[x][y]/4;
                canvas.setRGB(x, y, colorTable[lookup].getRGB());
            }
        }
        repaint();
    }
	private double transWidth(int pixel)
	{
		double slope = WIDTH/(endX-beginX);
		return ((double)(pixel-WIDTH))*(1/slope)+endX;
	}
	private double transHeight(int pixel)
	{
		double slope = HEIGHT/(endY-beginY);
		return ((double)(pixel-HEIGHT))*(1.0/slope) + endY;
	}
    private void fillPixels(int[][] m)
    {
        for(int x = 0; x<m.length; x++)
        {
            for(int y = 0; y<m[x].length; y++)
            {
				/*if(y>10)
				{
					System.exit(0);
				}
				System.out.println("x: " + transWidth(x));
				System.out.println("y: " + transHeight(y));*/
                m[x][y]=escapes(transWidth(x),transHeight(y));
            }
        }
    }
    private int escapes(double xScale, double yScale)
    {
        double xVal = 0;
        double yVal = 0;
        final int MAX_ITERATION = 1019;
        int iteration = 0;
        while(((xVal*xVal + yVal*yVal)<4)&&iteration<MAX_ITERATION)
        {
            double xTemp = xVal*xVal - yVal*yVal + xScale;
            yVal = yScale + 2*xVal*yVal;
            xVal = xTemp;
            iteration++;
        }
        return iteration;
    }
    public Dimension getPreferredSize()
    {
        return new Dimension(WIDTH, HEIGHT);
    }
    public static void main(String[] args)
    {
        Mandelbrot m = new Mandelbrot();
    }
	class mandListener implements MouseListener,ActionListener
	 {
		public void mousePressed(MouseEvent e)
		{
		}

		public void mouseReleased(MouseEvent e) 
		{
		}

		public void mouseEntered(MouseEvent e) 
		{
		}

		public void mouseExited(MouseEvent e) 
		{
		}

		public void mouseClicked(MouseEvent e) 
		{
			if(e.getButton()==MouseEvent.BUTTON1)
			{
				double[] lastVals = new double[]{beginX,endX,beginY,endY};
				oldVal.push(lastVals);
				int xPos = e.getX();
				int yPos = e.getY();
				double scaleX = transWidth(xPos);
				double scaleY = transHeight(yPos);
				beginX = transWidth(xPos-100);
				endX = transWidth(xPos+100);
				beginY = transHeight(yPos - 50);
				endY = transHeight(yPos + 50);
				fillPixels(pixelVals);
				fillCanvas(pixelVals);
			}
			else if(e.getButton()==MouseEvent.BUTTON3)
			{
				if(!oldVal.empty())
				{
					double[] goBack = oldVal.pop();
					beginX = goBack[0];
					endX = goBack[1];
					beginY = goBack[2];
					endY = goBack[3];
					fillPixels(pixelVals);
					fillCanvas(pixelVals);
				}
			}
		}
		public void actionPerformed(ActionEvent e)
		{
			//After I create a menu, I want this to handle events from the menu
		}
	 }
}