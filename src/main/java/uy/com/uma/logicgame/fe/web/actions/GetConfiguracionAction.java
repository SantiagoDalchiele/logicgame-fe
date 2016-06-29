package uy.com.uma.logicgame.fe.web.actions;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import uy.com.uma.comun.util.UtilJSON;
import uy.com.uma.logicgame.api.LogicGameException;
import uy.com.uma.logicgame.api.bean.DatosConfiguracion;
import uy.com.uma.logicgame.api.bean.DatosIdioma;
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
			DatosConfiguracion dc = manConf.getDatosConfiguracion(idUsuario);
			StringBuffer bufIdiomas = new StringBuffer();
			
			for (DatosIdioma di : dc.getIdiomas()) {
				bufIdiomas.append("{" + UtilJSON.getPropJSON(TAG_ID_IDIOMA) + UtilJSON.getValorJSON(di.getId()));
				bufIdiomas.append(UtilJSON.getPropJSON(TAG_NOMBRE_IDIOMA) + UtilJSON.getValorJSON(di.getNombre()));
				bufIdiomas.append(UtilJSON.getPropJSON(TAG_ICONO_IDIOMA) + UtilJSON.getComillasJSON(di.getIcono()) + "},");
			}
			
			if (bufIdiomas.length() > 0)
				bufIdiomas.deleteCharAt(bufIdiomas.length()-1);
			
			out.write("{" + UtilJSON.getPropJSON(TAG_IDIOMA) + UtilJSON.getValorJSON(dc.getDatosUsuario().getIdioma()) + 
							UtilJSON.getPropJSON(TAG_IDIOMAS) + "[" + bufIdiomas + "]}");
		} catch (PersistenciaException e) {
			throw new ServletException("Error al obtener la configuracion", e);
		}
	}
}
