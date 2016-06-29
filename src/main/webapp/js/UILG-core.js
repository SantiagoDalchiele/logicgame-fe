/**
 * Define métodos de uso común en todos los módulos
 */
var UILG = (function () {
	
	/** Instancia a retornar */
	var my = {};
	
	/** Lenguaje utilizado */
	var lang;
	
	/** Patrones para validación de datos */
	var PATRON_IDS = /^[a-zA-Z0-9]{1,1}[_a-zA-Z0-9- \.]{0,63}$/;
	var PATRON_CLAVES = /^[_a-zA-Z0-9-\.]{5,32}$/;
	var PATRON_EMAILS = /^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,4})+$/;
	
	
	
	
	/**
	 * Retorna las constantes
	 */
	my.PATRON_IDS = function() { return PATRON_IDS; }
	my.PATRON_CLAVES = function() { return PATRON_CLAVES; }
	my.PATRON_EMAILS = function() { return PATRON_EMAILS; }
	
	
	
	/**
	 * Getter y Setter de la variable lang
	 */
	my.getLang = function() {
		return lang;
	}
	
	my.setLang = function (languaje) {
		lang = languaje;
	}
	
	
	
	/**
	 * Retorna el lenguaje en un objeto listo para ser usado por la biblioteca i18next
	 */
	my.getLenguaje = function() {
		return {lng: lang};
	}
	
	
	
	return my;
} ());