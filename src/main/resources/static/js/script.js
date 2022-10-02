document.addEventListener('DOMContentLoaded', function() {
    initTooltips();
    initCharacterCounter();


    var password2 = document.getElementById("register_password2");
    password2.addEventListener('focusout', (event) => {
        validatePassword()
    });

    showErrorMessage();
});

function initTooltips() {
    var tooltips = document.querySelectorAll('.tooltipped');
    var tooltipsOptions = {
        'enterDelay': 500,
        'margin': 2
    };
    var tooltipsOptionsInstances = M.Tooltip.init(tooltips, tooltipsOptions);
}

function initCharacterCounter() {
    var textNeedCount = document.querySelectorAll('.register_values');
    M.CharacterCounter.init(textNeedCount);
}

function validateData(event) {
    var email = document.querySelector('#email').value;
    var password = document.querySelector('#password').value;

    if (!email || !password) {
        event.preventDefault();
        M.toast({html: "Email and password can't be empty !", displayLength: 3500});
    }
}

function showErrorMessage() {
    var errorInput = document.querySelector('#error-message');

    if (errorInput && errorInput.value) {
        M.toast({html: errorInput.value, displayLength: 3500});
        errorInput.value = null;
    }
}

function validatePassword() {
    var password1 = document.getElementById("register_password1");
    var password2 = document.getElementById("register_password2");
    var span = document.getElementById("register_password2_span");

    if (password1.value !== password2.value) {
        password2.dataset.error = "The two passwords aren't identical !";
        password2.classList.add("invalid");
        span.dataset.error = "The two passwords aren't identical !";
    } else {
        password2.classList.remove("invalid");
    }
}