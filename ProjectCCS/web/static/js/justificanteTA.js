
var _validFileExtensions = [".jpg",".jpeg"];

$(document).ready(function () {
    llenarEstado();
    $(document).on('change', '#getEstado', function(event) {
        llenarEstado();
    });

    $("#buttonCancelar").on("click",function(){
        window.location.href="/personal/justificantes/tipoa/cancelar";
    });
})
function ValidateSingleInput(oInput) {
    if (oInput.type == "file") {
        var sFileName = oInput.value;
        if (sFileName.length > 0) {
            var blnValid = false;
            for (var j = 0; j < _validFileExtensions.length; j++) {
                var sCurExtension = _validFileExtensions[j];
                if (sFileName.substr(sFileName.length - sCurExtension.length, sCurExtension.length).toLowerCase() == sCurExtension.toLowerCase()) {
                    blnValid = true;
                    break;
                }
            }
            if (!blnValid) {
                $("#mensajeArchivo").removeClass("hidden");
                oInput.value = "";
                return false;
            }
            $("#mensajeArchivo").addClass("hidden");
        }
    }
    return true;
}



function llenarEstado() {
        var estado=$("#getEstado option:selected").html();
        var datos = {
            nombre: estado
        };
        $.ajax({
            type: "POST",
            contentType: "application/json",
            url: window.location.origin + "/personal/justificantes/tipoa/unidadMedica",
            data: JSON.stringify(datos),
            dataType: "json",
            success : function (resultado) {
                $("#getUnidadMedica").empty();
                for(var i=0; i < resultado.length; i++){
                    var option = document.createElement("option"); //Creas el elemento opción
                    $(option).val(resultado[i].idUnidadMedica);
                    $(option).html(resultado[i].nameUnidadMedica); //Escribes en él el nombre de la provincia
                    $(option).appendTo("#getUnidadMedica"); //Lo metes en el select con id provincias
                }
            },
            error : function (json) {
                console.log("Algo anda mal.")
            }
        });
}