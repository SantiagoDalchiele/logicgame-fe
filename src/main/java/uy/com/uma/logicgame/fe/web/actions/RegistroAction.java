package uy.com.uma.logicgame.fe.web.actions;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import uy.com.uma.comun.util.UtilJSON;
import uy.com.uma.logicgame.api.LogicGameException;
import uy.com.uma.logicgame.api.persistencia.PersistenciaException;
import uy.com.uma.logicgame.api.validacion.UtilValidacionParametros;

/**
 * Registra el usuario en el sistema
 *
 * @author Santiago Dalchiele
 */
public class RegistroAction extends SeguridadAbstractAction {

	public RegistroAction() throws LogicGameException {
	}

	@Override
	public String getName() {
		return ID_REQ_REGISTRO;
	}
	
	@Override
	protected boolean usaIdUsuario() {
		return false;
	}

	@Override
	public void perform(HttpServletRequest req, PrintWriter out) throws ServletException, IOException {
		if (validarParametros(req, out)) {
			final String idioma = req.getParameter(ID_PARAM_IDIOMA);
			final String idUsuario = req.getParameter(ID_PARAM_ID_USUARIO);
			final String correo = req.getParameter(ID_PARAM_CORREO);
			final String clave = req.getParameter(ID_PARAM_CLAVE);		
	
			try {
				final short result = manSeg.registro(idioma, idUsuario, correo, clave);
				out.write("{" + UtilJSON.getPropJSON(ID_PARAM_RESULTADO) + UtilJSON.getValorJSON("" + result) + 
								UtilJSON.getPropJSON(ID_PARAM_ID_USUARIO) + UtilJSON.getComillasJSON(idUsuario) + "}");
			} catch (PersistenciaException e) {
				throw new ServletException("Error en el registro del usuario", e);
			}
		}
	}

	
	
	/**
	 * Valida los parametros idioma, id usuario, correo y clave
	 */
	protected boolean validarParametros(HttpServletRequest req, PrintWriter out) {
		final String idioma = req.getParameter(ID_PARAM_IDIOMA);
		final String idUsuario = req.getParameter(ID_PARAM_ID_USUARIO);
		final String correo = req.getParameter(ID_PARAM_CORREO);
		final String clave = req.getParameter(ID_PARAM_CLAVE);
		
		if (UtilValidacionParametros.esValidoIdIdioma(idioma) &&
				UtilValidacionParametros.esValidoIdUsuario(idUsuario) &&
				UtilValidacionParametros.esValidoCorreo(correo) &&
				UtilValidacionParametros.esValidaClave(clave))
			return true;
		else
			return super.validarParametros(req, out);
	}
	
}
