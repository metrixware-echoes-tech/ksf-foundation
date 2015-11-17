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
//    alert('login '+login );
    IE = window.ActiveXObject ? true : false;
    MOZ = window.sidebar ? true : false;
//    alert(pageData.foremanUrl)

//    if (IE)
//    {
//        var contenu_iframe = window.frames['iframe_article'].document.getElementsByName('authenticity_token');
//    } else {
//        var contenu_iframe = document.getElementById("formanFrame").contentWindow.document.getElementsByName('authenticity_token');
//
//    }
//
//    var keyAuthen = contenu_iframe[0].value;
//    alert(keyAuthen);
//    var frameContent = document.getElementById('formanFrame').contentDocument.body.innerHTML;

//    $.get("https://https://passerelle:8443/users/login", function (data) {
////        
////        
//        alert(data);
//    });


    var parametres = [{clé: "login[login]", valeur: login}, {clé: "login[password]", valeur: password}, {clé: "authenticity_token", valeur: keyAuthen}];
//    post_en_url(pageData.foremanUrl+"/users/login", parametres);
//    monobjet = window.frames['formanFrame'].document;
//    alert(monobjet);
//    alert('dd');
}

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

;


//$(document).ready(function ()
//{
//    var idIframe = $('#formanFrame');
//    alert(idIframe.contents().find("div").html());
//});



