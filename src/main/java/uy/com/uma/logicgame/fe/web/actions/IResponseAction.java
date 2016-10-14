package uy.com.uma.logicgame.fe.web.actions;

import javax.servlet.ServletResponse;

/**
 * Define el metodo para setear el response
 * Acciones que necesitan por ejemplo hacer el forward a otra página
 *
 * @author Santiago Dalchiele
 */
public interface IResponseAction {

	void setServletResponse (ServletResponse resp);
}
