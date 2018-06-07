package gario;

public class GarioBackground
{
	public String name;
	public boolean repeatable;
	public double rotateSpeed;

	private double percentRotated;

	public GarioBackground(String name, boolean repeatable, double rotateSpeed)
	{
		this.name = name;
		this.repeatable = repeatable;
		this.rotateSpeed = rotateSpeed;
		percentRotated = 0;
	}

	public void rotate(double xPos, double width)
	{
		if(repeatable)
		{
			long l1 = (long)xPos;
			long w = (long)(width / rotateSpeed);
			long modu = l1 % w;
			percentRotated = ((double)modu / (width / rotateSpeed));
		}
	}

	public double getPercentRotated() { return percentRotated; }

}
