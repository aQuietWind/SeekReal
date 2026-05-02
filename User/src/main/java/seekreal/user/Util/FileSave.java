package seekreal.user.Util;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class FileSave {
    //头像的保存路径
    private static final String UserImage="/home/admin/Documents/User/";
    //文件类型
    private static final List<String> ImageAllowList = Arrays.asList(".png", ".jpg", ".jpeg");
    // 限制文件大小 1MB
    private static final long MaxFileSize = 1024 * 1024;
    public static String saveUserImage(MultipartFile file) {
        if (file==null||file.isEmpty()) {throw new RuntimeException("文件不能为空");}
        if (file.getSize() > MaxFileSize) {
            throw new RuntimeException("文件大小不能超过1MB");
        }
        //检查文件名
        String oldFileName = file.getOriginalFilename();
        if (oldFileName == null || oldFileName.isBlank()) {
            throw new RuntimeException("文件名不能为空");
        }
        //获取后缀名
        int dotIndex = oldFileName.lastIndexOf(".");
        if (dotIndex == -1) {
            throw new RuntimeException("文件没有后缀，不支持上传");
        }
        // 统一转小写，避免大小写问题,并且获取类型
        String typeName = oldFileName.substring(dotIndex).toLowerCase();
        //检测头像的后缀名
        if (!ImageAllowList.contains(typeName)) {
            throw new RuntimeException("头像类型不正确，仅支持png/jpg/jpeg");
        }
        String newFileName= UUID.randomUUID()+typeName;                 //生成一个超级随机的文件名字
        //获取目录
        Path destDir = Paths.get(UserImage);
        //如果目录不存在
        if (!Files.exists(destDir)) {
            //尝试创建目录
            try {
                Files.createDirectories(destDir);
            } catch (IOException e) {
                throw new RuntimeException("文件保存目录创建失败,具体问题： "+ e.getMessage());
            }
        }
        //将目录与新的文件名尝试拼接
        Path destPath = destDir.resolve(newFileName);
        //保存文件
        try {
            Files.copy(file.getInputStream(), destPath);
        } catch (IOException e) {
            throw new RuntimeException("文件保存失败,具体问题： "+e.getMessage());
        }
        return newFileName;
    }
}
