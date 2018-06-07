package gario;
import java.awt.Color;
import java.awt.Graphics;
import java.util.HashMap;
import java.util.Map;

import garioEnts.Entity;
import garioMath.Box;
import garioMath.vec2;
import garioUtil.ErrorLog;
import garioUtil.GarioGlobals;
import garioUtil.Viewport;
import garioUtil.Window;

public class GraphicsManager
{
	private HashMap<String, Sprite> spriteCache;
	
	private Graphics __temp;
	
	private Viewport vp;
	
	public GraphicsManager(int width, int height)
	{
		spriteCache = new HashMap<>();
		vp = new Viewport(0, 0, width, height);
		__temp = null;
	}

	public boolean loadSprite(int width, int height, Color c, String name)
	{
		Sprite s = new Sprite(width, height, c);
		return addSprite(s, name);
	}

	public boolean loadSprite(String path, String name)
	{
		Sprite s = new Sprite(path);
		return addSprite(s, name);
	}
	
	public boolean loadSprite(String[] paths, int frameTime, String name)
	{
		Sprite s = new Sprite(paths, frameTime);
		return addSprite(s, name);
	}
	
	public boolean addSprite(Sprite s, String name)
	{
		if(s.loaded())
		{
			spriteCache.put(name, s);
		}
		return s.loaded();
	}
	
	public void acquireGraphics(Window w)
	{
		if(__temp != null)
		{
			ErrorLog.logError("Trying to acquire already acquired graphics object");
			return;
		}
		__temp = w.acquireGraphics();
	}
	
	public void releaseGraphics(Window w)
	{
		if(__temp == null)
		{
			ErrorLog.logWarn("Trying to release unacquired graphics object");
			return;
		}
		w.releaseGraphics();
		__temp = null;
	}
	
	public void clear(Color clearColor)
	{
		if(__temp == null) return;
		__temp.setColor(clearColor);
		__temp.fillRect(0, 0, (int)vp.dims.x, (int)vp.dims.y);
	}
	
	public void drawLine(vec2 start, vec2 end, Color c)
	{
		if(__temp == null) return;
		if(!vp.contains(start) && !vp.contains(end)) return;
		
		vec2 ts = vp.translate(start);
		vec2 te = vp.translate(end);
		long x1 = Math.round(ts.x), y1 = Math.round(ts.y), x2 = Math.round(te.x), y2 = Math.round(te.y);

		__temp.setColor(c);
		__temp.drawLine((int)x1, (int)y1, (int)x2, (int)y2);
	}
	
	public void drawBox(Box b, Color c)
	{
		if(__temp == null) return;
		if(!vp.intersects(b)) return;
		
		vec2 pos = vp.translate(b.pos);
		long x = Math.round(pos.x), y = Math.round(pos.y), w = Math.round(b.dims.x), h = Math.round(b.dims.y);
		
		__temp.setColor(c);
		__temp.drawRect((int)x, (int)y, (int)w, (int)h);
	}
	
	public void fillBox(Box b, Color c)
	{
		if(__temp == null) return;
		if(!vp.intersects(b)) return;

		vec2 pos = vp.translate(b.pos);
		long x = Math.round(pos.x), y = Math.round(pos.y), w = Math.round(b.dims.x), h = Math.round(b.dims.y);
		
		__temp.setColor(c);
		__temp.fillRect((int)x, (int)y, (int)w, (int)h);
	}

	public void drawSprite(String name, vec2 location, vec2 scaleTo, boolean useViewport)
	{
		if(spriteCache.containsKey(name))
		{
			drawSprite(spriteCache.get(name), location, scaleTo, useViewport);
		}
	}

	public void drawSprite(Sprite s, vec2 location, vec2 scaleTo, boolean useViewport)
	{
		if(__temp == null) return;

		Box spritePos = new Box(location.x, location.y, s.size().x, s.size().y);
		vec2 pos = location;

		if(useViewport)
		{
			if (!vp.intersects(spritePos))
				return;
			pos = vp.translate(location);
		}

		long x = Math.round(pos.x), y = Math.round(pos.y), w = Math.round(scaleTo.x), h = Math.round(scaleTo.y);

		__temp.drawImage(s.getFrame(), (int)x, (int)y, (int)w, (int)h, null);
	}

	public void drawSprite(String name, vec2 location, boolean useViewport)
	{
		if(spriteCache.containsKey(name))
		{
			drawSprite(spriteCache.get(name), location, useViewport);
		}
	}
	
	public void drawSprite(Sprite s, vec2 location, boolean useViewport)
	{
		if(__temp == null) return;
		
		Box spritePos = new Box(location.x, location.y, s.size().x, s.size().y);
		vec2 pos = location;

		if(useViewport)
		{
			if (!vp.intersects(spritePos))
				return;
			pos = vp.translate(location);
		}

		long x = Math.round(pos.x), y = Math.round(pos.y);
		
		__temp.drawImage(s.getFrame(), (int)x, (int)y, null);
	}

	public void drawBackground(GarioBackground b)
	{
		vec2 wdims = new vec2(GarioGlobals.SES_WND.getWidth(), GarioGlobals.SES_WND.getHeight());

		if(b.repeatable)
		{
			double backRotate = wdims.x * b.getPercentRotated();
			drawSprite(b.name, vec2.ZERO.sub(new vec2(backRotate, 0)), wdims, false);
			drawSprite(b.name, vec2.ZERO.add(new vec2(wdims.x - backRotate, 0)), wdims, false);
		}
		else
		{
			drawSprite(b.name, vec2.ZERO, wdims, false);
		}
	}

	public void renderWorld(World w)
	{
		if(GarioGlobals.TRACK_ENT != null)
		{
			vp.recenter(GarioGlobals.TRACK_ENT.getCenter());
		}

		if(w.getBackground() != null)
		{
			drawBackground(w.getBackground());
		}

		for(int a=0;a<w.getListSize();a++)
		{
			Entity e = w.getEntity(a);
			drawSprite(e.getSpriteName(), e.getPos(), true);
		}
	}

	public void stepSprites()
	{
		for(Map.Entry<String, Sprite> kv : spriteCache.entrySet())
		{
			kv.getValue().stepFrame();
		}
	}
}
