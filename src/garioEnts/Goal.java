package garioEnts;

public class Goal extends Entity
{
	public Goal()
	{
		super(true);
		this.setSpriteName("goal");
	}

	public int getType() { return 50; }
}
