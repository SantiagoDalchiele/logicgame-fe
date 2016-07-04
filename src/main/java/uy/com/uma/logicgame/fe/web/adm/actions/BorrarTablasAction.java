package uy.com.uma.logicgame.fe.web.adm.actions;

import uy.com.uma.logicgame.api.persistencia.IManejadorEstructura;
import uy.com.uma.logicgame.api.persistencia.PersistenciaException;

/**
 * Ejecuta la acción de borrado de tablas en la base de datos
 *
 * @author Santiago Dalchiele
 */
public class BorrarTablasAction extends AdmEstructuraAbstractAction {

	@Override
	public String getName() {
		return ID_REQ_BORRAR_TABLAS;
	}

	@Override
	protected String doAction(IManejadorEstructura manEstructura) throws PersistenciaException {
		manEstructura.borrarTablas();
		return "Tablas borradas con éxito";
	}
}