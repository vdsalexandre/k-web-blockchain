document.addEventListener('DOMContentLoaded', function() {
    initTooltips();
    initCharacterCounter();
    initFocusOutRegisterPassword();
    showErrorMessage();
    initModeCookie();
});

window.addEventListener('load', function() {
    showHtml();
});

function initTooltips() {
    let tooltips = document.querySelectorAll('.tooltipped');
    let tooltipsOptions = {
        'enterDelay': 500,
        'margin': 2
    };
    let tooltipsOptionsInstances = M.Tooltip.init(tooltips, tooltipsOptions);
}

function initCharacterCounter() {
    let textNeedCount = document.querySelectorAll('.register_values');
    M.CharacterCounter.init(textNeedCount);
}

function validateData(event) {
    let email = document.querySelector('#email').value;
    let password = document.querySelector('#password').value;

    if (!email || !password) {
        event.preventDefault();
        M.toast({html: "Email and password can't be empty !", displayLength: 3500});
    }
}

function showErrorMessage() {
    let errorInput = document.querySelector('#error-message');

    if (errorInput && errorInput.value) {
        M.toast({html: errorInput.value, displayLength: 3500});
        errorInput.value = null;
    }
}

function validatePassword() {
    let password1 = document.getElementById("register_password1");
    let password2 = document.getElementById("register_password2");
    let span = document.getElementById("register_password2_span");

    if (password1.value !== password2.value) {
        password2.dataset.error = "The two passwords aren't identical !";
        password2.classList.add("invalid");
        span.dataset.error = "The two passwords aren't identical !";
    } else {
        password2.classList.remove("invalid");
    }
}

function initFocusOutRegisterPassword() {
    let password2 = document.getElementById("register_password2");

    if (password2) {
        password2.addEventListener('focusout', (event) => {
            validatePassword()
        });
    }
}

function toggleSwitchMode() {
    let isDarkMode = document.body.classList.toggle("dark-mode");
    let iconMode = document.getElementById("icon-mode");

    let mode = isDarkMode ? 'dark' : 'light';
    let reverseMode = isDarkMode ? 'light' : 'dark';
    let addClass = isDarkMode ? 'blue-text' : 'orange-text';
    let removeClass = isDarkMode ? 'orange-text' : 'blue-text';

    document.cookie = "mode=" + mode + "; path=/blockchain;";
    iconMode.dataset.tooltip = "Show application in " + reverseMode + " mode"
    iconMode.innerHTML = reverseMode + "_mode"
    iconMode.classList.add(addClass)
    iconMode.classList.remove(removeClass)
    updateInputCss(mode);
}

function initModeCookie() {
    let cookie = document.cookie;

    if (!cookie) {
        document.cookie = "mode=dark; path=/blockchain;";
        updateInputCss("dark");
    } else {
        if (cookie === "mode=light") {
            toggleSwitchMode();
            updateInputCss("light");
        }
        else
            updateInputCss("dark");
    }
}

function showHtml() {
    document.getElementsByTagName("html")[0].style.visibility = "visible";
    let submitButton = document.querySelector("#login-submit-button");

    if (submitButton)
        submitButton.style.visibility = "visible";
}

function updateInputCss(mode) {
    let textColor = mode === 'dark' ? 'white' : 'black';

    let inputs = document.querySelectorAll('input');
    for (let i = 0; i < inputs.length; i++)
        inputs[i].style.color = textColor;
}