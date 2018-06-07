package gario;
import java.awt.*;
import java.awt.image.BufferedImage;

import garioMath.vec2;
import garioUtil.Util;

public class Sprite
{
	private boolean isAnimated;
	private boolean loaded;
	
	private BufferedImage staticSprite;
	
	private BufferedImage[] animatedSprite;
	private int curFrame;
	private int frameTime;
	private long lastFrameStepTime;
	
	private vec2 size;

	public Sprite(int w, int h, Color color)
	{
		isAnimated = false;
		staticSprite = Util.makeSolidColorImg(w, h, color);
		loaded = (staticSprite != null);

		if(loaded)
		{
			size = new vec2(staticSprite.getWidth(), staticSprite.getHeight());
		}
	}

	//loads a static image
	public Sprite(String path)
	{
		isAnimated = false;
		staticSprite = Util.loadImg(path);
		loaded = (staticSprite != null);

		if(loaded)
		{
			size = new vec2(staticSprite.getWidth(), staticSprite.getHeight());
		}
	}

	//loads a sequence of images in order of arr
	public Sprite(String[] paths, int frameTime)
	{
		curFrame = 0;
		this.frameTime = frameTime;
		lastFrameStepTime = System.currentTimeMillis();
		
		isAnimated = true;
		BufferedImage[] tmp = new BufferedImage[paths.length];
		animatedSprite = null;
		
		{
			int counter = 0;
			int w = Integer.MAX_VALUE;
			int h = Integer.MAX_VALUE;
			
			for(String s : paths)
			{
				tmp[counter] = Util.loadImg(s);
				if(tmp[counter] == null)
				{
					break;
				}
				if(tmp[counter].getWidth() < w && tmp[counter].getHeight() < h)
				{
					w = tmp[counter].getWidth();
					h = tmp[counter].getHeight();
				}
				counter++;
			}
			
			if(loaded = (counter == paths.length))
			{
				for(int a=0;a<tmp.length;a++)
				{
					tmp[a] = Util.resizeImg(w, h, tmp[a]);
				}
				
				size = new vec2(w, h);
				animatedSprite = tmp;
			}
		}
	}
	
	
	public boolean loaded()
	{
		return loaded;
	}
	
	public boolean animated()
	{
		return isAnimated;
	}
	
	public BufferedImage getFrame()
	{
		if(loaded)
		{
			if(isAnimated)
			{
				return animatedSprite[curFrame];
			}
			else
			{
				return staticSprite;
			}
		}
		return null;
	}

	public void stepFrame()
	{
		if(isAnimated)
		{
			long curTime = System.currentTimeMillis();
			int numSteps = ((int)(curTime - lastFrameStepTime) / frameTime);
			curFrame = (curFrame + numSteps) % animatedSprite.length;
			lastFrameStepTime = (numSteps > 0 ? curTime : lastFrameStepTime);
		}
	}
	
	static final vec2 INVALID_SIZE = new vec2(-1, -1);
	public vec2 size()
	{
		if(loaded)
		{
			return size;
		}
		return INVALID_SIZE;
	}
}
