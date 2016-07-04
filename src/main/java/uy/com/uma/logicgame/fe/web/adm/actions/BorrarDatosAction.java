package uy.com.uma.logicgame.fe.web.adm.actions;

import uy.com.uma.logicgame.api.persistencia.IManejadorEstructura;
import uy.com.uma.logicgame.api.persistencia.PersistenciaException;

/**
 * Lanza el borrado de datos de todas las tablas del sistema
 *
 * @author Santiago Dalchiele
 */
public class BorrarDatosAction extends AdmEstructuraAbstractAction {

	@Override
	public String getName() {
		return ID_REQ_BORRAR_DATOS;
	}
	
	@Override
	protected String doAction(IManejadorEstructura manEstructura) throws PersistenciaException {
		manEstructura.borrarDatos();
		return "Datos borrados con exito";
	}
}
