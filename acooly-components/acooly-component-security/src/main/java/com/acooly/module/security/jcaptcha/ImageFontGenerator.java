package com.acooly.module.security.jcaptcha;

import java.awt.Font;

import com.octo.captcha.component.image.fontgenerator.FontGenerator;

public class ImageFontGenerator implements FontGenerator {
	/** 最小字体 */
	private int minFontSize;

	/** 最大字体 */
	private int maxFontSize;

	/** 构造函数 */
	public ImageFontGenerator(int maxFontSize, int minFontSize) {
		this.maxFontSize = maxFontSize;
		this.minFontSize = minFontSize;
	}

	/** 返回字体 */
	public Font getFont() {
		Font font = new Font("fixedsys", this.maxFontSize, this.minFontSize);
		return font;

	}

	public int getMinFontSize() {
		return this.minFontSize;
	}

	public int getMaxFontSize() {
		return this.maxFontSize;
	}

	public void setMaxFontSize(int maxFontSize) {
		this.maxFontSize = maxFontSize;
	}

	public void setMinFontSize(int minFontSize) {
		this.minFontSize = minFontSize;
	}

}
