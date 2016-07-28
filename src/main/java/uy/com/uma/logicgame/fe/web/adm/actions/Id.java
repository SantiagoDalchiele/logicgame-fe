package uy.com.uma.logicgame.fe.web.adm.actions;

import uy.com.uma.comun.util.UtilJSON;
import uy.com.uma.logicgame.api.bean.IJSONObject;

/**
 * Encapsula un identificador de tipo String
 *
 * @author Santiago Dalchiele
 */
class Id implements IJSONObject {

	private String id;
	
	
	public Id() {		
	}
	
	public Id (String id) {
		setId(id);
	}
	
	
	
	public void setId (String id) {
		this.id = id;
	}
	public String getId() {
		return this.id;
	}
	
	
	@Override
	public String toJSON() {
		return UtilJSON.getComillasJSON(getId());
	}
}
