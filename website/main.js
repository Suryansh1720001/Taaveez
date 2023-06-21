const navToggle = document.querySelector(".nav__toggle"),
    navMenu = document.querySelector(".nav__menu"),
    changeTheme = document.querySelector(".change-theme"),
    navClose = document.querySelector(".nav__close"),
    navList = document.querySelectorAll(".nav__list"),
    allFaqs = document.querySelectorAll(".all_faqs"),
    themeBtnLight = document.getElementById("theme-button-light"),
    themeBtnDark = document.getElementById("theme-button-dark");

navToggle.addEventListener("click", () => {
    navMenu.classList.add("show-menu");
    navToggle.style.display = "none";
});

navClose.addEventListener("click", () => {
    navMenu.classList.remove("show-menu");
    navToggle.style.display = "block";
});

navList.forEach((item) => {
    item.addEventListener("click", () => {
        navMenu.classList.remove("show-menu");
        if (navigator.userAgent.match(/Android/i)
            || navigator.userAgent.match(/webOS/i)
            || navigator.userAgent.match(/iPhone/i)
            || navigator.userAgent.match(/iPad/i)
            || navigator.userAgent.match(/iPod/i)
            || navigator.userAgent.match(/BlackBerry/i)
            || navigator.userAgent.match(/Windows Phone/i)) {
            navToggle.style.display = "block";
        };
    });
});

allFaqs.forEach(item => {
    item.addEventListener("click", () => {
        item.classList.toggle("active");
    });
});

changeTheme.addEventListener("click", () => {
    document.body.classList.toggle("light-mode");
});