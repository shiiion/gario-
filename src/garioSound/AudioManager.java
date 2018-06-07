package garioSound;

import garioUtil.ErrorLog;
import garioUtil.Util;

import javax.sound.sampled.*;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public class AudioManager
{
	private AudioManager(){}

	public static AudioManager inst = new AudioManager();

	private class ClipCounter implements LineListener
	{
		private int numClipsPlaying;

		public void update(LineEvent event)
		{
			LineEvent.Type eType = event.getType();
			if(eType == LineEvent.Type.START)
			{
				synchronized (this)
				{
					numClipsPlaying++;
				}
			}
			else if(eType == LineEvent.Type.STOP)
			{
				synchronized (this)
				{
					numClipsPlaying--;
				}

				event.getLine().close();

				synchronized (clipMaps)
				{
					try
					{
						clipMaps.get(event.getLine()).close();
					}
					catch(Exception e)
					{
						ErrorLog.logError("Failed to close stream, resource leak");
					}
				}
			}
		}

		public int getNumClipsPlaying()
		{
			int ret;
			synchronized (this)
			{
				ret = numClipsPlaying;
			}

			return ret;
		}
	}

	private HashMap<String, String> soundMap = new HashMap<>();
	private HashMap<String, String> BGMMap = new HashMap<>();
	private ClipCounter counter = new ClipCounter();

	private ConcurrentHashMap<Line, AudioInputStream> clipMaps = new ConcurrentHashMap<>();

	//returns true if failed!!~!
	private String playStream(AudioInputStream stream, boolean repeat)
	{
		if(counter.getNumClipsPlaying() > 30)
		{
			return "too many clips";
		}
		try
		{
			AudioFormat fmt = stream.getFormat();
			DataLine.Info info = new DataLine.Info(Clip.class, fmt);
			Clip c = (Clip) AudioSystem.getLine(info);
			c.addLineListener(counter);
			c.open(stream);

			if(repeat)
			{
				c.loop(Clip.LOOP_CONTINUOUSLY);
			}
			c.start();

			synchronized (clipMaps)
			{
				clipMaps.put(c, stream);
			}
		}
		catch(Exception e)
		{
			return "failed to load clip";
		}
		return "";
	}

	public void playSound(String name)
	{
		if(soundMap.containsKey(name))
		{
			//dont worry this gets auto cleaned up :cekcmark":
			AudioInputStream stream = Util.loadAudioFile(soundMap.get(name));
			if(stream != null)
			{
				String error;
				if (!(error = playStream(stream, false)).equals(""))
				{
					ErrorLog.logError("Failed to play sound name: " + name + " | reason: " + error);
				}
			}
			else
			{
				ErrorLog.logError("Failed to load sound name: " + name + " from path " + soundMap.get(name));
			}
		}
	}

	public void playBGM(String name)
	{
		if(BGMMap.containsKey(name))
		{
			AudioInputStream stream = Util.loadAudioFile(BGMMap.get(name));
			if(stream != null)
			{
				String error;
				if (!(error = playStream(stream, true)).equals(""))
				{
					ErrorLog.logError("Failed to play BGM name: " + name + " reason: " + error);
				}
			}
			else
			{
				ErrorLog.logError("Failed to load sound name: " + name + " from path " + soundMap.get(name));
			}
		}
	}

	public void loadSound(String path, String name)
	{
		soundMap.put(name, path);
	}

	public void loadBGM(String path, String name)
	{
		BGMMap.put(name, path);
	}
}
