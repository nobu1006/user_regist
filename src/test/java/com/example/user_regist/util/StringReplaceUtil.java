package com.example.user_regist.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.stream.Collectors;

public class StringReplaceUtil {

    public static void replace(String beforeStr, String afterStr) {
        File file = new File(StringReplaceUtil.class.getResource("/application.yml").getPath());
        String root = file.getAbsolutePath().replace("application.yml", "");
        FileSystem fs = FileSystems.getDefault();
        Path dir = fs.getPath(root);
        try (DirectoryStream<Path> ds = Files.newDirectoryStream(dir)) {
            for (Path path : ds) {
                if (Files.isDirectory(path)) {
                    replace(path, beforeStr, afterStr);
                } else {
                    System.out.println(path);
                    List<String> lines = Files.lines(path).map(s -> s.replace(beforeStr, afterStr))
                            .collect(Collectors.toList());
                    Files.write(path, lines);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void replace(Path dir, String beforeStr, String afterStr) {
        try (DirectoryStream<Path> ds = Files.newDirectoryStream(dir)) {
            for (Path path : ds) {
                if (Files.isDirectory(path)) {
                    replace(path, beforeStr, afterStr);
                } else {
                    System.out.println(path);
                    List<String> lines = Files.lines(path).map(s -> s.replace(beforeStr, afterStr))
                            .collect(Collectors.toList());
                    Files.write(path, lines);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
