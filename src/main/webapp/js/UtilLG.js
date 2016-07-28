/**
 * Define métodos de utilería a ser utilizados en otros módulos como en codigo html
 * Define una función para JQuery a usar: $(<SELECTOR>).center(parent, alto, ancho), 
 * para centrar el objeto seleccionado según su padre o la ventana 
 */
var UtilLG = (function () {
	
	/** Instancia a retornar */
	var my = {};
	

	/**
	 * Al oprimir la tecla ENTER dispara el evento click de un boton
	 * Setea como botón por defecto este botón
	 * Para ser utilizado en los eventos del teclado, ejemplo onkeypress
	 */
	my.defaultButton = function (event, boton) {
		if (event.keyCode == 13)
			document.getElementById(boton).click();
	}
	
	
	
	/**
	 * Retorna la url de la página 
	 */
	my.getPageUrl = function() {
		var loc = '' + window.location;
		return loc.substring(0, 1+loc.lastIndexOf('/'));
	}
	
	
	
	/**
	 * Destruye una data table si esta existe (ya fue creada)
	 */
	my.destroyDataTable = function (selectorId) {
		if ($.fn.dataTable.isDataTable(selectorId)) {
		    table = $(selectorId).DataTable();
		    table.destroy();
		}
	}
	
	
	return my;
}());



/**
 * Centra un objeto relativo a su "padre" o al objeto window, con respecto solo a la altura o el ancho
 */
jQuery.fn.center = function(parent, alto, ancho) {
    if (parent)
        parent = this.parent();
    else
        parent = window;
        
    this.css("position","absolute");
    
    if (alto)
    	this.css("top", (($(parent).height() - this.outerHeight()) / 2) + $(parent).scrollTop() + "px");
    
    if (ancho)
    	this.css("left", (($(parent).width() - this.outerWidth()) / 2) + $(parent).scrollLeft() + "px");
    
    return this;
}