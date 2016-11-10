package uy.com.uma.logicgame.fe.test;

import java.util.Collection;

import uy.com.uma.logicgame.fe.web.css.ManejadorCss;
import uy.com.uma.logicgame.fe.web.css.PropCss;

/**
 *
 * @author Santiago Dalchiele
 */
public class TestManejadorCss {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String pathCssFile = "c:/santiago/lg/logicgame-fe/src/main/webapp/css/lg01.css";
		Collection<PropCss> col = ManejadorCss.loadCss(pathCssFile);
		System.out.println(col.size());
	}

}
