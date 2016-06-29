package uy.com.uma.logicgame.fe.web.actions;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import uy.com.uma.comun.util.UtilJSON;
import uy.com.uma.logicgame.api.LogicGameException;
import uy.com.uma.logicgame.api.bean.DatosUsuario;
import uy.com.uma.logicgame.api.persistencia.PersistenciaException;

/**
 * Retorna el ranking de usuarios
 *
 * @author Santiago Dalchiele
 */
public class GetRankingAction extends SeguridadAbstractAction {
	
	/** Constantes con las propiedades del objeto a retornar */
	private static final String TAG_RANKING = "ranking";
	private static final String TAG_NIVEL = "nivel";
	private static final String TAG_USUARIO = "usuario";

	
	public GetRankingAction() throws LogicGameException {
		super();
	}

	@Override
	public String getName() {
		return ID_REQ_GET_RANKING;
	}

	@Override
	public void perform(HttpServletRequest req, PrintWriter out) throws ServletException, IOException {
		String idUsuario = req.getSession().getAttribute(ID_PARAM_ID_USUARIO).toString();
		
		try {
			Collection<DatosUsuario> rank = manSeg.getRanking(idUsuario, configuracion.getMaxUsersRanking());
			StringBuffer ret = new StringBuffer("{" + UtilJSON.getPropJSON(TAG_RANKING) + "[");
			
			for (DatosUsuario du : rank) {
				ret.append("{" + UtilJSON.getPropJSON(TAG_NIVEL) + du.getNivel() + ",");
				ret.append(UtilJSON.getPropJSON(TAG_USUARIO) + UtilJSON.getComillasJSON(du.getAlias()) + "},");
			}
			
			ret.deleteCharAt(ret.length()-1);
			out.write(ret.toString() + "]}"); 
		} catch (PersistenciaException e) {
			throw new ServletException("Error al obtener el ranking", e);
		}
	}
}
