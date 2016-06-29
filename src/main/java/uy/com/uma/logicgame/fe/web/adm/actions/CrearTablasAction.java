package uy.com.uma.logicgame.fe.web.adm.actions;

import uy.com.uma.logicgame.api.persistencia.IManejadorEstructura;
import uy.com.uma.logicgame.api.persistencia.PersistenciaException;

/**
 * Invoca la creación de las tablas en la base de datos
 *
 * @author Santiago Dalchiele
 */
public class CrearTablasAction extends AdmEstructuraAbstractAction {

	@Override
	public String getName() {
		return ID_REQ_CREAR_TABLAS;
	}

	@Override
	protected String doAction(IManejadorEstructura manEstructura) throws PersistenciaException {
		manEstructura.crearTablas();
		return "Tablas creadas con exito";
	}
}
