package uy.com.uma.logicgame.fe.web.actions;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import uy.com.uma.logicgame.api.LogicGameException;
import uy.com.uma.logicgame.api.persistencia.PersistenciaException;
import uy.com.uma.logicgame.api.validacion.UtilValidacionParametros;

/**
 * Accion que recibe el identificador de usuario y un token, si son válidos guarda en session el identificador de usuario
 * y redirige a la página de cambio de contraseña
 * 
 * @author Santiago Dalchiele
 */
public class RecibirTokenAction extends SeguridadAbstractAction implements IResponseAction {
	
	private ServletResponse response;

	
	public RecibirTokenAction() throws LogicGameException {
	}
	
	@Override
	public String getName() {
		return ID_REQ_RECIBIR_TOKEN;
	}
	
	@Override
	protected boolean usaIdUsuario() {
		return false;
	}

	@Override
	public void setServletResponse (ServletResponse resp) {
		this.response = resp;
	}
	
	@Override
	public void perform(HttpServletRequest req, PrintWriter out) throws ServletException, IOException {
		if (validarParametros(req, out)) {
			final String idUsuario = req.getParameter(ID_PARAM_ID_USUARIO);
			final String token = req.getParameter(ID_PARAM_TOKEN);
			
			try {
				if (manSeg.existeUsuario(idUsuario) && manSeg.esValidoToken(idUsuario, token)) {
					req.getSession(false).invalidate();
					req.getSession(true).setAttribute(JuegoAbstractAction.ID_USUARIO_TOKEN, idUsuario);
					final String pagina = "/jsp/resetclavelg.jsp";
					RequestDispatcher dispatcher = req.getServletContext().getRequestDispatcher(pagina);
					dispatcher.forward(req, response);
				} else
					out.write(getErrorJSON (MSG_PARAMS_INCORRECTOS));
			} catch (PersistenciaException e) {
				throw new ServletException("Error al recibir el token", e);
			}
		}
	}
	
	
	
	/**
	 * Valida el parametro id usuario (NO puede ser un correo)
	 */
	@Override
	protected boolean validarParametros(HttpServletRequest req, PrintWriter out) {
		final String idUsuario = req.getParameter(ID_PARAM_ID_USUARIO);		
		
		if (UtilValidacionParametros.esValidoIdUsuario(idUsuario))
			return true;
		else
			return super.validarParametros(req, out);
	}	
}
