package com.cn.common.util;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.webkit.MimeTypeMap;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ABFileUtil {

    public static final String TAG = ABFileUtil.class.getSimpleName();

    public static final String SD_CARD_PATH = Environment.getExternalStorageDirectory().toString();

    public static String getSDPATH() {
        return SD_CARD_PATH;
    }

    public static File obtainDirF(String path) {
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }

    public static String obtainDirS(String path) {
        return obtainDirF(path).getAbsolutePath();
    }


    /**
     * 在SD卡上创建文件
     *
     * @throws java.io.IOException
     */
    public static File creatSDFile(String fileRelativePath) throws IOException {
        File file = new File(SD_CARD_PATH + fileRelativePath);
        file.createNewFile();
        return file;
    }

    /**
     * 在SD卡上创建目录
     *
     * @param dirRelativePath
     */
    public static File creatSDDir(String dirRelativePath) {
        File dir = new File(SD_CARD_PATH + dirRelativePath);
        dir.mkdirs();
        return dir;
    }

    /**
     * 判断SD卡上的文件夹是否存在
     */
    public static boolean isFileExist(String fileRelativePath) {
        File file = new File(SD_CARD_PATH + fileRelativePath);
        return file.exists();
    }

    /**
     * 将一个InputStream里面的数据写入到SD卡中
     */
    public static File write2SDFromInput(String relativePath, String fileName, InputStream input) {
        if (!relativePath.endsWith("/")) {
            relativePath = relativePath + "/";
        }
        File file = null;
        OutputStream output = null;
        try {
            creatSDDir(relativePath);
            file = creatSDFile(relativePath + fileName);
            output = new FileOutputStream(file);
            byte buffer[] = new byte[4 * 1024];
            int length = 0;
            while ((length = input.read(buffer)) != -1) {
                output.write(buffer, 0, length);
            }
            output.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ABIOUtil.closeIO(output);
        }
        return file;
    }

  
    
   

    /**
     * 压缩图片直到容量小于指定值(kb)，并保存到sdcard
     *
     * @param relativePath
     * @param fileName
     * @param bm
     * @param capacity
     * @return
     */
    public static int saveBitmap2SDWithCapacity(String relativePath, String fileName, Bitmap bm, int capacity) {
        if (!relativePath.endsWith("/")) {
            relativePath = relativePath + "/";
        }
        File file = null;
        FileOutputStream out = null;
        ByteArrayInputStream bais = null;
        try {
            creatSDDir(relativePath);
            file = creatSDFile(relativePath + fileName);
            out = new FileOutputStream(file.getPath());
            bais = compressImage(bm, capacity);
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = bais.read(buffer)) != -1) {
                out.write(buffer, 0, len);
            }
            out.flush();
            return 0;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        } finally {
            ABIOUtil.closeIO(out, bais);
        }

    }

    /**
     * 压缩图片直到容量小于指定值(kb)
     *
     * @param image
     * @param capacity
     * @return
     */
    public static ByteArrayInputStream compressImage(Bitmap image, int capacity) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > capacity) {  //循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();//重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
            if (options < 10) {
                break;
            }
            options -= 10;//每次都减少10
        }
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
        baos.reset();
        return bais;
    }



    public static void openFile(Context context, String path) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        String type = getMimeType(path);
        //参数二type见下面
        intent.setDataAndType(Uri.fromFile(new File(path)), type);
        context.startActivity(intent);
    }

    public static String getMimeType(String uri) {
        String type = null;
        String extension = MimeTypeMap.getFileExtensionFromUrl(uri);
        if (extension != null) {
            MimeTypeMap mime = MimeTypeMap.getSingleton();
            type = mime.getMimeTypeFromExtension(extension);
        }
        return type;
    }

    public static void deleteFile(File... files) {
    	 for (File file : files) {
             try {
                 file.delete();
             } catch (RuntimeException e) {
                 LogUtils.d(e.toString());
             }
         }
    }

    /**
     * 删除文件夹
     *
     * @param file
     */
    public static void deleteFolder(File file) {
        if (file.exists() && file.isDirectory()) {//判断是文件还是目录
            if (file.listFiles().length > 0) {//若目录下没有文件则直接删除
                //若有则把文件放进数组，并判断是否有下级目录
                File delFile[] = file.listFiles();
                int i = file.listFiles().length;
                for (int j = 0; j < i; j++) {
                    if (delFile[j].isDirectory()) {
                        deleteFolder(delFile[j]);//递归调用del方法并取得子目录路径
                    }
                    delFile[j].delete();//删除文件  
                }
            }
            file.delete();
        }
    }

    /**
     * 删除文件夹
     *
     * @param filePath
     */
    public static void deleteFolder(String filePath) {
        File file = new File(filePath);
        deleteFolder(file);
    }


    /**
     * 从文件中读取Byte数组
     * Add by Hubert
     *
     * @param file
     * @return byte[]
     * @throws IOException
     */
    public static byte[] getBytesFromFile(File file) throws IOException {
        InputStream is = new FileInputStream(file);
        // 获取文件大小
        long length = file.length();

        if (length > Integer.MAX_VALUE) {
            // 文件太大，无法读取
            throw new IOException("File is to large " + file.getName());
        }

        // 创建一个数据来保存文件数据
        byte[] bytes = new byte[(int) length];

        // 读取数据到byte数组中
        int offset = 0;

        int numRead = 0;

        while (offset < bytes.length && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
            offset += numRead;
        }

        // 确保所有数据均被读取
        if (offset < bytes.length) {
            throw new IOException("Could not completely read file " + file.getName());
        }

        // Close the input stream and return bytes
        is.close();

        return bytes;
    }

    public static byte[] getBytesFromInputStream(InputStream in) throws Exception {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] data = new byte[4096];
        int count;
        while ((count = in.read(data, 0, 4096)) != -1)
            outStream.write(data, 0, count);
        return outStream.toByteArray();
    }

    /**
     * 获取拍照的图片路径
     *
     * @return
     */
    @TargetApi(Build.VERSION_CODES.FROYO)
    public static File getDCIMFile() {
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
    }

    /**
     * 扫描目录（扫描后可以及时在图库中看到）
     *
     * @param context
     * @param path
     */
    @TargetApi(Build.VERSION_CODES.FROYO)
    public static void scanFile(Context context, String path) {
        MediaScannerConnection.scanFile(context, new String[]{path}, null, null);
    }

    /**
     * 获取一个文件对象，如果不存在，则自动创建
     *
     * @param filePath
     * @return
     */
    public static File getFileAutoCreated(String filePath) {
        File file = new File(filePath);
        if (file.isDirectory()) {
            return file;
        }
        if (file.exists()) {
            return file;
        }
        File parentFile = file.getParentFile();
        if (!parentFile.exists()) {
            parentFile.mkdirs();
        }
        try {
            file.createNewFile();
        } catch (IOException e) {
        	   LogUtils.d(e.toString());
        }
        return file;
    }

    /**
     * 获取一个目录对象，如果不存在，则自动创建
     *
     * @param dirPath
     * @return
     */
    public static File getDirAutoCreated(String dirPath) {
        File dirFile = new File(dirPath);
        if (dirFile.isFile()) {
            return dirFile;
        }
        if (!dirFile.exists()) {
            dirFile.mkdirs();
        }
        return dirFile;
    }
}