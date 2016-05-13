package com.huangbolun.sershou.waterex;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Js {
	public static String network() throws IOException {

		StringBuffer result = new StringBuffer();
		try {
			String url = "http://61.142.71.63:9090/VCIS/controller.do?login&id=get_secondhand_info&login_name=xx&key=value";
			URL getUrl = new URL(url);
			HttpURLConnection connection = (HttpURLConnection) getUrl          
					.openConnection();
			connection.connect();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					connection.getInputStream(),"utf-8"));
			String line;

			while ((line = reader.readLine()) != null) {
				System.out.println(line);
				result.append(line);
			}
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}
		return result.toString();

	}
}
