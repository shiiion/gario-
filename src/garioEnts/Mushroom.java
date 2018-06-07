package garioEnts;

import garioMath.vec2;

public class Mushroom extends Powerup
{
	private int moveDir;
	public static double MUSHROOM_MOVE_SPEED = 4;
	
	public Mushroom()
	{
		this.setSpriteName("p_mushroom");
		moveDir = 1;
	}

	public POWER getPower()
	{
		return POWER.large;
	}

	public void onCollision(Entity other, vec2 resolutionVector)
	{
		super.onCollision(other, resolutionVector);
		if(resolutionVector.x != 0)
		{
			moveDir = -moveDir;
		}
	}
	
	public void move()
	{
		this.getVel().x = moveDir * MUSHROOM_MOVE_SPEED;
	}
}
