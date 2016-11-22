package uy.com.uma.logicgame.fe.web.actions;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import uy.com.uma.comun.util.UtilJSON;
import uy.com.uma.comun.util.UtilWeb;
import uy.com.uma.logicgame.api.LogicGameException;
import uy.com.uma.logicgame.api.bean.LogAccionDO;
import uy.com.uma.logicgame.api.persistencia.IManejadorLogAcciones;
import uy.com.uma.logicgame.api.persistencia.PersistenciaException;
import uy.com.uma.logicgame.api.validacion.UtilValidacionParametros;
import uy.com.uma.logicgame.persistencia.seguridad.UtilSeguridad;

/**
 * Registra el usuario en el sistema
 *
 * @author Santiago Dalchiele
 */
public class RegistroAction extends ControlAbstractAction {

	public RegistroAction() throws LogicGameException {
	}

	@Override
	public String getName() {
		return ID_REQ_REGISTRO;
	}
	
	@Override
	protected boolean usaIdUsuario() {
		return false;
	}

	@Override
	public void perform(HttpServletRequest req, PrintWriter out) throws ServletException, IOException {
		if (validarParametros(req, out) && controlAccesos(req, out)) {
			final String idioma = req.getParameter(ID_PARAM_IDIOMA);
			final String idUsuario = req.getParameter(ID_PARAM_ID_USUARIO);
			final String correo = req.getParameter(ID_PARAM_CORREO);
			final String clave = req.getParameter(ID_PARAM_CLAVE);
			final LogAccionDO la = new LogAccionDO(req, IManejadorLogAcciones.REGISTRO);
			la.setUsuario(idUsuario);
			la.setClave(UtilSeguridad.getClaveEncriptada(idUsuario, clave));
	
			try {
				final short result = manSeg.registro(idioma, idUsuario, correo, clave);
				la.setResultado(result);
				manLog.persistirAccion(la);
				Map<String, Object> props = new LinkedHashMap<String, Object>();
				props.put(ID_PARAM_RESULTADO, result);
				props.put(ID_PARAM_ID_USUARIO, idUsuario);
				out.write(UtilJSON.getJSONObject(props).toString());
			} catch (PersistenciaException e) {
				throw new ServletException("Error en el registro del usuario", e);
			}
		}
	}

	
	
	/**
	 * Valida los parametros idioma, id usuario, correo y clave
	 */
	protected boolean validarParametros(HttpServletRequest req, PrintWriter out) {
		final String idioma = req.getParameter(ID_PARAM_IDIOMA);
		final String idUsuario = req.getParameter(ID_PARAM_ID_USUARIO);
		final String correo = req.getParameter(ID_PARAM_CORREO);
		final String clave = req.getParameter(ID_PARAM_CLAVE);
		
		if (UtilValidacionParametros.esValidoIdIdioma(idioma) &&
				UtilValidacionParametros.esValidoIdUsuario(idUsuario) &&
				UtilValidacionParametros.esValidoCorreo(correo) &&
				UtilValidacionParametros.esValidaClave(clave))
			return true;
		else
			return super.validarParametros(req, out);
	}
	
	
	
	/**
	 * Obtiene la cantidad de registros realizados desde esta ip y los compara contra los máximos seteados en
	 * la configuración, a los efectos de no permitir el abuso del servicio (temas de seguridad)
	 */
	@Override
	protected boolean controlAccesos(HttpServletRequest req, PrintWriter out) {	
		try {
			final String ip = UtilWeb.getClientIpAddr(req);
			final int cantIp = manLog.getCantAcciones(IManejadorLogAcciones.REGISTRO, ip, false);
			final int cantIpDia = manLog.getCantAcciones(IManejadorLogAcciones.REGISTRO, ip, true);
				
			if ((cantIp <= configuracion.getMaxRegistroxIp()) && (cantIpDia <= configuracion.getMaxRegistroxIpxDia()))
				return true;
			else
				return super.controlAccesos(req, out);
		} catch (PersistenciaException e) {
			e.printStackTrace();
			return true;
		}
	}	
}
