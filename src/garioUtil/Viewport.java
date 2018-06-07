package garioUtil;

import garioMath.Box;
import garioMath.vec2;

public class Viewport extends Box
{
	public Viewport(double x, double y, double w, double h)
	{
		super(x, y, w, h);
	}

	public void recenter(vec2 point)
	{
		pos.x = point.x - dims.x / 2;
		pos.y = point.y - dims.y / 2;
	}

	public vec2 translate(vec2 point)
	{
		return point.sub(pos);
	}
}
