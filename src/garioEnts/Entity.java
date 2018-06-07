package garioEnts;
import garioMath.vec2;
import gario.World;
import garioMath.Box;

//base object
public class Entity
{
	private vec2 position;
	private vec2 velocity;
	
	private Box collision;
	
	private String sprite;
	
	private boolean isStatic;
	
	public Entity(boolean isStatic)
	{
		position = new vec2(0, 0);
		velocity = new vec2(0, 0);
		this.isStatic = isStatic;
	}
	
	public void setPos(vec2 p)
	{
		position.x = p.x;
		position.y = p.y;
	}
	
	public void addPos(vec2 a)
	{
		position.x += a.x;
		position.y += a.y;
	}
	
	public void setVel(vec2 v)
	{
		velocity = v;
	}
	
	public vec2 getPos()
	{
		return position;
	}
	
	public vec2 getVel()
	{
		return velocity;
	}
	
	public boolean collides(Entity other)
	{
		if(collision == null || other.collision == null)
		{
			return false;
		}
		
		return collision.intersects(other.collision);
	}
	
	public vec2 getOverlapVector(Entity other)
	{
		if(collision == null || other.collision == null)
		{
			return vec2.ZERO;
		}
		
		return collision.getIntersectVector(other.collision);
	}
	
	public void addCollision(double w, double h)
	{
		collision = new Box(position, new vec2(w, h));
	}
	
	public void removeCollision()
	{
		collision = null;
	}
	
	public void stepEntity(double frameTime)
	{
		//maintain our original reference
		position.x += frameTime * velocity.x;
		position.y += frameTime * velocity.y;
		
		if(collision != null && !isStatic)
		{
			velocity = velocity.add(World.GRAVITY.mul(frameTime));
			double invDir = -Math.signum(velocity.x);
			double xVel = velocity.x;
			double frictAmt = frameTime * (World.GLOBAL_FRICTION * World.GRAVITY.y);
			frictAmt = Math.min(frictAmt, Math.abs(xVel));
			xVel += frictAmt * invDir;
			velocity.x = xVel;
		}
	}

	public boolean isStatic()
	{
		return isStatic;
	}
	
	public void setStatic(boolean s)
	{
		isStatic = s;
	}
	
	public String getSpriteName()
	{
		return sprite;
	}
	
	public void setSpriteName(String sname)
	{
		sprite = sname;
	}
	
	public boolean canCollide()
	{
		return collision != null;
	}
	
	public void onCollision(Entity other, vec2 resolutionVector)
	{
		if(resolutionVector.x == 0)
		{
			this.velocity.y = 0;
		}
		else
		{
			this.velocity.x = 0;
		}
	}
	
	public int getType()
	{
		return 0;
	}

	public vec2 getCenter()
	{
		if(canCollide())
		{
			return getPos().add(collision.dims);
		}
		else
		{
			return getPos();
		}
	}
}
