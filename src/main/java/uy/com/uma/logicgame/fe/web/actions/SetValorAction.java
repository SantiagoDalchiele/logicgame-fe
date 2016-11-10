package uy.com.uma.logicgame.fe.web.actions;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import uy.com.uma.comun.util.UtilJSON;
import uy.com.uma.logicgame.api.validacion.UtilValidacionParametros;
import uy.com.uma.logicgame.fe.web.model.MatrizJuego;

/**
 * Dada la matriz de juego guardada en session toma como parametros del request el id y el valor a setear
 * y setea ese valor en la matriz
 *
 * @author Santiago Dalchiele
 */
public class SetValorAction extends JuegoAbstractAction {
	
	private static final Logger log = LogManager.getLogger(SetValorAction.class.getName());

	@Override
	public String getName() {
		return ID_REQ_SET_VALOR;
	}

	@Override
	public void reset() {
	}
	
	@Override
	protected boolean usaIdUsuario() {
		return false;
	}

	@Override
	protected boolean usaMatriz() {
		return true;
	}

	
	
	/**
	 * Dada la matriz de juego guardada en session toma como parametros del request el id y el valor a setear
	 * y setea ese valor en la matriz
	 */
	@Override
	public void perform(HttpServletRequest req, PrintWriter out) throws ServletException, IOException {
		if (validarParametros(req, out)) {
			final String id = req.getParameter(ID_PARAM_ID_CELDA);
			final short valor = Short.parseShort(req.getParameter(ID_PARAM_VALOR));
			log.debug("Seteando valor de la celda [" + id + "] con el valor " + valor);
			MatrizJuego matriz = (MatrizJuego) req.getSession().getAttribute(ID_OBJ_MATRIZ_JUEGO);
			
			if (matriz == null)
				out.write(getErrorJSON(TIPO_ERROR_NO_LOGEADO, 666, "Timeout, vuelva a conectarse", ""));
			else {
				matriz.setValorIngresado(id, valor);
				Map<String, Object> props = new LinkedHashMap<String, Object>();
				props.put(ID_PARAM_ID_CELDA, id);
				props.put(ID_PARAM_VALOR, valor);
				out.write(UtilJSON.getJSONObject(props).toString());
			}
		}
	}

	
	
	/**
	 * Valida el identificador de la matriz de juego
	 */
	@Override
	protected boolean validarParametros(HttpServletRequest req, PrintWriter out) {
		final String id = req.getParameter(ID_PARAM_ID_CELDA);
		
		if (UtilValidacionParametros.esValidoIdMatrizJuego(id))
			return true;
		else
			return super.validarParametros(req, out);
	}	
}
