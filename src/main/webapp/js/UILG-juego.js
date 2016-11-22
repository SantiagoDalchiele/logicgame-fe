/**
 * Define la logica de manejo del juego en si
 * 
 * @see uy.com.uma.logicgame.fe.web.actions.JuegoAbstractAction
 * @see uy.com.uma.logicgame.fe.web.actions.FinJuegoAction
 * @see uy.com.uma.logicgame.resolucion.modelo.IValoresCuadroDecision
 * @see UILG-core.js, UILG-ajax.js, UILG-dialogos.js, UILG-main.js
 * @see i18next.js, jquery.js
 * 
 */
var UILG = (function (my) {
	
	/** 
	 * Nombre de los requerimientos enviados por AJAX
	 * @see uy.com.uma.logicgame.fe.web.actions.JuegoAbstractAction 
	 */
	var ID_REQ_GET_JUEGO = "getJuego.do";
	var ID_REQ_GET_RANKING = "getRanking.do";
	var ID_REQ_REINICIAR_JUEGO = "reiniciarJuego.do";
	var ID_REQ_GRABAR_JUEGO = "grabarJuego.do";
	var ID_REQ_SET_VALOR = "setValor.do";
	var ID_REQ_FIN_JUEGO = "finJuego.do";
	
	/** Mensajes parametrizados por lenguaje */
	var TITULO_DIALOGO_CONFIRMACION;
	var TITULO_DIALOGO_DESCRIPCION_JUEGO;
	var TXT_CONFIRMACION_REINICIAR;
	var TXT_EXITO_GRABAR;
	var ERROR_INCOMPLETO;
	var ERROR_INCORRECTO;	
	
	/** Identificadores de los elementos de la UI */
	var ID_ELEMENT_TABLA_JUEGO = "lg_tabla_juego";
	var ID_ELEMENT_BTT_HISTORIA = "lg_boton_historia";
	var ID_ELEMENT_BTT_FINALIZAR = "lg_boton_finalizar";

	/** Rutas de las imagenes utilizadas */
	var IMG_ACCION_BLANCO = "img/blanco.png";
	var IMG_ACCION_NEGACION = "img/uncheck.png";
	var IMG_ACCION_AFIRMACION = "img/check.png";

	/** 
	 * Valores de una acción en blanco/negacion/afirmacion
	 * @see uy.com.uma.logicgame.resolucion.modelo.IValoresCuadroDecision 
	 */
	var ACCION_EN_BLANCO = 1;
	var ACCION_AFIRMACION = 2;
	var ACCION_NEGACION = 3;	

	/** 
	 * Constantes que representan el resultado de evaluar el juego
	 * @see uy.com.uma.logicgame.api.persistencia.IManejadorJuegoWeb 
	 */
	var JUEGO_INCOMPLETO = 1;
	var JUEGO_ERRONEO = 2;
	var JUEGO_EXITOSO = 3;
	
	/** Constantes para efectos visuales */
	var BORDE_TABLA_GRUESO = "3px";
	var COLOR_FONDO_RESALTAR_VALORES = "#cfe7ff";	

	/** Mantiene los identificadores de cada celda de acción, utilizado para inicializar las imagenes de cada celda */
	var ids = [];
	
	/** Cantidad de valores de cada dimension del juego */
	var cantValores = 0;
	
	/** Arreglo de objetos de los valores de las dimensiones por filas y por columnas */
	var celdasValoresXFila = [];
	var celdasValoresXColumna = [];

	/** Mapeo entre el identificador de la celda y su valor (en blanco/negación/afirmación) */
	var matriz = {};
	
	/** Mapeo entre el identificador de la celda y su valor anterior (en blanco/negación/afirmación) */
	var valorAnterior = {};
	
	/** Texto/descripcion del juego */
	var descripcion;

	/** Pila de acciones realizadas/deshechas para implementar el undo y el redo */
	var pilaAccHechas = [];
	var pilaAccDes = [];
	
	/** Reglas de estilo seteadas */
	var reglasEstilo = [];

	
	
	/**
	 * ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	 * Se encarga de renderizar el juego
	 */
	var Render = (function() {
		
		/** Instancia a retornar */
		var myRender = {};
		
		
		/**
		 * Inicializa la UI del tablero de juego
		 */
		myRender.renderJuego = function (dimsSuperior, dimsLateral) {
			cantValores = dimsSuperior[0].valores.length;
			var tbl = document.getElementById(ID_ELEMENT_TABLA_JUEGO);
			$("#lg_tabla_juego").empty();
			renderDimsXColumnas (tbl, dimsSuperior);
			renderDimsXFilas (tbl, dimsSuperior, dimsLateral);	
		}



		/**
		 * Crea de forma dinámica las celdas de la table de la fila con los nombres de las dimensiones por columnas
		 * y sus valores
		 */
		function renderDimsXColumnas (tbl, dimsSuperior) {		

			/** fila con el cabezal con el nombre de las dimensiones */
			var rowCabDim = tbl.insertRow(-1);
			var celdaNula = rowCabDim.insertCell(-1);
			celdaNula.rowSpan = 2;
			celdaNula.colSpan = 2;
			celdaNula.className = "lg_juego-celda lg_juego-celda-nula";
			celdaNula.style.textAlign = "center";
			celdaNula.style.verticalAlign = "center";		
			
			/** Boton con la historia o descripcion del juego */
			var img = document.createElement("IMG");
			img.id = ID_ELEMENT_BTT_HISTORIA;
			img.src = "img/Document-Attach-icon.png";		
			img.className = "lgui-icono";
			img.onclick = function(){ UILG.descJuego(); };
			celdaNula.appendChild(img);
			
			for (var i = 0; i < dimsSuperior.length; i++) {
				var celda = rowCabDim.insertCell(-1);
				celda.colSpan = dimsSuperior[i].valores.length;
				celda.style.textAlign = "center";			
				celda.className = "lg_juego-celda";
				celda.style.borderTopWidth = BORDE_TABLA_GRUESO;
				celda.style.borderLeftWidth = BORDE_TABLA_GRUESO;
				
				if (i == (dimsSuperior.length-1))
					celda.style.borderRightWidth = BORDE_TABLA_GRUESO;
				
				var divId = document.createElement("DIV");
				divId.className = "lg_juego-tit-sup";
				divId.innerHTML = dimsSuperior[i].id;
				celda.appendChild(divId);
			}
			
			/** Fila con los valores de cada dimensión */
			var rowValsDim = tbl.insertRow(-1);
			
			for (var i = 0; i < dimsSuperior.length; i++) {
				var valores = dimsSuperior[i].valores;
				
				for (var j = 0; j < valores.length; j++) {
					var celda = rowValsDim.insertCell(-1);
					celda.className = "lg_juego-celda lg_juego-celda-sup";
					celda.style.borderBottomWidth = BORDE_TABLA_GRUESO;
					
					if (j == 0)
						celda.style.borderLeftWidth = BORDE_TABLA_GRUESO;
					
					if ((i == (dimsSuperior.length-1)) && (j == (valores.length-1)))
						celda.style.borderRightWidth = BORDE_TABLA_GRUESO;				
					
					var divVal = document.createElement("DIV");
					divVal.className = "lg_juego-vertical";
					divVal.style.minHeight = (9 * valores[j].length) + "px";
					divVal.style.maxHeight = (9 * valores[j].length) + "px";
					divVal.innerHTML = valores[j];
					celdasValoresXColumna.push(celda);
					celda.appendChild(divVal);
				}
			}
		}
		
		

		/**
		 * Crea de forma dinámica las celdas de la table que corresponden a los nombres de las dimensiones por filas, 
		 * sus valores y las celdas del tablero de acción (afirmaciones/negaciones/en blanco)
		 */
		function renderDimsXFilas (tbl, dimsSuperior, dimsLateral) {
			for (var i = 0; i < dimsLateral.length; i++) {
				var valores = dimsLateral[i].valores;
				
				for (var j = 0; j < valores.length; j++) {
					var row = tbl.insertRow(-1);
					var celda;
					var div;
					
					/** Celda con el id de la dimensión */
					if (j == 0) {
						celda = row.insertCell(-1);
						celda.rowSpan = valores.length;
						celda.className = "lg_juego-celda lg_juego-celda-lateral";
						celda.style.verticalAlign = "center";
						celda.style.textAlign = "center";
						celda.style.borderLeftWidth = BORDE_TABLA_GRUESO;
						celda.style.borderBottomWidth = BORDE_TABLA_GRUESO;
						
						if (i == 0)
							celda.style.borderTopWidth = BORDE_TABLA_GRUESO;
						
						div = document.createElement("DIV");
						div.className = "lg_juego-vertical";
						div.style.maxHeight = (10 * dimsLateral[i].id.length) + "px";
						div.innerHTML = dimsLateral[i].id;
						celda.appendChild(div);
					}
					
					/** Celda con el valor de la dimensión */
					celda = row.insertCell(-1);
					celda.className = "lg_juego-celda";
					celda.style.borderRightWidth = BORDE_TABLA_GRUESO;
					
					if (j == 0)
						celda.style.borderTopWidth = BORDE_TABLA_GRUESO;
					
					if ((i == (dimsLateral.length-1)) && (j == (valores.length-1)))
						celda.style.borderBottomWidth = BORDE_TABLA_GRUESO; 
					
					div = document.createElement("DIV");
					div.innerHTML = dimsLateral[i].valores[j];
					celdasValoresXFila.push(celda);
					celda.appendChild(div);
					
					/** Celdas de acción y botón de finalizar */
					renderCeldasAccion(row, i, j,  dimsSuperior, dimsLateral);
				}
			}
		}



		
		/**
		 * Crea de forma dinámica las celdas de acción + el botón de finalizar
		 */
		function renderCeldasAccion (row, i, j, dimsSuperior, dimsLateral) {
			var celda;
			
			/** Celdas de acción, donde poner las marcas */
			for (var k = 0; k < (dimsSuperior.length-i); k++) {
				for (var h = 0; h < dimsSuperior[k].valores.length; h++) {
					celda = row.insertCell(-1);
					celda.className = "lg_juego-celda";
					
					if (h == (dimsSuperior[k].valores.length-1))
						celda.style.borderRightWidth = BORDE_TABLA_GRUESO;
					
					if (j == (dimsSuperior[k].valores.length-1)) 
						celda.style.borderBottomWidth = BORDE_TABLA_GRUESO;
						
					var img = document.createElement("IMG");
					img.id = i + "." + k + "." + j + "." + h;
					ids.push(img.id);
					img.className = "lg_juego-celda-sel";			
					img.onclick = function(){ UILG.changeImage(this.id); };
					img.onmouseover = function() { UILG.celdaSeleccionada(this.id); };
					img.onmouseleave = function(){ UILG.cambioValor(this.id); };
					celda.appendChild(img);
				}
			}
		
			
			/** Boton de finalizar */
			if ((i == (dimsLateral.length - 1)) && (j == 0)) {
				for (var k = 0; k < (dimsSuperior.length-1); k++) {
					celda = row.insertCell(-1);
					celda.rowSpan = dimsSuperior[0].valores.length;
					celda.colSpan = dimsSuperior[0].valores.length;
					celda.className = "lg_juego-celda lg_juego-celda-nula";
				}									
				
				celda.style.textAlign = "right";
				celda.style.verticalAlign = "bottom";			
				var img = document.createElement("IMG");
				img.id = ID_ELEMENT_BTT_FINALIZAR;
				img.src = "img/checkered_flag_icon.png";
				img.className = "lg_juego-icono-grande";
				img.onclick = function(){ UILG.finJuego(); };			
				celda.appendChild(img);
			}
		}
		
		
		
		return myRender;
	}());
	/**
	 * ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	 */

	
	
	/**
	 * Inicializa los literales dependientes del idioma
	 */
	my.resetLangJuego = function() {
		i18n.init(UILG.getLenguaje(), function(err, t) {			
			$("#lg_dialogoError").prop('title', t("ui.titulosDialogos.error"));
			$("#lg_dialogoFinalizacion").prop('title',  t("ui.titulosDialogos.finalizacion"));
			$("#lg_dialogoConfiguracion").prop('title',  t("ui.titulosDialogos.configuracion"));
			$("#lg_finalizacion_msg").text(t("ui.mensajeFinalizacion"));			
			$("#lg_ranking_button").prop('title', t("ui.tooltips.botonRanking"));
			$("#lg_grabar_btt").prop('title', t("ui.tooltips.botonGrabar"));
			$("#lg_deshacer_btt").prop('title', t("ui.tooltips.botonDeshacer"));
			$("#lg_rehacer_btt").prop('title', t("ui.tooltips.botonRehacer"));
			$("#lg_configurar_btt").prop('title', t("ui.tooltips.botonConfigurar"));
			$("#lg_reiniciar_btt").prop('title', t("ui.tooltips.botonReiniciar"));
			$("#lg_cerrarSession_btt").prop('title', t("ui.tooltips.botonCerrarSession"));			
			
			TITULO_DIALOGO_CONFIRMACION = t("ui.titulosDialogos.confirmacion");
			TITULO_DIALOGO_DESCRIPCION_JUEGO = t("ui.titulosDialogos.descripcionJuego");
			TXT_CONFIRMACION_REINICIAR = t("ui.mensajeReiniciar");
			TXT_EXITO_GRABAR = t("ui.mensajeExitoGrabar");
			ERROR_INCOMPLETO = t("juego.incompleto");
			ERROR_INCORRECTO = t("juego.incorrecto");
    	});
		
		resetLangObjDinamicos();
	}
	
	function resetLangObjDinamicos() {
		i18n.init(UILG.getLenguaje(), function(err, t) {			
			$("#lg_boton_historia").prop('title', t("ui.tooltips.botonHistoria"));
			$("#lg_boton_finalizar").prop('title', t("ui.tooltips.botonFinalizar"));
    	});
	}
	
	
	
	/**
	 * Invoca por ajax a obtener el juego actual del usuario logeado
	 */
	my.renderJuego = function() {
		UILG.ocultarDialogos();
		UILG.dialogoEspera();
		UILG.ajaxPost (ID_REQ_GET_JUEGO, {}, false, renderPanelJuego);
	}
	
	
	
	/**
	 * Muestra/Oculta el ranking de los usuarios
	 */
	my.toggleRanking = function() {
		UILG.ocultarDialogos();
		$("#lg_ranking_panel").animate({width:'toggle'},350);
	}
	
	
	
	/**
	 * Grabar
	 */
	my.grabar = function() {
		UILG.ocultarDialogos();
		UILG.ajaxPost (ID_REQ_GRABAR_JUEGO, {}, false, resultadoGrabar);
	}
	
	
	
	/**
	 * Acciones de hacer/deshacer
	 */
	my.deshacer = function () {
		UILG.ocultarDialogos();
		
		if (pilaAccHechas.length > 0) {
			var dato = pilaAccHechas.pop();
			pilaAccDes.push(dato);
			hacerDeshacer ({ idCelda: dato.idCelda, valor: dato.valorAnterior });
		}
	}
	
	my.rehacer = function () {
		UILG.ocultarDialogos();
		
		if (pilaAccDes.length > 0) {
			var dato = pilaAccDes.pop();
			pilaAccHechas.push(dato);
			hacerDeshacer ({ idCelda: dato.idCelda, valor: dato.valorNuevo });
		}
	}	
	
	
	
	/**
	 * Reinicia el juego
	 */
	my.reiniciar = function() {
		UILG.dialogo (TITULO_DIALOGO_CONFIRMACION, TXT_CONFIRMACION_REINICIAR, true, reiniciarOK, true);
	}	
	
	function reiniciarOK() {
		UILG.ajaxPost (ID_REQ_REINICIAR_JUEGO, {}, false, reiniciarJuego);
	}
	
	
	
	/**
	 * Cambia la imagen de una celda de acción (afirmación/negación/en blanco)
	 */
	my.changeImage = function (id) {
		UILG.ocultarDialogos();
		var celda = matriz[id];
		var imagen = document.getElementById(id);
		
		if (celda == ACCION_EN_BLANCO) {
			imagen.src = IMG_ACCION_NEGACION;
			matriz[id] = ACCION_NEGACION;
		} else if (celda == ACCION_NEGACION) {
			imagen.src = IMG_ACCION_AFIRMACION;
			matriz[id] = ACCION_AFIRMACION;
		} else {
			imagen.src = IMG_ACCION_BLANCO;
			matriz[id] = ACCION_EN_BLANCO;
		}
	}
	
	
	
	/**
	 * Resalta (cambiando el fondo) el valor de las dimensiones seleccionadas
	 */
	my.celdaSeleccionada = function (id) {
		setBackgroundColorSeleccion(id, COLOR_FONDO_RESALTAR_VALORES);		
	}
	
	
	
	/**
	 * Avisa al servidor mediante req AJAX que un valor cambio en la matriz del juego
	 * @see uy.com.uma.logicgame.be.web.actions.JuegoAbstractAction
	 */
	my.cambioValor = function (id) {
		setBackgroundColorSeleccion(id, "");
		var valAnt = valorAnterior[id];
		var valActual = matriz[id];
			
		if (valAnt != valActual) {
			var dato = { idCelda: id, valorAnterior: valAnt, valorNuevo: valActual };
			pilaAccHechas.push(dato);		
			setValor ({ idCelda: id, valor: valActual });
		}
	}

	
	
	/**
	 * Muestra un div por delante de todo con la descripción del juego
	 */
	my.descJuego = function() {
		UILG.dialogo (TITULO_DIALOGO_DESCRIPCION_JUEGO, descripcion, false, null, false);
	}
	
	
	
	/**
	 * Envia los valores que no han sido actualizados en el servidor de forma sincrónica
	 * Envia el requerimiento de validar que el juego está finalizado (incompleto, correcto o incorrecto)
	 * @see uy.com.uma.logicgame.be.web.actions.JuegoAbstractAction
	 */
	my.finJuego = function() {
		UILG.ocultarDialogos();
		var parameters;
		
		for (id in matriz) {
			var valAnt = valorAnterior[id];
			var valActual = matriz[id];
			
			if (valAnt != valActual) {
				parameters = { idCelda: id, valor: valActual };
				UILG.ajaxPost (ID_REQ_SET_VALOR, parameters, false, actualizarValor);				
			}
		}
		
		parameters = "";
		UILG.ajaxPost (ID_REQ_FIN_JUEGO, parameters, false, resultadoFinJuego);
	}
	
	
	
	/**
	 * Renderiza el panel de juego, el juego en si, las pistas y el ranking
	 */
	function renderPanelJuego(data) {		
		init(data);				
		MainLG.initPistas(data.pistas);
		cargarRanking();
		resetLangObjDinamicos()
		$("#lg_panelEspera").hide();		
		$("#lg_panel_ui_juego").show();
	}
	
	
	
	/**
	 * Muestra dialogo de confirmación que grabó exitosamente el estado del juego
	 */
	function resultadoGrabar() {
		console.log(TITULO_DIALOGO_CONFIRMACION);
		console.log(TXT_EXITO_GRABAR);
		UILG.dialogo (TITULO_DIALOGO_CONFIRMACION, TXT_EXITO_GRABAR, true, null, false);
	}
	
	
	
	/**
	 * Reinicia el juego y setea la primer pista
	 */
	function reiniciarJuego() {
		reiniciarInt();
		MainLG.reiniciarPistas();
	}
	
	
	
	/**
	 * Carga el ranking
	 */
	function cargarRanking() {
		$("#lg_tabla_ranking").empty();
		UILG.ajaxPost (ID_REQ_GET_RANKING, {}, false, renderRanking);
	}
	
	function renderRanking (data) {
		var tbl = document.getElementById("lg_tabla_ranking");
		
		for (i in data.ranking) {
			var elem = data.ranking[i];
			var row = tbl.insertRow(-1);
			var celda = row.insertCell(-1);
			celda.innerHTML = elem.nivel;
			celda = row.insertCell(-1);
			celda.innerHTML = elem.usuario;
		}
	}
	
	
	
	/**
	 * Renderiza el juego. 
	 * Setea el estado anterior en este nivel
	 * @see uy.com.uma.logicgame.be.web.css.ManejadorCss
	 * @see uy.com.uma.logicgame.be.web.css.PropCss
	 */
	function init (data) {
		if (data == null)
			UILG.error (1, 201, "Respuesta no esperada", "datos del juego nulos");
		else {
			initVarClase();
			
			for (i in reglasEstilo) {
				var elem = reglasEstilo[i];			
				$(elem.regla).css(elem.propiedad, elem.valor);
			}
			
			reglasEstilo = [];
			
			if (data.reglasEstilo) {
				for (i in data.reglasEstilo) {
					var elem = data.reglasEstilo[i];
					var valorAnt = $(elem.regla).css(elem.propiedad);					
					$(elem.regla).css(elem.propiedad, elem.valor);
					elem.valor = valorAnt;
					reglasEstilo.push(elem);
				}
			} 
			
			$("#lg_nivel_juego").text(data.nivel);
			$("#lg_titulo_juego").text(data.titulo);			
			descripcion = data.texto;			
			Render.renderJuego(data.dimsSuperior, data.dimsLateral);
			reiniciarInt();			
		
			for (i in data.estado)
				hacerDeshacer(data.estado[i]);
		}
	}
	
	
	
	/**
	 * Re-inicializa el juego
	 * Inicializa imagenes del tablero de acciones
	 * Inicializa los valores de la matriz
	 */
	function reiniciarInt() {
		pilaAccHechas = [];
		pilaAccDes = [];
		
		for (i in ids) {
			var id = ids[i];
			document.getElementById(id).src = IMG_ACCION_BLANCO;
			matriz[id] = ACCION_EN_BLANCO;
			valorAnterior[id] = ACCION_EN_BLANCO;
		}
	}
	
	
	
	/**
	 * Muestra el panel de finalización de un nivel
	 */
	function finalizacion() {		
		$("#lg_dialogoFinalizacion").dialog({
			modal: true,
			closeOnEscape: false,
			open: function(event, ui) { $(".lg_ui-dialog-titlebar-close", ui.dialog | ui).hide(); },
			buttons: [{
				text: UILG.TXT_BOTON_ACEPTAR(),
				click: function() {				
					$(this).dialog("close");
					finalizacionAceptar();
				}
			}]
		});
	}
	
	
	
	/** 
	 * Esconde el panel con el mensaje de finalizacion, recarga el juego con el siguiente nivel 
	 */
	function finalizacionAceptar() {
		UILG.dialogoEspera();
		$("#lg_panel_ui_juego").hide();		
		UILG.ajaxPost (ID_REQ_GET_JUEGO, {}, false, renderPanelJuego);
	}
	
	
	
	/**
	 * Inicializa las variales privadas de la clase
	 */
	function initVarClase() {
		cantValores = 0;
		ids = [];
		celdasValoresXFila = [];
		celdasValoresXColumna = [];
		matriz = {};
		valorAnterior = {};
		pilaAccHechas = [];
		pilaAccDes = [];
	}
	
	
	
	/**
	 * Setea el valor nuevo/viejo en la matriz, actualiza la imagen y manda setear el dato en el servidor
	 */
	function hacerDeshacer (dato) {
		if ((dato != null) && (dato != "")) {
			matriz[dato.idCelda] = dato.valor;
			var imagen = document.getElementById(dato.idCelda);
			
			if (dato.valor == ACCION_EN_BLANCO) {
				imagen.src = IMG_ACCION_BLANCO;
			} else if (dato.valor == ACCION_NEGACION) {
				imagen.src = IMG_ACCION_NEGACION;
			} else {
				imagen.src = IMG_ACCION_AFIRMACION;
			}
			
			setValor(dato);
		}
	}	

	
	
	/**
	 * Envia el requerimiento de cambiar el valor
	 */
	function setValor (parameters) {
		UILG.ajaxPost (ID_REQ_SET_VALOR, parameters, true, actualizarValor);
	}	
	
	
	
	/**
	 * Actualiza el valor anterior al valor actual
	 */
	function actualizarValor (data) {
		if (data == null)
			UILG.error(1, 436, "Respuesta no esperada", "Datos en nulo");
		else			
			valorAnterior[data.idCelda] = data.valor;
	}
	
	
	
	/**
	 * Despliega en la interface gráfica si finaliza el juego o está incompleto o erroneo
	 */
	function resultadoFinJuego (response) {
		if (response == null)
			UILG.error (1, 402, "Respuesta no esperada", "datos de la respuesta nulos");
		else {
			if (response.resultado == JUEGO_INCOMPLETO)
				UILG.error (1, 405, ERROR_INCOMPLETO, "");
			else if (response.resultado == JUEGO_EXITOSO)
				finalizacion();
			else if (response.resultado == JUEGO_ERRONEO)
				UILG.error (1, 409, ERROR_INCORRECTO, "");
		}
	}
	
	
	
	/**
	 * Setea el backgroundColor de los valores de las dimensiones seleccionadas/deseleccionadas al color que se pasa como parámetro
	 */
	function setBackgroundColorSeleccion (id, color) {
		var vecId = id.split(".");
		var filaMatriz = vecId[0];
		var colMatriz = vecId[1];
		var filaCuadro = vecId[2];
		var colCuadro = vecId[3];
		var indXFila = parseInt(filaMatriz * (cantValores), 10) + parseInt(filaCuadro, 10);
		var indXCol = parseInt(colMatriz * (cantValores), 10) + parseInt(colCuadro, 10);		
		var celdaXFila = celdasValoresXFila[indXFila];
		var celdaXColumna = celdasValoresXColumna[indXCol];		
		celdaXFila.style.backgroundColor = color;
		celdaXColumna.style.backgroundColor = color;
	}
	
	
	
	return my;
}(UILG));
