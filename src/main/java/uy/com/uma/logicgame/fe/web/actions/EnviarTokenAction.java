package uy.com.uma.logicgame.fe.web.actions;

import java.io.IOException;
import java.io.PrintWriter;

import javax.mail.MessagingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import uy.com.uma.comun.mail.MailParams;
import uy.com.uma.comun.mail.UtilMail;
import uy.com.uma.comun.util.UtilJSON;
import uy.com.uma.logicgame.api.LogicGameException;
import uy.com.uma.logicgame.api.bean.UsuarioDO;
import uy.com.uma.logicgame.api.persistencia.IManejadorSeguridad;
import uy.com.uma.logicgame.api.persistencia.PersistenciaException;
import uy.com.uma.logicgame.api.validacion.UtilValidacionParametros;

/**
 * Accion que atiende la solicitud del envio de un token por mail al usuario que perdió su contraseña
 *
 * @author Santiago Dalchiele
 */
public class EnviarTokenAction extends SeguridadAbstractAction {

	
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
		if (validarParametros(req, out)) {
			String idUsuario = req.getParameter(ID_PARAM_ID_USUARIO);
			
			try {
				short result = 0;
				
				if (!manSeg.existeUsuario(idUsuario))
					result = IManejadorSeguridad.LOGIN_USUARIO_INEXISTENTE;
				else {
					idUsuario = manSeg.getIdUsuario(idUsuario);
					String token = manSeg.generarToken(idUsuario);
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
}
