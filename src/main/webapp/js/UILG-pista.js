/**
 * Define la logica de manejo de las pistas
 * 
 * @see i18next.js, jquery.js
 */
var UILG = (function (my) {

	/** Identificadores de los elementos de la UI */
	var ID_ELEMENT_TEXT_AREA_PISTA = "lg_pista";
	var ID_ELEMENT_IMG_PREVIO = "lg_previo";
	var ID_ELEMENT_IMG_SIGUIENTE = "lg_siguiente";
	
	/** Rutas de las imagenes utilizadas */
	var IMG_PREVIO_HABILITADO = "img/left.png";
	var IMG_PREVIO_INHABILITADO = "img/left-dis.png";
	var IMG_SIGUIENTE_HABILITADO = "img/right.png";
	var IMG_SIGUIENTE_INHABILITADO = "img/right-dis.png";
	
	
	/** Arreglo de pistas */
	var pistas = [];

	/** Número de pista seleccionada 0..pistas.length */
	var pistaSel = 0;
	
	
	
	/**
	 * Inicializa la interface grafica
	 */
	my.initPistas = function (data) {
		pistas = data;
		my.reiniciarPistas();
	}
	
	
	
	/**
	 * Actualiza todos los literales dependientes del idioma
	 */
	my.resetLangPistas = function() {
		i18n.init(UILG.getLenguaje(), function(err, t) {
			$("#lg_previo").prop('title', t("ui.tooltips.botonPrevio"));
			$("#lg_siguiente").prop('title', t("ui.tooltips.botonSiguiente"));			
    	});
	}
	
	
	
	/**
	 * Selecciona la primer pista
	 */
	my.reiniciarPistas = function() {
		pistaSel = 0;
		setPista();
	}
	
	
	
	/**
	 * Cambia el valor de la pista seleccionada hacia la anterior
	 */
	my.pistaAnterior = function() {
		if (pistaSel > 0) {
			pistaSel--;
			setPista();
		}
	}



	/**
	 * Cambia el valor de la pista seleccionada hacia la siguiente
	 */
	my.pistaSiguiente = function() {
		if (pistaSel < (pistas.length-1)) {
			pistaSel++;
			setPista();
		}
	}
	
	
	
	/**
	 * Recorre las distintas pistas según la variable pistaSel (pista seleccionada)
	 */
	function setPista() {
		document.getElementById(ID_ELEMENT_TEXT_AREA_PISTA).innerHTML = pistas[pistaSel];		
		var objPrevio = document.getElementById(ID_ELEMENT_IMG_PREVIO);
		var objSiguiente = document.getElementById(ID_ELEMENT_IMG_SIGUIENTE)
		
		if (pistaSel > 0)
			objPrevio.src = IMG_PREVIO_HABILITADO;
		else
			objPrevio.src = IMG_PREVIO_INHABILITADO;
		
		if (pistaSel < (pistas.length-1))
			objSiguiente.src = IMG_SIGUIENTE_HABILITADO;
		else
			objSiguiente.src = IMG_SIGUIENTE_INHABILITADO;
	}
	
	
	
	return my;
}(UILG));
