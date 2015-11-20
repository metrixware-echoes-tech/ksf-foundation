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

$('#loginForm').submit(function(ev) {
    ev.preventDefault();
    loginForman();
});

function loginForman() {

        var login = document.getElementById("username").value;
        var password = document.getElementById("password").value;

//    alert('login '+login );
        IE = window.ActiveXObject ? true : false;
        MOZ = window.sidebar ? true : false;

        try {
            if (IE) {
                var contenu_iframe = window.frames['iframe_article'].document.getElementsByName('authenticity_token');
            } else {
                var contenu_iframe = document.getElementById("formanFrame").contentWindow.document.getElementsByName('authenticity_token');

            }
            var keyAuthen = contenu_iframe[0].value;
        }
        catch (error) {
            console.log(error);
            submitLogin();
        }
//    alert(keyAuthen);


        var parametres = {
            "utf8": '✓',
            "login[login]": login,
            "login[password]": password,
            "authenticity_token": keyAuthen
        };
        post_en_url(pageData.foremanUrl + "/users/login", parametres);

}


function submitLogin()
{
    $('#loginForm').unbind().submit();
}


function post_en_url(url, parametres) {
//Création dynamique du formulaire
    var form = document.createElement('form');
    form.setAttribute('method', 'post');
    form.setAttribute('action', url);
    form.setAttribute('id', 'formForeman')
    form.setAttribute('accept-charset', 'UTF-8');
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

    var options = {
        success: submitLogin,
        error: submitLogin
    }

    $(form).ajaxSubmit(options);

}

;




