package uy.com.uma.logicgame.fe.web.actions;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import uy.com.uma.logicgame.api.LogicGameException;
import uy.com.uma.logicgame.api.persistencia.PersistenciaException;
import uy.com.uma.logicgame.fe.web.BackgroundThread;
import uy.com.uma.logicgame.fe.web.model.MatrizJuego;
import uy.com.uma.logicgame.fe.web.servlets.FachadaJuegoServlet;

/**
 * Acción que reinicializa el juego en el servidor, borra todos los valores de la matriz
 *
 * @author Santiago Dalchiele
 */
public class ReiniciarJuegoAction extends SeguridadAbstractAction {
	
	private static final Logger log = LogManager.getLogger(ReiniciarJuegoAction.class.getName());

	
	public ReiniciarJuegoAction() throws LogicGameException {
		super();
	}

	@Override
	public String getName() {
		return ID_REQ_REINICIAR_JUEGO;
	}	
	
	@Override
	protected boolean usaMatriz() {
		return true;
	}

	
	/**
	 * Vacia la matriz y setea el estado en vacío
	 */
	@Override
	public void perform(HttpServletRequest req, PrintWriter out) throws ServletException, IOException {
		BackgroundThread hilo = (BackgroundThread) req.getSession().getAttribute(FachadaJuegoServlet.ID_ATT_THREAD_BACKGROUND);
		hilo.setMatriz(null);
		MatrizJuego matriz = (MatrizJuego) req.getSession().getAttribute(ID_OBJ_MATRIZ_JUEGO);
		String idUsuario = req.getSession().getAttribute(ID_PARAM_ID_USUARIO).toString();
		matriz.vaciar();
		
		try {
			manSeg.setEstado(idUsuario, "");
		} catch (PersistenciaException e) {
			log.warn("Error al setear el estado", e);
		}
		
		hilo.setMatriz(matriz);
		out.write(RESPUESTA_JSON_DEFECTO_OK);
	}
}
