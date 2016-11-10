package uy.com.uma.logicgame.fe.web;

import java.io.PrintWriter;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import uy.com.uma.comun.util.UtilJSON;
import uy.com.uma.comun.util.UtilString;

/**
 * Logica utilitaria para las acciones del front-end
 *
 * @author Santiago Dalchiele
 */
public abstract class ActionsHelper {

	/** Constante para identificar el tipo de error que el usuario no está logeado */ 
	protected static final int TIPO_ERROR_NO_LOGEADO = 2666;
	
	/** Mensaje de error parametros incorrectos */
	protected static final String MSG_PARAMS_INCORRECTOS = "Parametros incorrectos";
	
	/** Constantes que definen los nombres de las propiedades del objeto error */
	private static final String PROP_ERROR = "error";
	private static final String PROP_ERROR_TIPO = "tipo";
	private static final String PROP_ERROR_NRO = "nro";
	private static final String PROP_ERROR_MENSAJE = "mensaje";
	private static final String PROP_ERROR_DETALLE = "detalle";
	

	
	
	
	/**
	 * Retorna el texto del objeto JSON utilizado para enviar un error a la UI 
	 */
	public static String getErrorJSON (Exception e) {
		return getErrorJSON(2, 77, e.getMessage(), "");
	}
	
	
	
	/**
	 * Retorna el texto del objeto JSON utilizado para enviar un error a la UI 
	 */
	public static String getErrorJSON (String mensaje) {
		return getErrorJSON(1, 86, mensaje, "");
	}
	
	
	
	/**
	 * Retorna el texto del objeto JSON utilizado para enviar un error a la UI 
	 */
	protected static String getErrorJSON (int tipo, int nro, String mensaje, String detalle) {
		Map<String, Object> props = new LinkedHashMap<String, Object>();
		Map<String, Object> error = new LinkedHashMap<String, Object>();
		props.put(PROP_ERROR_TIPO, tipo);
		props.put(PROP_ERROR_NRO, nro);
		props.put(PROP_ERROR_MENSAJE, UtilString.quitarNulo(mensaje));
		props.put(PROP_ERROR_DETALLE, UtilString.quitarNulo(detalle));
		error.put(PROP_ERROR, UtilJSON.getJSONObject(props));
		return UtilJSON.getJSONObject(error).toString();
	}
	
	
	
	/**
	 * Validacion por defecto de parametros, escribe en el PrintWriter el error de parametros incorrectos y retorna false
	 */
	protected boolean validarParametros (HttpServletRequest req, PrintWriter out) {
       	out.write(getErrorJSON (MSG_PARAMS_INCORRECTOS));
       	return false;
	}
}
