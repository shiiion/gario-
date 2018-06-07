package garioEnts;

import garioMath.vec2;

public class Star extends Powerup
{
	public static double STAR_JUMP_SPEED = 10;
	
	public Star()
	{
		this.setSpriteName("p_star");
	}
	
	public POWER getPower()
	{
		return POWER.invincible;
	}
	
	public void onCollision(Entity other, vec2 resolutionVector)
	{
		super.onCollision(other, resolutionVector);
		if(resolutionVector.y < 0)
		{
			getVel().y = -STAR_JUMP_SPEED;
		}
		if(resolutionVector.x != 0)
		{
			getVel().x = -getVel().x;
		}
	}
	
	public void move()
	{
	}
}
