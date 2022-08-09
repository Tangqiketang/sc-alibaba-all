package com.wm.common.util;

import lombok.extern.slf4j.Slf4j;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.*;
import java.nio.charset.Charset;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * 描述:
 *
 * @auther WangMin
 * @create 2022-08-08 13:52
 */
@Slf4j
public class ZipUtil {
    /**
     *
     * @param folderPath
     *            要压缩的文件夹路径
     * @param zipPath
     *            压缩后的zip文件路径
     * @param charset
     *            字符编码，解决中文名称乱码
     * @param propertyChangeListener
     *            进度通知
     * @throws Exception
     */
    public static void zip(String folderPath, String zipPath, String charset, PropertyChangeListener propertyChangeListener) throws Exception {
        long totalSize = getTotalSize(new File(folderPath));
        try (ZipOutputStream zipOutput = new ZipOutputStream(new FileOutputStream(zipPath), Charset.forName(charset)); BufferedOutputStream output = new BufferedOutputStream(zipOutput)) {
            File folder = new File(folderPath);
            zip(zipOutput, output, folder, folder.getName(), totalSize, 0, zipPath, propertyChangeListener);
        } catch (Exception e) {
            log.error("error", e);
        }
    }

    private static long zip(ZipOutputStream zipOutput, BufferedOutputStream output, File source, String sourceName, long totalSize, long readSize, String zipPath,
                            PropertyChangeListener propertyChangeListener) throws IOException {
        if (source.isDirectory()) {
            File[] flist = source.listFiles();
            if (flist.length == 0) {
                zipOutput.putNextEntry(new ZipEntry(sourceName + "/"));
            } else {
                for (int i = 0; i < flist.length; i++) {
                    readSize = zip(zipOutput, output, flist[i], sourceName + "/" + flist[i].getName(), totalSize, readSize, zipPath, propertyChangeListener);
                }
            }
            return readSize;
        } else {
            zipOutput.putNextEntry(new ZipEntry(sourceName));
            try (BufferedInputStream input = new BufferedInputStream(new FileInputStream(source))) {
                byte[] b = new byte[1024];
                for (int len = input.read(b); len > 0; len = input.read(b)) {
                    output.write(b, 0, len);
                }
            } catch (Exception e) {
                log.error("error", e);
            }
            Integer oldValue = (int) ((readSize * 1.0 / totalSize) * 100);// 已压缩的字节大小占总字节的大小的百分比
            readSize += source.length();// 累加字节长度
            Integer newValue = (int) ((readSize * 1.0 / totalSize) * 100);// 已压缩的字节大小占总字节的大小的百分比
            if (propertyChangeListener != null) {// 通知调用者压缩进度发生改变
                propertyChangeListener.propertyChange(new PropertyChangeEvent(zipPath, "progress", oldValue, newValue));
            }
            return readSize;
        }
    }

    private static long getTotalSize(File file) {
        if (file.isFile()) {
            return file.length();
        }
        File[] list = file.listFiles();
        long total = 0;
        if (list != null) {
            for (File f : list) {
                total += getTotalSize(f);
            }
        }
        return total;
    }

    /**
     * 解压
     *
     * @param zipPath
     *            要解压的zip文件路径
     * @param targetPath
     *            存放解压后文件的目录
     * @param charset
     *            字符编码，解决中文名称乱码
     * @param propertyChangeListener
     *            进度通知
     * @throws Exception
     */
    public static void unzip(String zipPath, String targetPath, String charset, PropertyChangeListener propertyChangeListener) throws Exception {
        long totalSize = new File(zipPath).length();// 总大小
        long readSize = 0;
        try (ZipInputStream zipInput = new ZipInputStream(new FileInputStream(zipPath), Charset.forName(charset))) {
            for (ZipEntry zipItem = zipInput.getNextEntry(); zipItem != null; zipItem = zipInput.getNextEntry()) {
                if (!zipItem.isDirectory()) {
                    File file = new File(targetPath, zipItem.getName());
                    if (!file.exists()) {
                        new File(file.getParent()).mkdirs();// 创建此文件的上级目录
                    }
                    try (BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(file))) {
                        byte[] b = new byte[1024];
                        for (int len = zipInput.read(b); len > 0; len = zipInput.read(b)) {
                            out.write(b, 0, len);
                        }
                    } catch (Exception e) {
                        log.error("error", e);
                    }
                    Integer oldValue = (int) ((readSize * 1.0 / totalSize) * 100);// 已解压的字节大小占总字节的大小的百分比
                    readSize += zipItem.getCompressedSize();// 累加字节长度
                    Integer newValue = (int) ((readSize * 1.0 / totalSize) * 100);// 已解压的字节大小占总字节的大小的百分比
                    if (propertyChangeListener != null) {// 通知调用者解压进度发生改变
                        propertyChangeListener.propertyChange(new PropertyChangeEvent(zipPath, "progress", oldValue, newValue));
                    }
                }
            }
        } catch (Exception e) {
            log.error("error", e);
        }
    }

    public static void main(String[] args) {
        try {
            zip("C:\\Users\\tangzhichao\\Desktop\\65534英语国标音标TS28-60005", "C:\\Users\\tangzhichao\\Desktop\\test.zip", "GBK", new PropertyChangeListener() {
                @Override
                public void propertyChange(PropertyChangeEvent e) {
                    System.out.println(">>>Source:" + e.getSource());
                    System.out.println(">>>NewValue:" + e.getNewValue());
                }
            });
            String zip_path1 = "C:\\Users\\tangzhichao\\Desktop\\test.zip";
            String unzip_path1 = "C:\\Users\\tangzhichao\\Desktop\\test";
            unzip(zip_path1, unzip_path1, "GBK", new PropertyChangeListener() {
                @Override
                public void propertyChange(PropertyChangeEvent e) {
                    System.out.println("<<<Source:" + e.getSource());
                    System.out.println("<<<NewValue:" + e.getNewValue());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}