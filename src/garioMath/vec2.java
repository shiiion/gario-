package garioMath;

public class vec2
{
	public static vec2 ZERO = new vec2(0, 0);
	
	public double x;
	public double y;
	
	public vec2(double x, double y)
	{
		this.x = x;
		this.y = y;
	}
	
	public vec2()
	{
		this(0, 0);
	}
	
	public vec2 add(vec2 other)
	{
		return new vec2(x + other.x, y + other.y);
	}
	
	public vec2 sub(vec2 other)
	{
		return new vec2(x - other.x, y - other.y);
	}
	
	public vec2 mul(double scalar)
	{
		return new vec2(x * scalar, y * scalar);
	}
	
	public double mag2()
	{
		return (x * x) + (y * y);
	}
	
	public double mag()
	{
		return Math.sqrt(mag2());
	}
}
