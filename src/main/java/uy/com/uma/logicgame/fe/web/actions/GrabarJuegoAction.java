package uy.com.uma.logicgame.fe.web.actions;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import uy.com.uma.logicgame.api.LogicGameException;
import uy.com.uma.logicgame.api.persistencia.PersistenciaException;
import uy.com.uma.logicgame.fe.web.model.MatrizJuego;

/**
 * Acción que graba el juego en el servidor
 *
 * @author Santiago Dalchiele
 */
public class GrabarJuegoAction extends SeguridadAbstractAction {

	
	public GrabarJuegoAction() throws LogicGameException {
		super();
	}
	
	@Override
	public String getName() {
		return ID_REQ_GRABAR_JUEGO;
	}
	
	@Override
	protected boolean usaMatriz() {
		return true;
	}

	@Override
	public void perform(HttpServletRequest req, PrintWriter out) throws ServletException, IOException {
		MatrizJuego matriz = (MatrizJuego) req.getSession().getAttribute(ID_OBJ_MATRIZ_JUEGO);
		String idUsuario = req.getSession().getAttribute(ID_PARAM_ID_USUARIO).toString();		
		
		if (matriz != null) {
			try {
				manSeg.setEstado(idUsuario, matriz.getEstado());
				out.write(RESPUESTA_JSON_DEFECTO_OK);
			} catch (PersistenciaException e) {
				throw new ServletException("Error al setear el estado", e);
			}
		} else
			out.write(getErrorJSON(TIPO_ERROR_NO_LOGEADO, 666, "Usuario no logeado", ""));
	}
}
