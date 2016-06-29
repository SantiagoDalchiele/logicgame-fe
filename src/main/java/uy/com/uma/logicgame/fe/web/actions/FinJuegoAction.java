package uy.com.uma.logicgame.fe.web.actions;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import uy.com.uma.comun.util.UtilJSON;
import uy.com.uma.logicgame.api.LogicGameException;
import uy.com.uma.logicgame.api.persistencia.PersistenciaException;
import uy.com.uma.logicgame.fe.web.BackgroundThread;
import uy.com.uma.logicgame.fe.web.model.MatrizJuego;
import uy.com.uma.logicgame.fe.web.servlets.FachadaJuegoServlet;

/**
 * Retorna el resultado del juego analizando la matriz de juego
 *
 * @author Santiago Dalchiele
 */
public class FinJuegoAction extends SeguridadAbstractAction {
	
	/** Constantes que representan el resultado de evaluar el juego */
	private static final short JUEGO_INCOMPLETO = 1;
	private static final short JUEGO_ERRONEO = 2;
	private static final short JUEGO_EXITOSO = 3;
	
	/** TAG para retornar el resultado del juego */
	private static final String TAG_RESULTADO = "resultado";
	

	
	public FinJuegoAction() throws LogicGameException {
		super();
	}

	
	
	@Override
	public String getName() {
		return ID_REQ_FIN_JUEGO;
	}
	
	@Override
	protected boolean usaMatriz() {
		return true;
	}



	/**
	 * Retorna el resultado del juego analizando la matriz de juego
	 */
	@Override
	public void perform(HttpServletRequest req, PrintWriter out) throws ServletException, IOException {
		MatrizJuego matriz = (MatrizJuego) req.getSession().getAttribute(ID_OBJ_MATRIZ_JUEGO);
		short resultado = JUEGO_ERRONEO;
				
		if (!matriz.estaCompleto())
			resultado = JUEGO_INCOMPLETO;
		else if (matriz.estaCorrecto()) {
			String idUsuario = req.getSession().getAttribute(ID_PARAM_ID_USUARIO).toString();
			BackgroundThread hilo = (BackgroundThread) req.getSession().getAttribute(FachadaJuegoServlet.ID_ATT_THREAD_BACKGROUND);
			hilo.setMatriz(null);			
			
			try {
				manSeg.incNivel(idUsuario);
				req.getSession().removeAttribute(ID_OBJ_MATRIZ_JUEGO);
				manSeg.setEstado(idUsuario, "");
				resultado = JUEGO_EXITOSO;
			} catch (PersistenciaException e) {
				throw new ServletException("Error al incrementar nivel", e);
			}
		}
		
		out.write("{" + UtilJSON.getPropJSON(TAG_RESULTADO) + resultado + "}");
	}
}
