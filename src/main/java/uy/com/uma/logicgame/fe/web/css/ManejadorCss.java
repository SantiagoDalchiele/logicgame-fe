package uy.com.uma.logicgame.fe.web.css;

import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collection;

import javax.json.JsonObject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.css.sac.InputSource;
import org.w3c.dom.css.CSSRule;
import org.w3c.dom.css.CSSRuleList;
import org.w3c.dom.css.CSSStyleDeclaration;
import org.w3c.dom.css.CSSStyleSheet;

import com.steadystate.css.dom.CSSStyleRuleImpl;
import com.steadystate.css.parser.CSSOMParser;

import uy.com.uma.comun.util.UtilJSON;

/**
 * Clase que encapsula la lógica de manejo de las clases de estilo
 *
 * @author Santiago Dalchiele
 */
public class ManejadorCss {
	
	private static final Logger log = LogManager.getLogger(ManejadorCss.class.getName());
	
	
	/** Constante del atributo JSON para la definición de clases de estilo (dentro de la definición del juego) */
	private static final String ID_ATT_REGLAS = "reglasEstilo";

	
	
	/**
	 * Dada la ruta absoluta a un archivo .css retorna las reglas de estilo definidas en él en el formato JSON
	 * 
	 * @param pathCssFile ruta absoluta al archivo
	 * @return Colección de reglas, objetos del tipo PropCss
	 */
	public static String getPropsToJSON (String pathCssFile) {
		return propsCsstoJSON(loadCss(pathCssFile));
	}
	
	
	
	/**
	 * Dada una coleccion de PropCss lo retorna en formato JSON
	 * "reglasEstilo":[{"regla":"regla1","propiedad":"propiedad1","valor":"valor1"},{"regla":"regla2","propiedad":"propiedad2","valor":"valor2"}] 
	 */
	public static String propsCsstoJSON (Collection<PropCss> props) {
		Collection<JsonObject> reglas = new ArrayList<JsonObject>();
		
		for (PropCss pc : props)
			reglas.add(pc.toJSON());		

		return "\"" + ID_ATT_REGLAS + "\":" + UtilJSON.getJSONArray(reglas.toArray()).toString();
	}
	
	
	
	/**
	 * Retorna una coleccion de reglas dada la ruta de la hoja de estilo
	 */
	public static Collection<PropCss> loadCss (String pathCssFile) {		
		Collection<PropCss> col = new ArrayList<PropCss>();
		
		if (pathCssFile != null) {
			try {			
				Reader r = new FileReader(pathCssFile);
				InputSource is = new InputSource(r);
		        CSSOMParser parser = new CSSOMParser();
		        CSSStyleSheet hoja = parser.parseStyleSheet(is, null, null);            
		        CSSRuleList rules = hoja.getCssRules();
		        
		        for (int i = 0; i < rules.getLength(); i++) {
		            CSSRule rule = rules.item(i);                
		            
		            if (rule.getType() == CSSRule.STYLE_RULE) {
		                String [] sels = ((CSSStyleRuleImpl) rule).getSelectorText().split(",");
		                
		                for (int j = 0; j < sels.length; j++) {
		                    CSSStyleDeclaration dec = ((CSSStyleRuleImpl) rule).getStyle();                    
		                
		                    for (int h = 0; h < dec.getLength(); h++) {
		                        String propiedad = dec.item(h);                           
		                        String valor = dec.getPropertyValue (propiedad);
		                        String regla = sels[j].trim().toLowerCase();                        
		                        col.add(new PropCss(regla, propiedad, valor));
		                    }
		                }
		            }
		        }
			} catch (Exception e) {
				log.error("Error al cargar la hoja de estilo [" + pathCssFile + "]", e);
			}
		}
		
		return col;
	}
}
