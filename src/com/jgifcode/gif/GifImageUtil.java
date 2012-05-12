package com.jgifcode.gif;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import com.jgifcode.gif.Scalr.Rotation;

public class GifImageUtil {

	static Logger logger = Logger.getLogger(GifImageUtil.class.getName());

	
	public enum FlipDirection{
		HORIZONTAL , VERTICAL;
	}
	
	public enum RotationDegree{
		DEGREE_90 , DEGREE_180 , DEGREE_270;
	}
	
	
	
	/**
	 * 
	 * @param filePath
	 * @return
	 */
	public static BufferedImage[] readGif(final String filePath) {
		InputStream in = null;
		try {
			in = new FileInputStream(filePath);
			ImageInputStream imageStream = ImageIO.createImageInputStream(in);

			return getImages(imageStream);

		} catch (IOException e) {
			throw new IllegalArgumentException(
					"Unable to get image stream from input stream");
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					logger.warning("Unable to close the file. "
							+ e.getMessage());
				}
			}
		}

	}

	/**
	 * 
	 * @param filePath
	 * @param images
	 * @return
	 */
	public static void writeGif(final String filePath,
			final BufferedImage[] images, final GifAttributes attributes)
			throws IOException {

		OutputStream outputStream = new FileOutputStream(filePath);
		AnimatedGifEncoder e = new AnimatedGifEncoder();

		int loopCount = attributes.getLoopCount();
		e.setRepeat(loopCount);
		e.start(outputStream);

		if (images != null && images.length > 0) {

			e.setSize(attributes.getWidth(), attributes.getHeight());
			if (attributes.isTransparency()) {
				// since AnimatedGifEncoder will always make the background
				// as black so seting the default background as black
				e.setTransparent(Color.BLACK);
			}
			e.setDispose(attributes.getDispose());

			for (int i = 0; i < images.length; i++) {
				BufferedImage image = images[i];

				try {
					int t = attributes.getDelay();
					int startX = 0, startY = 0;
					if (attributes.gifDecoder != null) {
						t = attributes.gifDecoder.getDelay(i);
						startX = attributes.gifDecoder.getGifFrame(i).ix;
						startY = attributes.gifDecoder.getGifFrame(i).iy;
					}
					e.setDelay(t); // 1 frame per sec

					e.addFrame(image, startX, startY);

				} catch (Throwable throwable) {
					throwable.printStackTrace();
					logger.warning("Failed to perform last action: "
							+ throwable.getMessage());
				}

			} // iterate through the images

			e.finish();

			outputStream.flush();
			outputStream.close();

		}

	}

	/**
	 * 
	 * @param srcPath
	 * @param dstPath
	 * @param newWidth
	 * @param newHeight
	 * @throws IOException
	 */
	public static void resizeGif(final String srcPath, final String dstPath,
			final int newWidth, final int newHeight) throws IOException {
		ResizeGifImage.resize(srcPath, dstPath, newWidth, newHeight);

	}
	
	public static void flipGif(final String srcPath, final String dstPath,
			final FlipDirection direction) {
		try {
			GifAttributes attributes = new GifAttributes(srcPath);

			BufferedImage[] images = null;
			images = GifImageUtil.readGif(srcPath);
			
			BufferedImage[] flippedImage = new BufferedImage[images.length];
			
			int i=0;
			for(BufferedImage image : images){
				switch(direction){
				case HORIZONTAL:
					flippedImage[i++] = Scalr.rotate(image, Rotation.FLIP_HORZ);
					break;
				case VERTICAL:
					flippedImage[i++] = Scalr.rotate(image, Rotation.FLIP_VERT);
					break;
					
				}
			}
			GifImageUtil.writeGif(dstPath, flippedImage, attributes);

		} catch (Exception ex) {
			logger.severe(ex.toString());
		}
	}
	
	public static void rotateGif(final String srcPath, final String dstPath,
			final RotationDegree degree) {
		try {
			GifAttributes attributes = new GifAttributes(srcPath);

			BufferedImage[] images = null;
			images = GifImageUtil.readGif(srcPath);
			
			BufferedImage[] flippedImage = new BufferedImage[images.length];
			
			int i=0;
			for(BufferedImage image : images){
				switch(degree){
				case DEGREE_90:
					flippedImage[i++] = Scalr.rotate(image, Rotation.CW_90);
					break;
				case DEGREE_180:
					flippedImage[i++] = Scalr.rotate(image, Rotation.CW_180);
					break;
				case DEGREE_270:
					flippedImage[i++] = Scalr.rotate(image, Rotation.CW_270);
					break;
					
				}
			}
			GifImageUtil.writeGif(dstPath, flippedImage, attributes);

		} catch (Exception ex) {
			logger.severe(ex.toString());
		}
	}
	

	private static BufferedImage[] getImages(final ImageInputStream imageStream)
			throws IllegalArgumentException {
		BufferedImage image = null;
		ArrayList images = new ArrayList();

		Iterator readers = ImageIO.getImageReaders(imageStream);

		if (readers.hasNext()) {
			while (readers.hasNext()) {
				ImageReader reader = (ImageReader) readers.next();
				int numFrames = 0;
				reader.setInput(imageStream);

				while (true) {
					try {
						// Read the frame
						image = reader.read(numFrames);
						images.add(image);

						// Bump up the frame counter...wait to do this until
						// after the read() call completes successly.
						++numFrames;
					} catch (IndexOutOfBoundsException e) {
						// This gets thrown when we reach the end of the
						// image frames. Just break out of the read() loop.
						// results.addFeedback("Caught: " +
						// e.getClass().getName() + ": " + e.getMessage());
						break;
					} catch (IOException e) {
						break;
					}
				}
			}
		} else {
			logger.warning("Did not find any readers for the giving ImageInputStream");
		}

		return (BufferedImage[]) (images.toArray(new BufferedImage[images
				.size()]));
	}

	public static void main(final String[] args) {

		try {

			if (args.length < 4) {
				System.out
						.println("Usage:java GifImageUtil <src_filename> <destination_filename> <height> <width>");
			} else {

				GifImageUtil.rotateGif(args[0], args[1],RotationDegree.DEGREE_180);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
