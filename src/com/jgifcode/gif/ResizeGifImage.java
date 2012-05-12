package com.jgifcode.gif;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Logger;

import com.jgifcode.gif.Scalr.Method;

public class ResizeGifImage {

	static Logger logger = Logger.getLogger(ResizeGifImage.class.getName());

	public static void resize(final String srcFileName,
			final String dstFileName, final int newWidth, final int newHeight)
			throws IOException {

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

		AnimatedGifEncoder e = new AnimatedGifEncoder();

		GifDecoder gifDecoder = new GifDecoder();
		gifDecoder.read(new FileInputStream(srcFileName));

		BufferedImage[] images = null;
		try {
			images = GifImageUtil.readGif(srcFileName);
		} catch (IllegalArgumentException e1) {
			throw e1;
		}

		int loopCount = gifDecoder.getLoopCount();
		e.setRepeat(loopCount);
		e.start(outputStream);

		int w = newWidth;
		int h = newHeight;

		if (images != null && images.length > 0) {

			e.setSize(w, h);
			if (gifDecoder.transparency) {
				// since AnimatedGifEncoder will always make the background
				// as black so seting the default background as black
				e.setTransparent(Color.BLACK);
			}
			e.setDispose(gifDecoder.dispose);
			float perw = (float) w / gifDecoder.width;
			float perh = (float) h / gifDecoder.height;

			for (int i = 0; i < images.length; i++) {
				BufferedImage image = images[i];

				try {

					int x = Math.round(gifDecoder.getGifFrame(i).ix * perw);
					int y = Math.round(gifDecoder.getGifFrame(i).iy * perh);

					int x1 = Math.round(images[i].getWidth() * perw);
					int y1 = Math.round(images[i].getHeight() * perh);

					image = resizeImage(image, x1, y1);

					int t = gifDecoder.getDelay(i);
					e.setDelay(t); // 1 frame per sec

					e.addFrame(image, x, y);

				} catch (Throwable throwable) {
					throwable.printStackTrace();
					logger.warning("Failed to perform last action: "
							+ throwable.getMessage());
				}

				images[i] = image;
			} // iterate through the images

			e.finish();
			FileOutputStream out = new FileOutputStream(dstFileName);
			out.write(outputStream.toByteArray());

			try {
				outputStream.flush();
				outputStream.close();
				out.flush();
				out.close();
				// input.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				throw e1;
			}

		}

	}

	private static BufferedImage resizeImage(final BufferedImage img,
			final int newW, final int newH) {
		BufferedImage dimg = null;
		try {
			dimg = Scalr.resize(img, Method.SPEED, newW, newH);
			// saveImages(dimg);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return dimg;
	}

}
