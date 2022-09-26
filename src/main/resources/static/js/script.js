document.addEventListener('DOMContentLoaded', function() {
    var tooltips = document.querySelectorAll('.tooltipped');
    var tooltipsOptions = {
        'enterDelay': 500,
        'margin': 2
    };
    var tooltipsOptionsInstances = M.Tooltip.init(tooltips, tooltipsOptions);
});

function validateData(event) {
    var email = document.querySelector('#email').value;
    var password = document.querySelector('#password').value;

    if (!email || !password) {
        event.preventDefault();
        M.toast({html: "Email and password can't be empty !", displayLength: 2000});
    }
}