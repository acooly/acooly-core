package com.acooly.module.security.jcaptcha;

import java.awt.Color;

import com.octo.captcha.component.image.backgroundgenerator.BackgroundGenerator;
import com.octo.captcha.component.image.backgroundgenerator.FunkyBackgroundGenerator;
import com.octo.captcha.component.image.textpaster.SimpleTextPaster;
import com.octo.captcha.component.image.wordtoimage.ComposedWordToImage;
import com.octo.captcha.component.word.wordgenerator.RandomWordGenerator;
import com.octo.captcha.engine.image.ListImageCaptchaEngine;
import com.octo.captcha.image.gimpy.GimpyFactory;

/**
 * SpringSide Custom的认证图片
 * 
 * @author cac
 */
public class SpringSideCaptchaEngine extends ListImageCaptchaEngine {

	/**
	 * @Override
	 */
	protected void buildInitialFactories() {
		com.octo.captcha.component.word.wordgenerator.WordGenerator wordGenerator = (new RandomWordGenerator(
				"0123456789"));
		// nteger minAcceptedWordLength, Integer maxAcceptedWordLength,Color[]
		// textColors
		com.octo.captcha.component.image.textpaster.TextPaster textPaster = new SimpleTextPaster(4, 4, Color.DARK_GRAY);
		// Integer width, Integer height
		BackgroundGenerator backgroundGenerator = new FunkyBackgroundGenerator(40, 25);

		// Integer minFontSize, Integer maxFontSize
		com.octo.captcha.component.image.fontgenerator.FontGenerator fontGenerator = new ImageFontGenerator(10, 15);
		com.octo.captcha.component.image.wordtoimage.WordToImage wordToImage = new ComposedWordToImage(fontGenerator,
				backgroundGenerator, textPaster);
		addFactory(new GimpyFactory(wordGenerator, wordToImage));
	}
}
