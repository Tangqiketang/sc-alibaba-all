package com.wm.common.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Date;
import java.util.Objects;

/**
 * 描述:
 *
 * @auther WangMin
 * @create 2022-06-07 17:26
 */
@Slf4j
public class FileKit {


    /**
     *
     * @param file  整个文件  a.jpg
     * @param sysLocation  根路径 如如/app/app_upload/ 或C:/app/app_upload/
     * @param moduleName 可以为null。 如 nickpicture
     * @return  /app/app_upload/nickpicture/a-1524324322.jpg
     */
    public static String saveFileReturnPath(MultipartFile file, String sysLocation,String moduleName) {
        long time = (new Date()).getTime();
        String realName;
        if ((Objects.requireNonNull(file.getOriginalFilename())).contains(".")) {
            //获取文件名字和后缀
            String caselsh = file.getOriginalFilename().substring(0, file.getOriginalFilename().lastIndexOf(46));
            String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(46) + 1);
            realName = caselsh + '-' + time + '.' + suffix;
        } else {
            realName = file.getOriginalFilename() + '-' + time;
        }
        if(StringUtils.isNotBlank(moduleName)){
            sysLocation = sysLocation+"/"+moduleName+"/";
        }

        Boolean success = saveFile(file, sysLocation, realName);
        if(success){
            return sysLocation+realName;
        }
        return "";
    }



    /**
     * 底层文件保存
     * @param multipartFile 文件
     * @param path  路径 如/app/app_upload/ 或C:/app/app_upload/
     * @param fileName 文件名称 如a.jpg
     * @return
     */
    public static boolean saveFile(MultipartFile multipartFile, String path, String fileName) {
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        boolean returnBool = false;
        try {
            FileInputStream fileInputStream = (FileInputStream)multipartFile.getInputStream();
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(path + File.separator + fileName));
            byte[] bs = new byte[1024];
            int len;
            while((len = fileInputStream.read(bs)) != -1) {
                bos.write(bs, 0, len);
            }
            bos.flush();
            bos.close();
            returnBool = true;
        } catch (IOException e) {
            log.error("保存文件失败,fileName:{}",fileName);
        }
        return returnBool;
    }

    /**
     * 从项目resource目录下获取文件流
     * @param response
     * @param fileName  static/systemLogo/a.jpg
     * @param contentType image/gif ：gif图片格式;
     *                    image/jpeg ：jpg图片格式;
     *                    image/png：png图片格式;
     *                    text/xml ：  XML格式;
     *                    text/plain ：纯文本格式;
     *                    text/html ： HTML格式;
     * @return 返回 /resource/static/systemLogo/a.jpg 流
     */
    public void getFileStreamFromProjectResource(HttpServletResponse response,String fileName,String contentType){
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        try {
            Resource[] resources = resolver.getResources(fileName);
            Resource resource = resources[0];
            InputStream fis = resource.getInputStream();

            byte[] buffer = new byte[((InputStream)fis).available()];
            fis.read(buffer);
            fis.close();
            response.reset();

            OutputStream outputStream = new BufferedOutputStream(response.getOutputStream());
            response.setContentType(contentType);
            outputStream.write(buffer);
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            log.error("无法从项目resource文件夹下获取到文件");
            throw new RuntimeException(e);
        }


    }



}
