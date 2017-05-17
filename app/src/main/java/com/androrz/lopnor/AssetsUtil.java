package com.androrz.lopnor;

import android.content.Context;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class AssetsUtil {
    private static String TAG = "AIOS-AssetsUtil";
    private static int BUFFER_SIZE = 4096;

    private static void unzip(InputStream is, File targetDir) throws IOException {

        ZipInputStream zis = new ZipInputStream(new BufferedInputStream(is, BUFFER_SIZE));

//        Log.d(TAG, "BufferSize:" + BUFFER_SIZE);

        ZipEntry ze;
        while ((ze = zis.getNextEntry()) != null) {
            if (ze.isDirectory()) {
                new File(targetDir, ze.getName()).mkdirs();
            } else {

                File file = new File(targetDir, ze.getName());
                File parentdir = file.getParentFile();
                if (parentdir != null && (!parentdir.exists())) {
                    parentdir.mkdirs();
                }

                int pos;
                byte[] buf = new byte[BUFFER_SIZE];
                OutputStream bos = new FileOutputStream(file);
                while ((pos = zis.read(buf, 0, BUFFER_SIZE)) > 0) {
                    bos.write(buf, 0, pos);
                }
                bos.flush();
                bos.close();

                Log.d(TAG, file.getAbsolutePath());
            }
        }

        zis.close();
        is.close();
    }

    private static String readFileAsString(File file) throws IOException {
        String line;
        StringBuffer sb = new StringBuffer();
        BufferedReader br = new BufferedReader(new FileReader(file));
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }

        br.close();
        return sb.toString();
    }

    private static void writeFileAsString(File file, String str) throws IOException {
        FileWriter fw = new FileWriter(file);
        fw.write(str);
        fw.close();
    }

    private static void writeFileAsBytes(File file, byte[] bytes) throws IOException {
        FileOutputStream fstream = new FileOutputStream(file);
        BufferedOutputStream stream = new BufferedOutputStream(fstream);
        stream.write(bytes);
        stream.close();
        fstream.close();
    }

    private static void removeDirectory(File directory) {
        if (directory.isDirectory()) {
            File[] files = directory.listFiles();
            for (int i = 0; i < files.length; i++) {
                if (files[i].isDirectory()) {
                    removeDirectory(files[i]);
                }
                files[i].delete();
            }
            directory.delete();
        }
    }

    public static String loadAssetTextAsString(Context context, String name) {
        BufferedReader in = null;
        try {
            StringBuilder buf = new StringBuilder();
            InputStream is = context.getAssets().open(name);
            in = new BufferedReader(new InputStreamReader(is));

            String str;
            boolean isFirst = true;
            while ((str = in.readLine()) != null) {
                if (isFirst)
                    isFirst = false;
                else
                    buf.append('\n');
                buf.append(str);
            }
            return buf.toString();
        } catch (IOException e) {
            Log.e(TAG, "Error opening asset " + name);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    Log.e(TAG, "Error closing asset " + name);
                }
            }
        }

        return null;
    }

    private static File getFilesDir(Context context) {

        File targetDir = null;

        if (targetDir == null || !targetDir.exists()) {
            targetDir = context.getFilesDir();
        }

        return targetDir;
    }

    private static void mvFile(String pathFrom, String pathTo) {
        File fromFile = new File(pathFrom);
        File toFile = new File(pathTo);
        boolean ret = fromFile.renameTo(toFile);
        Log.d(TAG, "rename file to " + pathTo + "  " + ret);
    }

    /**
     * 获取可用的内部存储空间大小(单位:Byte)
     *
     * @return
     */
    private static long getAvailableInternalMemorySize() {
        long size = Long.MAX_VALUE;
        try {
            File path = Environment.getDataDirectory();
            StatFs stat = new StatFs(path.getPath());

            long blockSize = stat.getBlockSize();
            long avaliableBlocks = stat.getAvailableBlocks();
            size = avaliableBlocks * blockSize;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return size;
    }

    private static String md5sum(InputStream is) {
        int bytes;
        byte buf[] = new byte[BUFFER_SIZE];
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            while ((bytes = is.read(buf, 0, BUFFER_SIZE)) > 0) {
                md.update(buf, 0, bytes);
            }
            is.close();
            return bytes2hex(md.digest());
        } catch (Exception e) {
            /* ignore */
        }
        return null;
    }

    private static String sha1(String message) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            md.update(message.getBytes(), 0, message.length());
            return bytes2hex(md.digest());
        } catch (Exception e) {
            /* ignore */
        }
        return null;
    }

    private static String bytes2hex(byte[] bytes) {
        StringBuffer sb = new StringBuffer(bytes.length * 2);
        for (int i = 0; i < bytes.length; i++) {
            int v = bytes[i] & 0xff;
            if (v < 16) {
                sb.append('0');
            }
            sb.append(Integer.toHexString(v));
        }
        return sb.toString();
    }

}
