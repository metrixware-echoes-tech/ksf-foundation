/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
function donner_focus(chp)
{
    document.getElementById(chp).focus();
    document.getElementById(chp).select();
}

function loginForman() {
    var login = document.getElementById("username").value;
    var password = document.getElementById("password").value;
    alert('login '+login );
//    var frameContent = document.getElementById('formanFrame').contentDocument.body.innerHTML;
//    var frameContent = document.getElementById('formanFrame').contentWindow.body.innerHTML;
//    $.get("https://https://passerelle:8443/users/login", function (data) {
////        
////        
//        alert(data);
//    });
function post_en_url(url, parametres) {
//Création dynamique du formulaire
    var form = document.createElement('form');
    form.setAttribute('method', 'POST');
    form.setAttribute('action', url);
//Ajout des paramètres sous forme de champs cachés
    for (var cle in parametres) {
        if (parametres.hasOwnProperty(cle)) {
            var champCache = document.createElement('input');
            champCache.setAttribute('type', 'hidden');
            champCache.setAttribute('name', cle);
            champCache.setAttribute('value', parametres[cle]);
            form.appendChild(champCache);
        }
    }
//Ajout du formulaire à la page et soumission du formulaire
    document.body.appendChild(form);
   var result = form.submit();
   alert(result);
}

var parametres = [{clé:"login[login]", valeur:login}, {clé:"login[password]", valeur:password}, {clé:"authenticity_token", valeur:"0U6GaVPli178sm2qeXb4KD8fVLn5KpTLtrR1ZJf2ghU="}];
    post_en_url("https://10.1.10.9/users/login",parametres)
//    monobjet = window.frames['formanFrame'].document;
//    alert(monobjet);
//    alert('dd');
}
;


//$(document).ready(function ()
//{
//    var idIframe = $('#formanFrame');
//    alert(idIframe.contents().find("div").html());
//});



