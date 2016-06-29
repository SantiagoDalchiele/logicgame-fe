package uy.com.uma.logicgame.fe.web.actions;

import uy.com.uma.logicgame.api.LogicGameException;
import uy.com.uma.logicgame.api.conf.ConfiguracionException;
import uy.com.uma.logicgame.api.persistencia.IManejadorSeguridad;
import uy.com.uma.logicgame.api.persistencia.PersistenciaFactory;

/**
 * Contiene un manejador de persistencia de la seguridad 
 *
 * @author Santiago Dalchiele
 */
abstract class SeguridadAbstractAction extends JuegoAbstractAction {

	/** Cadena de caracteres utilizada como respuesta ok por defecto en una solicitud JSON */
	protected static final String RESPUESTA_JSON_DEFECTO_OK = "{\"ok\":true}";
	
	
	/** Fachada de servicios de persistencia */
	protected IManejadorSeguridad manSeg;
	
	
	
	
	protected SeguridadAbstractAction() throws LogicGameException {
		try {
			manSeg = PersistenciaFactory.getInstancia().getManejadorSeguridad();
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | ConfiguracionException e) {
			throw new LogicGameException("Error al obtener conexion a la base de datos", e);
		}
	}
	
	
	

	/**
	 * Retorna por defecto TRUE
	 */
	@Override
	protected boolean usaIdUsuario() {
		return true;
	}



	/**
	 * Retorna por defecto FALSE
	 */
	@Override
	protected boolean usaMatriz() {
		return false;
	}



	@Override
	public void reset() throws LogicGameException {
		try {
			manSeg = PersistenciaFactory.getInstancia().getManejadorSeguridad();
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | ConfiguracionException e) {
			throw new LogicGameException("Error al resetear conexion a la base de datos", e);
		}
	}
}
