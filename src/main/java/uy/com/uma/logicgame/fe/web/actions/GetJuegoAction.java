package uy.com.uma.logicgame.fe.web.actions;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import uy.com.uma.comun.util.UtilFormato;
import uy.com.uma.comun.util.UtilJSON;
import uy.com.uma.logicgame.api.LogicGameException;
import uy.com.uma.logicgame.api.bean.UsuarioDO;
import uy.com.uma.logicgame.api.conf.ConfiguracionException;
import uy.com.uma.logicgame.api.persistencia.IManejadorJuegoWeb;
import uy.com.uma.logicgame.api.persistencia.PersistenciaException;
import uy.com.uma.logicgame.api.persistencia.PersistenciaFactory;
import uy.com.uma.logicgame.fe.web.BackgroundThread;
import uy.com.uma.logicgame.fe.web.ILogicGameWebConstants;
import uy.com.uma.logicgame.fe.web.css.ManejadorCss;
import uy.com.uma.logicgame.fe.web.model.MatrizJuego;

/**
 * Obtiene la definición del juego de la base de datos
 *
 * @author Santiago Dalchiele
 */
public class GetJuegoAction extends SeguridadAbstractAction implements ILogicGameWebConstants {
	
	private static final Logger log = LogManager.getLogger(GetJuegoAction.class.getName());
	
	/** Constantes con las propiedades del objeto a retornar */
	private static final String TAG_NIVEL = "nivel";

	/** Fachada de servicios de persistencia */
	protected IManejadorJuegoWeb mjw;
	
	

	/**
	 * Constructor
	 */
	public GetJuegoAction() throws LogicGameException {
		super();
		
		try {
			mjw = PersistenciaFactory.getInstancia().getManejadorJuegoWeb();
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | ConfiguracionException e) {
			throw new LogicGameException("Error al obtener conexion a la base de datos", e);		
		}
	}



	/** 
	 * Retorna el nombre de la acción 
	 */
	@Override
	public String getName() {
		return ID_REQ_GET_JUEGO;
	}

	
	
	/** 
	 * Resetea la instancia 
	 */
	public void reset() throws LogicGameException {
		try {
			mjw = PersistenciaFactory.getInstancia().getManejadorJuegoWeb();
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | ConfiguracionException e) {
			throw new LogicGameException("Error al resetear conexion a la base de datos", e);
		}
		
		super.reset();
	}

	
	
	/**
	 * Obtiene la definición del juego de la base de datos
	 * Retorna:
	 * {"nivel":<nro_nivel>,
	 *  "estado":[<arreglo (id,valor) con el ultimo estado del juego grabado>],
	 *  <definicion>} 
	 *  @see uy.com.uma.logicgame.persistencia.juego.DefJuegoBuilder
	 */
	@Override
	public void perform(HttpServletRequest request, PrintWriter out) throws ServletException, IOException {
		HttpSession session = request.getSession();
		String idUsuario = session.getAttribute(ID_PARAM_ID_USUARIO).toString();
		MatrizJuego matriz;
		
		try {
			UsuarioDO datos = manSeg.getDatosUsuario(idUsuario);
			String rutaCss = manSeg.getRutaHojaEstilo(idUsuario);
			
			if (! UtilFormato.esNulo(rutaCss))
				rutaCss = appRealPath + rutaCss;				
			
			matriz = new MatrizJuego(mjw.getParametros(datos.getIdJuego()));			
			session.setAttribute(ID_PARAM_ID_JUEGO, datos.getIdJuego());
			session.setAttribute(ID_OBJ_MATRIZ_JUEGO, matriz);			
			initBackgroundThread(session, idUsuario, matriz);
			String def = "{" + UtilJSON.getPropJSON(TAG_NIVEL) + datos.getNivel() + "," + 
						UtilJSON.getPropJSON(ID_PARAM_ESTADO) + "[" + datos.getEstado() + "]," +
						mjw.getDefinicion(datos.getIdJuego(), datos.getIdioma()) + 
						(UtilFormato.esNulo(rutaCss) ? "" : "," + ManejadorCss.getPropsToJSON(rutaCss)) + "}";
			log.debug(def);
			out.write (def);
		} catch (PersistenciaException | LogicGameException e) {
			throw new ServletException("Error al obtener el juego", e);
		}
	}
	
	
	
	/**
	 * Inicializa el hilo de tareas en background
	 */
	private void initBackgroundThread (HttpSession session, String idUsuario, MatrizJuego matriz) throws LogicGameException {
		BackgroundThread hilo;
		
		if (session.getAttribute(ID_ATT_THREAD_BACKGROUND) == null) {
			log.debug("Creando el hilo que se ejecuta en background [" + idUsuario + "]");
			hilo = new BackgroundThread(idUsuario, configuracion.getBackgroundThreadTimeout());
			hilo.start();
			session.setAttribute(ID_ATT_THREAD_BACKGROUND, hilo);
		} else
			hilo = (BackgroundThread) session.getAttribute(ID_ATT_THREAD_BACKGROUND);
		
		hilo.setMatriz(matriz);
	}
}
