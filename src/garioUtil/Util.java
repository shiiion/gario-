package garioUtil;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

public class Util
{

	public static BufferedImage loadImg(String path)
	{
		BufferedImage image = null;
		try
		{
			image = ImageIO.read(new File(path));
		}
		catch(Exception e)
		{
			ErrorLog.logError("Failed to load image from path " + path);
		}
		
		return image;
	}

	public static BufferedImage makeSolidColorImg(int w, int h, Color c)
	{
		BufferedImage image = new BufferedImage(w, h, BufferedImage.TYPE_4BYTE_ABGR);
		for(int x=0;x<image.getWidth();x++)
		{
			for(int y=0;y<image.getHeight();y++)
			{
				image.setRGB(x, y, c.getRGB());
			}
		}

		return image;
	}

	public static BufferedImage resizeImg(int w, int h, BufferedImage i)
	{
		//return i;
		Image temp = i.getScaledInstance(w, h, Image.SCALE_SMOOTH);
		BufferedImage ret = new BufferedImage(w, h, BufferedImage.TYPE_4BYTE_ABGR);
		
		Graphics g = ret.createGraphics();
		g.drawImage(temp, 0, 0, null);
		g.dispose();
		
		return ret;
	}
	
	public static List<String> readFile(String path)
	{
		List<String> ret = null;
		try
		{
			ret = Files.readAllLines(Paths.get(path), StandardCharsets.US_ASCII);
		}
		catch(Exception e)
		{
			ErrorLog.logError("Failed to read text from path " + path);
		}
		
		return ret;
	}

	public static AudioInputStream loadAudioFile(String path)
	{
		File f = new File(path);
		try
		{
			return AudioSystem.getAudioInputStream(f);
		}
		catch(Exception e)
		{
			return null;
		}
	}
}
