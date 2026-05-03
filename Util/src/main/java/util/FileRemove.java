package util;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;

public class FileRemove {
    public static void removeFile(String adder) {
        Path path = Paths.get(adder);
        //检查路径是否存在
        if (!Files.exists(path)){
            throw new RuntimeException("该路径文件不存在！！！无法删除");
        }
        //删除文件
        if(!path.toFile().delete()){
            throw new RuntimeException("文件删除失败！！！");
        }
        return;
    }
}
