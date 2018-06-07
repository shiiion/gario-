package garioUtil;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.concurrent.locks.ReentrantLock;

import javax.swing.JFrame;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class Window extends JFrame
{
	
	private class DrawSurface extends JPanel
	{
		private BufferedImage backBuffer;
		private Graphics backBufferGraphics;
		
		public ReentrantLock backBufferLock;
		
		DrawSurface(int width, int height)
		{
			backBufferLock = new ReentrantLock();
			
			Dimension d = new Dimension(width, height);
			setPreferredSize(d);
			
			backBuffer = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
			backBufferGraphics = backBuffer.getGraphics();
		}
		
		@Override
		public void paintComponent(Graphics g)
		{
			backBufferLock.lock();
			g.drawImage(backBuffer, 0, 0, backBuffer.getWidth(), backBuffer.getHeight(), null);
			backBufferLock.unlock();
		}
		
		public Graphics getBackBufferGfx()
		{
			return backBufferGraphics;
		}
	}
	private class WindowDrawMgr implements Runnable
	{
		private Window window;
		
		WindowDrawMgr(Window window)
		{
			this.window = window;
		}
		
		public void run()
		{
			while(running)
			{
				window.repaint();
				try {Thread.sleep(1);}catch(Exception e){}
			}
		}
	}
	private class KeyboardMgr implements KeyListener
	{
		private HashMap<Integer, Boolean> stateMap = new HashMap<Integer, Boolean>();
		
		@Override
		public void keyPressed(KeyEvent e)
		{
			stateMap.put(e.getKeyCode(), true);
		}

		@Override
		public void keyReleased(KeyEvent e)
		{
			stateMap.put(e.getKeyCode(), false);
		}

		public void keyTyped(KeyEvent e){}

		public boolean getKeyState(int keyCode)
		{
			return (stateMap.containsKey(keyCode) ? stateMap.get(keyCode) : false);
		}
	}
	
	private DrawSurface surface;
	private boolean running;
	private WindowDrawMgr drawMgr;
	private Thread drawMgrThread;
	private KeyboardMgr inputMgr;
	
	public Window(int clientWidth, int clientHeight, String title)
	{
		super(title);
		
		surface = new DrawSurface(clientWidth, clientHeight);
		drawMgr = new WindowDrawMgr(this);
		
		add(surface);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//center window
		{
			Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
			setLocation((d.width / 2) - (clientWidth / 2), (d.height / 2) - (clientHeight / 2));
		}
		
		this.addKeyListener(inputMgr = new KeyboardMgr());
		
		pack();
	}
	
	@Override
	public void setVisible(boolean b)
	{
		super.setVisible(b);
		if(!running)
		{
			drawMgrThread = new Thread(drawMgr);
			drawMgrThread.start();
		}
		running = b;
	}
	
	public boolean getKeyState(int keyCode)
	{
		return inputMgr.getKeyState(keyCode);
	}
	
	//must lock around usage of this
	public Graphics acquireGraphics()
	{
		surface.backBufferLock.lock();
		return surface.getBackBufferGfx();
	}
	
	public void releaseGraphics()
	{
		surface.backBufferLock.unlock();
	}
}
