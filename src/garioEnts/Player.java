package garioEnts;

import java.awt.event.KeyEvent;

import garioMath.vec2;
import garioUtil.GarioGlobals;
import garioUtil.Window;

enum POWER
{
	small,
	large,
	fire,
	invincible
}

public class Player extends Entity implements ControllableObject
{
	private POWER playerPower;
	
	public static double HVEL_WALK_MAX = 1000;
	
	private double moveAccel;
	private double jumpVelAmt;

	private boolean canJump;
	private boolean onEnemy;
	
	public Player()
	{
		super(false);
		canJump = false;
		moveAccel = 3000;
		jumpVelAmt = 500;
		this.setSpriteName("player");
	}
	
	//we load hardcoded resources here
	
	public POWER getPower()
	{
		return playerPower;
	}
	
	public void setPower(POWER p)
	{
		playerPower = p;
	}
	
	public void stepEntity(double frameTime)
	{
		super.stepEntity(frameTime);
		
		if(onEnemy)
		{
			getVel().y = -jumpVelAmt;
		}
	}
	
	public int getType()
	{
		return 1;
	}

	public void setMoveAccel(double v)
	{
		moveAccel = v;
	}

	public void setJumpVel(double v)
	{
		jumpVelAmt = v;
	}

	@Override
	public void onCollision(Entity other, vec2 resolutionVector)
	{
		super.onCollision(other, resolutionVector);
		
		if(resolutionVector.y < 0) //resolve upwards = we above
		{
			switch(other.getType())
			{
			case 0:
				canJump = true;
				break;
			case 1:
				onEnemy = true;
				break;
			case 3://powers
				setPower(((Powerup)other).getPower());
				break;
			}
		}

		if(other.getType() == 50)
		{
			//post end map status success
		}
	}
	
	public void move()
	{
		Window wnd = GarioGlobals.SES_WND;
		vec2 playerVel = getVel();
		
		if(wnd.getKeyState(KeyEvent.VK_LEFT))
		{
			if(playerVel.x > -HVEL_WALK_MAX)
			{
				playerVel.x -= (moveAccel * GarioGlobals.FRAME_TIME);
				playerVel.x = Math.max(playerVel.x, -HVEL_WALK_MAX);
			}
		}
		if(wnd.getKeyState(KeyEvent.VK_RIGHT))
		{
			if(playerVel.x < HVEL_WALK_MAX)
			{
				playerVel.x += (moveAccel * GarioGlobals.FRAME_TIME);
				playerVel.x = Math.min(playerVel.x, HVEL_WALK_MAX);
			}
		}
		if(wnd.getKeyState(KeyEvent.VK_SPACE))
		{
			if(canJump)
			{
				playerVel.y = -jumpVelAmt;
				canJump = false;
			}
		}
	}
}
