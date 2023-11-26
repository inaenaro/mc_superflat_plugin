package com.inaenaro.superflat.events;

import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.bukkit.Location;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;


public class Data implements Serializable {
    private static transient final long serialVersionUID = 1L;

    public Location coordinates;

    // Can be used for saving
    public Data(Location location) {
        this.coordinates = location;
    }
    // Can be used for loading
    public Data(Data loadedData) {
        this.coordinates = loadedData.coordinates;
    }

    public void saveFileData(String filePath) {
        try {
            BukkitObjectOutputStream out = new BukkitObjectOutputStream(new GZIPOutputStream(Files.newOutputStream(Paths.get(filePath))));
            out.writeObject(this);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static Data loadFileData(String filePath) {
        try {
            BukkitObjectInputStream in = new BukkitObjectInputStream(new GZIPInputStream(Files.newInputStream(Paths.get(filePath))));
            Data data = (Data) in.readObject();
            in.close();
            return data;
        } catch (ClassNotFoundException | IOException e) {
            return null;
        }
    }
    public static void saveData(Location location) {
        new Data(location).saveFileData("Superflat.data");
    }
    public static Data getData() {
        Data loadedData = Data.loadFileData("Superflat.data");
        if (loadedData == null) {
            return null;
        }
        return new Data(loadedData);
    }
}
