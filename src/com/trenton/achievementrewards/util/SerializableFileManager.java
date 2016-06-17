package com.trenton.achievementrewards.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Achievement;

public class SerializableFileManager {
	
	private static final String DATA_PATH = "./plugins/AchRewards/";
	
	@SuppressWarnings("unchecked")
	public synchronized static HashMap<String, ArrayList<Achievement>> loadMissedAchievementMap() {
		try {
			return (HashMap<String, ArrayList<Achievement>>) loadFile(new File(DATA_PATH + "missedAchievements.ach"));
		} catch (Throwable e) {
			System.out.print("Error loading missed achievement map.");
		}
		return new HashMap<String, ArrayList<Achievement>>();
	}
	
	public synchronized static void saveMissedAchievementMap(HashMap<String, ArrayList<Achievement>> map) {
		try {
			writeFile(map, new File(DATA_PATH + "missedAchievements.ach"));
		} catch (Throwable e) {
			System.out.println("Error saving missed achievement map.");
		}
	}
	
	public static final Object loadFile(File file) throws IOException, ClassNotFoundException {
		if (!file.exists())
			return null;
		ObjectInputStream in = new ObjectInputStream(new FileInputStream(file));
		Object object = in.readObject();
		in.close();
		return object;
	}

	public static final void writeFile(Serializable object, File file) throws IOException {
		ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file));
		out.writeObject(object);
		out.close();
	}

}
