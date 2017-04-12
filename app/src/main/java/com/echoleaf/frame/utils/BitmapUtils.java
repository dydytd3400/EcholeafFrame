/**
 *
 */
package com.echoleaf.frame.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader.TileMode;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author John zhang
 * @version 0.1
 */
public class BitmapUtils {

    private static final String TAG = "BitmapUtils";

    /**
     * 旋转图片
     *
     * @param b       图片流
     * @param degrees 旋转角度
     * @return
     */
    public static Bitmap rotate(Bitmap b, int degrees) {
        if (degrees != 0 && b != null) {
            Matrix m = new Matrix();
            m.setRotate(degrees, (float) b.getWidth() / 2, (float) b.getHeight() / 2);
            try {
                Bitmap b2 = Bitmap.createBitmap(b, 0, 0, b.getWidth(), b.getHeight(), m, true);
                if (b != b2) {
                    b.recycle();
                    b = null;
                    return b2;
                }
            } catch (OutOfMemoryError ex) {
            }
        }
        return null;
    }

    /**
     * 计算压缩比例
     *
     * @param options
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        if (reqWidth <= 0 || reqHeight <= 0) {
            reqWidth = 480;
            reqHeight = 800;
        }
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            if (width > height) {
                inSampleSize = Math.round((float) height / (float) reqHeight);
            } else {
                inSampleSize = Math.round((float) width / (float) reqWidth);
            }
        }
        return inSampleSize;
    }

    /**
     * 图片OOM优化，计算压缩率
     *
     * @param is
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public static BitmapFactory.Options buildOptions(InputStream is, int reqWidth, int reqHeight) {
        // 首先设置 inJustDecodeBounds=true 来检查尺寸
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(is, null, options);
        // 计算压缩比例
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        // 设置inJustDecodeBounds为false
        options.inJustDecodeBounds = false;
        options.outWidth = -1;
        options.outHeight = -1;
        options.inPreferredConfig = Config.RGB_565;
        options.inPurgeable = true;
        options.inInputShareable = true;
        return options;
    }

    /**
     * @param in
     * @return
     */
    public static Bitmap createBitmap(InputStream in) {
        if (in == null)
            return null;
        Bitmap bm = null;
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = false;
            options.inPreferredConfig = Config.RGB_565;

            bm = BitmapFactory.decodeStream(in, null, options);
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
        return bm;
    }

