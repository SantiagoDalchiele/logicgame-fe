package uy.com.uma.logicgame.fe.web.actions;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import uy.com.uma.comun.util.UtilJSON;
import uy.com.uma.logicgame.api.LogicGameException;
import uy.com.uma.logicgame.api.bean.LogAccionDO;
import uy.com.uma.logicgame.api.persistencia.IManejadorJuegoWeb;
import uy.com.uma.logicgame.api.persistencia.IManejadorLogAcciones;
import uy.com.uma.logicgame.api.persistencia.PersistenciaException;
import uy.com.uma.logicgame.fe.web.BackgroundThread;
import uy.com.uma.logicgame.fe.web.model.MatrizJuego;
import uy.com.uma.logicgame.fe.web.servlets.FachadaJuegoServlet;

/**
 * Retorna el resultado del juego analizando la matriz de juego
 *
 * @author Santiago Dalchiele
 */
public class FinJuegoAction extends ControlAbstractAction {
	
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
		if (controlAccesos(req, out)) {
			MatrizJuego matriz = (MatrizJuego) req.getSession().getAttribute(ID_OBJ_MATRIZ_JUEGO);
			final String idUsuario = req.getSession().getAttribute(ID_PARAM_ID_USUARIO).toString();
			short resultado = IManejadorJuegoWeb.JUEGO_ERRONEO;
			final LogAccionDO la = new LogAccionDO(req, IManejadorLogAcciones.FIN_JUEGO);
			la.setUsuario(idUsuario);
				
			try {
				if (!matriz.estaCompleto())
					resultado = IManejadorJuegoWeb.JUEGO_INCOMPLETO;
				else if (matriz.estaCorrecto()) {				
					BackgroundThread hilo = (BackgroundThread) req.getSession().getAttribute(FachadaJuegoServlet.ID_ATT_THREAD_BACKGROUND);
					hilo.setMatriz(null);									
					manSeg.incNivel(idUsuario);
					req.getSession().removeAttribute(ID_OBJ_MATRIZ_JUEGO);
					manSeg.setEstado(idUsuario, "");
					resultado = IManejadorJuegoWeb.JUEGO_EXITOSO;				
				}
				
				if (resultado != IManejadorJuegoWeb.JUEGO_INCOMPLETO) {
					la.setResultado(resultado);
					manLog.persistirAccion(la);
				}
			} catch (PersistenciaException e) {
				throw new ServletException("Error al incrementar nivel", e);
			}
			
			out.write(UtilJSON.getJSONObject(TAG_RESULTADO, "" + resultado).toString());
		}
	}
	
	
	
	/**
	 * Obtiene la cantidad de intentos de fin_juego realizados por este usuario y lo compara contra el máximo del juego
	 * a los efectos de no permitir el abuso del servicio (temas de seguridad)
	 */
	@Override
	protected boolean controlAccesos(HttpServletRequest req, PrintWriter out) {	
		try {
			final MatrizJuego matriz = (MatrizJuego) req.getSession().getAttribute(ID_OBJ_MATRIZ_JUEGO);
			final String idUsuario = req.getSession().getAttribute(ID_PARAM_ID_USUARIO).toString();
			final int cantIntentos = manLog.getCantAccionesUltExito(IManejadorLogAcciones.FIN_JUEGO, null, idUsuario, false);
				
			if (cantIntentos <= matriz.getMaxIntentos())
				return true;
			else
				return super.controlAccesos(req, out);
		} catch (PersistenciaException e) {
			e.printStackTrace();
			return true;
		}
	}
}
