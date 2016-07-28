package uy.com.uma.logicgame.fe.web.adm.actions;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import uy.com.uma.comun.util.UtilJSON;
import uy.com.uma.logicgame.api.bean.JuegoDO;
import uy.com.uma.logicgame.api.conf.ConfiguracionException;
import uy.com.uma.logicgame.api.persistencia.IManejadorJuego;
import uy.com.uma.logicgame.api.persistencia.PersistenciaException;
import uy.com.uma.logicgame.api.persistencia.PersistenciaFactory;
import uy.com.uma.logicgame.api.validacion.UtilValidacionParametros;

/**
 * Implementa la acci�n de obtener los juegos persistidos en la base de datos
 *
 * @author Santiago Dalchiele
 */
public class GetJuegosAction extends AdmAbstractAction {
	
	/** Constantes para los parametros de la p�gina */
	private static final String PARAM_IDIOMA = "idioma";

	/** TAGs para retornar el objeto JSON */
	private static final String TAG_JUEGOS = "juegos";
	
	
	
	@Override
	public String getName() {
		return ID_REQ_GET_JUEGOS;
	}

	
	/**
	 * Retorna la colecci�n de juegos de la siguiente forma:
	 * {"juegos":[j.toJSON(), ....] }
	 * @see uy.com.uma.logicgame.api.bean.JuegoDO
	 */
	@Override
	public void perform(HttpServletRequest req, PrintWriter out) throws ServletException, IOException {
		try {
			if ((checkLogin(req, out)) && (validarParametros(req, out))) {				
				final String idioma = req.getParameter(PARAM_IDIOMA);
				IManejadorJuego mj = PersistenciaFactory.getInstancia().getManejadorJuego();
				StringBuffer buf = new StringBuffer();
				
				for (JuegoDO j : mj.getJuegos(idioma)) 
					buf.append(j.toJSON() + ",");
				
				if (buf.length() > 0)
					buf.deleteCharAt(buf.length()-1);
				
				out.write("{" + UtilJSON.getPropJSON(TAG_JUEGOS) + "[" + buf + "]}");
			}
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | ConfiguracionException | PersistenciaException e) {
			throw new ServletException(e);
		}

	}

	
	
	/**
	 * Valida los parametros id usuario (puede ser un correo) y clave
	 */
	@Override
	protected boolean validarParametros(HttpServletRequest req, PrintWriter out) {
		final String idioma = req.getParameter(PARAM_IDIOMA);
		
		if (! UtilValidacionParametros.esValidoIdIdioma(idioma))
			return super.validarParametros(req, out);
		else 
			return true;
	}	
}
