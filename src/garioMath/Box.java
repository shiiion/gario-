package garioMath;

public class Box
{
	public vec2 pos;
	public vec2 dims;
	
	public Box(double x, double y, double w, double h)
	{
		pos = new vec2(x, y);
		dims = new vec2(w, h);
	}
	
	//shallow reference copy
	public Box(vec2 pos, vec2 dims)
	{
		this.pos = pos;
		this.dims = dims;
	}
	
	public boolean contains(vec2 point)
	{
		return (point.x > pos.x && point.y > pos.y && point.x < (pos.x + dims.x) && point.y < (pos.y + dims.y));
	}

	private boolean testOverlapY(Box other)
	{
		if(pos.y < other.pos.y && (pos.y + dims.y) > other.pos.y)
		{
			return true;
		}

		if((pos.y + dims.y) > (other.pos.y + other.dims.y) && pos.y < (other.pos.y + other.dims.y))
		{
			return true;
		}

		if(pos.y >= other.pos.y && (pos.y + dims.y) <= (other.pos.y + other.dims.y))
		{
			return true;
		}

		return false;
	}

	//aabb test
	public boolean intersects(Box other)
	{
//		vec2 p1 = pos;
//		vec2 p2 = p1.add(new vec2(dims.x, 0));
//		vec2 p3 = p1.add(dims);
//		vec2 p4 = p1.add(new vec2(0, dims.y));
//
//		vec2 p1o = other.pos;
//		vec2 p2o = p1o.add(new vec2(other.dims.x, 0));
//		vec2 p3o = p1o.add(other.dims);
//		vec2 p4o = p1o.add(new vec2(0, other.dims.y));
//
//		return other.contains(p1) || other.contains(p2) || other.contains(p3) || other.contains(p4) ||
//				contains(p1o) || contains(p2o) || contains(p3o) || contains(p4o);


		//x overlap right
		if(pos.x < other.pos.x && (pos.x + dims.x) > other.pos.x)
		{
			return testOverlapY(other);
		}

		//x overlap left
		if((pos.x + dims.x) > (other.pos.x + other.dims.x) && pos.x < (other.pos.x + other.dims.x))
		{
			return testOverlapY(other);
		}

		if(pos.x >= other.pos.x && (pos.x + dims.x) <= (other.pos.x + other.dims.x))
		{
			return testOverlapY(other);
		}

		return false;
	}
	
	
	//precond: boxes intersect
	//result: MTV along x or y axis for no overlap
	public vec2 getIntersectVector(Box other)
	{
		double xOverlap, yOverlap;
		vec2 opos = other.pos;
		vec2 odim = other.dims;

		if(this.dims.x < 100 && other.dims.x < 100)
		{
			int a =0;
		}

		if(pos.x < opos.x)
		{
			xOverlap = opos.x - (pos.x + dims.x);
		}
		else if(pos.x + dims.x > (opos.x + odim.x))
		{
			xOverlap = (opos.x + odim.x) - pos.x;
		}
		else
		{
			double rightTV = (opos.x + odim.x) - pos.x;
			double leftTV = opos.x - (pos.x + dims.x);
			xOverlap = (Math.abs(rightTV) < Math.abs(leftTV) ? rightTV : leftTV);
		}
		
		if(pos.y < opos.y)
		{
			yOverlap =  opos.y - (pos.y + dims.y);
		}
		else if(pos.y + dims.y > (opos.y + odim.y))
		{
			yOverlap = (opos.y + odim.y) - pos.y;
		}
		else
		{
			double downTV = (opos.y + odim.y) - pos.y;
			double upTV = opos.y - (pos.y + dims.y);
			yOverlap = (Math.abs(upTV) < Math.abs(downTV) ? upTV : downTV);
		}
		
		//gives slight preference to vertical resolves?haha
		if(Math.abs(xOverlap) >= Math.abs(yOverlap))
		{
			xOverlap = 0;
		}
		else
		{
			yOverlap = 0;
		}
		
		return new vec2(xOverlap, yOverlap);
	}
}