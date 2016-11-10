package uy.com.uma.logicgame.fe.web.adm.actions;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import uy.com.uma.comun.util.UtilJSON;
import uy.com.uma.logicgame.api.conf.ConfiguracionException;
import uy.com.uma.logicgame.api.persistencia.IManejadorEstructura;
import uy.com.uma.logicgame.api.persistencia.PersistenciaException;
import uy.com.uma.logicgame.api.persistencia.PersistenciaFactory;
import uy.com.uma.logicgame.fe.web.ILogicGameWebConstants;

/**
 * Acción genérica para los cambios en la estructura de la base de datos utilizando IManejadorEstructura
 * 
 * @see uy.com.uma.logicgame.api.persistencia.IManejadorEstructura *
 * @author Santiago Dalchiele
 */
public abstract class AdmEstructuraAbstractAction extends AdmAbstractAction implements ILogicGameWebConstants {

	
	/** Accion a implementar por cada clase que herede de esta */
	protected abstract String doAction (IManejadorEstructura manEstructura) throws PersistenciaException;
	
	
	
	@Override
	public void perform(HttpServletRequest req, PrintWriter out) throws ServletException, IOException {
		try {
			if (checkLogin(req, out)) {
				IManejadorEstructura manEstructura = PersistenciaFactory.getInstancia().getManejadorEstructura();			
				String result = doAction(manEstructura);
				out.write(UtilJSON.getJSONObject("resultado", result).toString());
			}
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | ConfiguracionException | PersistenciaException e) {
			throw new ServletException(e);
		}
	}
}
