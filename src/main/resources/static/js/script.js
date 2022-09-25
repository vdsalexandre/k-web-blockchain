document.addEventListener('DOMContentLoaded', function() {
    var elements = document.querySelectorAll('.tooltipped');
    var options = {
        'enterDelay': 500,
        'margin': 2
    }
    var instances = M.Tooltip.init(elements, options)
});