/**
 * Created with IntelliJ IDEA.
 * User: amundo
 * Date: 31/08/12
 * Time: 19:46
 * To change this template use File | Settings | File Templates.
 */

$(document).ready(function(){
    $("#global-masthead-container-btn").click(function(){
        var tab = $("#global-masthead-select").val();
        var url = "/frontier/template-service/templates/" + tab + "/header/main";
        $.ajax({
            type: 'GET',
            url: url,
            success: function(data, textStatus, jqXHR){
                $("#global-masthead-container").html(data);
            },
            dataType: "html"
        });
    });

    $("#global-masthead-container-clear").click(function(){
        $("#global-masthead-container").html("");
    });




    $("#local-masthead-container-btn").click(function(){
        var json = JSON.parse($("#local-masthead-json-configuration").val());
        $.ajax({
            type: 'POST',
            url: "/frontier/template-service/templates/header/specific",
            data: $("#local-masthead-json-configuration").val(),
            success: function(data, textStatus, jqXHR){
                $("#local-masthead-container").html(data);
            },
            dataType: "html"
        });
    });

    $("#local-masthead-container-clear").click(function(){
        $("#local-masthead-container").html("");
    });




    $("#global-footer-container-btn").click(function(){
        $.ajax({
            type: 'GET',
            url: "/frontier/template-service/templates/footer/main",
            success: function(data, textStatus, jqXHR){
                $("#global-footer-container").html(data);
            },
            dataType: "html"
        });
    });

    $("#global-footer-container-clear").click(function(){
        $("#global-footer-container").html("");
    });
});
