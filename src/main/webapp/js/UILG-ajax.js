/**
 * Define métodos de comunicación mediante AJAX
 * 
 * @see UtilLG.js, UILG-dialogos.js
 */
var UILG = (function (my) {
	
	

	/**
	 * Envia el requerimiento al servidor via AJAX
	 */
	my.ajaxPost = function (requerimiento, params, asincrono, procesaResponse) {
		$.ajax({type: "POST",
			url: UtilLG.getPageUrl() + requerimiento,
			data: params,
			async: asincrono,
			contentType: "application/x-www-form-urlencoded; charset=ISO-8859-1",
			dataType: "json",
			success: function (data, textStatus, jqXHR) {			
				if (data.error)
					UILG.error (data.error.tipo, data.error.nro, data.error.mensaje, data.error.detalle);
				else if (procesaResponse != null)
					procesaResponse (data);
			},
			error: function (xhr) {
				UILG.error (1, xhr.status, xhr.statusText, "Error producido en el requerimiento AJAX");
			}
		});
	}
	
	
	return my;
} (UILG));