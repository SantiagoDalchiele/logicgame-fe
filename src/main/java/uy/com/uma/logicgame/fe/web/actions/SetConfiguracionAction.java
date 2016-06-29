package uy.com.uma.logicgame.fe.web.actions;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import uy.com.uma.comun.util.UtilFormato;
import uy.com.uma.logicgame.api.LogicGameException;
import uy.com.uma.logicgame.api.persistencia.PersistenciaException;
import uy.com.uma.logicgame.api.validacion.UtilValidacionParametros;

/**
 * Setea la configuración a nivel de servidor
 *
 * @author Santiago Dalchiele
 */
public class SetConfiguracionAction extends SeguridadAbstractAction {

	
	
	public SetConfiguracionAction() throws LogicGameException {
		super();
	}

	@Override
	public String getName() {
		return ID_REQ_SET_CONFIGURACION;
	}

	@Override
	public void perform(HttpServletRequest req, PrintWriter out) throws ServletException, IOException {
		if (validarParametros(req, out)) {
			String idUsuario = req.getSession().getAttribute(ID_PARAM_ID_USUARIO).toString();
			String idioma = req.getParameter(ID_PARAM_IDIOMA);
			
			if (UtilFormato.esNulo(idioma))
				out.write(getErrorJSON("Idioma no valido"));
			else {			
				try {
					manSeg.setIdioma(idUsuario, idioma);
					out.write(RESPUESTA_JSON_DEFECTO_OK);
				} catch (PersistenciaException e) {
					throw new ServletException("Error al setear la configuracion", e);
				}
			}
		}
	}
	
	
	
	/**
	 * Valida el parametro idioma ingresado
	 */
	protected boolean validarParametros (HttpServletRequest req, PrintWriter out) {
		String idioma = req.getParameter(ID_PARAM_IDIOMA);		
	    
        if (UtilValidacionParametros.esValidoIdIdioma(idioma))
        	return true;
        else
        	return super.validarParametros(req, out);
	}
}
