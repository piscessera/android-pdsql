package com.piscessera.pdsql.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import android.app.Application;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class FileUtil extends Application {
	public static final String CONTEXT_NAME = "homeaccount.dat";
	private Context mContext;

	public FileUtil(Context mContext) {
		super();
		this.mContext = mContext;
	}

	public static String convertIs2String(InputStream var) {
		String result = "";
		try {

			BufferedReader br = new BufferedReader(new InputStreamReader(var,
					"utf-8"), 8);

			StringBuffer sb = new StringBuffer();

			String line = null;

			while ((line = br.readLine()) != null) {
				sb.append(line);
			}

			result = sb.toString();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public boolean saveContextFile(String fileName, String text) {
		boolean result = false;
		try {
			FileOutputStream fos = mContext.openFileOutput(fileName,
					MODE_PRIVATE);
			fos.write(text.getBytes());
			fos.close();
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		}
		return result;
	}

	public InputStream getContextFile(String fileName) {
		InputStream result = null;
		try {
			result = mContext.openFileInput(fileName);
		} catch (Exception e) {
			e.printStackTrace();
			result = null;
		}
		return result;
	}

	public String getContextContent(String fileName) {
		String result = "";
		result = convertIs2String(getContextFile(fileName));
		return result;
	}

	private void copyFile(InputStream in, OutputStream out) throws IOException {
		byte[] buffer = new byte[1024];
		int read;
		while ((read = in.read(buffer)) != -1) {
			out.write(buffer, 0, read);
		}
	}

	public void copyAssets(AssetManager assets) {
		AssetManager assetManager = assets;
		String[] files = null;
		try {
			files = assetManager.list("");
		} catch (IOException e) {
			DebugUtil.e(e.getMessage());
		}
		for (int i = 0; i < files.length; i++) {
			InputStream in = null;
			OutputStream out = null;
			try {
				in = assetManager.open(files[i]);
				out = new FileOutputStream("/sdcard/" + files[i]);
				copyFile(in, out);
				in.close();
				in = null;
				out.flush();
				out.close();
				out = null;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void copyAssets(AssetManager assets, String folder) {
		AssetManager assetManager = assets;
		String[] files = null;
		try {
			files = assetManager.list("");
		} catch (IOException e) {
			DebugUtil.e(e.getMessage());
		}

		File directory = new File("/sdcard/" + folder + "/");
		if (!directory.exists()) {
			// Create directory
			directory.mkdirs();
		}

		for (int i = 0; i < files.length; i++) {
			InputStream in = null;
			OutputStream out = null;
			try {
				in = assetManager.open(files[i]);
				out = new FileOutputStream("/sdcard/" + folder + "/" + files[i]);
				copyFile(in, out);
				in.close();
				in = null;
				out.flush();
				out.close();
				out = null;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void copyAssets(String fileName, String target) {
		InputStream in = null;
		OutputStream out = null;
		try {
			in = mContext.getAssets().open(fileName);
			out = new FileOutputStream(target + fileName);
			copyFile(in, out);
			in.close();
			in = null;
			out.flush();
			out.close();
			out = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void copyFileManual(String targetFile, String path, String fileName) {
		try {
			// Open local db as the input stream
			InputStream in = new FileInputStream(targetFile);

			// Path
			String outFileName = path + fileName;

			// Open the db as the output stream
			OutputStream out = new FileOutputStream(outFileName);

			// Transfer bytes from the inputfile to the outputfile
			copyFile(in, out);

			// Close the streams
			in.close();
			in = null;
			out.flush();
			out.close();
			out = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void copyFileManual(AssetManager assets, String path, String fileName) {
		try {
			// Open local db as the input stream
			InputStream in = assets.open(fileName);

			// Path
			String outFileName = path + fileName;

			// Open the db as the output stream
			OutputStream out = new FileOutputStream(outFileName);

			// Transfer bytes from the inputfile to the outputfile
			copyFile(in, out);

			// Close the streams
			in.close();
			in = null;
			out.flush();
			out.close();
			out = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void deleteFileIfExist(String absolutePath) {
		File file = new File(absolutePath);
		if (file.exists()) {
			file.delete();
		}
	}

	public Bitmap getBitmapFromAsset(String fileName, boolean setOption) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		if (setOption) {
			options.inSampleSize = 8;
		}
		Bitmap bitmap = null;
		InputStream in;
		try {
			in = mContext.getAssets().open(fileName);
			bitmap = BitmapFactory.decodeStream(in, null, options);
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bitmap;
	}

	public Bitmap getBitmapFromAsset(String fileName, int sampleSize) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inSampleSize = sampleSize;
		Bitmap bitmap = null;
		InputStream in;
		try {
			in = mContext.getAssets().open(fileName);
			bitmap = BitmapFactory.decodeStream(in, null, options);
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bitmap;
	}
}
