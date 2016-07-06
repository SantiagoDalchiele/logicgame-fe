package uy.com.uma.logicgame.fe.web.adm.actions;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import uy.com.uma.comun.util.UtilJSON;
import uy.com.uma.logicgame.api.bean.DatosIdioma;
import uy.com.uma.logicgame.api.conf.ConfiguracionException;
import uy.com.uma.logicgame.api.persistencia.IManejadorAdminInternacionalizacion;
import uy.com.uma.logicgame.api.persistencia.PersistenciaException;
import uy.com.uma.logicgame.api.persistencia.PersistenciaFactory;
import uy.com.uma.logicgame.fe.web.ILogicGameWebConstants;

/**
 * Implementa la acción de obtener los idiomas persistidos en la base de datos
 *
 * @author Santiago Dalchiele
 */
public class GetIdiomasAction extends AdmAbstractAction implements ILogicGameWebConstants {

	/** TAGs para retornar el objeto JSON */
	private static final String TAG_IDIOMAS = "idiomas";
	private static final String TAG_ID_IDIOMA = "id";
	private static final String TAG_NOMBRE_IDIOMA = "nombre";
	private static final String TAG_ICONO_IDIOMA = "icono";
	
	
	
	@Override
	public String getName() {
		return ID_REQ_GET_IDIOMAS;
	}

	
	
	/**
	 * Retorna la colección de idiomas de la siguiente forma:
	 * {"idiomas":[{"id":<id>,"nombre":<nombre>,"icono":<icono>},....]
	 * } 
	 */
	@Override
	public void perform(HttpServletRequest req, PrintWriter out) throws ServletException, IOException {
		try {
			if (checkLogin(req, out)) {
				IManejadorAdminInternacionalizacion mai = PersistenciaFactory.getInstancia().getManejadorAdminInternacionalizacion();
				StringBuffer bufIdiomas = new StringBuffer();
				
				for (DatosIdioma di : mai.getIdiomas()) {
					bufIdiomas.append("{" + UtilJSON.getPropJSON(TAG_ID_IDIOMA) + UtilJSON.getValorJSON(di.getId()));
					bufIdiomas.append(UtilJSON.getPropJSON(TAG_NOMBRE_IDIOMA) + UtilJSON.getValorJSON(di.getNombre()));
					bufIdiomas.append(UtilJSON.getPropJSON(TAG_ICONO_IDIOMA) + UtilJSON.getComillasJSON(di.getIcono()) + "},");
				}
				
				if (bufIdiomas.length() > 0)
					bufIdiomas.deleteCharAt(bufIdiomas.length()-1);
				
				out.write("{" + UtilJSON.getPropJSON(TAG_IDIOMAS) + "[" + bufIdiomas + "]}");
			}
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | ConfiguracionException | PersistenciaException e) {
			throw new ServletException(e);
		}
	}
}