    /**
     * @param in
     * @param width
     * @param height
     * @return
     */
    public static Bitmap createBitmap(InputStream in, int width, int height) {
        if (in == null)
            return null;
        Bitmap bm = null;
        try {
            BitmapFactory.Options options = buildOptions(in, width, height);

            bm = BitmapFactory.decodeStream(in, null, options);
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
        return bm;
    }

    /**
     * @param content
     * @return
     */
    public static Bitmap createBitmap(byte[] content) {
        if (CollectionUtils.isEmpty(content))
            return null;
        Bitmap bm = null;
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = false;
            options.inPreferredConfig = Config.RGB_565;
            bm = BitmapFactory.decodeByteArray(content, 0, content.length, options);
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
        return bm;
    }

    /**
     * @param content
     * @param width
     * @param height
     * @return
     */
    public static Bitmap createBitmap(byte[] content, int width, int height) {
        if (CollectionUtils.isEmpty(content))
            return null;
        Bitmap bm = null;
        try {
            BitmapFactory.Options options = buildOptions(new ByteArrayInputStream(content), width, height);
            options.inJustDecodeBounds = false;
            options.inPreferredConfig = Config.RGB_565;
            bm = BitmapFactory.decodeByteArray(content, 0, content.length, options);
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
        return bm;
    }

    /**
     * 写图片文件 在Android系统中，文件保存在 /data/data/PACKAGE_NAME/files 目录下
     *
     * @return
     * @throws IOException
     */
    public static File saveImage(Context context, String fileName, Bitmap bitmap) throws IOException {
        return saveImage(context, fileName, bitmap, 100);
    }

    public static File saveImage(Context context, String fileName, Bitmap bitmap, int quality) throws IOException {
        return saveImage(context, fileName, bitmap, quality, Context.MODE_PRIVATE);
    }

    public static File saveImage(Context context, String fileName, Bitmap bitmap, int quality, int mode) throws IOException {
        if (bitmap == null)
            return null;

        FileOutputStream fos = context.openFileOutput(fileName, mode);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(CompressFormat.JPEG, quality, stream);
        byte[] bytes = stream.toByteArray();
        fos.write(bytes);
        fos.close();
        return context.getFileStreamPath(fileName);
    }

    /**
     * 获取bitmap
     *
     * @param context
     * @param fileName
     * @return
     */
    public static Bitmap getBitmap(Context context, String fileName) {
        return getBitmap(context, fileName, 0, 0);
    }

    /**
     * 获取bitmap
     *
     * @param filePath
     * @return
     */
    public static Bitmap getBitmap(String filePath, int reqWidth, int reqHeight) {
        BitmapFactory.Options optionsIn = new BitmapFactory.Options();
        optionsIn.inJustDecodeBounds = true;
        // 计算压缩比例
        optionsIn.inSampleSize = calculateInSampleSize(optionsIn, reqWidth, reqHeight);
        // 设置inJustDecodeBounds为false
        optionsIn.inJustDecodeBounds = false;
        optionsIn.inPreferredConfig = Config.RGB_565;
        optionsIn.inPurgeable = true;
        optionsIn.inInputShareable = true;

        return BitmapFactory.decodeFile(filePath, optionsIn);
    }

    /**
     * 获取bitmap
     *
     * @param context
     * @param fileName
     * @return
     */
    public static Bitmap getBitmap(Context context, String fileName, int width, int height) {
        FileInputStream fis = null;
        Bitmap bitmap = null;
        BitmapFactory.Options options = null;
        try {
            fis = context.openFileInput(fileName);
            if (width > 0 && height > 0) {
                options = buildOptions(fis, width, height);
            }
            bitmap = decodeStream(fis, options);

        } catch (FileNotFoundException e) {
            // e.printStackTrace ();
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        } finally {
            try {
                fis.close();
            } catch (Exception e) {
            }
        }
        return bitmap;
    }

    private static Bitmap decodeStream(InputStream in, BitmapFactory.Options opts) {
        if (opts == null) {
            opts = new BitmapFactory.Options();
            opts.inJustDecodeBounds = true;
            // BitmapFactory.decodeStream ( in, null, opts );
            // 计算压缩比例
            opts.inSampleSize = 1;
            // 设置inJustDecodeBounds为false
            opts.inJustDecodeBounds = false;
            opts.outWidth = -1;
            opts.outHeight = -1;
            opts.inPreferredConfig = Config.RGB_565;
            opts.inPurgeable = true;
            opts.inInputShareable = true;
        }
        // opts.inJustDecodeBounds = false;
        opts.inJustDecodeBounds = false;
        opts.inPreferredConfig = Config.RGB_565;
        return BitmapFactory.decodeStream(in, null, opts);
    }

    /**
     * 创建倒影图片
     *
     * @param srcBitmap        源图片的bitmap
     * @param reflectionHeight 图片倒影的高度
     * @return Bitmap
     * @throw
     */
    public static Bitmap createReflectedBitmap(Bitmap srcBitmap, int reflectionHeight) {

        if (null == srcBitmap) {
            Log.e(TAG, "the srcBitmap is null");
            return null;
        }

        // The gap between the reflection bitmap and original bitmap.
        final int REFLECTION_GAP = 0;

        int srcWidth = srcBitmap.getWidth();
        int srcHeight = srcBitmap.getHeight();

        if (0 == srcWidth || srcHeight == 0) {
            Log.e(TAG, "the srcBitmap is null");
            return null;
        }

        // The matrix
        Matrix matrix = new Matrix();
        matrix.preScale(1, -1);

        try {

            // The reflection bitmap, width is same with original's, height is
            // half of original's.
            Bitmap reflectionBitmap = Bitmap.createBitmap(srcBitmap, 0, srcHeight - reflectionHeight, srcWidth, reflectionHeight, matrix, false);

            if (null == reflectionBitmap) {
                Log.e(TAG, "Create the reflectionBitmap is failed");
                return null;
            }

            // Create the bitmap which contains original and reflection bitmap.
            Bitmap bitmapWithReflection = Bitmap.createBitmap(srcWidth, srcHeight + reflectionHeight, Config.ARGB_8888);

            if (null == bitmapWithReflection) {
                return null;
            }

            // Prepare the canvas to draw stuff.
            Canvas canvas = new Canvas(bitmapWithReflection);

            // Draw the original bitmap.
            canvas.drawBitmap(srcBitmap, 0, 0, null);

            // Draw the reflection bitmap.
            canvas.drawBitmap(reflectionBitmap, 0, srcHeight + REFLECTION_GAP, null);

            Paint paint = new Paint();
            paint.setAntiAlias(true);
            LinearGradient shader = new LinearGradient(0, srcHeight, 0, bitmapWithReflection.getHeight() + REFLECTION_GAP, 0x70FFFFFF, 0x00FFFFFF, TileMode.MIRROR);
            paint.setShader(shader);
            paint.setXfermode(new PorterDuffXfermode(android.graphics.PorterDuff.Mode.DST_IN));

            canvas.save();
            // Draw the linear shader.
            canvas.drawRect(0, srcHeight, srcWidth, bitmapWithReflection.getHeight() + REFLECTION_GAP, paint);
            if (reflectionBitmap != null && !reflectionBitmap.isRecycled()) {
                reflectionBitmap.recycle();
                reflectionBitmap = null;
            }

            canvas.restore();

            return bitmapWithReflection;
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.e(TAG, "Create the reflectionBitmap is failed");
        return null;
    }

    /**
     * 图片圆角处理
     *
     * @param srcBitmap 源图片的bitmap
     * @param ret       圆角的度数
     * @return Bitmap
     * @throw
     */
    public static Bitmap getRoundImage(Bitmap srcBitmap, float ret) {

        if (null == srcBitmap) {
            Log.e(TAG, "the srcBitmap is null");
            return null;
        }

        int bitWidth = srcBitmap.getWidth();
        int bitHight = srcBitmap.getHeight();

        BitmapShader bitmapShader = new BitmapShader(srcBitmap, TileMode.CLAMP, TileMode.CLAMP);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setShader(bitmapShader);

        RectF rectf = new RectF(0, 0, bitWidth, bitHight);

        Bitmap outBitmap = Bitmap.createBitmap(bitWidth, bitHight, Config.ARGB_8888);
        Canvas canvas = new Canvas(outBitmap);
        canvas.drawRoundRect(rectf, ret, ret, paint);
        canvas.save();
        canvas.restore();

        return outBitmap;
    }

    /**
     * 图片沿着Y轴旋转一定角度
     *
     * @param srcBitmap        源图片的bitmap
     * @param reflectionHeight 图片倒影的高度
     * @param rotate           图片旋转的角度
     * @return Bitmap
     * @throw
     */
    public static Bitmap skewImage(Bitmap srcBitmap, float rotate, int reflectionHeight) {

        if (null == srcBitmap) {
            Log.e(TAG, "the srcBitmap is null");
            return null;
        }

        Bitmap reflecteBitmap = createReflectedBitmap(srcBitmap, reflectionHeight);

        if (null == reflecteBitmap) {
            Log.e(TAG, "failed to createReflectedBitmap");
            return null;
        }

        int wBitmap = reflecteBitmap.getWidth();
        int hBitmap = reflecteBitmap.getHeight();
        float scaleWidth = ((float) 180) / wBitmap;
        float scaleHeight = ((float) 270) / hBitmap;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        reflecteBitmap = Bitmap.createBitmap(reflecteBitmap, 0, 0, wBitmap, hBitmap, matrix, true);
        Camera localCamera = new Camera();
        localCamera.save();
        Matrix localMatrix = new Matrix();
        localCamera.rotateY(rotate);
        localCamera.getMatrix(localMatrix);
        localCamera.restore();
        localMatrix.preTranslate(-reflecteBitmap.getWidth() >> 1, -reflecteBitmap.getHeight() >> 1);
        Bitmap localBitmap2 = Bitmap.createBitmap(reflecteBitmap, 0, 0, reflecteBitmap.getWidth(), reflecteBitmap.getHeight(), localMatrix, true);
        Bitmap localBitmap3 = Bitmap.createBitmap(localBitmap2.getWidth(), localBitmap2.getHeight(), Config.ARGB_8888);
        Canvas localCanvas = new Canvas(localBitmap3);
        Paint localPaint = new Paint();
        localPaint.setAntiAlias(true);
        localPaint.setFilterBitmap(true);
        localCanvas.drawBitmap(localBitmap2, 0.0F, 0.0F, localPaint);
        if (null != reflecteBitmap && !reflecteBitmap.isRecycled()) {
            reflecteBitmap.recycle();
            reflecteBitmap = null;
        }
        if (null != localBitmap2 && !localBitmap2.isRecycled()) {
            localBitmap2.recycle();
            localBitmap2 = null;
        }
        localCanvas.save();
        localCanvas.restore();
        return localBitmap3;
    }

    /**
     * 给图片添加指定颜色的边框
     *
     * @param srcBitmap   原图片
     * @param borderWidth 边框宽度
     * @param color       边框的颜色值
     * @return
     */
    public static Bitmap addFrameBitmap(Bitmap srcBitmap, int borderWidth, int color) {
        if (srcBitmap == null) {
            Log.e(TAG, "the srcBitmap or borderBitmap is null");
            return null;
        }

        int newWidth = srcBitmap.getWidth() + borderWidth;
        int newHeight = srcBitmap.getHeight() + borderWidth;

        Bitmap outBitmap = Bitmap.createBitmap(newWidth, newHeight, Config.ARGB_8888);

        Canvas canvas = new Canvas(outBitmap);

        Rect rec = canvas.getClipBounds();
        rec.bottom--;
        rec.right--;
        Paint paint = new Paint();
        // 设置边框颜色
        paint.setColor(color);
        paint.setStyle(Paint.Style.STROKE);
        // 设置边框宽度
        paint.setStrokeWidth(borderWidth);
        canvas.drawRect(rec, paint);

        canvas.drawBitmap(srcBitmap, borderWidth / 2, borderWidth / 2, null);
        canvas.save(Canvas.ALL_SAVE_FLAG);
        canvas.restore();
        if (srcBitmap != null && !srcBitmap.isRecycled()) {
            srcBitmap.recycle();
            srcBitmap = null;
        }

        return outBitmap;
    }

}
