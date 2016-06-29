package uy.com.uma.logicgame.fe.web.actions;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import uy.com.uma.comun.util.UtilJSON;
import uy.com.uma.logicgame.api.LogicGameException;
import uy.com.uma.logicgame.api.persistencia.IManejadorSeguridad;
import uy.com.uma.logicgame.api.persistencia.PersistenciaException;
import uy.com.uma.logicgame.api.validacion.UtilValidacionParametros;

/**
 * Realiza el login en el sistema
 *
 * @author Santiago Dalchiele
 */
public class LoginAction extends SeguridadAbstractAction {

	public LoginAction() throws LogicGameException {
	}

	@Override
	public String getName() {
		return ID_REQ_LOGIN;
	}
	
	@Override
	protected boolean usaIdUsuario() {
		return false;
	}

	@Override
	public void perform(HttpServletRequest req, PrintWriter out) throws ServletException, IOException {
		if (validarParametros(req, out)) {
			String idUsuario = req.getParameter(ID_PARAM_ID_USUARIO);
			final String clave = req.getParameter(ID_PARAM_CLAVE);		
			
			try {
				final short loginResult = manSeg.login(idUsuario, clave);
				
				if (loginResult == IManejadorSeguridad.LOGIN_EXITOSO) {
					req.getSession(false).invalidate();
					req.getSession(true).setAttribute(JuegoAbstractAction.ID_PARAM_ID_USUARIO, manSeg.getIdUsuario(idUsuario));
				}
				
				out.write("{" + UtilJSON.getPropJSON(ID_PARAM_RESULTADO) + UtilJSON.getComillasJSON("" + loginResult) + "}");
			} catch (PersistenciaException e) {
				throw new ServletException("Error en el login de usuario", e);
			}
		}
	}		

	
	
	/**
	 * Valida los parametros id usuario (puede ser un correo) y clave
	 */
	@Override
	protected boolean validarParametros(HttpServletRequest req, PrintWriter out) {
		final String idUsuario = req.getParameter(ID_PARAM_ID_USUARIO);
		final String clave = req.getParameter(ID_PARAM_CLAVE);
		
		if (! UtilValidacionParametros.esValidaClave(clave))
			return super.validarParametros(req, out);
		else {
			if (!UtilValidacionParametros.esValidoIdUsuario(idUsuario)) {
				if (UtilValidacionParametros.esValidoCorreo(idUsuario))
					return true;
				else
					return super.validarParametros(req, out);
			} else
				return true;
		}
	}	
	
}
