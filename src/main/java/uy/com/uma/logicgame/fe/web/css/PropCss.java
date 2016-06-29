package uy.com.uma.logicgame.fe.web.css;

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
	public String toJSON() {
		return toJSON(this);
	}
	

	
	/**
	 * Retorna el objeto en formato JSON
	 */
	public static String toJSON (PropCss o) {
		return "{" + UtilJSON.getPropJSON(ID_ATT_REGLA) + UtilJSON.getValorJSON(o.getRegla()) +
					UtilJSON.getPropJSON(ID_ATT_PROPIEDAD) + UtilJSON.getValorJSON(o.getPropiedad()) +
					UtilJSON.getPropJSON(ID_ATT_VALOR) + UtilJSON.getComillasJSON(o.getValor()) + "}";
	}
}
