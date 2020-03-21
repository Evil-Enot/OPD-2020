/*
 $('.nav-primary')
 // test the menu to see if all items fit horizontally
 .bind('testfit', function(){

 })

 // ...and update the nav on window events
 $(window).bind('load resize orientationchange', function(){
 $('.nav-primary').trigger('testfit');
 });
 */

$('header').find('i')
    .bind('click focus', function () {
        $(".nav-primary").toggleClass('expanded')
    });

$(".ms-slide a").hover(function(e) {
    $(this).parent().find(".ms-caption__title").toggleClass("hover")
});