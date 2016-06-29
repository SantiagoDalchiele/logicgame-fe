package uy.com.uma.logicgame.fe.web.actions;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import uy.com.uma.logicgame.api.LogicGameException;
import uy.com.uma.logicgame.api.persistencia.PersistenciaException;
import uy.com.uma.logicgame.fe.web.ILogicGameWebConstants;

/**
 * Realiza el logout en el sistema
 *
 * @author Santiago Dalchiele
 */
public class LogoutAction extends SeguridadAbstractAction implements ILogicGameWebConstants {

	public LogoutAction() throws LogicGameException {
	}
	
	@Override
	public String getName() {
		return ID_REQ_LOGOUT;
	}
	
	
	
	/**
	 * Realiza el logout en la base de datos
	 * Remueve de session los atributos
	 * Invalida la session
	 */
	@Override
	public void perform(HttpServletRequest req, PrintWriter out) throws ServletException, IOException {
		HttpSession session = req.getSession(); 
		String idUsuario = session.getAttribute(ID_PARAM_ID_USUARIO).toString();
		
		try {
			manSeg.logout(idUsuario);
			session.removeAttribute(ID_PARAM_ID_USUARIO);
			session.removeAttribute(ID_OBJ_MATRIZ_JUEGO);
			session.removeAttribute(ID_ATT_THREAD_BACKGROUND);
			req.getSession(false).invalidate();
			out.write(RESPUESTA_JSON_DEFECTO_OK);
		} catch (PersistenciaException e) {
			throw new ServletException("Error al intentar realizar el logout", e);
		}
	}
}
