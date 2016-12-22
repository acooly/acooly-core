package com.acooly.module.security.jcaptcha;

import java.awt.Color;
import java.awt.Font;

import com.octo.captcha.component.image.backgroundgenerator.BackgroundGenerator;
import com.octo.captcha.component.image.backgroundgenerator.GradientBackgroundGenerator;
import com.octo.captcha.component.image.color.ColorGenerator;
import com.octo.captcha.component.image.color.RandomListColorGenerator;
import com.octo.captcha.component.image.fontgenerator.FontGenerator;
import com.octo.captcha.component.image.fontgenerator.RandomFontGenerator;
import com.octo.captcha.component.image.textpaster.DecoratedRandomTextPaster;
import com.octo.captcha.component.image.textpaster.TextPaster;
import com.octo.captcha.component.image.textpaster.textdecorator.BaffleTextDecorator;
import com.octo.captcha.component.image.textpaster.textdecorator.TextDecorator;
import com.octo.captcha.component.image.wordtoimage.ComposedWordToImage;
import com.octo.captcha.component.image.wordtoimage.WordToImage;
import com.octo.captcha.component.word.wordgenerator.RandomWordGenerator;
import com.octo.captcha.component.word.wordgenerator.WordGenerator;
import com.octo.captcha.engine.image.ListImageCaptchaEngine;
import com.octo.captcha.image.gimpy.GimpyFactory;

public class ZtendImageCaptchaEngine extends ListImageCaptchaEngine {

	@Override
	protected void buildInitialFactories() {

		WordGenerator wordGenerator = new RandomWordGenerator("0123456789");

		Color[] color = new Color[] { new Color(13, 141, 20),
				new Color(25, 74, 180), new Color(171, 24, 4),
				new Color(185, 149, 3) };
		ColorGenerator colorGenerator = new RandomListColorGenerator(color);

		// 
		TextDecorator textDecorator = new BaffleTextDecorator(0, colorGenerator);
		TextPaster textPaster = new DecoratedRandomTextPaster(new Integer(4),
				new Integer(4), colorGenerator, true,
				new TextDecorator[] { textDecorator });

		// 背景图生成
		BackgroundGenerator backgroundGenerator = new GradientBackgroundGenerator(
				new Integer(100), new Integer(40), Color.LIGHT_GRAY,
				Color.LIGHT_GRAY);

		// 字体样式生成
		Font[] fonts = { new Font("Verdana", Font.BOLD, 28),
				new Font("Tahoma", Font.BOLD, 28) };
		FontGenerator fontGenerator = new RandomFontGenerator(new Integer(28),
				new Integer(28), fonts);

		// 
		WordToImage wordToImage = new ComposedWordToImage(fontGenerator,
				backgroundGenerator, textPaster);

		this.addFactory(new GimpyFactory(wordGenerator, wordToImage));

	}

}
