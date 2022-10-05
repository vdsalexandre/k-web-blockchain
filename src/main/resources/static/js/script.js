const DARK_MODE_COOKIE = "mode=dark; path=/blockchain;";
const LIGHT_MODE_COOKIE = "mode=light; path=/blockchain;";

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

    if (isDarkMode) {
        document.cookie = DARK_MODE_COOKIE;
        updateInputCss("dark");
    } else {
        document.cookie = LIGHT_MODE_COOKIE;
        updateInputCss("light");
    }
}

function initModeCookie() {
    let cookie = document.cookie;

    if (!cookie) {
        document.cookie = DARK_MODE_COOKIE;
        updateInputCss("dark");
    } else {
        if (cookie === "mode=light") {
            document.querySelector("#toggle-mode-input").click();
            updateInputCss("light");
        } else
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