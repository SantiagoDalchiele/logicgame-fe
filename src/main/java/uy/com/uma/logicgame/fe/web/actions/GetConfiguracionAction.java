package uy.com.uma.logicgame.fe.web.actions;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.json.JsonObject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import uy.com.uma.comun.util.UtilJSON;
import uy.com.uma.logicgame.api.LogicGameException;
import uy.com.uma.logicgame.api.bean.ConfiguracionDO;
import uy.com.uma.logicgame.api.bean.IdiomaDO;
import uy.com.uma.logicgame.api.conf.ConfiguracionException;
import uy.com.uma.logicgame.api.persistencia.IManejadorConfiguracion;
import uy.com.uma.logicgame.api.persistencia.PersistenciaException;
import uy.com.uma.logicgame.api.persistencia.PersistenciaFactory;

/**
 * Implementa la acción que retorna la configuración registrada en el servidor
 *
 * @author Santiago Dalchiele
 */
public class GetConfiguracionAction extends JuegoAbstractAction {

	/** TAGs para retornar el objeto JSON */
	private static final String TAG_IDIOMA = ID_PARAM_IDIOMA;
	private static final String TAG_IDIOMAS = "idiomas";
	private static final String TAG_ID_IDIOMA = "id";
	private static final String TAG_NOMBRE_IDIOMA = "nombre";
	private static final String TAG_ICONO_IDIOMA = "icono";
	
	/** Manejadores de persistencia */
	private IManejadorConfiguracion manConf;
	
	
	
	/**
	 * Constructor
	 */
	public GetConfiguracionAction() throws LogicGameException {
		try {
			manConf = PersistenciaFactory.getInstancia().getManejadorConfiguracion();
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | ConfiguracionException e) {
			throw new LogicGameException("Error al obtener conexion a la base de datos", e);		
		}
	}
	
	
	@Override
	public String getName() {
		return ID_REQ_GET_CONFIGURACION;
	}	

	@Override
	public void reset() throws LogicGameException {
		try {
			manConf = PersistenciaFactory.getInstancia().getManejadorConfiguracion();
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | ConfiguracionException e) {
			throw new LogicGameException("Error al resetear conexion a la base de datos", e);
		}
	}

	@Override
	protected boolean usaIdUsuario() {
		return true;
	}

	@Override
	protected boolean usaMatriz() {
		return false;
	}

	
	
	/**
	 * Retorna:
	 * {"lang":<id_idioma_del usuario>,
	 *  "idiomas":[{"id":<id>,"nombre":<nombre>,"icono":<icono>},....]
	 * } 
	 */
	@Override
	public void perform(HttpServletRequest req, PrintWriter out) throws ServletException, IOException {
		String idUsuario = req.getSession().getAttribute(ID_PARAM_ID_USUARIO).toString();
		
		try {
			ConfiguracionDO dc = manConf.getDatosConfiguracion(idUsuario);
			Map<String, Object> props = new LinkedHashMap<String, Object>();
			Collection<JsonObject> idiomas = new ArrayList<JsonObject>();
			
			for (IdiomaDO di : dc.getIdiomas()) {
				Map<String, Object> idioma = new LinkedHashMap<String, Object>();
				idioma.put(TAG_ID_IDIOMA, di.getId());
				idioma.put(TAG_NOMBRE_IDIOMA, di.getNombre());
				idioma.put(TAG_ICONO_IDIOMA, di.getIcono());
				idiomas.add(UtilJSON.getJSONObject(idioma));
			}
			
			props.put(TAG_IDIOMA, dc.getDatosUsuario().getIdioma());
			props.put(TAG_IDIOMAS, idiomas.toArray());
			out.write(UtilJSON.getJSONObject(props).toString());
		} catch (PersistenciaException e) {
			throw new ServletException("Error al obtener la configuracion", e);
		}
	}
}
