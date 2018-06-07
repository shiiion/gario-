package gario;
import java.util.ArrayList;
import java.util.List;

import garioEnts.*;
import garioMath.vec2;
import garioUtil.Util;

//buffer between world & file data
public class Level
{
	private ArrayList<Entity> levelEnts;

	public Level()
	{
		levelEnts = new ArrayList<>();
	}

	private Entity parsePlayerLine(String line)
	{
		Player ret = new Player();

		String[] l = line.split(",");

		ret.addCollision(50, 50);
		ret.setPos(new vec2(Double.parseDouble(l[0]), Double.parseDouble(l[1])));

		return ret;
	}

	private Enemy parseGoomba(String[] params)
	{
		Goomba ret = new Goomba();

		ret.addCollision(50, 50);
		ret.setPos(new vec2(Double.parseDouble(params[0]), Double.parseDouble(params[1])));

		return ret;
	}

//	private Enemy parseKoopa(String[] params)
//	{
//		Koopa ret = new Goomba();
//
//		ret.addCollision(50, 50);
//		ret.setPos(new vec2(Double.parseDouble(params[0]), Double.parseDouble(params[1])));
//
//		return ret;
//	}

	private Entity parseEnemyLine(String line)
	{
		Enemy ret = null;

		String[] l = line.split(",");

		switch(l[0])
		{
		case "g":
			ret = parseGoomba(l);
			break;
		case "k":
//			ret = parseKoopa(l);
			break;
		default:
			break;
		}
		return ret;
	}

	private Entity parseWorldLine(String line)
	{
		Entity ret = new Entity(true);

		String[] l = line.split(",");

		ret.addCollision(Double.parseDouble(l[2]), Double.parseDouble(l[3]));
		ret.setPos(new vec2(Double.parseDouble(l[0]), Double.parseDouble(l[1])));

		return ret;
	}

	private Entity parseGoalLine(String line)
	{
		Goal ret = new Goal();

		String[] l = line.split(",");

		ret.addCollision(Double.parseDouble(l[2]), Double.parseDouble((l[3])));
		ret.setPos(new vec2(Double.parseDouble(l[0]), Double.parseDouble((l[1]))));

		return ret;
	}

	public void load(String file)
	{
		levelEnts.clear();

		List<String> lines = Util.readFile(file);
		if(lines == null) return;

		for(String s : lines)
		{
			switch(s.charAt(0))
			{
			//start pos
			case 's':
				levelEnts.add(parsePlayerLine(s.substring(1)));
				break;
			case 'e':
				levelEnts.add(parseEnemyLine(s.substring(1)));
				break;
			case 'w':
				levelEnts.add(parseWorldLine(s.substring(1)));
				break;
			case 'g':
				levelEnts.add(parseGoalLine(s.substring(1)));
				break;
			}
		}
	}

	public List<Entity> getLevelEnts()
	{
		return levelEnts;
	}
}
