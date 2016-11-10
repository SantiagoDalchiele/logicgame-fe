package uy.com.uma.logicgame.fe.test.seguridad;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;

import org.apache.tools.ant.filters.StringInputStream;

/**
 * http://stackoverflow.com/questions/2793150/using-java-net-urlconnection-to-fire-and-handle-http-requests
 * https://www.mkyong.com/java/how-to-send-http-request-getpost-in-java/
 * http://stackoverflow.com/questions/18899232/how-to-parse-this-json-response-in-java
 * 
 * LOGIN_EXITOSO = 1
 * LOGIN_USUARIO_INEXISTENTE = 2
 * LOGIN_CLAVE_INCORRECTA = 3
 * 
 * @author Santiago Dalchiele
 */
public class TestLoginAttack {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		String url = "http://192.168.184.169:8080/lgweb/login.do";
		String charset = StandardCharsets.ISO_8859_1.name();
		String usuario = "admin";
		String clave = "nolase";
		Scanner scanner = null;
		JsonReader reader = null;
		
		try {
			String query = String.format("idUsuario=%s&clave=%s", 
					URLEncoder.encode(usuario, charset), 
					URLEncoder.encode(clave, charset));
			System.out.println("Enviando un post a [" + url + "]");
			URLConnection connection = new URL(url + "?" + query).openConnection();
			connection.setDoOutput(true); // Triggers POST.
			connection.setRequestProperty("Accept-Charset", charset);
			connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=" + charset);

			OutputStream output = connection.getOutputStream();
			output.write(query.getBytes(charset));			
			InputStream response = connection.getInputStream();			
			scanner = new Scanner(response);
		    String jsonResponse = scanner.useDelimiter("\\A").next();
		    reader = Json.createReader(new StringInputStream(jsonResponse));
		    JsonObject jsonObject = reader.readObject();		    
		    int resultado = Integer.parseInt(jsonObject.getString("resultado"));
		    
		    if (resultado == 1) {
		    	System.out.println("Login exitoso, usuario=[" + usuario + "] clave [" + clave + "]");
		    } else if (resultado == 2)
		    	System.out.println("El usuario [" + usuario + "] no existe");
		    else if (resultado == 3)
		    	System.out.println("El usuario ["+ usuario + "] existe");
			
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try { if (reader != null) reader.close(); } catch (Exception e) {}
			try { if (scanner != null) scanner.close(); } catch (Exception e) {}
		}
	}
}
