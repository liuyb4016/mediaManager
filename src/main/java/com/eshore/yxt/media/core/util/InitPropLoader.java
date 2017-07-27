/**
 */
package com.eshore.yxt.media.core.util;

import java.io.InputStream;
import java.util.Properties;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2007</p>
 * <p>Company: poson</p>
 * <p><a href="InitPropLoader.java.html"><i>View Source</i></a></p>
 * @author <a href="Yusm@gsta.com">Yu Songming</a>
 * @version 1.0
 * Modified at:
 * Modified by:
 */
public class InitPropLoader {
	
	public Properties getProperties(String propName){
		Properties prop = new Properties();
		InputStream in = null;
		try {
			in = (InitPropLoader.class).getResourceAsStream("/"+propName);
			prop.load(in);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (Exception e) {
			}
		}
		return prop;
	}
	
}
