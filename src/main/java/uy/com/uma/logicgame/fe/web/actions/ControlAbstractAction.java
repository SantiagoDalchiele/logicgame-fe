package uy.com.uma.logicgame.fe.web.actions;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;

import uy.com.uma.logicgame.api.LogicGameException;
import uy.com.uma.logicgame.api.conf.ConfiguracionException;
import uy.com.uma.logicgame.api.persistencia.IManejadorLogAcciones;
import uy.com.uma.logicgame.api.persistencia.PersistenciaFactory;

/**
 * Contiene el manejador de log de acciones, para persistir y controlar las acciones que están limitadas (en cantidad de llamadas) en el sistema.
 *
 * @author Santiago Dalchiele
 */
abstract class ControlAbstractAction extends SeguridadAbstractAction {
	
	/** Define número y mensaje de error para control de número de accesos */
	protected final static int NRO_ERROR_CONTROL_NRO_ACCESOS = 4774;
	protected final static String MSG_ERROR_CONTROL_NRO_ACCESOS = "Maximo de intentos superados";
	
	
	/** Fachada de servicios de persistencia del log de acciones */
	protected IManejadorLogAcciones manLog;

	
	
	/**
	 * Constructor, intenta obtener el manejador del log de acciones
	 */
	protected ControlAbstractAction() throws LogicGameException {
		try {
			manLog = PersistenciaFactory.getInstancia().getManejadorLogAcciones();
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | ConfiguracionException e) {
			throw new LogicGameException("Error al obtener manejador log de acciones", e);
		}
	}

	
	
	/**
	 * Validacion por defecto de control de accesos a servicios, escribe en el PrintWriter el error y retorna false
	 */
	protected boolean controlAccesos (HttpServletRequest req, PrintWriter out) {
       	out.write(getErrorJSON(1, NRO_ERROR_CONTROL_NRO_ACCESOS, MSG_ERROR_CONTROL_NRO_ACCESOS, ""));
       	return false;
	}
}
