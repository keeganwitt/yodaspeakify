/*
 * YodaspeakifyProfile.Java
 */

package com.appspot.yodaspeakify;

import com.google.wave.api.ProfileServlet;

/**
 * This class provides the profile for WorldCat-Bot
 * 
 * @author Keegan Witt
 */
@SuppressWarnings("serial")
public class YodaspeakifyProfile extends ProfileServlet {

	@Override
	public String getRobotName() {
		return "Yodaspeakify";
	}

	@Override
	public String getRobotAvatarUrl() {
		return "http://yodaspeakify.appspot.com/icon.jpg";
	}

	@Override
	public String getRobotProfilePageUrl() {
		return "http://yodaspeakify.appspot.com/";
	}
}