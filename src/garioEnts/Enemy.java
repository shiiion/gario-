package garioEnts;

public abstract class Enemy extends Entity implements ControllableObject
{
	public Enemy()
	{
		super(false);
	}
	
	public int getType()
	{
		return 2;
	}
}
