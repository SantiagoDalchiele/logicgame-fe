package uy.com.uma.logicgame.fe.web.adm.actions;

import java.util.ArrayList;
import java.util.Collection;

import uy.com.uma.logicgame.api.bean.IJSONObject;
import uy.com.uma.logicgame.api.conf.ConfiguracionException;
import uy.com.uma.logicgame.api.persistencia.IManejadorSeguridad;
import uy.com.uma.logicgame.api.persistencia.PersistenciaException;
import uy.com.uma.logicgame.api.persistencia.PersistenciaFactory;

/**
 * Implementa la acción de obtener las rutas persistidas en la base de datos
 * 
 * @author Santiago Dalchiele
 */
public class GetRutasAction extends GetDatosAbstractAction {

	/** TAGs para retornar el objeto JSON */
	private static final String TAG_RUTAS = "rutas";
	
	
	@Override
	public String getName() {
		return ID_REQ_GET_RUTAS;
	}
	
	@Override
	protected String getTagColeccion() {		
		return TAG_RUTAS;
	}


	@Override
	protected Collection<IJSONObject> getDatos() throws PersistenciaException, InstantiationException, IllegalAccessException, ClassNotFoundException, ConfiguracionException {
		IManejadorSeguridad ms = PersistenciaFactory.getInstancia().getManejadorSeguridad();
		Collection<IJSONObject> ret = new ArrayList<IJSONObject>();
		ret.addAll(ms.getRutas());
		return ret;
	}
}
