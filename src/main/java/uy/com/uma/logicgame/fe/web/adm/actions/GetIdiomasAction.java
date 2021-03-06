package uy.com.uma.logicgame.fe.web.adm.actions;

import java.util.ArrayList;
import java.util.Collection;

import uy.com.uma.logicgame.api.bean.IJSONObject;
import uy.com.uma.logicgame.api.conf.ConfiguracionException;
import uy.com.uma.logicgame.api.persistencia.IManejadorAdminInternacionalizacion;
import uy.com.uma.logicgame.api.persistencia.PersistenciaException;
import uy.com.uma.logicgame.api.persistencia.PersistenciaFactory;

/**
 * Implementa la acci�n de obtener los idiomas persistidos en la base de datos
 *
 * @author Santiago Dalchiele
 */
public class GetIdiomasAction extends GetDatosAbstractAction {

	/** TAGs para retornar el objeto JSON */
	private static final String TAG_IDIOMAS = "idiomas";
	
	
	
	@Override
	public String getName() {
		return ID_REQ_GET_IDIOMAS;
	}
	
	@Override
	protected String getTagColeccion() {
		return TAG_IDIOMAS;
	}

	@Override
	protected Collection<IJSONObject> getDatos() throws PersistenciaException, InstantiationException, IllegalAccessException, ClassNotFoundException, ConfiguracionException {
		IManejadorAdminInternacionalizacion mai = PersistenciaFactory.getInstancia().getManejadorAdminInternacionalizacion();
		Collection<IJSONObject> ret = new ArrayList<IJSONObject>();
		ret.addAll(mai.getIdiomas());
		return ret;
	}
}
