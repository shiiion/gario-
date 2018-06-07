package gario;
import java.util.List;

import garioEnts.Entity;


//test & resolve in one pass
public interface EntityPartitioner
{
	void collisionTestAndResolve(List<Entity> entList, CollisionEvent ce);
}

//resolution expected
interface CollisionEvent
{
	void onCollision(Entity a, Entity b);
}

//n^2 runtime, cuts out static collision tests
class DynamicEntityPartitioner implements EntityPartitioner
{
	public void collisionTestAndResolve(List<Entity> entList, CollisionEvent ce)
	{
		for(Entity e : entList)
		{
			if(e.canCollide() && !e.isStatic())
			{
				for(int a=0;a<entList.size();a++)
				{
					if(entList.get(a) == e) continue;
					if(e.collides(entList.get(a))) ce.onCollision(e, entList.get(a));
				}
			}
		}
	}
}