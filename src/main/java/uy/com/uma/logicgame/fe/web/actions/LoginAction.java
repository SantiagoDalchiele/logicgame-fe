package uy.com.uma.logicgame.fe.web.actions;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import uy.com.uma.comun.util.UtilJSON;
import uy.com.uma.comun.util.UtilWeb;
import uy.com.uma.logicgame.api.LogicGameException;
import uy.com.uma.logicgame.api.bean.LogAccionDO;
import uy.com.uma.logicgame.api.persistencia.IManejadorLogAcciones;
import uy.com.uma.logicgame.api.persistencia.IManejadorSeguridad;
import uy.com.uma.logicgame.api.persistencia.PersistenciaException;
import uy.com.uma.logicgame.api.validacion.UtilValidacionParametros;
import uy.com.uma.logicgame.persistencia.seguridad.UtilSeguridad;

/**
 * Realiza el login en el sistema
 *
 * @author Santiago Dalchiele
 */
public class LoginAction extends ControlAbstractAction {

	public LoginAction() throws LogicGameException {
	}

	@Override
	public String getName() {
		return ID_REQ_LOGIN;
	}
	
	@Override
	protected boolean usaIdUsuario() {
		return false;
	}

	@Override
	public void perform(HttpServletRequest req, PrintWriter out) throws ServletException, IOException {
		if (validarParametros(req, out) && controlAccesos(req, out)) {
			String idUsuario = req.getParameter(ID_PARAM_ID_USUARIO);
			final String clave = req.getParameter(ID_PARAM_CLAVE);
			final LogAccionDO la = new LogAccionDO(req, IManejadorLogAcciones.LOGIN);			
			la.setClave(UtilSeguridad.getClaveEncriptada(idUsuario, clave));			
			
			try {
				final short loginResult = manSeg.login(idUsuario, clave);				
				
				if (loginResult == IManejadorSeguridad.LOGIN_EXITOSO) {
					req.getSession(false).invalidate();
					req.getSession(true).setAttribute(JuegoAbstractAction.ID_PARAM_ID_USUARIO, manSeg.getIdUsuario(idUsuario));
				}
				
				la.setUsuario(manSeg.getIdUsuario(idUsuario));
				la.setResultado(loginResult);
				manLog.persistirAccion(la);				
				out.write(UtilJSON.getJSONObject(ID_PARAM_RESULTADO, "" + loginResult).toString());
			} catch (PersistenciaException e) {
				throw new ServletException("Error en el login de usuario", e);
			}
		}
	}		

	
	
	/**
	 * Valida los parametros id usuario (puede ser un correo) y clave
	 */
	@Override
	protected boolean validarParametros(HttpServletRequest req, PrintWriter out) {
		final String idUsuario = req.getParameter(ID_PARAM_ID_USUARIO);
		final String clave = req.getParameter(ID_PARAM_CLAVE);
		
		if (! UtilValidacionParametros.esValidaClave(clave))
			return super.validarParametros(req, out);
		else {
			if (!UtilValidacionParametros.esValidoIdUsuario(idUsuario)) {
				if (UtilValidacionParametros.esValidoCorreo(idUsuario))
					return true;
				else
					return super.validarParametros(req, out);
			} else
				return true;
		}
	}

	
	
	/**
	 * Obtiene la cantidad de logins realizados desde esta ip, y por este usuario y los compara contra los máximos seteados en
	 * la configuración, a los efectos de no permitir el abuso del servicio (temas de seguridad)
	 */
	@Override
	protected boolean controlAccesos(HttpServletRequest req, PrintWriter out) {	
		try {
			final String idUsuario = manSeg.getIdUsuario(req.getParameter(ID_PARAM_ID_USUARIO));
			final String ip = UtilWeb.getClientIpAddr(req);
			final int cantIp = manLog.getCantAccionesUltExito(IManejadorLogAcciones.LOGIN, ip, null, false);
			final int cantIpDia = manLog.getCantAccionesUltExito(IManejadorLogAcciones.LOGIN, ip, null, true);
			final int cantUsuario = manLog.getCantAccionesUltExito(IManejadorLogAcciones.LOGIN, null, idUsuario, false);
			final int cantUsuarioDia = manLog.getCantAccionesUltExito(IManejadorLogAcciones.LOGIN, null, idUsuario, true);
				
			if ((cantIp <= configuracion.getMaxLoginxIp()) && (cantIpDia <= configuracion.getMaxLoginxIpxDia()) &&
					(cantUsuario <= configuracion.getMaxLoginxUsuario()) && (cantUsuarioDia <= configuracion.getMaxLoginxUsuarioxDia()))
				return true;
			else
				return super.controlAccesos(req, out);
		} catch (PersistenciaException e) {
			e.printStackTrace();
			return true;
		}
	}	
}
