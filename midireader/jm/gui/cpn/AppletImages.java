/*
 * AppletImages.java 0.0.1 8th July 2001
 *
 * Copyright (C) 2000, 2001 Andrew Sorensen, Andrew Brown, Adam Kirby
 *
 * <This Java Class is part of the jMusic API version 1.5, March 2004.>
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  
 *
 * See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */

package jm.gui.cpn;

import java.applet.Applet;
import java.awt.Image;
import java.net.URL;

/**
 * Defines images for use with {@link Stave} components loaded via an applet's
 * getImage method.
 * 
 * @see Stave
 * 
 * @author Andrew Sorensen, Andrew Brown, Adam Kirby
 * @version 0.0.1, 8th July 2001
 */
public class AppletImages implements Images {

	private final Image bassClef;

	private final Image crotchetDown;

	private final Image crotchetRest;

	private final Image crotchetUp;

	private final Image delete;

	private final Image dot;

	private final Image eight;

	private final Image five;

	private final Image flat;

	private final Image four;

	private final Image minimDown;

	private final Image minimRest;

	private final Image minimUp;

	private final Image natural;

	private final Image nine;

	private final Image one;

	private final Image quaverDown;

	private final Image quaverRest;

	private final Image quaverUp;

	private final Image semibreve;

	private final Image semibreveRest;

	private final Image semiquaverDown;

	private final Image semiquaverRest;

	private final Image semiquaverUp;

	private final Image seven;

	private final Image sharp;

	private final Image six;

	private final Image three;

	private final Image tieOver;

	private final Image tieUnder;

	private final Image trebleClef;

	private final Image two;

	/**
	 * Constructs a set of stave images loaded from the specified
	 * <code>applet</code>. For the images to be loaded correctly they must be
	 * stored as a series of .gif files (such as trebleClef.gif, bassClef.gif,
	 * etc.) in the path specified by <code>baseURL</code>.
	 * 
	 * @param applet
	 *            the Applet to be used to load the images
	 * @param baseURL
	 *            the path storing the .gif images to be loaded
	 */
	public AppletImages(final Applet applet, final URL baseURL) {
		trebleClef = applet.getImage(baseURL, "trebleClef.gif");
		bassClef = applet.getImage(baseURL, "bassClef.gif");
		crotchetDown = applet.getImage(baseURL, "crotchetDown.gif");
		crotchetUp = applet.getImage(baseURL, "crotchetUp.gif");
		quaverDown = applet.getImage(baseURL, "quaverDown.gif");
		quaverUp = applet.getImage(baseURL, "quaverUp.gif");
		semiquaverDown = applet.getImage(baseURL, "semiquaverDown.gif");
		semiquaverUp = applet.getImage(baseURL, "semiquaverUp.gif");
		minimDown = applet.getImage(baseURL, "minimDown.gif");
		minimUp = applet.getImage(baseURL, "minimUp.gif");
		semibreve = applet.getImage(baseURL, "semibreve.gif");
		dot = applet.getImage(baseURL, "dot.gif");
		semiquaverRest = applet.getImage(baseURL, "semiquaverRest.gif");
		quaverRest = applet.getImage(baseURL, "quaverRest.gif");
		crotchetRest = applet.getImage(baseURL, "crotchetRest.gif");
		minimRest = applet.getImage(baseURL, "minimRest.gif");
		semibreveRest = applet.getImage(baseURL, "semibreveRest.gif");
		sharp = applet.getImage(baseURL, "sharp.gif");
		flat = applet.getImage(baseURL, "flat.gif");
		natural = applet.getImage(baseURL, "natural.gif");
		one = applet.getImage(baseURL, "one.gif");
		two = applet.getImage(baseURL, "two.gif");
		three = applet.getImage(baseURL, "three.gif");
		four = applet.getImage(baseURL, "four.gif");
		five = applet.getImage(baseURL, "five.gif");
		six = applet.getImage(baseURL, "six.gif");
		seven = applet.getImage(baseURL, "seven.gif");
		eight = applet.getImage(baseURL, "eight.gif");
		nine = applet.getImage(baseURL, "nine.gif");
		delete = applet.getImage(baseURL, "delete.gif");
		tieOver = applet.getImage(baseURL, "tieOver.gif");
		tieUnder = applet.getImage(baseURL, "tieUnder.gif");
	}

	@Override
	public Image getBassClef() {
		return bassClef;
	}

	@Override
	public Image getCrotchetDown() {
		return crotchetDown;
	}

	@Override
	public Image getCrotchetRest() {
		return crotchetRest;
	}

	@Override
	public Image getCrotchetUp() {
		return crotchetUp;
	}

	@Override
	public Image getDelete() {
		return delete;
	}

	@Override
	public Image getDot() {
		return dot;
	}

	@Override
	public Image getEight() {
		return eight;
	}

	@Override
	public Image getFive() {
		return five;
	}

	@Override
	public Image getFlat() {
		return flat;
	}

	@Override
	public Image getFour() {
		return four;
	}

	@Override
	public Image getMinimDown() {
		return minimDown;
	}

	@Override
	public Image getMinimRest() {
		return minimRest;
	}

	@Override
	public Image getMinimUp() {
		return minimUp;
	}

	@Override
	public Image getNatural() {
		return natural;
	}

	@Override
	public Image getNine() {
		return nine;
	}

	@Override
	public Image getOne() {
		return one;
	}

	@Override
	public Image getQuaverDown() {
		return quaverDown;
	}

	@Override
	public Image getQuaverRest() {
		return quaverRest;
	}

	@Override
	public Image getQuaverUp() {
		return quaverUp;
	}

	@Override
	public Image getSemibreve() {
		return semibreve;
	}

	@Override
	public Image getSemibreveRest() {
		return semibreveRest;
	}

	@Override
	public Image getSemiquaverDown() {
		return semiquaverDown;
	}

	@Override
	public Image getSemiquaverRest() {
		return semiquaverRest;
	}

	@Override
	public Image getSemiquaverUp() {
		return semiquaverUp;
	}

	@Override
	public Image getSeven() {
		return seven;
	}

	@Override
	public Image getSharp() {
		return sharp;
	}

	@Override
	public Image getSix() {
		return six;
	}

	@Override
	public Image getThree() {
		return three;
	}

	@Override
	public Image getTieOver() {
		return tieOver;
	}

	@Override
	public Image getTieUnder() {
		return tieUnder;
	}

	@Override
	public Image getTrebleClef() {
		return trebleClef;
	}

	@Override
	public Image getTwo() {
		return two;
	}
}
