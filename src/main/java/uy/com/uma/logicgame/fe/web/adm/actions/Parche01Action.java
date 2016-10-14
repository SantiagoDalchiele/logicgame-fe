package uy.com.uma.logicgame.fe.web.adm.actions;

import uy.com.uma.logicgame.api.persistencia.IManejadorEstructura;
import uy.com.uma.logicgame.api.persistencia.PersistenciaException;

/**
 * Invoca la logica del parche 01: crear los atributos token y fch_expira_token en la tabla de usuarios
 *
 * @author Santiago Dalchiele
 */
public class Parche01Action extends AdmEstructuraAbstractAction {

	@Override
	public String getName() {
		return ID_REQ_PARCHE_TOKEN;
	}

	@Override
	protected String doAction(IManejadorEstructura manEstructura) throws PersistenciaException {
		manEstructura.parche01TokenUsuarios();
		return "Tabla de usuarios modificada con exito";
	}
}