package gario;

import java.util.ArrayList;
import java.util.List;

import garioEnts.ControllableObject;
import garioEnts.Entity;
import garioMath.vec2;
import garioUtil.GarioGlobals;

public class World
{
	public static vec2 GRAVITY = new vec2(0, 500);
	public static double GLOBAL_FRICTION = 2;
	public static World inst = new World();

	private ArrayList<Entity> entList;
	private ArrayList<Entity> removeList;
	private EntityPartitioner collisionMgr;
	private GarioBackground background;

	private World()
	{
		entList = new ArrayList<>();
		removeList = new ArrayList<>();
		collisionMgr = new DynamicEntityPartitioner();
		background = new GarioBackground("", false, 0);
	}

	public void removalQueue(Entity e)
	{
		removeList.add(e);
	}

	public void setBackground(GarioBackground bg)
	{
		background = bg;
	}

	public GarioBackground getBackground()
	{
		return background;
	}

	public void addEntity(Entity e)
	{
		entList.add(e);
	}

	public void addEntities(List<Entity> ents)
	{
		entList.addAll(ents);
	}

	public void removeEntity(int index)
	{
		entList.remove(index);
	}

	public void removeEntity(Entity e)
	{
		entList.remove(e);
	}

	public Entity getEntity(int i)
	{
		return entList.get(i);
	}

	public int getListSize()
	{
		return entList.size();
	}

	private static class SimpleCollisionResolve implements CollisionEvent
	{
		// stacking is a bad idea
		// A guaranteed non-static
		public void onCollision(Entity a, Entity b)
		{
			vec2 overlapVector = a.getOverlapVector(b);

			if (b.isStatic())
			{
				a.addPos(overlapVector);
				a.onCollision(b, overlapVector);
			}
			else
			{
				vec2 halfOverlap = overlapVector.mul(0.5);
				a.addPos(halfOverlap);
				b.addPos(halfOverlap.mul(-1));
				a.onCollision(b, halfOverlap);
				b.onCollision(a, halfOverlap.mul(-1));
			}

		}
	}

	private static final SimpleCollisionResolve rMethod = new SimpleCollisionResolve();

	public void stepEntities(double frameTime)
	{
		GarioGlobals.FRAME_TIME = frameTime;


		for (Entity e : entList)
		{
			e.stepEntity(frameTime);
		}

		collisionMgr.collisionTestAndResolve(entList, rMethod);

		//for stepping the background
		if(GarioGlobals.TRACK_ENT != null)
		{
			background.rotate(GarioGlobals.TRACK_ENT.getPos().x, GarioGlobals.SES_WND.getWidth());
		}
	}

	public void moveAllControllables()
	{
		for (Entity e : entList)
		{
			if (e instanceof ControllableObject)
			{
				ControllableObject co = (ControllableObject) e;
				co.move();
			}
		}
	}

	public void postStep()
	{
		for (Entity e : removeList)
		{
			entList.remove(e);
		}
		removeList.clear();
	}
}
