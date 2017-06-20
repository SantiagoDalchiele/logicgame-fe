package uy.com.uma.logicgame.fe.web.adm.actions;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import uy.com.uma.comun.util.UtilJSON;
import uy.com.uma.logicgame.api.validacion.UtilValidacionParametros;
import uy.com.uma.logicgame.persistencia.seguridad.UtilSeguridad;

/**
 * Controla contra una clave parametrizada en el archivo logicgame.properties el login de administracion
 *
 * @author Santiago Dalchiele
 */
public class LoginAction extends AdmAbstractAction {

	@Override
	public String getName() {
		return ID_REQ_LOGIN;
	}

	@Override
	public void perform(HttpServletRequest req, PrintWriter out) throws ServletException, IOException {
		req.getSession().removeAttribute(ID_ATT_CLAVE_ADM);
		short loginResult = 0;			
		final String clave = req.getParameter(ID_ATT_CLAVE_ADM);
		
		if (UtilValidacionParametros.esValidaClave(clave)) {
			final String pass = UtilSeguridad.getClaveEncriptada(ID_USUARIO_ADMIN, clave);
			
			if ((configuracion != null) && (configuracion.getAdmPassword().equals(pass))) {
				req.getSession(false).invalidate();
				req.getSession(true).setAttribute(ID_ATT_CLAVE_ADM, pass);
				loginResult = 1;					
			}
		}
		
		out.write(UtilJSON.getJSONObject("resultado", "" + loginResult).toString());
	}
}
