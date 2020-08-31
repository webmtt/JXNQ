package com.htht.cn.jiaxing.utils;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.SocketException;
import java.util.Date;

/**
 * @program:
 * @description:
 * @author:
 * @create: 2020-01-10 09:59
 **/
public class FileUtil {


    private static Logger logger = LoggerFactory.getLogger(FileUtil.class);

    /**
     * 读取文件创建时间
     */
    public static Date getCreateTime(String filePath) {
        String strTime = null;
        String fileType = filePath.substring(filePath.lastIndexOf("."));
        try {
            Process p = Runtime.getRuntime().exec("cmd /C dir "
                    + filePath
                    + "/tc");
            InputStream is = p.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = br.readLine()) != null) {
                if (line.endsWith(fileType)) {
                    strTime = line.substring(0, 17);
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return DateUtils.stringToDate(strTime, "yyyy/MM/dd  HH:mm");
        //输出：创建时间   2009-08-17  10:21
    }

    //将文件转换成Byte数组
    public static byte[] getBytesByFile(String pathStr) {
        File file = new File(pathStr);
        try {
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);
            byte[] b = new byte[1000];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            fis.close();
            byte[] data = bos.toByteArray();
            bos.close();
            return data;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //将Byte数组转换成文件
    public static void getFileByBytes(byte[] bytes, String fileParentPath, String fileName) {
        getFileByBytes(bytes, fileParentPath + File.separator + fileName);
    }

    //将Byte数组转换成文件
    public static void getFileByBytes(byte[] bytes, String Path) {
        BufferedOutputStream bos = null;
        FileOutputStream fos = null;
        File file = null;
        try {
            file = new File(Path);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();//创建父级文件路径
                file.createNewFile();//创建文件
            }
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static boolean copy(File file, File toFile) throws Exception {
        boolean flag = false;
        if (!file.exists()) {
            return flag;
        }

        if (!toFile.getParentFile().exists()) {
            toFile.getParentFile().mkdirs();//创建父级文件路径
            toFile.createNewFile();//创建文件
        }
        byte[] b = new byte[1024];
        int a;
        FileInputStream fis;
        FileOutputStream fos;
        //写文件
        fis = new FileInputStream(file);
        fos = new FileOutputStream(toFile);
        while ((a = fis.read(b)) != -1) {
            fos.write(b, 0, a);
        }
        fis.close();
        fos.close();
        flag = true;
        return flag;
    }

    public static boolean copyFile(File file, File toFile, boolean delete) throws Exception {
        boolean flag = copy(file, toFile);
        if (flag && delete) {
            file.delete();
        }
        return flag;
    }

    /**
     * 获取FTPClient对象
     *
     * @param ftpHost     FTP主机服务器
     * @param ftpPassword FTP 登录密码
     * @param ftpUserName FTP登录用户名
     * @param ftpPort     FTP端口 默认为21
     * @return
     */
    public static FTPClient getFTPClient(String ftpHost, String ftpPassword, String ftpUserName, int ftpPort) {
        FTPClient ftpClient = null;
        try {
            ftpClient = new FTPClient();
            ftpClient.connect(ftpHost, ftpPort);// 连接FTP服务器
            ftpClient.login(ftpUserName, ftpPassword);// 登陆FTP服务器
            if (!FTPReply.isPositiveCompletion(ftpClient.getReplyCode())) {
                logger.info("未连接到FTP，用户名或密码错误。");
                ftpClient.disconnect();
            } else {
                logger.info("FTP连接成功。");
            }
        } catch (SocketException e) {
            e.printStackTrace();
            logger.info("FTP的IP地址可能错误，请正确配置。");
        } catch (IOException e) {
            e.printStackTrace();
            logger.info("FTP的端口错误,请正确配置。");
        }
        return ftpClient;
    }

    /**
     * 去 服务器的FTP路径下上读取文件
     *
     * @param ftpUserName
     * @param ftpPassword
     * @param ftpPath
     * @param
     * @return
     */
    public static String readFTPFile(String ftpUserName, String ftpPassword, String ftpPath, String ftpHost, int ftpPort, String fileName) {
        StringBuffer resultBuffer = new StringBuffer();
        FileInputStream inFile = null;
        InputStream in = null;
        FTPClient ftpClient = null;
        logger.info("开始读取绝对路径" + ftpPath + "文件!");
        try {
            ftpClient = getFTPClient(ftpHost, ftpPassword, ftpUserName,
                    ftpPort);
            ftpClient.setControlEncoding("UTF-8"); // 中文支持
            //ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
            ftpClient.enterLocalPassiveMode();
            ftpClient.changeWorkingDirectory(ftpPath);
            in = ftpClient.retrieveFileStream(fileName);
        } catch (FileNotFoundException e) {
            logger.error("没有找到" + ftpPath + "文件");
            e.printStackTrace();
            return "下载配置文件失败，请联系管理员.";
        } catch (SocketException e) {
            logger.error("连接FTP失败.");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("文件读取错误。");
            e.printStackTrace();
            return "配置文件读取失败，请联系管理员.";
        }
        if (in != null) {
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String data = null;
            try {
                while ((data = br.readLine()) != null) {
                    resultBuffer.append(data.trim() + ";");
                }
            } catch (IOException e) {
                logger.error("文件读取错误。");
                e.printStackTrace();
                return "配置文件读取失败，请联系管理员.";
            } finally {
                try {
                    ftpClient.disconnect();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            logger.error("in为空，不能读取。");
            return "配置文件读取失败，请联系管理员.";
        }
        return resultBuffer.toString().trim();
    }

    /*
    * 换行
    */
    /*public static String TxtToString(File file) {
        StringBuilder result = new StringBuilder();
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));//构造一个BufferedReader类来读取文件
            String s = null;
            while ((s = br.readLine()) != null) {//使用readLine方法，一次读一行
                result.append(System.lineSeparator() + s);
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result.toString();
    }*/

    /*
    * 不换行
    */
    public static String TxtToString(File file) {
        StringBuilder result = new StringBuilder();
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));//构造一个BufferedReader类来读取文件
            String s = null;
            while ((s = br.readLine()) != null) {//使用readLine方法，一次读一行
                result.append(s);
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result.toString();
    }

}