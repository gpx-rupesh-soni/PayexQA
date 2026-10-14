package rcom.payex.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AppConfig {	
	
	public static String OUTFILEDIR;
	public static String GPXFILEDIR;
	public static String CONFIGDIR;
	public static String BRGSTNDIR;
	
	@Value("#{ConfigProps['OUTFILEDIR']}")
	public void OUTFILEDIR(String OUTFILEDIR){
		AppConfig.OUTFILEDIR = OUTFILEDIR;
	}
	
	@Value("#{ConfigProps['GPXFILEDIR']}")
	public void GPXFILEDIR(String GPXFILEDIR){
		AppConfig.GPXFILEDIR = GPXFILEDIR;
	}
	
	@Value("#{ConfigProps['CONFIGDIR']}")
	public void CONFIGDIR(String CONFIGDIR){
		AppConfig.CONFIGDIR = CONFIGDIR;
	}
	
	@Value("#{ConfigProps['BRGSTNDIR']}")
	public void BRGSTNDIR(String BRGSTNDIR){
		AppConfig.BRGSTNDIR = BRGSTNDIR;
	}
}