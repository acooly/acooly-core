package com.acooly.module.security.jcaptcha;

import java.awt.Color;
import java.awt.Font;
import java.awt.image.ImageFilter;

import com.jhlabs.image.WaterFilter;
import com.octo.captcha.component.image.backgroundgenerator.BackgroundGenerator;
import com.octo.captcha.component.image.backgroundgenerator.GradientBackgroundGenerator;
import com.octo.captcha.component.image.color.ColorGenerator;
import com.octo.captcha.component.image.color.RandomListColorGenerator;
import com.octo.captcha.component.image.deformation.ImageDeformation;
import com.octo.captcha.component.image.deformation.ImageDeformationByFilters;
import com.octo.captcha.component.image.fontgenerator.FontGenerator;
import com.octo.captcha.component.image.fontgenerator.RandomFontGenerator;
import com.octo.captcha.component.image.textpaster.DecoratedRandomTextPaster;
import com.octo.captcha.component.image.textpaster.TextPaster;
import com.octo.captcha.component.image.textpaster.textdecorator.BaffleTextDecorator;
import com.octo.captcha.component.image.textpaster.textdecorator.TextDecorator;
import com.octo.captcha.component.image.wordtoimage.DeformedComposedWordToImage;
import com.octo.captcha.component.image.wordtoimage.WordToImage;
import com.octo.captcha.component.word.wordgenerator.RandomWordGenerator;
import com.octo.captcha.component.word.wordgenerator.WordGenerator;
import com.octo.captcha.engine.image.ListImageCaptchaEngine;
import com.octo.captcha.image.gimpy.GimpyFactory;

public class AcoolyImageCaptchaEngine extends ListImageCaptchaEngine {

	@Override
	protected void buildInitialFactories() {
//		WordGenerator wordGenerator = new RandomWordGenerator("0123456789");
//		Color[] color = new Color[] { new Color(13, 141, 20), new Color(25, 74, 180) };
//		// , new Color(171, 24, 4),new Color(185, 149, 3), new Color(13, 0, 20)
//		ColorGenerator colorGenerator = new RandomListColorGenerator(color);
//		// 气泡干扰
//		TextDecorator textDecorator = new BaffleTextDecorator(0, Color.WHITE);
//		// 自定义整体干扰线
//		InterferingLineTextDecorator lineTextDecorator = new InterferingLineTextDecorator(colorGenerator);
//		TextPaster textPaster = new DecoratedRandomTextPaster(4, 4, colorGenerator, true, new TextDecorator[] {
//				textDecorator, lineTextDecorator });
//
//		// 背景图生成
//		BackgroundGenerator backgroundGenerator = new GradientBackgroundGenerator(60, 38, Color.white, Color.LIGHT_GRAY);
//
//		// 字体样式生成
//		Font[] fonts = { new Font("Verdana", Font.BOLD, 25), new Font("Verdana", Font.ITALIC, 25),
//				new Font("Tahoma", Font.BOLD, 25), new Font("Tahoma", Font.ITALIC, 25) };
//		FontGenerator fontGenerator = new RandomFontGenerator(25, 28, fonts);
//
//		// 过滤器
//		WaterFilter water = new WaterFilter();
////		water.setAmplitude(2d);// 振幅
////		water.setAntialias(true);// 显示字会出现锯齿状,true就是平滑的
//		// water.setPhase(30d);//月亮的盈亏
////		water.setWavelength(60d);//
//
//		ImageDeformation backDef = new ImageDeformationByFilters(new ImageFilter[] {});
//		ImageDeformation textDef = new ImageDeformationByFilters(new ImageFilter[] {});
////		ImageDeformation postDef = new ImageDeformationByFilters(new ImageFilter[] { water });
//
//		WordToImage wordToImage = new DeformedComposedWordToImage(fontGenerator, backgroundGenerator, textPaster,
//				backDef, textDef, postDef);
//		this.addFactory(new GimpyFactory(wordGenerator, wordToImage));
	}
}

