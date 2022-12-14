document.addEventListener('DOMContentLoaded', function() {
    initTooltips();
    initCharacterCounter();
    initFocusOutRegisterPassword();
    showErrorMessage();
    initModeCookie();
    handlePreloader('hide');
    callQrCodeUrl();
});

window.addEventListener('load', function() {
    showHtml();
});

function handlePreloader(action) {
    document.getElementById('preloader').style.display = (action === 'show') ? 'inline-block' : 'none';
}

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
    } else
        handlePreloader('show');
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
    let addClass = isDarkMode ? 'orange-text' : 'blue-text';
    let removeClass = isDarkMode ? 'blue-text' : 'orange-text';

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
    let loginSubmitButton = document.querySelector("#login-submit-button");
    let logoutSubmitButton = document.querySelector("#logout-submit-button");
    let createWalletSubmitButton = document.querySelector("#create-wallet-submit-button");

    if (loginSubmitButton) loginSubmitButton.style.visibility = "visible";
    if (logoutSubmitButton) logoutSubmitButton.style.visibility = "visible";
    if (createWalletSubmitButton) createWalletSubmitButton.style.visibility = "visible";
}

function updateInputCss(mode) {
    let textColor = mode === 'dark' ? 'white' : 'black';

    let inputs = document.querySelectorAll('input');
    for (let i = 0; i < inputs.length; i++)
        inputs[i].style.color = textColor;

    let userSpan = document.querySelectorAll('.highlight-span');
    if (userSpan) {
        for (let i = 0; i < userSpan.length; i++) {
            if (mode === 'dark')
                userSpan[i].style.color = "#A9EAFE";
            else
                userSpan[i].style.color = "#1E7FCB";
        }
    }
}

function callQrCodeUrl() {
    let walletId = document.getElementById("span-wallet-id");

    if (walletId) {
        const request = new XMLHttpRequest();
        request.open("GET", "/auth/wallet/" + walletId.innerHTML, true);
        request.responseType = "json";

        request.onload = (event) => {
            const json = request.response;
            if (json) {
                if (walletId) {
                    document.getElementById("qrcode").src = "data:image/png;base64," + json.qrCode;
                }
            }
        };

        request.send(null);
    }
}