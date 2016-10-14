package uy.com.uma.logicgame.fe.web.actions;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import uy.com.uma.logicgame.api.LogicGameException;
import uy.com.uma.logicgame.api.persistencia.PersistenciaException;
import uy.com.uma.logicgame.api.validacion.UtilValidacionParametros;

/**
 * Accion que atiende el reseteo de la clave.  
 * Controla que en session exista el usuario registrado previamente por la acción RecibirToken.
 * Resetea la password el usuario y redirige a la página de inicio del juego.
 *
 * @author Santiago Dalchiele
 */
public class ResetClaveAction extends SeguridadAbstractAction {

	public ResetClaveAction() throws LogicGameException {		
	}
	
	@Override
	public String getName() {
		return ID_REQ_RESET_CLAVE;
	}
	
	@Override
	protected boolean usaIdUsuario() {
		return false;
	}

	@Override
	public void perform(HttpServletRequest req, PrintWriter out) throws ServletException, IOException {
		if (validarParametros(req, out)) {
			HttpSession session = req.getSession();
			
			if (session.getAttribute(ID_USUARIO_TOKEN) == null)
				out.write(getErrorJSON (MSG_PARAMS_INCORRECTOS));
			else {
				final String idUsuario = session.getAttribute(ID_USUARIO_TOKEN).toString();
			
				try {
					if (!manSeg.existeUsuario(idUsuario))
						out.write(getErrorJSON (MSG_PARAMS_INCORRECTOS));
					else {
						final String clave = req.getParameter(ID_PARAM_CLAVE);
						manSeg.resetClave(idUsuario, clave);
						session.removeAttribute(ID_USUARIO_TOKEN);
						req.getSession(false).invalidate();
						out.write(RESPUESTA_JSON_DEFECTO_OK);
					}
				} catch (PersistenciaException e) {
					throw new ServletException("Error al recibir el token", e);
				}
			}
		}
	}

	
	
	/**
	 * Valida el parametro id usuario (NO puede ser un correo)
	 */
	@Override
	protected boolean validarParametros(HttpServletRequest req, PrintWriter out) {
		final String clave = req.getParameter(ID_PARAM_CLAVE);		
		
		if (UtilValidacionParametros.esValidaClave(clave))
			return true;
		else
			return super.validarParametros(req, out);
	}
}
