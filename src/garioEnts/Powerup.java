package garioEnts;

import gario.World;
import garioMath.vec2;

public abstract class Powerup extends Entity implements ControllableObject
{
	public Powerup()
	{
		super(false);
	}

	public int getType()
	{
		return 3;
	}
	
	public abstract POWER getPower();
	
	public void onCollision(Entity other, vec2 resolutionVector)
	{
		super.onCollision(other, resolutionVector);
		
		if(other.getType() == 1)
		{
			World.inst.removalQueue(this);
		}
	}
}
