package org.horaapps.leafpic.data.filter; // (rank 398) copied from https://github.com/HoraApps/LeafPic/blob/dc3ddc14705e4c8b6f6e7d3c778756661b73c7ae/app/src/main/java/org/horaapps/leafpic/data/filter/ImageFileFilter.java

import java.io.File;
import java.io.FilenameFilter;
import java.util.regex.Pattern;

/**
 * Created by dnld on 24/04/16.
 */
public class ImageFileFilter implements FilenameFilter {

    private Pattern pattern;

    public ImageFileFilter(boolean includeVideo) {
        pattern = includeVideo
                ? Pattern.compile(".(jpg|png|gif|jpe|jpeg|bmp|webp|mp4|mkv|webm|avi)$", Pattern.CASE_INSENSITIVE)
                : Pattern.compile(".(jpg|png|gif|jpe|jpeg|bmp|webp)$", Pattern.CASE_INSENSITIVE);
    }

    @Override
    public boolean accept(File dir, String filename) {
        return new File(dir, filename).isFile() && pattern.matcher(filename).find();
    }
}