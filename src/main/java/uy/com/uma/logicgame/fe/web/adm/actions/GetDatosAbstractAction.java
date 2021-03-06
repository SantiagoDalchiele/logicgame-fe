package uy.com.uma.logicgame.fe.web.adm.actions;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;

import javax.json.JsonObject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import uy.com.uma.comun.util.UtilJSON;
import uy.com.uma.logicgame.api.bean.IJSONObject;
import uy.com.uma.logicgame.api.conf.ConfiguracionException;
import uy.com.uma.logicgame.api.persistencia.PersistenciaException;

/**
 * Clase gen�rica que retorna colecci�n una colecci�n de datos persistidos en la base de datos
 *
 * @author Santiago Dalchiele
 */
abstract class GetDatosAbstractAction extends AdmAbstractAction {

	
	/** Retorna el TAG o identificador de atributo con el que se nombra a la colecci�n de datos a retornar en formato JSON */
	protected abstract String getTagColeccion();
	
	/** Retorna la colecci�n de datos a retornar */
	protected abstract Collection<IJSONObject> getDatos() throws PersistenciaException, InstantiationException, IllegalAccessException, ClassNotFoundException, ConfiguracionException;
	
	
	
	@Override
	public boolean usaConexionBD() {
		return true;
	}
	
	
	/**
	 * Retorna la colecci�n de datos
	 */
	@Override
	public void perform(HttpServletRequest req, PrintWriter out) throws ServletException, IOException {
		try {
			if (checkLogin(req, out)) {				
				Collection<JsonObject> datos = new ArrayList<JsonObject>();
				
				for (IJSONObject o : getDatos())
					datos.add(o.toJSON());
				
				out.write(UtilJSON.getJSONObject(getTagColeccion(), datos.toArray()).toString());				
			}
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | 
					ConfiguracionException | PersistenciaException e) {
			throw new ServletException(e);
		}
	}
}
