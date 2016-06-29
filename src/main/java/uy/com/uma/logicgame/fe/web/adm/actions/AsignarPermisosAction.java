package uy.com.uma.logicgame.fe.web.adm.actions;

import uy.com.uma.logicgame.api.persistencia.IManejadorEstructura;
import uy.com.uma.logicgame.api.persistencia.PersistenciaException;

/**
 * Lanza la solicitud de asignación de permisos para los roles de la base de datos
 *
 * @author Santiago Dalchiele
 */
public class AsignarPermisosAction extends AdmEstructuraAbstractAction {

	@Override
	public String getName() {
		return ID_REQ_ASIGNAR_PERMISOS;
	}

	@Override
	protected String doAction(IManejadorEstructura manEstructura) throws PersistenciaException {
		manEstructura.asignarPermisos();
		return "Se asignaron los permisos con exito";
	}
}
