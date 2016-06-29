package uy.com.uma.logicgame.fe.web.servlets;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import uy.com.uma.logicgame.api.conf.ConfiguracionException;
import uy.com.uma.logicgame.api.persistencia.IManejadorSeguridad;
import uy.com.uma.logicgame.api.persistencia.PersistenciaException;
import uy.com.uma.logicgame.api.persistencia.PersistenciaFactory;
import uy.com.uma.logicgame.fe.web.ILogicGameWebConstants;
import uy.com.uma.logicgame.fe.web.actions.JuegoAbstractAction;

/**
 * Controla cuando se pierde la session por timeout o por lo que sea, destruir la información del usuario y deslogearlo del sistema
 *
 * @author Santiago Dalchiele
 */
@WebListener
public class JuegoSessionListener implements HttpSessionListener, ILogicGameWebConstants {
	
	private static final Logger log = LogManager.getLogger(JuegoSessionListener.class.getName());

	
	
	@Override
	public void sessionCreated(HttpSessionEvent e) {
	}

	
	
	/**
	 * Detiene el hilo con tareas en background.
	 * Deslogea el usuario
	 */
	@Override
	public void sessionDestroyed(HttpSessionEvent e) {
		HttpSession session = e.getSession();		
		
		if (session.getAttribute(JuegoAbstractAction.ID_PARAM_ID_USUARIO) != null) {
			String idUsuario = session.getAttribute(JuegoAbstractAction.ID_PARAM_ID_USUARIO).toString();
			
			try {				
				IManejadorSeguridad manSeg = PersistenciaFactory.getInstancia().getManejadorSeguridad();
				manSeg.logout(idUsuario);
			} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | ConfiguracionException | PersistenciaException e1) {
				log.warn("No se pudo deslogear el usuario [" + idUsuario + "] al destruir la session", e1);
			}
		}
		
		if (session.getAttribute(ID_ATT_THREAD_BACKGROUND) != null) {
			Thread hiloBackground = (Thread) session.getAttribute(ID_ATT_THREAD_BACKGROUND);
			hiloBackground.interrupt();
		}
	}
}
