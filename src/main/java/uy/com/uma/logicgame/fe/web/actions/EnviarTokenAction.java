package uy.com.uma.logicgame.fe.web.actions;

import java.io.IOException;
import java.io.PrintWriter;

import javax.mail.MessagingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import uy.com.uma.comun.mail.MailParams;
import uy.com.uma.comun.mail.UtilMail;
import uy.com.uma.comun.util.UtilJSON;
import uy.com.uma.comun.util.UtilWeb;
import uy.com.uma.logicgame.api.LogicGameException;
import uy.com.uma.logicgame.api.bean.LogAccionDO;
import uy.com.uma.logicgame.api.bean.UsuarioDO;
import uy.com.uma.logicgame.api.persistencia.IManejadorLogAcciones;
import uy.com.uma.logicgame.api.persistencia.IManejadorSeguridad;
import uy.com.uma.logicgame.api.persistencia.PersistenciaException;
import uy.com.uma.logicgame.api.validacion.UtilValidacionParametros;

/**
 * Accion que atiende la solicitud del envio de un token por mail al usuario que perdió su contraseña
 *
 * @author Santiago Dalchiele
 */
public class EnviarTokenAction extends ControlAbstractAction {

	
	public EnviarTokenAction() throws LogicGameException {		
	}
	
	@Override
	public String getName() {
		return ID_REQ_ENVIAR_TOKEN;
	}

	@Override
	protected boolean usaIdUsuario() {
		return false;
	}
	
	@Override
	public void perform(HttpServletRequest req, PrintWriter out) throws ServletException, IOException {
		if ((validarParametros(req, out)) && controlAccesos(req, out)) {
			String idUsuario = req.getParameter(ID_PARAM_ID_USUARIO);
			final LogAccionDO la = new LogAccionDO(req, IManejadorLogAcciones.ENVIO_TOKEN);
			la.setUsuario(idUsuario);			
			
			try {
				short result = 0;
				
				if (!manSeg.existeUsuario(idUsuario))
					result = IManejadorSeguridad.LOGIN_USUARIO_INEXISTENTE;
				else {
					idUsuario = manSeg.getIdUsuario(idUsuario);
					final String token = manSeg.generarToken(idUsuario);
					UsuarioDO userData = manSeg.getDatosUsuario(idUsuario);
					String plantilla = configuracion.getPlantillaContenidoCorreo();
					final String reqUrl = req.getRequestURL().toString();
					final String sitio = reqUrl.substring(0, reqUrl.lastIndexOf("/")+1); 
					final String link = sitio + ID_REQ_RECIBIR_TOKEN + "?" + ID_PARAM_ID_USUARIO + "=" + idUsuario + "&" + 
																			ID_PARAM_TOKEN + "=" + token;
					plantilla = plantilla.replaceAll("@@link", link);
					MailParams parms = configuracion.getParametrosCorreo();
					parms.setTo(userData.getCorreo());
					parms.setContenidoHTML(plantilla);
					UtilMail.enviar(parms);
					result = IManejadorSeguridad.LOGIN_EXITOSO;
				}
				
				la.setResultado(result);
				manLog.persistirAccion(la);	
				out.write(UtilJSON.getJSONObject(ID_PARAM_RESULTADO, "" + result).toString());
			} catch (PersistenciaException | MessagingException e) {
				throw new ServletException("Error en el envio de token", e);
			}
		}
	}

	
	
	/**
	 * Valida el parametro id usuario (puede ser un correo)
	 */
	@Override
	protected boolean validarParametros(HttpServletRequest req, PrintWriter out) {
		final String idUsuario = req.getParameter(ID_PARAM_ID_USUARIO);
		
		if (!UtilValidacionParametros.esValidoIdUsuario(idUsuario)) {
			if (UtilValidacionParametros.esValidoCorreo(idUsuario))
				return true;
			else
				return super.validarParametros(req, out);
		} else
			return true;
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
			final int cantIp = manLog.getCantAccionesUltExito(IManejadorLogAcciones.ENVIO_TOKEN, ip, null, false);
			final int cantIpDia = manLog.getCantAccionesUltExito(IManejadorLogAcciones.ENVIO_TOKEN, ip, null, true);
			final int cantUsuario = manLog.getCantAccionesUltExito(IManejadorLogAcciones.ENVIO_TOKEN, null, idUsuario, false);
			final int cantUsuarioDia = manLog.getCantAccionesUltExito(IManejadorLogAcciones.ENVIO_TOKEN, null, idUsuario, true);
				
			if ((cantIp <= configuracion.getMaxEnvioTokenxIp()) && (cantIpDia <= configuracion.getMaxEnvioTokenxIpxDia()) &&
					(cantUsuario <= configuracion.getMaxEnvioTokenxUsuario()) && (cantUsuarioDia <= configuracion.getMaxEnvioTokenxUsuarioxDia()))
				return true;
			else
				return super.controlAccesos(req, out);
		} catch (PersistenciaException e) {
			e.printStackTrace();
			return true;
		}
	}	
}
