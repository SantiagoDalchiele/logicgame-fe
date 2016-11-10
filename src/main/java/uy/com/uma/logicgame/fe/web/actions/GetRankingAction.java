package uy.com.uma.logicgame.fe.web.actions;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.json.JsonObject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import uy.com.uma.comun.util.UtilJSON;
import uy.com.uma.logicgame.api.LogicGameException;
import uy.com.uma.logicgame.api.bean.UsuarioDO;
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
			Collection<UsuarioDO> rank = manSeg.getRanking(idUsuario, configuracion.getMaxUsersRanking());
			Map<String, Object> props = new LinkedHashMap<String, Object>();
			Collection<JsonObject> ranking = new ArrayList<JsonObject>();
			
			for (UsuarioDO du : rank) {
				Map<String, Object> user = new LinkedHashMap<String, Object>();
				user.put(TAG_NIVEL, du.getNivel());
				user.put(TAG_USUARIO, du.getAlias());
				ranking.add(UtilJSON.getJSONObject(user));
			}
			
			props.put(TAG_RANKING, ranking.toArray());
			out.write(UtilJSON.getJSONObject(props).toString());
		} catch (PersistenciaException e) {
			throw new ServletException("Error al obtener el ranking", e);
		}
	}
}
