var re_hora = /^\d{2}:\d{2}$/;
var re_fecha =/^\d{4}-\d{2}-\d{2}$/;
var re_tarjeta =/^\d{6,8}$/;
var $inputTarjeta = $("#inputTarjeta");
var $inputEntrada = $("#inputEntrada");
var $inputSalida = $("#inputSalida");
var $inputFecha = $("#inputFecha");
var $bodyRegistros = $("#bodyRegistros");
var lista_registros = [];

$('#data_1 .input-group.date').datepicker({
    todayBtn: true,
    keyboardNavigation: false,
    forceParse: false,
    calendarWeeks: false,
    autoclose: true,
    format: "yyyy-mm-dd"
});

function limpiar_campos() {
    $inputTarjeta.val("");
    $inputEntrada.val("");
    $inputSalida.val("");
    $inputFecha.val("");
}

function agregar_asistencia(e) {
    e.preventDefault();
    $(".help-block").addClass("hidden");
    console.log("agregar_asistencia");

    var $fila = $(document.createElement("tr"));
    var $tdTarjeta = $(document.createElement("td"));
    var $tdEntrada = $(document.createElement("td"));
    var $tdSalida = $(document.createElement("td"));
    var $tdFecha = $(document.createElement("td"));
    var $tdEliminar = $(document.createElement("td"));
    var $btnEliminar = $(document.createElement("button"));
    var $iconoEliminar = $(document.createElement("i"));

    var data = {
        estado: 0,
        noTarjeta: parseInt($inputTarjeta.val(), 10),
        fechaRegistro: $inputFecha.val()
    };



    // Trayectoria D
    if (lista_registros.indexOf(data.noTarjeta) >= 0) {
        limpiar_campos();
        return;
    }

    /*
    if (!re_tarjeta.test($inputTarjeta.val())) {
        $("#errorB").css("display", "block");
        return;
    }
     */

    if (!re_fecha.test(data.fechaRegistro)) {
        $("#error6").removeClass("hidden");
        return;
    }
    var arreglo_fecha = data.fechaRegistro.split("-");
    var fechaObj = new Date(parseInt(arreglo_fecha[0]), parseInt(arreglo_fecha[1])-1, parseInt(arreglo_fecha[2]));
    if (fechaObj.getDay() === 0 || fechaObj.getDay() === 6) {
        $("#error6").removeClass("hidden");
        return;
    }
    var actual = new Date();
    if (fechaObj > actual) {
        $("#error6").removeClass("hidden");
        return;
    }
    $.ajax({
        type: "POST",
        contentType: "application/json",
        url: window.location.origin + "/api/dch/asistencias/consultar",
        data: JSON.stringify(data),
        dataType: "json",
        success: function(resultado) {
            if (resultado.estado === 2) { // Trayectoria A
                console.log("Mostar error de trayectoria A");
                $("#error1").removeClass("hidden");
                $inputTarjeta.val("");
                return;
            } else if (resultado.estado === 3 ){ // Trayectoria B
                console.log("Mostrar error de trayectoria B");
                $("#errorB").removeClass("hidden");
                return;
            } else if (resultado.estado === 4) { // Trayectoria C
                console.log("Mostrar otro de Trayectoria C");
                limpiar_campos();
                $("#errorC").removeClass("hidden");
                return;
            }

            // Verificar hora (Trayectoria E)
            var hora_entrada = $inputEntrada.val();
            var hora_salida = $inputSalida.val();

            if (!re_hora.test(hora_entrada)) {
                console.log("LA hora de entrada esta mal");
                $("#error3").removeClass("hidden");
                return;
            }
            if (!re_hora.test(hora_salida)) {
                console.log("La hora de salida esta mal");
                $("#error4").removeClass("hidden");
                return;
            }
            var condicion = Date.parse("01/01/2011 " + hora_entrada + ":00") < Date.parse("01/01/2011 6:30:00");
            condicion = condicion || Date.parse("01/01/2011 " + hora_entrada + ":00") > Date.parse("01/01/2011 22:00:00");
            if (condicion) {
                console.log("La hora de entrada esta mal");
                $("#error3").removeClass("hidden");
                return;
            }
            condicion = Date.parse("01/01/2011 " + hora_salida + ":00") < Date.parse("01/01/2011 7:00");
            condicion = condicion || Date.parse("01/01/2011 " + hora_salida + ":00") > Date.parse("01/01/2011 23:00:00");
            if (condicion) {
                console.log("La hora de salida esta mal");
                $("#error4").removeClass("hidden");
                return;
            }

            $tdTarjeta.text($inputTarjeta.val());
            $tdEntrada.text($inputEntrada.val());
            $tdSalida.text($inputSalida.val());
            $tdFecha.text($inputFecha.val());

            $btnEliminar.addClass("btn btn-white");
            $iconoEliminar.addClass("fa fa-times");
            $btnEliminar.append($iconoEliminar);
            $btnEliminar.on("click", eliminar_registro_tabla);
            $tdEliminar.append($btnEliminar);

            $fila.append($tdTarjeta);
            $fila.append($tdEntrada);
            $fila.append($tdSalida);
            $fila.append($tdFecha);
            $fila.append($tdEliminar);

            $bodyRegistros.append($fila);
            lista_registros.push(resultado.noTarjeta);
            $(".help-block").addClass("hidden");
        },
        error: function(err) {
            console.log("ERROR: " + err);
        }
    });
}

function registrar_asistencias(e) {
    e.preventDefault();
    // Trayectoria F
    // For para obtener toda la data
    var lista = [];
    var hijos = $bodyRegistros.children();

    if (hijos.length < 1)
        return;

    for (var i = 0; i < hijos.length; i++) {
        var tempNoTarjeta = hijos[i].children[0].textContent;
        tempNoTarjeta = parseInt(tempNoTarjeta, 10);
        var tempHoraEntrada = hijos[i].children[1].textContent;
        var tempHoraSalida = hijos[i].children[2].textContent;
        var tempFecha = hijos[i].children[3].textContent;
        if (tempHoraEntrada.length < 5)
            tempHoraEntrada = "0" + tempHoraEntrada;
        if (tempHoraSalida.length < 5)
            tempHoraSalida = "0" + tempHoraSalida;
        lista.push({
            noTarjeta: tempNoTarjeta,
            horaEntrada: tempHoraEntrada,
            horaSalida: tempHoraSalida,
            fechaRegistro: tempFecha
        });
    }
    $.ajax({
        type: "POST",
        contentType: "application/json",
        url: window.location.origin + "/api/dch/asistencias/agregar",
        data: JSON.stringify(lista),
        dataType: "json",
        success: function(resultado) {
            console.log(resultado);
            limpiar_campos();
            $bodyRegistros.empty();
            lista_registros = [];
            $("#exito").removeClass("hidden");
        },
        error: function(err) {
            console.log("ERROR: " + err);
            $("#errorF").removeClass("hidden");
        }
    });
}

function eliminar_registro_tabla(e) {
    e.preventDefault();
    var $elemento = $(this);
    var temp_tarjeta = $elemento.parent().parent().children()[0];
    var index = lista_registros.indexOf(parseInt(temp_tarjeta.textContent, 10));
    if (index > -1)
        lista_registros.splice(index, 1);
    $(this).parent().parent().remove();

}

$('#btnRegistrarAsistencias').on("click", registrar_asistencias);
$('#btnAgregar').on("click", agregar_asistencia);