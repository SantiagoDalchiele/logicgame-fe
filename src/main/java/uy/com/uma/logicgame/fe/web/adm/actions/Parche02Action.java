package uy.com.uma.logicgame.fe.web.adm.actions;

import uy.com.uma.logicgame.api.persistencia.IManejadorEstructura;
import uy.com.uma.logicgame.api.persistencia.PersistenciaException;

/**
 * Crea la tabla de log de acciones
 *
 * @author Santiago Dalchiele
 */
public class Parche02Action extends AdmEstructuraAbstractAction {

	@Override
	public String getName() {
		return ID_REQ_PARCHE_LOG_ACCIONES;
	}
	
	@Override
	protected String doAction(IManejadorEstructura manEstructura) throws PersistenciaException {
		manEstructura.parche02LogAcciones();
		return "Tabla log_acciones creada con exito";
	}
}