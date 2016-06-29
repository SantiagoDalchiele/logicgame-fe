package uy.com.uma.logicgame.fe.web.adm.actions;

import uy.com.uma.logicgame.api.persistencia.IManejadorEstructura;
import uy.com.uma.logicgame.api.persistencia.PersistenciaException;

/**
 * Accion para crear los roles de la base de datos
 *
 * @author Santiago Dalchiele
 */
public class CrearRolesAction extends AdmEstructuraAbstractAction {

	@Override
	public String getName() {		
		return ID_REQ_CREAR_ROLES;
	}

	@Override
	protected String doAction(IManejadorEstructura manEstructura) throws PersistenciaException {
		manEstructura.crearRoles();
		return "Roles creados con exito";
	}
}
