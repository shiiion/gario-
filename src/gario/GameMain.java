package gario;

import java.awt.Color;

import garioEnts.Entity;
import garioEnts.Player;
import garioMath.vec2;
import garioSound.AudioManager;
import garioUtil.ErrorLog;
import garioUtil.GarioGlobals;
import garioUtil.Window;

public class GameMain
{
	private GraphicsManager gMgr;
	private long lastCallTime;

	public static void main(String[] args)
	{
		(new GameMain()).run();
	}

	private double getElapsedTime()
	{
		long curTime = System.currentTimeMillis();
		double ret = (double) (curTime - lastCallTime) / 1000.0;
		lastCallTime = curTime;
		return .016;
	}

	void run()
	{
		GarioGlobals.SES_WND = new Window(1000, 800, "test window");
		GarioGlobals.SES_WND.setVisible(true);

		GarioGlobals.SES_WORLD = World.inst;

		// quick access
		World w = World.inst;
		{
			GarioBackground epic = new GarioBackground("whyy", true, 0.1);

			Entity e = new Entity(true);
			e.addCollision(30000, 50);
			e.setSpriteName("ground");
			e.setPos(new vec2(100, 450));
			Entity e2 = new Entity(false);
			e2.addCollision(50, 50);
			e2.setSpriteName("player");
			e2.setPos(new vec2(500, 200));
			e2.setVel(new vec2(50, -50));
			Player p = new Player();
			p.setPos(new vec2(150, 50));
			//p.setVel(new vec2(0, -200));
			p.addCollision(50, 50);

			w.addEntity(e);
			w.addEntity(e2);
			w.addEntity(p);
			w.setBackground(epic);
			GarioGlobals.TRACK_ENT = p;
		}
		
		gMgr = new GraphicsManager(1000, 800);
		
		gMgr.loadSprite("c:\\d\\gariotest\\player.png", "player");
		gMgr.loadSprite(30000, 50, Color.green, "ground");
		gMgr.loadSprite("c:\\d\\gariotest\\back1.png", "whyy");

		lastCallTime = System.currentTimeMillis();

		while (true)
		{
			gMgr.stepSprites();

			// step phys
			w.stepEntities(getElapsedTime());
			w.moveAllControllables();
			w.postStep();

			gMgr.acquireGraphics(GarioGlobals.SES_WND);
			gMgr.clear(Color.black);
			
			gMgr.renderWorld(w);

			gMgr.releaseGraphics(GarioGlobals.SES_WND);


			try{Thread.sleep(8);} catch (Exception e){}
		}
	}
}
