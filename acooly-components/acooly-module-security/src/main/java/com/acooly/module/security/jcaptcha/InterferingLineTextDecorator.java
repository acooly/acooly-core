package com.acooly.module.security.jcaptcha;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.security.SecureRandom;
import java.text.AttributedString;
import java.util.Random;

import com.octo.captcha.component.image.color.ColorGenerator;
import com.octo.captcha.component.image.color.SingleColorGenerator;
import com.octo.captcha.component.image.textpaster.ChangeableAttributedString;
import com.octo.captcha.component.image.textpaster.textdecorator.TextDecorator;

/**
 * jcaptcha1.0-干扰线
 * 
 * @author zhangpu
 * 
 */
public class InterferingLineTextDecorator implements TextDecorator {

	private Random myRandom = new SecureRandom();
	private int alphaCompositeType = AlphaComposite.SRC_OVER;

	private ColorGenerator holesColorGenerator;
	private int width = 100;
	private int height = 40;

	public InterferingLineTextDecorator() {
		super();
	}

	/**
	 * @param holesColorGenerator
	 */
	public InterferingLineTextDecorator(ColorGenerator holesColorGenerator) {
		this.holesColorGenerator = holesColorGenerator;
	}

	public InterferingLineTextDecorator(Color holesColor) {
		this.holesColorGenerator = new SingleColorGenerator(holesColor != null ? holesColor : Color.white);
	}

	@Override
	public void decorateAttributedString(Graphics2D g2, AttributedString arg1, ChangeableAttributedString arg2) {

		Color oldColor = g2.getColor();
		Composite oldComp = g2.getComposite();
		g2.setComposite(AlphaComposite.getInstance(alphaCompositeType));
		int y = myRandom.nextInt(height / 3 * 2);
		int h = myRandom.nextInt(height / 3 * 2) + height / 3;
		g2.drawLine(0, y, width, h);
		g2.setColor(oldColor);
		g2.setComposite(oldComp);
	}

	public ColorGenerator getHolesColorGenerator() {
		return holesColorGenerator;
	}

	public void setHolesColorGenerator(ColorGenerator holesColorGenerator) {
		this.holesColorGenerator = holesColorGenerator;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

}
