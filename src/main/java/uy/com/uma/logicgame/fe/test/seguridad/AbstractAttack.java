package uy.com.uma.logicgame.fe.test.seguridad;

import java.io.IOException;
import java.io.OutputStream;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;

import uy.com.uma.comun.util.UtilString;

/**
 * Propiedades y metodos comunes de los ataques
 *
 * @author Santiago Dalchiele
 */
abstract class AbstractAttack {

	protected String url;
	protected String charset = StandardCharsets.ISO_8859_1.name();
	protected Map<String, Object> parametros = new HashMap<String, Object>();
	protected String tagResultado = "resultado";
	
	
	
	public AbstractAttack(String url) {
		this.url = url;
	}	
	
	public AbstractAttack(String url, String charset) {
		this.url = url;
		this.charset = charset;
	}
	
	
	
	/**
	 * Setea los parametros
	 */
	public abstract void setParameters();
	
	
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getCharset() {
		return charset;
	}
	public void setCharset(String charset) {
		this.charset = charset;
	}
	public Map<String, Object> getParametros() {
		return parametros;
	}
	public String getTagResultado() {
		return tagResultado;
	}
	public void setTagResultado (String tagResultado) {
		this.tagResultado = tagResultado;
	}
	
	
	
	/**
	 * Retorna el resultado del ataque
	 */
	public int doAttack() throws IOException {
		Scanner scanner = null;
		JsonReader reader = null;
		setParameters();
		
		try {
			final String query = getQueryString();
			System.out.println("Enviando un post a [" + url + "]");
			URLConnection connection = new URL(url + "?" + query).openConnection();
			connection.setDoOutput(true); // Triggers POST.
			connection.setRequestProperty("Accept-Charset", charset);
			connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=" + charset);
			OutputStream output = connection.getOutputStream();
			output.write(query.getBytes(charset));	
			scanner = new Scanner(connection.getInputStream());
		    final String jsonResponse = scanner.useDelimiter("\\A").next();
		    reader = Json.createReader(new StringReader(jsonResponse));
		    final JsonObject jObject = reader.readObject();
		    
		    if ((jObject == null) || (!jObject.containsKey(tagResultado)))
		    	return 0;
		    else
		    	return Integer.parseInt(UtilString.quitarComillas(jObject.get(tagResultado).toString()));
		} finally {
			try { if (reader != null) reader.close(); } catch (Exception e) {}
			try { if (scanner != null) scanner.close(); } catch (Exception e) {}
		}
	}
	
	
	
	/**
	 * Retorna el QueryString con los identificadores de los parámetros y sus valores codificados 
	 */
	private String getQueryString() throws UnsupportedEncodingException {
		String query = UtilString.concatenar(parametros.keySet(), "=%s&") + "=%s";
		Collection<String> valuesCoded = new ArrayList<String>();
		
		for (String key : parametros.keySet())
			valuesCoded.add(URLEncoder.encode(parametros.get(key).toString(), charset));
			
		return String.format(query, valuesCoded.toArray());
	}
}
