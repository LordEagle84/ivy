package ch.monokellabs.test;

import java.io.File;

import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;

/**
 * Helper to use a fixed (portable) version of firefox. 
 * So selenium doesn't need an update to be compatible with the current firefox version under test.
 * 
 * <p>The portable firefox must be installed as follows:<ol>
 * <li>install with a FF portable installer binary</li>
 * <li>disconnect your network adapter</li>
 * <li>start the portable firefox</li>
 * <li>deactivate update via URL 'about:config'
 *   <ul>
 *     <li>app.update.enabled = false</li>
 *     <li>app.update.url = ""</li>
 *     <li>app.update.url.manual = ""</li>
 *   </ul>
 * </li>
 * <li>close firefox</li>
 * <li>reconnect your network adapter</li>
 * <li>start firefox several times -> no updates should be installed</li></ol>
 */
public class FirefoxPortable{
	private static final String FIREFOX_PORTABLE_PATH_PROPERTY = "firefox.portable.path";
	
	public static FirefoxPortable createWithSystemProperty()
	{
	    String firefoxPortablePath = System.getProperty(FIREFOX_PORTABLE_PATH_PROPERTY);
	    if (firefoxPortablePath == null){
	    	throw new IllegalStateException("JVM System Property '"+FIREFOX_PORTABLE_PATH_PROPERTY+"' is not set. "
	    			+ "Firefox portable can not be used.");
	    }
	    
	    File ffPortableDir = new File(firefoxPortablePath);
	    if (!ffPortableDir.exists() || !ffPortableDir.isDirectory()){
	    	throw new IllegalArgumentException("The JVM system property '"+FIREFOX_PORTABLE_PATH_PROPERTY+"' "
	    			+ "does reference a not existing directory in '"+ffPortableDir.getAbsolutePath()+"'."
	    					+ "Firefox portable can not be used.");
	    }
	    
	    return new FirefoxPortable(ffPortableDir);
	}
	
	private File ffDir;
	private FirefoxProfile profile;

	public FirefoxPortable(File ffPortableDir){
		this.ffDir = ffPortableDir;
	}
	
	public FirefoxProfile profile(){
		if (profile == null){
			File profileDir = new File(ffDir, "Data/profile");
			if (!profileDir.exists()){
				throw new IllegalStateException("Firefox portable profile directory in '"+
						profileDir.getAbsolutePath()+"' does not exist.");
			}
			profile = new FirefoxProfile(profileDir);
		}
		return profile;
	}
	
	/**
	 * @param language e.g. 'de' or 'en'.
	 * @return
	 */
	public FirefoxPortable setLanguage(String language){
		profile().setPreference("intl.accept_languages", language);
		return this;
	}
	
	public FirefoxDriver createDriver(){
		File firefoxBinaryFile = new File(ffDir, "App/Firefox/firefox.exe");
		if (!firefoxBinaryFile.exists()){
			throw new IllegalStateException("The firefox portable binary file '"+
					firefoxBinaryFile.getAbsolutePath()+"' does not exist.");
		}
		FirefoxBinary ffBinary = new FirefoxBinary(firefoxBinaryFile);
		return new FirefoxDriver(ffBinary, profile());
	}
	
}