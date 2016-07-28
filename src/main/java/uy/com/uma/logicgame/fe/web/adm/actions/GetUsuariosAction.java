package uy.com.uma.logicgame.fe.web.adm.actions;

import java.util.ArrayList;
import java.util.Collection;

import uy.com.uma.logicgame.api.bean.IJSONObject;
import uy.com.uma.logicgame.api.conf.ConfiguracionException;
import uy.com.uma.logicgame.api.persistencia.IManejadorSeguridad;
import uy.com.uma.logicgame.api.persistencia.PersistenciaException;
import uy.com.uma.logicgame.api.persistencia.PersistenciaFactory;

/**
 * Implementa la acción de retornar todos los usuarios del sistema
 *
 * @author Santiago Dalchiele
 */
public class GetUsuariosAction extends GetDatosAbstractAction {

	private static final String TAG_USUARIOS = "usuarios";
	
	
	@Override
	public String getName() {
		return ID_REQ_GET_USUARIOS;
	}

	
	@Override
	protected String getTagColeccion() {
		return TAG_USUARIOS;
	}

	@Override
	protected Collection<IJSONObject> getDatos() throws PersistenciaException, InstantiationException,
			IllegalAccessException, ClassNotFoundException, ConfiguracionException {
		IManejadorSeguridad ms = PersistenciaFactory.getInstancia().getManejadorSeguridad();
		Collection<IJSONObject> ret = new ArrayList<IJSONObject>();
		ret.addAll(ms.getUsuarios());
		return ret;
	}
}
