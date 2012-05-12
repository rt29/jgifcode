package com.jgifcode.gif;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class GifAttributes {

	// TODO need to set the default value for these attributes
	private int loopCount = 0;
	private int delay = 1;
	private boolean transparency = false;
	private int dispose = 0;
	private int width;
	private int height;
	GifDecoder gifDecoder = null;

	public GifAttributes(final String gifFile) {
		gifDecoder = new GifDecoder();
		InputStream in = null;
		try {
			in = new FileInputStream(gifFile);
			gifDecoder.read(in);
			this.loopCount = gifDecoder.loopCount;
			this.dispose = gifDecoder.dispose;
			this.delay = gifDecoder.delay;
			this.transparency = gifDecoder.transparency;
			this.width = gifDecoder.width;
			this.height = gifDecoder.height;

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException ignore) {

				}
			}
		}

	}

	public GifAttributes(final int loopCount, final int dispose,
			final boolean transparency, final int delay, final int height,
			final int width) {
		this.loopCount = loopCount;
		this.delay = delay;
		this.dispose = dispose;
		this.transparency = transparency;
		this.height = height;
		this.width = width;
	}

	public GifAttributes(final int height, final int width) {
		this.height = height;
		this.width = width;
	}

	public int getLoopCount() {
		return loopCount;
	}

	public void setLoopCount(final int loopCount) {
		this.loopCount = loopCount;
	}

	public int getDelay() {
		return delay;
	}

	public void setDelay(final int delay) {
		this.delay = delay;
	}

	public boolean isTransparency() {
		return transparency;
	}

	public void setTransparency(final boolean transparency) {
		this.transparency = transparency;
	}

	public int getDispose() {
		return dispose;
	}

	public void setDispose(final int dispose) {
		this.dispose = dispose;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(final int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(final int height) {
		this.height = height;
	}

}
