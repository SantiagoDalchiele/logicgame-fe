package uy.com.uma.logicgame.fe.web.actions;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import uy.com.uma.comun.util.UtilJSON;
import uy.com.uma.logicgame.api.LogicGameException;
import uy.com.uma.logicgame.api.persistencia.PersistenciaException;

/**
 * Retorna si esta logeado o no el usuario.
 * Debe estar en session el identificador de usuario y la matriz del juego
 * Si no esta la matriz en session realiza el logout 
 *
 * @author Santiago Dalchiele
 */
public class EstaLogeadoAction extends SeguridadAbstractAction {

	public EstaLogeadoAction() throws LogicGameException {
	}
	
	@Override
	public String getName() {
		return ID_REQ_ESTA_LOGEADO;
	}	

	@Override
	protected boolean usaIdUsuario() {
		return false;
	}

	@Override
	public void perform(HttpServletRequest req, PrintWriter out) throws ServletException, IOException {
		boolean logeado = false;
		HttpSession session = req.getSession();
		
		if (!session.isNew()) {
			if (session.getAttribute(ID_PARAM_ID_USUARIO) != null) {
				String idUsuario = session.getAttribute(ID_PARAM_ID_USUARIO).toString();
			
				try {
					if (session.getAttribute(ID_OBJ_MATRIZ_JUEGO) != null)
						logeado = manSeg.estaLogeado(idUsuario);
					else
						manSeg.logout(idUsuario);
				} catch (PersistenciaException e) {
					throw new ServletException("Error en el login de usuario", e);
				}
			}
		}

		out.write(UtilJSON.getJSONObject(ID_PARAM_RESULTADO, logeado ? "1" : "0").toString());
	}

}
