package map;

import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

public class MapHandler implements Runnable {

public static final String MAT = "_mat.png";
public static final String ADD = "_add.png";
public static final String COLOURED = "_coloured.png";
public static final String FRONT_MAP_NAME = "front";
public static final String LEFT_MAP_NAME = "left";
public static final String RIGHT_MAP_NAME = "right";
public static final String UP_MAP_NAME = "up";
public static final String DOWN_MAP_NAME = "down";
public static final String BACK_MAP_NAME = "back";


public static final String[] MAP_NAMES = {FRONT_MAP_NAME, LEFT_MAP_NAME, RIGHT_MAP_NAME, UP_MAP_NAME, DOWN_MAP_NAME, BACK_MAP_NAME};

	MapData mapData;
	String inputPath;
	String outputPath;
	boolean surfaceHintMaps;
	boolean colouredMaps;
	
	public MapHandler(MapData mapData, String inputPath, String outputPath, boolean surfaceHintMaps, boolean colouredMaps) {
		this.mapData = mapData;
		this.inputPath = inputPath;
		this.outputPath = outputPath;
		this.surfaceHintMaps = surfaceHintMaps;
		this.colouredMaps = colouredMaps;
	}
	
	public MapData loadMapData() {
		for(int i = 0; i < MAP_NAMES.length; ++i) {
			String mapName = MAP_NAMES[i];
			try {
				mapData.images[i] = ImageIO.read(Paths.get(inputPath, mapName + MAT).toFile());
				if(surfaceHintMaps) {
					mapData.surfaceHintImages[i] = ImageIO.read(Paths.get(inputPath, mapName + ADD).toFile());
				}
				if(colouredMaps) {
					mapData.colouredMaps[i] = ImageIO.read(Paths.get(inputPath, mapName + MAT).toFile());
				}
			}
			catch(Exception e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(null, "Unable to load image files from directory:\n" + inputPath, "File Read Error", JOptionPane.ERROR_MESSAGE);
				return null;
			}
		}
		mapData.calculateMapSize();

		return mapData;
	}
	
	public void writeMapData() {
		try {
			
			for(int i = 0; i < mapData.images.length; ++i) {
				if(mapData.images[i] == null) {
					continue;
				}
				String mapName = MAP_NAMES[i];
				Path path = Paths.get(outputPath);
				if(Files.notExists(path)) {
					Files.createDirectories(path);
				}
				ImageIO.write(mapData.images[i], "png" , Paths.get(outputPath, mapName + MAT).toFile());
				if(surfaceHintMaps) {
					
					ImageIO.write(mapData.surfaceHintImages[i], "png" , Paths.get(outputPath, mapName + ADD).toFile());
				}
				if(colouredMaps) {
					ImageIO.write(mapData.colouredMaps[i], "png" , Paths.get(outputPath, mapName + COLOURED).toFile());
				}
			}
		}
		catch(Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Unable to create image files in directory:\n" + outputPath, "File Write Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
	}

	@Override
	public void run() {
		//write
		writeMapData();
	}
}
