package uy.com.uma.logicgame.fe.web.adm.actions;

import uy.com.uma.logicgame.api.persistencia.IManejadorEstructura;
import uy.com.uma.logicgame.api.persistencia.PersistenciaException;

/**
 * Lanza el crear la ruta por defecto que se les asigna a los usuarios nuevos cuando se crean
 *
 * @author Santiago Dalchiele
 */
public class CrearRutaXDefectoAction extends AdmEstructuraAbstractAction {

	@Override
	public String getName() {
		return ID_REQ_CREAR_RUTA_X_DEFECTO;
	}

	@Override
	protected String doAction(IManejadorEstructura manEstructura) throws PersistenciaException {
		manEstructura.crearRutaXDefecto();
		return "Se creo la ruta por defecto en forma exitosa";		
	}

}
