package uy.com.uma.logicgame.fe.web.css;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.json.JsonObject;

import uy.com.uma.comun.util.UtilJSON;

/**
 * Clase que encapsula una propiedad:valor de una regla de una hoja de una clase de estilos (.css)
 *
 * @author Santiago Dalchiele
 */
public class PropCss {
	
	/** Definición de constantes de atributos para enumerar en JSON */
	private static final String ID_ATT_REGLA = "regla"; 
	private static final String ID_ATT_PROPIEDAD = "propiedad";
	private static final String ID_ATT_VALOR = "valor";

	
	private String regla;
	private String propiedad;
	private String valor;
	
	
	
	public PropCss() {		
	}
	public PropCss(String regla, String propiedad, String valor) {
		this.regla = regla;
		this.propiedad = propiedad;
		this.valor = valor;
	}	
	
	
	
	public String getRegla() {
		return regla;
	}
	public void setRegla(String regla) {
		this.regla = regla;
	}
	public String getPropiedad() {
		return propiedad;
	}
	public void setPropiedad(String propiedad) {
		this.propiedad = propiedad;
	}
	public String getValor() {
		return valor;
	}
	public void setValor(String valor) {
		this.valor = valor;
	}
	public JsonObject toJSON() {
		return toJSON(this);
	}
	

	
	/**
	 * Retorna el objeto en formato JSON
	 */
	public static JsonObject toJSON (PropCss o) {
		Map<String, Object> props = new LinkedHashMap<String, Object>();
		props.put(ID_ATT_REGLA, o.getRegla());
		props.put(ID_ATT_PROPIEDAD, o.getPropiedad());
		props.put(ID_ATT_VALOR, o.getValor());
		return UtilJSON.getJSONObject(props);
	}
}
