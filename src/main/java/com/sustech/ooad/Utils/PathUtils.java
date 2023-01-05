package com.sustech.ooad.Utils;

import com.sustech.ooad.property.StaticProp;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class PathUtils {
    public static String getPicturePosix(String picturePath){
        File jpgFile = new File(picturePath + ".jpg");
        File pngFile = new File(picturePath + ".png");
        if (jpgFile.exists())
            return ".jpg";
        if (pngFile.exists())
            return ".png";
        else return null;
    }

    public static long directoryCount(String directoryPath){
        try (Stream<Path> stream = Files.list(Paths.get(directoryPath))){
            return stream.count();
        } catch (Exception e) {
            return 0;
        }
    }
}
